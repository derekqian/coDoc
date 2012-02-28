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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDoStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTDoStatement extends CPPASTNode implements IASTDoStatement,
		IASTAmbiguityParent {
	private IASTStatement body;

	private IASTExpression condition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDoStatement#getBody()
	 */
	public IASTStatement getBody() {
		return body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDoStatement#setBody(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement)
	 */
	public void setBody(IASTStatement body) {
		this.body = body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDoStatement#getCondition()
	 */
	public IASTExpression getCondition() {
		return condition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDoStatement#setCondition(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setCondition(IASTExpression condition) {
		this.condition = condition;
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
		if (body != null)
			if (!body.accept(action))
				return false;
		if (condition != null)
			if (!condition.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (body == child) {
			other.setPropertyInParent(body.getPropertyInParent());
			other.setParent(body.getParent());
			body = (IASTStatement) other;
		}
		if (child == condition) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			condition = (IASTExpression) other;
		}
	}
}
