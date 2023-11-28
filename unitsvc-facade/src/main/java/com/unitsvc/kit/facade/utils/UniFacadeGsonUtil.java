package com.unitsvc.kit.facade.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jpardus.spider.sccs.utils.ObjectUtils;

import java.util.Map;

/**
 * 功能描述：gson工具类
 * <p>
 * 说明：源自GsonUtils类，但改类会出现JsonNull空指针异常
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/11 14:31
 **/
public class UniFacadeGsonUtil {

    public static void handleNumberDecimal(JsonObject item) {
        if (null != item && !item.isJsonNull()) {
            for (Map.Entry<String, JsonElement> entry : item.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                // numberDecimal类型转换 decimal->double
                if (value instanceof JsonObject) {
                    JsonObject valObj = value.getAsJsonObject();
                    if (valObj.has("$numberDecimal")) {
                        item.addProperty(key, valObj.get("$numberDecimal").getAsDouble());
                    }
                }
            }
        }
    }

    public static void unwindJsonObject(JsonObject item) {
        if (null != item && !item.isJsonNull()) {
            for (Map.Entry<String, JsonElement> entry : item.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (value instanceof JsonObject) {
                    JsonObject objValue = value.getAsJsonObject();
                    if (null == objValue || objValue.isJsonNull()) {
                        continue;
                    }
                    // 处理时间类型
                    if (objValue.has("$numberDecimal")) {
                        item.addProperty(key, objValue.get("$numberDecimal").getAsDouble());
                    } else if (objValue.has("$oid")) {
                        item.addProperty(key, objValue.get("$oid").getAsString());
                    } else {
                        // 展开对象
                        for (Map.Entry<String, JsonElement> objEntry : objValue.entrySet()) {
                            String objEntryKey = objEntry.getKey();
                            JsonElement objEntryValue = objEntry.getValue();
                            item.add(String.format("%s.%s", key, objEntryKey), objEntryValue);
                        }
                        // 移除对象
                        item.remove(key);
                    }
                }
            }
        }
    }

    public static String getString(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsString();
    }

    public static Boolean getBoolean(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsBoolean();
    }

    public static Integer getInt(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsInt();
    }

    public static Long getLong(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsLong();
    }

    public static Double getDouble(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsDouble();
    }

    public static Float getFloat(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsFloat();
    }

    public static Short getShort(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsShort();
    }

    public static Byte getByte(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsByte();
    }

    public static JsonArray getJsonArray(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsJsonArray();
    }

    public static JsonObject getJsonObject(JsonElement element, String path) {
        element = getElement(element, path);
        return element == null || element.isJsonNull() ? null : element.getAsJsonObject();
    }

    public static JsonElement getElement(JsonElement element, String path) {
        ObjectUtils.requireNonEmpty(path, "getElement.path");
        if (element != null && !element.isJsonNull() && !element.isJsonPrimitive()) {
            String[] var2 = path.split("\\.");
            int var3 = var2.length;

            for (String field : var2) {
                if (element.isJsonObject()) {
                    element = element.getAsJsonObject().get(field);
                } else {
                    if (!element.isJsonArray()) {
                        return null;
                    }

                    try {
                        element = element.getAsJsonArray().get(Integer.parseInt(field));
                    } catch (NumberFormatException var7) {
                        return null;
                    }
                }

                if (element == null) {
                    return null;
                }
            }

            return element;
        } else {
            return null;
        }
    }
}
