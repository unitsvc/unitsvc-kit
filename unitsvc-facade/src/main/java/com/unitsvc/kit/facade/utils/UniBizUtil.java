package com.unitsvc.kit.facade.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能描述：复杂业务工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/11/7 23:46
 **/
public class UniBizUtil {

    /**
     * 转换变量参数
     *
     * @param varParamMapping 变量映射
     * @return
     */
    public static List<JsonObject> toVarParams(Map<String, List<String>> varParamMapping) {
        // 变量集合
        Set<String> varList = varParamMapping.keySet();
        List<JsonObject> params = new ArrayList<>();
        recurveParams(Lists.newArrayList(varList), 0, params, new JsonObject(), varParamMapping);
        return params;
    }

    /**
     * 递归处理笛卡尔积变量
     *
     * @param varList         变量名称
     * @param position        偏移量
     * @param returnParams    递归返回值
     * @param cacheParam      临时变量
     * @param varParamMapping 变量映射
     */
    private static void recurveParams(List<String> varList, int position, List<JsonObject> returnParams, JsonObject cacheParam, Map<String, List<String>> varParamMapping) {
        String key = varList.get(position);
        List<String> dataList = varParamMapping.get(key);
        for (int i = 0; i < dataList.size(); i++) {
            JsonObject childCacheParam = cacheParam;
            if (i != dataList.size() - 1) {
                // 笛卡尔积参数合并
                JsonObject newParam = new JsonObject();
                for (Map.Entry<String, JsonElement> entry : cacheParam.entrySet()) {
                    newParam.add(entry.getKey(), entry.getValue());
                }
                childCacheParam = newParam;
            }
            childCacheParam.addProperty(key, dataList.get(i));
            // 遍历到最后退出递归
            if (position == varList.size() - 1) {
                returnParams.add(childCacheParam);
                continue;
            }
            recurveParams(varList, position + 1, returnParams, childCacheParam, varParamMapping);
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> params = Maps.newHashMap();
        params.put("k1", Lists.newArrayList("a", "b"));
        params.put("k2", Lists.newArrayList("c", "d"));
        params.put("k3", Lists.newArrayList("e", "f"));
        List<JsonObject> list = toVarParams(params);
        System.out.println("list = " + list);
    }

}
