package com.unitsvc.kit.poi4.excel.common.model;

import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellAttrLabelEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldFixedEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：通用动态表头定义
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/13 16:58
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UniExcelHeaderReq implements Serializable {

    private static final long serialVersionUID = -1427695019557477942L;

    /**
     * 必填，字段对应中文名
     * <p>
     * 备注：若groupList分组存在，则nameCn=分组名称。
     */
    private String nameCn;

    /**
     * 必填，字段对应英文名
     * <p>
     * 备注：若groupList分组存在，则nameEn=group。
     */
    private String nameEn;

    /**
     * 选填，列是否隐藏，默认为空，列展示
     * <p>
     * 备注：控制字段所在列是否隐藏。
     */
    private Boolean columnHidden;

    /**
     * 选填，字段唯一ID，扩展字段
     * <p>
     * 备注：动态取值用，目前以nameEn为准，该字段暂不生效。
     */
    private String fieldId;

    /**
     * 选填，是否可以编辑，默认为空，不可编辑
     * <p>
     * 备注：控制单元格所在列是否可编辑。
     */
    private Boolean columnEdit;

    /**
     * 选填，列宽，默认为空。
     * <p>
     * 备注：256*(字符数+5)，设置的宽度包括4像素的边距填充（每侧两个），加上网格线的1像素填充，像素px/8大约为展示的字符数。
     */
    private Integer columnWidth;

    /**
     * 选填，是否固定
     */
    private UniExcelCellFieldFixedEnum fixed;

    /**
     * 选填，下拉可选值，默认为空。
     * <pre>
     * 备注：
     * 1.仅有限下拉选项，暂不支持选项非常多且值过长的下拉值。示例：是、否
     * 2.该值会被OPTIONS_BOOL标签覆盖
     * </pre>
     */
    private List<String> selectValueList;

    /**
     * 必填，扩展字段属性，综合判断当前字段具体类型。
     */
    private UniExcelCellFieldTypeEnum fieldType;

    /**
     * 选填，分组字段集合。
     * <p>
     * 备注：若为空，则不分组。
     */
    private List<UniExcelHeaderReq> groupList;

    /**
     * 选填，字段展示标签
     * <pre>
     * 备注：2023-01-04 扩展配置
     * 说明：
     * 1.用于控制单元格额外属性、比如数值精度、单元格是否强制文本类型、布尔值展示类型
     * 2.ATTR、NUMBER、BOOL开头标签有且只有一个。示例：NUMBER_14
     * </pre>
     */
    private List<UniExcelCellAttrLabelEnum> showLabels;

    /**
     * 选填，默认取值ID
     * <pre>
     * 备注：2023-08-24 17:25 扩展配置
     * 功能：
     * 1.若nameCn取不到值，则依次从defaultTakeValueIds取值。
     * 2.示例，取一级部门CN，若取不到，再取一级部门EN，否则取一级部门ID。
     * 3.注意，需要保证数据类型一致，反例，ID是数值，CN、EN是字符串则不适用。
     * 4.仅支持poi4，暂不支持poi5，且版本v1.0.5及以上使用。
     * </pre>
     */
    private List<String> defaultTakeValueIds;

    /**
     * 选填，是否敏感字段
     * <pre>
     *  备注：2023-09-21 13:54 扩展配置
     *  说明：数据合规，同一份数据，对指定字段，不同地区动态看到敏感字段内容。
     *  功能：
     *  1.若该字段是敏感字段，则不展示该数据。
     * </pre>
     */
    private Boolean sensitive;

    /**
     * 特殊可选，敏感字段比对值
     * <pre>
     *  备注：2023-09-21 13:56 扩展配置
     *  功能：
     *  1.仅sensitive=true生效，则compareSensitiveText必填，否则该功能不生效
     *    示例：sensitiveShowContent=***，数据值=***，则展示***，否则展示原始值。
     *  2.产生原因，解决数据类型转换问题。
     * </pre>
     */
    private String compareSensitiveText;

    /**
     * 动态表头构造函数
     *
     * @param nameCn 中文名
     * @param nameEn 英文名
     */
    public UniExcelHeaderReq(String nameCn, String nameEn) {
        this.nameCn = nameCn;
        this.nameEn = nameEn;
        this.fieldType = UniExcelCellFieldTypeEnum.FIELD_STRING;
    }

    /**
     * 动态表头构造函数
     * <p>
     * 备注：默认字符串类型
     *
     * @param nameCn     中文名
     * @param nameEn     英文名
     * @param showLabels 展示标签
     */
    public UniExcelHeaderReq(String nameCn, String nameEn, List<UniExcelCellAttrLabelEnum> showLabels) {
        this.nameCn = nameCn;
        this.nameEn = nameEn;
        this.fieldType = UniExcelCellFieldTypeEnum.FIELD_STRING;
        this.setShowLabels(showLabels);
    }

    /**
     * 动态表头构造函数
     *
     * @param nameCn      中文名
     * @param nameEn      英文名
     * @param columnEdit  列是否允许被编辑
     * @param columnWidth 列宽
     */
    public UniExcelHeaderReq(String nameCn, String nameEn, Boolean columnEdit, Integer columnWidth) {
        this.nameCn = nameCn;
        this.nameEn = nameEn;
        this.columnEdit = columnEdit;
        this.columnWidth = columnWidth;
        this.fieldType = UniExcelCellFieldTypeEnum.FIELD_STRING;
    }

    /**
     * 动态表头构造函数
     *
     * @param nameCn 中文名
     * @param nameEn 英文名
     * @param fixed  固定方式
     */
    public UniExcelHeaderReq(String nameCn, String nameEn, UniExcelCellFieldFixedEnum fixed) {
        this.nameCn = nameCn;
        this.nameEn = nameEn;
        this.fixed = fixed;
        this.fieldType = UniExcelCellFieldTypeEnum.FIELD_STRING;
    }

}
