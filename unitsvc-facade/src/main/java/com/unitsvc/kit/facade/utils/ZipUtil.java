package com.unitsvc.kit.facade.utils;

import com.jpardus.spider.sccs.Log;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 功能描述：字符串压缩工具类
 * <p>
 * 说明：主要用于序列化数据
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/26 15:13
 **/
public class ZipUtil {

    /**
     * 压缩字符串
     *
     * @param unzipString 原始字符串
     * @return 压缩后字符串
     */
    public static String zipString(String unzipString) {
        // 使用指定的压缩级别创建一个新的压缩器。
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256)) {
            // 设置压缩输入数据。
            deflater.setInput(unzipString.getBytes());
            // 当被调用时，表示压缩应该以输入缓冲区的当前内容结束。
            deflater.finish();
            final byte[] bytes = new byte[256];
            while (!deflater.finished()) {
                // 压缩输入数据并用压缩数据填充指定的缓冲区。
                int length = deflater.deflate(bytes);
                outputStream.write(bytes, 0, length);
            }
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            Log.error(String.format("【zip字符串压缩异常】：%s", e.getMessage()), e);
            return null;
        } finally {
            // 关闭压缩器并丢弃任何未处理的输入。
            deflater.end();
        }
    }

    /**
     * 解压缩字符串
     *
     * @param zipString 压缩后字符串
     * @return 原始字符串
     */
    public static String unzipString(String zipString) {
        byte[] decode = Base64.getDecoder().decode(zipString);
        Inflater inflater = new Inflater();
        // 设置解压缩的输入数据。
        inflater.setInput(decode);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256)) {
            final byte[] bytes = new byte[256];
            // finished() 如果已到达压缩数据流的末尾，则返回true。
            while (!inflater.finished()) {
                //将字节解压缩到指定的缓冲区中。
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
            return outputStream.toString();
        } catch (Exception e) {
            Log.error(String.format("【unzip字符串解压异常】：%s", e.getMessage()), e);
            return null;
        } finally {
            // 关闭解压缩器并丢弃任何未处理的输入。
            inflater.end();
        }
    }

}
