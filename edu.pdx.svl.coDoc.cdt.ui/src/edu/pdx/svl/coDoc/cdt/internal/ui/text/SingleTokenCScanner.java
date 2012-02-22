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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

/**
 * 
 */
public final class SingleTokenCScanner extends BufferedRuleBasedScanner {

	protected IToken fDefaultReturnToken;

	private String fProperty;

	private IColorManager fColorManager;

	public SingleTokenCScanner(IColorManager manager, String property) {
		super(20);
		fProperty = property;
		fColorManager = manager;
		createRules();
	}

	protected void createRules() {
		fDefaultReturnToken = new Token(new TextAttribute(fColorManager
				.getColor(fProperty), fColorManager.getColor(new RGB(255, 255,
				255)), SWT.NORMAL));
		setDefaultReturnToken(fDefaultReturnToken);
	}

	/**
	 * setRange -- sets the range to be scanned
	 */

	private int position, end;

	private int size;

	public void setRange(IDocument document, int offset, int length) {

		super.setRange(document, offset, length);
		position = offset;
		size = length;
		end = offset + length;
	}

	/**
	 * Returns the next token in the document.
	 * 
	 * @return the next token in the document
	 */
	public IToken nextToken() {

		fTokenOffset = position;

		if (position < end) {
			size = end - position;
			position = end;
			return fDefaultReturnToken;
		}
		return Token.EOF;
	}

	public int getTokenLength() {
		return size;
	}
}
