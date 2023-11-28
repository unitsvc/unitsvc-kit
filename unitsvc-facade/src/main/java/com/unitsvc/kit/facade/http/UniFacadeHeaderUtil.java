package com.unitsvc.kit.facade.http;

import com.unitsvc.kit.core.file.enums.UniHttpHeaderEnum;
import com.jpardus.spider.facade.server.Request;
import com.jpardus.spider.facade.server.Response;

/**
 * 功能描述：http头信息处理工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/3/1 18:22
 **/
public class UniFacadeHeaderUtil {

    /**
     * 获取form-data表单文件名称
     *
     * @param formDataFileName 请求参数标识，示例：file
     * @return 示例：文件上传.xlsx
     */
    public static String requestFormDataFileName(String formDataFileName) {
        return Request.getParameter(formDataFileName);
    }

    /**
     * 设置浏览器响应头，文件预览
     *
     * @param fileName 文件完整名称包含后缀，示例：文件预览.pdf
     */
    public static void setHttpFileHeaderInline(String fileName) {
        // 暴露响应头
        Response.header("Access-Control-Expose-Headers", "Content-Disposition");
        // 设置响应内容格式
        Response.header(UniHttpHeaderEnum.HEADER_CONTENT_TYPE.getNameEn(), UniHttpHeaderEnum.buildContentTypeValue(fileName));
        // 设置处理响应内容方式：浏览器预览，仅支持可预览类型
        Response.header(UniHttpHeaderEnum.HEADER_CONTENT_DISPOSITION.getNameEn(), UniHttpHeaderEnum.buildFileInlineHeaderValue(fileName));
    }

    /**
     * 设置浏览器响应头，文件下载
     *
     * @param fileName 文件完整名称包含后缀，示例：文件下载.xlsx
     */
    public static void setHttpFileHeaderAttachment(String fileName) {
        // 暴露响应头
        Response.header("Access-Control-Expose-Headers", "Content-Disposition");
        // 设置响应内容格式
        Response.header(UniHttpHeaderEnum.HEADER_CONTENT_TYPE.getNameEn(), UniHttpHeaderEnum.buildContentTypeValue(fileName));
        // 设置处理响应内容：浏览器下载
        Response.header(UniHttpHeaderEnum.HEADER_CONTENT_DISPOSITION.getNameEn(), UniHttpHeaderEnum.buildFileAttachmentHeaderValue(fileName));
    }

    /**
     * 获取浏览器请求头参数
     *
     * @param headerName 请求头名称
     * @return
     */
    public static String requestHeader(String headerName) {
        return Request.getHeaderFirst(headerName);
    }

}
