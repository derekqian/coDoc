/*******************************************************************************
 * Copyright (c) 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.core.model;

import edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTCompletionNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * @author Doug Schaefer
 * 
 */
public interface ILanguage extends IAdaptable {

	// public static final QualifiedName KEY = new
	// QualifiedName(CCorePlugin.PLUGIN_ID, "language"); //$NON-NLS-1$
	public static final String KEY = "language"; //$NON-NLS-1$

	/**
	 * Style for getTranslationUnit. Use the index for resolving bindings that
	 * aren't found in the AST.
	 */
	public static final int AST_USE_INDEX = 1;

	/**
	 * Style for getTranslationUnit. Don't parse header files. It's a good idea
	 * to turn on AST_USE_INDEX when you do this.
	 */
	public static final int AST_SKIP_ALL_HEADERS = 2;

	/**
	 * Style for getTranslationUnit. Used by the indexer to skip over headers it
	 * already has indexed.
	 */
	public static final int AST_SKIP_INDEXED_HEADERS = 4;

	/**
	 * Style for getTranslationUnit. Don't parse the file if there is no build
	 * information for it.
	 */
	public static final int AST_SKIP_IF_NO_BUILD_INFO = 8;

	/**
	 * Return the language id for this language in the given PDOM. This is to
	 * differentiate languages from eachother.
	 * 
	 * @return language id
	 */
	public String getId();

	/**
	 * Create the AST for the given file with the given style.
	 * 
	 * @param file
	 * @param style
	 * @return
	 */
	public IASTTranslationUnit getASTTranslationUnit(ITranslationUnit file,
			int style) throws CoreException;

	/**
	 * Create the AST for the given file with the given style with a given code
	 * reader factory.
	 * 
	 * @param file
	 * @param style
	 * @return
	 */
	public IASTTranslationUnit getASTTranslationUnit(ITranslationUnit file,
			ICodeReaderFactory codeReaderFactory, int style)
			throws CoreException;

	/**
	 * Return the AST Completion Node for the given working copy at the given
	 * offset.
	 * 
	 * @param workingCopy
	 * @param offset
	 * @return
	 */
	public ASTCompletionNode getCompletionNode(IWorkingCopy workingCopy,
			int offset) throws CoreException;

	/**
	 * Gather the list of IASTNames that appear the selection with the given
	 * start offset and length in the given ITranslationUnit.
	 * 
	 * @param tu
	 * @param start
	 * @param length
	 * @param style
	 * @return
	 */
	public IASTName[] getSelectedNames(IASTTranslationUnit ast, int start,
			int length);

	/**
	 * Used to override the default model building behavior for a translation
	 * unit.
	 * 
	 * @param tu
	 *            the <code>ITranslationUnit</code> to be parsed (non-<code>null</code>)
	 * @return an <code>IModelBuilder</code>, which parses the given
	 *         translation unit and returns the <code>ICElement</code>s of
	 *         its model, or <code>null</code> to parse using the default CDT
	 *         model builder
	 */
	public IContributedModelBuilder createModelBuilder(ITranslationUnit tu);
}
