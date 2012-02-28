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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceAlias;

/**
 * @author jcamelon
 */
public class CPPASTNamespaceAlias extends CPPASTNode implements
		ICPPASTNamespaceAlias {

	private IASTName alias;

	private IASTName qualifiedName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceAlias#getAlias()
	 */
	public IASTName getAlias() {
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceAlias#setAlias(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public void setAlias(IASTName name) {
		this.alias = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceAlias#getQualifiedName()
	 */
	public IASTName getMappingName() {
		return qualifiedName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceAlias#setQualifiedName(edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTQualifiedName)
	 */
	public void setMappingName(IASTName qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

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

		if (alias != null)
			if (!alias.accept(action))
				return false;
		if (qualifiedName != null)
			if (!qualifiedName.accept(action))
				return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNameOwner#getRoleForName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public int getRoleForName(IASTName n) {
		if (alias == n)
			return r_definition;
		if (qualifiedName == n)
			return r_reference;
		return r_unclear;
	}

}
