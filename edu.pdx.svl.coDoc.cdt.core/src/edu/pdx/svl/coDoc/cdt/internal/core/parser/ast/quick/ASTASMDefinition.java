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
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.quick;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTASMDefinition;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;

/**
 * @author jcamelon
 * 
 */
public class ASTASMDefinition extends ASTDeclaration implements
		IASTASMDefinition {

	private final char[] assembly;

	private final char[] fn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getFilename()
	 */
	public char[] getFilename() {
		return fn;
	}

	/**
	 * @param scope
	 * @param filename
	 */
	public ASTASMDefinition(IASTScope scope, char[] assembly, char[] filename) {
		super(scope);
		this.assembly = assembly;
		fn = filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTASMDefinition#getBody()
	 */
	public String getBody() {
		return String.valueOf(assembly);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#accept(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		try {
			requestor.acceptASMDefinition(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#enter(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void enterScope(ISourceElementRequestor requestor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#exit(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void exitScope(ISourceElementRequestor requestor) {
	}

	private int startingLineNumber, startingOffset, endingLineNumber,
			endingOffset;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getStartingLine()
	 */
	public final int getStartingLine() {
		return startingLineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getEndingLine()
	 */
	public final int getEndingLine() {
		return endingLineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#setStartingOffset(int)
	 */
	public final void setStartingOffsetAndLineNumber(int offset, int lineNumber) {
		startingOffset = offset;
		startingLineNumber = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#setEndingOffset(int)
	 */
	public final void setEndingOffsetAndLineNumber(int offset, int lineNumber) {
		endingOffset = offset;
		endingLineNumber = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getStartingOffset()
	 */
	public final int getStartingOffset() {
		return startingOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getEndingOffset()
	 */
	public final int getEndingOffset() {
		return endingOffset;
	}

}
