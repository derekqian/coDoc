/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.core.parser;

import java.util.Iterator;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTASMDefinition;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractTypeSpecifierDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCodeScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumeratorReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTField;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFieldReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunctionReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMacro;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethodReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTParameterReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateInstantiation;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateParameterReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateSpecialization;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefReference;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDirective;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariableReference;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.InternalParserUtil;

public class NullSourceElementRequestor implements ISourceElementRequestor {
	private ParserMode mode = ParserMode.COMPLETE_PARSE;

	public NullSourceElementRequestor() {
	}

	public NullSourceElementRequestor(ParserMode mode) {
		this.mode = mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptProblem(edu.pdx.svl.coDoc.cdt.core.parser.IProblem)
	 */
	public boolean acceptProblem(IProblem problem) {
		return DefaultProblemHandler.ruleOnProblem(problem, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptMacro(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMacro)
	 */
	public void acceptMacro(IASTMacro macro) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptVariable(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable)
	 */
	public void acceptVariable(IASTVariable variable) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptFunctionDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction)
	 */
	public void acceptFunctionDeclaration(IASTFunction function) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptUsingDirective(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDirective)
	 */
	public void acceptUsingDirective(IASTUsingDirective usageDirective) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptUsingDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDeclaration)
	 */
	public void acceptUsingDeclaration(IASTUsingDeclaration usageDeclaration) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptASMDefinition(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTASMDefinition)
	 */
	public void acceptASMDefinition(IASTASMDefinition asmDefinition) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptTypedef(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedef)
	 */
	public void acceptTypedefDeclaration(IASTTypedefDeclaration typedef) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptEnumerationSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationSpecifier)
	 */
	public void acceptEnumerationSpecifier(IASTEnumerationSpecifier enumeration) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterFunctionBody(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction)
	 */
	public void enterFunctionBody(IASTFunction function) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitFunctionBody(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction)
	 */
	public void exitFunctionBody(IASTFunction function) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterCompilationUnit(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit)
	 */
	public void enterCompilationUnit(IASTCompilationUnit compilationUnit) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterInclusion(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion)
	 */
	public void enterInclusion(IASTInclusion inclusion) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterNamespaceDefinition(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition)
	 */
	public void enterNamespaceDefinition(
			IASTNamespaceDefinition namespaceDefinition) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterClassSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassSpecifier)
	 */
	public void enterClassSpecifier(IASTClassSpecifier classSpecification) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterLinkageSpecification(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification)
	 */
	public void enterLinkageSpecification(IASTLinkageSpecification linkageSpec) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterTemplateDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateDeclaration)
	 */
	public void enterTemplateDeclaration(IASTTemplateDeclaration declaration) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterTemplateSpecialization(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateSpecialization)
	 */
	public void enterTemplateSpecialization(
			IASTTemplateSpecialization specialization) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterTemplateExplicitInstantiation(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateInstantiation)
	 */
	public void enterTemplateInstantiation(
			IASTTemplateInstantiation instantiation) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptMethodDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod)
	 */
	public void acceptMethodDeclaration(IASTMethod method) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterMethodBody(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod)
	 */
	public void enterMethodBody(IASTMethod method) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitMethodBody(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethod)
	 */
	public void exitMethodBody(IASTMethod method) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptField(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTField)
	 */
	public void acceptField(IASTField field) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptClassReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassReference)
	 */
	public void acceptClassReference(IASTClassReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitTemplateDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateDeclaration)
	 */
	public void exitTemplateDeclaration(IASTTemplateDeclaration declaration) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitTemplateSpecialization(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateSpecialization)
	 */
	public void exitTemplateSpecialization(
			IASTTemplateSpecialization specialization) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitTemplateExplicitInstantiation(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateInstantiation)
	 */
	public void exitTemplateExplicitInstantiation(
			IASTTemplateInstantiation instantiation) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitLinkageSpecification(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification)
	 */
	public void exitLinkageSpecification(IASTLinkageSpecification linkageSpec) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitClassSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTClassSpecifier)
	 */
	public void exitClassSpecifier(IASTClassSpecifier classSpecification) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitNamespaceDefinition(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition)
	 */
	public void exitNamespaceDefinition(
			IASTNamespaceDefinition namespaceDefinition) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitInclusion(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion)
	 */
	public void exitInclusion(IASTInclusion inclusion) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitCompilationUnit(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit)
	 */
	public void exitCompilationUnit(IASTCompilationUnit compilationUnit) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptAbstractTypeSpecDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractTypeSpecifierDeclaration)
	 */
	public void acceptAbstractTypeSpecDeclaration(
			IASTAbstractTypeSpecifierDeclaration abstractDeclaration) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptTypedefReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypedefReference)
	 */
	public void acceptTypedefReference(IASTTypedefReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptNamespaceReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceReference)
	 */
	public void acceptNamespaceReference(IASTNamespaceReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptEnumerationReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationReference)
	 */
	public void acceptEnumerationReference(IASTEnumerationReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptVariableReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariableReference)
	 */
	public void acceptVariableReference(IASTVariableReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptFunctionReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunctionReference)
	 */
	public void acceptFunctionReference(IASTFunctionReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptFieldReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFieldReference)
	 */
	public void acceptFieldReference(IASTFieldReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptMethodReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMethodReference)
	 */
	public void acceptMethodReference(IASTMethodReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptElaboratedForewardDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTElaboratedTypeSpecifier)
	 */
	public void acceptElaboratedForewardDeclaration(
			IASTElaboratedTypeSpecifier elaboratedType) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#enterCodeBlock(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope)
	 */
	public void enterCodeBlock(IASTCodeScope scope) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#exitCodeBlock(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope)
	 */
	public void exitCodeBlock(IASTCodeScope scope) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptEnumeratorReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerationReference)
	 */
	public void acceptEnumeratorReference(IASTEnumeratorReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptParameterReference(edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTParameterReference)
	 */
	public void acceptParameterReference(IASTParameterReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#createReader(java.lang.String)
	 */
	public CodeReader createReader(String finalPath, Iterator workingCopies) {
		return InternalParserUtil.createFileReader(finalPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptTemplateParameterReference(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateParameterReference)
	 */
	public void acceptTemplateParameterReference(
			IASTTemplateParameterReference reference) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor#acceptFriendDeclaration(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration)
	 */
	public void acceptFriendDeclaration(IASTDeclaration declaration) {
		// TODO Auto-generated method stub

	}
}
