/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICFunctionScope;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CASTFunctionDefinition extends CASTNode implements
		IASTFunctionDefinition, IASTAmbiguityParent {

	private IASTDeclSpecifier declSpecifier;

	private IASTFunctionDeclarator declarator;

	private IASTStatement bodyStatement;

	private ICFunctionScope scope;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#getDeclSpecifier()
	 */
	public IASTDeclSpecifier getDeclSpecifier() {
		return declSpecifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#setDeclSpecifier(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier)
	 */
	public void setDeclSpecifier(IASTDeclSpecifier declSpec) {
		declSpecifier = declSpec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#getDeclarator()
	 */
	public IASTFunctionDeclarator getDeclarator() {
		return declarator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#setDeclarator(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDeclarator)
	 */
	public void setDeclarator(IASTFunctionDeclarator declarator) {
		this.declarator = declarator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#getBody()
	 */
	public IASTStatement getBody() {
		return bodyStatement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#setBody(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement)
	 */
	public void setBody(IASTStatement statement) {
		bodyStatement = statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition#getScope()
	 */
	public IScope getScope() {
		if (scope == null)
			scope = new CFunctionScope(this);
		return scope;
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

		if (declSpecifier != null)
			if (!declSpecifier.accept(action))
				return false;
		if (declarator != null)
			if (!declarator.accept(action))
				return false;
		if (bodyStatement != null)
			if (!bodyStatement.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (bodyStatement == child) {
			other.setPropertyInParent(bodyStatement.getPropertyInParent());
			other.setParent(bodyStatement.getParent());
			bodyStatement = (IASTStatement) other;
		}
	}

}
