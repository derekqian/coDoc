/*******************************************************************************
 * Copyright (c) 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.cpp;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IField;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPBase;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPConstructor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPField;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPMethod;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMBinding;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMMember;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMMemberOwner;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNode;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNotImplementedError;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMCPPClassType extends PDOMMemberOwner implements ICPPClassType,
		ICPPClassScope {

	private static final int KEY = PDOMMemberOwner.RECORD_SIZE + 0; // byte

	protected static final int RECORD_SIZE = PDOMMemberOwner.RECORD_SIZE + 1;

	public PDOMCPPClassType(PDOM pdom, PDOMNode parent, IASTName name)
			throws CoreException {
		super(pdom, parent, name);

		IBinding binding = name.resolveBinding();
		int key = 0;
		if (binding instanceof ICPPClassType) // not sure why it wouldn't
			key = ((ICPPClassType) binding).getKey();
		pdom.getDB().putByte(record + KEY, (byte) key);
	}

	public PDOMCPPClassType(PDOM pdom, int bindingRecord) {
		super(pdom, bindingRecord);
	}

	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	public int getNodeType() {
		return PDOMCPPLinkage.CPPCLASSTYPE;
	}

	public boolean isSameType(IType type) {
		if (type instanceof PDOMBinding)
			return record == ((PDOMBinding) type).getRecord();
		else
			// TODO - should we check for real?
			return false;
	}

	public Object clone() {
		throw new PDOMNotImplementedError();
	}

	public IField findField(String name) throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public ICPPMethod[] getAllDeclaredMethods() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public ICPPBase[] getBases() throws DOMException {
		// TODO
		return new ICPPBase[0];
	}

	public ICPPConstructor[] getConstructors() throws DOMException {
		// TODO
		return new ICPPConstructor[0];
	}

	public ICPPField[] getDeclaredFields() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public ICPPMethod[] getDeclaredMethods() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IField[] getFields() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IBinding[] getFriends() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public ICPPMethod[] getMethods() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public ICPPClassType[] getNestedClasses() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IScope getCompositeScope() throws DOMException {
		return this;
	}

	public int getKey() throws DOMException {
		try {
			return pdom.getDB().getByte(record + KEY);
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return ICPPClassType.k_class; // or something
		}
	}

	public String[] getQualifiedName() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public char[][] getQualifiedNameCharArray() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isGloballyQualified() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public ICPPClassType getClassType() {
		return null;
		// TODO - do we need the real type?
		// throw new PDOMNotImplementedError();
	}

	public ICPPMethod[] getImplicitMethods() {
		throw new PDOMNotImplementedError();
	}

	public void addBinding(IBinding binding) throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public void addName(IASTName name) throws DOMException {
		// TODO - this might be a better way of adding names to scopes
		// but for now do nothing.
	}

	public IBinding[] find(String name) throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public void flushCache() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IBinding getBinding(IASTName name, boolean resolve)
			throws DOMException {
		try {
			PDOMMember[] matches = findMembers(name.toCharArray());
			// TODO - need to check for overloads
			return matches.length > 0 ? matches[0] : null;
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}

	public IScope getParent() throws DOMException {
		return null;
	}

	public IASTNode getPhysicalNode() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IASTName getScopeName() throws DOMException {
		try {
			IASTName name = getFirstDefinition();
			if (name == null)
				name = getFirstDefinition();
			return name;
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}

	public boolean isFullyCached() throws DOMException {
		return true;
	}

	public void removeBinding(IBinding binding) throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public void setFullyCached(boolean b) throws DOMException {
		throw new PDOMNotImplementedError();
	}

}
