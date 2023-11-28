package com.unitsvc.kit.facade.antlr4.inner;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FilterExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FilterExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FilterExprParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(FilterExprParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExprParser#orExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(FilterExprParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExprParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(FilterExprParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableParents}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableParents(FilterExprParser.VariableParentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code queryParents}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryParents(FilterExprParser.QueryParentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueCondition(FilterExprParser.ValueConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCondition(FilterExprParser.ArrayConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueVarCondition(FilterExprParser.ValueVarConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVarCondition(FilterExprParser.ArrayVarConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueLikeCondition(FilterExprParser.ValueLikeConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueLikeLeftCondition(FilterExprParser.ValueLikeLeftConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueLikeRightCondition(FilterExprParser.ValueLikeRightConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueVarLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueVarLikeCondition(FilterExprParser.ValueVarLikeConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueVarLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueVarLikeLeftCondition(FilterExprParser.ValueVarLikeLeftConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueVarLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueVarLikeRightCondition(FilterExprParser.ValueVarLikeRightConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueCondition(FilterExprParser.EmptyValueConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyArrayCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyArrayCondition(FilterExprParser.EmptyArrayConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueLikeCondition(FilterExprParser.EmptyValueLikeConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueLikeLeftCondition(FilterExprParser.EmptyValueLikeLeftConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueLikeRightCondition(FilterExprParser.EmptyValueLikeRightConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueVarCondition(FilterExprParser.EmptyValueVarConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyArrayVarCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyArrayVarCondition(FilterExprParser.EmptyArrayVarConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueVarLikeCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueVarLikeCondition(FilterExprParser.EmptyValueVarLikeConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueVarLikeLeftCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueVarLikeLeftCondition(FilterExprParser.EmptyValueVarLikeLeftConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyValueVarLikeRightCondition}
	 * labeled alternative in {@link FilterExprParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyValueVarLikeRightCondition(FilterExprParser.EmptyValueVarLikeRightConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExprParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(FilterExprParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link FilterExprParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(FilterExprParser.ValueContext ctx);
}