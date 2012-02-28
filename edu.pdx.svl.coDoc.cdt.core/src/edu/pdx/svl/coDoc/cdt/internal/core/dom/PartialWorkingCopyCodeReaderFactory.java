/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom;

import java.util.Arrays;
import java.util.Iterator;

import edu.pdx.svl.coDoc.cdt.core.dom.CDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopyProvider;
import edu.pdx.svl.coDoc.cdt.core.parser.CodeReader;
import edu.pdx.svl.coDoc.cdt.core.parser.ICodeReaderCache;
import edu.pdx.svl.coDoc.cdt.core.parser.IScanner;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.EmptyIterator;

/**
 * @author jcamelon
 */
public class PartialWorkingCopyCodeReaderFactory implements ICodeReaderFactory {

	private final IWorkingCopyProvider provider;

	private ICodeReaderCache cache = null;

	/**
	 * @param provider
	 */
	public PartialWorkingCopyCodeReaderFactory(IWorkingCopyProvider provider) {
		this.provider = provider;
		cache = SavedCodeReaderFactory.getInstance().getCodeReaderCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#getUniqueIdentifier()
	 */
	public int getUniqueIdentifier() {
		return CDOM.PARSE_WORKING_COPY_WITH_SAVED_INCLUSIONS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#createCodeReaderForTranslationUnit(java.lang.String)
	 */
	public CodeReader createCodeReaderForTranslationUnit(String path) {
		return checkWorkingCopyThenCache(path);
	}

	public CodeReader createCodeReaderForTranslationUnit(ITranslationUnit tu) {
		return new CodeReader(tu.getPath().toOSString(), tu.getContents());
	}

	/**
	 * @param path
	 * @return
	 */
	protected CodeReader checkWorkingCopyThenCache(String path) {
		char[] buffer = ParserUtil.findWorkingCopyBuffer(path,
				createWorkingCopyIterator());
		if (buffer != null)
			return new CodeReader(path, buffer);
		return cache.get(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#createCodeReaderForInclusion(java.lang.String)
	 */
	public CodeReader createCodeReaderForInclusion(IScanner scanner, String path) {
		return cache.get(path);
	}

	/**
	 * @return
	 */
	protected Iterator createWorkingCopyIterator() {
		if (provider == null)
			return EmptyIterator.EMPTY_ITERATOR;
		return Arrays.asList(provider.getWorkingCopies()).iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#getCodeReaderCache()
	 */
	public ICodeReaderCache getCodeReaderCache() {
		return cache;
	}

}
