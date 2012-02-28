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

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblemTypeId;

/**
 * @author jcamelon
 */
public class CPPASTProblemTypeId extends CPPASTTypeId implements
		IASTProblemTypeId {

	private IASTProblem problem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblemTypeId#getProblem()
	 */
	public IASTProblem getProblem() {
		return problem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblemTypeId#setProblem(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem)
	 */
	public void setProblem(IASTProblem p) {
		problem = p;
	}

}
