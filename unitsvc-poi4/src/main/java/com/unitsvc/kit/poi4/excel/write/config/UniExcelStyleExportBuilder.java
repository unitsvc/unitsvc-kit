package com.unitsvc.kit.poi4.excel.write.config;

import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellBoolFmtEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：通用导出配置帮助类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/5 11:40
 **/
@Data
@NoArgsConstructor
public class UniExcelStyleExportBuilder implements Serializable {

    private static final long serialVersionUID = 5122009238099630399L;

    /**
     * 定义是否开启单元格保护
     * <p>
     * 说明：默认关闭
     */
    private boolean protectSheet = false;

    /**
     * 定义是否展示标题
     * <p>
     * 说明：默认关闭
     */
    private boolean showTitle = false;

    /**
     * 定义标题从首行开始占用几行
     * <p>
     * 说明：默认占用2行。可选值条件：大于1
     */
    private int titleHeaderSpanSize = 2;

    /**
     * 定义是否开启行编辑保护
     * <p>
     * 说明：默认开启
     */
    private boolean protectRowEdit = true;

    /**
     * 定义单元格行是否允许编辑字段标识
     * <p>
     * 说明：该字段默认为布尔类型。
     */
    private String rowEditFieldEn = "hint";

    /**
     * 定义默认全局列宽
     * <p>
     * 说明：默认22
     */
    private int defaultColumnWidth = 22;

    // **************************************************** 最左侧列固定配置 ****************************************************

    /**
     * 定义固定左侧列数
     * <p>
     * 说明：默认1，至少固定第1列，示例：=0则该字段不生效，>=1则至少固定第1列。
     * 注意：若开启动态固定列，则具体固定列数取最大值。
     */
    private int minFixedLeftColumnSize = 1;

    /**
     * 定义是否开启依据表头字段Fixed属性动态固定左侧列
     * <p>
     * 说明：默认true
     */
    private boolean dynamicFixedLeftColumn = true;

    /**
     * 定义是否展示单行表头
     * <p>
     * 说明：默认false，true|展示单行表头、false|表头固定占两行
     */
    private boolean headerOnlyShowOwn;

    /**
     * 定义小数点保留位数
     * <p>
     * 说明：默认保留2位，最多保留14位，仅对NUMBER类型生效
     */
    private int decimalPointNum = 2;

    /**
     * 定义时间展示格式
     * <p>
     * 说明：默认yyyy-MM-dd，注意调整列宽（20像素），否则yyyy-MM-dd HH:mm:ss会展示为#######
     */
    private String dataFormatStr = "yyyy-MM-dd";

    /**
     * 定义布尔类型展示格式
     * <p>
     * 说明：默认是|否
     */
    private UniExcelCellBoolFmtEnum boolFormatEnum = UniExcelCellBoolFmtEnum.FMT_YES_NO;

    /**
     * 构建通用导出样式配置类
     *
     * @return
     */
    public static UniExcelStyleExportBuilder build() {
        return new UniExcelStyleExportBuilder();
    }

    /**
     * 定义是否开启单元格保护
     *
     * @param protectSheet 开启|true、关闭|false
     * @return
     */
    public UniExcelStyleExportBuilder protectSheet(boolean protectSheet) {
        this.protectSheet = protectSheet;
        return this;
    }

