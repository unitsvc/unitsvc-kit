package com.unitsvc.kit.facade.db.antlr.d3.inner;
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
	 * Enter a parse tree produced by the {@code arrayCondition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrayCondition(ExprParser.ArrayConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayCondition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrayCondition(ExprParser.ArrayConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueCondition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterValueCondition(ExprParser.ValueConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueCondition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitValueCondition(ExprParser.ValueConditionContext ctx);
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
	 * Enter a parse tree produced by {@link ExprParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(ExprParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExprParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(ExprParser.ArrayContext ctx);
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
}