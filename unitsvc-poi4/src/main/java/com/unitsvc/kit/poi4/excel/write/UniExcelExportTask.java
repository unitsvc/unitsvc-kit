package com.unitsvc.kit.poi4.excel.write;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.gson.reflect.TypeToken;
import com.unitsvc.kit.poi4.excel.common.UniExcelBaseService;
import com.unitsvc.kit.poi4.excel.write.style.IUniExcelCustomStyle;
import com.unitsvc.kit.poi4.excel.read.handler.IUniExportDataHandler;
import com.unitsvc.kit.poi4.excel.write.config.UniExcelStyleExportConfig;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldFixedEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellAttrLabelEnum;
import com.unitsvc.kit.poi4.excel.write.style.defalut.UniExcelDefaultStyleImpl;
import com.unitsvc.kit.poi4.excel.write.utils.UniExcelExportUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能描述：通用EXCEL导出任务类，支持复杂业务场景下导出。
 *
 * <pre>
 * 备注：仅支持对多sheet进行多线程操作。不支持单sheet下多线程操作。
 * 使用方式：
 * 方式一，常规导出表格，分批写入磁盘避免内存溢出，不支持合并单元格。
 * new UniExcelExportTask().buildHeader().addExcelData().mergeExcelColumn().exportExcel();
 * 方式二，合并列单元格，需要基于内存操作。
 * new UniExcelExportTask(-1).buildHeader().addExcelData().mergeExcelColumn().exportExcel();
 * </pre>
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2022/11/9 12:43
 **/
public class UniExcelExportTask extends UniExcelBaseService {

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 工作簿
     */
    private SXSSFWorkbook workbook = null;

    /**
     * 工作表容器，sheetName名称->sheet工作表
     */
    private Map<String, SXSSFSheet> sheetMap = new ConcurrentHashMap<>();

    /**
     * 数据起始行容器，sheetName名称->数据起始行，基于数据动态改变
     */
    private Map<String, Integer> currentStartDataRowIndexMap = new ConcurrentHashMap<>();

    /**
     * 数据起始行容器，sheetName名称->数据起始行，数据初始化起始行
     */
    private Map<String, Integer> initStartDataRowIndexMap = new ConcurrentHashMap<>();

    /**
     * 自定义导出配置容器，sheetName名称->自定义导出样式
     */
    private Map<String, UniExcelStyleExportConfig> uniExcelStyleExportConfigMap = new ConcurrentHashMap<>();

    /**
     * 动态表头容器，sheetName名称->dynamicDataHeaderList动态表头集合
     */
    private Map<String, List<UniExcelHeaderReq>> dynamicDataHeaderListMap = new ConcurrentHashMap<>();

    /**
     * 单元格样式容器，sheetName名称->cellAllTypeMap单元格样式集合
     */
    private Map<String, Map<UniExcelHeaderReq, UniCellDataCellStyle>> cellTypeMap = new ConcurrentHashMap<>();

    /**
     * 统计导出时间
     */
    private final Long startTime = System.currentTimeMillis();

    /**
     * 统计写入数据量
     */
    private final AtomicLong dataSizeSum = new AtomicLong();

    /**
     * 标识位，是否允许列单元格合并
     */
    private Boolean allowMergeColumn = false;

    /**
     * 构造函数
     *
     * <pre>
     * 说明：默认方法，建议使用，能够有效避免内存溢出，默认2000条刷盘。
     * </pre>
     */
    public UniExcelExportTask() {
        this.workbook = new SXSSFWorkbook(2000);
    }

    /**
     * 构造函数
     * <pre>
     * 说明：
     * 1.参数-1，基于内存操作，可用于列单元格合并，new UniExcelExportTask(-1);
     * 2.其余参数用于动态调整写入磁盘文件条数。
     * </pre>
     *
     * @param rowAccessWindowSize 在刷新之前保留在内存中的行数
     */
    public UniExcelExportTask(int rowAccessWindowSize) {
        if (rowAccessWindowSize == -1) {
            this.allowMergeColumn = true;
        }
        this.workbook = new SXSSFWorkbook(rowAccessWindowSize);
    }

    /**
     * 第一步，构建工作表表头
     * <p>
     * 备注：使用默认样式配置，默认开启单元格保护。
     * </p>
     *
     * @param sheetName            工作表名称
     * @param titleName            标题名称
     * @param dynamicHeaderDTOList 动态表头
     *                             <p>
     *                             备注：该动态表头集合仅仅是副本集，不会被内部方法修改。
     *                             </p>
     * @return
     */
    public UniExcelExportTask buildHeader(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList) {
        // 默认样式配置
        UniExcelStyleExportConfig uniExcelStyleExportConfig = new UniExcelStyleExportConfig();
        // 开启单元格保护
        uniExcelStyleExportConfig.setProtectSheet(true);
        return this.buildHeader(sheetName, titleName, dynamicHeaderDTOList, new UniExcelDefaultStyleImpl(), uniExcelStyleExportConfig);
    }

