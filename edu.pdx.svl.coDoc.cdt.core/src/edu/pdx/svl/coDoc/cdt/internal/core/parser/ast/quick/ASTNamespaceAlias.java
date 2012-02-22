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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTNotImplementedException;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceAlias;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;

/**
 * @author jcamelon
 * 
 */
public class ASTNamespaceAlias extends ASTDeclaration implements
		IASTNamespaceAlias {

	private final char[] alias;

	private final char[] identifier;

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
	 * @param identifier
	 * @param string
	 * @param startingOffset
	 * @param nameOffset
	 * @param endOffset
	 * @param filename
	 */
	public ASTNamespaceAlias(IASTScope scope, char[] identifier, char[] string,
			int startingOffset, int nameOffset, int nameEndOffset,
			int endOffset, int startingLine, int nameLine, int endingLine,
			char[] filename) {
		super(scope);
		setStartingOffsetAndLineNumber(startingOffset, startingLine);
		setNameOffset(nameOffset);
		setNameEndOffsetAndLineNumber(nameEndOffset, nameLine);
		setEndingOffsetAndLineNumber(endOffset, endingLine);
		this.identifier = identifier;
		this.alias = string;
		fn = filename;
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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceAlias#getAlias()
	 */
	public String getAlias() {
		return String.valueOf(alias);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceAlias#getNamespace()
	 */
	public IASTNamespaceDefinition getNamespace()
			throws ASTNotImplementedException {
		throw new ASTNotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getName()
	 */
	public String getName() {
		return String.valueOf(identifier);
	}

	private int startingLineNumber, startingOffset, endingLineNumber,
			endingOffset, nameStartOffset, nameEndOffset, nameLineNumber;

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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameLineNumber()
	 */
	public final int getNameLineNumber() {
		return nameLineNumber;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameOffset()
	 */
	public final int getNameOffset() {
		return nameStartOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#setNameOffset(int)
	 */
	public final void setNameOffset(int o) {
		nameStartOffset = o;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameEndOffset()
	 */
	public final int getNameEndOffset() {
		return nameEndOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#setNameEndOffset(int)
	 */
	public final void setNameEndOffsetAndLineNumber(int offset, int lineNumber) {
		nameEndOffset = offset;
		nameLineNumber = lineNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		return identifier;
	}
}
