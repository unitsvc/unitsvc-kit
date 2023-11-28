package com.unitsvc.kit.core.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：文件响应头枚举
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/7 11:39
 **/
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public enum UniFileHeaderEnum {

    /**
     * 纯文本格式
     */
    TEXT("txt", "text/plain", "纯文本格式"),

    /**
     * jpg图片格式
     */
    IMAGE_JPEG("jpg", "image/jpeg", "jpg图片格式"),

    /**
     * png图片格式
     */
    IMAGE_PNG("png", "image/png", "png图片格式"),

    /**
     * gif图片格式
     */
    IMAGE_GIF("gif", "image/gif", "gif图片格式"),

    /**
     * pdf格式
     */
    PDF("pdf", "application/pdf", "pdf格式"),

    /**
     * xml数据格式
     */
    XML("xml", "application/xml", "xml数据格式"),

    /**
     * zip压缩文件格式
     */
    ZIP("zip", "application/zip", "zip压缩文件格式"),

    /**
     * ppt演示文档格式
     */
    OFFICE_PPT("ppt", "application/vnd.ms-powerpoint", "ppt演示文档格式"),

    /**
     * ppt演示文档格式
     */
    OFFICE_POT("pot", "application/vnd.ms-powerpoint", "ppt演示文档格式"),

    /**
     * ppt演示文档格式
     */
    OFFICE_PPS("pps", "application/vnd.ms-powerpoint", "ppt演示文档格式"),

    /**
     * ppt演示文档格式
     */
    OFFICE_PPA("ppa", "application/vnd.ms-powerpoint", "ppt演示文档格式"),

    /**
     * word文档格式
     */
    WORD_DOC("doc", "application/msword", "word文档格式"),

    /**
     * word文档格式
     */
    WORD_DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "word文档格式"),

    /**
     * excel表格格式
     */
    EXCEL_XLS("xls", "application/vnd.ms-excel", "excel表格格式"),

    /**
     * excel表格格式
     */
    EXCEL_XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "excel表格格式"),

    /**
     * 二进制数据流
     */
    STREAM_BIN("", "application/octet-stream", "二进制数据流");

    /**
     * 文件扩展名称
     */
    private final String fileExtName;

    /**
     * 内容类型
     */
    private final String contentType;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据文件扩展名称查找文件内容类型
     * <p>
     * 说明：默认返回二进制类型
     *
     * @param fileExtName 文件扩展名称，示例：txt、ppt、xlsx
     * @return
     */
    public static UniFileHeaderEnum getContentTypeByFileExt(String fileExtName) {
        for (UniFileHeaderEnum contentTypeEnum : UniFileHeaderEnum.values()) {
            if (contentTypeEnum.fileExtName.equals(fileExtName.toLowerCase())) {
                return contentTypeEnum;
            }
        }
        return UniFileHeaderEnum.STREAM_BIN;
    }

}
