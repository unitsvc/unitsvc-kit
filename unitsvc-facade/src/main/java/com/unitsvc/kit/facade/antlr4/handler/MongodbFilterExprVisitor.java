package com.unitsvc.kit.facade.antlr4.handler;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.unitsvc.kit.facade.antlr4.config.FilterExprConfig;
import com.unitsvc.kit.facade.antlr4.constant.FilterVersionConstant;
import com.unitsvc.kit.facade.antlr4.exception.FilterExprException;
import com.unitsvc.kit.facade.antlr4.utils.Antlr4Util;
import com.unitsvc.kit.facade.script.UniScriptEvalTool;
import com.unitsvc.kit.facade.antlr4.inner.FilterExprBaseVisitor;
import com.unitsvc.kit.facade.antlr4.inner.FilterExprParser;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：Mongodb表达式规则解析
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/16 13:36
 **/
public class MongodbFilterExprVisitor extends FilterExprBaseVisitor<Document> {

    /**
     * 规则表达式
     */
    private String filterExpr;

    /**
     * 扩展字段
     */
    private JsonObject relations;

    /**
     * js变量
     */
    private JsonObject evalVars;

    /**
     * 规则配置
     */
    private FilterExprConfig filterExprConfig;

    /**
     * 构造函数
     *
     * @param filterExpr 规则表达式
     * @param relations  扩展字段
     */
    public MongodbFilterExprVisitor(String filterExpr, JsonObject relations) {
        this.filterExpr = filterExpr;
        this.relations = null != relations ? relations : new JsonObject();
        this.filterExprConfig = new FilterExprConfig();
        this.evalVars = new JsonObject();
    }

    /**
     * 构造函数
     *
     * @param filterExpr       规则表达式
     * @param relations        扩展字段
     * @param filterExprConfig 规则配置
     */
    public MongodbFilterExprVisitor(String filterExpr, JsonObject relations, FilterExprConfig filterExprConfig) {
        this.filterExpr = filterExpr;
        this.relations = null != relations ? relations : new JsonObject();
        this.filterExprConfig = filterExprConfig;
        this.evalVars = new JsonObject();
    }

    /**
     * 构造函数
     *
     * @param filterExpr       规则表达式
     * @param relations        扩展字段
     * @param evalVars         计算字段
     * @param filterExprConfig 规则配置
     */
    public MongodbFilterExprVisitor(String filterExpr, JsonObject relations, JsonObject evalVars, FilterExprConfig filterExprConfig) {
        this.filterExpr = filterExpr;
        this.relations = null != relations ? relations : new JsonObject();
        this.filterExprConfig = filterExprConfig;
        this.evalVars = evalVars;
    }

    @Override
    public Document visitQuery(FilterExprParser.QueryContext ctx) {
        return visit(ctx.orExpression());
    }

