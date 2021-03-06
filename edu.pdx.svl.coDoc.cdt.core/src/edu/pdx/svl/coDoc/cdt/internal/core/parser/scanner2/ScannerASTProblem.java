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
package edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFileLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ParserMessages;

/**
 * @author jcamelon
 */
public class ScannerASTProblem extends ASTNode implements IASTProblem {

	private final char[] arg;

	private final int id;

	private final boolean isError;

	private final boolean isWarning;

	private String message = null;

	public ScannerASTProblem(int id, char[] arg, boolean warn, boolean error) {
		this.id = id;
		this.arg = arg;
		this.isWarning = warn;
		this.isError = error;
	}

	public int getID() {
		return id;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isWarning() {
		return isWarning;
	}

	protected static final Map errorMessages;
	static {
		errorMessages = new HashMap();
		errorMessages
				.put(
						new Integer(IASTProblem.PREPROCESSOR_POUND_ERROR),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.error")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.PREPROCESSOR_INCLUSION_NOT_FOUND),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.inclusionNotFound")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.PREPROCESSOR_DEFINITION_NOT_FOUND),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.definitionNotFound")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.PREPROCESSOR_INVALID_MACRO_DEFN),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.invalidMacroDefn")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.PREPROCESSOR_INVALID_MACRO_REDEFN),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.invalidMacroRedefn")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.PREPROCESSOR_UNBALANCE_CONDITION),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.unbalancedConditional")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.PREPROCESSOR_CONDITIONAL_EVAL_ERROR),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.conditionalEval")); //$NON-NLS-1$
		errorMessages.put(new Integer(
				IASTProblem.PREPROCESSOR_MACRO_USAGE_ERROR), ParserMessages
				.getString("ScannerProblemFactory.error.preproc.macroUsage")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.PREPROCESSOR_CIRCULAR_INCLUSION),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.circularInclusion")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.PREPROCESSOR_INVALID_DIRECTIVE),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.invalidDirective")); //$NON-NLS-1$
		errorMessages.put(new Integer(
				IASTProblem.PREPROCESSOR_MACRO_PASTING_ERROR), ParserMessages
				.getString("ScannerProblemFactory.error.preproc.macroPasting")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.PREPROCESSOR_MISSING_RPAREN_PARMLIST),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.missingRParen")); //$NON-NLS-1$       
		errorMessages
				.put(
						new Integer(IASTProblem.PREPROCESSOR_INVALID_VA_ARGS),
						ParserMessages
								.getString("ScannerProblemFactory.error.preproc.invalidVaArgs")); //$NON-NLS-1$       
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_INVALID_ESCAPECHAR),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.invalidEscapeChar")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_UNBOUNDED_STRING),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.unboundedString")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_BAD_FLOATING_POINT),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.badFloatingPoint")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_BAD_HEX_FORMAT),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.badHexFormat")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_BAD_OCTAL_FORMAT),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.badOctalFormat")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_BAD_DECIMAL_FORMAT),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.badDecimalFormat")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_ASSIGNMENT_NOT_ALLOWED),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.assignmentNotAllowed")); //$NON-NLS-1$        
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_DIVIDE_BY_ZERO),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.divideByZero")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_MISSING_R_PAREN),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.missingRParen")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_EXPRESSION_SYNTAX_ERROR),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.expressionSyntaxError")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_ILLEGAL_IDENTIFIER),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.illegalIdentifier")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(
								IASTProblem.SCANNER_BAD_CONDITIONAL_EXPRESSION),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.badConditionalExpression")); //$NON-NLS-1$        
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_UNEXPECTED_EOF),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.unexpectedEOF")); //$NON-NLS-1$
		errorMessages
				.put(
						new Integer(IASTProblem.SCANNER_BAD_CHARACTER),
						ParserMessages
								.getString("ScannerProblemFactory.error.scanner.badCharacter")); //$NON-NLS-1$
		errorMessages.put(new Integer(IASTProblem.SYNTAX_ERROR), ParserMessages
				.getString("ParserProblemFactory.error.syntax.syntaxError")); //$NON-NLS-1$
	}

	protected final static String PROBLEM_PATTERN = "BaseProblemFactory.problemPattern"; //$NON-NLS-1$

	public String getMessage() {
		if (message != null)
			return message;

		String msg = (String) errorMessages.get(new Integer(id));
		if (msg == null)
			msg = ""; //$NON-NLS-1$

		if (arg != null) {
			msg = MessageFormat.format(msg, new Object[] { new String(arg) });
		}

		IASTFileLocation f = getFileLocation();
		String file = null;
		int line = 0;
		if (f == null) {
			file = ""; //$NON-NLS-1$
		} else {
			file = f.getFileName();
			line = f.getStartingLineNumber();
		}
		Object[] args = new Object[] { msg, file, new Integer(line) }; //$NON-NLS-1$        
		message = ParserMessages.getFormattedString(PROBLEM_PATTERN, args);
		return message;
	}

	public boolean checkCategory(int bitmask) {
		return ((id & bitmask) != 0);
	}

	public String getArguments() {
		return arg != null ? String.valueOf(arg) : ""; //$NON-NLS-1$
	}

}
