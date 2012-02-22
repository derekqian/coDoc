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

import java.util.List;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.parser.IParserLogService;
import edu.pdx.svl.coDoc.cdt.core.parser.IToken;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTPointerOperator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTAbstractDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTArrayModifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTInclusion;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTMacro;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTypeSpecifier;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator.DesignatorKind;
import edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension;

/**
 * @author jcamelon
 * 
 */
public class BaseASTFactory {

	public BaseASTFactory(IASTFactoryExtension extension) {
		this.extension = extension;
	}

	protected IParserLogService logService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.IASTFactory#createMacro(java.lang.String,
	 *      int, int, int)
	 */
	public IASTMacro createMacro(char[] name, int startingOffset,
			int startingLine, int nameOffset, int nameEndOffset, int nameLine,
			int endingOffset, int endingLine, char[] fn, boolean isImplicit) {
		IASTMacro m = new ASTMacro(name, startingOffset, startingLine,
				nameOffset, nameEndOffset, nameLine, endingOffset, endingLine,
				fn, isImplicit);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.IASTFactory#createInclusion(java.lang.String,
	 *      java.lang.String, boolean)
	 */
	public IASTInclusion createInclusion(char[] name, char[] fileName,
			boolean local, int startingOffset, int startingLine,
			int nameOffset, int nameEndOffset, int nameLine, int endingOffset,
			int endingLine, char[] fn, boolean isImplicit) {
		IASTInclusion inclusion = new ASTInclusion(name, fileName, local,
				startingOffset, startingLine, nameOffset, nameEndOffset,
				nameLine, endingOffset, endingLine, fn, isImplicit);
		return inclusion;
	}

	public IASTAbstractDeclaration createAbstractDeclaration(boolean isConst,
			boolean isVolatile, IASTTypeSpecifier typeSpecifier,
			List pointerOperators, List arrayModifiers, List parameters,
			ASTPointerOperator pointerOperator) {
		return new ASTAbstractDeclaration(isConst, isVolatile, typeSpecifier,
				pointerOperators, arrayModifiers, parameters, pointerOperator);
	}

	public IASTArrayModifier createArrayModifier(IASTExpression exp) {
		return new ASTArrayModifier(exp);
	}

	public IASTDesignator createDesignator(DesignatorKind kind,
			IASTExpression constantExpression, IToken fieldIdentifier,
			Map extensionParms) {
		if (extension.overrideCreateDesignatorMethod(kind))
			return extension.createDesignator(kind, constantExpression,
					fieldIdentifier, extensionParms);
		return new ASTDesignator(kind, constantExpression,
				fieldIdentifier == null ? new char[0] : fieldIdentifier
						.getCharImage(), //$NON-NLS-1$
				fieldIdentifier == null ? -1 : fieldIdentifier.getOffset());
	}

	public void setLogger(IParserLogService log) {
		logService = log;
	}

	protected final IASTFactoryExtension extension;

	protected static final char[] EMPTY_STRING = new char[0];

}
