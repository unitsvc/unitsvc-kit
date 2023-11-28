package com.unitsvc.kit.facade.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.jpardus.spider.sccs.Log;

import java.io.StringWriter;
import java.util.Collection;

/**
 * 功能描述：GSON序列化工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 9:51
 **/
public class GsonUtil {

    /**
     * 序列化
     */
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    /**
     * 序列化集合
     *
     * @param list 集合
     * @return
     */
    public static String toJson(Collection<?> list) {
        try (StringWriter stringWriter = new StringWriter(); JsonWriter writer = new JsonWriter(stringWriter)) {
            // 设置缩进，默认无缩进和换行
            writer.setIndent("");
            // 开始数组
            writer.beginArray();
            for (Object obj : list) {
                GSON.toJson(obj, obj.getClass(), writer);
            }
            // 结束数组
            writer.endArray();
            return stringWriter.getBuffer().toString();
        } catch (Exception e) {
            Log.error(String.format("【gson序列化异常】：%s", e.getMessage()), e);
            return null;
        }
    }

}
