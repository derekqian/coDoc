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
package edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2;

import edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.CodeReader;
import edu.pdx.svl.coDoc.cdt.core.parser.ICodeReaderCache;
import edu.pdx.svl.coDoc.cdt.core.parser.IScanner;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.EmptyCodeReaderCache;

/**
 * @author jcamelon
 */
public class FileCodeReaderFactory implements ICodeReaderFactory {

	private static FileCodeReaderFactory instance;

	private ICodeReaderCache cache = null;

	private FileCodeReaderFactory(ICodeReaderCache cache) {
		this.cache = cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#getUniqueIdentifier()
	 */
	public int getUniqueIdentifier() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#createCodeReaderForTranslationUnit(java.lang.String)
	 */
	public CodeReader createCodeReaderForTranslationUnit(String path) {
		return cache.get(path);
	}

	public CodeReader createCodeReaderForTranslationUnit(ITranslationUnit tu) {
		return new CodeReader(tu.getPath().toOSString(), tu.getContents());
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
	public static FileCodeReaderFactory getInstance() {
		if (instance == null)
			instance = new FileCodeReaderFactory(new EmptyCodeReaderCache());
		return instance;
	}

	public ICodeReaderCache getCodeReaderCache() {
		return cache;
	}

}
