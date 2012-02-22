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

package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast;

import java.util.Collections;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.parser.IToken;
import edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserMode;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator.DesignatorKind;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.Kind;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier.Type;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.gcc.IASTGCCDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.gcc.IASTGCCExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.gcc.IASTGCCSimpleTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTExpression;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTTypeId;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.gcc.ASTGCCSimpleTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.gcc.GCCASTCompleteExtension;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.gcc.ASTGCCDesignator;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.quick.GCCASTExpressionExtension;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbol;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ITypeInfo;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ParserSymbolTable;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.TypeInfoProvider;

/**
 * @author jcamelon
 * 
 */
public abstract class GCCASTExtension implements IASTFactoryExtension {
	protected final ParserMode mode;

	protected static final char[] EMPTY_STRING = "".toCharArray(); //$NON-NLS-1$

	/**
	 * @param mode
	 */
	public GCCASTExtension(ParserMode mode) {
		this.mode = mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension#canHandleExpressionKind(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.Kind)
	 */
	public boolean canHandleExpressionKind(Kind kind) {
		if (kind == IASTGCCExpression.Kind.UNARY_ALIGNOF_TYPEID
				|| kind == IASTGCCExpression.Kind.UNARY_ALIGNOF_UNARYEXPRESSION
				|| kind == IASTGCCExpression.Kind.UNARY_TYPEOF_UNARYEXPRESSION
				|| kind == IASTGCCExpression.Kind.UNARY_TYPEOF_TYPEID
				|| kind == IASTGCCExpression.Kind.RELATIONAL_MAX
				|| kind == IASTGCCExpression.Kind.RELATIONAL_MIN
				|| kind == IASTGCCExpression.Kind.STATEMENT_EXPRESSION)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension#getExpressionResultType(edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.TypeInfo,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.Kind,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeId,
	 *      edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbol)
	 */
	public ITypeInfo getExpressionResultType(Kind kind, IASTExpression lhs,
			IASTExpression rhs, IASTTypeId typeId) {
		ITypeInfo info = null;
		if (kind == IASTGCCExpression.Kind.UNARY_ALIGNOF_TYPEID
				|| kind == IASTGCCExpression.Kind.UNARY_ALIGNOF_UNARYEXPRESSION) {
			info = TypeInfoProvider.newTypeInfo(ITypeInfo.t_int);
			info.setBit(true, ITypeInfo.isUnsigned);
		} else if (kind == IASTGCCExpression.Kind.RELATIONAL_MAX
				|| kind == IASTGCCExpression.Kind.RELATIONAL_MIN) {
			if (lhs instanceof ASTExpression)
				info = TypeInfoProvider.newTypeInfo(((ASTExpression) lhs)
						.getResultType().getResult());
		} else if (kind == IASTGCCExpression.Kind.UNARY_TYPEOF_TYPEID) {
			if (typeId instanceof ASTTypeId)
				info = TypeInfoProvider.newTypeInfo(((ASTTypeId) typeId)
						.getTypeSymbol().getTypeInfo());
		} else if (kind == IASTGCCExpression.Kind.UNARY_TYPEOF_UNARYEXPRESSION) {
			if (lhs instanceof ASTExpression) {
				if (((ASTExpression) lhs).getResultType() != null)
					info = TypeInfoProvider.newTypeInfo(((ASTExpression) lhs)
							.getResultType().getResult());
				else {
					info = TypeInfoProvider.newTypeInfo(ITypeInfo.t_void);
				}
			}
		}

		if (info != null)
			return info;
		return TypeInfoProvider.newTypeInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension#overrideCreateSimpleTypeSpecifierMethod()
	 */
	public boolean overrideCreateSimpleTypeSpecifierMethod(Type type) {
		if (type == IASTGCCSimpleTypeSpecifier.Type.TYPEOF)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension#createSimpleTypeSpecifier(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier.Type,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ITokenDuple, boolean, boolean, boolean,
	 *      boolean, boolean, boolean, boolean, boolean)
	 */
	public IASTSimpleTypeSpecifier createSimpleTypeSpecifier(
			ParserSymbolTable pst, IASTScope scope, Type kind,
			ITokenDuple typeName, boolean isShort, boolean isLong,
			boolean isSigned, boolean isUnsigned, boolean isTypename,
			boolean isComplex, boolean isImaginary, boolean isGlobal,
			Map extensionParms) {
		if (kind == IASTGCCSimpleTypeSpecifier.Type.TYPEOF) {
			ASTExpression typeOfExpression = (ASTExpression) extensionParms
					.get(IASTGCCSimpleTypeSpecifier.TYPEOF_EXRESSION);
			ISymbol s = pst.newSymbol(EMPTY_STRING);
			s.setTypeInfo(typeOfExpression.getResultType().getResult());
			return new ASTGCCSimpleTypeSpecifier(s, isTypename,
					(typeName == null ? EMPTY_STRING : typeName.toCharArray()),
					Collections.EMPTY_LIST, typeOfExpression);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension#overrideCreateDesignatorMethod(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator.DesignatorKind)
	 */
	public boolean overrideCreateDesignatorMethod(DesignatorKind kind) {
		if (kind == IASTGCCDesignator.DesignatorKind.SUBSCRIPT_RANGE)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension#createDesignator(edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator.DesignatorKind,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression,
	 *      edu.pdx.svl.coDoc.cdt.core.parser.IToken, java.util.Map)
	 */
	public IASTDesignator createDesignator(DesignatorKind kind,
			IASTExpression constantExpression, IToken fieldIdentifier,
			Map extensionParms) {
		IASTExpression secondExpression = (IASTExpression) extensionParms
				.get(IASTGCCDesignator.SECOND_EXRESSION);
		return new ASTGCCDesignator(kind, constantExpression, EMPTY_STRING, -1,
				secondExpression);
	}

	/**
	 * @param mode2
	 * @return
	 */
	public static IASTFactoryExtension createExtension(ParserMode parseMode) {
		if (parseMode == ParserMode.QUICK_PARSE)
			return new GCCASTExpressionExtension(parseMode);

		return new GCCASTCompleteExtension(parseMode);
	}
}
