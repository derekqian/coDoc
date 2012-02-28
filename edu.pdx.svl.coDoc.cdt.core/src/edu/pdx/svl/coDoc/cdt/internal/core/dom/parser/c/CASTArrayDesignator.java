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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.CASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICASTArrayDesignator;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CASTArrayDesignator extends CASTNode implements
		ICASTArrayDesignator, IASTAmbiguityParent {

	private IASTExpression exp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICASTArrayDesignator#getSubscriptExpression()
	 */
	public IASTExpression getSubscriptExpression() {
		return exp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICASTArrayDesignator#setSubscriptExpression(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setSubscriptExpression(IASTExpression value) {
		exp = value;
	}

	public boolean accept(ASTVisitor action) {
		if (action instanceof CASTVisitor
				&& ((CASTVisitor) action).shouldVisitDesignators) {
			switch (((CASTVisitor) action).visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
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