    /**
     * 第一步，构建工作表表头
     * <p>
     * 备注：使用默认样式配置，自定义导出配置。高频使用。
     * </p>
     *
     * @param sheetName            工作表名称
     * @param titleName            标题名称
     * @param dynamicHeaderDTOList 动态表头
     *                             <p>
     *                             备注：该动态表头集合仅仅是副本集，不会被内部方法修改。
     *                             </p>
     * @return
     */
    public UniExcelExportTask buildHeader(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        return this.buildHeader(sheetName, titleName, dynamicHeaderDTOList, new UniExcelDefaultStyleImpl(), uniExcelStyleExportConfig);
    }

    /**
     * 第一步，构建工作表表头
     * <p>
     * 备注：使用自定义样式、自定义导出配置
     * </P>
     *
     * @param sheetName                 工作表名称
     * @param titleName                 标题名称
     * @param dynamicHeaderDTOList      动态表头
     *                                  <p>
     *                                  备注：该动态表头集合仅仅是副本集，不会被内部方法修改。
     *                                  </p>
     * @param uniExcelCustomStyle       自定义样式
     * @param uniExcelStyleExportConfig 导出配置
     * @return
     */
    public UniExcelExportTask buildHeader(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, IUniExcelCustomStyle uniExcelCustomStyle, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        // ----------------------------------------------------- 初始化工作表 ---------------------------------------------
        // 创建工作表
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        // 禁止重复创建相同工作簿的表头，否则会出现异常
        if (this.sheetMap.containsKey(sheetName)) {
            throw new RuntimeException(String.format("请勿重复创建相同的工作表【%s】表头", sheetName));
        }
        // ----------------------------------------------------- 初始化配置 ---------------------------------------------
        // 初始化工作簿容器
        this.sheetMap.put(sheetName, sheet);
        // 初始化导出配置
        this.uniExcelStyleExportConfigMap.put(sheetName, uniExcelStyleExportConfig);
        // -------------------------------------------------------------------------------------------------------------
        // 是否开启单元格保护
        boolean isProtectSheet = uniExcelStyleExportConfig.isProtectSheet();
        // 获取默认全局列宽
        int defaultColumnWidth = uniExcelStyleExportConfig.getDefaultColumnWidth();
        // 控制是否开启单元格保护
        if (isProtectSheet) {
            // 开启单元格保护
            sheet.protectSheet("");
            // 备注：当启用单元格保护后，以下设置仅仅在该列单元格为可编辑条件下才生效。
            // 启用自动过滤
            sheet.lockAutoFilter(false);
            // 启用排序
            sheet.lockSort(false);
        }
        // 设置默认列宽
        sheet.setDefaultColumnWidth(defaultColumnWidth);
        // ------------------------------------------------------------------------------------------------------------------------------------

        // 动态表头字段集合深拷贝，备注：此处目的是防止多sheet复用表头导致表头数据缺失。
        List<UniExcelHeaderReq> copyDynamicHeaderDTOList = GSON.fromJson(GSON.toJson(dynamicHeaderDTOList), new TypeToken<List<UniExcelHeaderReq>>() {
        }.getType());

        // 表头字段排序
        List<UniExcelHeaderReq> sortDynamicHeaderList = this.sortHeaderDTOList(copyDynamicHeaderDTOList);

        // ************************************************** 配置项 **********************************************************
        // 是否展示标题
        boolean isShowTitle = uniExcelStyleExportConfig.isShowTitle();
        int titleSpanRowSize = 0;
        if (isShowTitle) {
            // 获取标题行配置
            int titleHeaderSpanSizeConfig = uniExcelStyleExportConfig.getTitleHeaderSpanSize();
            // 配置标题占几行
            titleSpanRowSize = titleHeaderSpanSizeConfig <= 1 ? 2 : titleHeaderSpanSizeConfig;
        }
        // 获取最小左侧固定列数
        int minFixedLeftColumnSize = uniExcelStyleExportConfig.getMinFixedLeftColumnSize();
        // 获取是否动态固定左侧第一列
        boolean dynamicFixedLeftColumn = uniExcelStyleExportConfig.isDynamicFixedLeftColumn();
        // 是否展示两行合并单元格的表头
        boolean headerOnlyShowOwn = uniExcelStyleExportConfig.isHeaderOnlyShowOwn();
        // ************************************************************************************************************************
        // ---------------------------------- 判断是否多行表头，用于判断是否需要单元格合并 --------------------------------------
        // 定义第一行表头序号
        int startHeaderRowIndex = (titleSpanRowSize - 1) + 1;
        // 定义最后行表头序号
        int endHeaderRowIndex = this.calcMaxDataHeaderDeep(sortDynamicHeaderList, startHeaderRowIndex);
        // 定义最大表头行数
        int maxHeaderRowSize = !headerOnlyShowOwn ? (endHeaderRowIndex + 1) : (startHeaderRowIndex + 1);
        // 定义数据列起始行数据
        int startDataRowIndex = (maxHeaderRowSize - 1) + 1;
        // ------------------------------------------- 初始化数据列起始行 -------------------------------------------
        // 初始化工作表数据所在行
        this.currentStartDataRowIndexMap.put(sheetName, startDataRowIndex);
        // 初始化工作表数据所在行
        this.initStartDataRowIndexMap.put(sheetName, startDataRowIndex);
        // ------------------------------------------------------------------------------------------------------

        // 动态固定左侧列或仅固定第一列
        if (minFixedLeftColumnSize > 0 || dynamicFixedLeftColumn) {
            // 定义左侧固定列数，规定：
            int fixedLeftColumnSize = 0;
            for (UniExcelHeaderReq dynamicHeaderDTO : sortDynamicHeaderList) {
                // 统计表头字段左固定数目
                if (null != dynamicHeaderDTO.getFixed() && dynamicHeaderDTO.getFixed().equals(UniExcelCellFieldFixedEnum.LEFT)) {
                    fixedLeftColumnSize++;
                } else {
                    break;
                }
            }
            // 取左侧固定列最大值
            fixedLeftColumnSize = Math.max(fixedLeftColumnSize, minFixedLeftColumnSize);
            // 固定表头及第一列，警告：windows版本与mac版本用法存在差异，windows版本不支持多行
            sheet.createFreezePane(fixedLeftColumnSize, maxHeaderRowSize, fixedLeftColumnSize, maxHeaderRowSize);
        } else {
            // 仅固定表头，警告：windows版本与mac版本用法存在差异，windows版本不支持多行
            sheet.createFreezePane(0, maxHeaderRowSize, 0, maxHeaderRowSize);
        }

        // ************************************************ 创建并合并表头，占第二、三行 ************************************************
        // 定义待导出的列字段属性
        List<UniExcelHeaderReq> realDynamicDataHeaderList = this.findDataHeaderList(sortDynamicHeaderList);
        // ------------------------------------------- 初始化动态表头 ----------------------------------------------
        // 初始化动态表头容器
        this.dynamicDataHeaderListMap.put(sheetName, realDynamicDataHeaderList);
        // ------------------------------------------------------------------------------------------------------

        // 创建数据列可编辑表头样式
        CellStyle dataHeaderEditStyle = workbook.createCellStyle();
        // 创建数据列可编辑表头字体
        Font dataHeaderEditFont = workbook.createFont();
        // 设置自定义数据列可编辑表头样式
        uniExcelCustomStyle.setDataHeaderEditStyle(dataHeaderEditFont, dataHeaderEditStyle);

        // 创建数据列可编辑表头样式
        CellStyle dataHeaderReadonlyStyle = workbook.createCellStyle();
        // 创建数据列可编辑表头字体
        Font dataHeaderReadonlyFont = workbook.createFont();
        // 设置自定义数据列可编辑表头样式
        uniExcelCustomStyle.setDataHeaderReadonlyStyle(dataHeaderReadonlyFont, dataHeaderReadonlyStyle);

        // 创建分组列表头样式
        CellStyle groupHeaderStyle = workbook.createCellStyle();
        // 创建分组列表头字体
        Font groupHeaderFont = workbook.createFont();
        // 设置自定义分组列表头样式
        uniExcelCustomStyle.setGroupHeaderStyle(groupHeaderFont, groupHeaderStyle);
        if (headerOnlyShowOwn) {
            // --------------------------------- 仅仅导出数据列 -------------------------------------
            // 创建一级表头
            Row startHeaderRow = sheet.createRow(startHeaderRowIndex);
            // 定义一级表头序号
            int ownHeaderColIndex = 0;
            for (UniExcelHeaderReq dynamicHeaderDTO : realDynamicDataHeaderList) {
                // 创建第一列单元格
                Cell firstHeaderCell = startHeaderRow.createCell(ownHeaderColIndex++);
                // 设置一级表头名称
                firstHeaderCell.setCellValue(dynamicHeaderDTO.getNameCn());
                // 设置编辑、不可编辑样式
                if (null != dynamicHeaderDTO.getColumnEdit() && dynamicHeaderDTO.getColumnEdit()) {
                    // 补充数据列表头样式，可编辑列表头
                    firstHeaderCell.setCellStyle(dataHeaderEditStyle);
                } else {
                    // 不可编辑列表头
                    firstHeaderCell.setCellStyle(dataHeaderReadonlyStyle);
                }
            }
        } else {
            // ----------------------------------- 创建动态表头行 -------------------------------------------------
            Map<Integer, Row> headerRowMapping = new HashMap<>(endHeaderRowIndex - startHeaderRowIndex + 1);
            for (int index = startHeaderRowIndex; index <= endHeaderRowIndex; index++) {
                headerRowMapping.put(index, sheet.createRow(index));
            }
            // ----------------------------------- 处理嵌套分组表头 -------------------------------------------------
            // 递归处理多级嵌套表头分组错乱问题。
            this.toDynamicGroupDataHeader(
                    headerRowMapping,
                    sortDynamicHeaderList, sheet, dataHeaderEditStyle, dataHeaderReadonlyStyle, groupHeaderStyle,
                    startHeaderRowIndex, maxHeaderRowSize, 0
            );
        }

        // ************************************************ 创建标题行，占第一行 ************************************************
        if (isShowTitle) {
            // 创建标题样式
            CellStyle titleStyle = workbook.createCellStyle();
            // 创建标题字体样式
            Font titleFont = workbook.createFont();
            // 设置自定义标题样式
            uniExcelCustomStyle.setTitleHeaderStyle(titleFont, titleStyle);
            for (int titleRowIndex = 0; titleRowIndex < titleSpanRowSize; titleRowIndex++) {
                // 创建标题行
                Row titleRow = sheet.createRow(titleRowIndex);
                // 补充第一行单元格样式，否则合并后会丢失样式
                for (int titleColIndex = 0; titleColIndex < realDynamicDataHeaderList.size(); titleColIndex++) {
                    // 创建第一行指定列单元格
                    Cell titleCell = titleRow.createCell(titleColIndex);
                    if (titleRowIndex == 0 && titleColIndex == 0) {
                        // 设置第一行标题内容
                        titleCell.setCellValue(titleName);
                    }
                    // 设置单元格样式
                    titleCell.setCellStyle(titleStyle);
                }
            }
            // 合并标题所在行：示例：A1-B2所在对角线单元格
            sheet.addMergedRegion(new CellRangeAddress(0, titleSpanRowSize - 1, 0, realDynamicDataHeaderList.size() - 1));
        }

        // ******************************************* 处理实际表头列及二级表头所在列特殊样式 *******************************************
        for (int colIndex = 0; colIndex < realDynamicDataHeaderList.size(); colIndex++) {
            // 获取动态表头
            UniExcelHeaderReq dynamicHeaderDTO = realDynamicDataHeaderList.get(colIndex);
            // 获取展示标签
            List<UniExcelCellAttrLabelEnum> showLabels = Optional.ofNullable(dynamicHeaderDTO.getShowLabels()).orElse(Collections.emptyList());

            // 过滤出宽度相关标签，处理所在列单元格宽度，优先级高于自定义设置值
            UniExcelCellAttrLabelEnum widthLabel = showLabels.stream().filter(v -> v.name().startsWith("WIDTH")).findFirst().orElse(null);
            if (CollectionUtils.isNotEmpty(showLabels) && null != widthLabel) {
                // 获取单元格宽度
                String format = widthLabel.getFormat();
                // 指定列宽度
                sheet.setColumnWidth(colIndex, 256 * Integer.parseInt(format));
            } else {
                // **** 处理指定列宽度 ****
                Integer columnWidth = dynamicHeaderDTO.getColumnWidth();
                if (null != columnWidth) {
                    // 指定列宽度
                    sheet.setColumnWidth(colIndex, columnWidth);
                }
            }

            // ***** 处理指定列是否隐藏 *****
            if (null != dynamicHeaderDTO.getColumnHidden() && dynamicHeaderDTO.getColumnHidden()) {
                // 指定列隐藏
                sheet.setColumnHidden((short) colIndex, true);
            }

            // 过滤出布尔相关标签，处理布尔值标签的下拉框选项，优先级高于自定义设置值
            UniExcelCellAttrLabelEnum boolLabel = showLabels.stream().filter(v -> v.name().startsWith("BOOL")).findFirst().orElse(null);
            if (CollectionUtils.isNotEmpty(showLabels) && showLabels.contains(UniExcelCellAttrLabelEnum.OPTIONS_BOOL) && null != boolLabel) {
                String format = boolLabel.getFormat();
                String[] options = format.split("\\|");
                // 备注：此方法具有局限性，只能设置有限值
                this.setCellDownSelectValueListByRestrict(
                        sheet, dynamicHeaderDTO.getNameCn(),
                        startDataRowIndex, 1048576 - startDataRowIndex,
                        colIndex, colIndex,
                        options
                );
            } else {
                // ***** 设置下拉值 *****
                if (null != dynamicHeaderDTO.getSelectValueList() && !dynamicHeaderDTO.getSelectValueList().isEmpty()) {
                    // 备注：此方法具有局限性，只能设置有限值
                    this.setCellDownSelectValueListByRestrict(
                            sheet, dynamicHeaderDTO.getNameCn(),
                            startDataRowIndex, 1048576 - startDataRowIndex,
                            colIndex, colIndex,
                            dynamicHeaderDTO.getSelectValueList().toArray(new String[0])
                    );
                }
            }

        }
        // --------------------------------------------------- 初始化动态表头 -----------------------------------------------------
        // 获取所有列单元格样式
        Map<UniExcelHeaderReq, UniCellDataCellStyle> cellAllTypeMap = this.createAllColumnCellType(workbook, realDynamicDataHeaderList, uniExcelCustomStyle, uniExcelStyleExportConfig);
        // 初始化单元格样式
        this.cellTypeMap.put(sheetName, cellAllTypeMap);
        // ----------------------------------------------------------------------------------------------------------------------
        return this;
    }

