package com.unitsvc.kit.poi5.excel.read.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.Comments;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static org.apache.poi.xssf.usermodel.XSSFRelation.NS_SPREADSHEETML;

/**
 * 说明：重新v5版本，sax模式导入表格方法，实现公式读取
 *
 * 备注：v5版本不兼容v4版本
 * <p>
 * This class handles the streaming processing of a sheet#.xml
 * sheet part of a XSSF .xlsx file, and generates
 * row and cell events for it.
 * <p>
 * This allows to build functionality which reads huge files
 * without needing large amounts of main memory.
 * <p>
 * See {@link XSSFSheetXMLHandler.SheetContentsHandler} for the interface that
 * you need to implement for reading information from a file.
 */
@SuppressWarnings("all")
public class UniV5XSSFSheetXMLHandler extends DefaultHandler {

    private static final Logger LOG = LogManager.getLogger(UniV5XSSFSheetXMLHandler.class);

    /**
     * These are the different kinds of cells we support.
     * We keep track of the current one between
     * the start and end.
     */
    public enum xssfDataType {
        BOOLEAN,
        ERROR,
        FORMULA,
        /**
         * 兼容模式，富文本格式
         */
        INLINE_STRING,
        /**
         * 标准模式
         */
        SST_STRING,
        /**
         * 数字、时间
         */
        NUMBER,
    }

    // -------------------------------------------- 扩展字段 -------------------------------------------------------------
    /**
     * 单元格样式
     */
    private XSSFCellStyle customExtStyle;
    /**
     * 公式字符串
     */
    private String customExtFormula;
    /**
     * 单元格原始值
     */
    private String customExtOriginValue;
    /**
     * 工作表序号，从0开始
     */
    private Integer customExtSheetIndex;
    /**
     * 工作表名称
     */
    private String customExtSheetName;
    /**
     * 列序号，从0开始
     */
    private Integer customExtColIndex;
    /**
     * 列标题，示例：A、B、AA、AB
     */
    private String customExtColTitle;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Table with the styles used for formatting
     */
    private final Styles stylesTable;

    /**
     * Table with cell comments
     */
    private final Comments comments;

    /**
     * Read only access to the shared strings table, for looking
     * up (most) string cell's contents
     */
    private final SharedStrings sharedStringsTable;

    /**
     * Where our text is going
     */
    private final SheetContentsHandler output;

    // Set when V start element is seen
    private boolean vIsOpen;
    // Set when F start element is seen
    private boolean fIsOpen;
    // Set when an Inline String "is" is seen
    private boolean isIsOpen;
    // Set when a header/footer element is seen
    private boolean hfIsOpen;

    // Set when cell start element is seen;
    // used when cell close element is seen.
    private xssfDataType nextDataType;

    // Used to format numeric cell values.
    private short formatIndex;
    private String formatString;
    private final DataFormatter formatter;
    private int rowNum;
    private int nextRowNum;      // some sheets do not have rowNums, Excel can read them so we should try to handle them correctly as well
    private String cellRef;
    private final boolean formulasNotResults;

    // Gathers characters as they are seen.
    private final StringBuilder value = new StringBuilder(64);
    private final StringBuilder formula = new StringBuilder(64);
    private final StringBuilder headerFooter = new StringBuilder(64);

    private Queue<CellAddress> commentCellRefs;

    /**
     * 重写构造方法，传入工作表信息
     *
     * @param styles  Table of styles
     * @param strings Table of shared strings
     */
    public UniV5XSSFSheetXMLHandler(
            Integer sheetIndex,
            String sheetName,
            Styles styles,
            Comments comments,
            SharedStrings strings,
            SheetContentsHandler sheetContentsHandler,
            DataFormatter dataFormatter,
            boolean formulasNotResults) {
        // ---------------------- 新增字段 -------------------------
        this.customExtSheetIndex = sheetIndex;
        this.customExtSheetName = sheetName;
        // -------------------------------------------------------
        this.stylesTable = styles;
        this.comments = comments;
        this.sharedStringsTable = strings;
        this.output = sheetContentsHandler;
        this.formulasNotResults = formulasNotResults;
        this.nextDataType = xssfDataType.NUMBER;
        this.formatter = dataFormatter;
        init(comments);
    }

