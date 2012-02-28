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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPVariable;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPVisitor;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMBinding;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNode;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNotImplementedError;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMCPPVariable extends PDOMBinding implements ICPPVariable {

	private static final int TYPE_OFFSET = PDOMBinding.RECORD_SIZE + 0;

	protected static final int RECORD_SIZE = PDOMBinding.RECORD_SIZE + 4;

	public PDOMCPPVariable(PDOM pdom, PDOMNode parent, IASTName name)
			throws CoreException {
		super(pdom, parent, name);

		// Find the type record
		IASTNode nameParent = name.getParent();
		if (nameParent instanceof IASTDeclarator) {
			IASTDeclarator declarator = (IASTDeclarator) nameParent;
			IType type = CPPVisitor.createType(declarator);
			if (type != null && type instanceof IBinding) {
				PDOMBinding pdomType = parent.getLinkage().adaptBinding(
						(IBinding) type);
				if (pdomType != null)
					pdom.getDB().putInt(record + TYPE_OFFSET,
							pdomType.getRecord());
			}
		}
	}

	public PDOMCPPVariable(PDOM pdom, int record) {
		super(pdom, record);
	}

	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	public int getNodeType() {
		return PDOMCPPLinkage.CPPVARIABLE;
	}

	public boolean isMutable() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IType getType() throws DOMException {
		try {
			int typeRec = pdom.getDB().getInt(record + TYPE_OFFSET);
			return (IType) getLinkage().getNode(typeRec);
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
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

	public String[] getQualifiedName() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public char[][] getQualifiedNameCharArray() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isGloballyQualified() throws DOMException {
		throw new PDOMNotImplementedError();
	}

}
