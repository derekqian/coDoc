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
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNameOwner;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateId;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CPPASTTemplateId extends CPPASTNode implements ICPPASTTemplateId,
		IASTAmbiguityParent {
	private IASTName templateName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateId#getTemplateName()
	 */
	public IASTName getTemplateName() {
		return templateName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateId#setTemplateName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public void setTemplateName(IASTName name) {
		templateName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateId#addTemplateArgument(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId)
	 */
	public void addTemplateArgument(IASTTypeId typeId) {
		templateArguments = (IASTNode[]) ArrayUtil.append(IASTNode.class,
				templateArguments, typeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateId#addTemplateArgument(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void addTemplateArgument(IASTExpression expression) {
		templateArguments = (IASTNode[]) ArrayUtil.append(IASTNode.class,
				templateArguments, expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTemplateId#getTemplateArguments()
	 */
	public IASTNode[] getTemplateArguments() {
		if (templateArguments == null)
			return ICPPASTTemplateId.EMPTY_ARG_ARRAY;
		return (IASTNode[]) ArrayUtil.removeNulls(IASTNode.class,
				templateArguments);
	}

	private IASTNode[] templateArguments = null;

	private IBinding binding = null;

	private boolean resolving = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName#resolveBinding()
	 */
	public IBinding resolveBinding() {
		if (binding == null && !resolving) {
			// protect for infinite recursion
			resolving = true;
			binding = CPPTemplates.createBinding(this);
			resolving = false;
		}

		return binding;
	}

	public IBinding[] resolvePrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName#toCharArray()
	 */
	public char[] toCharArray() {
		return templateName.toCharArray();
	}

	public String toString() {
		return templateName.toString();
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitNames) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
		if (templateName != null)
			if (!templateName.accept(action))
				return false;

		IASTNode[] nodes = getTemplateArguments();
		for (int i = 0; i < nodes.length; i++) {
			if (!nodes[i].accept(action))
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName#isDeclaration()
	 */
	public boolean isDeclaration() {
		return false; // for now this seems to be true
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName#isReference()
	 */
	public boolean isReference() {
		return true; // for now this seems to be true
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNameOwner#getRoleForName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public int getRoleForName(IASTName n) {
		if (n == templateName)
			return r_reference;
		return r_unclear;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName#getBinding()
	 */
	public IBinding getBinding() {
		return binding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName#setBinding(edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding)
	 */
	public void setBinding(IBinding binding) {
		this.binding = binding;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (templateArguments == null)
			return;
		for (int i = 0; i < templateArguments.length; ++i) {
			if (child == templateArguments[i]) {
				other.setPropertyInParent(child.getPropertyInParent());
				other.setParent(child.getParent());
				templateArguments[i] = other;
			}
		}
	}

	public boolean isDefinition() {
		IASTNode parent = getParent();
		if (parent instanceof IASTNameOwner) {
			int role = ((IASTNameOwner) parent).getRoleForName(this);
			if (role == IASTNameOwner.r_definition)
				return true;
			return false;
		}
		return false;
	}

}
