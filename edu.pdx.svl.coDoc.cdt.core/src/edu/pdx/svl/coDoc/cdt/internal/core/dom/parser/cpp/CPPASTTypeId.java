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
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;

/**
 * @author jcamelon
 */
public class CPPASTTypeId extends CPPASTNode implements IASTTypeId {

	private IASTDeclSpecifier declSpec;

	private IASTDeclarator absDecl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId#getDeclSpecifier()
	 */
	public IASTDeclSpecifier getDeclSpecifier() {
		return declSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId#setDeclSpecifier(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier)
	 */
	public void setDeclSpecifier(IASTDeclSpecifier declSpec) {
		this.declSpec = declSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId#getAbstractDeclarator()
	 */
	public IASTDeclarator getAbstractDeclarator() {
		return absDecl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId#setAbstractDeclarator(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator)
	 */
	public void setAbstractDeclarator(IASTDeclarator abstractDeclarator) {
		this.absDecl = abstractDeclarator;
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitTypeIds) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}

		if (declSpec != null)
			if (!declSpec.accept(action))
				return false;
		if (absDecl != null)
			if (!absDecl.accept(action))
				return false;
		return true;
	}
}
