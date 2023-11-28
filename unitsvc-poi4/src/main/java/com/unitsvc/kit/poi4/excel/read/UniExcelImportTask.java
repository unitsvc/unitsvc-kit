package com.unitsvc.kit.poi4.excel.read;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.unitsvc.kit.poi4.excel.read.service.UniV4Excel2007ReadService;
import com.unitsvc.kit.poi4.excel.read.service.model.UniExcelImportResultResp;
import com.unitsvc.kit.poi4.excel.read.config.UniExcelStyleImportConfig;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum;
import com.unitsvc.kit.poi4.excel.common.exception.UniV4ExcelException;
import com.unitsvc.kit.poi4.excel.read.handler.UniV4XSSFSheetXMLHandler;
import com.unitsvc.kit.poi4.excel.read.service.model.TaskFieldModel;
import com.unitsvc.kit.poi4.excel.read.service.model.TaskHeaderMeta;
import com.unitsvc.kit.poi4.excel.read.service.model.TaskSheetMeta;
import com.unitsvc.kit.poi4.excel.read.utils.UniExcelCellTypeUtil;
import com.unitsvc.kit.poi4.excel.common.utils.UniExcelConvertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum.FIELD_BOOLEAN;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/7 16:36
 **/
public class UniExcelImportTask {

    /**
     * 自定义表头标识
     */
    private static final String CUSTOM_HEADER_INDEX_MARK = "__";

