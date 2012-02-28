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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.CPPASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespace;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTNamespaceDefinition extends CPPASTNode implements
		ICPPASTNamespaceDefinition, IASTAmbiguityParent {

	private IASTName name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition#getName()
	 */
	public IASTName getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition#setName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public void setName(IASTName name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition#getDeclarations()
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
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition#addDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration)
	 */
	public void addDeclaration(IASTDeclaration declaration) {
		declarations = (IASTDeclaration[]) ArrayUtil.append(
				IASTDeclaration.class, declarations, declaration);
	}

	private IASTDeclaration[] declarations = new IASTDeclaration[32];

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition#getScope()
	 */
	public IScope getScope() {
		try {
			return ((ICPPNamespace) name.resolveBinding()).getNamespaceScope();
		} catch (DOMException e) {
			return e.getProblem();
		}
	}

	public boolean accept(ASTVisitor action) {
		if (action instanceof CPPASTVisitor
				&& ((CPPASTVisitor) action).shouldVisitNamespaces) {
			switch (((CPPASTVisitor) action).visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}

		if (name != null)
			if (!name.accept(action))
				return false;
		IASTDeclaration[] decls = getDeclarations();
		for (int i = 0; i < decls.length; i++)
			if (!decls[i].accept(action))
				return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNameOwner#getRoleForName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public int getRoleForName(IASTName n) {
		if (name == n)
			return r_definition;
		return r_unclear;
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