    /**
     * 定义是否展示标题
     *
     * @param showTitle 展示标题|true、隐藏标题|false
     * @return
     */
    public UniExcelStyleExportBuilder showTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }

    /**
     * 定义标题从首行开始占用几行
     *
     * @param titleHeaderSpanSize 标题占用几行，示例：2
     * @return
     */
    public UniExcelStyleExportBuilder titleHeaderSpanSize(int titleHeaderSpanSize) {
        this.titleHeaderSpanSize = titleHeaderSpanSize;
        return this;
    }

    /**
     * 定义是否开启单元格保护
     *
     * @param protectRowEdit 开启|false、关闭|false
     * @return
     */
    public UniExcelStyleExportBuilder protectRowEdit(boolean protectRowEdit) {
        this.protectRowEdit = protectRowEdit;
        return this;
    }

    /**
     * 定义单元格行是否允许编辑字段标识
     *
     * @param rowEditFieldEn 字段标识，示例：hint
     * @return
     */
    public UniExcelStyleExportBuilder rowEditFieldEn(String rowEditFieldEn) {
        this.rowEditFieldEn = rowEditFieldEn;
        return this;
    }

    /**
     * 定义默认全局列宽
     *
     * @param defaultColumnWidth 默认列宽，示例：22
     * @return
     */
    public UniExcelStyleExportBuilder defaultColumnWidth(int defaultColumnWidth) {
        this.defaultColumnWidth = defaultColumnWidth;
        return this;
    }

    /**
     * 定义固定左侧列数
     *
     * @param minFixedLeftColumnSize 最小固定列数
     * @return
     */
    public UniExcelStyleExportBuilder minFixedLeftColumnSize(int minFixedLeftColumnSize) {
        this.minFixedLeftColumnSize = minFixedLeftColumnSize;
        return this;
    }

    /**
     * 定义是否开启依据表头字段Fixed属性动态固定左侧列
     *
     * @param dynamicFixedLeftColumn 动态固定|true、正常|false
     * @return
     */
    public UniExcelStyleExportBuilder dynamicFixedLeftColumn(boolean dynamicFixedLeftColumn) {
        this.dynamicFixedLeftColumn = dynamicFixedLeftColumn;
        return this;
    }

    /**
     * 定义是否展示单行表头
     *
     * @param headerOnlyShowOwn true|展示单行表头、false|表头固定占两行
     * @return
     */
    public UniExcelStyleExportBuilder headerOnlyShowOwn(boolean headerOnlyShowOwn) {
        this.headerOnlyShowOwn = headerOnlyShowOwn;
        return this;
    }

    /**
     * 定义小数点保留位数
     *
     * @param decimalPointNum 小数点保留位数
     * @return
     */
    public UniExcelStyleExportBuilder decimalPointNum(int decimalPointNum) {
        this.decimalPointNum = decimalPointNum;
        return this;
    }

    /**
     * 定义时间展示格式
     *
     * @param dataFormatStr 示例：yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
     * @return
     */
    public UniExcelStyleExportBuilder dataFormatStr(String dataFormatStr) {
        this.dataFormatStr = dataFormatStr;
        return this;
    }

    /**
     * 定义布尔类型展示格式
     *
     * @param boolFormatEnum 默认是|否
     * @return
     */
    public UniExcelStyleExportBuilder boolFormatEnum(UniExcelCellBoolFmtEnum boolFormatEnum) {
        this.boolFormatEnum = boolFormatEnum;
        return this;
    }

    /**
     * 创建通用导出配置
     *
     * @return
     */
    public UniExcelStyleExportConfig create() {
        UniExcelStyleExportConfig uniExcelStyleExportConfig = new UniExcelStyleExportConfig();
        uniExcelStyleExportConfig.setProtectSheet(this.protectSheet);
        uniExcelStyleExportConfig.setShowTitle(this.showTitle);
        uniExcelStyleExportConfig.setTitleHeaderSpanSize(this.titleHeaderSpanSize);
        uniExcelStyleExportConfig.setProtectRowEdit(this.protectRowEdit);
        uniExcelStyleExportConfig.setRowEditFieldEn(this.rowEditFieldEn);
        uniExcelStyleExportConfig.setDefaultColumnWidth(this.defaultColumnWidth);
        uniExcelStyleExportConfig.setMinFixedLeftColumnSize(this.minFixedLeftColumnSize);
        uniExcelStyleExportConfig.setDynamicFixedLeftColumn(this.dynamicFixedLeftColumn);
        uniExcelStyleExportConfig.setHeaderOnlyShowOwn(this.headerOnlyShowOwn);
        uniExcelStyleExportConfig.setDecimalPointNum(this.decimalPointNum);
        uniExcelStyleExportConfig.setDataFormatStr(this.dataFormatStr);
        uniExcelStyleExportConfig.setBoolFormatEnum(this.boolFormatEnum);
        return uniExcelStyleExportConfig;
    }

    public static void main(String[] args) {
        UniExcelStyleExportConfig uniExcelStyleExportConfig = UniExcelStyleExportBuilder
                .build().create();
        System.out.println("uniExcelStyleExportConfig = " + uniExcelStyleExportConfig);
    }

}
