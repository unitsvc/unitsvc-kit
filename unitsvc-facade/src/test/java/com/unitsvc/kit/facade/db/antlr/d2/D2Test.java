package com.unitsvc.kit.facade.db.antlr.d2;

import cn.hutool.core.date.StopWatch;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.unitsvc.kit.facade.db.antlr.d2.visitor.FilterUtil;
import org.bson.Document;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/16 9:39
 **/
public class D2Test {
    @Test
    public void testFLoadCalculate() {
        List<String> exprList = Lists.newArrayList(
                "(age >= -30.01 && name == 'John') || (color == 'blue' && (type == 'A' || type == 'B'))",
                "(age >= 30.01 && name == 'John') || (color == 'blue' && (type == 'E' || type == 'B'))",
                "(age >= 30 && name == 'John') || (color == 'blue' && (type == 'C' || type == 'F'))"
        );
        Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        StopWatch stopWatch = new StopWatch();
        for (String expr : exprList) {
            stopWatch.start(String.format("解析：%s", expr));
            Document document = FilterUtil.buildFilter(expr);
            System.out.println("GSON.toJson(document) = " + GSON.toJson(document));
            stopWatch.stop();
        }
        System.out.println("stopWatch.prettyPrint(TimeUnit.MILLISECONDS) = " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

}
