package edu.pdx.svl.coDoc.cdt.internal.ui.text;

/*******************************************************************************
 * Copyright (c) 2000 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/
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
 * A C code scanner.
 */
public final class CCodeScanner extends BufferedRuleBasedScanner {

	private IColorManager fColorManager;

	/** Constants which are additionally colored. */
	private static String[] fgConstants = { "NULL", "__DATE__", "__LINE__", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$  
			"__TIME__", "__FILE__", "__STDC__", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"bool", "TRUE", "FALSE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"EXT_TEXT" }; //$NON-NLS-1$

	/**
	 * Creates a C code scanner.
	 * 
	 * @param manager
	 *            Color manager.
	 * @param store
	 *            Preference store.
	 */
	public CCodeScanner(IColorManager manager) {
		fColorManager = manager;
		createRules();
	}

	/**
	 * Add rules to the scanner
	 */
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
		Iterator i = ParserFactory.getKeywordSet(KeywordSetKey.KEYWORDS,
				ParserLanguage.C).iterator();
		while (i.hasNext())
			wordRule.addWord((String) i.next(), token);
		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_TYPE)));
		i = ParserFactory.getKeywordSet(KeywordSetKey.TYPES, ParserLanguage.C)
				.iterator();
		while (i.hasNext())
			wordRule.addWord((String) i.next(), token);

		for (int j = 0; j < fgConstants.length; j++)
			wordRule.addWord(fgConstants[j], token);

		rules.add(wordRule);

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

		token = new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_TYPE)));
		PreprocessorRule preprocessorRule = new PreprocessorRule(
				new CWordDetector(), token);

		i = ParserFactory.getKeywordSet(KeywordSetKey.PP_DIRECTIVE,
				ParserLanguage.C).iterator();
		while (i.hasNext())
			preprocessorRule.addWord((String) i.next(), token);

		rules.add(preprocessorRule);

		setDefaultReturnToken(new Token(new TextAttribute(fColorManager
				.getColor(ICColorConstants.C_DEFAULT))));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}