package com.unitsvc.kit.poi4.excel.read.config;

import com.unitsvc.kit.poi4.excel.read.service.model.TaskFieldModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/7 18:19
 **/
@Data
@NoArgsConstructor
public class UniExcelStyleImportBuilder {

    /**
     * 定义是否仅仅只读可编辑字段
     * <p>
     * 说明：建议false。否则会过滤掉与业务相关的联合主键字段。
     */
    private boolean onlyReadEditField = false;

    /**
     * 定义表头所在行序号，必填，默认为序号3
     * <p>
     * 说明：请明确数据所在行。
     */
    private int readHeaderRowIndex = 3;

    // -------------------------------------------------------------------------------------

    /**
     * 校验可编辑列头中文名称是否都不存在，若不存在则抛出异常，必填
     * <p>
     * 说明：默认false，若按可编辑列动态导入则设为true
     */
    private boolean checkEditColumnNameCnIsAllEmpty = false;

    /**
     * 定义表格联合主键，选填
     * <p>
     * 说明：用于数据常规校验，若配置则会进行校验列名，若都不否存在，则会抛出异常
     */
    private List<String> checkExitsPkNameCns;

    /**
     * 定义全局列字段常量，选填
     */
    private List<UniExcelStyleImportConfig.ColumnConstant> globalColumnConstants;

    // -------------------------------------------------------------------------------------

    /**
     * 定义是否添加唯一索引，选填
     * <p>
     * 说明：唯一索引，慎重开启，若之前数据存在索引，则会覆盖
     */
    private boolean addExtraIndex = false;

    /**
     * 定义索引名称，特殊可选，默认_id
     * <p>
     * 说明：仅addExtraIndex=true必填
     */
    private String extraIndexName = "_id";

    // -------------------------------------------------------------------------------------

    /**
     * 定义是否添加工作表序号，选填
     * <p>
     * 说明：2022-12-09 14:28 新增配置，会额外添加，__sheetIndex字段，表明该条数据来自哪个工作表
     */
    private boolean addSheetIndex = false;

    /**
     * 定义是否读取自定义字段，选填
     * <p>
     * 说明：2022-12-09 14:29 新增配置，会读取除动态表头字段外的数据
     */
    private boolean readCustomHeaderData = false;

    // -------------------------------------------------------------------------------------

    /**
     * 定义所有任务字段表头信息，选填
     * <p>
     * 说明：默认为空，主要用于查找表头字段元信息。
     */
    private List<TaskFieldModel> taskFields;

    // -------------------------------------------------------------------------------------

    /**
     * 定义所有任务字段表头信息，选填
     *
     * @param taskFields 所有任务字段元信息
     * @return
     */
    public UniExcelStyleImportBuilder taskFields(List<TaskFieldModel> taskFields) {
        this.taskFields = taskFields;
        return this;
    }

    /**
     * 定义是否添加工作表序号，选填
     * <p>
     * 说明：默认false，新增配置，会额外添加，__sheetIndex字段，表明该条数据来自哪个工作表
     *
     * @param addSheetIndex true|新增__sheetIndex，false|无操作
     * @return
     */
    public UniExcelStyleImportBuilder addSheetIndex(boolean addSheetIndex) {
        this.addSheetIndex = addSheetIndex;
        return this;
    }

    /**
     * 定义是否读取自定义字段，选填
     * <p>
     * 说明：默认false，用于判断是否读取动态表头外的额外字段
     *
     * @param readCustomHeaderData true|读取自定义字段、false|无操作
     * @return
     */
    public UniExcelStyleImportBuilder readCustomHeaderData(boolean readCustomHeaderData) {
        this.readCustomHeaderData = readCustomHeaderData;
        return this;
    }

    /**
     * 定义是否仅仅只读可编辑字段
     *
     * @param onlyReadEditField true|读所有字段、false|读可编辑字段
     * @return
     */
    public UniExcelStyleImportBuilder onlyReadEditField(boolean onlyReadEditField) {
        this.onlyReadEditField = onlyReadEditField;
        return this;
    }

