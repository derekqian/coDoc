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

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunction;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunctionType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IParameter;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMBinding;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNode;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom.PDOMNotImplementedError;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMCFunction extends PDOMBinding implements IFunction {

	public PDOMCFunction(PDOM pdom, PDOMNode parent, IASTName name)
			throws CoreException {
		super(pdom, parent, name);
	}

	public PDOMCFunction(PDOM pdom, int record) {
		super(pdom, record);
	}

	protected int getRecordSize() {
		return RECORD_SIZE;
	}

	public int getNodeType() {
		return PDOMCLinkage.CFUNCTION;
	}

	public IParameter[] getParameters() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IScope getFunctionScope() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public IFunctionType getType() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isStatic() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isExtern() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isAuto() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isRegister() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean isInline() throws DOMException {
		throw new PDOMNotImplementedError();
	}

	public boolean takesVarArgs() throws DOMException {
		throw new PDOMNotImplementedError();
	}

}
