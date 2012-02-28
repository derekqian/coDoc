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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateSpecialization;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateScope;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTTemplateSpecialization extends CPPASTNode implements
		ICPPASTTemplateSpecialization, ICPPASTTemplateDeclaration,
		IASTAmbiguityParent {

	private IASTDeclaration declaration;

	private ICPPTemplateScope templateScope;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateSpecialization#getDeclaration()
	 */
	public IASTDeclaration getDeclaration() {
		return declaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateSpecialization#setDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration)
	 */
	public void setDeclaration(IASTDeclaration declaration) {
		this.declaration = declaration;
	}

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

		if (declaration != null)
			if (!declaration.accept(action))
				return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#isExported()
	 */
	public boolean isExported() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#setExported(boolean)
	 */
	public void setExported(boolean value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#getTemplateParameters()
	 */
	public ICPPASTTemplateParameter[] getTemplateParameters() {
		return ICPPASTTemplateParameter.EMPTY_TEMPLATEPARAMETER_ARRAY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration#addTemplateParamter(edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateParameter)
	 */
	public void addTemplateParamter(ICPPASTTemplateParameter parm) {
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
