package com.unitsvc.kit.poi4.excel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.ToNumberPolicy;
import com.unitsvc.kit.core.excel.enums.UniExcelCellTypeEnum;
import com.unitsvc.kit.poi4.excel.read.UniExcelImportTask;
import com.unitsvc.kit.poi4.excel.write.UniExcelExportTask;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellAttrLabelEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellBoolFmtEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldFixedEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.read.config.UniExcelStyleImportBuilder;
import com.unitsvc.kit.poi4.excel.read.config.UniExcelStyleImportConfig;
import com.unitsvc.kit.poi4.excel.read.handler.IUniExportDataHandler;
import com.unitsvc.kit.poi4.excel.read.handler.UniCustomSheetHandler;
import com.unitsvc.kit.poi4.excel.read.handler.UniV4SheetDefaultHandler;
import com.unitsvc.kit.poi4.excel.read.service.UniV4Excel2007ReadService;
import com.unitsvc.kit.poi4.excel.read.service.model.UniExcelImportResultResp;
import com.unitsvc.kit.poi4.excel.read.utils.UniExcelCellDateUtil;
import com.unitsvc.kit.poi4.excel.write.config.UniExcelStyleExportBuilder;
import com.unitsvc.kit.poi4.excel.write.style.defalut.UniExcelDefaultStyleImpl;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 功能描述：表格导入导出测试
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 11:03
 **/
public class UniExcelTest {

