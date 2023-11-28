package com.unitsvc.kit.poi4.excel.read.service.model;

import com.unitsvc.kit.poi4.excel.read.service.enums.FieldFormatEnum;
import com.unitsvc.kit.poi4.excel.read.service.enums.FieldShowLabelEnum;
import com.unitsvc.kit.poi4.excel.read.service.enums.FieldTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 任务字段定义
 *
 * @author jun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFieldModel implements Serializable {

    private static final long serialVersionUID = -2771132517376359161L;

    public TaskFieldModel(String id, String textValue, String nameCn, String nameEn) {
        this.id = id;
        this.textValue = textValue;
        this.nameCn = nameCn;
        this.nameEn = nameEn;
    }

    public TaskFieldModel(String id, String textValue, String nameCn, boolean ableEdit) {
        this.id = id;
        this.textValue = textValue;
        this.nameCn = nameCn;
        this.ableEdit = ableEdit;
    }

    public TaskFieldModel(String id, String nameEn, String nameCn, boolean required, boolean ableEdit) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameCn = nameCn;
        this.ableEdit = ableEdit;
        this.required = required;
    }

    public TaskFieldModel(String id, String nameEn, String nameCn, boolean required, boolean ableEdit, String fixed) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameCn = nameCn;
        this.ableEdit = ableEdit;
        this.required = required;
        this.fixed = fixed;
    }

    /**
     * 字段ID
     */
    private String id;

    /**
     * 字段中文名称
     */
    private String nameCn;

    /**
     * 字段英文名称
     */
    private String nameEn;

    /**
     * 字段类型
     */
    private FieldTypeEnum type;

    /**
     * 字段格式
     * <p>
     * 说明：特指输入格式
     */
    private FieldFormatEnum fieldFormat;

    /**
     * 格式是多选框，单选时的数据源 fieldFormat=RADIO，CHECKBOX时生效。 格式为：key1|value1,key2|value2
     */
    private String textValue;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 是否显示
     */
    private Boolean show;

    /**
     * 是否可以编辑
     */
    private boolean ableEdit;

    /**
     * 是否固定
     */
    private String fixed;

    /**
     * 可选，动态字段功能标签
     * <p>
     * 说明：主要用于格式化展示
     */
    private List<FieldShowLabelEnum> showLabels;

}