    /**
     * Accepts objects needed while parsing.
     *
     * @param styles  Table of styles
     * @param strings Table of shared strings
     */
    public UniV5XSSFSheetXMLHandler(
            Styles styles,
            Comments comments,
            SharedStrings strings,
            SheetContentsHandler sheetContentsHandler,
            DataFormatter dataFormatter,
            boolean formulasNotResults) {
        this.stylesTable = styles;
        this.comments = comments;
        this.sharedStringsTable = strings;
        this.output = sheetContentsHandler;
        this.formulasNotResults = formulasNotResults;
        this.nextDataType = xssfDataType.NUMBER;
        this.formatter = dataFormatter;
        init(comments);
    }

    /**
     * Accepts objects needed while parsing.
     *
     * @param styles  Table of styles
     * @param strings Table of shared strings
     */
    public UniV5XSSFSheetXMLHandler(
            Styles styles,
            SharedStrings strings,
            SheetContentsHandler sheetContentsHandler,
            DataFormatter dataFormatter,
            boolean formulasNotResults) {
        this(styles, null, strings, sheetContentsHandler, dataFormatter, formulasNotResults);
    }

    /**
     * Accepts objects needed while parsing.
     *
     * @param styles  Table of styles
     * @param strings Table of shared strings
     */
    public UniV5XSSFSheetXMLHandler(
            Styles styles,
            SharedStrings strings,
            SheetContentsHandler sheetContentsHandler,
            boolean formulasNotResults) {
        this(styles, strings, sheetContentsHandler, new DataFormatter(), formulasNotResults);
    }

    private void init(Comments commentsTable) {
        if (commentsTable != null) {
            commentCellRefs = new LinkedList<>();
            for (Iterator<CellAddress> iter = commentsTable.getCellAddresses(); iter.hasNext(); ) {
                commentCellRefs.add(iter.next());
            }
        }
    }

    private boolean isTextTag(String name) {
        if ("v".equals(name)) {
            // Easy, normal v text tag
            return true;
        }
        if ("inlineStr".equals(name)) {
            // Easy inline string
            return true;
        }
        if ("t".equals(name) && isIsOpen) {
            // Inline string <is><t>...</t></is> pair
            return true;
        }
        // It isn't a text tag
        return false;
    }

    @Override
    @SuppressWarnings("unused")
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (uri != null && !uri.equals(NS_SPREADSHEETML)) {
            return;
        }

        if (isTextTag(localName)) {
            vIsOpen = true;
            // Clear contents cache
            if (!isIsOpen) {
                value.setLength(0);
            }
        } else if ("is".equals(localName)) {
            // Inline string outer tag
            isIsOpen = true;
        } else if ("f".equals(localName)) {
            // Clear contents cache
            formula.setLength(0);

            // Mark us as being a formula if not already
            if (nextDataType == xssfDataType.NUMBER) {
                nextDataType = xssfDataType.FORMULA;
            }

            // Decide where to get the formula string from
            String type = attributes.getValue("t");
            if (type != null && type.equals("shared")) {
                // Is it the one that defines the shared, or uses it?
                String ref = attributes.getValue("ref");
                String si = attributes.getValue("si");

                if (ref != null) {
                    // This one defines it
                    // TODO Save it somewhere
                    fIsOpen = true;
                } else {
                    // This one uses a shared formula
                    // TODO Retrieve the shared formula and tweak it to
                    //  match the current cell
                    if (formulasNotResults) {
                        LOG.atWarn().log("shared formulas not yet supported!");
                    } /*else {
                   // It's a shared formula, so we can't get at the formula string yet
                   // However, they don't care about the formula string, so that's ok!
                }*/
                }
            } else {
                fIsOpen = true;
            }
        } else if ("oddHeader".equals(localName) || "evenHeader".equals(localName) ||
                "firstHeader".equals(localName) || "firstFooter".equals(localName) ||
                "oddFooter".equals(localName) || "evenFooter".equals(localName)) {
            hfIsOpen = true;
            // Clear contents cache
            headerFooter.setLength(0);
        } else if ("row".equals(localName)) {
            String rowNumStr = attributes.getValue("r");
            if (rowNumStr != null) {
                rowNum = Integer.parseInt(rowNumStr) - 1;
            } else {
                rowNum = nextRowNum;
            }
            output.startRow(rowNum, customExtSheetIndex, customExtSheetName);
        }
        // c => cell
        else if ("c".equals(localName)) {
            // ----------------------------------------------- 数据初始化 ------------------------------------------------
            // 单元格公式初始化
            this.customExtFormula = null;
            // 单元格原始值初始化
            this.customExtOriginValue = null;
            // 单元格列序号初始化
            this.customExtColIndex = null;
            // 单元格列标题初始化
            this.customExtColTitle = null;
            // ---------------------------------------------------------------------------------------------------------
            // 单元格样式初始化
            this.customExtStyle = null;
            // ---------------------------------------------------------------------------------------------------------
            // Set up defaults.
            this.nextDataType = xssfDataType.NUMBER;
            this.formatIndex = -1;
            this.formatString = null;
            cellRef = attributes.getValue("r");
            String cellType = attributes.getValue("t");
            String cellStyleStr = attributes.getValue("s");

            // ------------------------------------------ 样式提前获取，否则样式丢失 ----------------------------------------
            // 备注：若单元格格式与数值不匹配，获取到的则是错误数据。
            XSSFCellStyle style = null;
            if (stylesTable != null) {
                if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    style = stylesTable.getStyleAt(styleIndex);
                } else if (stylesTable.getNumCellStyles() > 0) {
                    style = stylesTable.getStyleAt(0);
                }
            }
            // 设置当前单元格样式
            this.customExtStyle = style;
            // ---------------------------------------------------------------------------------------------------------

