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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;

/**
 * @author jcamelon
 */
public class CPPASTEnumerationSpecifier extends CPPASTBaseDeclSpecifier
		implements IASTEnumerationSpecifier, ICPPASTDeclSpecifier {

	private IASTName name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier#addEnumerator(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator)
	 */
	public void addEnumerator(IASTEnumerator enumerator) {
		if (enumerator != null) {
			enumeratorsPos++;
			enumerators = (IASTEnumerator[]) ArrayUtil.append(
					IASTEnumerator.class, enumerators, enumerator);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier#getEnumerators()
	 */
	public IASTEnumerator[] getEnumerators() {
		if (enumerators == null)
			return IASTEnumerator.EMPTY_ENUMERATOR_ARRAY;
		enumerators = (IASTEnumerator[]) ArrayUtil.removeNullsAfter(
				IASTEnumerator.class, enumerators, enumeratorsPos);
		return enumerators;
	}

	private IASTEnumerator[] enumerators = null;

	private int enumeratorsPos = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier#setName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public void setName(IASTName name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier#getName()
	 */
	public IASTName getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier#getRawSignature()
	 */
	public String getRawSignature() {
		return getName().toString() == null ? "" : getName().toString(); //$NON-NLS-1$
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitDeclSpecifiers) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
		if (name != null)
			if (!name.accept(action))
				return false;
		IASTEnumerator[] enums = getEnumerators();
		for (int i = 0; i < enums.length; i++)
			if (!enums[i].accept(action))
				return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNameOwner#getRoleForName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public int getRoleForName(IASTName n) {
		if (name == n)
			return r_definition;
		return r_unclear;
	}
}
