/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.gcc;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.gcc.IASTGCCDesignator;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.ASTDesignator;

/**
 * @author jcamelon
 * 
 */
public class ASTGCCDesignator extends ASTDesignator implements
		IASTGCCDesignator {
	private final IASTExpression secondExpression;

	/**
	 * @param kind
	 * @param constantExpression
	 * @param fieldName
	 * @param fieldOffset
	 */
	public ASTGCCDesignator(IASTDesignator.DesignatorKind kind,
			IASTExpression constantExpression, char[] fieldName,
			int fieldOffset, IASTExpression secondSubscriptExpression) {
		super(kind, constantExpression, fieldName, fieldOffset);
		secondExpression = secondSubscriptExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.gcc.IASTGCCDesignator#arraySubscriptExpression2()
	 */
	public IASTExpression arraySubscriptExpression2() {
		return secondExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		super.acceptElement(requestor);
		if (secondExpression != null)
			secondExpression.acceptElement(requestor);
	}
}