    /**
     * 构建分组表头，合并单元格操作
     *
     * <pre>
     * 说明：暂不兼容嵌套表头单元格合并，处理中
     * </pre>
     *
     * @param sortDynamicHeaderList   分组表头数据
     * @param sheet                   工作表
     * @param dataHeaderEditStyle     数据编辑样式
     * @param dataHeaderReadonlyStyle 数据只读样式
     * @param groupHeaderStyle        表头分组样式
     * @param startHeaderRowIndex     起始行序号
     * @param endHeaderRowIndex       最后行序号
     * @param curHeaderColIndex       起始列序号
     */
    private void toDynamicGroupDataHeader(Map<Integer, Row> headerRowMapping, List<UniExcelHeaderReq> sortDynamicHeaderList, SXSSFSheet sheet, CellStyle dataHeaderEditStyle, CellStyle dataHeaderReadonlyStyle, CellStyle groupHeaderStyle,
                                          int startHeaderRowIndex, int endHeaderRowIndex, int curHeaderColIndex) {
        // 定义起始行序号
        int startHeaderColIndex = curHeaderColIndex;
        // -------------------------------- 导出带分组的数据列 --------------------------------
        for (UniExcelHeaderReq dynamicHeaderDTO : sortDynamicHeaderList) {
            // 创建表头列单元格
            Cell startHeaderCell = headerRowMapping.get(startHeaderRowIndex).createCell(startHeaderColIndex);
            if (CollectionUtils.isEmpty(dynamicHeaderDTO.getGroupList())) {
                // 设置表头名称
                startHeaderCell.setCellValue(dynamicHeaderDTO.getNameCn());
                // ---------------------------- 合并一级、最后一级表头 ----------------------------
                for (int curRowIndex = startHeaderRowIndex; curRowIndex < endHeaderRowIndex; curRowIndex++) {
                    Row curRow = headerRowMapping.get(curRowIndex);
                    // 创建下级表头列单元格
                    Cell emptyCell = curRow.createCell(startHeaderColIndex);
                    emptyCell.setCellValue(dynamicHeaderDTO.getNameCn());
                    // ----------------- 第一级后与最后一级（包含）之间表头单元格样式填充 -----------------
                    if (null != dynamicHeaderDTO.getColumnEdit() && dynamicHeaderDTO.getColumnEdit()) {
                        // 补充数据列表头样式，可编辑表头
                        emptyCell.setCellStyle(dataHeaderEditStyle);
                    } else {
                        // 只读表头样式
                        emptyCell.setCellStyle(dataHeaderReadonlyStyle);
                    }
                }
                if (startHeaderRowIndex < endHeaderRowIndex - 1) {
                    // TODO: 2023/8/18 此处嵌套合并存在问题
                    // 列合并，示例：相同列单元格：A1-C1
                    sheet.addMergedRegion(new CellRangeAddress(startHeaderRowIndex, endHeaderRowIndex - 1, startHeaderColIndex, startHeaderColIndex));
                }
                // 列起始序号加1
                startHeaderColIndex = startHeaderColIndex + 1;
            } else {
                // 下个分组起始行序号
                int curNextHeaderColIndex = startHeaderColIndex;
                // ------------------------------------- 行单元格合并 -----------------------------------------
                // 计算分组表头行跨度，用于合并单元格
                int childHeaderSpan = this.calcGroupDataHeaderSpan(dynamicHeaderDTO);
                // 获取起始行
                Row startHeaderRow = headerRowMapping.get(startHeaderRowIndex);
                for (int colIndex = curNextHeaderColIndex; colIndex < curNextHeaderColIndex + childHeaderSpan; colIndex++) {
                    // 创建下级表头列单元格
                    Cell emptyCell = startHeaderRow.createCell(colIndex);
                    emptyCell.setCellValue(dynamicHeaderDTO.getNameCn());
                    // ----------------- 第一级后与最后一级（包含）之间表头单元格样式填充 -----------------
                    if (null != dynamicHeaderDTO.getColumnEdit() && dynamicHeaderDTO.getColumnEdit()) {
                        // 补充数据列表头样式，可编辑表头
                        emptyCell.setCellStyle(dataHeaderEditStyle);
                    } else {
                        // 只读表头样式
                        emptyCell.setCellStyle(dataHeaderReadonlyStyle);
                    }
                }
                // 合并行单元格
                sheet.addMergedRegion(new CellRangeAddress(startHeaderRowIndex, startHeaderRowIndex, curNextHeaderColIndex, curNextHeaderColIndex + childHeaderSpan - 1));
                // 核心：处理嵌套表头
                List<UniExcelHeaderReq> groupList = dynamicHeaderDTO.getGroupList();
                this.toDynamicGroupDataHeader(
                        headerRowMapping, groupList, sheet, dataHeaderEditStyle, dataHeaderReadonlyStyle, groupHeaderStyle,
                        startHeaderRowIndex + 1, endHeaderRowIndex, startHeaderColIndex
                );
                // 子分组单元格起始列
                startHeaderColIndex = curNextHeaderColIndex + childHeaderSpan;
            }
            //  ------------------------------ 起始表头单元格样式填充 -------------------------------
            if (null != dynamicHeaderDTO.getColumnEdit() && dynamicHeaderDTO.getColumnEdit()) {
                // 补充数据列表头样式，可编辑列表头样式
                startHeaderCell.setCellStyle(dataHeaderEditStyle);
            } else {
                // 不可编辑列表头样式
                startHeaderCell.setCellStyle(dataHeaderReadonlyStyle);
            }
        }
    }