    /**
     * 表格读取测试
     */
    @Test
    public void customV4Excel2007ReadCustomTest() {
        try {
            UniV4Excel2007ReadService uniExcel2007Read = new UniV4Excel2007ReadService(new UniV4SheetDefaultHandler() {
                @Override
                public void startRowRead(int rowNum, int sheetIndex, String sheetName) {
                    String format = String.format("开始读取->行号【%s】工作表序号【%s】工作表名称【%s】", rowNum, sheetIndex, sheetName);
                    System.out.println("format = " + format);
                }

                @Override
                public void endRowRead(int rowNum, int sheetIndex, String sheetName) {
                    String format = String.format("结束读取->行号【%s】工作表序号【%s】工作表名称【%s】", rowNum, sheetIndex, sheetName);
                    System.out.println("format = " + format);
                }

                @Override
                public void cellRead(String cellRef, int rowNum, Integer sheetIndex, String sheetName, int colNum, UniExcelCellTypeEnum cellType, String cellValue, String fmtValue, String formula) {
                    String format = String.format("结束读取->单元格位置【%s】行号【%s】列号【%s】工作表序号【%s】工作表名称【%s】类型【%s】源数据【%s】展示值【%s】公式【%s】", cellRef, rowNum, colNum, sheetIndex, sheetName, cellType, cellValue, fmtValue, formula);
                    System.out.println("format = " + format);
                }


            });
            uniExcel2007Read.read("测试导入模拟数据.xlsx", -1);
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    /**
     * 构建动态表头
     *
     * @return
     */
    public List<UniExcelHeaderReq> buildHeaderList() {
        // 公共字段-1
        UniExcelHeaderReq idDTO = UniExcelHeaderReq.builder()
                .nameEn("id").nameCn("工号")
                .fieldType(UniExcelCellFieldTypeEnum.FIELD_STRING)
                .fixed(UniExcelCellFieldFixedEnum.LEFT)
                .showLabels(Lists.newArrayList(UniExcelCellAttrLabelEnum.ATTR_TEXT))
                .columnWidth(256 * (5 + 5))
                .build();

        // 公共字段-2
        UniExcelHeaderReq timeDTO = UniExcelHeaderReq.builder()
                .nameEn("time").nameCn("入职时间").columnEdit(false)
                .columnHidden(false)
                .fieldType(UniExcelCellFieldTypeEnum.FIELD_DATE)
                .showLabels(Lists.newArrayList(UniExcelCellAttrLabelEnum.DATE_YMD_2, UniExcelCellAttrLabelEnum.WIDTH_15))
                .build();

        // 公共字段-3
        UniExcelHeaderReq studyDTO = UniExcelHeaderReq.builder()
                .nameEn("study").nameCn("学习期").columnEdit(false)
                .fieldType(UniExcelCellFieldTypeEnum.FIELD_BOOLEAN)
                .showLabels(Lists.newArrayList(UniExcelCellAttrLabelEnum.BOOL_YN_EN, UniExcelCellAttrLabelEnum.OPTIONS_BOOL, UniExcelCellAttrLabelEnum.WIDTH_10))
                .selectValueList(Arrays.asList("是", "否"))
                .build();
        // 公共字段-3
        UniExcelHeaderReq teamDTO = UniExcelHeaderReq.builder()
                .nameEn("team").nameCn("是否组长").columnEdit(false)
                .fieldType(UniExcelCellFieldTypeEnum.FIELD_BOOLEAN)
                .selectValueList(Arrays.asList("是", "否"))
                .build();
        // 公共字段-4
        UniExcelHeaderReq formulaDTO = UniExcelHeaderReq.builder()
                .nameEn("formula").nameCn("公式").columnEdit(false)
                .fieldType(UniExcelCellFieldTypeEnum.FIELD_FORMULA)
                .build();
        // 分组表头-1
        UniExcelHeaderReq group1DTO = UniExcelHeaderReq.builder()
                .nameEn("group").nameCn("员工信息-分组")
                .groupList(
                        Arrays.asList(
                                UniExcelHeaderReq.builder()
                                        .nameEn("name").nameCn("姓名").columnEdit(true)
                                        .fieldType(UniExcelCellFieldTypeEnum.FIELD_STRING)
                                        .build(),
                                UniExcelHeaderReq.builder()
                                        .nameEn("age").nameCn("年龄").columnEdit(true)
                                        .fieldType(UniExcelCellFieldTypeEnum.FIELD_INTEGER)
                                        .selectValueList(Arrays.asList("18", "19", "20", "21"))
                                        .build()
                        )
                ).build();
        // 分组表头-2
        UniExcelHeaderReq group2DTO = UniExcelHeaderReq.builder()
                .nameEn("group").nameCn("工资信息-分组")
                .groupList(
                        Arrays.asList(
                                UniExcelHeaderReq.builder()
                                        .nameEn("money").nameCn("基本工资").columnEdit(true)
                                        .fieldType(UniExcelCellFieldTypeEnum.FIELD_INTEGER)
                                        .build(),
                                UniExcelHeaderReq.builder()
                                        .nameEn("discount").nameCn("折算系数").columnEdit(false)
                                        .fieldType(UniExcelCellFieldTypeEnum.FIELD_NUMBER)
                                        .showLabels(Lists.newArrayList(UniExcelCellAttrLabelEnum.NUMBER_1))
                                        .build(),
                                UniExcelHeaderReq.builder()
                                        .nameEn("bonus").nameCn("奖金基数").columnEdit(true)
                                        .columnHidden(false)
                                        .fieldType(UniExcelCellFieldTypeEnum.FIELD_NUMBER)
                                        .sensitive(true)
                                        .compareSensitiveText("***")
                                        .build()
                        )
                ).build();
        UniExcelHeaderReq group3DTO = UniExcelHeaderReq.builder()
                .nameEn("group1").nameCn("复杂分组-分组1")
                .groupList(
                        Arrays.asList(
                                UniExcelHeaderReq.builder()
                                        .nameEn("group1-1").nameCn("复杂分组-分组1-1")
                                        .groupList(
                                                Arrays.asList(
                                                        UniExcelHeaderReq.builder()
                                                                .nameEn("field1").nameCn("字段1").columnEdit(true)
                                                                .fieldType(UniExcelCellFieldTypeEnum.FIELD_STRING)
                                                                .defaultTakeValueIds(Lists.newArrayList("field1Cn", "field1En"))
                                                                .build(),
                                                        UniExcelHeaderReq.builder()
                                                                .nameEn("field2").nameCn("字段2").columnEdit(true)
                                                                .fieldType(UniExcelCellFieldTypeEnum.FIELD_STRING)
                                                                .defaultTakeValueIds(Lists.newArrayList("field2Cn", "field2En"))
                                                                .build()
                                                )
                                        )
                                        .build(),
                                UniExcelHeaderReq.builder()
                                        .nameEn("group2-1").nameCn("复杂分组-分组2-1")
                                        .groupList(Arrays.asList(
                                                UniExcelHeaderReq.builder()
                                                        .nameEn("group3-1").nameCn("复杂分组-分组3-1")
                                                        .groupList(
                                                                Arrays.asList(
                                                                        UniExcelHeaderReq.builder()
                                                                                .nameEn("field1").nameCn("字段1").columnEdit(true)
                                                                                .fieldType(UniExcelCellFieldTypeEnum.FIELD_STRING)
                                                                                .defaultTakeValueIds(Lists.newArrayList("field1Cn", "field1En"))
                                                                                .sensitive(true)
                                                                                .compareSensitiveText("***")
                                                                                .build(),
                                                                        UniExcelHeaderReq.builder()
                                                                                .nameEn("field3").nameCn("字段3").columnEdit(true)
                                                                                .fieldType(UniExcelCellFieldTypeEnum.FIELD_STRING)
                                                                                .build()
                                                                )
                                                        )
                                                        .build()
                                        ))
                                        .build()
                        )
                ).build();
        // 定义动态表头集合
        List<UniExcelHeaderReq> dynamicHeaderDTOList = new LinkedList<>(Arrays.asList(
                idDTO, timeDTO, studyDTO, teamDTO, formulaDTO, group1DTO, group2DTO, group3DTO
        ));
        return dynamicHeaderDTOList;
    }

    /**
     * 构建动态数据
     *
     * @return
     */
    public List<JsonObject> buildDataList() {
        // 定义动态数据集合
        JsonObject data1 = new JsonObject();
        data1.addProperty("id", "01000");
        data1.addProperty("name", "张三");
        data1.addProperty("age", 20);
        data1.addProperty("money", 1000000000);
        // 测试后规律：第15位数值，取决于16位17位。若16不满足四舍五入，看第17位满足四舍五入，16位加一，再进行四舍五入
        data1.addProperty("discount", 0.512345123451234541);
        data1.addProperty("bonus", 200.01);
        data1.addProperty("time", DateUtil.now());
        data1.addProperty("study", false);
        data1.addProperty("team", true);
        data1.addProperty("formula", "I2+G2*H2");
        //data1.addProperty("field1", "10");
        data1.addProperty("field2", "11");
        data1.addProperty("field3", "12");
        data1.addProperty("field1Cn", "中文");
        data1.addProperty("__summary_mark", true);
        //data1.addProperty("field1En", "英文");

        JsonObject data2 = new JsonObject();
        data2.addProperty("id", "12000");
        data2.addProperty("name", "李四");
        data2.addProperty("age", 20);
        data2.addProperty("money", 2000);
        data2.addProperty("discount", 0.4101);
        data2.addProperty("bonus", "***");
        data2.addProperty("time", DateUtil.now());
        data2.addProperty("study", true);
        data2.addProperty("team", false);
        data2.addProperty("hint", false);
        //data2.addProperty("field1", "10");
        data2.addProperty("field2", "11");
        data2.addProperty("field3", "12");
        //data2.addProperty("field1Cn", "中文");
        data2.addProperty("field1En", "英文");

        JsonObject data3 = new JsonObject();
        data3.addProperty("id", "12000");
        data3.addProperty("name", "李四");
        data3.addProperty("age", 20);
        data3.addProperty("money", 2000);
        data3.addProperty("discount", 0.4101);
        data3.addProperty("bonus", 300);
        data3.addProperty("time", DateUtil.now());
        data3.addProperty("study", true);
        data3.addProperty("team", false);
        data3.addProperty("hint", false);
        data3.addProperty("field1", "***");
        data3.addProperty("field2", "11");
        data3.addProperty("field3", "12");
        //data3.addProperty("field1Cn", "中文");
        data3.addProperty("field1En", "英文");

        List<JsonObject> dynamicDataDTOList = new LinkedList<>(Arrays.asList(
                data1, data2, data3
        ));
        return dynamicDataDTOList;
    }

    /**
     * 测试通用导出
     *
     * @throws Exception
     */
    @Test
    public void uniExcelExportTest() throws Exception {

        // 通用导入导出
        UniExcelExportTask uniExcelExportTask = new UniExcelExportTask();
        try (FileOutputStream fileOutputStream = new FileOutputStream("./uni-excel-demo.xlsx")) {
            List<UniExcelHeaderReq> dynamicHeaderDTOList = this.buildHeaderList();
            List<JsonObject> dynamicDataDTOList = this.buildDataList();
            String sheetName = "通用导出《sheet》";
            // 通用导出
            byte[] bytes = uniExcelExportTask
                    .buildHeader(
                            sheetName + "1", "员工汇总《标题》", dynamicHeaderDTOList,
                            new UniExcelDefaultStyleImpl().setDataWrapText(false),
                            UniExcelStyleExportBuilder.build()
                                    .minFixedLeftColumnSize(1)
                                    .showTitle(true)
                                    .headerOnlyShowOwn(true)
                                    .decimalPointNum(2)
                                    .dataFormatStr("yyyy-MM-dd HH:mm:ss")
                                    .defaultColumnWidth(20)
                                    .boolFormatEnum(UniExcelCellBoolFmtEnum.FMT_YES_NO)
                                    .create()
                    )
                    .addExcelData(sheetName + "1", dynamicDataDTOList)
                    .buildHeader(
                            sheetName + "2", "员工汇总《标题》", dynamicHeaderDTOList,
                            UniExcelStyleExportBuilder.build()
                                    .showTitle(false)
                                    .minFixedLeftColumnSize(2)
                                    .headerOnlyShowOwn(false)
                                    .create()
                    )
                    .addExcelData(sheetName + "2", dynamicDataDTOList)
                    .exportExcel();
            fileOutputStream.write(bytes);
        }
    }


    /**
     * 测试通用导入
     *
     * @throws Exception
     */
    @Test
    public void uniExcelImportTest() throws Exception {
        FileInputStream inputStream = new FileInputStream("./uni-excel-demo.xlsx");
        List<UniExcelHeaderReq> dynamicHeaderDTOList = this.buildHeaderList();

        // 通用导入导出
        UniExcelImportTask uniExcelImportTask = new UniExcelImportTask();

        // 通用导入
        UniExcelImportResultResp excelImportResultResp = uniExcelImportTask.importDynamicExcel(inputStream, dynamicHeaderDTOList,
                UniExcelStyleImportBuilder.build()
                        // 表头所在行
                        .readHeaderRowIndex(0)
                        // 读可编辑字段
                        .onlyReadEditField(false)
                        // 校验可编辑字段是否都为空
                        .checkEditColumnNameCnIsAllEmpty(true)
                        // 设置检查的联合主键
                        .checkExitsPkNameCns(Lists.newArrayList("姓名"))
                        // 添加唯一索引
                        .addExtraIndex(true)
                        // 索引名称
                        .extraIndexName("_id")
                        // ------------ 读取自定义字段 ---------------
                        .readCustomHeaderData(true)
                        // ------------ 添加sheetIndex索引 ----------
                        .addSheetIndex(true)
                        // 设置补充的固定字段
                        .globalColumnConstants(
                                Lists.newArrayList(
                                        // 主键字段
                                        UniExcelStyleImportConfig.ColumnConstant.builder().nameEn("__checkId").value(IdUtil.getSnowflakeNextIdStr()).build(),
                                        UniExcelStyleImportConfig.ColumnConstant.builder().nameEn("__month").value("2022-10").build(),
                                        UniExcelStyleImportConfig.ColumnConstant.builder().nameEn("__checkDate").value(DateUtil.now()).build()
                                )
                        )
                        .create()
        );

        System.out.println("new GsonBuilder().setPrettyPrinting().create().toJson(excelImportResultResp) = " + new GsonBuilder().setPrettyPrinting().create().toJson(excelImportResultResp));
    }

    @Test
    public void testErrorTime() {
        String time1 = "2022-11-01 12:01:01:000";
        System.out.println("UniExcelCellDataUtil.handleTime(time1) = " + UniExcelCellDateUtil.handleTime(time1));

        String time2 = String.valueOf(System.currentTimeMillis());
        System.out.println("UniExcelCellDataUtil.handleTime(time2) = " + UniExcelCellDateUtil.handleTime(time2));

        String time3 = String.valueOf(System.currentTimeMillis() / 1000);
        System.out.println("UniExcelCellDataUtil.handleTime(time3) = " + UniExcelCellDateUtil.handleTime(time3));

        String time4 = "202211011201";
        System.out.println("UniExcelCellDataUtil.handleTime(time4) = " + UniExcelCellDateUtil.handleTime(time4));

        String time5 = "2022-11-1 12:1";
        System.out.println("UniExcelCellDataUtil.handleTime(time5) = " + UniExcelCellDateUtil.handleTime(time5));
    }

    /**
     * 通用导出任务
     */
    @Test
    public void exportTaskTest() throws Exception {
        try (FileOutputStream fileOutputStream = new FileOutputStream("./uni-excel-task-demo.xlsx")) {
            // 构建动态表头
            List<UniExcelHeaderReq> dynamicHeaderDTOList = this.buildHeaderList();
            UniExcelExportTask uniExcelExportTask = new UniExcelExportTask();
            for (int i = 1; i < 10; i++) {
                String sheetName = "通用导出《sheet》-" + i;
                // 生成表头
                uniExcelExportTask.buildHeader(sheetName, "员工汇总《标题》" + i, dynamicHeaderDTOList);
                List<JsonObject> dynamicDataDTOList = this.buildDataList();
                for (int j = 0; j < i; j++) {
                    // 添加数据
                    uniExcelExportTask.addExcelData(sheetName, dynamicDataDTOList);
                }

                if (i == 1) {
                    uniExcelExportTask.addExcelDataByPage(sheetName, new IUniExportDataHandler() {
                        @Override
                        public Long queryTotalSize() {
                            return 20000L;
                        }

                        @Override
                        public List<JsonObject> queryPageData(long pageNum, long pageSize) {
                            return buildDataList();
                        }
                    });
                }

            }
            // 导出表格
            byte[] bytes = uniExcelExportTask.exportExcel();
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void customExcel2007ReadTest() {
        try {
            UniV4Excel2007ReadService uniExcel2007Read = new UniV4Excel2007ReadService(new UniCustomSheetHandler());
            uniExcel2007Read.read("测试导入模拟数据.xlsx", -1);
        } catch (Exception e) {

        }
    }

    @Test
    public void gsonTest() {
        Gson gson = new GsonBuilder()
                // 默认整数会转换成double，更新策略还原数据类型
                .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                .disableHtmlEscaping()
                .setPrettyPrinting().create();
        Map<String, Object> params = new HashMap<>();
        params.put("f1", 1);
        params.put("f2", 20000L);
        params.put("f3", 2D);
        params.put("f4", 4.21f);
        JsonObject object = gson.fromJson(gson.toJson(params), JsonObject.class);
        System.out.println("object = " + object);
    }

}
