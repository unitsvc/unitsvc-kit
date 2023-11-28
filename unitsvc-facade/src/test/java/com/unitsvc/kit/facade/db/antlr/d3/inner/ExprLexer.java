package com.unitsvc.kit.facade.db.antlr.d3.inner;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, STRING=6, BOOL=7, NUMBER=8, NULL=9, 
		ID=10, WS=11, AND=12, OR=13, EQ=14, NE=15, GTE=16, LTE=17, GT=18, LT=19, 
		IN=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "STRING", "BOOL", "NUMBER", "NULL", 
			"ID", "WS", "DIGIT", "AND", "OR", "EQ", "NE", "GTE", "LTE", "GT", "LT", 
			"IN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "','", "']'", null, null, null, "'null'", 
			null, null, "'&&'", "'||'", "'=='", "'!='", "'>='", "'<='", "'>'", "'<'", 
			"'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "STRING", "BOOL", "NUMBER", "NULL", 
			"ID", "WS", "AND", "OR", "EQ", "NE", "GTE", "LTE", "GT", "LT", "IN"
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
		"\u0004\u0000\u0014\u009b\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0005\u00058\b\u0005\n\u0005\f\u0005;\t\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006H\b\u0006\u0001\u0007"+
		"\u0003\u0007K\b\u0007\u0001\u0007\u0004\u0007N\b\u0007\u000b\u0007\f\u0007"+
		"O\u0001\u0007\u0001\u0007\u0005\u0007T\b\u0007\n\u0007\f\u0007W\t\u0007"+
		"\u0003\u0007Y\b\u0007\u0001\u0007\u0001\u0007\u0003\u0007]\b\u0007\u0001"+
		"\u0007\u0004\u0007`\b\u0007\u000b\u0007\f\u0007a\u0003\u0007d\b\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0004\tl\b\t\u000b\t"+
		"\f\tm\u0001\t\u0001\t\u0004\tr\b\t\u000b\t\f\ts\u0005\tv\b\t\n\t\f\ty"+
		"\t\t\u0001\n\u0004\n|\b\n\u000b\n\f\n}\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0000\u0000\u0015"+
		"\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r"+
		"\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\u0000\u0019\f\u001b\r"+
		"\u001d\u000e\u001f\u000f!\u0010#\u0011%\u0012\'\u0013)\u0014\u0001\u0000"+
		"\u0006\u0001\u0000\"\"\u0002\u0000++--\u0002\u0000EEee\u0002\u0000AZa"+
		"z\u0003\u0000\t\n\r\r  \u0001\u000009\u00a6\u0000\u0001\u0001\u0000\u0000"+
		"\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000"+
		"\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000"+
		"\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000"+
		"\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000"+
		"\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000"+
		"\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000"+
		"\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000"+
		"!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001"+
		"\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000"+
		"\u0000\u0000\u0001+\u0001\u0000\u0000\u0000\u0003-\u0001\u0000\u0000\u0000"+
		"\u0005/\u0001\u0000\u0000\u0000\u00071\u0001\u0000\u0000\u0000\t3\u0001"+
		"\u0000\u0000\u0000\u000b5\u0001\u0000\u0000\u0000\rG\u0001\u0000\u0000"+
		"\u0000\u000fJ\u0001\u0000\u0000\u0000\u0011e\u0001\u0000\u0000\u0000\u0013"+
		"k\u0001\u0000\u0000\u0000\u0015{\u0001\u0000\u0000\u0000\u0017\u0081\u0001"+
		"\u0000\u0000\u0000\u0019\u0083\u0001\u0000\u0000\u0000\u001b\u0086\u0001"+
		"\u0000\u0000\u0000\u001d\u0089\u0001\u0000\u0000\u0000\u001f\u008c\u0001"+
		"\u0000\u0000\u0000!\u008f\u0001\u0000\u0000\u0000#\u0092\u0001\u0000\u0000"+
		"\u0000%\u0095\u0001\u0000\u0000\u0000\'\u0097\u0001\u0000\u0000\u0000"+
		")\u0099\u0001\u0000\u0000\u0000+,\u0005(\u0000\u0000,\u0002\u0001\u0000"+
		"\u0000\u0000-.\u0005)\u0000\u0000.\u0004\u0001\u0000\u0000\u0000/0\u0005"+
		"[\u0000\u00000\u0006\u0001\u0000\u0000\u000012\u0005,\u0000\u00002\b\u0001"+
		"\u0000\u0000\u000034\u0005]\u0000\u00004\n\u0001\u0000\u0000\u000059\u0005"+
		"\"\u0000\u000068\b\u0000\u0000\u000076\u0001\u0000\u0000\u00008;\u0001"+
		"\u0000\u0000\u000097\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000"+
		":<\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000<=\u0005\"\u0000\u0000"+
		"=\f\u0001\u0000\u0000\u0000>?\u0005t\u0000\u0000?@\u0005r\u0000\u0000"+
		"@A\u0005u\u0000\u0000AH\u0005e\u0000\u0000BC\u0005f\u0000\u0000CD\u0005"+
		"a\u0000\u0000DE\u0005l\u0000\u0000EF\u0005s\u0000\u0000FH\u0005e\u0000"+
		"\u0000G>\u0001\u0000\u0000\u0000GB\u0001\u0000\u0000\u0000H\u000e\u0001"+
		"\u0000\u0000\u0000IK\u0007\u0001\u0000\u0000JI\u0001\u0000\u0000\u0000"+
		"JK\u0001\u0000\u0000\u0000KM\u0001\u0000\u0000\u0000LN\u0003\u0017\u000b"+
		"\u0000ML\u0001\u0000\u0000\u0000NO\u0001\u0000\u0000\u0000OM\u0001\u0000"+
		"\u0000\u0000OP\u0001\u0000\u0000\u0000PX\u0001\u0000\u0000\u0000QU\u0005"+
		".\u0000\u0000RT\u0003\u0017\u000b\u0000SR\u0001\u0000\u0000\u0000TW\u0001"+
		"\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000"+
		"VY\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000XQ\u0001\u0000\u0000"+
		"\u0000XY\u0001\u0000\u0000\u0000Yc\u0001\u0000\u0000\u0000Z\\\u0007\u0002"+
		"\u0000\u0000[]\u0007\u0001\u0000\u0000\\[\u0001\u0000\u0000\u0000\\]\u0001"+
		"\u0000\u0000\u0000]_\u0001\u0000\u0000\u0000^`\u0003\u0017\u000b\u0000"+
		"_^\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000"+
		"\u0000ab\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000cZ\u0001\u0000"+
		"\u0000\u0000cd\u0001\u0000\u0000\u0000d\u0010\u0001\u0000\u0000\u0000"+
		"ef\u0005n\u0000\u0000fg\u0005u\u0000\u0000gh\u0005l\u0000\u0000hi\u0005"+
		"l\u0000\u0000i\u0012\u0001\u0000\u0000\u0000jl\u0007\u0003\u0000\u0000"+
		"kj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000nw\u0001\u0000\u0000\u0000oq\u0005.\u0000"+
		"\u0000pr\u0007\u0003\u0000\u0000qp\u0001\u0000\u0000\u0000rs\u0001\u0000"+
		"\u0000\u0000sq\u0001\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tv\u0001"+
		"\u0000\u0000\u0000uo\u0001\u0000\u0000\u0000vy\u0001\u0000\u0000\u0000"+
		"wu\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000x\u0014\u0001\u0000"+
		"\u0000\u0000yw\u0001\u0000\u0000\u0000z|\u0007\u0004\u0000\u0000{z\u0001"+
		"\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000"+
		"}~\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0080"+
		"\u0006\n\u0000\u0000\u0080\u0016\u0001\u0000\u0000\u0000\u0081\u0082\u0007"+
		"\u0005\u0000\u0000\u0082\u0018\u0001\u0000\u0000\u0000\u0083\u0084\u0005"+
		"&\u0000\u0000\u0084\u0085\u0005&\u0000\u0000\u0085\u001a\u0001\u0000\u0000"+
		"\u0000\u0086\u0087\u0005|\u0000\u0000\u0087\u0088\u0005|\u0000\u0000\u0088"+
		"\u001c\u0001\u0000\u0000\u0000\u0089\u008a\u0005=\u0000\u0000\u008a\u008b"+
		"\u0005=\u0000\u0000\u008b\u001e\u0001\u0000\u0000\u0000\u008c\u008d\u0005"+
		"!\u0000\u0000\u008d\u008e\u0005=\u0000\u0000\u008e \u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0005>\u0000\u0000\u0090\u0091\u0005=\u0000\u0000\u0091"+
		"\"\u0001\u0000\u0000\u0000\u0092\u0093\u0005<\u0000\u0000\u0093\u0094"+
		"\u0005=\u0000\u0000\u0094$\u0001\u0000\u0000\u0000\u0095\u0096\u0005>"+
		"\u0000\u0000\u0096&\u0001\u0000\u0000\u0000\u0097\u0098\u0005<\u0000\u0000"+
		"\u0098(\u0001\u0000\u0000\u0000\u0099\u009a\u0005@\u0000\u0000\u009a*"+
		"\u0001\u0000\u0000\u0000\u000e\u00009GJOUX\\acmsw}\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}