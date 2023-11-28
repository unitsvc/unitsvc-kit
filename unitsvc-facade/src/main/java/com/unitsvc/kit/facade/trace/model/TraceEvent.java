package com.unitsvc.kit.facade.trace.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/26 14:08
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TraceEvent implements Serializable {
    private static final long serialVersionUID = -9054378855075922174L;
    /**
     * 必填，事件名称
     */
    private String eventName;
    /**
     * 可选，事件关联字段
     */
    private JsonObject relations;

    /**
     * 添加属性
     *
     * @param key   属性键
     * @param value 属性值
     * @return
     */
    public TraceEvent addRelationAttr(String key, JsonElement value) {
        if (null == relations) {
            relations = new JsonObject();
        }
        if (StringUtils.isNotEmpty(key)) {
            relations.add(key, value);
        }
        return this;
    }

    /**
     * 添加属性
     *
     * @param key   属性键
     * @param value 属性值
     * @return
     */
    public TraceEvent addRelationAttr(String key, String value) {
        if (null == relations) {
            relations = new JsonObject();
        }
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            relations.addProperty(key, value);
        }
        return this;
    }

    /**
     * 添加属性
     *
     * @param key   属性键
     * @param value 属性值
     * @return
     */
    public TraceEvent addRelationAttr(String key, Number value) {
        if (null == relations) {
            relations = new JsonObject();
        }
        if (StringUtils.isNotEmpty(key) && null != value) {
            relations.addProperty(key, value);
        }
        return this;
    }

    /**
     * 添加属性
     *
     * @param key   属性键
     * @param value 属性值
     * @return
     */
    public TraceEvent addRelationAttr(String key, Boolean value) {
        if (null == relations) {
            relations = new JsonObject();
        }
        if (StringUtils.isNotEmpty(key) && null != value) {
            relations.addProperty(key, value);
        }
        return this;
    }

}
