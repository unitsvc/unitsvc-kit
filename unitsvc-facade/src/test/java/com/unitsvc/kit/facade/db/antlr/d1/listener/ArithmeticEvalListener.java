package com.unitsvc.kit.facade.db.antlr.d1.listener;

import com.unitsvc.kit.facade.db.antlr.d1.inner.CalculatorBaseListener;
import com.unitsvc.kit.facade.db.antlr.d1.inner.CalculatorParser;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 功能描述：Listener模式
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/13 17:21
 **/
public class ArithmeticEvalListener extends CalculatorBaseListener {

    private final Deque<Integer> stack = new LinkedList<>();

    /**
     * The last value on the stack is the result of all applied calculations.
     *
     * @return Integer
     */
    public Integer getResult() {
        return this.stack.peek();
    }

    @Override
    public void exitMulDiv(CalculatorParser.MulDivContext ctx) {
        int right = this.stack.pop();
        int left = this.stack.pop();
        if (ctx.op.getType() == CalculatorParser.MUL) {
            this.stack.push(left * right);
        } else {
            this.stack.push(left / right);
        }
    }

    @Override
    public void exitAddSub(CalculatorParser.AddSubContext ctx) {
        int right = this.stack.pop();
        int left = this.stack.pop();
        if (ctx.op.getType() == CalculatorParser.Add) {
            this.stack.push(left + right);
        } else {
            this.stack.push(left - right);
        }
    }

    @Override
    public void exitInt(CalculatorParser.IntContext ctx) {
        Integer number = Integer.parseInt(ctx.INT().getText());
        this.stack.push(number);
    }
}