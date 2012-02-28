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
 * Created on Dec 21, 2004
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPConstructor;

/**
 * @author aniefer
 */
public class CPPConstructor extends CPPMethod implements ICPPConstructor {

	static public class CPPConstructorProblem extends
			CPPMethod.CPPMethodProblem implements ICPPConstructor {
		public CPPConstructorProblem(IASTNode node, int id, char[] arg) {
			super(node, id, arg);
		}

		public boolean isExplicit() throws DOMException {
			throw new DOMException(this);
		}
	}

	/**
	 * @param declarator
	 */
	public CPPConstructor(ICPPASTFunctionDeclarator declarator) {
		super(declarator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPConstructor#isExplicit()
	 */
	public boolean isExplicit() {
		// TODO Auto-generated method stub
		return false;
	}

}
