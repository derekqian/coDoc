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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLiteralExpression;

/**
 * @author jcamelon
 */
public class CPPASTLiteralExpression extends CPPASTNode implements
		ICPPASTLiteralExpression {

	private int kind;

	private String value = ""; //$NON-NLS-1$

	public int getKind() {
		return kind;
	}

	public void setKind(int value) {
		this.kind = value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
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
		return true;
	}

	public IType getExpressionType() {
		return CPPVisitor.getExpressionType(this);
	}

}
