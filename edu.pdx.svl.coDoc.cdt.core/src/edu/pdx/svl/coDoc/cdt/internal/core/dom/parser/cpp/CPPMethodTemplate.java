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
 * Created on Mar 31, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTSimpleDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTVisiblityLabel;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPClassType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPMethod;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.CharArrayUtils;

/**
 * @author aniefer
 */
public class CPPMethodTemplate extends CPPFunctionTemplate implements
		ICPPMethod {

	/**
	 * @param name
	 */
	public CPPMethodTemplate(IASTName name) {
		super(name);
	}

	public IASTDeclaration getPrimaryDeclaration() throws DOMException {
		// first check if we already know it
		if (declarations != null) {
			for (int i = 0; i < declarations.length; i++) {
				IASTNode parent = declarations[i].getParent();
				while (!(parent instanceof IASTDeclaration))
					parent = parent.getParent();

				IASTDeclaration decl = (IASTDeclaration) parent.getParent();
				if (decl instanceof ICPPASTCompositeTypeSpecifier)
					return decl;
			}
		}

		char[] myName = getNameCharArray();

		IScope scope = getScope();
		if (scope instanceof ICPPTemplateScope)
			scope = scope.getParent();
		ICPPClassScope clsScope = (ICPPClassScope) scope;
		ICPPASTCompositeTypeSpecifier compSpec = (ICPPASTCompositeTypeSpecifier) clsScope
				.getPhysicalNode();
		IASTDeclaration[] members = compSpec.getMembers();
		for (int i = 0; i < members.length; i++) {
			if (members[i] instanceof ICPPASTTemplateDeclaration) {
				IASTDeclaration decl = ((ICPPASTTemplateDeclaration) members[i])
						.getDeclaration();
				if (decl instanceof IASTSimpleDeclaration) {
					IASTDeclarator[] dtors = ((IASTSimpleDeclaration) decl)
							.getDeclarators();
					for (int j = 0; j < dtors.length; j++) {
						IASTName name = CPPVisitor.getMostNestedDeclarator(
								dtors[j]).getName();
						if (CharArrayUtils.equals(name.toCharArray(), myName)
								&& name.resolveBinding() == this) {
							return members[i];
						}
					}
				} else if (decl instanceof IASTFunctionDefinition) {
					IASTName name = CPPVisitor.getMostNestedDeclarator(
							((IASTFunctionDefinition) decl).getDeclarator())
							.getName();
					if (CharArrayUtils.equals(name.toCharArray(), myName)
							&& name.resolveBinding() == this) {
						return members[i];
					}
				}
			}

		}
		return null;
	}

	public int getVisibility() throws DOMException {
		IASTDeclaration decl = getPrimaryDeclaration();
		if (decl == null) {
			IScope scope = getScope();
			if (scope instanceof ICPPTemplateScope)
				scope = scope.getParent();
			if (scope instanceof ICPPClassScope) {
				ICPPClassType cls = ((ICPPClassScope) scope).getClassType();
				if (cls != null)
					return (cls.getKey() == ICPPClassType.k_class) ? ICPPASTVisiblityLabel.v_private
							: ICPPASTVisiblityLabel.v_public;
			}
			return ICPPASTVisiblityLabel.v_private;
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

	public boolean isVirtual() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInline() throws DOMException {
		IASTDeclaration decl = getPrimaryDeclaration();
		if (decl instanceof ICPPASTTemplateDeclaration
				&& ((ICPPASTTemplateDeclaration) decl).getDeclaration() instanceof IASTFunctionDefinition)
			return true;

		return super.isInline();
	}

	public boolean isDestructor() {
		char[] name = getNameCharArray();
		if (name.length > 1 && name[0] == '~')
			return true;

		return false;
	}

}
