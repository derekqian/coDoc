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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTEnumerator extends CPPASTNode implements IASTEnumerator,
		IASTAmbiguityParent {
	private IASTName name;

	private IASTExpression value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator#setName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public void setName(IASTName name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator#getName()
	 */
	public IASTName getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator#setValue(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setValue(IASTExpression expression) {
		this.value = expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator#getValue()
	 */
	public IASTExpression getValue() {
		return value;
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitEnumerators) {
			switch (action.visit(this)) {
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
			return r_definition;
		return r_reference;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == value) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			value = (IASTExpression) other;
		}
	}
}
