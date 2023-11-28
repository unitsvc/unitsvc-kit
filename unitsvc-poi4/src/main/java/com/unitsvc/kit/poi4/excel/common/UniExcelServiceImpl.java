package com.unitsvc.kit.poi4.excel.common;

import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.google.gson.JsonObject;

import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.read.config.UniExcelStyleImportConfig;
import com.unitsvc.kit.poi4.excel.write.UniExcelExportTask;
import com.unitsvc.kit.poi4.excel.write.config.UniExcelStyleExportConfig;
import com.unitsvc.kit.poi4.excel.write.style.IUniExcelCustomStyle;
import com.unitsvc.kit.poi4.excel.write.style.defalut.UniExcelDefaultStyleImpl;
import com.unitsvc.kit.poi4.excel.read.utils.UniExcelCellTypeUtil;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述：通用导入导出接口实现
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/13 16:58
 */
public class UniExcelServiceImpl implements IUniExcelService {

    /**
     * 导出动态表头的工作簿，规定：表头占四行，其中第一、二行是表头名称且合并单元格，第三、四行为动态表头数据若包含字段分组则合并单元格。若进行导入，则从第四行开始读取。
     *
     * @param sheetName            工作表名称
     * @param titleName            标题名称
     * @param dynamicHeaderDTOList 动态表头集合
     * @param dynamicDataDTOList   动态数据集合
     * @return 返回文件字节
     */
    @Override
    public byte[] exportDynamicExcel(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, List<JsonObject> dynamicDataDTOList) {
        // 默认样式配置
        UniExcelStyleExportConfig uniExcelStyleExportConfig = new UniExcelStyleExportConfig();
        // 开启单元格保护
        uniExcelStyleExportConfig.setProtectSheet(true);
        // 按默认样式导出
        return exportDynamicExcel(sheetName, titleName, dynamicHeaderDTOList, dynamicDataDTOList, new UniExcelDefaultStyleImpl(), uniExcelStyleExportConfig);
    }

    /**
     * @param sheetName                 工作表名称
     * @param titleName                 标题名称
     * @param dynamicHeaderDTOList      动态表头集合
     * @param dynamicDataDTOList        动态数据集合
     * @param uniExcelStyleExportConfig 自定义表格配置
     * @return
     */
    @Override
    public byte[] exportDynamicExcel(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, List<JsonObject> dynamicDataDTOList, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        // 按默认样式导出
        return exportDynamicExcel(sheetName, titleName, dynamicHeaderDTOList, dynamicDataDTOList, new UniExcelDefaultStyleImpl(), uniExcelStyleExportConfig);
    }

    /**
     * 自定义样式，导出动态表头的工作簿，规定：默认表头占四行，其中第一、二行是表头名称且合并单元格，第三、四行为动态表头数据若包含字段分组则合并单元格。若进行导入，则默认从第四行开始读取。
     *
     * @param sheetName                 工作表名称
     * @param titleName                 标题名称
     * @param dynamicHeaderDTOList      动态表头集合
     * @param dynamicDataDTOList        动态数据集合
     * @param uniExcelCustomStyle       自定义表格样式
     * @param uniExcelStyleExportConfig 自定义表格配置
     * @return 返回文件字节
     */
    @Override
    public byte[] exportDynamicExcel(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, List<JsonObject> dynamicDataDTOList, IUniExcelCustomStyle uniExcelCustomStyle, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        return new UniExcelExportTask()
                .buildHeader(sheetName, titleName, dynamicHeaderDTOList, uniExcelCustomStyle, uniExcelStyleExportConfig)
                .addExcelData(sheetName, dynamicDataDTOList)
                .exportExcel();
    }

