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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractTypeSpecifierDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplate;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.IContainerSymbol;

/**
 * @author jcamelon
 * 
 */
public class ASTAbstractTypeSpecifierDeclaration extends
		ASTAnonymousDeclaration implements IASTAbstractTypeSpecifierDeclaration {
	private final IASTTypeSpecifier typeSpec;

	private final IASTTemplate ownerTemplate;

	private final boolean isFriendDeclaration;

	/**
	 * @param ownerScope
	 * @param filename
	 */
	public ASTAbstractTypeSpecifierDeclaration(IContainerSymbol ownerScope,
			IASTTypeSpecifier typeSpecifier, IASTTemplate ownerTemplate,
			int startingOffset, int startingLine, int endingOffset,
			int endingLine, boolean isFriend, char[] filename) {
		super(ownerScope);
		this.typeSpec = typeSpecifier;
		this.ownerTemplate = ownerTemplate;
		this.isFriendDeclaration = isFriend;
		setStartingOffsetAndLineNumber(startingOffset, startingLine);
		setEndingOffsetAndLineNumber(endingOffset, endingLine);
		fn = filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		try {
			if (isFriendDeclaration())
				requestor.acceptFriendDeclaration(this);
			else
				requestor.acceptAbstractTypeSpecDeclaration(this);
		} catch (Exception e) {
			/* do nothing */
		}
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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeSpecifierOwner#getTypeSpecifier()
	 */
	public IASTTypeSpecifier getTypeSpecifier() {
		return typeSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplatedDeclaration#getOwnerTemplateDeclaration()
	 */
	public IASTTemplate getOwnerTemplateDeclaration() {
		return ownerTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractTypeSpecifierDeclaration#isFriendDeclaration()
	 */
	public boolean isFriendDeclaration() {
		return isFriendDeclaration;
	}

	private int startingLineNumber, startingOffset, endingLineNumber,
			endingOffset;

	private final char[] fn;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getFilename()
	 */
	public char[] getFilename() {
		return fn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getName()
	 */
	public String getName() {
		if (typeSpec instanceof IASTOffsetableNamedElement)
			return ((IASTOffsetableNamedElement) typeSpec).getName();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		if (typeSpec instanceof IASTOffsetableNamedElement)
			return ((IASTOffsetableNamedElement) typeSpec).getNameCharArray();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameOffset()
	 */
	public int getNameOffset() {
		if (typeSpec instanceof IASTOffsetableNamedElement)
			return ((IASTOffsetableNamedElement) typeSpec).getNameOffset();

		return 0;
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
		if (typeSpec instanceof IASTOffsetableNamedElement)
			return ((IASTOffsetableNamedElement) typeSpec).getNameEndOffset();

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#setNameEndOffsetAndLineNumber(int,
	 *      int)
	 */
	public void setNameEndOffsetAndLineNumber(int offset, int lineNumber) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameLineNumber()
	 */
	public int getNameLineNumber() {
		if (typeSpec instanceof IASTOffsetableNamedElement)
			return ((IASTOffsetableNamedElement) typeSpec).getNameLineNumber();

		return 0;
	}
}
