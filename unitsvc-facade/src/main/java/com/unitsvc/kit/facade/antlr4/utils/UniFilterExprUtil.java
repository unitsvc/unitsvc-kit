package com.unitsvc.kit.facade.antlr4.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.unitsvc.kit.facade.antlr4.constant.FilterVersionConstant;
import com.unitsvc.kit.facade.antlr4.exception.FilterExprException;
import com.jpardus.spider.sccs.Log;
import com.unitsvc.kit.facade.antlr4.config.FilterExprConfig;
import com.unitsvc.kit.facade.antlr4.inner.FilterExprLexer;
import com.unitsvc.kit.facade.antlr4.inner.FilterExprParser;
import com.unitsvc.kit.facade.antlr4.handler.MongodbFilterExprVisitor;
import org.antlr.v4.runtime.*;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

/**
 * 功能描述：表达式解析工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/26 16:41
 **/
public class UniFilterExprUtil {

    /**
     * 序列化
     */
    private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    /**
     * 构建mongodb过滤条件，默认规则不正确，抛出异常
     * <pre>
     *  说明：
     *  1.推荐捕获异常，书写正确的表达式，避免表达式解析不完整
     *  2.不支持eval(``` 表达式 ```)写法
     * </pre>
     *
     * @param filterExpr 规则表达式，示例：$enable == false || $age @ [1,2,3]) && $year != '10'
     * @param relations  扩展字段
     * @return
     */
    public static Document buildMongodbFilter(String filterExpr, JsonObject relations) throws FilterExprException {
        return buildMongodbFilter(filterExpr, relations, new JsonObject(), new FilterExprConfig().setIgnoreLikeNullException(false).setIgnoreSyntaxException(false));
    }

    /**
     * 构建mongodb过滤条件，默认规则不正确，抛出异常
     * <pre>
     *  说明：
     *  1.推荐捕获异常，书写正确的表达式，避免表达式解析不完整
     *  2.支持eval(``` 表达式 ```)写法
     * </pre>
     *
     * @param filterExpr 规则表达式，示例：($name == eval(``` return {{a}} + {{b}} ;```) || $age @ [1,2,3]) && $year != '10'
     * @param relations  扩展字段
     * @param evalVars   计算字段
     * @return
     */
    public static Document buildMongodbFilter(String filterExpr, JsonObject relations, JsonObject evalVars) throws FilterExprException {
        return buildMongodbFilter(filterExpr, relations, evalVars, new FilterExprConfig().setIgnoreLikeNullException(false).setIgnoreSyntaxException(false));
    }

    /**
     * 构建mongodb过滤条件
     *
     * <pre>
     *  说明：
     *  1.推荐捕获异常，书写正确的表达式，避免表达式解析不完整
     *  2.不支持eval(``` 表达式 ```)写法
     *  3.自定义配置
     * </pre>
     *
     * @param filterExpr       规则表达式，示例：($name == eval(``` return {{a}} + {{b}} ;```) || $age @ [1,2,3]) && $year != '10'
     * @param relations        扩展字段
     * @param evalVars         计算字段
     * @param filterExprConfig 规则配置，允许忽略部分异常
     * @return
     * @throws FilterExprException
     */
    public static Document buildMongodbFilter(String filterExpr, JsonObject relations, JsonObject evalVars, FilterExprConfig filterExprConfig) throws FilterExprException {
        Log.debug(String.format("%s input：%s", FilterVersionConstant.VERSION_DESC, filterExpr));
        if (StringUtils.isBlank(filterExpr)) {
            Log.debug(String.format("%s output：%s", FilterVersionConstant.VERSION_DESC, null));
            return null;
        }
        // 读取源代码文件
        CodePointCharStream input = CharStreams.fromString(filterExpr);
        // 创建词法分析器对象
        FilterExprLexer lexer = new FilterExprLexer(input);
        // 获取词法分析器输出的tokens
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        // 创建语法分析器对象，并将词法分析器输出的tokens作为语法分析器的输入
        FilterExprParser parser = new FilterExprParser(tokenStream);
        // 移除默认错误监听器
        parser.removeErrorListeners();
        // 添加自定义错误监听器
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                String error = String.format("%s error：line：%s，position：%s，msg：%s，please check filter expr：%s", FilterVersionConstant.VERSION_DESC, line, charPositionInLine, msg, filterExpr);
                // 自定义错误处理逻辑
                Log.error(error, e);
                if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreSyntaxException())) {
                    throw new FilterExprException(error, e);
                }
            }
        });
        // 分析程序生成AST，递归分析源代码
        FilterExprParser.QueryContext tree = parser.query();
        // 创建Visitor对象
        MongodbFilterExprVisitor eval = new MongodbFilterExprVisitor(filterExpr, relations, evalVars, filterExprConfig);
        // 开始遍历AST
        Document visit = eval.visit(tree);
        Log.debug(String.format("%s output：%s", FilterVersionConstant.VERSION_DESC, GSON.toJson(visit)));
        return visit;
    }

}