    /**
     * 第二步，添加工作表数据
     * <p>
     * 警告：此处是循环分页查询，然后写数据，以时间换取空间，防止内存溢出。
     * </p>
     *
     * @param sheetName 工作表名称
     * @param handler   数据处理器
     * @return
     */
    public UniExcelExportTask addExcelDataByPage(String sheetName, IUniExportDataHandler handler) {
        // 写入分页数据
        handler.writeData(sheetName, this);
        return this;
    }

    /**
     * 第二步，添加工作表数据
     *
     * @param sheetName          工作表名称
     * @param dynamicDataDTOList 动态数据集合
     * @return
     */
    public UniExcelExportTask addExcelData(String sheetName, List<JsonObject> dynamicDataDTOList) {
        // 累计数据量
        dataSizeSum.addAndGet(dynamicDataDTOList.size());

        // 获取工作表
        SXSSFSheet sheet = this.sheetMap.get(sheetName);
        if (null == sheet) {
            throw new RuntimeException(String.format("请先完成工作表【%s】表头初始化", sheetName));
        }
        // 获取处理后的表头字段
        List<UniExcelHeaderReq> dynamicDataHeaderList = this.dynamicDataHeaderListMap.get(sheetName);
        // 获取数据起始行
        Integer startDataRowIndex = currentStartDataRowIndexMap.get(sheetName);

        // 获取导出配置
        UniExcelStyleExportConfig uniExcelStyleExportConfig = this.uniExcelStyleExportConfigMap.get(sheetName);
        // 获取行是否可编辑配置
        String rowEditEnConfig = uniExcelStyleExportConfig.getRowEditFieldEn();
        // 定义行是否可编辑
        String rowEditEn = StringUtils.isEmpty(rowEditEnConfig) ? "hint" : rowEditEnConfig;

        // 获取所有单元格样式
        Map<UniExcelHeaderReq, UniCellDataCellStyle> cellAllTypeMap = this.cellTypeMap.get(sheetName);

        // 遍历处理表格行数据
        if (!CollectionUtils.isEmpty(dynamicDataDTOList) && dynamicDataDTOList.size() > 0) {
            // 处理表格数据
            for (JsonObject jsonData : dynamicDataDTOList) {
                // 定义数据列序号
                int dataColumnIndex = 0;
                // 创建数据行，警告：多线程下，非线程安全
                Row dataRow = sheet.createRow(startDataRowIndex++);
                // 填充单元格数据
                for (UniExcelHeaderReq dynamicHeaderDTO : dynamicDataHeaderList) {

                    // 获取当前单元格样式
                    UniCellDataCellStyle uniCellDataCellStyle = cellAllTypeMap.get(dynamicHeaderDTO);

                    // 创建单元格
                    Cell dataCell = dataRow.createCell(dataColumnIndex++);
                    String nameEn = dynamicHeaderDTO.getNameEn();
                    // 获取指定数据
                    JsonElement dataValueJson = jsonData.get(nameEn);
                    // 行是否可编辑
                    JsonElement rowEditValueJson = jsonData.get(rowEditEn);

                    // ************************************************ 单元格赋值 ************************************************

                    // 备注：排除字段值为数组对象数据
                    if (null != dataValueJson && !dataValueJson.isJsonNull() && !dataValueJson.isJsonArray() && StringUtils.isNotEmpty(dataValueJson.getAsString())) {
                        // 获取列数据
                        String dataValue = dataValueJson.getAsString();
                        // 设置单元格值
                        uniCellDataCellStyle.setCellValue(dataCell, dataValue, dynamicHeaderDTO, uniExcelStyleExportConfig);
                    } else {
                        // 取默认字段逻辑
                        List<String> defaultTakeValueIds = Optional.ofNullable(dynamicHeaderDTO.getDefaultTakeValueIds()).orElse(Lists.newArrayList());
                        if (CollectionUtils.isNotEmpty(dynamicHeaderDTO.getDefaultTakeValueIds())) {
                            for (String defaultTakeValueId : defaultTakeValueIds) {
                                JsonElement defaultValueJson = jsonData.get(defaultTakeValueId);
                                if ((null != defaultValueJson && !defaultValueJson.isJsonNull() && !defaultValueJson.isJsonArray() && StringUtils.isNotEmpty(defaultValueJson.getAsString()))) {
                                    // 获取列数据
                                    String defaultDataValue = defaultValueJson.getAsString();
                                    // 设置单元格值
                                    uniCellDataCellStyle.setCellValue(dataCell, defaultDataValue, dynamicHeaderDTO, uniExcelStyleExportConfig);
                                    break;
                                } else {
                                    // 无数据则设置为空
                                    dataCell.setBlank();
                                }
                            }
                        } else {
                            // 无数据则设置为空
                            dataCell.setBlank();
                        }
                    }
                    // ************************************* 处理单元格是否可编辑样式 *************************************

                    // 定义列是否允许被编辑，默认不允许编辑
                    boolean dataColEdit = false;
                    // 定义行是否允许被编辑，默认允许编辑
                    boolean dataRowEdit = true;

                    // 获取行是否可编辑
                    if (null != rowEditValueJson && !rowEditValueJson.isJsonNull()) {
                        // 行数据是否允许被编辑
                        dataRowEdit = rowEditValueJson.getAsBoolean();
                    }

                    // 获取列是否可编辑
                    if (null != dynamicHeaderDTO.getColumnEdit()) {
                        // 列数据是否允许被编辑
                        dataColEdit = dynamicHeaderDTO.getColumnEdit();
                    }

                    if (dataColEdit && dataRowEdit) {
                        if (uniExcelStyleExportConfig.isEnableSummaryMarkStyle() && jsonData.has(uniExcelStyleExportConfig.getSummaryMark())) {
                            // 单元格允许编辑，如果包含汇总行标识，汇总行加粗显示
                            dataCell.setCellStyle(uniCellDataCellStyle.getCellDataAllowEditSummaryMarkStyle());
                        } else {
                            // 单元格允许编辑
                            dataCell.setCellStyle(uniCellDataCellStyle.getCellDataAllowEditStyle());
                        }
                    } else {
                        if (uniExcelStyleExportConfig.isEnableSummaryMarkStyle() && jsonData.has(uniExcelStyleExportConfig.getSummaryMark())) {
                            // 单元格只读，如果包含汇总行标识，汇总行加粗显示
                            dataCell.setCellStyle(uniCellDataCellStyle.getCellDataReadonlySummaryMarkStyle());
                        } else {
                            // 单元格只读
                            dataCell.setCellStyle(uniCellDataCellStyle.getCellDataReadonlyStyle());
                        }
                    }
                }
            }

            // ------------------------------------- 更新数据起始行索引 -------------------------------------
            this.currentStartDataRowIndexMap.put(sheetName, startDataRowIndex);
            // ------------------------------------------------------------------------------------------

            // *********************************************** 数据处理完毕 ***********************************************
        }
        return this;
    }

