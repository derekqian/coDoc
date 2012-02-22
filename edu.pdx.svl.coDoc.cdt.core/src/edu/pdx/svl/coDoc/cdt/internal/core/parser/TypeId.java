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
package edu.pdx.svl.coDoc.cdt.internal.core.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTPointerOperator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTArrayModifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;

/**
 * @author jcamelon
 * 
 */
public class TypeId implements IDeclarator {
	private static final int DEFAULT_ARRAYLIST_SIZE = 4;

	private ITokenDuple name;

	private List arrayModifiers;

	private List pointerOperators;

	private IASTScope scope;

	/**
	 * @param scope2
	 */
	public void reset(IASTScope s) {
		this.scope = s;
		arrayModifiers = Collections.EMPTY_LIST;
		pointerOperators = Collections.EMPTY_LIST;
		name = null;
	}

	/**
	 * 
	 */
	public TypeId() {
		reset(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#getPointerOperators()
	 */
	public List getPointerOperators() {
		return pointerOperators;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#addPointerOperator(edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTPointerOperator)
	 */
	public void addPointerOperator(ASTPointerOperator ptrOp) {
		if (pointerOperators == Collections.EMPTY_LIST)
			pointerOperators = new ArrayList(DEFAULT_ARRAYLIST_SIZE);
		pointerOperators.add(ptrOp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#addArrayModifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTArrayModifier)
	 */
	public void addArrayModifier(IASTArrayModifier arrayMod) {
		if (arrayModifiers == Collections.EMPTY_LIST)
			arrayModifiers = new ArrayList(DEFAULT_ARRAYLIST_SIZE);
		arrayModifiers.add(arrayMod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#getArrayModifiers()
	 */
	public List getArrayModifiers() {
		return arrayModifiers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#setPointerOperatorName(edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple)
	 */
	public void setPointerOperatorName(ITokenDuple nameDuple) {
		name = nameDuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#getPointerOperatorNameDuple()
	 */
	public ITokenDuple getPointerOperatorNameDuple() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.IDeclarator#getScope()
	 */
	public IASTScope getScope() {
		return scope;
	}
}