    @Override
    public Document visitOrExpression(FilterExprParser.OrExpressionContext ctx) {
        // 获取AND表达式
        List<FilterExprParser.AndExpressionContext> ands = ctx.andExpression();
        List<Document> orDocuments = new ArrayList<>();
        for (FilterExprParser.AndExpressionContext and : ands) {
            // 获取变量
            List<FilterExprParser.VariableContext> variables = and.variable();
            List<Document> andDocuments = new ArrayList<>();
            for (FilterExprParser.VariableContext variable : variables) {
                Document visitResult = visit(variable);
                if (null != visitResult) {
                    andDocuments.add(visitResult);
                }
            }
            if (CollectionUtils.isNotEmpty(andDocuments)) {
                if (andDocuments.size() > 1) {
                    orDocuments.add(new Document("$and", andDocuments));
                } else {
                    orDocuments.add(andDocuments.get(0));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(orDocuments)) {
            // 优化查询条件
            if (orDocuments.size() > 1) {
                return new Document("$or", orDocuments);
            } else {
                return orDocuments.get(0);
            }
        }
        return null;
    }

    @Override
    public Document visitAndExpression(FilterExprParser.AndExpressionContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Document visitVariableParents(FilterExprParser.VariableParentsContext ctx) {
        return visit(ctx.variable());
    }

    @Override
    public Document visitQueryParents(FilterExprParser.QueryParentsContext ctx) {
        return visit(ctx.query());
    }

    @Override
    public Document visitArrayCondition(FilterExprParser.ArrayConditionContext ctx) {
        String field = ctx.ID().getText();
        FilterExprParser.ArrayContext array = ctx.array();
        List<FilterExprParser.ValueContext> values = array.value();
        TerminalNode jsValues = array.JavaScript();
        Document condition = new Document();
        if (CollectionUtils.isNotEmpty(values)) {
            List<Object> params = new ArrayList<>();
            for (FilterExprParser.ValueContext value : values) {
                TerminalNode b1 = value.BOOL();
                TerminalNode n1 = value.NUMBER();
                TerminalNode s1 = value.STRING();
                TerminalNode n2 = value.NULL();
                TerminalNode j1 = value.JavaScript();
                if (null != b1) {
                    params.add(Boolean.valueOf(b1.getText()));
                } else if (null != n1) {
                    params.add(new BigDecimal(n1.getText()));
                } else if (null != s1) {
                    // 去除转义字符
                    params.add(Antlr4Util.replaceStartAndEndStr(s1.getText()));
                } else if (null != n2) {
                    params.add(null);
                } else if (null != j1) {
                    Object result = UniScriptEvalTool.compileFunctionAndCache(j1.getText().replace("```", "")).executeFunction(evalVars);
                    params.add(result);
                }
            }
            condition.put(field, new Document("$in", params));
            return condition;
        } else if (null != jsValues) {
            Object result = UniScriptEvalTool.compileFunctionAndCache(jsValues.getText().replace("```", "")).executeFunction(evalVars);
            condition.put(field, new Document("$in", result));
        }
        return condition;
    }

    @Override
    public Document visitValueCondition(FilterExprParser.ValueConditionContext ctx) {
        String field = ctx.ID().getText();
        String operator = ctx.op.getText();
        TerminalNode n1 = ctx.value().NUMBER();
        TerminalNode b1 = ctx.value().BOOL();
        TerminalNode s1 = ctx.value().STRING();
        TerminalNode n2 = ctx.value().NULL();
        TerminalNode j1 = ctx.value().JavaScript();
        Object value = null;
        if (null != b1) {
            value = Boolean.valueOf(b1.getText());
        } else if (null != n1) {
            value = new BigDecimal(n1.getText());
        } else if (null != s1) {
            // 去除转义字符
            value = Antlr4Util.replaceStartAndEndStr(s1.getText());
        } else if (null != n2) {
            value = null;
        } else if (null != j1) {
            value = UniScriptEvalTool.compileFunctionAndCache(j1.getText().replace("```", "")).executeFunction(evalVars);
        }
        Document condition = new Document();
        switch (operator) {
            case "==":
                condition.put(field, new Document("$eq", value));
                break;
            case "!=":
                condition.put(field, new Document("$ne", value));
                break;
            case ">=":
                condition.put(field, new Document("$gte", value));
                break;
            case "<=":
                condition.put(field, new Document("$lte", value));
                break;
            case ">":
                condition.put(field, new Document("$gt", value));
                break;
            case "<":
                condition.put(field, new Document("$lt", value));
                break;
            default:
                // pass
        }
        return condition;
    }

    @Override
    public Document visitValueVarCondition(FilterExprParser.ValueVarConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().get(0).getText();
        // 扩展字段ID
        String relation = ctx.ID().get(1).getText();
        // 操作符
        String operator = ctx.op.getText();
        // 获取扩展字段值
        JsonElement relationValue = relations.get(relation);
        Object value = null;
        if (null != relationValue) {
            JsonPrimitive valueAsJsonPrimitive = relationValue.getAsJsonPrimitive();
            if (valueAsJsonPrimitive.isBoolean()) {
                value = valueAsJsonPrimitive.getAsBoolean();
            } else if (valueAsJsonPrimitive.isNumber()) {
                value = valueAsJsonPrimitive.getAsBigDecimal();
            } else if (valueAsJsonPrimitive.isString()) {
                value = valueAsJsonPrimitive.getAsString();
            }
        }
        Document condition = new Document();
        switch (operator) {
            case "==":
                condition.put(field, new Document("$eq", value));
                break;
            case "!=":
                condition.put(field, new Document("$ne", value));
                break;
            case ">=":
                condition.put(field, new Document("$gte", value));
                break;
            case "<=":
                condition.put(field, new Document("$lte", value));
                break;
            case ">":
                condition.put(field, new Document("$gt", value));
                break;
            case "<":
                condition.put(field, new Document("$lt", value));
                break;
            default:
                // pass
        }
        return condition;
    }

    @Override
    public Document visitArrayVarCondition(FilterExprParser.ArrayVarConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().get(0).getText();
        // 扩展字段ID
        String relation = ctx.ID().get(1).getText();
        // 获取扩展字段值
        JsonElement relationValue = relations.get(relation);
        List<Object> params = new ArrayList<>();
        if (null != relationValue) {
            JsonArray valueAsJsonArray = relationValue.getAsJsonArray();
            if (null != valueAsJsonArray && valueAsJsonArray.size() > 0) {
                for (JsonElement element : valueAsJsonArray) {
                    JsonPrimitive valueAsJsonPrimitive = element.getAsJsonPrimitive();
                    if (valueAsJsonPrimitive.isBoolean()) {
                        params.add(valueAsJsonPrimitive.getAsBoolean());
                    } else if (valueAsJsonPrimitive.isNumber()) {
                        params.add(valueAsJsonPrimitive.getAsBigDecimal());
                    } else if (valueAsJsonPrimitive.isString()) {
                        params.add(valueAsJsonPrimitive.getAsString());
                    }
                }
            }
        }
        Document condition = new Document();
        condition.put(field, new Document("$in", params));
        return condition;
    }

    @Override
    public Document visitValueLikeCondition(FilterExprParser.ValueLikeConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().getText();
        TerminalNode n1 = ctx.value().NUMBER();
        TerminalNode b1 = ctx.value().BOOL();
        TerminalNode s1 = ctx.value().STRING();
        TerminalNode n2 = ctx.value().NULL();
        TerminalNode j1 = ctx.value().JavaScript();
        Object value = null;
        if (null != b1) {
            value = Boolean.valueOf(b1.getText());
        } else if (null != n1) {
            value = new BigDecimal(n1.getText());
        } else if (null != s1) {
            // 去除转义字符
            value = Antlr4Util.replaceStartAndEndStr(s1.getText());
        } else if (null != n2) {
            value = null;
        } else if (null != j1) {
            value = UniScriptEvalTool.compileFunctionAndCache(j1.getText().replace("```", "")).executeFunction(evalVars);
        }
        Document condition = new Document();
        condition.append(field, new Document("$regex", value));
        return condition;
    }

    @Override
    public Document visitValueLikeLeftCondition(FilterExprParser.ValueLikeLeftConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().getText();
        TerminalNode n1 = ctx.value().NUMBER();
        TerminalNode b1 = ctx.value().BOOL();
        TerminalNode s1 = ctx.value().STRING();
        TerminalNode n2 = ctx.value().NULL();
        TerminalNode j1 = ctx.value().JavaScript();
        Object value = null;
        if (null != b1) {
            value = Boolean.valueOf(b1.getText());
        } else if (null != n1) {
            value = new BigDecimal(n1.getText());
        } else if (null != s1) {
            // 去除转义字符
            value = Antlr4Util.replaceStartAndEndStr(s1.getText());
        } else if (null != n2) {
            value = null;
        } else if (null != j1) {
            value = UniScriptEvalTool.compileFunctionAndCache(j1.getText().replace("```", "")).executeFunction(evalVars);
        }
        Document condition = new Document();
        condition.append(field, new Document("$regex", "^" + value));
        return condition;
    }

    @Override
    public Document visitValueLikeRightCondition(FilterExprParser.ValueLikeRightConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().getText();
        TerminalNode n1 = ctx.value().NUMBER();
        TerminalNode b1 = ctx.value().BOOL();
        TerminalNode s1 = ctx.value().STRING();
        TerminalNode n2 = ctx.value().NULL();
        TerminalNode j1 = ctx.value().JavaScript();
        Object value = null;
        if (null != b1) {
            value = Boolean.valueOf(b1.getText());
        } else if (null != n1) {
            value = new BigDecimal(n1.getText());
        } else if (null != s1) {
            // 去除转义字符
            value = Antlr4Util.replaceStartAndEndStr(s1.getText());
        } else if (null != n2) {
            value = null;
        } else if (null != j1) {
            value = UniScriptEvalTool.compileFunctionAndCache(j1.getText().replace("```", "")).executeFunction(evalVars);
        }
        Document condition = new Document();
        condition.append(field, new Document("$regex", value + "$"));
        return condition;
    }

    @Override
    public Document visitValueVarLikeCondition(FilterExprParser.ValueVarLikeConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().get(0).getText();
        // 扩展字段ID
        String relation = ctx.ID().get(1).getText();
        // 获取扩展字段值
        JsonElement relationValue = relations.get(relation);
        Object value = null;
        if (null != relationValue) {
            JsonPrimitive valueAsJsonPrimitive = relationValue.getAsJsonPrimitive();
            if (valueAsJsonPrimitive.isBoolean()) {
                value = valueAsJsonPrimitive.getAsBoolean();
            } else if (valueAsJsonPrimitive.isNumber()) {
                value = valueAsJsonPrimitive.getAsBigDecimal();
            } else if (valueAsJsonPrimitive.isString()) {
                value = valueAsJsonPrimitive.getAsString();
            }
        }
        Document condition = new Document();
        condition.append(field, new Document("$regex", value));
        return condition;
    }

    @Override
    public Document visitValueVarLikeLeftCondition(FilterExprParser.ValueVarLikeLeftConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().get(0).getText();
        // 扩展字段ID
        String relation = ctx.ID().get(1).getText();
        // 获取扩展字段值
        JsonElement relationValue = relations.get(relation);
        Object value = null;
        if (null != relationValue) {
            JsonPrimitive valueAsJsonPrimitive = relationValue.getAsJsonPrimitive();
            if (valueAsJsonPrimitive.isBoolean()) {
                value = valueAsJsonPrimitive.getAsBoolean();
            } else if (valueAsJsonPrimitive.isNumber()) {
                value = valueAsJsonPrimitive.getAsBigDecimal();
            } else if (valueAsJsonPrimitive.isString()) {
                value = valueAsJsonPrimitive.getAsString();
            }
        }
        Document condition = new Document();
        condition.append(field, new Document("$regex", "^" + value));
        return condition;
    }

    @Override
    public Document visitValueVarLikeRightCondition(FilterExprParser.ValueVarLikeRightConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().get(0).getText();
        // 扩展字段ID
        String relation = ctx.ID().get(1).getText();
        // 获取扩展字段值
        JsonElement relationValue = relations.get(relation);
        Object value = null;
        if (null != relationValue) {
            JsonPrimitive valueAsJsonPrimitive = relationValue.getAsJsonPrimitive();
            if (valueAsJsonPrimitive.isBoolean()) {
                value = valueAsJsonPrimitive.getAsBoolean();
            } else if (valueAsJsonPrimitive.isNumber()) {
                value = valueAsJsonPrimitive.getAsBigDecimal();
            } else if (valueAsJsonPrimitive.isString()) {
                value = valueAsJsonPrimitive.getAsString();
            }
        }
        Document condition = new Document();
        condition.append(field, new Document("$regex", value + "$"));
        return condition;
    }

    @Override
    public Document visitEmptyValueCondition(FilterExprParser.EmptyValueConditionContext ctx) {
        String field = ctx.ID().getText();
        String operator = ctx.op.getText();
        Document condition = new Document();
        switch (operator) {
            case "==":
                condition.put(field, new Document("$eq", null));
                break;
            case "!=":
                condition.put(field, new Document("$ne", null));
                break;
            case ">=":
                condition.put(field, new Document("$gte", null));
                break;
            case "<=":
                condition.put(field, new Document("$lte", null));
                break;
            case ">":
                condition.put(field, new Document("$gt", null));
                break;
            case "<":
                condition.put(field, new Document("$lt", null));
                break;
            default:
                // pass
        }
        return condition;
    }

    @Override
    public Document visitEmptyArrayCondition(FilterExprParser.EmptyArrayConditionContext ctx) {
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$in", Lists.newArrayList()));
        return condition;
    }

    @Override
    public Document visitEmptyValueLikeCondition(FilterExprParser.EmptyValueLikeConditionContext ctx) {
        // 配置是否忽略异常
        if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreLikeNullException())) {
            String position = String.format("`%s %s`", ctx.ID().getText(), ctx.op.getText());
            throw new FilterExprException(String.format("%s error：%s，possible position in %s，please check：%s", FilterVersionConstant.VERSION_DESC, "`like` condition param is undefined", position, this.filterExpr));
        }
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$regex", "null"));
        return condition;
    }

    @Override
    public Document visitEmptyValueLikeLeftCondition(FilterExprParser.EmptyValueLikeLeftConditionContext ctx) {
        // 配置是否忽略异常
        if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreLikeNullException())) {
            String position = String.format("`%s %s`", ctx.ID().getText(), ctx.op.getText());
            throw new FilterExprException(String.format("%s error：%s，possible position in %s，please check：%s", FilterVersionConstant.VERSION_DESC, "`like` condition param is undefined", position, this.filterExpr));
        }
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$regex", "^null"));
        return condition;
    }

    @Override
    public Document visitEmptyValueLikeRightCondition(FilterExprParser.EmptyValueLikeRightConditionContext ctx) {
        // 配置是否忽略异常
        if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreLikeNullException())) {
            String position = String.format("`%s %s`", ctx.ID().getText(), ctx.op.getText());
            throw new FilterExprException(String.format("%s error：%s，possible position in %s，please check：%s", FilterVersionConstant.VERSION_DESC, "`like` condition param is undefined", position, this.filterExpr));
        }
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$regex", "null$"));
        return condition;
    }

    @Override
    public Document visitEmptyValueVarCondition(FilterExprParser.EmptyValueVarConditionContext ctx) {
        String field = ctx.ID().getText();
        String operator = ctx.op.getText();
        Document condition = new Document();
        switch (operator) {
            case "==":
                condition.put(field, new Document("$eq", null));
                break;
            case "!=":
                condition.put(field, new Document("$ne", null));
                break;
            case ">=":
                condition.put(field, new Document("$gte", null));
                break;
            case "<=":
                condition.put(field, new Document("$lte", null));
                break;
            case ">":
                condition.put(field, new Document("$gt", null));
                break;
            case "<":
                condition.put(field, new Document("$lt", null));
                break;
            default:
                // pass
        }
        return condition;
    }

    @Override
    public Document visitEmptyArrayVarCondition(FilterExprParser.EmptyArrayVarConditionContext ctx) {
        // 字段ID
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$in", Lists.newArrayList()));
        return condition;
    }

    @Override
    public Document visitEmptyValueVarLikeCondition(FilterExprParser.EmptyValueVarLikeConditionContext ctx) {
        // 配置是否忽略异常
        if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreLikeNullException())) {
            String position = String.format("`%s %s`", ctx.ID().getText(), ctx.op.getText());
            throw new FilterExprException(String.format("%s error：%s，possible position in %s，please check：%s", FilterVersionConstant.VERSION_DESC, "`like` condition param is undefined", position, this.filterExpr));
        }
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$regex", "null"));
        return condition;
    }

    @Override
    public Document visitEmptyValueVarLikeLeftCondition(FilterExprParser.EmptyValueVarLikeLeftConditionContext ctx) {
        // 配置是否忽略异常
        if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreLikeNullException())) {
            String position = String.format("`%s %s`", ctx.ID().getText(), ctx.op.getText());
            throw new FilterExprException(String.format("%s error：%s，possible position in %s，please check：%s", FilterVersionConstant.VERSION_DESC, "`like` condition param is undefined", position, this.filterExpr));
        }
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$regex", "^null"));
        return condition;
    }

    @Override
    public Document visitEmptyValueVarLikeRightCondition(FilterExprParser.EmptyValueVarLikeRightConditionContext ctx) {
        // 配置是否忽略异常
        if (!Boolean.TRUE.equals(filterExprConfig.getIgnoreLikeNullException())) {
            String position = String.format("`%s %s`", ctx.ID().getText(), ctx.op.getText());
            throw new FilterExprException(String.format("%s error：%s，possible position in %s，please check：%s", FilterVersionConstant.VERSION_DESC, "`like` condition param is undefined", position, this.filterExpr));
        }
        String field = ctx.ID().getText();
        Document condition = new Document();
        condition.put(field, new Document("$regex", "null$"));
        return condition;
    }

}
