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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTNotImplementedException;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.Parser;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.ASTQualifiedNamedElement;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbol;

/**
 * @author jcamelon
 * 
 */
public class ASTTypedef extends ASTSymbol implements IASTTypedefDeclaration {

	private final IASTAbstractDeclaration mapping;

	private final ASTQualifiedNamedElement qualifiedName;

	private List references;

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
	 * @param newSymbol
	 * @param mapping
	 * @param startingOffset
	 * @param nameOffset
	 * @param references
	 * @param filename
	 */
	public ASTTypedef(ISymbol newSymbol, IASTAbstractDeclaration mapping,
			int startingOffset, int startingLine, int nameOffset,
			int nameEndOffset, int nameLine, List references, char[] filename) {
		super(newSymbol);
		this.mapping = mapping;
		this.references = references;
		this.qualifiedName = new ASTQualifiedNamedElement(getOwnerScope(),
				newSymbol.getName());
		setStartingOffsetAndLineNumber(startingOffset, startingLine);
		setNameOffset(nameOffset);
		setNameEndOffsetAndLineNumber(nameEndOffset, nameLine);
		fn = filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration#getName()
	 */
	public String getName() {
		return String.valueOf(getSymbol().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration#getAbstractDeclarator()
	 */
	public IASTAbstractDeclaration getAbstractDeclarator() {
		return mapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		try {
			requestor.acceptTypedefDeclaration(this);
		} catch (Exception e) {
			/* do nothing */
		}
		Parser.processReferences(references, requestor);
		references = null;
		getAbstractDeclarator().acceptElement(requestor);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration#getFinalTypeSpecifier()
	 */
	public IASTTypeSpecifier getFinalTypeSpecifier()
			throws ASTNotImplementedException {
		IASTTypeSpecifier currentTypeSpec = mapping.getTypeSpecifier();
		while (currentTypeSpec instanceof IASTSimpleTypeSpecifier
				|| currentTypeSpec instanceof IASTTypedefDeclaration) {
			if (currentTypeSpec instanceof IASTSimpleTypeSpecifier) {
				IASTSimpleTypeSpecifier simpleTypeSpec = (IASTSimpleTypeSpecifier) currentTypeSpec;
				if (simpleTypeSpec.getType() == IASTSimpleTypeSpecifier.Type.CLASS_OR_TYPENAME)
					currentTypeSpec = simpleTypeSpec.getTypeSpecifier();
				else
					break;
			} else if (currentTypeSpec instanceof IASTTypedefDeclaration) {
				currentTypeSpec = ((IASTTypedefDeclaration) currentTypeSpec)
						.getAbstractDeclarator().getTypeSpecifier();
			}
		}
		return currentTypeSpec;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableNamedElement#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		return getSymbol().getName();
	}
}
