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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.IASTNewExpressionDescriptor;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.EmptyIterator;

/**
 * @author jcamelon
 */
public class ASTNewDescriptor implements IASTNewExpressionDescriptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.IASTNewExpressionDescriptor#getNewInitializerExpressions()
	 */
	public Iterator getNewInitializerExpressions() {
		return EmptyIterator.EMPTY_ITERATOR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.IASTNewExpressionDescriptor#getNewPlacementExpressions()
	 */
	public Iterator getNewPlacementExpressions() {
		return EmptyIterator.EMPTY_ITERATOR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.IASTNewExpressionDescriptor#getTypeIdExpressions()
	 */
	public Iterator getNewTypeIdExpressions() {
		return EmptyIterator.EMPTY_ITERATOR;
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
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.IASTNewExpressionDescriptor#freeReferences(edu.pdx.svl.coDoc.cdt.core.parser.ast.IReferenceManager)
	 */
	public void freeReferences() {
	}

}
