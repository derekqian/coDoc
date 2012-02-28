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
 * Created on May 3, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IField;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPBase;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPConstructor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPField;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPMethod;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;

/**
 * @author aniefer
 */
public class CPPUnknownClass extends CPPUnknownBinding implements ICPPClassType {

	/**
	 * @param scope
	 * @param name
	 */
	public CPPUnknownClass(ICPPScope scope, IBinding scopeBinding, IASTName name) {
		super(scope, scopeBinding, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getBases()
	 */
	public ICPPBase[] getBases() {
		return ICPPBase.EMPTY_BASE_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.ICompositeType#getFields()
	 */
	public IField[] getFields() {
		return IField.EMPTY_FIELD_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.ICompositeType#findField(java.lang.String)
	 */
	public IField findField(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getDeclaredFields()
	 */
	public ICPPField[] getDeclaredFields() {
		return ICPPField.EMPTY_CPPFIELD_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getMethods()
	 */
	public ICPPMethod[] getMethods() {
		return ICPPMethod.EMPTY_CPPMETHOD_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getAllDeclaredMethods()
	 */
	public ICPPMethod[] getAllDeclaredMethods() {
		return ICPPMethod.EMPTY_CPPMETHOD_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getDeclaredMethods()
	 */
	public ICPPMethod[] getDeclaredMethods() {
		return ICPPMethod.EMPTY_CPPMETHOD_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getConstructors()
	 */
	public ICPPConstructor[] getConstructors() {
		return ICPPConstructor.EMPTY_CONSTRUCTOR_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType#getFriends()
	 */
	public IBinding[] getFriends() {
		return IBinding.EMPTY_BINDING_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.ICompositeType#getKey()
	 */
	public int getKey() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.ICompositeType#getCompositeScope()
	 */
	public IScope getCompositeScope() {
		return getUnknownScope();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IType#isSameType(edu.pdx.svl.coDoc.cdt.core.dom.ast.IType)
	 */
	public boolean isSameType(IType type) {
		return type == this;
	}

	public ICPPClassType[] getNestedClasses() {
		return ICPPClassType.EMPTY_CLASS_ARRAY;
	}

}
