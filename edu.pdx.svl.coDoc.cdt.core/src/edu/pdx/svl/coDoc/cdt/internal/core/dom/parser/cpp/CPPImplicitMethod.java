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
 * Created on Jan 19, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTSimpleDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunctionType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IParameter;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTVisiblityLabel;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPMethod;
import edu.pdx.svl.coDoc.cdt.core.parser.util.CharArrayUtils;

/**
 * @author aniefer
 */
public class CPPImplicitMethod extends CPPImplicitFunction implements
		ICPPMethod {

	public CPPImplicitMethod(ICPPClassScope scope, char[] name,
			IType returnType, IParameter[] params) {
		super(name, scope, returnType, params, false);
	}

	public int getVisibility() throws DOMException {
		IASTDeclaration decl = getPrimaryDeclaration();
		if (decl == null) {
			// 12.1-5, 12.8-10 Implicitl constructors and assignment operators
			// are public
			return ICPPASTVisiblityLabel.v_public;
		}

		IASTCompositeTypeSpecifier cls = (IASTCompositeTypeSpecifier) decl
				.getParent();
		IASTDeclaration[] members = cls.getMembers();
		ICPPASTVisiblityLabel vis = null;
		for (int i = 0; i < members.length; i++) {
			if (members[i] instanceof ICPPASTVisiblityLabel)
				vis = (ICPPASTVisiblityLabel) members[i];
			else if (members[i] == decl)
				break;
		}
		if (vis != null) {
			return vis.getVisibility();
		} else if (cls.getKey() == ICPPASTCompositeTypeSpecifier.k_class) {
			return ICPPASTVisiblityLabel.v_private;
		}
		return ICPPASTVisiblityLabel.v_public;
	}

	public ICPPClassType getClassOwner() throws DOMException {
		ICPPClassScope scope = (ICPPClassScope) getScope();
		return scope.getClassType();
	}

	public IASTDeclaration getPrimaryDeclaration() throws DOMException {
		// first check if we already know it
		if (declarations != null) {
			for (int i = 0; i < declarations.length; i++) {
				IASTDeclaration decl = (IASTDeclaration) declarations[i]
						.getParent();
				if (decl.getParent() instanceof ICPPASTCompositeTypeSpecifier)
					return decl;
			}
		}

		IFunctionType ftype = getType();
		IType[] params = ftype.getParameterTypes();

		ICPPASTCompositeTypeSpecifier compSpec = (ICPPASTCompositeTypeSpecifier) getScope()
				.getPhysicalNode();
		IASTDeclaration[] members = compSpec.getMembers();
		for (int i = 0; i < members.length; i++) {
			IASTDeclarator dtor = null;
			IASTDeclarator[] ds = null;
			int di = -1;

			IASTDeclaration member = members[i];
			if (member instanceof ICPPASTTemplateDeclaration)
				member = ((ICPPASTTemplateDeclaration) member).getDeclaration();
			if (member instanceof IASTSimpleDeclaration) {
				ds = ((IASTSimpleDeclaration) member).getDeclarators();
			} else if (member instanceof IASTFunctionDefinition) {
				dtor = ((IASTFunctionDefinition) member).getDeclarator();
			}
			if (ds != null && ds.length > 0) {
				di = 0;
				dtor = ds[0];
			}

			while (dtor != null) {
				IASTName name = dtor.getName();
				if (dtor instanceof ICPPASTFunctionDeclarator
						&& CharArrayUtils.equals(name.toCharArray(),
								getNameCharArray())) {
					IFunctionType t = (IFunctionType) CPPVisitor
							.createType(dtor);
					IType[] ps = t.getParameterTypes();
					if (ps.length == params.length) {
						int idx = 0;
						for (; idx < ps.length && ps[idx] != null; idx++) {
							if (!ps[idx].isSameType(params[idx]))
								break;
						}
						if (idx == ps.length) {
							name.setBinding(this);
							if (member instanceof IASTSimpleDeclaration)
								addDeclaration(dtor);
							else if (member instanceof IASTFunctionDefinition)
								addDefinition(dtor);
							return members[i];
						}

					}
				}
				dtor = (di > -1 && ++di < ds.length) ? ds[di] : null;
			}
		}
		return null;
	}

	public boolean isVirtual() {
		return false;
	}

	public boolean isDestructor() {
		char[] n = getNameCharArray();
		if (n != null && n.length > 0)
			return n[0] == '~';
		return false;
	}
}
