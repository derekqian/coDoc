package edu.pdx.svl.coDoc.cdt.internal.ui.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pdx.svl.coDoc.cdt.core.parser.KeywordSetKey;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserFactory;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserLanguage;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.util.CWordDetector;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 * A C++ code scanner.
 */
public final class CppCodeScanner extends BufferedRuleBasedScanner {

	private IColorManager fColorManager;

	private static String[] fgConstants = { "NULL", //$NON-NLS-1$  
			"__DATE__", "__LINE__", "__TIME__", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
			"__FILE__", "__STDC__" }; //$NON-NLS-1$ //$NON-NLS-2$ 

	/**
	 * Creates a C++ code scanner
	 */
	public CppCodeScanner(IColorManager manager) {
		fColorManager = manager;
		createRules();
	}

	protected void createRules() {

		List rules = new ArrayList();

		// Add rule for strings
		Token token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_STRING)));
		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("'", "'", token, '\\')); //$NON-NLS-1$ //$NON-NLS-2$

		// Add word rule for keywords, types, and constants.
		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_DEFAULT)));
		WordRule wordRule = new WordRule(new CWordDetector(), token);

		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_KEYWORD)));
		Iterator iter = ParserFactory.getKeywordSet(KeywordSetKey.KEYWORDS,
				ParserLanguage.CPP).iterator();
		while (iter.hasNext())
			wordRule.addWord((String) iter.next(), token);
		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_TYPE)));
		iter = ParserFactory.getKeywordSet(KeywordSetKey.TYPES,
				ParserLanguage.CPP).iterator();
		while (iter.hasNext())
			wordRule.addWord((String) iter.next(), token);
		for (int i = 0; i < fgConstants.length; i++)
			wordRule.addWord(fgConstants[i], token);
		rules.add(wordRule);

		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_TYPE)));
		PreprocessorRule preprocessorRule = new PreprocessorRule(
				new CWordDetector(), token);
		iter = ParserFactory.getKeywordSet(KeywordSetKey.PP_DIRECTIVE,
				ParserLanguage.CPP).iterator();

		while (iter.hasNext())
			preprocessorRule.addWord((String) iter.next(), token);

		rules.add(preprocessorRule);

		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_NUMBER)));
		NumberRule numberRule = new NumberRule(token);
		rules.add(numberRule);

		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_OPERATOR)));
		COperatorRule opRule = new COperatorRule(token);
		rules.add(opRule);

		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_BRACES)));
		CBraceRule braceRule = new CBraceRule(token);
		rules.add(braceRule);

		setDefaultReturnToken(new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_DEFAULT))));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}