    /**
     * 第二步【后续，特殊可选】，合并单元格
     * <pre>
     *  备注：2023-08-16 18:17 新增
     *  说明：
     *  1.new UniExcelExportTask(-1).buildHeader().addExcelData().mergeExcelColumn().exportExcel()。
     *  2.初始化参数必须设置-1，才允许合并单元格操作。
     * </pre>
     *
     * @param sheetName 工作表名称
     * @param startRow  起始行，说明：从第几行开始合并，起始行为0
     * @param endColumn 结束行，说明，合并到第几列截止，起始列为0
     * @return
     */
    public UniExcelExportTask mergeExcelColumn(String sheetName, int startRow, int endColumn) {
        // 判断是否允许合并单元格，由于一旦部分数据写入临时磁盘，sheet将无法读取，导致部分数据无法合并单元格，故所有数据必须在内存中操作。
        if (!this.allowMergeColumn) {
            throw new RuntimeException(String.format("警告：该方法不支持【%s】合并单元格操作，请使用new UniExcelExportTask(-1)初始化，注意该方法会基于内存操作，其余场景使用默认参数即可。", sheetName));
        }
        // 获取工作表
        SXSSFSheet sheet = this.sheetMap.get(sheetName);
        if (null == sheet) {
            throw new RuntimeException(String.format("请先完成工作表【%s】表头初始化", sheetName));
        }
        // 合并列单元格数据
        UniExcelExportUtil.mergeCellColumn(sheet, startRow, endColumn);
        return this;
    }

