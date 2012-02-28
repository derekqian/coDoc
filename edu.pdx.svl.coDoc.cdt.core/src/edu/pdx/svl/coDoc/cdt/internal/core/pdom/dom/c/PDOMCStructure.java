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

package edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.c;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ICompositeType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IField;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMMember;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMMemberOwner;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNode;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNotImplementedError;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMCStructure extends PDOMMemberOwner implements ICompositeType {

	public PDOMCStructure(PDOM pdom, PDOMNode parent, IASTName name)
			throws CoreException {
		super(pdom, parent, name);
	}

	public PDOMCStructure(PDOM pdom, int record) {
		super(pdom, record);
	}

	public int getNodeType() {
		return PDOMCLinkage.CSTRUCTURE;
	}

	public Object clone() {
		throw new PDOMNotImplementedError();
	}

	public int getKey() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IField[] getFields() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IField findField(String name) throws DOMException {
		try {
			PDOMMember[] members = findMembers(name.toCharArray());
			return members.length > 0 ? (PDOMCField) members[0] : null;
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}

	public IScope getCompositeScope() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isSameType(IType type) {
		if (equals(type))
			return true;
		else
			// TODO - see if it matches
			return false;
	}

}
