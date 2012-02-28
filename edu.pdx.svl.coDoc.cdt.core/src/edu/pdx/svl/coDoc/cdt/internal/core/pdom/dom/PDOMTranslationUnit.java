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
package edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom;

import edu.pdx.svl.coDoc.cdt.core.dom.IPDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTNodeProperty;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFileLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNodeLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorMacroDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserLanguage;

/**
 * @author Doug Schaefer
 * 
 * This is really a dummy translation unit that is necessary for names to be
 * valid.
 */
public class PDOMTranslationUnit implements IASTTranslationUnit {

	public IASTDeclaration[] getDeclarations() {
		throw new PDOMNotImplementedError();
	}

	public void addDeclaration(IASTDeclaration declaration) {
		throw new PDOMNotImplementedError();
	}

	public IScope getScope() {
		throw new PDOMNotImplementedError();
	}

	public IASTName[] getDeclarations(IBinding binding) {
		throw new PDOMNotImplementedError();
	}

	public IASTName[] getDefinitions(IBinding binding) {
		throw new PDOMNotImplementedError();
	}

	public IASTName[] getReferences(IBinding binding) {
		throw new PDOMNotImplementedError();
	}

	public IASTNodeLocation[] getLocationInfo(int offset, int length) {
		throw new PDOMNotImplementedError();
	}

	public IASTNode selectNodeForLocation(String path, int offset, int length) {
		throw new PDOMNotImplementedError();
	}

	public IASTPreprocessorMacroDefinition[] getMacroDefinitions() {
		throw new PDOMNotImplementedError();
	}

	public IASTPreprocessorIncludeStatement[] getIncludeDirectives() {
		throw new PDOMNotImplementedError();
	}

	public IASTPreprocessorStatement[] getAllPreprocessorStatements() {
		throw new PDOMNotImplementedError();
	}

	public IASTProblem[] getPreprocessorProblems() {
		throw new PDOMNotImplementedError();
	}

	public String getUnpreprocessedSignature(IASTNodeLocation[] locations) {
		throw new PDOMNotImplementedError();
	}

	public String getFilePath() {
		throw new PDOMNotImplementedError();
	}

	public IASTFileLocation flattenLocationsToFile(
			IASTNodeLocation[] nodeLocations) {
		throw new PDOMNotImplementedError();
	}

	public IDependencyTree getDependencyTree() {
		throw new PDOMNotImplementedError();
	}

	public String getContainingFilename(int offset) {
		throw new PDOMNotImplementedError();
	}

	public ParserLanguage getParserLanguage() {
		throw new PDOMNotImplementedError();
	}

	public IPDOM getIndex() {
		throw new PDOMNotImplementedError();
	}

	public void setIndex(IPDOM pdom) {
		throw new PDOMNotImplementedError();
	}

	public IASTTranslationUnit getTranslationUnit() {
		throw new PDOMNotImplementedError();
	}

	public IASTNodeLocation[] getNodeLocations() {
		throw new PDOMNotImplementedError();
	}

	public IASTFileLocation getFileLocation() {
		throw new PDOMNotImplementedError();
	}

	public String getContainingFilename() {
		throw new PDOMNotImplementedError();
	}

	public IASTNode getParent() {
		throw new PDOMNotImplementedError();
	}

	public void setParent(IASTNode node) {
		throw new PDOMNotImplementedError();
	}

	public ASTNodeProperty getPropertyInParent() {
		throw new PDOMNotImplementedError();
	}

	public void setPropertyInParent(ASTNodeProperty property) {
		throw new PDOMNotImplementedError();
	}

	public boolean accept(ASTVisitor visitor) {
		throw new PDOMNotImplementedError();
	}

	public String getRawSignature() {
		throw new PDOMNotImplementedError();
	}

	public ILanguage getLanguage() {
		throw new PDOMNotImplementedError();
	}

}
