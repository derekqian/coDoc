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

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFileLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNodeLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorMacroDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IMacroBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit.IDependencyTree;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTPreprocessorSelectionResult;

/**
 * @author jcamelon
 */
public interface ILocationResolver {

	public IASTPreprocessorMacroDefinition[] getMacroDefinitions();

	public IASTPreprocessorIncludeStatement[] getIncludeDirectives();

	public IASTPreprocessorStatement[] getAllPreprocessorStatements();

	public IASTNodeLocation[] getLocations(int offset, int length);

	public char[] getUnpreprocessedSignature(IASTNodeLocation[] locations);

	public IASTProblem[] getScannerProblems();

	public String getTranslationUnitPath();

	public void cleanup();

	public ASTPreprocessorSelectionResult getPreprocessorNode(String path,
			int offset, int length) throws InvalidPreprocessorNodeException;

	public void setRootNode(IASTTranslationUnit root);

	public IASTFileLocation flattenLocations(IASTNodeLocation[] nodeLocations);

	public IASTName[] getReferences(IMacroBinding binding);

	public IASTName[] getDeclarations(IMacroBinding binding);

	public IASTName[] getMacroExpansions();

	public IDependencyTree getDependencyTree();

	public String getContainingFilename(int offset);

}
