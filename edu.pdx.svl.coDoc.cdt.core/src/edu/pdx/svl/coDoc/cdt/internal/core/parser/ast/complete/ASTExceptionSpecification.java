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
import java.util.List;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExceptionSpecification;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.EmptyIterator;

/**
 * @author jcamelon
 * 
 */
public class ASTExceptionSpecification implements IASTExceptionSpecification {
	private final List typeIds;

	/**
	 * @param newTypeIds
	 */
	public ASTExceptionSpecification(List newTypeIds) {
		this.typeIds = newTypeIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExceptionSpecification#getTypeIds()
	 */
	public Iterator getTypeIds() {
		if (typeIds == null)
			return EmptyIterator.EMPTY_ITERATOR;
		return typeIds.iterator();
	}
}
