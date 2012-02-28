/*******************************************************************************
 * Copyright (c) 2006 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceAlias;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceScope;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMBinding;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNode;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNotImplementedError;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMCPPNamespaceAlias extends PDOMBinding implements
		ICPPNamespaceAlias {

	public PDOMCPPNamespaceAlias(PDOM pdom, PDOMNode parent, IASTName name)
			throws CoreException {
		super(pdom, parent, name);
	}

	public PDOMCPPNamespaceAlias(PDOM pdom, int record) {
		super(pdom, record);
	}

	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	public int getNodeType() {
		return PDOMCPPLinkage.CPPNAMESPACEALIAS;
	}

	public ICPPNamespaceScope getNamespaceScope() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IBinding[] getMemberBindings() throws DOMException {
		throw new PDOMNotImplementedError();
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

	public int getDelegateType() {
		throw new PDOMNotImplementedError();
	}

	public IBinding getBinding() {
		throw new PDOMNotImplementedError();
	}

}
