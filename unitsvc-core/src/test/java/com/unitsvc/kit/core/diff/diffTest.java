package com.unitsvc.kit.core.diff;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.unitsvc.kit.core.diff.model.DiffConfig;
import com.unitsvc.kit.core.diff.model.DiffWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 17:41
 **/
public class diffTest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BeanA {

        // 使用注解标记列
        //@DiffField(name = "字段A")
        private String a;

        // 忽略这个
        //@DiffField(name = "字段B", ignore = false)
        private String b;

        // 集合递归支持
        //@DiffField(name = "BList集合")
        private List<BeanB> bList;

        // 日期格式转换
        //@DiffField(name = "开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
        private Date start;

        //@DiffField(name = "价格")
        private BigDecimal price;

        //@DiffField(name = "名称")
        private String name;

        //@DiffField(name = "动态值")
        private JsonElement dynamic;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeanB {

        //@DiffId(name = "订单编号")
        //@DiffField(name = "主键")
        private long id;

        //@DiffField(name = "名称")
        private String name;

        //@DiffField(name = "CList集合")
        private List<BeanC> cList;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeanC {

        //@DiffId(name = "C主键")
        //@DiffField(name = "主键")
        private String id;

        //@DiffField(name = "名称")
        private String name;

        //@DiffField(name = "取值")
        private JsonPrimitive value;

        //@DiffField(name = "开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
        private Date date;

    }

    @Test
    public void diffTest() throws Exception {

        BeanA beanA1 = new BeanA();
        beanA1.setA("A11");
        beanA1.setB("A12");
        beanA1.setName("A名称");
        beanA1.setDynamic(new JsonPrimitive(true));
        beanA1.setBList(
                Lists.newArrayList(
                        BeanB.builder().id(1L).name("11").cList(
                                Lists.newArrayList(
                                        BeanC.builder().id("C1").name("C")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(23)
                                                ).build(),
                                        BeanC.builder().id("C2").name("C")
                                                .date(new Date())
                                                .value(
                                                        new JsonPrimitive(34)
                                                ).build()
                                )
                        ).build(),
                        BeanB.builder().id(2L).name("12").cList(
                                Lists.newArrayList(
                                        BeanC.builder().id("C1").name("C2")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(32)
                                                ).build(),
                                        BeanC.builder().id("C2").name("C2")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(23)
                                                ).build()
                                )
                        ).build()
                )
        );

        JsonArray elements = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "tim");
        jsonObject.addProperty("age", 12);
        elements.add(jsonObject);

        BeanA beanA2 = new BeanA();
        beanA2.setA("A21");
        beanA2.setB("A22");
        beanA2.setDynamic(
                elements
        );
        beanA2.setBList(
                Lists.newArrayList(
                        BeanB.builder().id(1L).name("21").cList(
                                Lists.newArrayList(
                                        BeanC.builder().id("C1").name("C")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(1)
                                                ).build(),
                                        BeanC.builder().id("C3").name("C")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(2)
                                                ).build()
                                )
                        ).build(),
                        BeanB.builder().id(3L).name("22").cList(
                                Lists.newArrayList(
                                        BeanC.builder().id("C1").name("C2")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(21)
                                                ).build(),
                                        BeanC.builder().id("C2").name("C2")
                                                .date(DateUtil.beginOfMonth(new Date()))
                                                .value(
                                                        new JsonPrimitive(23)
                                                ).build()
                                )
                        ).build()
                )
        );

        List<DiffConfig> diffConfigs = Lists.newArrayList(
                DiffConfig.builder().simpleClassName("BeanA").fieldConfigs(
                        Lists.newArrayList(
                                DiffConfig.FieldConfig.builder().property("a").nameCn("字段A").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("b").nameCn("字段B").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("bList").nameCn("BList集合").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("start").nameCn("开始时间").nameEn("").isPrimary(false).ignore(false).dataFormat("yyyy-MM-dd HH:mm:ss").build(),
                                DiffConfig.FieldConfig.builder().property("price").nameCn("价格").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("name").nameCn("名称").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("dynamic").nameCn("动态值").nameEn("").isPrimary(false).ignore(false).dataFormat("").build()
                        )
                ).build(),
                DiffConfig.builder().simpleClassName("BeanB").fieldConfigs(
                        Lists.newArrayList(
                                DiffConfig.FieldConfig.builder().property("id").nameCn("主键").nameEn("").isPrimary(true).primaryName("订单编号").ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("name").nameCn("名称").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("cList").nameCn("CList集合").nameEn("").isPrimary(false).ignore(false).dataFormat("").build()
                        )
                ).build(),
                DiffConfig.builder().simpleClassName("BeanC").fieldConfigs(
                        Lists.newArrayList(
                                DiffConfig.FieldConfig.builder().property("id").nameCn("C主键").nameEn("").isPrimary(true).primaryName("主键").ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("name").nameCn("名称").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("value").nameCn("取值").nameEn("").isPrimary(false).ignore(false).dataFormat("").build(),
                                DiffConfig.FieldConfig.builder().property("date").nameCn("开始时间").nameEn("").isPrimary(false).ignore(false).dataFormat("yyyy-MM-dd HH:mm:ss").build()
                        )
                ).build()
        );


        List<DiffWrapper> diffWrappers = AbstractObjectDiffV2.generateDiff(beanA1, beanA2, diffConfigs);
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
        String json = gson.toJson(diffWrappers);
        System.out.println("json = \n" + json);

        String note = ChineseObjectDiff.genChineseDiffStr(beanA1, beanA2);
        System.out.println("note = \n" + note);
    }

}
