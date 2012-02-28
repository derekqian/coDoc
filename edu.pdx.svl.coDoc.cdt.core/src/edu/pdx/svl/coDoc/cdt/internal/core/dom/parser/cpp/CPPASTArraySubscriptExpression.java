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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTArraySubscriptExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTArraySubscriptExpression extends CPPASTNode implements
		IASTArraySubscriptExpression, IASTAmbiguityParent {

	private IASTExpression subscriptExp;

	private IASTExpression arrayExpression;

	public IASTExpression getArrayExpression() {
		return arrayExpression;
	}

	public void setArrayExpression(IASTExpression expression) {
		arrayExpression = expression;
	}

	public IASTExpression getSubscriptExpression() {
		return subscriptExp;
	}

	public void setSubscriptExpression(IASTExpression expression) {
		subscriptExp = expression;
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitExpressions) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
		if (arrayExpression != null)
			if (!arrayExpression.accept(action))
				return false;
		if (subscriptExp != null)
			if (!subscriptExp.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == subscriptExp) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			subscriptExp = (IASTExpression) other;
		}
		if (child == arrayExpression) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			arrayExpression = (IASTExpression) other;
		}
	}

	public IType getExpressionType() {
		return CPPVisitor.getExpressionType(this);
	}

}
