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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunctionReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTReference;

public class ASTFunctionReference extends ASTReference implements
		IASTReference, IASTFunctionReference {
	private IASTFunction declaration;

	/**
	 * @param offset
	 */
	public ASTFunctionReference(int offset, IASTFunction referencedDeclaration) {
		super(offset);
		this.declaration = referencedDeclaration;
	}

	/**
	 * 
	 */
	public ASTFunctionReference() {
		super(0);
		declaration = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTReference#getReferencedElement()
	 */
	public ISourceElementCallbackDelegate getReferencedElement() {
		return declaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		try {
			requestor.acceptFunctionReference(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ReferenceCache.ASTReference#initialize(int,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate)
	 */
	public void initialize(int o,
			ISourceElementCallbackDelegate referencedElement) {
		super.initialize(o);
		this.declaration = (IASTFunction) referencedElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ReferenceCache.ASTReference#reset()
	 */
	public void reset() {
		resetOffset();
		this.declaration = null;
	}
}
