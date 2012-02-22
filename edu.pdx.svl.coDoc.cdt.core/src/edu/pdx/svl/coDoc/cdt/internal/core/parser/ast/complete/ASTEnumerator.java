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

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbol;

/**
 * @author jcamelon
 * 
 */
public class ASTEnumerator extends ASTSymbol implements IASTEnumerator {

	private final IASTExpression initialValue;

	private final IASTEnumerationSpecifier owner;

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
	 * @param enumeratorSymbol
	 * @param startingOffset
	 * @param endingOffset
	 * @param initialValue
	 * @param filename
	 */
	public ASTEnumerator(ISymbol enumeratorSymbol,
			IASTEnumerationSpecifier owner, int startingOffset,
			int startingLine, int nameOffset, int nameEndOffset, int nameLine,
			int endingOffset, int endingLine, IASTExpression initialValue,
			char[] filename) {
		super(enumeratorSymbol);
		setStartingOffsetAndLineNumber(startingOffset, startingLine);
		setNameOffset(nameOffset);
		setNameEndOffsetAndLineNumber(nameEndOffset, nameLine);
		setEndingOffsetAndLineNumber(endingOffset, endingLine);
		this.initialValue = initialValue;
		this.owner = owner;
		fn = filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerator#getOwnerEnumerationSpecifier()
	 */
	public IASTEnumerationSpecifier getOwnerEnumerationSpecifier() {
		return owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerator#getInitialValue()
	 */
	public IASTExpression getInitialValue() {
		return initialValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getName()
	 */
	public String getName() {
		return String.valueOf(symbol.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerator#freeReferences(edu.pdx.svl.coDoc.cdt.core.parser.ast.IReferenceManager)
	 */
	public void freeReferences() {
		if (initialValue != null)
			initialValue.freeReferences();
	}

	private int startingLineNumber, startingOffset, endingLineNumber,
			endingOffset, nameEndOffset;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getStartingLine()
	 */
	public int getStartingLine() {
		return startingLineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getEndingLine()
	 */
	public int getEndingLine() {
		return endingLineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameLineNumber()
	 */
	public int getNameLineNumber() {
		return getStartingLine();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#setStartingOffset(int)
	 */
	public void setStartingOffsetAndLineNumber(int offset, int lineNumber) {
		startingOffset = offset;
		startingLineNumber = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#setEndingOffset(int)
	 */
	public void setEndingOffsetAndLineNumber(int offset, int lineNumber) {
		endingOffset = offset;
		endingLineNumber = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getStartingOffset()
	 */
	public int getStartingOffset() {
		return startingOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getEndingOffset()
	 */
	public int getEndingOffset() {
		return endingOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameOffset()
	 */
	public int getNameOffset() {
		return getStartingOffset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#setNameOffset(int)
	 */
	public void setNameOffset(int o) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameEndOffset()
	 */
	public int getNameEndOffset() {
		return nameEndOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#setNameEndOffset(int)
	 */
	public void setNameEndOffsetAndLineNumber(int offset, int lineNumber) {
		nameEndOffset = offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		return symbol.getName();
	}
}
