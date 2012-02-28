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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTLinkageSpecification extends CPPASTNode implements
		ICPPASTLinkageSpecification, IASTAmbiguityParent {

	private String literal;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification#getLiteral()
	 */
	public String getLiteral() {
		return literal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification#setLiteral(java.lang.String)
	 */
	public void setLiteral(String value) {
		this.literal = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification#getDeclarations()
	 */
	public IASTDeclaration[] getDeclarations() {
		if (declarations == null)
			return IASTDeclaration.EMPTY_DECLARATION_ARRAY;
		return (IASTDeclaration[]) ArrayUtil.removeNulls(IASTDeclaration.class,
				declarations);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification#addDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration)
	 */
	public void addDeclaration(IASTDeclaration declaration) {
		declarations = (IASTDeclaration[]) ArrayUtil.append(
				IASTDeclaration.class, declarations, declaration);
	}

	private IASTDeclaration[] declarations = new IASTDeclaration[4];

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitDeclarations) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}

		IASTDeclaration[] decls = getDeclarations();
		for (int i = 0; i < decls.length; i++)
			if (!decls[i].accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (declarations == null)
			return;
		for (int i = 0; i < declarations.length; ++i) {
			if (declarations[i] == null)
				continue;
			if (declarations[i] == child) {
				other.setParent(child.getParent());
				other.setPropertyInParent(child.getPropertyInParent());
				declarations[i] = (IASTDeclaration) other;
			}
		}
	}
}
