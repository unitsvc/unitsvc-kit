package com.unitsvc.kit.facade.db.antlr.d4.inner;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, BOOL=6, NUMBER=7, NULL=8, ID=9, 
		WS=10, STRING=11, AND=12, OR=13, EQ=14, NE=15, GTE=16, LTE=17, GT=18, 
		LT=19, IN=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "BOOL", "NUMBER", "NULL", "ID", 
			"WS", "STRING", "DIGIT", "AND", "OR", "EQ", "NE", "GTE", "LTE", "GT", 
			"LT", "IN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "','", "']'", null, null, "'null'", null, 
			null, null, "'&&'", "'||'", "'=='", "'!='", "'>='", "'<='", "'>'", "'<'", 
			"'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "BOOL", "NUMBER", "NULL", "ID", "WS", 
			"STRING", "AND", "OR", "EQ", "NE", "GTE", "LTE", "GT", "LT", "IN"
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


	public ExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0014\u00a9\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005?\b\u0005\u0001\u0006\u0003\u0006B\b\u0006\u0001"+
		"\u0006\u0004\u0006E\b\u0006\u000b\u0006\f\u0006F\u0001\u0006\u0001\u0006"+
		"\u0005\u0006K\b\u0006\n\u0006\f\u0006N\t\u0006\u0003\u0006P\b\u0006\u0001"+
		"\u0006\u0001\u0006\u0003\u0006T\b\u0006\u0001\u0006\u0004\u0006W\b\u0006"+
		"\u000b\u0006\f\u0006X\u0003\u0006[\b\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\b\u0004\bc\b\b\u000b\b\f\bd\u0001"+
		"\b\u0001\b\u0004\bi\b\b\u000b\b\f\bj\u0005\bm\b\b\n\b\f\bp\t\b\u0001\t"+
		"\u0004\ts\b\t\u000b\t\f\tt\u0001\t\u0001\t\u0001\n\u0001\n\u0005\n{\b"+
		"\n\n\n\f\n~\t\n\u0001\n\u0001\n\u0001\n\u0005\n\u0083\b\n\n\n\f\n\u0086"+
		"\t\n\u0001\n\u0001\n\u0004\n\u008a\b\n\u000b\n\f\n\u008b\u0003\n\u008e"+
		"\b\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0000\u0000\u0015\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005"+
		"\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\u0000\u0019"+
		"\f\u001b\r\u001d\u000e\u001f\u000f!\u0010#\u0011%\u0012\'\u0013)\u0014"+
		"\u0001\u0000\u0007\u0002\u0000++--\u0002\u0000EEee\u0002\u0000AZaz\u0003"+
		"\u0000\t\n\r\r  \u0001\u0000\"\"\u0001\u0000\'\'\u0001\u000009\u00b8\u0000"+
		"\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000"+
		"\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000"+
		"\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r"+
		"\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0000)\u0001\u0000\u0000\u0000\u0001+\u0001\u0000\u0000\u0000\u0003"+
		"-\u0001\u0000\u0000\u0000\u0005/\u0001\u0000\u0000\u0000\u00071\u0001"+
		"\u0000\u0000\u0000\t3\u0001\u0000\u0000\u0000\u000b>\u0001\u0000\u0000"+
		"\u0000\rA\u0001\u0000\u0000\u0000\u000f\\\u0001\u0000\u0000\u0000\u0011"+
		"b\u0001\u0000\u0000\u0000\u0013r\u0001\u0000\u0000\u0000\u0015\u008d\u0001"+
		"\u0000\u0000\u0000\u0017\u008f\u0001\u0000\u0000\u0000\u0019\u0091\u0001"+
		"\u0000\u0000\u0000\u001b\u0094\u0001\u0000\u0000\u0000\u001d\u0097\u0001"+
		"\u0000\u0000\u0000\u001f\u009a\u0001\u0000\u0000\u0000!\u009d\u0001\u0000"+
		"\u0000\u0000#\u00a0\u0001\u0000\u0000\u0000%\u00a3\u0001\u0000\u0000\u0000"+
		"\'\u00a5\u0001\u0000\u0000\u0000)\u00a7\u0001\u0000\u0000\u0000+,\u0005"+
		"(\u0000\u0000,\u0002\u0001\u0000\u0000\u0000-.\u0005)\u0000\u0000.\u0004"+
		"\u0001\u0000\u0000\u0000/0\u0005[\u0000\u00000\u0006\u0001\u0000\u0000"+
		"\u000012\u0005,\u0000\u00002\b\u0001\u0000\u0000\u000034\u0005]\u0000"+
		"\u00004\n\u0001\u0000\u0000\u000056\u0005t\u0000\u000067\u0005r\u0000"+
		"\u000078\u0005u\u0000\u00008?\u0005e\u0000\u00009:\u0005f\u0000\u0000"+
		":;\u0005a\u0000\u0000;<\u0005l\u0000\u0000<=\u0005s\u0000\u0000=?\u0005"+
		"e\u0000\u0000>5\u0001\u0000\u0000\u0000>9\u0001\u0000\u0000\u0000?\f\u0001"+
		"\u0000\u0000\u0000@B\u0007\u0000\u0000\u0000A@\u0001\u0000\u0000\u0000"+
		"AB\u0001\u0000\u0000\u0000BD\u0001\u0000\u0000\u0000CE\u0003\u0017\u000b"+
		"\u0000DC\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000FD\u0001\u0000"+
		"\u0000\u0000FG\u0001\u0000\u0000\u0000GO\u0001\u0000\u0000\u0000HL\u0005"+
		".\u0000\u0000IK\u0003\u0017\u000b\u0000JI\u0001\u0000\u0000\u0000KN\u0001"+
		"\u0000\u0000\u0000LJ\u0001\u0000\u0000\u0000LM\u0001\u0000\u0000\u0000"+
		"MP\u0001\u0000\u0000\u0000NL\u0001\u0000\u0000\u0000OH\u0001\u0000\u0000"+
		"\u0000OP\u0001\u0000\u0000\u0000PZ\u0001\u0000\u0000\u0000QS\u0007\u0001"+
		"\u0000\u0000RT\u0007\u0000\u0000\u0000SR\u0001\u0000\u0000\u0000ST\u0001"+
		"\u0000\u0000\u0000TV\u0001\u0000\u0000\u0000UW\u0003\u0017\u000b\u0000"+
		"VU\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000"+
		"\u0000XY\u0001\u0000\u0000\u0000Y[\u0001\u0000\u0000\u0000ZQ\u0001\u0000"+
		"\u0000\u0000Z[\u0001\u0000\u0000\u0000[\u000e\u0001\u0000\u0000\u0000"+
		"\\]\u0005n\u0000\u0000]^\u0005u\u0000\u0000^_\u0005l\u0000\u0000_`\u0005"+
		"l\u0000\u0000`\u0010\u0001\u0000\u0000\u0000ac\u0007\u0002\u0000\u0000"+
		"ba\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000db\u0001\u0000\u0000"+
		"\u0000de\u0001\u0000\u0000\u0000en\u0001\u0000\u0000\u0000fh\u0005.\u0000"+
		"\u0000gi\u0007\u0002\u0000\u0000hg\u0001\u0000\u0000\u0000ij\u0001\u0000"+
		"\u0000\u0000jh\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000km\u0001"+
		"\u0000\u0000\u0000lf\u0001\u0000\u0000\u0000mp\u0001\u0000\u0000\u0000"+
		"nl\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000o\u0012\u0001\u0000"+
		"\u0000\u0000pn\u0001\u0000\u0000\u0000qs\u0007\u0003\u0000\u0000rq\u0001"+
		"\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000"+
		"tu\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vw\u0006\t\u0000\u0000"+
		"w\u0014\u0001\u0000\u0000\u0000x|\u0005\"\u0000\u0000y{\b\u0004\u0000"+
		"\u0000zy\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000|z\u0001\u0000"+
		"\u0000\u0000|}\u0001\u0000\u0000\u0000}\u007f\u0001\u0000\u0000\u0000"+
		"~|\u0001\u0000\u0000\u0000\u007f\u008e\u0005\"\u0000\u0000\u0080\u0084"+
		"\u0005\'\u0000\u0000\u0081\u0083\b\u0005\u0000\u0000\u0082\u0081\u0001"+
		"\u0000\u0000\u0000\u0083\u0086\u0001\u0000\u0000\u0000\u0084\u0082\u0001"+
		"\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u0087\u0001"+
		"\u0000\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0087\u008e\u0005"+
		"\'\u0000\u0000\u0088\u008a\u0007\u0002\u0000\u0000\u0089\u0088\u0001\u0000"+
		"\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u0089\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008e\u0001\u0000"+
		"\u0000\u0000\u008dx\u0001\u0000\u0000\u0000\u008d\u0080\u0001\u0000\u0000"+
		"\u0000\u008d\u0089\u0001\u0000\u0000\u0000\u008e\u0016\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0007\u0006\u0000\u0000\u0090\u0018\u0001\u0000\u0000"+
		"\u0000\u0091\u0092\u0005&\u0000\u0000\u0092\u0093\u0005&\u0000\u0000\u0093"+
		"\u001a\u0001\u0000\u0000\u0000\u0094\u0095\u0005|\u0000\u0000\u0095\u0096"+
		"\u0005|\u0000\u0000\u0096\u001c\u0001\u0000\u0000\u0000\u0097\u0098\u0005"+
		"=\u0000\u0000\u0098\u0099\u0005=\u0000\u0000\u0099\u001e\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0005!\u0000\u0000\u009b\u009c\u0005=\u0000\u0000\u009c"+
		" \u0001\u0000\u0000\u0000\u009d\u009e\u0005>\u0000\u0000\u009e\u009f\u0005"+
		"=\u0000\u0000\u009f\"\u0001\u0000\u0000\u0000\u00a0\u00a1\u0005<\u0000"+
		"\u0000\u00a1\u00a2\u0005=\u0000\u0000\u00a2$\u0001\u0000\u0000\u0000\u00a3"+
		"\u00a4\u0005>\u0000\u0000\u00a4&\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005"+
		"<\u0000\u0000\u00a6(\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005@\u0000"+
		"\u0000\u00a8*\u0001\u0000\u0000\u0000\u0011\u0000>AFLOSXZdjnt|\u0084\u008b"+
		"\u008d\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}