            if ("b".equals(cellType)) {
                nextDataType = xssfDataType.BOOLEAN;
            } else if ("e".equals(cellType)) {
                nextDataType = xssfDataType.ERROR;
            } else if ("inlineStr".equals(cellType)) {
                nextDataType = xssfDataType.INLINE_STRING;
            } else if ("s".equals(cellType)) {
                nextDataType = xssfDataType.SST_STRING;
            } else if ("str".equals(cellType)) {
                nextDataType = xssfDataType.FORMULA;
            } else {
                // Number, but almost certainly with a special style or format
                if (style != null) {
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (uri != null && !uri.equals(NS_SPREADSHEETML)) {
            return;
        }

        // v => contents of a cell
        if (isTextTag(localName)) {
            vIsOpen = false;

            if (!isIsOpen) {
                outputCell();
                value.setLength(0);
            }
        } else if ("f".equals(localName)) {
            fIsOpen = false;
        } else if ("is".equals(localName)) {
            isIsOpen = false;
            outputCell();
            value.setLength(0);
        } else if ("row".equals(localName)) {
            // Handle any "missing" cells which had comments attached
            checkForEmptyCellComments(EmptyCellCommentsCheckType.END_OF_ROW);

            // Finish up the row
            output.endRow(rowNum, customExtSheetIndex, customExtSheetName);

            // some sheets do not have rowNum set in the XML, Excel can read them so we should try to read them as well
            nextRowNum = rowNum + 1;
        } else if ("sheetData".equals(localName)) {
            // Handle any "missing" cells which had comments attached
            checkForEmptyCellComments(EmptyCellCommentsCheckType.END_OF_SHEET_DATA);

            // indicate that this sheet is now done
            output.endSheet(customExtSheetIndex, customExtSheetName);
        } else if ("oddHeader".equals(localName) || "evenHeader".equals(localName) ||
                "firstHeader".equals(localName)) {
            hfIsOpen = false;
            output.headerFooter(headerFooter.toString(), true, localName, customExtSheetIndex, customExtSheetName);
        } else if ("oddFooter".equals(localName) || "evenFooter".equals(localName) ||
                "firstFooter".equals(localName)) {
            hfIsOpen = false;
            output.headerFooter(headerFooter.toString(), false, localName, customExtSheetIndex, customExtSheetName);
        }
    }

    /**
     * Captures characters only if a suitable element is open.
     * Originally was just "v"; extended for inlineStr also.
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (vIsOpen) {
            value.append(ch, start, length);
        }
        if (fIsOpen) {
            formula.append(ch, start, length);
        }
        if (hfIsOpen) {
            headerFooter.append(ch, start, length);
        }
    }

    private void outputCell() {
        String thisStr = null;

        // Process the value contents as required, now we have it all
        switch (nextDataType) {
            // 说明，单元格对应的索引值或是内容值
            // 1.单元格类型是布尔值、错误、公式，索引—>内容值
            // 2.单元格类型是字符串、INLINE_STRING、数字、日期，索引->索引值
            case BOOLEAN:
                // ---------------- 原始值 ----------------
                this.customExtOriginValue = value.toString();
                // ---------------------------------------
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;

            case ERROR:
                // ---------------- 原始值 ----------------
                // TODO 待测试
                this.customExtOriginValue = value.toString();
                // ---------------------------------------
                thisStr = "ERROR:" + value;
                break;

            case FORMULA:
                // -------------------------------------------
                // 保存单元格公式
                customExtFormula = formula.toString();
                // -------------------------------------------
                if (formulasNotResults) {
                    thisStr = formula.toString();
                } else {
                    String fv = value.toString();
                    // ---------------- 原始值 ----------------
                    this.customExtOriginValue = value.toString();
                    // ---------------------------------------
                    if (this.formatString != null) {
                        try {
                            // Try to use the value as a formattable number
                            double d = Double.parseDouble(fv);
                            thisStr = formatter.formatRawCellContents(d, this.formatIndex, this.formatString);
                        } catch (NumberFormatException e) {
                            // Formula is a String result not a Numeric one
                            thisStr = fv;
                        }
                    } else {
                        // No formatting applied, just do raw value in all cases
                        thisStr = fv;
                    }
                }
                break;

            case INLINE_STRING:
                // TODO: Can these ever have formatting on them?
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                thisStr = rtsi.toString();
                // ---------------- 索引值对应的内容值 ----------------
                // TODO 待测试
                this.customExtOriginValue = thisStr;
                // -------------------------------------------------
                break;

            case SST_STRING:
                String sstIndex = value.toString();
                if (sstIndex.length() > 0) {
                    try {
                        int idx = Integer.parseInt(sstIndex);
                        RichTextString rtss = sharedStringsTable.getItemAt(idx);
                        thisStr = rtss.toString();
                        // ---------------- 索引值对应的内容值 ----------------
                        // TODO 待测试
                        this.customExtOriginValue = thisStr;
                        // -------------------------------------------------
                    } catch (NumberFormatException ex) {
                        LOG.atError().withThrowable(ex).log("Failed to parse SST index '{}'", sstIndex);
                    }
                }
                break;

            case NUMBER:
                String n = value.toString();
                // ---------------- 内容值 -------------------------
                // TODO 待测试，说明：时间格式，格式化后的数据与表格展示的数据不一致
                this.customExtOriginValue = n;
                // -------------------------------------------------
                if (this.formatString != null && n.length() > 0) {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                } else {
                    thisStr = n;
                }
                break;

            default:
                thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                break;
        }

        // Do we have a comment for this cell?
        checkForEmptyCellComments(EmptyCellCommentsCheckType.CELL);
        XSSFComment comment = comments != null ? comments.findCellComment(new CellAddress(cellRef)) : null;

        // 单元格列编号
        this.customExtColTitle = cellRef.replaceAll("\\d+", "");
        this.customExtColIndex = this.titleToNumber(customExtColTitle) - 1;

        // Output
        output.cell(cellRef, thisStr, comment, rowNum, customExtColIndex, customExtColTitle, nextDataType, customExtStyle, customExtOriginValue, customExtFormula, customExtSheetIndex, customExtSheetName);
    }

    /**
     * Do a check for, and output, comments in otherwise empty cells.
     */
    private void checkForEmptyCellComments(EmptyCellCommentsCheckType type) {
        if (commentCellRefs != null && !commentCellRefs.isEmpty()) {
            // If we've reached the end of the sheet data, output any
            //  comments we haven't yet already handled
            if (type == EmptyCellCommentsCheckType.END_OF_SHEET_DATA) {
                while (!commentCellRefs.isEmpty()) {
                    outputEmptyCellComment(commentCellRefs.remove());
                }
                return;
            }

            // At the end of a row, handle any comments for "missing" rows before us
            if (this.cellRef == null) {
                if (type == EmptyCellCommentsCheckType.END_OF_ROW) {
                    while (!commentCellRefs.isEmpty()) {
                        if (commentCellRefs.peek().getRow() == rowNum) {
                            outputEmptyCellComment(commentCellRefs.remove());
                        } else {
                            return;
                        }
                    }
                    return;
                } else {
                    throw new IllegalStateException("Cell ref should be null only if there are only empty cells in the row; rowNum: " + rowNum);
                }
            }

            CellAddress nextCommentCellRef;
            do {
                CellAddress cellRef = new CellAddress(this.cellRef);
                CellAddress peekCellRef = commentCellRefs.peek();
                if (type == EmptyCellCommentsCheckType.CELL && cellRef.equals(peekCellRef)) {
                    // remove the comment cell ref from the list if we're about to handle it alongside the cell content
                    commentCellRefs.remove();
                    return;
                } else {
                    // fill in any gaps if there are empty cells with comment mixed in with non-empty cells
                    int comparison = peekCellRef.compareTo(cellRef);
                    if (comparison > 0 && type == EmptyCellCommentsCheckType.END_OF_ROW && peekCellRef.getRow() <= rowNum) {
                        nextCommentCellRef = commentCellRefs.remove();
                        outputEmptyCellComment(nextCommentCellRef);
                    } else if (comparison < 0 && type == EmptyCellCommentsCheckType.CELL && peekCellRef.getRow() <= rowNum) {
                        nextCommentCellRef = commentCellRefs.remove();
                        outputEmptyCellComment(nextCommentCellRef);
                    } else {
                        nextCommentCellRef = null;
                    }
                }
            } while (nextCommentCellRef != null && !commentCellRefs.isEmpty());
        }
    }


    /**
     * Output an empty-cell comment.
     */
    private void outputEmptyCellComment(CellAddress cellRef) {
        XSSFComment comment = comments.findCellComment(cellRef);
        output.cell(cellRef.formatAsString(), null, comment, rowNum, customExtColIndex, customExtColTitle, nextDataType, customExtStyle, customExtOriginValue, customExtFormula, customExtSheetIndex, customExtSheetName);
    }

    private enum EmptyCellCommentsCheckType {
        CELL,
        END_OF_ROW,
        END_OF_SHEET_DATA
    }

    /**
     * This interface allows to provide callbacks when reading
     * a sheet in streaming mode.
     * <p>
     * The XSLX file is usually read via {@link XSSFReader}.
     * <p>
     * By implementing the methods, you can process arbitrarily
     * large files without exhausting main memory.
     */
    public interface SheetContentsHandler {
        /**
         * A row with the (zero based) row number has started
         */
        void startRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName);

        /**
         * A row with the (zero based) row number has ended
         */
        void endRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName);

