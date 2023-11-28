package com.unitsvc.kit.facade.db.antlr;

import cn.hutool.core.date.StopWatch;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.unitsvc.kit.facade.antlr4.config.FilterExprConfig;
import com.unitsvc.kit.facade.antlr4.utils.UniFilterExprUtil;
import org.bson.Document;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/30 14:17
 **/
public class FilterExprTest {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    @Test
    public void filterTest() {
        List<String> exprList = Lists.newArrayList(
                //"($age >= -3.14e-10 && $array @ [false,'100','字符串',\"字符串\",100,0.001,-1.002] ) || ($color @ $$v1 || $type == $$v2 && ($type == 'A' || $type == \"B\" ))",
                //"($age >= -3.14e-10 && $array @ [false,'100','字符串',\"字符串\",100,0.001,-1.002] ) || ($color @ $$v1 || $type == $$v2 && ($type == 'A' || $type == \"B\" ))",
                //"($age >= -3.14e-10 && $array @ [false,'100','字符串',\"字符串\",100,0.001,-1.002] ) || ($color @ $$v1 || $type L_LIKE $$v2 && ($type left_like 'A' || $type == \"B\" ))",
                //"($year==&&$age==12)||($name like && $price==$$price )",
                //"$title.cn like && $apiKey=='abc'",
                //"($hcDeptId @ $$deptIdCommit && $hcLevel1DeptId @ $$deptIdCommit) || $hcLevel2DeptId @ $$deptIdCommit || $hcLevel3DeptId @ $$deptIdCommit || $hcLevel4DeptId @ $$deptIdCommit || $hcLevel5DeptId @ $$deptIdCommit || $hcLevel6DeptId @ $$deptIdCommit && ($round==1)",
                //"$name like '字符串' || $year like $$var",
                "$name == eval(``` return {{a}} + {{b}} ;```) || $age @ [1,2,3] && $year != '10'",
                "$name @ eval( ```function() {\n" +
                        "        var preMonth = '';\n" +
                        "        var month = new Date().getMonth() + 1;\n" +
                        "        if (month <= 1) {\n" +
                        "                preMonth = '12';\n" +
                        "        } else {\n" +
                        "                preMonth = String(month - 1);\n" +
                        "        }\n" +
                        "        return [preMonth];\n" +
                        "} ``` )"
        );
        StopWatch stopWatch = new StopWatch();
        Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();
        for (String expr : exprList) {
            stopWatch.start(String.format("解析：%s", expr));
            JsonObject relations = new JsonObject();
            relations.add("v1", GSON.fromJson(GSON.toJson(Lists.newArrayList("a", 1, "2")), JsonArray.class));
            relations.add("v2", new JsonPrimitive("字符串"));
            relations.addProperty("price", 20);

            JsonObject javascriptVars = new JsonObject();
            javascriptVars.addProperty("a", 1);
            javascriptVars.addProperty("b", 2);

            Document document = UniFilterExprUtil.buildMongodbFilter(expr, relations, javascriptVars, new FilterExprConfig().setIgnoreLikeNullException(false));
            System.out.println("GSON.toJson(document) = \n" + GSON.toJson(document));
            stopWatch.stop();
        }
        System.out.println("stopWatch.prettyPrint(TimeUnit.MILLISECONDS) = " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }
}
