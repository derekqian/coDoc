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
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumeratorReference;

public class ASTEnumeratorReference extends ASTReference implements
		IASTEnumeratorReference {

	private IASTEnumerator enumerator;

	/**
	 * @param offset
	 * @param enumerator
	 */
	public ASTEnumeratorReference(int offset, IASTEnumerator enumerator) {
		super(offset);
		this.enumerator = enumerator;
	}

	/**
	 * 
	 */
	public ASTEnumeratorReference() {
		super(0);
		enumerator = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTReference#getReferencedElement()
	 */
	public ISourceElementCallbackDelegate getReferencedElement() {
		return enumerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
		try {
			requestor.acceptEnumeratorReference(this);
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
		this.enumerator = (IASTEnumerator) referencedElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ReferenceCache.ASTReference#reset()
	 */
	public void reset() {
		super.resetOffset();
		this.enumerator = null;
	}
}
