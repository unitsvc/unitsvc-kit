package com.unitsvc.kit.poi4.excel.write.config;

import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellBoolFmtEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：动态表格导出样式配置
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/18 8:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UniExcelStyleExportConfig implements Serializable {

    private static final long serialVersionUID = -4753604538611446166L;

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
     * 定义是否启用汇总行加粗样式
     * <p>
     * 说明：默认开启
     */
    private boolean enableSummaryMarkStyle = true;

    /**
     * 定义汇总行字段标识
     * <pre>
     * 备注：2023-10-08 17:20 补充
     * 说明：该字段默认为布尔类型，用于控制汇总行样式
     * </pre>
     */
    private String summaryMark = "__summary_mark";

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
     * 说明：默认保留2位，最多保留14位
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

}
