package com.unitsvc.kit.facade.db.antlr.d2.visitor;

import com.unitsvc.kit.facade.db.antlr.d2.inner.ExprLexer;
import com.unitsvc.kit.facade.db.antlr.d2.inner.ExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.bson.Document;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/26 16:41
 **/
public class FilterUtil {

    /**
     * 构建过滤条件
     *
     * @param expr 表达式
     * @return
     */
    public static Document buildFilter(String expr) {
        // 读取源代码文件
        CodePointCharStream input = CharStreams.fromString(expr);
        // 创建词法分析器对象
        ExprLexer lexer = new ExprLexer(input);
        // 获取词法分析器输出的tokens
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        // 创建语法分析器对象，并将词法分析器输出的tokens作为语法分析器的输入
        ExprParser parser = new ExprParser(tokenStream);
        // 分析程序生成AST，递归分析源代码
        ExprParser.ExprContext tree = parser.expr();
        // 创建Visitor对象
        FilterVisitor eval = new FilterVisitor();
        // 开始遍历AST
        return eval.visit(tree);
    }

}
