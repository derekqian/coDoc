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

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.CDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.CodeReader;
import edu.pdx.svl.coDoc.cdt.core.parser.CodeReaderCache;
import edu.pdx.svl.coDoc.cdt.core.parser.ICodeReaderCache;
import edu.pdx.svl.coDoc.cdt.core.parser.IScanner;
import org.eclipse.core.runtime.Preferences;

/**
 * @author jcamelon
 */
public class SavedCodeReaderFactory implements ICodeReaderFactory {

	private ICodeReaderCache cache = null;

	public static SavedCodeReaderFactory getInstance() {
		return instance;
	}

	private static SavedCodeReaderFactory instance = new SavedCodeReaderFactory();

	private SavedCodeReaderFactory() {
		int size = 0;
		Preferences pluginPreferences = CCorePlugin.getDefault()
				.getPluginPreferences();
		if (CCorePlugin.getDefault() == null || pluginPreferences == null)
			size = CodeReaderCache.DEFAULT_CACHE_SIZE_IN_MB;
		else
			size = pluginPreferences.getInt(CodeReaderCache.CODE_READER_BUFFER);

		if (size > 0)
			cache = new CodeReaderCache(size);
		else if (size == 0) {
			// necessary for cache to work headless
			String[] properties = pluginPreferences.propertyNames();
			boolean found = false;
			for (int j = 0; j < properties.length; ++j)
				if (properties[j].equals(CodeReaderCache.CODE_READER_BUFFER)) {
					found = true;
					break;
				}

			if (!found && size == 0)
				cache = new CodeReaderCache(
						CodeReaderCache.DEFAULT_CACHE_SIZE_IN_MB);
			else
				cache = new CodeReaderCache(0);
		} else
			cache = new CodeReaderCache(
					CodeReaderCache.DEFAULT_CACHE_SIZE_IN_MB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#getUniqueIdentifier()
	 */
	public int getUniqueIdentifier() {
		return CDOM.PARSE_SAVED_RESOURCES;
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
		return new CodeReader(tu.getResource().getLocation().toOSString(), tu
				.getContents());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#createCodeReaderForInclusion(java.lang.String)
	 */
	public CodeReader createCodeReaderForInclusion(IScanner scanner, String path) {
		return cache.get(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory#createCodeReaderForInclusion(java.lang.String)
	 */
	public ICodeReaderCache getCodeReaderCache() {
		return cache;
	}
}
