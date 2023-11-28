package com.unitsvc.kit.core.file.enums;

import cn.hutool.core.io.file.FileNameUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URLEncoder;

/**
 * 功能描述：文件下载http响应头
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 13:40
 **/
@Getter
@AllArgsConstructor
public enum UniHttpHeaderEnum {

    /**
     * 响应内容格式
     */
    HEADER_CONTENT_TYPE("content-type", "响应内容格式"),

    /**
     * 处理响应内容
     */
    HEADER_CONTENT_DISPOSITION("Content-Disposition", "处理响应内容"),
    ;

    /**
     * 英文标识
     */
    private final String nameEn;

    /**
     * 中文标识
     */
    private final String nameCn;

    /**
     * 构建内容处理的响应头内容值
     * <p>
     * 说明：
     * <p>
     * 1、浏览器预览，支持可预览的类型，若不可预览，则默认行为为下载
     * <p>
     * 2、仅适用于Content-Disposition的响应头
     *
     * @param name 文件完整名称，包含后缀，示例：文件预览.pdf
     * @return
     */
    public static String buildFileInlineHeaderValue(String name) {
        // 默认使用原始名称
        String encodeName = name;
        try {
            // 名称编码
            encodeName = URLEncoder.encode(name, "UTF-8");
        } catch (Exception e) {
            // pass
            // 若编码失败，不做任何处理
        }
        return String.format("inline; filename=%s; filename*=UTF-8''%s", encodeName, encodeName);
    }

    /**
     * 构建内容处理的响应头内容值
     * <p>
     * 说明：
     * <p>
     * 1、浏览器下载
     * <p>
     * 2、仅适用于Content-Disposition的响应头
     *
     * @param name 文件完整名称，包含后缀，示例：文件下载.xlsx
     * @return
     */
    public static String buildFileAttachmentHeaderValue(String name) {
        // 默认使用原始名称
        String encodeName = name;
        try {
            // 名称编码
            encodeName = URLEncoder.encode(name, "UTF-8");
        } catch (Exception e) {
            // pass
            // 若编码失败，不做任何处理
        }
        return String.format("attachment; filename=%s; filename*=UTF-8''%s", encodeName, encodeName);
    }

    /**
     * 构建响应内容的响应头内容值
     * <p>
     * 说明：
     * <p>
     * 1、仅适用于content-type的响应头
     *
     * @param name 文件完整名称，包含后缀，示例：文件下载.xlsx
     * @return
     */
    public static String buildContentTypeValue(String name) {
        // 获取扩展名称
        String extName = FileNameUtil.extName(name);
        return String.format("%s; charset=utf8", UniFileHeaderEnum.getContentTypeByFileExt(extName).getContentType());
    }

}
