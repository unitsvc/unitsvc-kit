package com.unitsvc.kit.facade.db.antlr.d3;

import cn.hutool.core.date.StopWatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.unitsvc.kit.facade.db.antlr.d3.visitor.FilterUtil;
import com.jpardus.db.jmongo.MongoQuerier;

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
public class D3Test {
    @Test
    public void testFLoadCalculate() {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        List<Object> a1 = Lists.newArrayList(false, true, "1.2", 123, 0.234, -3.14e-10, "字符串");
        String e1 = String.format("(age >= -3.14e-10 && name @ %s ) || (color == \"blue\" && (type == \"A\" || type == \"B\"))", gson.toJson(a1));
        List<String> exprList = Lists.newArrayList(
                "(age >= -3.14e-10 && name @ [false,true,\"1.2\",123,0.234,-3.14e-10,\"字符串\"] ) || (color == \"blue\" && (type == \"A\" || type == \"B\"))",
                "(age >= -3.14e-10 && name @ [false,null,\"1.2\",123,0.234,-3.14e-10,\"字符串\"] ) || (color == \"blue\" && (type == null || type == \"B\"))",
                e1
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
    public void testNumber() {
        BigDecimal bigDecimal = new BigDecimal("-3.14E-10");
        System.out.println("bigDecimal = " + bigDecimal.doubleValue());

        BigDecimal bigDecimal2 = new BigDecimal("-3.14e-10");
        System.out.println("bigDecimal2 = " + bigDecimal2.doubleValue());
    }

    @Test
    public void testQuery() {
        Document f = FilterUtil.buildFilter("title.cn==\"xxx\"");
        System.out.println("f = " + f);
        JsonArray query = MongoQuerier.query("table_xxx", f);
        System.out.println("query = " + query.size());
    }

}
