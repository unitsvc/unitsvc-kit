package com.unitsvc.kit.poi4.excel.read.config;

import com.unitsvc.kit.poi4.excel.read.service.model.TaskFieldModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：动态表格导入样式配置
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniExcelStyleImportConfig implements Serializable {

    private static final long serialVersionUID = -3293389394313804594L;

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

    // ------------------------------------------------------------------------------

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
    private List<ColumnConstant> globalColumnConstants;

    // ------------------------------------------------------------------------------

    /**
     * 定义是否添加唯一索引，选填
     * <p>
     * 说明：唯一索引，慎重开启，若之前数据存在索引，则会覆盖
     */
    private Boolean addExtraIndex;

    /**
     * 定义索引名称，特殊可选，默认_id
     * <p>
     * 说明：仅addExtraIndex=true生效
     */
    private String extraIndexName;

    // -------------------------------------------------------------------------------

    /**
     * 定义是否添加工作表序号，选填
     * <p>
     * 说明：2022-12-09 14:28 新增配置，会额外添加，__sheetIndex字段，表明该条数据来自哪个工作表
     */
    private Boolean addSheetIndex;

    /**
     * 定义是否读取自定义字段，选填
     * <p>
     * 说明：2022-12-09 14:29 新增配置，会读取除动态表头字段外的数据
     */
    private Boolean readCustomHeaderData;

    // ------------------------------------------------------------------------------

    /**
     * 定义所有任务字段表头信息，选填
     * <p>
     * 说明：默认为空，主要用于查找表头字段元信息。
     */
    private List<TaskFieldModel> taskFields;

    // ------------------------------------------------------------------------------
    /**
     * 定义字段常量
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnConstant implements Serializable {

        private static final long serialVersionUID = 4318191356720049862L;

        /**
         * 常量字段英文名称，必填
         */
        private String nameEn;

        /**
         * 常量字段值，必填
         * <p>
         * 说明：允许为空字符串，不能为null。约定若为null，则是字符串“null”值。
         */
        private String value;

    }

}
