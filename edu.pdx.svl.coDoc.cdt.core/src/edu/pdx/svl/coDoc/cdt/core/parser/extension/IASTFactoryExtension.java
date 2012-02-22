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

package edu.pdx.svl.coDoc.cdt.core.parser.extension;

import java.util.List;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.parser.IToken;
import edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFactory;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.IASTNewExpressionDescriptor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.Kind;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier.Type;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ITypeInfo;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ParserSymbolTable;

/**
 * @author jcamelon
 * 
 */
public interface IASTFactoryExtension {

	public boolean overrideCreateExpressionMethod();

	public IASTExpression createExpression(IASTScope scope,
			IASTExpression.Kind kind, IASTExpression lhs, IASTExpression rhs,
			IASTExpression thirdExpression, IASTTypeId typeId,
			ITokenDuple idExpression, char[] literal,
			IASTNewExpressionDescriptor newDescriptor, List references);

	public boolean canHandleExpressionKind(IASTExpression.Kind kind);

	/**
	 * @param kind
	 * @param lhs
	 * @param rhs
	 * @param typeId
	 * @return TODO
	 */
	public ITypeInfo getExpressionResultType(Kind kind, IASTExpression lhs,
			IASTExpression rhs, IASTTypeId typeId);

	public boolean overrideCreateSimpleTypeSpecifierMethod(Type type);

	public IASTSimpleTypeSpecifier createSimpleTypeSpecifier(
			ParserSymbolTable pst, IASTScope scope,
			IASTSimpleTypeSpecifier.Type kind, ITokenDuple typeName,
			boolean isShort, boolean isLong, boolean isSigned,
			boolean isUnsigned, boolean isTypename, boolean isComplex,
			boolean isImaginary, boolean isGlobal, Map extensionParms);

	public boolean overrideCreateDesignatorMethod(
			IASTDesignator.DesignatorKind kind);

	public IASTDesignator createDesignator(IASTDesignator.DesignatorKind kind,
			IASTExpression constantExpression, IToken fieldIdentifier,
			Map extensionParms);

	public void initialize(IASTFactory factory,
			IASTCompilationUnit compilationUnit);

}
