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
package edu.pdx.svl.coDoc.cdt.core.parser.ast;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate;

/**
 * @author jcamelon
 * 
 */
public interface IASTInclusion extends IASTOffsetableNamedElement,
		ISourceElementCallbackDelegate {

	public String getName();

	public String getFullFileName();

	public boolean isLocal();

	public boolean isImplicit();
}
