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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPDelegate;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPParameter;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ObjectMap;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPParameter.CPPParameterDelegate;

/**
 * @author aniefer
 */
public class CPPParameterSpecialization extends CPPSpecialization implements
		ICPPParameter {
	private IType type = null;

	/**
	 * @param scope
	 * @param orig
	 * @param argMap
	 */
	public CPPParameterSpecialization(ICPPParameter orig, ICPPScope scope,
			ObjectMap argMap) {
		super(orig, scope, argMap);
	}

	private ICPPParameter getParameter() {
		return (ICPPParameter) getSpecializedBinding();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IVariable#getType()
	 */
	public IType getType() throws DOMException {
		if (type == null) {
			type = CPPTemplates.instantiateType(getParameter().getType(),
					argumentMap);
		}
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IVariable#isStatic()
	 */
	public boolean isStatic() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IVariable#isExtern()
	 */
	public boolean isExtern() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IVariable#isAuto()
	 */
	public boolean isAuto() throws DOMException {
		return getParameter().isAuto();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IVariable#isRegister()
	 */
	public boolean isRegister() throws DOMException {
		return getParameter().isRegister();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPVariable#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}

	public IASTInitializer getDefaultValue() {
		return getParameter().getDefaultValue();
	}

	public ICPPDelegate createDelegate(IASTName name) {
		return new CPPParameterDelegate(name, this);
	}
}
