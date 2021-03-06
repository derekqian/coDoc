/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Nov 29, 2004
 */
package edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;

/**
 * @author aniefer
 */
public interface ICPPNamespaceScope extends ICPPScope {

	/**
	 * Add an IASTNode that nominates another namespace to this scope Most
	 * commonly, ICPPASTUsingDirectives, but in the case of unnamed namespaces,
	 * it could be an ICPPASTNamespaceDefinition
	 * 
	 * @param directive
	 */
	public void addUsingDirective(IASTNode directive) throws DOMException;

	/**
	 * Get the IASTNodes that have been added to this scope to nominate other
	 * namespaces during lookup. (ICPPASTUsingDirective or
	 * ICPPASTNamespaceDefinition)
	 * 
	 * @return
	 */
	public IASTNode[] getUsingDirectives() throws DOMException;

}
