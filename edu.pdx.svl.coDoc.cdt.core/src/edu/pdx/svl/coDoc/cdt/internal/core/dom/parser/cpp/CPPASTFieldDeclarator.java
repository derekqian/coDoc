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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFieldDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTFieldDeclarator extends CPPASTDeclarator implements
		IASTFieldDeclarator, IASTAmbiguityParent {

	private IASTExpression bitField;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFieldDeclarator#getBitFieldSize()
	 */
	public IASTExpression getBitFieldSize() {
		return bitField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFieldDeclarator#setBitFieldSize(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setBitFieldSize(IASTExpression size) {
		this.bitField = size;
	}

	protected boolean postAccept(ASTVisitor action) {
		if (bitField != null)
			if (!bitField.accept(action))
				return false;

		IASTInitializer initializer = getInitializer();
		if (initializer != null)
			if (!initializer.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == bitField) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			bitField = (IASTExpression) other;
		}

	}

}
