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
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;

/**
 * @author jcamelon
 * 
 */
public class ASTDesignator implements IASTDesignator {
	/**
	 * @param kind
	 * @param constantExpression
	 * @param string
	 */
	public ASTDesignator(DesignatorKind kind,
			IASTExpression constantExpression, char[] fieldName, int fieldOffset) {
		this.fieldName = fieldName;
		this.constantExpression = constantExpression;
		this.kind = kind;
		this.fieldOffset = fieldOffset;
	}

	private int fieldOffset;

	private final char[] fieldName;

	private final IASTExpression constantExpression;

	private final DesignatorKind kind;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator#getKind()
	 */
	public DesignatorKind getKind() {
		return kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator#arraySubscriptExpression()
	 */
	public IASTExpression arraySubscriptExpression() {
		return constantExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator#fieldName()
	 */
	public String fieldName() {
		return String.valueOf(fieldName);
	}

	public char[] fieldNameCharArray() {
		return fieldName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		if (constantExpression != null)
			constantExpression.acceptElement(requestor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#enterScope(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void enterScope(ISourceElementRequestor requestor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#exitScope(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void exitScope(ISourceElementRequestor requestor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator#fieldOffset()
	 */
	public int fieldOffset() {
		return fieldOffset;
	}
}
