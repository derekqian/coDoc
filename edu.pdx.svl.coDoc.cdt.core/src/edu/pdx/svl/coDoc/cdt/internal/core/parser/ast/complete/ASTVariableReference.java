/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate;
import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariableReference;

public class ASTVariableReference extends ASTReference implements
		IASTReference, IASTVariableReference {

	private IASTVariable referencedElement;

	/**
	 * @param offset
	 * @param variable
	 */
	public ASTVariableReference(int offset, IASTVariable variable) {
		super(offset);
		referencedElement = variable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ReferenceCache.ASTReference#initialize(int)
	 */
	public void initialize(int o, ISourceElementCallbackDelegate var) {
		super.initialize(o);
		referencedElement = (IASTVariable) var;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ReferenceCache.ASTReference#reset()
	 */
	public void reset() {
		super.resetOffset();
		referencedElement = null;
	}

	/**
	 * 
	 */
	public ASTVariableReference() {
		super(0);
		referencedElement = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTReference#getReferencedElement()
	 */
	public ISourceElementCallbackDelegate getReferencedElement() {
		return referencedElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		try {
			requestor.acceptVariableReference(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}
}
