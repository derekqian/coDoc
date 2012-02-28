/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 13, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ITypedef;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTSimpleTypeTemplateParameter;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateTypeParameter;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ObjectMap;

/**
 * @author aniefer
 */
public class CPPTemplateTypeParameter extends CPPTemplateParameter implements
		ICPPTemplateTypeParameter, IType, ICPPInternalUnknown {

	private ICPPScope unknownScope = null;

	/**
	 * @param name
	 */
	public CPPTemplateTypeParameter(IASTName name) {
		super(name);
	}

	public ICPPScope getUnknownScope() {
		if (unknownScope == null) {
			IASTName n = null;
			IASTNode[] nodes = getDeclarations();
			if (nodes != null && nodes.length > 0)
				n = (IASTName) nodes[0];
			unknownScope = new CPPUnknownScope(this, n);
		}
		return unknownScope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateTypeParameter#getDefault()
	 */
	public IType getDefault() {
		IASTNode[] nds = getDeclarations();
		if (nds == null || nds.length == 0)
			return null;
		IASTName name = (IASTName) nds[0];
		ICPPASTSimpleTypeTemplateParameter simple = (ICPPASTSimpleTypeTemplateParameter) name
				.getParent();
		IASTTypeId typeId = simple.getDefaultType();
		if (typeId != null)
			return CPPVisitor.createType(typeId);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IType#isSameType(edu.pdx.svl.coDoc.cdt.core.dom.ast.IType)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalUnknown#resolveUnknown(edu.pdx.svl.coDoc.cdt.core.parser.util.ObjectMap)
	 */
	public IBinding resolveUnknown(ObjectMap argMap) {
		// TODO Auto-generated method stub
		return null;
	}
}