    /**
     * 导入动态Excel表格并返回解析后的实体类集合
     *
     * @param excelFileInputStream 待导入的Excel文件输入流
     * @param dynamicHeaderDTOList 动态表头集合
     * @return 返回表格JsonObject数据集合
     */
    @Override
    public List<JsonObject> importDynamicExcel(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList) {
        // 默认导入配置
        UniExcelStyleImportConfig uniExcelStyleImportConfig = new UniExcelStyleImportConfig();
        return this.importDynamicExcel(excelFileInputStream, dynamicHeaderDTOList, uniExcelStyleImportConfig);
    }

    /**
     * 自定义导入配置，导入动态Excel表格并返回解析后的实体类集合
     *
     * @param excelFileInputStream      待导入的Excel文件输入流
     * @param dynamicHeaderDTOList      动态表头集合
     * @param uniExcelStyleImportConfig 导入样式配置
     * @return 返回表格JsonObject数据集合
     */
    @Override
    public List<JsonObject> importDynamicExcel(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList, UniExcelStyleImportConfig uniExcelStyleImportConfig) {

        // ****************************************** 读取的配置项 *************************************

        // 定义表头所在行序号
        int readHeaderRowIndex = uniExcelStyleImportConfig.getReadHeaderRowIndex();

        // 定义是否仅仅只读可编辑字段
        boolean onlyReadEditField = uniExcelStyleImportConfig.isOnlyReadEditField();

        // *******************************************************************************************

        // 定义待导出的表头列字段属性
        List<UniExcelHeaderReq> headerDataList = new LinkedList<>();
        for (UniExcelHeaderReq dynamicHeaderDTO : dynamicHeaderDTOList) {
            List<UniExcelHeaderReq> groupList = dynamicHeaderDTO.getGroupList();
            if (null == groupList || groupList.isEmpty()) {
                headerDataList.add(dynamicHeaderDTO);
            } else {
                headerDataList.addAll(groupList);
            }
        }

        // 定义中文字段映射，nameCn->表头
        Map<String, UniExcelHeaderReq> fieldNameCnMappingMap = new HashMap<>(dynamicHeaderDTOList.size());
        for (UniExcelHeaderReq headerData : headerDataList) {
            fieldNameCnMappingMap.put(headerData.getNameCn(), headerData);
        }

        // 定义中文字段映射，nameEn->表头
        Map<String, UniExcelHeaderReq> fieldNameEnMappingMap = new HashMap<>(dynamicHeaderDTOList.size());
        for (UniExcelHeaderReq headerData : headerDataList) {
            fieldNameEnMappingMap.put(headerData.getNameEn(), headerData);
        }

        // 定义获取表头字段的序列，nameEn->index
        Map<String, Integer> dataNameEnIndexMappingMap = new HashMap<>(headerDataList.size());
        // 定义表格数据集合
        List<JsonObject> dataBeanList = new ArrayList<>();
        ExcelUtil.readBySax(excelFileInputStream, -1, new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
                Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowList);
                if (sheetIndex == 0) {
                    // 读取表头数据，并封装
                    if (rowIndex == readHeaderRowIndex) {
                        // 获取重复的表头字段，备注：表头重复将无法正确读取数据
                        List<String> repeatHeaderList = rowList.stream().filter(Objects::nonNull)
                                .map(Object::toString).collect(Collectors.toMap(key -> key, value -> 1, Integer::sum))
                                .entrySet()
                                .stream().filter(map -> map.getValue() > 1)
                                .map(Map.Entry::getKey).collect(Collectors.toList());
                        if (!repeatHeaderList.isEmpty()) {
                            throw new RuntimeException(String.format("导入的表格，第%s行表头包含重复字段：%s", readHeaderRowIndex + 1, repeatHeaderList));
                        }

                        // 解析表头信息
                        for (int index = rowList.size() - 1; index >= 0; index--) {
                            String nameCn = rowList.get(index).toString();
                            UniExcelHeaderReq dynamicHeaderDTO = fieldNameCnMappingMap.get(nameCn);
                            if (null != dynamicHeaderDTO) {
                                // 找出excel表头映射的nameEn
                                dataNameEnIndexMappingMap.put(dynamicHeaderDTO.getNameEn(), index);
                            }
                        }

                        // 无效表头
                        if (dataNameEnIndexMappingMap.size() == 0) {
                            throw new RuntimeException("导入的表格模板不正确");
                        }
                    }

                    // 读取表格数据，并处理
                    if (rowIndex > readHeaderRowIndex) {
                        // 定义待封装的表格行数据
                        JsonObject dataDomainObj = new JsonObject();
                        dataNameEnIndexMappingMap.forEach((nameEn, index) -> {
                            // 原始数据类型
                            Object valueObj = rowList.get(index);
                            // 获取表头字段定义
                            UniExcelHeaderReq dynamicHeaderDTO = fieldNameEnMappingMap.get(nameEn);
                            // 定义是否跳过该字段
                            boolean skipField = false;
                            if (onlyReadEditField) {
                                // 跳过不可编辑字段
                                if (null != dynamicHeaderDTO.getColumnEdit() && !dynamicHeaderDTO.getColumnEdit()) {
                                    skipField = true;
                                }
                            }
                            // 非跳过字段，数据类型处理
                            if (!skipField) {
                                // 定义为字符串类型
                                String valueStr = null;
                                if (null != valueObj) {
                                    // 获取表格字段值
                                    valueStr = String.valueOf(rowList.get(index));
                                }
                                // 解析并处理单元格数据
                                if (StringUtils.isNotEmpty(valueStr)) {
                                    try {
                                        switch (dynamicHeaderDTO.getFieldType()) {
                                            case FIELD_NUMBER:
                                                // 数字
                                            case FIELD_DOUBLE:
                                                // 小数
                                                valueStr = UniExcelCellTypeUtil.toDoubleStr(valueStr);
                                                break;
                                            case FIELD_INTEGER:
                                                // 整数
                                                valueStr = UniExcelCellTypeUtil.toIntegerStr(valueStr);
                                                break;
                                            case FIELD_DATE:
                                                // 时间
                                                valueStr = UniExcelCellTypeUtil.toDateStr(valueStr);
                                                break;
                                            case FIELD_BOOLEAN:
                                                // 布尔
                                                valueStr = UniExcelCellTypeUtil.toBooleanStr(valueStr);
                                            case FIELD_STRING:
                                                // 字符串
                                                break;
                                            default:
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException(String.format("表格第%s行，%s，输入的数据格式不正确。错误数据：%s", rowIndex + 1, dynamicHeaderDTO.getNameCn(), valueStr), e);
                                    }
                                    // 封装行数据属性，备注：此处要进行数据类型转换
                                    if (StringUtils.isNotEmpty(valueStr)) {
                                        switch (dynamicHeaderDTO.getFieldType()) {
                                            case FIELD_NUMBER:
                                                // 数字
                                            case FIELD_DOUBLE:
                                                // 小数
                                            case FIELD_INTEGER:
                                                // 构建字段属性
                                                dataDomainObj.addProperty(nameEn, new BigDecimal(valueStr));
                                                break;
                                            case FIELD_BOOLEAN:
                                                // 布尔
                                                // 构建字段属性
                                                dataDomainObj.addProperty(nameEn, Boolean.parseBoolean(valueStr));
                                                break;
                                            case FIELD_DATE:
                                                // 构建字段属性
                                            case FIELD_STRING:
                                                // 字符串
                                            default:
                                                // 构建字段属性
                                                dataDomainObj.addProperty(nameEn, valueStr);
                                        }
                                    }
                                }
                            }
                        });
                        dataBeanList.add(dataDomainObj);
                    }
                }
            }
        });

        // 释放内存
        fieldNameCnMappingMap.clear();
        fieldNameEnMappingMap.clear();
        dataNameEnIndexMappingMap.clear();

        return dataBeanList;
    }
}
