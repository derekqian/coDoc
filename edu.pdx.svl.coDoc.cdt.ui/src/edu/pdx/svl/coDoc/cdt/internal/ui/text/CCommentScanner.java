package edu.pdx.svl.coDoc.cdt.internal.ui.text;

import edu.pdx.svl.coDoc.cdt.internal.ui.text.util.CColorManager;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * CCommentScanner.java
 */
public class CCommentScanner extends BufferedRuleBasedScanner {
	public CCommentScanner(CColorManager fColorManager, String fAttribute) {
		IToken commentToken = new Token(new TextAttribute(fColorManager
				.getColor(fAttribute)));
		setDefaultReturnToken(commentToken);
	}
}
