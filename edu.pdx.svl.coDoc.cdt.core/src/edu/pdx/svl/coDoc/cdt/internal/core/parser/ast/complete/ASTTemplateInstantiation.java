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

import java.util.Iterator;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTNotImplementedException;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateInstantiation;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.IContainerSymbol;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbol;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ITemplateFactory;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ITemplateSymbol;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.StandardSymbolExtension;

/**
 * @author jcamelon
 * 
 */
public class ASTTemplateInstantiation extends ASTSymbol implements
		IASTTemplateInstantiation {
	private ITemplateFactory factory;

	private IASTScope ownerScope;

	private IASTTemplateDeclaration instantiatedTemplate;

	private ISymbol instance;

	private final char[] fn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTOffsetableElement#getFilename()
	 */
	public char[] getFilename() {
		return fn;
	}

	public ASTTemplateInstantiation(IASTScope scope, char[] filename) {
		super(null);
		IContainerSymbol container = ((ASTScope) scope).getContainerSymbol();

		factory = container.getSymbolTable().newTemplateFactory();
		factory.setContainingSymbol(container);

		factory.setASTExtension(new StandardSymbolExtension(factory, this));

		factory.pushTemplate(null);
		ownerScope = scope;
		fn = filename;
	}

	public IASTTemplateDeclaration getInstantiatedTemplate() {
		return instantiatedTemplate;
	}

	public void releaseFactory() {
		factory = null;
	}

	public void setInstanceSymbol(ISymbol sym) {
		instance = sym;
		ITemplateSymbol template = (ITemplateSymbol) instance
				.getInstantiatedSymbol().getContainingSymbol();
		instantiatedTemplate = (IASTTemplateDeclaration) template
				.getASTExtension().getPrimaryDeclaration();
		setSymbol(instance.getInstantiatedSymbol().getContainingSymbol());
	}

	public ISymbol getInstanceSymbol() {
		return instance;
	}

	public IContainerSymbol getContainerSymbol() {
		return factory != null ? (IContainerSymbol) factory
				: ((ASTTemplateDeclaration) getInstantiatedTemplate())
						.getContainerSymbol();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplate#getOwnedDeclaration()
	 */
	public IASTDeclaration getOwnedDeclaration() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplate#setOwnedDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration)
	 */
	public void setOwnedDeclaration(IASTDeclaration declaration) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScopedElement#getOwnerScope()
	 */
	public IASTScope getOwnerScope() {
		return ownerScope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#enterScope(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void enterScope(ISourceElementRequestor requestor) {
		try {
			requestor.enterTemplateInstantiation(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#exitScope(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void exitScope(ISourceElementRequestor requestor) {
		try {
			requestor.exitTemplateExplicitInstantiation(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope#getDeclarations()
	 */
	public Iterator getDeclarations() throws ASTNotImplementedException {
		// TODO Auto-generated method stub
		return null;
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
