/*******************************************************************************
 * Copyright (c) 2000, 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.core.model;

/**
 * Represents a "using" declaration in C translation unit.
 */
public interface IUsing extends ICElement, ISourceManipulation,
		ISourceReference {
	/**
	 * Returns the name of the package the statement refers to. This is a
	 * handle-only method.
	 */
	String getElementName();

	boolean isDirective();
}
