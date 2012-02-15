package edu.pdx.svl.coDoc.cdt.core.parser;

/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/

import java.util.Set;

import edu.pdx.svl.coDoc.cdt.internal.core.parser.token.KeywordSets;

/**
 * @author jcamelon
 * 
 */
public class ParserFactory {

	public static Set getKeywordSet(KeywordSetKey key, ParserLanguage language) {
		return KeywordSets.getKeywords(key, language);
	}
}
