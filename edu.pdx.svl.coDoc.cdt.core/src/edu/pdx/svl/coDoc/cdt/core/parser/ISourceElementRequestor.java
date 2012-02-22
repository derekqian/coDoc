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

/**
 * @author jcamelon
 * 
 */
public interface ISourceElementRequestor {

	public boolean acceptProblem(IProblem problem);

	public void acceptMacro(IASTMacro macro);

	public void acceptVariable(IASTVariable variable);

	public void acceptFunctionDeclaration(IASTFunction function);

	public void acceptUsingDirective(IASTUsingDirective usageDirective);

	public void acceptUsingDeclaration(IASTUsingDeclaration usageDeclaration);

	public void acceptASMDefinition(IASTASMDefinition asmDefinition);

	public void acceptTypedefDeclaration(IASTTypedefDeclaration typedef);

	public void acceptEnumerationSpecifier(IASTEnumerationSpecifier enumeration);

	public void acceptElaboratedForewardDeclaration(
			IASTElaboratedTypeSpecifier elaboratedType);

	public void acceptAbstractTypeSpecDeclaration(
			IASTAbstractTypeSpecifierDeclaration abstractDeclaration);

	public void enterFunctionBody(IASTFunction function);

	public void exitFunctionBody(IASTFunction function);

	public void enterCodeBlock(IASTCodeScope scope);

	public void exitCodeBlock(IASTCodeScope scope);

	public void enterCompilationUnit(IASTCompilationUnit compilationUnit);

	public void enterInclusion(IASTInclusion inclusion);

	public void enterNamespaceDefinition(
			IASTNamespaceDefinition namespaceDefinition);

	public void enterClassSpecifier(IASTClassSpecifier classSpecification);

	public void enterLinkageSpecification(IASTLinkageSpecification linkageSpec);

	public void enterTemplateDeclaration(IASTTemplateDeclaration declaration);

	public void enterTemplateSpecialization(
			IASTTemplateSpecialization specialization);

	public void enterTemplateInstantiation(
			IASTTemplateInstantiation instantiation);

	public void acceptMethodDeclaration(IASTMethod method);

	public void enterMethodBody(IASTMethod method);

	public void exitMethodBody(IASTMethod method);

	public void acceptField(IASTField field);

	public void acceptClassReference(IASTClassReference reference);

	public void acceptTypedefReference(IASTTypedefReference reference);

	public void acceptNamespaceReference(IASTNamespaceReference reference);

	public void acceptEnumerationReference(IASTEnumerationReference reference);

	public void acceptVariableReference(IASTVariableReference reference);

	public void acceptFunctionReference(IASTFunctionReference reference);

	public void acceptFieldReference(IASTFieldReference reference);

	public void acceptMethodReference(IASTMethodReference reference);

	public void acceptEnumeratorReference(IASTEnumeratorReference reference);

	public void acceptParameterReference(IASTParameterReference reference);

	public void acceptTemplateParameterReference(
			IASTTemplateParameterReference reference);

	public void acceptFriendDeclaration(IASTDeclaration declaration);

	public void exitTemplateDeclaration(IASTTemplateDeclaration declaration);

	public void exitTemplateSpecialization(
			IASTTemplateSpecialization specialization);

	public void exitTemplateExplicitInstantiation(
			IASTTemplateInstantiation instantiation);

	public void exitLinkageSpecification(IASTLinkageSpecification linkageSpec);

	public void exitClassSpecifier(IASTClassSpecifier classSpecification);

	public void exitNamespaceDefinition(
			IASTNamespaceDefinition namespaceDefinition);

	public void exitInclusion(IASTInclusion inclusion);

	public void exitCompilationUnit(IASTCompilationUnit compilationUnit);

	/**
	 * @param finalPath
	 * @return
	 */
	public CodeReader createReader(String finalPath, Iterator workingCopies);

}
