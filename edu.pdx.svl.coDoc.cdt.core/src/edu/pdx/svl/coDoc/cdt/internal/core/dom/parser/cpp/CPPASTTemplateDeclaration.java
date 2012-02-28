/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateParameter;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTTemplateDeclaration extends CPPASTNode implements
		ICPPASTTemplateDeclaration, IASTAmbiguityParent {

	private boolean exported;

	private IASTDeclaration declaration;

	private ICPPTemplateScope templateScope;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#isExported()
	 */
	public boolean isExported() {
		return exported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#setExported(boolean)
	 */
	public void setExported(boolean value) {
		exported = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#getDeclaration()
	 */
	public IASTDeclaration getDeclaration() {
		return declaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#setDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration)
	 */
	public void setDeclaration(IASTDeclaration declaration) {
		this.declaration = declaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#getTemplateParameters()
	 */
	public ICPPASTTemplateParameter[] getTemplateParameters() {
		if (parameters == null)
			return ICPPASTTemplateParameter.EMPTY_TEMPLATEPARAMETER_ARRAY;
		parameters = (ICPPASTTemplateParameter[]) ArrayUtil.removeNullsAfter(
				ICPPASTTemplateParameter.class, parameters, parametersPos);
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#addTemplateParamter(edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateParameter)
	 */
	public void addTemplateParamter(ICPPASTTemplateParameter parm) {
		if (parm != null) {
			parametersPos++;
			parameters = (ICPPASTTemplateParameter[]) ArrayUtil.append(
					ICPPASTTemplateParameter.class, parameters, parm);
		}
	}

	private ICPPASTTemplateParameter[] parameters = null;

	private int parametersPos = -1;

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitDeclarations) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}

		ICPPASTTemplateParameter[] params = getTemplateParameters();
		for (int i = 0; i < params.length; i++) {
			if (!params[i].accept(action))
				return false;
		}

		if (declaration != null)
			if (!declaration.accept(action))
				return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#getScope()
	 */
	public ICPPTemplateScope getScope() {
		if (templateScope == null)
			templateScope = new CPPTemplateScope(this);
		return templateScope;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (declaration == child) {
			other.setParent(child.getParent());
			other.setPropertyInParent(child.getPropertyInParent());
			declaration = (IASTDeclaration) other;
		}
	}
}
