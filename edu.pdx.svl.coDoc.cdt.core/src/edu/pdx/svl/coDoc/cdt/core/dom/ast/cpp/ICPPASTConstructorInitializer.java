/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTNodeProperty;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer;

/**
 * This is an initializer that is a call to the constructor for the declarator.
 * 
 * @author Doug Schaefer
 */
public interface ICPPASTConstructorInitializer extends IASTInitializer {

	/**
	 * <code>EXPRESSION</code> represents the expression being conusmed in a
	 * constructor.
	 */
	public static final ASTNodeProperty EXPRESSION = new ASTNodeProperty(
			"ICPPASTConstructorInitializer.EXPRESSION - Expression consumed in constructor"); //$NON-NLS-1$

	/**
	 * Get the arguments to the constructor.
	 * 
	 * @return IASTExpression
	 */
	public IASTExpression getExpression();

	/**
	 * Set the arguments to the constructor.
	 * 
	 * @param expression
	 */
	public void setExpression(IASTExpression expression);

}
