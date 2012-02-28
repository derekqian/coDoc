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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTUnaryExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTUnaryExpression extends CPPASTNode implements
		IASTUnaryExpression, IASTAmbiguityParent {

	private int operator;

	private IASTExpression operand;

	public int getOperator() {
		return operator;
	}

	public void setOperator(int value) {
		this.operator = value;
	}

	public IASTExpression getOperand() {
		return operand;
	}

	public void setOperand(IASTExpression expression) {
		operand = expression;
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

		if (operand != null)
			if (!operand.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == operand) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			operand = (IASTExpression) other;
		}

	}

	public IType getExpressionType() {
		return CPPVisitor.getExpressionType(this);
	}

}