    /**
     * 第二步【后续，特殊可选】，合并单元格，默认起始行为数据行
     * <pre>
     *  备注：2023-08-25 10:24 新增
     *  说明：
     *  1.new UniExcelExportTask(-1).buildHeader().addExcelData().mergeExcelColumn().exportExcel()。
     *  2.初始化参数必须设置-1，才允许合并单元格操作。
     * </pre>
     *
     * @param sheetName 工作表名称
     * @param endColumn 结束行，说明，合并到第几列截止，起始列为0
     * @return
     */
    public UniExcelExportTask mergeExcelColumn(String sheetName, int endColumn) {
        // 判断是否允许合并单元格，由于一旦部分数据写入临时磁盘，sheet将无法读取，导致部分数据无法合并单元格，故所有数据必须在内存中操作。
        if (!this.allowMergeColumn) {
            throw new RuntimeException(String.format("警告：该方法不支持【%s】合并单元格操作，请使用new UniExcelExportTask(-1)初始化，注意该方法会基于内存操作，其余场景使用默认参数即可。", sheetName));
        }
        // 获取工作表
        SXSSFSheet sheet = this.sheetMap.get(sheetName);
        if (null == sheet) {
            throw new RuntimeException(String.format("请先完成工作表【%s】表头初始化", sheetName));
        }
        // 获取数据起始行
        Integer startDataRowIndex = this.initStartDataRowIndexMap.get(sheetName);
        // 合并列单元格数据
        UniExcelExportUtil.mergeCellColumn(sheet, startDataRowIndex, endColumn);
        return this;
    }

    /**
     * 第三步，导出工作簿
     *
     * @return
     */
    public byte[] exportExcel() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 写入流数据
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 释放临时文件
            workbook.dispose();
            // 强制取消引用
            sheetMap.clear();
            sheetMap = null;
            currentStartDataRowIndexMap.clear();
            currentStartDataRowIndexMap = null;
            initStartDataRowIndexMap.clear();
            initStartDataRowIndexMap = null;
            uniExcelStyleExportConfigMap.clear();
            uniExcelStyleExportConfigMap = null;
            dynamicDataHeaderListMap.clear();
            dynamicDataHeaderListMap = null;
            cellTypeMap.clear();
            cellTypeMap = null;
            Console.log("{}【表格导出】写入 {} 条记录,耗时 {} ms.", DateUtil.now(), dataSizeSum, (System.currentTimeMillis() - startTime));
        }
    }
}