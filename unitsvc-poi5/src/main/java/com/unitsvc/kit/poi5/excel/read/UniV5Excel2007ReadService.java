package com.unitsvc.kit.poi5.excel.read;

import cn.hutool.core.io.FileUtil;
import com.unitsvc.kit.poi5.excel.read.handler.UniV5XSSFSheetXMLHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.InputStream;

/**
 * 功能描述：Excel2007解析器，表格格式：xlsx
 *
 * @author : jun
 * @version : v1.1.0
 * @date : 2022/12/16 16:40
 **/
public class UniV5Excel2007ReadService {

    /**
     * 工作表处理器
     */
    private final UniV5XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler;

    /**
     * 初始化构造方法
     *
     * @param sheetContentsHandler 工作表处理器
     */
    public UniV5Excel2007ReadService(UniV5XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler) {
        this.sheetContentsHandler = sheetContentsHandler;
    }

    /**
     * 读取工作表数据
     *
     * @param path       表格路径
     * @param sheetIndex 工作表序号，序号从0开始，小于0读取所有工作表
     * @throws Exception
     */
    public void read(String path, Integer sheetIndex) throws Exception {
        // 获取文件路径
        File file = FileUtil.file(path);
        // 根据Excel获取OPCPackage对象
        try (OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ)) {
            this.parse(pkg, sheetContentsHandler, sheetIndex);
        }
    }

    /**
     * 读取工作表数据
     *
     * @param inputStream 文件流
     * @param sheetIndex  工作表序号，序号从0开始，小于0读取所有工作表
     * @throws Exception
     */
    public void read(InputStream inputStream, Integer sheetIndex) throws Exception {
        // 根据Excel获取OPCPackage对象
        try (OPCPackage pkg = OPCPackage.open(inputStream)) {
            this.parse(pkg, sheetContentsHandler, sheetIndex);
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    /**
     * 读取工作表数据
     *
     * @param path      文件路径
     * @param sheetName 工作表名称，名称为空读取所有工作表，否则读取指定工作表
     * @throws Exception
     */
    public void read(String path, String sheetName) throws Exception {
        // 获取文件路径
        File file = FileUtil.file(path);
        // 根据Excel获取OPCPackage对象
        try (OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ)) {
            this.parse(pkg, sheetContentsHandler, sheetName);
        }
    }

    /**
     * 读取工作表数据
     *
     * @param inputStream 文件流
     * @param sheetName   工作表名称，名称为空读取所有工作表，否则读取指定工作表
     * @throws Exception
     */
    public void read(InputStream inputStream, String sheetName) throws Exception {
        // 根据Excel获取OPCPackage对象
        try (OPCPackage pkg = OPCPackage.open(inputStream)) {
            this.parse(pkg, sheetContentsHandler, sheetName);
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    /**
     * 解析工作表
     *
     * @param pkg                  解析器
     * @param sheetContentsHandler 工作表处理器
     * @param importSheetName      工作表名称
     * @throws Exception
     */
    private void parse(OPCPackage pkg, UniV5XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler, String importSheetName) throws Exception {
        // 1.创建XSSFReader对象
        XSSFReader reader = new XSSFReader(pkg);
        // 2.获取SharedStringsTable对象
        SharedStrings sst = reader.getSharedStringsTable();
        // 3.获取StylesTable对象
        StylesTable styles = reader.getStylesTable();
        // 4.创建Sax的XmlReader对象
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        // 5.获取工作表迭代器
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) reader.getSheetsData();
        int sheetIndex = 0;
        // 6.逐行读取工作表
        while (sheets.hasNext()) {
            try (InputStream sheetStream = sheets.next()) {
                String sheetName = sheets.getSheetName();
                Comments sheetComments = sheets.getSheetComments();
                if (StringUtils.isNotEmpty(importSheetName)) {
                    if (!sheetName.equals(importSheetName)) {
                        continue;
                    }
                }
                // 7.设置工作表处理器
                xmlReader.setContentHandler(
                        new UniV5XSSFSheetXMLHandler(sheetIndex++, sheetName, styles, sheetComments, sst, sheetContentsHandler, new DataFormatter(), false)
                );
                InputSource sheetSource = new InputSource(sheetStream);
                // 解析工作表
                xmlReader.parse(sheetSource);
            }
        }
    }

    /**
     * 解析工作表
     *
     * @param pkg                  解析器
     * @param sheetContentsHandler 工作表处理器
     * @param importSheetIndex     工作表序号
     * @throws Exception
     */
    private void parse(OPCPackage pkg, UniV5XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler, Integer importSheetIndex) throws Exception {
        // 1.创建XSSFReader对象
        XSSFReader reader = new XSSFReader(pkg);
        // 2.获取SharedStringsTable对象
        SharedStrings sst = reader.getSharedStringsTable();
        // 3.获取StylesTable对象
        StylesTable styles = reader.getStylesTable();
        // 4.创建Sax的XmlReader对象
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        // 5.获取工作表迭代器
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) reader.getSheetsData();
        int sheetIndex = 0;
        // 6.逐行读取工作表
        while (sheets.hasNext()) {
            try (InputStream sheetStream = sheets.next()) {
                String sheetName = sheets.getSheetName();
                Comments sheetComments = sheets.getSheetComments();
                if (null != importSheetIndex && importSheetIndex >= 0) {
                    if (sheetIndex != importSheetIndex) {
                        continue;
                    }
                }
                // 7.设置工作表处理器
                xmlReader.setContentHandler(
                        new UniV5XSSFSheetXMLHandler(sheetIndex, sheetName, styles, sheetComments, sst, sheetContentsHandler, new DataFormatter(), false)
                );
                InputSource sheetSource = new InputSource(sheetStream);
                // 解析工作表
                xmlReader.parse(sheetSource);
            } finally {
                sheetIndex++;
            }
        }
    }

}
