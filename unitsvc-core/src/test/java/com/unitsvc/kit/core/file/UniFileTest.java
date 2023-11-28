package com.unitsvc.kit.core.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unitsvc.kit.core.file.enums.UniHttpHeaderEnum;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：文件测试类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 14:01
 **/
public class UniFileTest {

    @Test
    public void fileHeaderTest() {

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        Map<String, String> header1 = new HashMap<>(2);
        // 必填响应头
        header1.put(UniHttpHeaderEnum.HEADER_CONTENT_TYPE.getNameEn(), UniHttpHeaderEnum.buildContentTypeValue("文件预留.pdf"));
        // 必填响应头，二选一
        header1.put(UniHttpHeaderEnum.HEADER_CONTENT_DISPOSITION.getNameEn(), UniHttpHeaderEnum.buildFileInlineHeaderValue("文件预留.pdf"));
        System.out.println("gson.toJson(header1) = " + gson.toJson(header1));

        Map<String, String> header2 = new HashMap<>(2);
        // 必填响应头
        header2.put(UniHttpHeaderEnum.HEADER_CONTENT_TYPE.getNameEn(), UniHttpHeaderEnum.buildContentTypeValue("文件下载.xlsx"));
        // 必填响应头，二选一
        header2.put(UniHttpHeaderEnum.HEADER_CONTENT_DISPOSITION.getNameEn(), UniHttpHeaderEnum.buildFileAttachmentHeaderValue("文件下载.xlsx"));
        System.out.println("gson.toJson(header2) = " + gson.toJson(header2));

    }

}
