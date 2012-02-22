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
package edu.pdx.svl.coDoc.cdt.internal.core.parser;

import edu.pdx.svl.coDoc.cdt.core.parser.ParserFactoryError;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserMode;
import edu.pdx.svl.coDoc.cdt.core.parser.extension.ExtensionDialect;
import edu.pdx.svl.coDoc.cdt.core.parser.extension.IASTFactoryExtension;
import edu.pdx.svl.coDoc.cdt.core.parser.extension.IParserExtension;
import edu.pdx.svl.coDoc.cdt.core.parser.extension.IParserExtensionFactory;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.GCCASTExtension;

/**
 * @author jcamelon
 */
public class ParserExtensionFactory implements IParserExtensionFactory {

	private final ExtensionDialect dialect;

	/**
	 * @param dialect
	 */
	public ParserExtensionFactory(ExtensionDialect dialect) {
		this.dialect = dialect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IParserExtensionFactory#createParserExtension()
	 */
	public IParserExtension createParserExtension() throws ParserFactoryError {
		if (dialect == ExtensionDialect.GCC)
			return new GCCParserExtension();
		throw new ParserFactoryError(ParserFactoryError.Kind.BAD_DIALECT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.extension.IParserExtensionFactory#createASTExtension(edu.pdx.svl.coDoc.cdt.core.parser.ParserMode)
	 */
	public IASTFactoryExtension createASTExtension(ParserMode mode) {
		if (dialect == ExtensionDialect.GCC)
			return GCCASTExtension.createExtension(mode);
		throw new ParserFactoryError(ParserFactoryError.Kind.BAD_DIALECT);
	}

}
