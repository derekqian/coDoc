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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTCastExpression;

/**
 * @author jcamelon
 */
public class CPPASTCastExpression extends CPPASTUnaryExpression implements
		ICPPASTCastExpression {
	private IASTTypeId typeId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTUnaryTypeIdExpression#setTypeId(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId)
	 */
	public void setTypeId(IASTTypeId typeId) {
		this.typeId = typeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTUnaryTypeIdExpression#getTypeId()
	 */
	public IASTTypeId getTypeId() {
		return typeId;
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

		if (typeId != null)
			if (!typeId.accept(action))
				return false;
		IASTExpression op = getOperand();
		if (op != null)
			if (!op.accept(action))
				return false;
		return true;
	}
}
