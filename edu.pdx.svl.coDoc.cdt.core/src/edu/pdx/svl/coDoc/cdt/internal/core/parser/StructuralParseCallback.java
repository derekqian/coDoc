/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.parser;

import java.util.LinkedList;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractTypeSpecifierDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTField;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMacro;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDirective;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.QuickParseCallback;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTLinkageSpecification;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTScope;

/**
 * @author hamer
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class StructuralParseCallback extends QuickParseCallback {

	protected LinkedList scopeStack = new LinkedList();

	protected IASTScope currentScope = null;

	protected int inclusionLevel = 0;

	private void addElement(IASTDeclaration element) {
		if (inclusionLevel == 0) {
			if (currentScope instanceof ASTScope)
				((ASTScope) currentScope).addDeclaration(element);
			else if (currentScope instanceof ASTLinkageSpecification)
				((ASTLinkageSpecification) currentScope)
						.addDeclaration(element);
		}
	}

	private void enterScope(IASTNode node) {
		if (node instanceof IASTScope) {
			if (node instanceof ASTScope) {
				((ASTScope) node).initDeclarations();
			}
			pushScope((IASTScope) node);
		}
	}

	private void exitScope(IASTNode node) {
		if (node instanceof IASTScope) {
			popScope();
		}
	}

	private void pushScope(IASTScope scope) {
		scopeStack.addFirst(currentScope);
		currentScope = scope;
	}

	private IASTScope popScope() {
		IASTScope oldScope = currentScope;
		currentScope = (scopeStack.size() > 0) ? (IASTScope) scopeStack
				.removeFirst() : null;
		return oldScope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptMacro(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMacro)
	 */
	public void acceptMacro(IASTMacro macro) {
		if (inclusionLevel == 0)
			macros.add(macro);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptVariable(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable)
	 */
	public void acceptVariable(IASTVariable variable) {
		addElement(variable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptFunctionDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction)
	 */
	public void acceptFunctionDeclaration(IASTFunction function) {
		if (function.getOwnerTemplateDeclaration() == null)
			addElement(function);
		else if (function.getOwnerTemplateDeclaration() instanceof IASTTemplateDeclaration)
			addElement((IASTTemplateDeclaration) function
					.getOwnerTemplateDeclaration());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptTypedefDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration)
	 */
	public void acceptTypedefDeclaration(IASTTypedefDeclaration typedef) {
		addElement(typedef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptUsingDirective(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDirective)
	 */
	public void acceptUsingDirective(IASTUsingDirective usageDirective) {
		addElement(usageDirective);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptEnumerationSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationSpecifier)
	 */
	public void acceptEnumerationSpecifier(IASTEnumerationSpecifier enumeration) {
		enterScope(enumeration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptElaboratedForewardDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier)
	 */
	public void acceptElaboratedForewardDeclaration(
			IASTElaboratedTypeSpecifier elaboratedType) {
		enterScope(elaboratedType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptAbstractTypeSpecDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractTypeSpecifierDeclaration)
	 */
	public void acceptAbstractTypeSpecDeclaration(
			IASTAbstractTypeSpecifierDeclaration abstractDeclaration) {
		if (abstractDeclaration.getOwnerTemplateDeclaration() == null)
			addElement(abstractDeclaration);
		else if (abstractDeclaration.getOwnerTemplateDeclaration() instanceof IASTTemplateDeclaration)
			addElement((IASTTemplateDeclaration) abstractDeclaration
					.getOwnerTemplateDeclaration());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterInclusion(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion)
	 */
	public void enterInclusion(IASTInclusion inclusion) {
		if (inclusionLevel == 0)
			inclusions.add(inclusion);
		inclusionLevel++;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterNamespaceDefinition(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition)
	 */
	public void enterNamespaceDefinition(
			IASTNamespaceDefinition namespaceDefinition) {
		addElement(namespaceDefinition);
		enterScope(namespaceDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterClassSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassSpecifier)
	 */
	public void enterClassSpecifier(IASTClassSpecifier classSpecification) {
		enterScope(classSpecification);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterLinkageSpecification(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification)
	 */
	public void enterLinkageSpecification(IASTLinkageSpecification linkageSpec) {
		addElement(linkageSpec);
		enterScope(linkageSpec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterCompilationUnit(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit)
	 */
	public void enterCompilationUnit(IASTCompilationUnit compilationUnit) {
		enterScope(compilationUnit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptMethodDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod)
	 */
	public void acceptMethodDeclaration(IASTMethod method) {
		if (method.getOwnerTemplateDeclaration() == null)
			addElement(method);
		else if (method.getOwnerTemplateDeclaration() instanceof IASTTemplateDeclaration)
			addElement((IASTTemplateDeclaration) method
					.getOwnerTemplateDeclaration());
	} /*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptField(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTField)
		 */

	public void acceptField(IASTField field) {
		addElement(field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitCompilationUnit(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit)
	 */
	public void exitCompilationUnit(IASTCompilationUnit compilationUnit) {
		exitScope(compilationUnit);
		this.compUnit = compilationUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitInclusion(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion)
	 */
	public void exitInclusion(IASTInclusion inclusion) {
		inclusionLevel--;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.IQuickParseCallback#getCompilationUnit()
	 */
	public IASTCompilationUnit getCompilationUnit() {
		return compUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitClassSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassSpecifier)
	 */
	public void exitClassSpecifier(IASTClassSpecifier classSpecification) {
		exitScope(classSpecification);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitLinkageSpecification(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification)
	 */
	public void exitLinkageSpecification(IASTLinkageSpecification linkageSpec) {
		exitScope(linkageSpec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitNamespaceDefinition(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition)
	 */
	public void exitNamespaceDefinition(
			IASTNamespaceDefinition namespaceDefinition) {
		exitScope(namespaceDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterFunctionBody(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction)
	 */
	public void enterFunctionBody(IASTFunction function) {
		if (function.getOwnerTemplateDeclaration() == null)
			addElement(function);
		else if (function.getOwnerTemplateDeclaration() instanceof IASTTemplateDeclaration)
			addElement((IASTTemplateDeclaration) function
					.getOwnerTemplateDeclaration());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterMethodBody(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod)
	 */
	public void enterMethodBody(IASTMethod method) {
		if (method.getOwnerTemplateDeclaration() == null)
			addElement(method);
		else if (method.getOwnerTemplateDeclaration() instanceof IASTTemplateDeclaration)
			addElement((IASTTemplateDeclaration) method
					.getOwnerTemplateDeclaration());
	}

}
