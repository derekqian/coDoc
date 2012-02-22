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
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete;

import java.util.List;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTConstructorMemberInitializer;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.Parser;

/**
 * @author jcamelon
 * 
 */
public class ASTConstructorMemberInitializer implements
		IASTConstructorMemberInitializer {
	private final int nameOffset;

	private final boolean requireNameResolution;

	private final char[] name;

	private final IASTExpression expression;

	private List references;

	/**
	 * 
	 */
	public ASTConstructorMemberInitializer(IASTExpression expression,
			char[] name, int nameOffset, List references,
			boolean requireNameResolution) {
		this.expression = expression;
		this.name = name;
		this.nameOffset = nameOffset;
		this.references = references;
		this.requireNameResolution = requireNameResolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTConstructorMemberInitializer#getExpressionList()
	 */
	public IASTExpression getExpressionList() {
		return expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTConstructorMemberInitializer#getName()
	 */
	public String getName() {
		return String.valueOf(name);
	}

	public char[] getNameCharArray() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		Parser.processReferences(references, requestor);
		references = null;
		if (expression != null)
			expression.freeReferences();
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

	/**
	 * @return
	 */
	public boolean requiresNameResolution() {
		return requireNameResolution;
	}

	/**
	 * @return
	 */
	public List getReferences() {
		return references;
	}

	/**
	 * @return
	 */
	public int getNameOffset() {
		return nameOffset;
	}

}
