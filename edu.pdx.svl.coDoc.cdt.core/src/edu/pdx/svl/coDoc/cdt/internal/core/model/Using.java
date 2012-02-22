/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.model;

import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.IUsing;

public class Using extends SourceManipulation implements IUsing {

	boolean directive;

	public Using(ICElement parent, String name, boolean isDirective) {
		super(parent, name, ICElement.C_USING);
		directive = isDirective;
	}

	protected CElementInfo createElementInfo() {
		return new SourceManipulationInfo(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IUsing#isDirective()
	 */
	public boolean isDirective() {
		return directive;
	}

}
