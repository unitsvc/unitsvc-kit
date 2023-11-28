package com.unitsvc.kit.facade.db.antlr.d2.visitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unitsvc.kit.facade.db.antlr.d2.inner.ExprBaseVisitor;
import com.unitsvc.kit.facade.db.antlr.d2.inner.ExprParser;
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
    public Document visitCondition(ExprParser.ConditionContext ctx) {
        String field = ctx.ID().getText();
        String operator = ctx.op.getText();
        ExprParser.NumberContext number = ctx.value().number();
        ExprParser.StrContext str = ctx.value().str();
        Object value = null;
        if (number != null) {
            value = new BigDecimal(number.getText());
        }
        if (str != null) {
            value = str.getText()
                    .replaceAll("'", "");
        }
        Document condition = new Document();
        switch (operator) {
            case "==":
                condition.put(field, value);
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
