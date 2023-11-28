package com.unitsvc.kit.poi4.excel.common;

import com.google.gson.JsonObject;
import com.unitsvc.kit.poi4.excel.write.style.IUniExcelCustomStyle;
import com.unitsvc.kit.poi4.excel.write.config.UniExcelStyleExportConfig;
import com.unitsvc.kit.poi4.excel.read.config.UniExcelStyleImportConfig;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;

import java.io.InputStream;
import java.util.List;

/**
 * 通用导入导出接口定义
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/13 16:58
 */
public interface IUniExcelService {

    /**
     * 导出动态表头的工作簿，规定：表头占四行，其中第一、二行是表头名称且合并单元格，第三、四行为动态表头数据若包含字段分组则合并单元格。若进行导入，则从第四行开始读取。
     * <p>
     * 备注：2022-12-05 11:09 逐步废弃，原因：功能有限。请用UniExcelExportTask替代。
     *
     * @param sheetName            工作表名称
     * @param titleName            标题名称
     * @param dynamicHeaderDTOList 动态表头集合
     * @param dynamicDataDTOList   动态数据集合
     * @return 返回文件字节
     */
    @Deprecated
    byte[] exportDynamicExcel(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, List<JsonObject> dynamicDataDTOList);

    /**
     * 导出动态表头的工作簿，规定：表头占四行，其中第一、二行是表头名称且合并单元格，第三、四行为动态表头数据若包含字段分组则合并单元格。若进行导入，则从第四行开始读取。
     * <p>
     * 备注：2022-12-05 11:09 逐步废弃，原因：功能有限。请用UniExcelExportTask替代。
     *
     * @param sheetName                 工作表名称
     * @param titleName                 标题名称
     * @param dynamicHeaderDTOList      动态表头集合
     * @param dynamicDataDTOList        动态数据集合
     * @param uniExcelStyleExportConfig 自定义表格配置
     * @return 返回文件字节
     */
    @Deprecated
    byte[] exportDynamicExcel(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, List<JsonObject> dynamicDataDTOList, UniExcelStyleExportConfig uniExcelStyleExportConfig);

    /**
     * 自定义样式，导出动态表头的工作簿，规定：默认表头占四行，其中第一、二行是表头名称且合并单元格，第三、四行为动态表头数据若包含字段分组则合并单元格。若进行导入，则默认从第四行开始读取。
     * <p>
     * 备注：2022-12-05 11:09 逐步废弃，原因：功能有限。请用UniExcelExportTask替代。
     *
     * @param sheetName                 工作表名称
     * @param titleName                 标题名称
     * @param dynamicHeaderDTOList      动态表头集合
     * @param dynamicDataDTOList        动态数据集合
     * @param uniExcelCustomStyle       自定义表格样式
     * @param uniExcelStyleExportConfig 自定义表格配置
     * @return 返回文件字节
     */
    @Deprecated
    byte[] exportDynamicExcel(String sheetName, String titleName, List<UniExcelHeaderReq> dynamicHeaderDTOList, List<JsonObject> dynamicDataDTOList,
                              IUniExcelCustomStyle uniExcelCustomStyle, UniExcelStyleExportConfig uniExcelStyleExportConfig);

    /**
     * 导入动态Excel表格并返回解析后的实体类集合
     * <p>
     * 备注：2022-12-07 16:37 逐步废弃，原因：功能有限。请用UniExcelImportTask替代。
     *
     * @param excelFileInputStream 待导入的Excel文件输入流
     * @param dynamicHeaderDTOList 动态表头集合
     * @return 返回表格JsonObject数据集合
     */
    @Deprecated
    List<JsonObject> importDynamicExcel(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList);

    /**
     * 自定义导入配置，导入动态Excel表格并返回解析后的实体类集合
     * <p>
     * 备注：2022-12-07 16:37 逐步废弃，原因：功能有限。请用UniExcelImportTask替代。
     *
     * @param excelFileInputStream      待导入的Excel文件输入流
     * @param dynamicHeaderDTOList      动态表头集合
     * @param uniExcelStyleImportConfig 导入样式配置
     * @return 返回表格JsonObject数据集合
     */
    @Deprecated
    List<JsonObject> importDynamicExcel(InputStream excelFileInputStream, List<UniExcelHeaderReq> dynamicHeaderDTOList,
                                        UniExcelStyleImportConfig uniExcelStyleImportConfig);

}