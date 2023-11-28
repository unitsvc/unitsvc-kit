package com.unitsvc.kit.facade.antlr4.inner;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FilterExprParser}.
 */
public interface FilterExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FilterExprParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(FilterExprParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExprParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(FilterExprParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExprParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(FilterExprParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExprParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(FilterExprParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExprParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(FilterExprParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExprParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(FilterExprParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variableParents}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariableParents(FilterExprParser.VariableParentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variableParents}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariableParents(FilterExprParser.VariableParentsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code queryParents}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterQueryParents(FilterExprParser.QueryParentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code queryParents}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitQueryParents(FilterExprParser.QueryParentsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueCondition(FilterExprParser.ValueConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueCondition(FilterExprParser.ValueConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterArrayCondition(FilterExprParser.ArrayConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitArrayCondition(FilterExprParser.ArrayConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueVarCondition(FilterExprParser.ValueVarConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueVarCondition(FilterExprParser.ValueVarConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterArrayVarCondition(FilterExprParser.ArrayVarConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitArrayVarCondition(FilterExprParser.ArrayVarConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueLikeCondition(FilterExprParser.ValueLikeConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueLikeCondition(FilterExprParser.ValueLikeConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueLikeLeftCondition(FilterExprParser.ValueLikeLeftConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueLikeLeftCondition(FilterExprParser.ValueLikeLeftConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueLikeRightCondition(FilterExprParser.ValueLikeRightConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueLikeRightCondition(FilterExprParser.ValueLikeRightConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueVarLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueVarLikeCondition(FilterExprParser.ValueVarLikeConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueVarLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueVarLikeCondition(FilterExprParser.ValueVarLikeConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueVarLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueVarLikeLeftCondition(FilterExprParser.ValueVarLikeLeftConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueVarLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueVarLikeLeftCondition(FilterExprParser.ValueVarLikeLeftConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueVarLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterValueVarLikeRightCondition(FilterExprParser.ValueVarLikeRightConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueVarLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitValueVarLikeRightCondition(FilterExprParser.ValueVarLikeRightConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueCondition(FilterExprParser.EmptyValueConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueCondition(FilterExprParser.EmptyValueConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyArrayCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyArrayCondition(FilterExprParser.EmptyArrayConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyArrayCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyArrayCondition(FilterExprParser.EmptyArrayConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueLikeCondition(FilterExprParser.EmptyValueLikeConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueLikeCondition(FilterExprParser.EmptyValueLikeConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueLikeLeftCondition(FilterExprParser.EmptyValueLikeLeftConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueLikeLeftCondition(FilterExprParser.EmptyValueLikeLeftConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueLikeRightCondition(FilterExprParser.EmptyValueLikeRightConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueLikeRightCondition(FilterExprParser.EmptyValueLikeRightConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueVarCondition(FilterExprParser.EmptyValueVarConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueVarCondition(FilterExprParser.EmptyValueVarConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyArrayVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyArrayVarCondition(FilterExprParser.EmptyArrayVarConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyArrayVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyArrayVarCondition(FilterExprParser.EmptyArrayVarConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueVarLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueVarLikeCondition(FilterExprParser.EmptyValueVarLikeConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueVarLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueVarLikeCondition(FilterExprParser.EmptyValueVarLikeConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueVarLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueVarLikeLeftCondition(FilterExprParser.EmptyValueVarLikeLeftConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueVarLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueVarLikeLeftCondition(FilterExprParser.EmptyValueVarLikeLeftConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyValueVarLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterEmptyValueVarLikeRightCondition(FilterExprParser.EmptyValueVarLikeRightConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyValueVarLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitEmptyValueVarLikeRightCondition(FilterExprParser.EmptyValueVarLikeRightConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExprParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(FilterExprParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExprParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(FilterExprParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link FilterExprParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(FilterExprParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link FilterExprParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(FilterExprParser.ValueContext ctx);
}