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
 * Created on Dec 14, 2004
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IEnumeration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IEnumerator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ITypedef;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPBlockScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPDelegate;
import org.eclipse.core.runtime.PlatformObject;

/**
 * @author aniefer
 */
public class CPPEnumeration extends PlatformObject implements IEnumeration,
		ICPPInternalBinding {
	public static class CPPEnumerationDelegate extends CPPDelegate implements
			IEnumeration {
		public CPPEnumerationDelegate(IASTName name, IEnumeration binding) {
			super(name, binding);
		}

		public IEnumerator[] getEnumerators() throws DOMException {
			return ((IEnumeration) getBinding()).getEnumerators();
		}

		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
			}
			return null;
		}

		public boolean isSameType(IType type) {
			return ((IEnumeration) getBinding()).isSameType(type);
		}

	}

	private IASTName enumName;

	/**
	 * @param specifier
	 */
	public CPPEnumeration(IASTName name) {
		this.enumName = name;
		name.setBinding(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPBinding#getDeclarations()
	 */
	public IASTNode[] getDeclarations() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPBinding#getDefinition()
	 */
	public IASTNode getDefinition() {
		return enumName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getName()
	 */
	public String getName() {
		return enumName.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		return enumName.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getScope()
	 */
	public IScope getScope() {
		return CPPVisitor.getContainingScope(enumName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getPhysicalNode()
	 */
	public IASTNode getPhysicalNode() {
		return enumName;
	}

	public Object clone() {
		IType t = null;
		try {
			t = (IType) super.clone();
		} catch (CloneNotSupportedException e) {
			// not going to happen
		}
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IEnumeration#getEnumerators()
	 */
	public IEnumerator[] getEnumerators() {
		IASTEnumerationSpecifier.IASTEnumerator[] enums = ((IASTEnumerationSpecifier) enumName
				.getParent()).getEnumerators();
		IEnumerator[] bindings = new IEnumerator[enums.length];

		for (int i = 0; i < enums.length; i++) {
			bindings[i] = (IEnumerator) enums[i].getName().resolveBinding();
		}
		return bindings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getFullyQualifiedName()
	 */
	public String[] getQualifiedName() {
		return CPPVisitor.getQualifiedName(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getFullyQualifiedNameCharArray()
	 */
	public char[][] getQualifiedNameCharArray() {
		return CPPVisitor.getQualifiedNameCharArray(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPBinding#isGloballyQualified()
	 */
	public boolean isGloballyQualified() throws DOMException {
		IScope scope = getScope();
		while (scope != null) {
			if (scope instanceof ICPPBlockScope)
				return false;
			scope = scope.getParent();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#createDelegate(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public ICPPDelegate createDelegate(IASTName name) {
		return new CPPEnumerationDelegate(name, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#addDefinition(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode)
	 */
	public void addDefinition(IASTNode node) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#addDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode)
	 */
	public void addDeclaration(IASTNode node) {
		// TODO Auto-generated method stub

	}

	public void removeDeclaration(IASTNode node) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IType#isSameType(edu.pdx.svl.coDoc.cdt.core.dom.ast.IType)
	 */
	public boolean isSameType(IType type) {
		if (type == this)
			return true;
		if (type instanceof ITypedef)
			return ((ITypedef) type).isSameType(this);
		return false;
	}
}
