/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration;

/**
 * @author jcamelon
 */
public class CASTParameterDeclaration extends CASTNode implements
		IASTParameterDeclaration {

	private IASTDeclSpecifier declSpec;

	private IASTDeclarator declarator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration#getDeclSpecifier()
	 */
	public IASTDeclSpecifier getDeclSpecifier() {
		return declSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration#getDeclarator()
	 */
	public IASTDeclarator getDeclarator() {
		return declarator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration#setDeclSpecifier(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier)
	 */
	public void setDeclSpecifier(IASTDeclSpecifier declSpec) {
		this.declSpec = declSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration#setDeclarator(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator)
	 */
	public void setDeclarator(IASTDeclarator declarator) {
		this.declarator = declarator;
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitParameterDeclarations) {
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
		if (declarator != null)
			if (!declarator.accept(action))
				return false;
		return true;
	}
}
