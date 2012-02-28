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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CASTIfStatement extends CASTNode implements IASTIfStatement,
		IASTAmbiguityParent {

	private IASTExpression condition;

	private IASTStatement thenClause;

	private IASTStatement elseClause;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement#getCondition()
	 */
	public IASTExpression getConditionExpression() {
		return condition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement#setCondition(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setConditionExpression(IASTExpression condition) {
		this.condition = condition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement#getThenClause()
	 */
	public IASTStatement getThenClause() {
		return thenClause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement#setThenClause(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement)
	 */
	public void setThenClause(IASTStatement thenClause) {
		this.thenClause = thenClause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement#getElseClause()
	 */
	public IASTStatement getElseClause() {
		return elseClause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTIfStatement#setElseClause(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement)
	 */
	public void setElseClause(IASTStatement elseClause) {
		this.elseClause = elseClause;
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitStatements) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
		if (condition != null)
			if (!condition.accept(action))
				return false;
		if (thenClause != null)
			if (!thenClause.accept(action))
				return false;
		if (elseClause != null)
			if (!elseClause.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (thenClause == child) {
			other.setParent(child.getParent());
			other.setPropertyInParent(child.getPropertyInParent());
			thenClause = (IASTStatement) other;
		}
		if (elseClause == child) {
			other.setParent(child.getParent());
			other.setPropertyInParent(child.getPropertyInParent());
			elseClause = (IASTStatement) other;
		}
		if (child == condition) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			condition = (IASTExpression) other;
		}
	}
}
