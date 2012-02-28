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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTArrayModifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTArrayModifier extends CPPASTNode implements
		IASTArrayModifier, IASTAmbiguityParent {

	private IASTExpression exp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTArrayModifier#getConstantExpression()
	 */
	public IASTExpression getConstantExpression() {
		return exp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTArrayModifier#setConstantExpression(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setConstantExpression(IASTExpression expression) {
		exp = expression;
	}

	public boolean accept(ASTVisitor action) {
		if (exp != null)
			if (!exp.accept(action))
				return false;

		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == exp) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			exp = (IASTExpression) other;
		}
	}
}
