package com.unitsvc.kit.facade.db.antlr.d1;

import com.unitsvc.kit.facade.db.antlr.d1.inner.CalculatorLexer;
import com.unitsvc.kit.facade.db.antlr.d1.inner.CalculatorParser;
import com.unitsvc.kit.facade.db.antlr.d1.listener.ArithmeticEvalListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/13 17:21
 **/
public class D1Test {

    @Test
    public void testSimpleAdd() {
        final String expr = "1+1";
        Assert.assertEquals(2, calculate(expr));
    }

    @Test
    public void testSimpleSub() {
        String expr = "6-1";
        Assert.assertEquals(5, calculate(expr));
    }

    @Test
    public void testSimpleMul(){
        String expr = "2*3";
        Assert.assertEquals(6, calculate(expr));
    }

    @Test
    public void testSimpleDiv(){
        String expr = "6/3";
        Assert.assertEquals(2, calculate(expr));
    }

    @Test
    public void testSubAdd(){
        String expr = "6-1+3";
        Assert.assertEquals(8, calculate(expr));
    }

    @Test
    public void testDivMul(){
        String expr = "6/2*3";
        Assert.assertEquals(9, calculate(expr));
    }

    @Test
    public void testComplex1(){
        String expr = "6/(1+1)*3";
        Assert.assertEquals(9, calculate(expr));
    }

    @Test
    public void testComplex2(){
        String expr = "6/2+4/2";
        Assert.assertEquals(5, calculate(expr));
    }

    @Test
    public void testFLoadCalculate(){
        final String expr = "1.2+2.1";
        Number result = calculate(expr);
        Assert.assertEquals(3.3, result.floatValue(),0.001);
    }

    private Number calculate(String expr) {
        CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.expr();

        ParseTreeWalker walker = new ParseTreeWalker();
        ArithmeticEvalListener listener = new ArithmeticEvalListener();
        walker.walk(listener, tree);
        return listener.getResult();
    }

}
