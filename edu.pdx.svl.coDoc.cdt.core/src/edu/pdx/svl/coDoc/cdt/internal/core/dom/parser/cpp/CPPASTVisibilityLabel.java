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
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTVisiblityLabel;

/**
 * @author jcamelon
 */
public class CPPASTVisibilityLabel extends CPPASTNode implements
		ICPPASTVisiblityLabel {

	private int visibility;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTVisiblityLabel#getVisibility()
	 */
	public int getVisibility() {
		return visibility;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTVisiblityLabel#setVisibility(int)
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public boolean accept(ASTVisitor action) {
		return true;
	}
}
