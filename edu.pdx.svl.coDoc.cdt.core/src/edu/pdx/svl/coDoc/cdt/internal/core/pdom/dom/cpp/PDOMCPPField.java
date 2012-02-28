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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPField;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPVisitor;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMMember;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNotImplementedError;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMCPPField extends PDOMMember implements ICPPField {

	public PDOMCPPField(PDOM pdom, PDOMCPPClassType parent, IASTName name)
			throws CoreException {
		super(pdom, parent, name);
	}

	public PDOMCPPField(PDOM pdom, int bindingRecord) {
		super(pdom, bindingRecord);
	}

	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	public int getNodeType() {
		return PDOMCPPLinkage.CPPFIELD;
	}

	public ICPPClassType getClassOwner() throws DOMException {
		try {
			return (ICPPClassType) getMemberOwner();
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}

	public String[] getQualifiedName() throws DOMException {
		return CPPVisitor.getQualifiedName(this);
	}

	public char[][] getQualifiedNameCharArray() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isGloballyQualified() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public int getVisibility() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isMutable() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IType getType() throws DOMException {
		// TODO
		return null;
	}

	public boolean isAuto() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isExtern() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isRegister() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isStatic() throws DOMException {
		throw new PDOMNotImplementedError();
	}

}