    /**
     * 导入动态Excel表格并返回解析后的实体类集合
     *
     * @param excelFileInputStream 待导入的Excel文件输入流
     * @param dynamicHeaderDTOList 动态表头集合
     * @return 返回表格JsonObject数据集合
     */
    public UniExcelImportResultResp importDynamicExcel(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList) {
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
    public UniExcelImportResultResp importDynamicExcel(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList, UniExcelStyleImportConfig uniExcelStyleImportConfig) {
        // 调用基础导入方法
        return baseImportDynamicExcelV2(excelFileInputStream, dynamicHeaderDTOList, uniExcelStyleImportConfig);
    }

    /**
     * 基础导入V1版本，导入动态Excel表格并返回解析后的实体类集合，即将被废弃
     *
     * @param excelFileInputStream      待导入的Excel文件输入流
     * @param dynamicHeaderDTOList      动态表头集合
     * @param uniExcelStyleImportConfig 导入样式配置
     * @return 返回表格JsonObject数据集合
     */
    @Deprecated
    private UniExcelImportResultResp baseImportDynamicExcelV1(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList, UniExcelStyleImportConfig uniExcelStyleImportConfig) {
        // ****************************************** 读取的配置项 *************************************

        // 定义表头所在行序号
        int readHeaderRowIndex = uniExcelStyleImportConfig.getReadHeaderRowIndex();

        // 定义是否仅仅只读可编辑字段
        boolean onlyReadEditField = uniExcelStyleImportConfig.isOnlyReadEditField();

        // 检查可编辑字段是否都为空
        boolean checkEditColumnNameCnIsAllEmpty = uniExcelStyleImportConfig.isCheckEditColumnNameCnIsAllEmpty();
        // 联合主键名称集合
        List<String> checkExitsPkNameCns = uniExcelStyleImportConfig.getCheckExitsPkNameCns();
        // 全局字段常量集合
        List<UniExcelStyleImportConfig.ColumnConstant> globalColumnConstants = uniExcelStyleImportConfig.getGlobalColumnConstants();
        // 是否添加唯一索引
        Boolean addExtraIndex = uniExcelStyleImportConfig.getAddExtraIndex();
        // 索引字段名称
        String extraIndexName = uniExcelStyleImportConfig.getExtraIndexName();

        // -------------------------------------------------------------------------------------------
        // 是否添加工作表索引
        Boolean addSheetIndex = uniExcelStyleImportConfig.getAddSheetIndex();
        // 是否读取自定义字段
        Boolean readCustomHeaderData = uniExcelStyleImportConfig.getReadCustomHeaderData();

        // 获取任务字段元信息
        List<TaskFieldModel> taskFieldsMetas = Optional.ofNullable(uniExcelStyleImportConfig.getTaskFields()).orElse(Collections.emptyList());
        // 获取任务字段信息
        Map<String, TaskFieldModel> taskFieldNameCnMapping = taskFieldsMetas.stream().collect(Collectors.toMap(TaskFieldModel::getNameCn, Function.identity(), (n1, n2) -> n1));

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
        // 定义读取的可编辑列集合
        List<String> readEditNameCnList = new ArrayList<>();

        // -------------------------------------------------------------------------------------------------------------
        // 定义额外的属性字段列表
        Set<String> extraDataAttrSet = new HashSet<>();
        // 定义工作表元信息
        TaskSheetMeta taskSheetMeta = new TaskSheetMeta();
        // -------------------------------------------------------------------------------------------------------------
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
                            throw new RuntimeException(String.format("%s列重复，请检查修正后重新导入", repeatHeaderList));
                        }

                        // 获取所有导入的字段中文名称
                        List<String> allHeaderNameCnList = rowList.stream()
                                .filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
                        // 校验是否包含联合主键字段
                        if (CollectionUtils.isNotEmpty(checkExitsPkNameCns)) {
                            List<String> lackPks = new ArrayList<>();
                            for (String pkNameCn : checkExitsPkNameCns) {
                                if (!allHeaderNameCnList.contains(pkNameCn)) {
                                    lackPks.add(pkNameCn);
                                }
                            }
                            if (CollectionUtils.isNotEmpty(lackPks)) {
                                throw new RuntimeException(String.format("缺失%s主键列，请检查修正后重新导入", lackPks));
                            }
                        }

                        // 获取可编辑列中文名称
                        List<String> editNameCnList = headerDataList.stream().filter(v -> (null != v.getColumnEdit() && v.getColumnEdit()))
                                .map(UniExcelHeaderReq::getNameCn).collect(Collectors.toList());
                        // 校验可编辑字段是否都为空
                        if (checkEditColumnNameCnIsAllEmpty) {
                            // 判断可编辑字段与导入字段是否存在交集
                            boolean noCommon = Collections.disjoint(editNameCnList, allHeaderNameCnList);
                            if (noCommon) {
                                throw new RuntimeException("匹配不到可导入列，请检查导入字段名是否和线上对应字段一致");
                            }
                        }
                        // 获取编辑字段列
                        Collection<String> unionEditFields = CollectionUtils.intersection(editNameCnList, allHeaderNameCnList);
                        readEditNameCnList.addAll(unionEditFields);

                        // 解析表头信息
                        for (int index = 0; index < rowList.size(); index++) {
                            String nameCn = rowList.get(index).toString();
                            UniExcelHeaderReq dynamicHeaderDTO = fieldNameCnMappingMap.get(nameCn);
                            if (null != dynamicHeaderDTO) {
                                // 找出excel表头映射的nameEn
                                dataNameEnIndexMappingMap.put(dynamicHeaderDTO.getNameEn(), index);
                            } else {
                                TaskFieldModel taskField = taskFieldNameCnMapping.get(nameCn);
                                // ------------------- 处理自定义字段 --------------------------
                                if (null != readCustomHeaderData && readCustomHeaderData) {
                                    if (null != taskField) {
                                        String nameEn = taskField.getNameEn();
                                        dataNameEnIndexMappingMap.put(nameEn, index);
                                        fieldNameEnMappingMap.put(nameEn, UniExcelConvertUtil.buildDynamicHeaderFromTaskField(taskField));
                                    } else {
                                        String nameEn = String.format("__%s__", index);
                                        dataNameEnIndexMappingMap.put(nameEn, index);
                                        fieldNameEnMappingMap.put(nameEn, new UniExcelHeaderReq(nameCn, nameEn));
                                    }

                                }
                            }
                        }

                        // ------------------------------------ 封装表头元数据信息 ----------------------------------------
                        List<TaskHeaderMeta> taskHeaderMetas = buildTaskHeaderMeta(rowList, fieldNameCnMappingMap, readCustomHeaderData, taskFieldNameCnMapping);
                        taskSheetMeta.setSheetIndex(sheetIndex);
                        taskSheetMeta.setHeaderMetas(taskHeaderMetas);
                        // ---------------------------------------------------------------------------------------------

                        // 无效表头
                        if (dataNameEnIndexMappingMap.size() == 0) {
                            throw new RuntimeException("导入的表格列名不正确");
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

                        // 补充全局字段常量
                        if (CollectionUtils.isNotEmpty(globalColumnConstants)) {
                            for (UniExcelStyleImportConfig.ColumnConstant globalColumnConstant : globalColumnConstants) {
                                if (null == globalColumnConstant.getNameEn() || null == globalColumnConstant.getValue()) {
                                    throw new RuntimeException("通用导入配置错误，请检查全局字段常量是否有误");
                                }
                                String globalNameEn = globalColumnConstant.getNameEn();
                                dataDomainObj.addProperty(globalNameEn, globalColumnConstant.getValue());
                                // 记录额外字段
                                extraDataAttrSet.add(globalNameEn);
                            }
                            // 添加唯一索引，可配
                            if (null != addExtraIndex && addExtraIndex) {
                                String indexName = "_id";
                                if (StringUtils.isNotEmpty(extraIndexName)) {
                                    indexName = extraIndexName;
                                }
                                dataDomainObj.addProperty(indexName, IdUtil.getSnowflakeNextIdStr());
                                // 记录额外字段
                                extraDataAttrSet.add(indexName);
                            }
                        }
                        // 若添加工作表索引
                        if (null != addSheetIndex && addSheetIndex) {
                            String sheetIndexNameEn = "__sheetIndex";
                            dataDomainObj.addProperty(sheetIndexNameEn, sheetIndex);
                            // 记录额外字段
                            extraDataAttrSet.add(sheetIndexNameEn);
                        }
                        dataBeanList.add(dataDomainObj);
                    }
                }
            }

        });

        // 释放内存
        fieldNameCnMappingMap.clear();
        fieldNameEnMappingMap.clear();
        dataNameEnIndexMappingMap.clear();

        // 封装返回值
        UniExcelImportResultResp uniExcelImportResultResp = new UniExcelImportResultResp();
        uniExcelImportResultResp.setDataList(dataBeanList);
        uniExcelImportResultResp.setReadEditNameCnList(readEditNameCnList);
        uniExcelImportResultResp.setSheetIndex(0);
        uniExcelImportResultResp.setExtraDataAttrs(Lists.newArrayList(extraDataAttrSet));
        uniExcelImportResultResp.setTaskSheetMeta(taskSheetMeta);
        return uniExcelImportResultResp;
    }

    /**
     * 基础导入V2版本，导入动态Excel表格并返回解析后的实体类集合，修改中
     *
     * @param excelFileInputStream      待导入的Excel文件输入流
     * @param dynamicHeaderDTOList      动态表头集合
     * @param uniExcelStyleImportConfig 导入样式配置
     * @return 返回表格JsonObject数据集合
     */
    private UniExcelImportResultResp baseImportDynamicExcelV2(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList, UniExcelStyleImportConfig uniExcelStyleImportConfig) {
        // ****************************************** 读取的配置项 *************************************

        // 定义表头所在行序号
        int readHeaderRowIndex = uniExcelStyleImportConfig.getReadHeaderRowIndex();

        // 定义是否仅仅只读可编辑字段
        boolean onlyReadEditField = uniExcelStyleImportConfig.isOnlyReadEditField();

        // 检查可编辑字段是否都为空
        boolean checkEditColumnNameCnIsAllEmpty = uniExcelStyleImportConfig.isCheckEditColumnNameCnIsAllEmpty();
        // 联合主键名称集合
        List<String> checkExitsPkNameCns = uniExcelStyleImportConfig.getCheckExitsPkNameCns();
        // 全局字段常量集合
        List<UniExcelStyleImportConfig.ColumnConstant> globalColumnConstants = uniExcelStyleImportConfig.getGlobalColumnConstants();
        // 是否添加唯一索引
        Boolean addExtraIndex = uniExcelStyleImportConfig.getAddExtraIndex();
        // 索引字段名称
        String extraIndexName = uniExcelStyleImportConfig.getExtraIndexName();

        // -------------------------------------------------------------------------------------------
        // 是否添加工作表索引
        Boolean addSheetIndex = uniExcelStyleImportConfig.getAddSheetIndex();
        // 是否读取自定义字段
        Boolean readCustomHeaderData = uniExcelStyleImportConfig.getReadCustomHeaderData();

        // 获取任务字段元信息
        List<TaskFieldModel> taskFieldsMetas = Optional.ofNullable(uniExcelStyleImportConfig.getTaskFields()).orElse(Collections.emptyList());
        // 获取任务字段信息
        Map<String, TaskFieldModel> taskFieldNameCnMapping = taskFieldsMetas.stream().collect(Collectors.toMap(TaskFieldModel::getNameCn, Function.identity(), (n1, n2) -> n1));

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

        // 定义获取表头字段的序列，index->nameEn
        Map<Integer, String> dataIndexNameEnMappingMap = new HashMap<>(headerDataList.size());
        // 定义表格数据集合
        List<JsonObject> dataBeanList = new ArrayList<>();
        // 定义读取的可编辑列集合
        List<String> readEditNameCnList = new ArrayList<>();

        // -------------------------------------------------------------------------------------------------------------
        // 定义额外的属性字段列表
        Set<String> extraDataAttrSet = new HashSet<>();
        // 定义工作表元信息
        TaskSheetMeta taskSheetMeta = new TaskSheetMeta();
        // -------------------------------------------------------------------------------------------------------------
        UniV4Excel2007ReadService uniExcel2007Read = new UniV4Excel2007ReadService(new UniV4XSSFSheetXMLHandler.SheetContentsHandler() {

            /**
             * 返回数据模型
             */
            private JsonObject dataDomainObj;

            /**
             * 判断是否表头数据行
             */
            private boolean isHeaderRow;

            /**
             * 表头字段映射，序号->表头中文名称
             */
            private Map<Integer, String> initHeaderMapping;

            @Override
            public void startRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName) {
                // 判断是否表头数据
                isHeaderRow = (rowNum == readHeaderRowIndex);
                if (isHeaderRow) {
                    // 初始化表头数据
                    initHeaderMapping = new HashMap<>(32);
                }
                if (rowNum > readHeaderRowIndex) {
                    // 初始化行数据
                    dataDomainObj = new JsonObject();
                }
            }

            @Override
            public void endRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName) {
                // ---------------------------------- 校验表头数据 ----------------------------------------
                if (isHeaderRow) {
                    List<String> rowList = Lists.newArrayList(initHeaderMapping.values());
                    // 获取重复的表头字段，备注：表头重复将无法正确读取数据
                    List<String> repeatHeaderList = rowList.stream().filter(Objects::nonNull)
                            .map(Object::toString).collect(Collectors.toMap(key -> key, value -> 1, Integer::sum))
                            .entrySet()
                            .stream().filter(map -> map.getValue() > 1)
                            .map(Map.Entry::getKey).collect(Collectors.toList());
                    if (!repeatHeaderList.isEmpty()) {
                        throw new RuntimeException(String.format("%s列重复，请检查修正后重新导入", repeatHeaderList));
                    }

                    // 获取所有导入的字段中文名称
                    List<String> allHeaderNameCnList = rowList.stream()
                            .filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
                    // 校验是否包含联合主键字段
                    if (CollectionUtils.isNotEmpty(checkExitsPkNameCns)) {
                        List<String> lackPks = new ArrayList<>();
                        for (String pkNameCn : checkExitsPkNameCns) {
                            if (!allHeaderNameCnList.contains(pkNameCn)) {
                                lackPks.add(pkNameCn);
                            }
                        }
                        if (CollectionUtils.isNotEmpty(lackPks)) {
                            throw new RuntimeException(String.format("缺失%s主键列，请检查修正后重新导入", lackPks));
                        }
                    }

                    // 获取可编辑列中文名称
                    List<String> editNameCnList = headerDataList.stream().filter(v -> (null != v.getColumnEdit() && v.getColumnEdit()))
                            .map(UniExcelHeaderReq::getNameCn).collect(Collectors.toList());
                    // 校验可编辑字段是否都为空
                    if (checkEditColumnNameCnIsAllEmpty) {
                        // 判断可编辑字段与导入字段是否存在交集
                        boolean noCommon = Collections.disjoint(editNameCnList, allHeaderNameCnList);
                        if (noCommon) {
                            throw new RuntimeException("匹配不到可导入列，请检查导入字段名是否和线上对应字段一致");
                        }
                    }
                    // 获取编辑字段列
                    Collection<String> unionEditFields = CollectionUtils.intersection(editNameCnList, allHeaderNameCnList);
                    readEditNameCnList.addAll(unionEditFields);

                    // 解析表头信息
                    for (Map.Entry<Integer, String> headerEntry : initHeaderMapping.entrySet()) {
                        Integer index = headerEntry.getKey();
                        String nameCn = headerEntry.getValue();
                        UniExcelHeaderReq dynamicHeaderDTO = fieldNameCnMappingMap.get(nameCn);
                        if (null != dynamicHeaderDTO) {
                            // 找出excel表头映射的nameEn
                            dataIndexNameEnMappingMap.put(index, dynamicHeaderDTO.getNameEn());
                        } else {
                            TaskFieldModel taskField = taskFieldNameCnMapping.get(nameCn);
                            // ------------------- 处理自定义字段 --------------------------
                            if (null != readCustomHeaderData && readCustomHeaderData) {
                                if (null != taskField) {
                                    String nameEn = taskField.getNameEn();
                                    dataIndexNameEnMappingMap.put(index, nameEn);
                                    fieldNameEnMappingMap.put(nameEn, UniExcelConvertUtil.buildDynamicHeaderFromTaskField(taskField));
                                } else {
                                    String nameEn = String.format("__%s__", index);
                                    dataIndexNameEnMappingMap.put(index, nameEn);
                                    fieldNameEnMappingMap.put(nameEn, new UniExcelHeaderReq(nameCn, nameEn));
                                }

                            }
                        }
                    }
                    // 无效表头
                    if (dataIndexNameEnMappingMap.size() == 0) {
                        throw new RuntimeException("导入的表格列名不正确");
                    }
                }

                // -----------------------------------------------------------------------------------------------------
                // 添加到业务数据
                if (null != dataDomainObj) {
                    // 补充全局字段常量
                    if (CollectionUtils.isNotEmpty(globalColumnConstants)) {
                        for (UniExcelStyleImportConfig.ColumnConstant globalColumnConstant : globalColumnConstants) {
                            if (null == globalColumnConstant.getNameEn() || null == globalColumnConstant.getValue()) {
                                throw new RuntimeException("通用导入配置错误，请检查全局字段常量是否有误");
                            }
                            String globalNameEn = globalColumnConstant.getNameEn();
                            dataDomainObj.addProperty(globalNameEn, globalColumnConstant.getValue());
                            // 记录额外字段
                            extraDataAttrSet.add(globalNameEn);
                        }
                        // 添加唯一索引，可配
                        if (null != addExtraIndex && addExtraIndex) {
                            String indexName = "_id";
                            if (StringUtils.isNotEmpty(extraIndexName)) {
                                indexName = extraIndexName;
                            }
                            dataDomainObj.addProperty(indexName, IdUtil.getSnowflakeNextIdStr());
                            // 记录额外字段
                            extraDataAttrSet.add(indexName);
                        }
                    }
                    // 若添加工作表索引
                    if (null != addSheetIndex && addSheetIndex) {
                        String sheetIndexNameEn = "__sheetIndex";
                        dataDomainObj.addProperty(sheetIndexNameEn, customExtSheetIndex);
                        // 记录额外字段
                        extraDataAttrSet.add(sheetIndexNameEn);
                    }

                    dataBeanList.add(dataDomainObj);
                }
            }

            @Override
            public void cell(String cellReference, String formattedValue, XSSFComment comment, Integer customExtRowIndex, Integer customExtColIndex, String customExtColTitle, UniV4XSSFSheetXMLHandler.xssfDataType customExtXssfDataType, XSSFCellStyle customExtStyle, String customExtOriginValue, String customExtFormula, Integer customExtSheetIndex, String customExtSheetName) {
                Console.log("[{}] [{}] [{}] [{}] [{} {} {}]", cellReference, customExtRowIndex, customExtColIndex, customExtColTitle, customExtXssfDataType, customExtOriginValue, formattedValue);
                if (null != comment) {
                    // 去除批注逻辑
                    return;
                }
                // --------------------------------------- 封装表头数据，用于业务处理 ---------------------------------------
                if (isHeaderRow && null != initHeaderMapping) {
                    initHeaderMapping.put(customExtColIndex, formattedValue);
                }
                // 处理业务数据
                if (null != dataDomainObj) {
                    // ----------------------------------------- 处理表格数据 ---------------------------------------------
                    String nameEn = dataIndexNameEnMappingMap.get(customExtColIndex);
                    // 获取表头字段定义
                    UniExcelHeaderReq dynamicHeaderDTO = fieldNameEnMappingMap.get(nameEn);
                    // 处理无表头数据
                    if (StringUtils.isEmpty(nameEn)) {
                        if (null != readCustomHeaderData && readCustomHeaderData) {
                            nameEn = String.format("__%s__", customExtColIndex);
                            UniExcelHeaderReq uniExcelHeaderReq = new UniExcelHeaderReq("", nameEn);
                            // 获取单元格类型
                            UniExcelCellFieldTypeEnum fieldTypeEnum = toExcelCellType(customExtXssfDataType, customExtStyle);
                            // 设置单元格类型
                            uniExcelHeaderReq.setFieldType(fieldTypeEnum);
                            fieldNameEnMappingMap.put(nameEn, uniExcelHeaderReq);
                            // 补充表头缺失的空白列头，该表头不存在，但是该列存在数据
                            initHeaderMapping.put(customExtColIndex, "");
                            dynamicHeaderDTO = uniExcelHeaderReq;
                        } else {
                            return;
                        }
                    } else {
                        // 重新评估自定义表头的单元格类型，约定：所有列单元格类型保持一致
                        if (nameEn.startsWith(CUSTOM_HEADER_INDEX_MARK) && nameEn.endsWith(CUSTOM_HEADER_INDEX_MARK)) {
                            // 获取单元格类型
                            UniExcelCellFieldTypeEnum fieldTypeEnum = toExcelCellType(customExtXssfDataType, customExtStyle);
                            // 设置单元格类型
                            dynamicHeaderDTO.setFieldType(fieldTypeEnum);
                        }
                    }

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
                        String valueStr = customExtOriginValue;
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
                                    case FIELD_FORMULA:
                                        // 公式
                                        valueStr = customExtFormula;
                                    default:
                                }
                            } catch (Exception e) {
                                if (e instanceof UniV4ExcelException) {
                                    throw new RuntimeException(String.format("表格第%s行，%s，%s。错误数据：%s", customExtRowIndex + 1, dynamicHeaderDTO.getNameCn(), e.getMessage(), valueStr), e);
                                } else {
                                    throw new RuntimeException(String.format("表格第%s行，%s，输入的数据格式不正确。错误数据：%s", customExtRowIndex + 1, dynamicHeaderDTO.getNameCn(), valueStr), e);
                                }
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

                }
            }

            @Override
            public void endSheet(Integer customExtSheetIndex, String customExtSheetName) {
                // ------------------------------------ 封装表头元数据信息 ----------------------------------------
                List<TaskHeaderMeta> taskHeaderMetas = buildTaskHeaderMetaV2(initHeaderMapping, fieldNameCnMappingMap, fieldNameEnMappingMap, readCustomHeaderData, taskFieldNameCnMapping);
                taskSheetMeta.setSheetIndex(customExtSheetIndex);
                taskSheetMeta.setHeaderMetas(taskHeaderMetas);
                taskSheetMeta.setSheetName(customExtSheetName);

                // ---------------------------------------------------------------------------------------------
                // 清空表头数据
                if (null != initHeaderMapping) {
                    initHeaderMapping.clear();
                    initHeaderMapping = null;
                }
            }
        });

        try {
            // 仅读取首个工作表
            uniExcel2007Read.read(excelFileInputStream, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 释放内存
        fieldNameCnMappingMap.clear();
        fieldNameEnMappingMap.clear();
        dataIndexNameEnMappingMap.clear();

        // 封装返回值
        UniExcelImportResultResp uniExcelImportResultResp = new UniExcelImportResultResp();
        uniExcelImportResultResp.setDataList(dataBeanList);
        uniExcelImportResultResp.setReadEditNameCnList(readEditNameCnList);
        uniExcelImportResultResp.setSheetIndex(0);
        uniExcelImportResultResp.setExtraDataAttrs(Lists.newArrayList(extraDataAttrSet));
        uniExcelImportResultResp.setTaskSheetMeta(taskSheetMeta);
        return uniExcelImportResultResp;
    }

    /**
     * 转换单元格类型
     *
     * @param customExtXssfDataType 单元格原始类型
     * @param customExtStyle        单元格样式
     * @return
     */
    private UniExcelCellFieldTypeEnum toExcelCellType(UniV4XSSFSheetXMLHandler.xssfDataType customExtXssfDataType, XSSFCellStyle customExtStyle) {
        UniExcelCellFieldTypeEnum fieldTypeEnum = null;
        switch (customExtXssfDataType) {
            case FORMULA:
                // 公式
                fieldTypeEnum = UniExcelCellFieldTypeEnum.FIELD_FORMULA;
                break;
            case BOOLEAN:
                // 布尔
                fieldTypeEnum = FIELD_BOOLEAN;
                break;
            case NUMBER:
                // "数字"，修复 #2895，数字类型解析错误
                short formatIndex = customExtStyle.getDataFormat();
                String formatString = customExtStyle.getDataFormatString();
                if (null == formatString) {
                    formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
                }
                // 校验是否时间格式化
                boolean checkIsDataFormat = DateUtil.isADateFormat(formatIndex, formatString);
                if (checkIsDataFormat) {
                    fieldTypeEnum = UniExcelCellFieldTypeEnum.FIELD_DATE;
                } else {
                    fieldTypeEnum = UniExcelCellFieldTypeEnum.FIELD_NUMBER;
                }
                break;
            default:
                fieldTypeEnum = UniExcelCellFieldTypeEnum.FIELD_STRING;
        }
        return fieldTypeEnum;
    }

    /**
     * 构造表头元数据
     *
     * @param rowList               表头数据
     * @param fieldNameCnMappingMap 字段映射
     * @return
     */
    private List<TaskHeaderMeta> buildTaskHeaderMeta(List<?> rowList, Map<String, UniExcelHeaderReq> fieldNameCnMappingMap, Boolean readCustomHeaderData, Map<String, TaskFieldModel> taskFieldNameCnMapping) {
        // 定义表头元数据
        List<TaskHeaderMeta> taskHeaderMetaList = new ArrayList<>();
        for (int index = 0; index < rowList.size(); index++) {
            String nameCn = rowList.get(index).toString();
            UniExcelHeaderReq dynamicHeaderDTO = fieldNameCnMappingMap.get(nameCn);
            String nameEn = "";
            TaskHeaderMeta.HeaderAttrEnum headerAttr;
            TaskHeaderMeta.DataPermEnum dataPerm = TaskHeaderMeta.DataPermEnum.SHOW;
            TaskHeaderMeta.DataCellEnum dataCell = TaskHeaderMeta.DataCellEnum.STRING;
            if (null != dynamicHeaderDTO) {
                // 定义表头属性
                headerAttr = TaskHeaderMeta.HeaderAttrEnum.SYSTEM_CONFIG;
                // 获取字段nameEn
                nameEn = dynamicHeaderDTO.getNameEn();
                // 获取字段类型
                UniExcelCellFieldTypeEnum fieldType = dynamicHeaderDTO.getFieldType();
                // 获取当前单元格类型
                dataCell = this.toCellTypeByFieldType(fieldType);
                // 判断数据权限
                Boolean columnEdit = dynamicHeaderDTO.getColumnEdit();
                if (null != columnEdit && columnEdit) {
                    dataPerm = TaskHeaderMeta.DataPermEnum.EDIT;
                }
            } else {
                // 跳过自定义字段表头元信息
                if (null == readCustomHeaderData || !readCustomHeaderData) {
                    continue;
                }
                TaskFieldModel taskField = taskFieldNameCnMapping.get(nameCn);
                if (null == taskField) {
                    headerAttr = TaskHeaderMeta.HeaderAttrEnum.PEOPLE_CUSTOM;
                    nameEn = String.format("__%s__", index);
                } else {
                    headerAttr = TaskHeaderMeta.HeaderAttrEnum.SYSTEM_CONFIG;
                    nameEn = taskField.getNameEn();
                    UniExcelCellFieldTypeEnum fieldType = UniExcelConvertUtil.transformFieldType(taskField.getNameEn(), taskField.getFieldFormat(), taskField.getType());
                    // 获取当前单元格类型
                    dataCell = this.toCellTypeByFieldType(fieldType);
                }
            }
            // 定义表头元数据
            TaskHeaderMeta taskHeaderMeta = new TaskHeaderMeta();
            taskHeaderMeta.setHeaderIndex(index);
            taskHeaderMeta.setNameCn(nameCn);
            taskHeaderMeta.setNameEn(nameEn);
            taskHeaderMeta.setHeaderAttr(headerAttr);
            taskHeaderMeta.setDataPerm(dataPerm);
            taskHeaderMeta.setDataCell(dataCell);
            //添加数据
            taskHeaderMetaList.add(taskHeaderMeta);
        }
        return taskHeaderMetaList;
    }

    /**
     * 构造表头元数据
     *
     * @param headerRowMap           表头数据
     * @param fieldNameCnMappingMap  字段中文映射
     * @param fieldNameEnMappingMap  字段英文映射
     * @param readCustomHeaderData   是否读取自定义表头
     * @param taskFieldNameCnMapping 字段映射
     * @return
     */
    private List<TaskHeaderMeta> buildTaskHeaderMetaV2(Map<Integer, String> headerRowMap, Map<String, UniExcelHeaderReq> fieldNameCnMappingMap, Map<String, UniExcelHeaderReq> fieldNameEnMappingMap, Boolean readCustomHeaderData, Map<String, TaskFieldModel> taskFieldNameCnMapping) {
        // 定义表头元数据
        List<TaskHeaderMeta> taskHeaderMetaList = new ArrayList<>();
        for (Map.Entry<Integer, String> headerEntry : headerRowMap.entrySet()) {
            Integer index = headerEntry.getKey();
            String nameCn = headerEntry.getValue();
            UniExcelHeaderReq dynamicHeaderDTO = fieldNameCnMappingMap.get(nameCn);
            String nameEn = "";
            TaskHeaderMeta.HeaderAttrEnum headerAttr;
            TaskHeaderMeta.DataPermEnum dataPerm = TaskHeaderMeta.DataPermEnum.SHOW;
            TaskHeaderMeta.DataCellEnum dataCell = TaskHeaderMeta.DataCellEnum.STRING;
            if (null != dynamicHeaderDTO) {
                // 定义表头属性
                headerAttr = TaskHeaderMeta.HeaderAttrEnum.SYSTEM_CONFIG;
                // 获取字段nameEn
                nameEn = dynamicHeaderDTO.getNameEn();
                // 获取字段类型
                UniExcelCellFieldTypeEnum fieldType = dynamicHeaderDTO.getFieldType();
                // 获取当前单元格类型
                dataCell = this.toCellTypeByFieldType(fieldType);
                // 判断数据权限
                Boolean columnEdit = dynamicHeaderDTO.getColumnEdit();
                if (null != columnEdit && columnEdit) {
                    dataPerm = TaskHeaderMeta.DataPermEnum.EDIT;
                }
            } else {
                // 跳过自定义字段表头元信息
                if (null == readCustomHeaderData || !readCustomHeaderData) {
                    continue;
                }
                TaskFieldModel taskField = taskFieldNameCnMapping.get(nameCn);
                if (null == taskField) {
                    headerAttr = TaskHeaderMeta.HeaderAttrEnum.PEOPLE_CUSTOM;
                    nameEn = String.format("__%s__", index);
                    // 获取表头
                    UniExcelHeaderReq uniExcelHeaderReq = fieldNameEnMappingMap.get(nameEn);
                    dataCell = this.toCellTypeByFieldType(uniExcelHeaderReq.getFieldType());
                } else {
                    headerAttr = TaskHeaderMeta.HeaderAttrEnum.SYSTEM_CONFIG;
                    nameEn = taskField.getNameEn();
                    UniExcelCellFieldTypeEnum fieldType = UniExcelConvertUtil.transformFieldType(taskField.getNameEn(), taskField.getFieldFormat(), taskField.getType());
                    // 获取当前单元格类型
                    dataCell = this.toCellTypeByFieldType(fieldType);
                }
            }
            // 定义表头元数据
            TaskHeaderMeta taskHeaderMeta = new TaskHeaderMeta();
            taskHeaderMeta.setHeaderIndex(index);
            taskHeaderMeta.setNameCn(nameCn);
            taskHeaderMeta.setNameEn(nameEn);
            taskHeaderMeta.setHeaderAttr(headerAttr);
            taskHeaderMeta.setDataPerm(dataPerm);
            taskHeaderMeta.setDataCell(dataCell);
            //添加数据
            taskHeaderMetaList.add(taskHeaderMeta);
        }
        return taskHeaderMetaList;
    }

    /**
     * 字段类型转单元格类型
     *
     * @param fieldType 字段类型枚举
     * @return
     */
    private TaskHeaderMeta.DataCellEnum toCellTypeByFieldType(UniExcelCellFieldTypeEnum fieldType) {
        TaskHeaderMeta.DataCellEnum dataCell = TaskHeaderMeta.DataCellEnum.STRING;
        switch (fieldType) {
            case FIELD_STRING:
                // 字符串
                dataCell = TaskHeaderMeta.DataCellEnum.STRING;
                break;
            case FIELD_NUMBER:
                // 数字
                dataCell = TaskHeaderMeta.DataCellEnum.NUMBER;
                break;
            case FIELD_INTEGER:
                // 整数
                dataCell = TaskHeaderMeta.DataCellEnum.INTEGER;
                break;
            case FIELD_DOUBLE:
                // 小数
                dataCell = TaskHeaderMeta.DataCellEnum.DOUBLE;
                break;
            case FIELD_BOOLEAN:
                // 布尔
                dataCell = TaskHeaderMeta.DataCellEnum.BOOLEAN;
                break;
            case FIELD_DATE:
                // 时间
                dataCell = TaskHeaderMeta.DataCellEnum.DATE;
                break;
            case FIELD_FORMULA:
                // 公式
                dataCell = TaskHeaderMeta.DataCellEnum.FORMULA;
                break;
            default:
        }
        return dataCell;
    }
}
