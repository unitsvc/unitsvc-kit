package com.unitsvc.kit.facade.antlr4.inner;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class FilterExprParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, BOOL=19, STRING=20, NUMBER=21, NULL=22, JavaScript=23, ID=24, 
		WS=25, AND=26, OR=27, EQ=28, NE=29, GTE=30, LTE=31, GT=32, LT=33, IN=34;
	public static final int
		RULE_query = 0, RULE_orExpression = 1, RULE_andExpression = 2, RULE_variable = 3, 
		RULE_array = 4, RULE_value = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"query", "orExpression", "andExpression", "variable", "array", "value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'$'", "'$$'", "'like'", "'LIKE'", "'l_like'", "'L_LIKE'", 
			"'left_like'", "'LEFT_LIKE'", "'r_like'", "'R_LIKE'", "'right_like'", 
			"'RIGHT_LIKE'", "'['", "','", "']'", "'eval'", null, null, null, "'null'", 
			null, null, null, "'&&'", "'||'", "'=='", "'!='", "'>='", "'<='", "'>'", 
			"'<'", "'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "BOOL", "STRING", "NUMBER", 
			"NULL", "JavaScript", "ID", "WS", "AND", "OR", "EQ", "NE", "GTE", "LTE", 
			"GT", "LT", "IN"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "FilterExpr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FilterExprParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryContext extends ParserRuleContext {
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			orExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrExpressionContext extends ParserRuleContext {
		public List<AndExpressionContext> andExpression() {
			return getRuleContexts(AndExpressionContext.class);
		}
		public AndExpressionContext andExpression(int i) {
			return getRuleContext(AndExpressionContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(FilterExprParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(FilterExprParser.OR, i);
		}
		public OrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrExpressionContext orExpression() throws RecognitionException {
		OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_orExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			andExpression();
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(15);
				match(OR);
				setState(16);
				andExpression();
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AndExpressionContext extends ParserRuleContext {
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(FilterExprParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(FilterExprParser.AND, i);
		}
		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			variable();
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(23);
				match(AND);
				setState(24);
				variable();
				}
				}
				setState(29);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
	 
		public VariableContext() { }
		public void copyFrom(VariableContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyArrayVarConditionContext extends VariableContext {
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public TerminalNode IN() { return getToken(FilterExprParser.IN, 0); }
		public EmptyArrayVarConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyArrayVarCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyArrayVarCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyArrayVarCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueVarLikeLeftConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public EmptyValueVarLikeLeftConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueVarLikeLeftCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueVarLikeLeftCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueVarLikeLeftCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableParentsContext extends VariableContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public VariableParentsContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterVariableParents(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitVariableParents(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitVariableParents(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode EQ() { return getToken(FilterExprParser.EQ, 0); }
		public TerminalNode NE() { return getToken(FilterExprParser.NE, 0); }
		public TerminalNode GTE() { return getToken(FilterExprParser.GTE, 0); }
		public TerminalNode LTE() { return getToken(FilterExprParser.LTE, 0); }
		public TerminalNode GT() { return getToken(FilterExprParser.GT, 0); }
		public TerminalNode LT() { return getToken(FilterExprParser.LT, 0); }
		public ValueConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueLikeConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ValueLikeConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueLikeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueLikeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueLikeCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueVarLikeConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public EmptyValueVarLikeConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueVarLikeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueVarLikeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueVarLikeCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueLikeConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public EmptyValueLikeConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueLikeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueLikeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueLikeCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueVarConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public TerminalNode EQ() { return getToken(FilterExprParser.EQ, 0); }
		public TerminalNode NE() { return getToken(FilterExprParser.NE, 0); }
		public TerminalNode GTE() { return getToken(FilterExprParser.GTE, 0); }
		public TerminalNode LTE() { return getToken(FilterExprParser.LTE, 0); }
		public TerminalNode GT() { return getToken(FilterExprParser.GT, 0); }
		public TerminalNode LT() { return getToken(FilterExprParser.LT, 0); }
		public EmptyValueVarConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueVarCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueVarCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueVarCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayVarConditionContext extends VariableContext {
		public List<TerminalNode> ID() { return getTokens(FilterExprParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(FilterExprParser.ID, i);
		}
		public TerminalNode IN() { return getToken(FilterExprParser.IN, 0); }
		public ArrayVarConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterArrayVarCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitArrayVarCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitArrayVarCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueLikeLeftConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ValueLikeLeftConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueLikeLeftCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueLikeLeftCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueLikeLeftCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueLikeLeftConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public EmptyValueLikeLeftConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueLikeLeftCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueLikeLeftCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueLikeLeftCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueVarConditionContext extends VariableContext {
		public Token op;
		public List<TerminalNode> ID() { return getTokens(FilterExprParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(FilterExprParser.ID, i);
		}
		public TerminalNode EQ() { return getToken(FilterExprParser.EQ, 0); }
		public TerminalNode NE() { return getToken(FilterExprParser.NE, 0); }
		public TerminalNode GTE() { return getToken(FilterExprParser.GTE, 0); }
		public TerminalNode LTE() { return getToken(FilterExprParser.LTE, 0); }
		public TerminalNode GT() { return getToken(FilterExprParser.GT, 0); }
		public TerminalNode LT() { return getToken(FilterExprParser.LT, 0); }
		public ValueVarConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueVarCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueVarCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueVarCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayConditionContext extends VariableContext {
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public TerminalNode IN() { return getToken(FilterExprParser.IN, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrayConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterArrayCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitArrayCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitArrayCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueVarLikeConditionContext extends VariableContext {
		public Token op;
		public List<TerminalNode> ID() { return getTokens(FilterExprParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(FilterExprParser.ID, i);
		}
		public ValueVarLikeConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueVarLikeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueVarLikeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueVarLikeCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueLikeRightConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ValueLikeRightConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueLikeRightCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueLikeRightCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueLikeRightCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueLikeRightConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public EmptyValueLikeRightConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueLikeRightCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueLikeRightCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueLikeRightCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueVarLikeRightConditionContext extends VariableContext {
		public Token op;
		public List<TerminalNode> ID() { return getTokens(FilterExprParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(FilterExprParser.ID, i);
		}
		public ValueVarLikeRightConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueVarLikeRightCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueVarLikeRightCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueVarLikeRightCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueVarLikeRightConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public EmptyValueVarLikeRightConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueVarLikeRightCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueVarLikeRightCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueVarLikeRightCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyArrayConditionContext extends VariableContext {
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public TerminalNode IN() { return getToken(FilterExprParser.IN, 0); }
		public EmptyArrayConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyArrayCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyArrayCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyArrayCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class QueryParentsContext extends VariableContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public QueryParentsContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterQueryParents(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitQueryParents(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitQueryParents(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueVarLikeLeftConditionContext extends VariableContext {
		public Token op;
		public List<TerminalNode> ID() { return getTokens(FilterExprParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(FilterExprParser.ID, i);
		}
		public ValueVarLikeLeftConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValueVarLikeLeftCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValueVarLikeLeftCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValueVarLikeLeftCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyValueConditionContext extends VariableContext {
		public Token op;
		public TerminalNode ID() { return getToken(FilterExprParser.ID, 0); }
		public TerminalNode EQ() { return getToken(FilterExprParser.EQ, 0); }
		public TerminalNode NE() { return getToken(FilterExprParser.NE, 0); }
		public TerminalNode GTE() { return getToken(FilterExprParser.GTE, 0); }
		public TerminalNode LTE() { return getToken(FilterExprParser.LTE, 0); }
		public TerminalNode GT() { return getToken(FilterExprParser.GT, 0); }
		public TerminalNode LT() { return getToken(FilterExprParser.LT, 0); }
		public EmptyValueConditionContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterEmptyValueCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitEmptyValueCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitEmptyValueCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_variable);
		int _la;
		try {
			setState(118);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new VariableParentsContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				match(T__0);
				setState(31);
				variable();
				setState(32);
				match(T__1);
				}
				break;
			case 2:
				_localctx = new QueryParentsContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(34);
				match(T__0);
				setState(35);
				query();
				setState(36);
				match(T__1);
				}
				break;
			case 3:
				_localctx = new ValueConditionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(38);
				match(T__2);
				setState(39);
				match(ID);
				setState(40);
				((ValueConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16911433728L) != 0)) ) {
					((ValueConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(41);
				value();
				}
				break;
			case 4:
				_localctx = new ArrayConditionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(42);
				match(T__2);
				setState(43);
				match(ID);
				setState(44);
				match(IN);
				setState(45);
				array();
				}
				break;
			case 5:
				_localctx = new ValueVarConditionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(46);
				match(T__2);
				setState(47);
				match(ID);
				setState(48);
				((ValueVarConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16911433728L) != 0)) ) {
					((ValueVarConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(49);
				match(T__3);
				setState(50);
				match(ID);
				}
				break;
			case 6:
				_localctx = new ArrayVarConditionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(51);
				match(T__2);
				setState(52);
				match(ID);
				setState(53);
				match(IN);
				setState(54);
				match(T__3);
				setState(55);
				match(ID);
				}
				break;
			case 7:
				_localctx = new ValueLikeConditionContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(56);
				match(T__2);
				setState(57);
				match(ID);
				setState(58);
				((ValueLikeConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__4 || _la==T__5) ) {
					((ValueLikeConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(59);
				value();
				}
				break;
			case 8:
				_localctx = new ValueLikeLeftConditionContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(60);
				match(T__2);
				setState(61);
				match(ID);
				setState(62);
				((ValueLikeLeftConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1920L) != 0)) ) {
					((ValueLikeLeftConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(63);
				value();
				}
				break;
			case 9:
				_localctx = new ValueLikeRightConditionContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(64);
				match(T__2);
				setState(65);
				match(ID);
				setState(66);
				((ValueLikeRightConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30720L) != 0)) ) {
					((ValueLikeRightConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(67);
				value();
				}
				break;
			case 10:
				_localctx = new ValueVarLikeConditionContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(68);
				match(T__2);
				setState(69);
				match(ID);
				setState(70);
				((ValueVarLikeConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__5 || _la==T__6) ) {
					((ValueVarLikeConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(71);
				match(T__3);
				setState(72);
				match(ID);
				}
				break;
			case 11:
				_localctx = new ValueVarLikeLeftConditionContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(73);
				match(T__2);
				setState(74);
				match(ID);
				setState(75);
				((ValueVarLikeLeftConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1920L) != 0)) ) {
					((ValueVarLikeLeftConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(76);
				match(T__3);
				setState(77);
				match(ID);
				}
				break;
			case 12:
				_localctx = new ValueVarLikeRightConditionContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(78);
				match(T__2);
				setState(79);
				match(ID);
				setState(80);
				((ValueVarLikeRightConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30720L) != 0)) ) {
					((ValueVarLikeRightConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(81);
				match(T__3);
				setState(82);
				match(ID);
				}
				break;
			case 13:
				_localctx = new EmptyValueConditionContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(83);
				match(T__2);
				setState(84);
				match(ID);
				setState(85);
				((EmptyValueConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16911433728L) != 0)) ) {
					((EmptyValueConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 14:
				_localctx = new EmptyArrayConditionContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(86);
				match(T__2);
				setState(87);
				match(ID);
				setState(88);
				match(IN);
				}
				break;
			case 15:
				_localctx = new EmptyValueLikeConditionContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(89);
				match(T__2);
				setState(90);
				match(ID);
				setState(91);
				((EmptyValueLikeConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__4 || _la==T__5) ) {
					((EmptyValueLikeConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 16:
				_localctx = new EmptyValueLikeLeftConditionContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(92);
				match(T__2);
				setState(93);
				match(ID);
				setState(94);
				((EmptyValueLikeLeftConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1920L) != 0)) ) {
					((EmptyValueLikeLeftConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 17:
				_localctx = new EmptyValueLikeRightConditionContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(95);
				match(T__2);
				setState(96);
				match(ID);
				setState(97);
				((EmptyValueLikeRightConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30720L) != 0)) ) {
					((EmptyValueLikeRightConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case 18:
				_localctx = new EmptyValueVarConditionContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(98);
				match(T__2);
				setState(99);
				match(ID);
				setState(100);
				((EmptyValueVarConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16911433728L) != 0)) ) {
					((EmptyValueVarConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(101);
				match(T__3);
				}
				break;
			case 19:
				_localctx = new EmptyArrayVarConditionContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(102);
				match(T__2);
				setState(103);
				match(ID);
				setState(104);
				match(IN);
				setState(105);
				match(T__3);
				}
				break;
			case 20:
				_localctx = new EmptyValueVarLikeConditionContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(106);
				match(T__2);
				setState(107);
				match(ID);
				setState(108);
				((EmptyValueVarLikeConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__4 || _la==T__5) ) {
					((EmptyValueVarLikeConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(109);
				match(T__3);
				}
				break;
			case 21:
				_localctx = new EmptyValueVarLikeLeftConditionContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(110);
				match(T__2);
				setState(111);
				match(ID);
				setState(112);
				((EmptyValueVarLikeLeftConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1920L) != 0)) ) {
					((EmptyValueVarLikeLeftConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(113);
				match(T__3);
				}
				break;
			case 22:
				_localctx = new EmptyValueVarLikeRightConditionContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(114);
				match(T__2);
				setState(115);
				match(ID);
				setState(116);
				((EmptyValueVarLikeRightConditionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30720L) != 0)) ) {
					((EmptyValueVarLikeRightConditionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(117);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode JavaScript() { return getToken(FilterExprParser.JavaScript, 0); }
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_array);
		int _la;
		try {
			setState(135);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__14:
				enterOuterAlt(_localctx, 1);
				{
				setState(120);
				match(T__14);
				setState(121);
				value();
				setState(126);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__15) {
					{
					{
					setState(122);
					match(T__15);
					setState(123);
					value();
					}
					}
					setState(128);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(129);
				match(T__16);
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 2);
				{
				setState(131);
				match(T__17);
				setState(132);
				match(T__0);
				setState(133);
				match(JavaScript);
				setState(134);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(FilterExprParser.STRING, 0); }
		public TerminalNode BOOL() { return getToken(FilterExprParser.BOOL, 0); }
		public TerminalNode NUMBER() { return getToken(FilterExprParser.NUMBER, 0); }
		public TerminalNode NULL() { return getToken(FilterExprParser.NULL, 0); }
		public TerminalNode JavaScript() { return getToken(FilterExprParser.JavaScript, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterExprListener ) ((FilterExprListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterExprVisitor ) return ((FilterExprVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_value);
		try {
			setState(145);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(137);
				match(STRING);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(138);
				match(BOOL);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 3);
				{
				setState(139);
				match(NUMBER);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 4);
				{
				setState(140);
				match(NULL);
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 5);
				{
				setState(141);
				match(T__17);
				setState(142);
				match(T__0);
				setState(143);
				match(JavaScript);
				setState(144);
				match(T__1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\"\u0094\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0005\u0001\u0012\b\u0001\n\u0001\f\u0001\u0015\t\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0005\u0002\u001a\b\u0002\n\u0002\f\u0002\u001d"+
		"\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003w\b"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004}\b"+
		"\u0004\n\u0004\f\u0004\u0080\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u0088\b\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005\u0092\b\u0005\u0001\u0005\u0000\u0000\u0006\u0000"+
		"\u0002\u0004\u0006\b\n\u0000\u0005\u0001\u0000\u001c!\u0001\u0000\u0005"+
		"\u0006\u0001\u0000\u0007\n\u0001\u0000\u000b\u000e\u0001\u0000\u0006\u0007"+
		"\u00aa\u0000\f\u0001\u0000\u0000\u0000\u0002\u000e\u0001\u0000\u0000\u0000"+
		"\u0004\u0016\u0001\u0000\u0000\u0000\u0006v\u0001\u0000\u0000\u0000\b"+
		"\u0087\u0001\u0000\u0000\u0000\n\u0091\u0001\u0000\u0000\u0000\f\r\u0003"+
		"\u0002\u0001\u0000\r\u0001\u0001\u0000\u0000\u0000\u000e\u0013\u0003\u0004"+
		"\u0002\u0000\u000f\u0010\u0005\u001b\u0000\u0000\u0010\u0012\u0003\u0004"+
		"\u0002\u0000\u0011\u000f\u0001\u0000\u0000\u0000\u0012\u0015\u0001\u0000"+
		"\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0013\u0014\u0001\u0000"+
		"\u0000\u0000\u0014\u0003\u0001\u0000\u0000\u0000\u0015\u0013\u0001\u0000"+
		"\u0000\u0000\u0016\u001b\u0003\u0006\u0003\u0000\u0017\u0018\u0005\u001a"+
		"\u0000\u0000\u0018\u001a\u0003\u0006\u0003\u0000\u0019\u0017\u0001\u0000"+
		"\u0000\u0000\u001a\u001d\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u0005\u0001\u0000"+
		"\u0000\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001e\u001f\u0005\u0001"+
		"\u0000\u0000\u001f \u0003\u0006\u0003\u0000 !\u0005\u0002\u0000\u0000"+
		"!w\u0001\u0000\u0000\u0000\"#\u0005\u0001\u0000\u0000#$\u0003\u0000\u0000"+
		"\u0000$%\u0005\u0002\u0000\u0000%w\u0001\u0000\u0000\u0000&\'\u0005\u0003"+
		"\u0000\u0000\'(\u0005\u0018\u0000\u0000()\u0007\u0000\u0000\u0000)w\u0003"+
		"\n\u0005\u0000*+\u0005\u0003\u0000\u0000+,\u0005\u0018\u0000\u0000,-\u0005"+
		"\"\u0000\u0000-w\u0003\b\u0004\u0000./\u0005\u0003\u0000\u0000/0\u0005"+
		"\u0018\u0000\u000001\u0007\u0000\u0000\u000012\u0005\u0004\u0000\u0000"+
		"2w\u0005\u0018\u0000\u000034\u0005\u0003\u0000\u000045\u0005\u0018\u0000"+
		"\u000056\u0005\"\u0000\u000067\u0005\u0004\u0000\u00007w\u0005\u0018\u0000"+
		"\u000089\u0005\u0003\u0000\u00009:\u0005\u0018\u0000\u0000:;\u0007\u0001"+
		"\u0000\u0000;w\u0003\n\u0005\u0000<=\u0005\u0003\u0000\u0000=>\u0005\u0018"+
		"\u0000\u0000>?\u0007\u0002\u0000\u0000?w\u0003\n\u0005\u0000@A\u0005\u0003"+
		"\u0000\u0000AB\u0005\u0018\u0000\u0000BC\u0007\u0003\u0000\u0000Cw\u0003"+
		"\n\u0005\u0000DE\u0005\u0003\u0000\u0000EF\u0005\u0018\u0000\u0000FG\u0007"+
		"\u0004\u0000\u0000GH\u0005\u0004\u0000\u0000Hw\u0005\u0018\u0000\u0000"+
		"IJ\u0005\u0003\u0000\u0000JK\u0005\u0018\u0000\u0000KL\u0007\u0002\u0000"+
		"\u0000LM\u0005\u0004\u0000\u0000Mw\u0005\u0018\u0000\u0000NO\u0005\u0003"+
		"\u0000\u0000OP\u0005\u0018\u0000\u0000PQ\u0007\u0003\u0000\u0000QR\u0005"+
		"\u0004\u0000\u0000Rw\u0005\u0018\u0000\u0000ST\u0005\u0003\u0000\u0000"+
		"TU\u0005\u0018\u0000\u0000Uw\u0007\u0000\u0000\u0000VW\u0005\u0003\u0000"+
		"\u0000WX\u0005\u0018\u0000\u0000Xw\u0005\"\u0000\u0000YZ\u0005\u0003\u0000"+
		"\u0000Z[\u0005\u0018\u0000\u0000[w\u0007\u0001\u0000\u0000\\]\u0005\u0003"+
		"\u0000\u0000]^\u0005\u0018\u0000\u0000^w\u0007\u0002\u0000\u0000_`\u0005"+
		"\u0003\u0000\u0000`a\u0005\u0018\u0000\u0000aw\u0007\u0003\u0000\u0000"+
		"bc\u0005\u0003\u0000\u0000cd\u0005\u0018\u0000\u0000de\u0007\u0000\u0000"+
		"\u0000ew\u0005\u0004\u0000\u0000fg\u0005\u0003\u0000\u0000gh\u0005\u0018"+
		"\u0000\u0000hi\u0005\"\u0000\u0000iw\u0005\u0004\u0000\u0000jk\u0005\u0003"+
		"\u0000\u0000kl\u0005\u0018\u0000\u0000lm\u0007\u0001\u0000\u0000mw\u0005"+
		"\u0004\u0000\u0000no\u0005\u0003\u0000\u0000op\u0005\u0018\u0000\u0000"+
		"pq\u0007\u0002\u0000\u0000qw\u0005\u0004\u0000\u0000rs\u0005\u0003\u0000"+
		"\u0000st\u0005\u0018\u0000\u0000tu\u0007\u0003\u0000\u0000uw\u0005\u0004"+
		"\u0000\u0000v\u001e\u0001\u0000\u0000\u0000v\"\u0001\u0000\u0000\u0000"+
		"v&\u0001\u0000\u0000\u0000v*\u0001\u0000\u0000\u0000v.\u0001\u0000\u0000"+
		"\u0000v3\u0001\u0000\u0000\u0000v8\u0001\u0000\u0000\u0000v<\u0001\u0000"+
		"\u0000\u0000v@\u0001\u0000\u0000\u0000vD\u0001\u0000\u0000\u0000vI\u0001"+
		"\u0000\u0000\u0000vN\u0001\u0000\u0000\u0000vS\u0001\u0000\u0000\u0000"+
		"vV\u0001\u0000\u0000\u0000vY\u0001\u0000\u0000\u0000v\\\u0001\u0000\u0000"+
		"\u0000v_\u0001\u0000\u0000\u0000vb\u0001\u0000\u0000\u0000vf\u0001\u0000"+
		"\u0000\u0000vj\u0001\u0000\u0000\u0000vn\u0001\u0000\u0000\u0000vr\u0001"+
		"\u0000\u0000\u0000w\u0007\u0001\u0000\u0000\u0000xy\u0005\u000f\u0000"+
		"\u0000y~\u0003\n\u0005\u0000z{\u0005\u0010\u0000\u0000{}\u0003\n\u0005"+
		"\u0000|z\u0001\u0000\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001"+
		"\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0081\u0001\u0000"+
		"\u0000\u0000\u0080~\u0001\u0000\u0000\u0000\u0081\u0082\u0005\u0011\u0000"+
		"\u0000\u0082\u0088\u0001\u0000\u0000\u0000\u0083\u0084\u0005\u0012\u0000"+
		"\u0000\u0084\u0085\u0005\u0001\u0000\u0000\u0085\u0086\u0005\u0017\u0000"+
		"\u0000\u0086\u0088\u0005\u0002\u0000\u0000\u0087x\u0001\u0000\u0000\u0000"+
		"\u0087\u0083\u0001\u0000\u0000\u0000\u0088\t\u0001\u0000\u0000\u0000\u0089"+
		"\u0092\u0005\u0014\u0000\u0000\u008a\u0092\u0005\u0013\u0000\u0000\u008b"+
		"\u0092\u0005\u0015\u0000\u0000\u008c\u0092\u0005\u0016\u0000\u0000\u008d"+
		"\u008e\u0005\u0012\u0000\u0000\u008e\u008f\u0005\u0001\u0000\u0000\u008f"+
		"\u0090\u0005\u0017\u0000\u0000\u0090\u0092\u0005\u0002\u0000\u0000\u0091"+
		"\u0089\u0001\u0000\u0000\u0000\u0091\u008a\u0001\u0000\u0000\u0000\u0091"+
		"\u008b\u0001\u0000\u0000\u0000\u0091\u008c\u0001\u0000\u0000\u0000\u0091"+
		"\u008d\u0001\u0000\u0000\u0000\u0092\u000b\u0001\u0000\u0000\u0000\u0006"+
		"\u0013\u001bv~\u0087\u0091";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}