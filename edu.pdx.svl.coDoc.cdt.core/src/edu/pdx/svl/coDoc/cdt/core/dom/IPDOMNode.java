/*******************************************************************************
 * Copyright (c) 2006 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.core.dom;

import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public interface IPDOMNode {

	/**
	 * Visit the children of this node.
	 * 
	 * @param visitor
	 * @throws CoreException
	 */
	public void accept(IPDOMVisitor visitor) throws CoreException;
}
