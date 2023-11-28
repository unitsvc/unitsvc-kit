package com.unitsvc.kit.facade.db.antlr.d2.inner;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExprParser}.
 */
public interface ExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(ExprParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(ExprParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code condition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCondition(ExprParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code condition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCondition(ExprParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relation}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRelation(ExprParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relation}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRelation(ExprParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ExprParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ExprParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#str}.
	 * @param ctx the parse tree
	 */
	void enterStr(ExprParser.StrContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#str}.
	 * @param ctx the parse tree
	 */
	void exitStr(ExprParser.StrContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(ExprParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(ExprParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#decimal}.
	 * @param ctx the parse tree
	 */
	void enterDecimal(ExprParser.DecimalContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#decimal}.
	 * @param ctx the parse tree
	 */
	void exitDecimal(ExprParser.DecimalContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExprParser#negativeDecimal}.
	 * @param ctx the parse tree
	 */
	void enterNegativeDecimal(ExprParser.NegativeDecimalContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#negativeDecimal}.
	 * @param ctx the parse tree
	 */
	void exitNegativeDecimal(ExprParser.NegativeDecimalContext ctx);
}