package com.unitsvc.kit.facade.db.antlr.d1.visitor;

import com.unitsvc.kit.facade.db.antlr.d1.inner.CalculatorBaseVisitor;
import com.unitsvc.kit.facade.db.antlr.d1.inner.CalculatorParser;

/**
 * 功能描述：Visitor模式
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/13 17:20
 **/
public class ArithmeticEvalVisitor extends CalculatorBaseVisitor<Integer> {

    @Override
    public Integer visitMulDiv(CalculatorParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == CalculatorParser.MUL) {
            return left * right;
        }
        return left / right;
    }

    @Override
    public Integer visitAddSub(CalculatorParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == CalculatorParser.Add) {
            return left + right;
        }
        return left - right;
    }

    @Override
    public Integer visitInt(CalculatorParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitParens(CalculatorParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}