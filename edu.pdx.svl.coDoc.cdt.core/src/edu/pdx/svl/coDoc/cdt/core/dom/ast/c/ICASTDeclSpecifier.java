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
package edu.pdx.svl.coDoc.cdt.core.dom.ast.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;

/**
 * C extension to IASTDeclSpecifier. (restrict keyword)
 * 
 * @author Doug Schaefer
 */
public interface ICASTDeclSpecifier extends IASTDeclSpecifier {

	/**
	 * Is restrict keyword used?
	 * 
	 * @return boolean
	 */
	public boolean isRestrict();

	/**
	 * Set restrict to value.
	 * 
	 * @param value
	 */
	public void setRestrict(boolean value);

}