        /**
         * A cell, with the given formatted value (may be null),
         * and possibly a comment (may be null), was encountered.
         * <p>
         * Sheets that have missing or empty cells may result in
         * sparse calls to <code>cell</code>. See the code in
         * <code>src/examples/src/org/apache/poi/xssf/eventusermodel/XLSX2CSV.java</code>
         * for an example of how to handle this scenario.
         *
         * @param cellReference         单元格位置，示例：A1
         * @param formattedValue        单元格格式化后数值
         * @param comment               批注，说明：若备注存在，则该单元格数据会重复处理两次，唯一的区别是一个有批注、一个无批注，其余信息相同，同一个单元格下，有批准的单元格先到达
         * @param customExtRowIndex     单元格行序号，从0开始
         * @param customExtColIndex     单元格列序号，从0开始
         * @param customExtColTitle     单元格列标题，示例：A，B，AA
         * @param customExtXssfDataType 单元格内置类型
         * @param customExtStyle        单元格样式
         * @param customExtOriginValue  单元格原始值
         * @param customExtFormula      单元格公式
         * @param customExtSheetIndex   工作表序号，从0开始
         * @param customExtSheetName    工作表名称
         */
        void cell(String cellReference, String formattedValue, XSSFComment comment, Integer customExtRowIndex, Integer customExtColIndex, String customExtColTitle, xssfDataType customExtXssfDataType, XSSFCellStyle customExtStyle, String customExtOriginValue, String customExtFormula, Integer customExtSheetIndex, String customExtSheetName);

        /**
         * A header or footer has been encountered
         */
        default void headerFooter(String text, boolean isHeader, String tagName, Integer customExtSheetIndex, String customExtSheetName) {
        }

        /**
         * Signal that the end of a sheet was been reached
         */
        default void endSheet(Integer customExtSheetIndex, String customExtSheetName) {
        }
    }

    /**
     * 列标题转列序号
     *
     * @param columnTitle 列标题
     * @return
     */
    private int titleToNumber(String columnTitle) {
        int number = 0;
        int multiple = 1;
        for (int i = columnTitle.length() - 1; i >= 0; i--) {
            int k = columnTitle.charAt(i) - 'A' + 1;
            number += k * multiple;
            multiple *= 26;
        }
        return number;
    }

}