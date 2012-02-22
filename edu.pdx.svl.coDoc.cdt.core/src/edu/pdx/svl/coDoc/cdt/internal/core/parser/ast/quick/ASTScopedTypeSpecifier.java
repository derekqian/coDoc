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

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScopedTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.ASTQualifiedNamedElement;

/**
 * @author jcamelon
 * 
 */
public class ASTScopedTypeSpecifier extends ASTQualifiedNamedElement implements
		IASTScopedTypeSpecifier {
	private final IASTScope scope;

	public ASTScopedTypeSpecifier(IASTScope scope, char[] name) {
		super(scope, name);
		this.scope = scope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScopedElement#getOwnerScope()
	 */
	public IASTScope getOwnerScope() {
		return scope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode#lookup(java.lang.String,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode.LookupKind[],
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode)
	 */
	public ILookupResult lookup(String prefix, LookupKind[] kind,
			IASTNode context, IASTExpression functionParameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
