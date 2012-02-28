/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
/*
 * Created on Mar 29, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPDelegate;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPField;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ObjectMap;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPField.CPPFieldDelegate;

/**
 * @author aniefer
 */
public class CPPFieldSpecialization extends CPPSpecialization implements
		ICPPField {
	private IType type = null;

	/**
	 * @param orig
	 * @param args
	 * @param args
	 */
	public CPPFieldSpecialization(IBinding orig, ICPPScope scope,
			ObjectMap argMap) {
		super(orig, scope, argMap);
	}

	private ICPPField getField() {
		return (ICPPField) getSpecializedBinding();
	}

	public int getVisibility() throws DOMException {
		return getField().getVisibility();
	}

	public ICPPClassType getClassOwner() throws DOMException {
		return getField().getClassOwner();
	}

	public IType getType() throws DOMException {
		if (type == null) {
			type = CPPTemplates.instantiateType(getField().getType(),
					argumentMap);
		}
		return type;
	}

	public boolean isStatic() throws DOMException {
		return getField().isStatic();
	}

	public boolean isExtern() throws DOMException {
		return getField().isExtern();
	}

	public boolean isAuto() throws DOMException {
		return getField().isAuto();
	}

	public boolean isRegister() throws DOMException {
		return getField().isRegister();
	}

	public boolean isMutable() throws DOMException {
		return getField().isMutable();
	}

	public ICPPDelegate createDelegate(IASTName name) {
		return new CPPFieldDelegate(name, this);
	}

}