    /**
     * 定义表头所在行序号，必填，
     * <p>
     * 说明：默认为序号3，双层表头+标题
     *
     * @param readHeaderRowIndex 单行表头->1，双层表头->2，以实际表头所在列为准，从序号0开始
     * @return
     */
    public UniExcelStyleImportBuilder readHeaderRowIndex(int readHeaderRowIndex) {
        this.readHeaderRowIndex = readHeaderRowIndex;
        return this;
    }

    /**
     * 校验可编辑列头中文名称是否都不存在，若不存在则抛出异常，必填
     * <p>
     * 说明：默认false不校验
     *
     * @param checkEditColumnNameCnIsAllEmpty true|校验可编辑字段列是否都不存在、false|不校验
     * @return
     */
    public UniExcelStyleImportBuilder checkEditColumnNameCnIsAllEmpty(boolean checkEditColumnNameCnIsAllEmpty) {
        this.checkEditColumnNameCnIsAllEmpty = checkEditColumnNameCnIsAllEmpty;
        return this;
    }

    /**
     * 定义表格联合主键，选填
     *
     * @param checkExitsPkNameCns 需要校验的联合主键中文名称
     * @return
     */
    public UniExcelStyleImportBuilder checkExitsPkNameCns(List<String> checkExitsPkNameCns) {
        this.checkExitsPkNameCns = checkExitsPkNameCns;
        return this;
    }

    /**
     * 定义全局列字段常量，选填
     *
     * @param globalColumnConstants 全局常量字段
     * @return
     */
    public UniExcelStyleImportBuilder globalColumnConstants(List<UniExcelStyleImportConfig.ColumnConstant> globalColumnConstants) {
        this.globalColumnConstants = globalColumnConstants;
        return this;
    }

    /**
     * 定义是否添加主键索引，选填
     *
     * @param addExtraIndex true|添加唯一索引，false|无操作
     * @return
     */
    public UniExcelStyleImportBuilder addExtraIndex(boolean addExtraIndex) {
        this.addExtraIndex = addExtraIndex;
        return this;
    }

    /**
     * 定义索引名称，特殊可选，默认_id
     *
     * @param extraIndexName 索引名称，默认_id
     * @return
     */
    public UniExcelStyleImportBuilder extraIndexName(String extraIndexName) {
        this.extraIndexName = extraIndexName;
        return this;
    }

    /**
     * 构建通用导入样式配置类
     *
     * @return
     */
    public static UniExcelStyleImportBuilder build() {
        return new UniExcelStyleImportBuilder();
    }

    /**
     * 创建通用导入配置
     *
     * @return
     */
    public UniExcelStyleImportConfig create() {
        UniExcelStyleImportConfig uniExcelStyleImportConfig = new UniExcelStyleImportConfig();
        uniExcelStyleImportConfig.setOnlyReadEditField(this.onlyReadEditField);
        uniExcelStyleImportConfig.setReadHeaderRowIndex(this.readHeaderRowIndex);
        uniExcelStyleImportConfig.setCheckEditColumnNameCnIsAllEmpty(this.checkEditColumnNameCnIsAllEmpty);
        uniExcelStyleImportConfig.setCheckExitsPkNameCns(this.checkExitsPkNameCns);
        uniExcelStyleImportConfig.setGlobalColumnConstants(this.globalColumnConstants);
        uniExcelStyleImportConfig.setAddExtraIndex(this.addExtraIndex);
        uniExcelStyleImportConfig.setExtraIndexName(this.extraIndexName);
        uniExcelStyleImportConfig.setAddSheetIndex(this.addSheetIndex);
        uniExcelStyleImportConfig.setReadCustomHeaderData(this.readCustomHeaderData);
        uniExcelStyleImportConfig.setTaskFields(this.taskFields);
        return uniExcelStyleImportConfig;
    }

}
