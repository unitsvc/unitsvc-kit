package com.unitsvc.kit.facade.db.antlr.d3.inner;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(ExprParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayCondition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCondition(ExprParser.ArrayConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueCondition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueCondition(ExprParser.ValueConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relation}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(ExprParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(ExprParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(ExprParser.ValueContext ctx);
}