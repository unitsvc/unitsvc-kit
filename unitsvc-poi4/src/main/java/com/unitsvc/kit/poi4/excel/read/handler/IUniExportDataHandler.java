package com.unitsvc.kit.poi4.excel.read.handler;

import com.google.gson.JsonObject;
import com.unitsvc.kit.poi4.excel.write.UniExcelExportTask;

import java.util.List;

/**
 * 功能描述：分页处理导出数据
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/11/9 17:18
 **/
public interface IUniExportDataHandler {

    /**
     * 获取页数，默认每页2000条数据
     *
     * @return
     */
    default Long getPageSize() {
        return 20000L;
    }

    /**
     * 查询总条数
     * <p>
     * 警告：此处实现查询总数方法。
     * </p>
     *
     * @return
     */
    Long queryTotalSize();

    /**
     * 查询分页数据，
     * <p>
     * 警告：此处实现分页查询方法，不可直接传入数据集合。因为该数据查询后会被销毁。
     * </p>
     *
     * @param pageNum  页号
     * @param pageSize 页数
     * @return
     */
    List<JsonObject> queryPageData(long pageNum, long pageSize);

    /**
     * 写入表格数据
     *
     * @param sheetName          工作表名称
     * @param uniExcelExportTask 通用导出任务
     */
    default void writeData(String sheetName, UniExcelExportTask uniExcelExportTask) {
        // 查询总记录数
        Long totalSize = this.queryTotalSize();
        // 获取每页条数
        Long pageSize = this.getPageSize();
        // 计算总页数
        long totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        // 循环写入工作表数据
        for (long pageNum = 1L; pageNum <= totalPage; pageNum++) {
            // 查询分页数据
            List<JsonObject> pageData = this.queryPageData(pageNum, pageSize);
            // 写入工作表
            uniExcelExportTask.addExcelData(sheetName, pageData);
            // 清空数据
            pageData.clear();
            pageData = null;
        }
    }

}
