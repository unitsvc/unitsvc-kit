package com.unitsvc.kit.facade.db.antlr.d2.inner;
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
	 * Visit a parse tree produced by the {@code condition}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(ExprParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relation}
	 * labeled alternative in {@link ExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(ExprParser.RelationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(ExprParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#str}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStr(ExprParser.StrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(ExprParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#decimal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimal(ExprParser.DecimalContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#negativeDecimal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegativeDecimal(ExprParser.NegativeDecimalContext ctx);
}