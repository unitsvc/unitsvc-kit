package com.unitsvc.kit.facade.db.antlr.d3.visitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.unitsvc.kit.facade.db.antlr.d3.inner.ExprBaseVisitor;
import com.unitsvc.kit.facade.db.antlr.d3.inner.ExprParser;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/16 13:36
 **/
public class FilterVisitor extends ExprBaseVisitor<Document> {

    @Override
    public Document visitParens(ExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Document visitArrayCondition(ExprParser.ArrayConditionContext ctx) {
        String field = ctx.ID().getText();
        ExprParser.ArrayContext array = ctx.array();
        List<ExprParser.ValueContext> values = array.value();
        List<Object> params = new ArrayList<>();
        for (ExprParser.ValueContext value : values) {
            TerminalNode b1 = value.BOOL();
            TerminalNode n1 = value.NUMBER();
            TerminalNode s1 = value.STRING();
            TerminalNode n2 = value.NULL();
            if (null != b1) {
                params.add(Boolean.valueOf(b1.getText()));
            } else if (null != n1) {
                params.add(new BigDecimal(n1.getText()));
            } else if (null != s1) {
                // 去除转义字符
                params.add(s1.getText().replaceAll("\"", ""));
            } else if (null != n2) {
                params.add(null);
            }
        }
        Document condition = new Document();
        condition.put(field, new Document("$in", params));
        return condition;
    }

    @Override
    public Document visitValueCondition(ExprParser.ValueConditionContext ctx) {
        String field = ctx.ID().getText();
        String operator = ctx.op.getText();
        TerminalNode n1 = ctx.value().NUMBER();
        TerminalNode b1 = ctx.value().BOOL();
        TerminalNode s1 = ctx.value().STRING();
        TerminalNode n2 = ctx.value().NULL();
        Object value = new Object();
        if (null != b1) {
            value = Boolean.valueOf(b1.getText());
        } else if (null != n1) {
            value = new BigDecimal(n1.getText());
        } else if (null != s1) {
            // 去除转义字符
            value = s1.getText().replaceAll("\"", "");
        } else if (null != n2) {
            value = null;
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
        }
        return condition;
    }


    @Override
    public Document visitRelation(ExprParser.RelationContext ctx) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String relation = ctx.relation.getText();
        Document leftDoc = visit(ctx.expr(0));
        Document rightDoc = visit(ctx.expr(1));
        switch (relation) {
            case "||":
                List<Document> or = new ArrayList<>();
                or.add(leftDoc);
                or.add(rightDoc);
                return new Document("$or", or);
            case "&&":
                List<Document> and = new ArrayList<>();
                and.add(leftDoc);
                and.add(rightDoc);
                return new Document("$and", and);
            default:
        }
        return super.visitRelation(ctx);
    }

}
