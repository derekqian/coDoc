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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTClassKind;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.Parser;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.ASTQualifiedNamedElement;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbol;

/**
 * @author jcamelon
 * 
 */
public class ASTElaboratedTypeSpecifier extends ASTSymbol implements
		IASTElaboratedTypeSpecifier {
	private List references;

	private final boolean isForwardDeclaration;

	private final ASTClassKind kind;

	private final ASTQualifiedNamedElement qualifiedName;

	/**
	 * @param checkSymbol
	 * @param kind
	 * @param startingOffset
	 * @param endOffset
	 * @param filename
	 */
	public ASTElaboratedTypeSpecifier(ISymbol checkSymbol, ASTClassKind kind,
			int startingOffset, int startingLine, int nameOffset,
			int nameEndOffset, int nameLine, int endOffset, int endingLine,
			List references, boolean isDecl, char[] filename) {
		super(checkSymbol);
		this.kind = kind;
		setStartingOffsetAndLineNumber(startingOffset, startingLine);
		setNameOffset(nameOffset);
		setNameEndOffsetAndLineNumber(nameEndOffset, nameLine);
		setEndingOffsetAndLineNumber(endOffset, endingLine);
		qualifiedName = new ASTQualifiedNamedElement(getOwnerScope(),
				checkSymbol.getName());
		isForwardDeclaration = isDecl;
		this.references = references;
		fn = filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier#getName()
	 */
	public String getName() {
		return String.valueOf(getSymbol().getName());
	}

	public char[] getNameCharArray() {
		return getSymbol().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier#getClassKind()
	 */
	public ASTClassKind getClassKind() {
		return kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier#isResolved()
	 */
	public boolean isResolved() {
		return !getSymbol().isForwardDeclaration();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		if (isForwardDeclaration)
			try {
				requestor.acceptElaboratedForewardDeclaration(this);
			} catch (Exception e) {
				/* do nothing */
			}
		Parser.processReferences(references, requestor);
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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTQualifiedNameElement#getFullyQualifiedName()
	 */
	public String[] getFullyQualifiedName() {
		return qualifiedName.getFullyQualifiedName();
	}

	public char[][] getFullyQualifiedNameCharArrays() {
		return qualifiedName.getFullyQualifiedNameCharArrays();
	}

	public List getReferences() {
		return references;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IASTElaboratedTypeSpecifier))
			return false;
		IASTElaboratedTypeSpecifier elab = (IASTElaboratedTypeSpecifier) obj;
		if (elab.getClassKind() != getClassKind())
			return false;
		if (!elab.getName().equals(getName()))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		String coded = getName().toString();
		return coded.hashCode();
	}

	private int startingLineNumber, startingOffset, endingLineNumber,
			endingOffset, nameStartOffset, nameEndOffset, nameLineNumber;

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
		return nameLineNumber;
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
		return nameStartOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#setNameOffset(int)
	 */
	public void setNameOffset(int o) {
		nameStartOffset = o;
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
		nameLineNumber = lineNumber;
	}

	private final char[] fn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getFilename()
	 */
	public char[] getFilename() {
		return fn;
	}
}
