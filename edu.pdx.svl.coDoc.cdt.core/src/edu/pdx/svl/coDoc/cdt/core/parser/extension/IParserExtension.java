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
package edu.pdx.svl.coDoc.cdt.core.parser.extension;

import edu.pdx.svl.coDoc.cdt.core.parser.IToken;
import edu.pdx.svl.coDoc.cdt.core.parser.KeywordSetKey;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserLanguage;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTPointerOperator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode.CompletionKind;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression.Kind;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.DeclarationWrapper;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.IParserData;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.Parser;

/**
 * @author jcamelon
 */
public interface IParserExtension {

	public boolean isValidCVModifier(ParserLanguage language, int tokenType);

	public ASTPointerOperator getPointerOperator(ParserLanguage language,
			int tokenType);

	public boolean isValidUnaryExpressionStart(int tokenType);

	public IASTExpression parseUnaryExpression(IASTScope scope,
			IParserData data, CompletionKind kind, KeywordSetKey key);

	public boolean isValidRelationalExpressionStart(ParserLanguage language,
			int tokenType);

	public IASTExpression parseRelationalExpression(IASTScope scope,
			IParserData data, CompletionKind kind, KeywordSetKey key,
			IASTExpression lhsExpression);

	/**
	 * @param i
	 * @return
	 */
	public boolean canHandleDeclSpecifierSequence(int tokenType);

	public interface IDeclSpecifierExtensionResult {
		public IToken getFirstToken();

		public IToken getLastToken();

		public Parser.Flags getFlags();
	}

	/**
	 * @param parser
	 * @param flags
	 * @param sdw
	 * @param key
	 *            TODO
	 * @return TODO
	 */
	public IDeclSpecifierExtensionResult parseDeclSpecifierSequence(
			IParserData parser, Parser.Flags flags, DeclarationWrapper sdw,
			CompletionKind kind, KeywordSetKey key);

	/**
	 * @param i
	 * @return
	 */
	public boolean canHandleCDesignatorInitializer(int tokenType);

	/**
	 * @param parserData
	 * @param scope
	 *            TODO
	 * @return
	 */
	public IASTDesignator parseDesignator(IParserData parserData,
			IASTScope scope);

	/**
	 * @return
	 */
	public boolean supportsStatementsInExpressions();

	/**
	 * @return
	 */
	public Kind getExpressionKindForStatement();

	public boolean supportsExtendedTemplateInstantiationSyntax();

	public boolean isValidModifierForInstantiation(IToken la);
}
