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

import java.util.Iterator;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTNotImplementedException;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.EmptyIterator;

/**
 * @author jcamelon
 */
public class ASTInitializerClause implements IASTInitializerClause {

	private final IASTInitializerClause.Kind kind;

	private IASTVariable ownerDeclaration = null;

	/**
	 * @param kind
	 * @param assignmentExpression
	 * @param initializerClauses
	 */
	public ASTInitializerClause(Kind kind) {
		this.kind = kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#getKind()
	 */
	public Kind getKind() {
		return kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#getInitializerList()
	 */
	public Iterator getInitializers() {
		return EmptyIterator.EMPTY_ITERATOR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#getAssigmentExpression()
	 */
	public IASTExpression getAssigmentExpression() {
		return null;
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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#getDesignators()
	 */
	public Iterator getDesignators() {
		return EmptyIterator.EMPTY_ITERATOR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#setOwnerDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration)
	 */
	public void setOwnerVariableDeclaration(IASTVariable declaration) {
		ownerDeclaration = declaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#getOwnerDeclaration()
	 */
	public IASTVariable getOwnerVariableDeclaration() {
		return ownerDeclaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInitializerClause#findExpressionForDuple(edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple)
	 */
	public IASTExpression findExpressionForDuple(ITokenDuple finalDuple)
			throws ASTNotImplementedException {
		throw new ASTNotImplementedException();
	}

}
