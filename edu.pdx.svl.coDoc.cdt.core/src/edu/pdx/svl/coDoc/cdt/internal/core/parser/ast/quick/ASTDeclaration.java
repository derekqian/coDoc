/*******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.quick;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;

/**
 * @author jcamelon
 * 
 */
public abstract class ASTDeclaration extends ASTNode implements IASTDeclaration {

	private final IASTScope scope;

	public ASTDeclaration(IASTScope scope) {
		this.scope = scope;
		if (scope != null && scope instanceof IASTQScope)
			((IASTQScope) scope).addDeclaration(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration#getOwnerScope()
	 */
	public IASTScope getOwnerScope() {
		return scope;
	}

}
