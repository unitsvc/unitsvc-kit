package com.unitsvc.kit.facade.db.antlr.d4;

import cn.hutool.core.date.StopWatch;
import com.google.common.collect.Lists;
import com.google.gson.*;

import com.unitsvc.kit.facade.db.antlr.d4.vistor.FilterUtil;
import com.unitsvc.kit.facade.antlr4.utils.UniFilterExprUtil;
import org.bson.Document;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/16 9:39
 **/
public class D4Test {
    @Test
    public void testFLoadCalculate() {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<Object> a1 = Lists.newArrayList(false, true, "1.2", 123, 0.234, -3.14e-10, "字符串");
        String e1 = String.format("(age >= -3.14e-10 && name @ %s ) || (color == \"blue\" && (type == \"A\" || type == \"B\"))", gson.toJson(a1));
        List<String> exprList = Lists.newArrayList(
                e1,
                "(age >= -3.14e-10 && name @ [false,true,\"1.2\",123,0.234,-3.14e-10,\"字符串\"] ) || (color == \"blue\" && (type == \"A\" || type == \"B\"))",
                "(age >= -3.14e-10 && name @ [false,null,\"1.2\",123,0.234,-3.14e-10,\"字符串\"] ) || (color == \"blue\" && (type == null || type == \"B\"))",
                "(age >= -3.14e-10 && name @ [false,true,\"1.2\",123,0.234,-1.002,\"字符串\"] ) || (color == bule && (type == 'A' || type == \"B\" || type== C ))"
        );
        StopWatch stopWatch = new StopWatch();
        Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
        for (String expr : exprList) {
            stopWatch.start(String.format("解析：%s", expr));
            Document document = FilterUtil.buildFilter(expr);
            System.out.println("GSON.toJson(document) = " + GSON.toJson(document));
            stopWatch.stop();
        }
        System.out.println("stopWatch.prettyPrint(TimeUnit.MILLISECONDS) = " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testFLoadCalculate2() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<Object> a1 = Lists.newArrayList(false, true, "1.2", 123, 0.234, -3.14e-10, "字符串");
        String e1 = String.format("($age >= -3.14e-10 && $name @ %s ) || ($color == \"blue\" && ($type == \"A\" || $type == \"B\"))", gson.toJson(a1));
        List<String> exprList = Lists.newArrayList(
                e1,
                "($age >= -3.14e-10 && $name @ [false,true,\"1.2\",123,0.234,-3.14e-10,\"字符串\"] ) || ($color == \"blue\" && ($type == \"A\" || $type == \"B\"))",
                "($age >= -3.14e-10 && $name @ [false,null,\"1.2\",123,0.234,-3.14e-10,\"字符串\"] ) || ($color == \"blue\" && ($type == null || $type == \"B\"))",
                "($age >= -3.14e-10 && $name @ [false,true,\"1.2\",123,0.234,-1.002,\"字符串\"] ) || ($color == $$bule && ($type == 'A' || $type == \"B\" || $type== $$C ))"
        );
        StopWatch stopWatch = new StopWatch();
        Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
        for (String expr : exprList) {
            stopWatch.start(String.format("解析：%s", expr));
            Document document = UniFilterExprUtil.buildMongodbFilter(expr, new JsonObject());
            System.out.println("GSON.toJson(document) = " + GSON.toJson(document));
            stopWatch.stop();
        }
        System.out.println("stopWatch.prettyPrint(TimeUnit.MILLISECONDS) = " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testFLoadCalculate3() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<String> exprList = Lists.newArrayList(
                "($age >= -3.14e-10 && $name @ [false,true,\"1.2\",123,0.234,-1.002,\"字符串\"] ) || ($color @ $$v1 || $type == $$v2 && ($type == 'A' || $type == \"B\" ))"
        );
        StopWatch stopWatch = new StopWatch();
        Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
        for (String expr : exprList) {
            stopWatch.start(String.format("解析：%s", expr));
            JsonObject relations = new JsonObject();
            relations.add("v1", GSON.fromJson(GSON.toJson(Lists.newArrayList("a", 1, "2")), JsonArray.class));
            relations.add("v2", new JsonPrimitive("字符串"));

            Document document = UniFilterExprUtil.buildMongodbFilter(expr, relations);
            System.out.println("GSON.toJson(document) = " + GSON.toJson(document));
            stopWatch.stop();
        }
        System.out.println("stopWatch.prettyPrint(TimeUnit.MILLISECONDS) = " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testNumber() {
        BigDecimal bigDecimal = new BigDecimal("-3.14E-10");
        System.out.println("bigDecimal = " + bigDecimal.doubleValue());

        BigDecimal bigDecimal2 = new BigDecimal("-3.14e-10");
        System.out.println("bigDecimal2 = " + bigDecimal2.doubleValue());
    }

}
