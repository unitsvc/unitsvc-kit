package com.unitsvc.kit.core.field.utils;

import cn.hutool.core.io.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 功能描述：文件工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/3 17:06
 **/
public class UniFileUtil {

    public static final Integer K_SIZE = 1024;

    public static final Integer M_SIZE = 1048576;

    public static final Integer G_SIZE = 1073741824;

    public static final String B = "B";

    public static final String K = "K";

    public static final String M = "M";

    public static final String G = "G";

    /**
     * 将字节数据转为带单位的字符串
     *
     * @param fileSize 文件字节大小
     * @return
     */
    public static String formatFileSize(Long fileSize) {
        String fileSizeStr = "";
        if (Objects.isNull(fileSize)) {
            return fileSizeStr;
        }
        if (fileSize == 0L) {
            return "0".concat(B);
        }
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileSize < K_SIZE) {
            fileSizeStr = df.format((double) fileSize) + B;
        } else if (fileSize < M_SIZE) {
            fileSizeStr = df.format((double) fileSize / K_SIZE) + K;
        } else if (fileSize < G_SIZE) {
            fileSizeStr = df.format((double) fileSize / M_SIZE) + M;
        } else {
            fileSizeStr = df.format((double) fileSize / G_SIZE) + G;
        }
        return fileSizeStr;
    }

    /**
     * 文件转换成二进制数组
     *
     * @param filename 文件路径名称
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {
        File f = FileUtil.file(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        // 当文件大于1.9G时，数组装不下
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length())) {
            BufferedInputStream in = null;
            in = new BufferedInputStream(Files.newInputStream(f.toPath()));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
