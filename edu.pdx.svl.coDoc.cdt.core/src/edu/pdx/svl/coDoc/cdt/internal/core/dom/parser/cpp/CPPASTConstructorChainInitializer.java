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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTConstructorChainInitializer;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTConstructorChainInitializer extends CPPASTNode implements
		ICPPASTConstructorChainInitializer, IASTAmbiguityParent {

	private IASTName name;

	private IASTExpression value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTConstructorChainInitializer#getMemberInitializerId()
	 */
	public IASTName getMemberInitializerId() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTConstructorChainInitializer#setMemberInitializerId(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public void setMemberInitializerId(IASTName name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTConstructorChainInitializer#getInitializerValue()
	 */
	public IASTExpression getInitializerValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTConstructorChainInitializer#setInitializerValue(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setInitializerValue(IASTExpression expression) {
		value = expression;
	}

	public boolean accept(ASTVisitor action) {
		if (name != null)
			if (!name.accept(action))
				return false;
		if (value != null)
			if (!value.accept(action))
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
			return r_reference;
		return r_unclear;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == value) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			value = (IASTExpression) other;
		}
	}
}
