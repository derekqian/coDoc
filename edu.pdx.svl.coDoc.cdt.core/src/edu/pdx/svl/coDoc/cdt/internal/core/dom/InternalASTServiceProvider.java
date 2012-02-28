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
package edu.pdx.svl.coDoc.cdt.internal.core.dom;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.IASTServiceProvider;
import edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory;
import edu.pdx.svl.coDoc.cdt.core.dom.IParserConfiguration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTCompletionNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.CodeReader;
import edu.pdx.svl.coDoc.cdt.core.parser.IScanner;
import edu.pdx.svl.coDoc.cdt.core.parser.IScannerInfo;
import edu.pdx.svl.coDoc.cdt.core.parser.IScannerInfoProvider;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserFactory;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserLanguage;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserMode;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserUtil;
import edu.pdx.svl.coDoc.cdt.core.parser.ScannerInfo;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ISourceCodeParser;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.ANSICParserExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.GCCParserExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.GNUCSourceParser;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.ICParserExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ANSICPPParserExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.GNUCPPSourceParser;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.GPPParserExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPParserExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.DOMScanner;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.GCCScannerExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.GPPScannerExtensionConfiguration;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.IScannerExtensionConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.content.IContentType;

/**
 * @author jcamelon
 */
public class InternalASTServiceProvider implements IASTServiceProvider {

	protected static final GCCScannerExtensionConfiguration C_GNU_SCANNER_EXTENSION = new GCCScannerExtensionConfiguration();

	protected static final GPPScannerExtensionConfiguration CPP_GNU_SCANNER_EXTENSION = new GPPScannerExtensionConfiguration();

	private static final String[] dialects = { "C99", //$NON-NLS-1$
			"C++98", //$NON-NLS-1$
			"GNUC", //$NON-NLS-1$
			"GNUC++" //$NON-NLS-1$
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.IASTServiceProvider#getName()
	 */
	public String getName() {
		// TODO is this a name or an id?
		return "CDT AST Service"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.IASTServiceProvider#getTranslationUnit()
	 */
	public IASTTranslationUnit getTranslationUnit(IFile fileToParse)
			throws UnsupportedDialectException {
		return getTranslationUnit(fileToParse.getLocation().toOSString(),
				fileToParse, SavedCodeReaderFactory.getInstance(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.IASTServiceProvider#getTranslationUnit(edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory)
	 */
	public IASTTranslationUnit getTranslationUnit(IFile fileToParse,
			ICodeReaderFactory fileCreator) throws UnsupportedDialectException {
		return getTranslationUnit(fileToParse.getLocation().toOSString(),
				fileToParse, fileCreator, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.IASTServiceProvider#getTranslationUnit(edu.pdx.svl.coDoc.cdt.core.dom.ICodeReaderFactory,
	 *      edu.pdx.svl.coDoc.cdt.core.dom.IParserConfiguration)
	 */
	public IASTTranslationUnit getTranslationUnit(IFile fileToParse,
			ICodeReaderFactory fileCreator, IParserConfiguration configuration)
			throws UnsupportedDialectException {
		return getTranslationUnit(fileToParse.getLocation().toOSString(),
				fileToParse, fileCreator, configuration);
	}

	public IASTTranslationUnit getTranslationUnit(String filename,
			IResource infoProvider, ICodeReaderFactory fileCreator,
			IParserConfiguration configuration)
			throws UnsupportedDialectException {
		IProject project = infoProvider.getProject();
		IScannerInfo scanInfo = null;

		if (configuration == null) {
			IScannerInfoProvider provider = CCorePlugin.getDefault()
					.getScannerInfoProvider(project);
			if (provider != null) {
				IScannerInfo buildScanInfo = provider
						.getScannerInformation(infoProvider);

				if (buildScanInfo != null)
					scanInfo = buildScanInfo;
				else
					scanInfo = new ScannerInfo();
			}
		} else
			scanInfo = configuration.getScannerInfo();

		CodeReader reader = fileCreator
				.createCodeReaderForTranslationUnit(filename);
		if (reader == null)
			return null;
		IScanner scanner = null;
		ISourceCodeParser parser = null;

		if (configuration == null || configuration.getParserDialect() == null) {
			ParserLanguage l = getLanguage(filename, project);
			IScannerExtensionConfiguration scannerExtensionConfiguration;
			if (l == ParserLanguage.CPP)
				scannerExtensionConfiguration = CPP_GNU_SCANNER_EXTENSION;
			else
				scannerExtensionConfiguration = C_GNU_SCANNER_EXTENSION;

			scanner = new DOMScanner(reader, scanInfo,
					ParserMode.COMPLETE_PARSE, l, ParserFactory
							.createDefaultLogService(),
					scannerExtensionConfiguration, fileCreator);
			// assume GCC
			if (l == ParserLanguage.C)
				parser = new GNUCSourceParser(scanner,
						ParserMode.COMPLETE_PARSE, ParserUtil
								.getParserLogService(),
						new GCCParserExtensionConfiguration());
			else
				parser = new GNUCPPSourceParser(scanner,
						ParserMode.COMPLETE_PARSE, ParserUtil
								.getParserLogService(),
						new GPPParserExtensionConfiguration());
		} else {
			String dialect = configuration.getParserDialect();
			if (dialect.equals(dialects[0]) || dialect.equals(dialects[2]))
				scanner = new DOMScanner(reader, scanInfo,
						ParserMode.COMPLETE_PARSE, ParserLanguage.C, ParserUtil
								.getScannerLogService(),
						C_GNU_SCANNER_EXTENSION, fileCreator);
			else if (dialect.equals(dialects[1]) || dialect.equals(dialects[3]))
				scanner = new DOMScanner(reader, scanInfo,
						ParserMode.COMPLETE_PARSE, ParserLanguage.CPP,
						ParserUtil.getScannerLogService(),
						CPP_GNU_SCANNER_EXTENSION, fileCreator);
			else
				throw new UnsupportedDialectException();

			if (dialect.equals(dialects[0])) {
				ICParserExtensionConfiguration config = new ANSICParserExtensionConfiguration();
				parser = new GNUCSourceParser(scanner,
						ParserMode.COMPLETE_PARSE, ParserUtil
								.getParserLogService(), config);
			} else if (dialect.equals(dialects[1])) {
				ICPPParserExtensionConfiguration config = new ANSICPPParserExtensionConfiguration();
				parser = new GNUCPPSourceParser(scanner,
						ParserMode.COMPLETE_PARSE, ParserUtil
								.getParserLogService(), config);
			} else if (dialect.equals(dialects[2])) {
				ICParserExtensionConfiguration config = new GCCParserExtensionConfiguration();
				parser = new GNUCSourceParser(scanner,
						ParserMode.COMPLETE_PARSE, ParserUtil
								.getParserLogService(), config);
			} else if (dialect.equals(dialects[3])) {
				ICPPParserExtensionConfiguration config = new GPPParserExtensionConfiguration();
				parser = new GNUCPPSourceParser(scanner,
						ParserMode.COMPLETE_PARSE, ParserUtil
								.getParserLogService(), config);
			}
		}
		// Parse
		return parser.parse();
	}

	public ASTCompletionNode getCompletionNode(IStorage fileToParse,
			IProject project, int offset, ICodeReaderFactory fileCreator)
			throws UnsupportedDialectException {
		return getCompletionNode(fileToParse.getFullPath().toOSString(),
				project, offset, fileCreator);
	}

	public ASTCompletionNode getCompletionNode(IFile fileToParse, int offset,
			ICodeReaderFactory fileCreator) throws UnsupportedDialectException {
		return getCompletionNode(fileToParse.getLocation().toOSString(),
				fileToParse, offset, fileCreator);
	}

	public ASTCompletionNode getCompletionNode(String filename,
			IResource infoProvider, int offset, ICodeReaderFactory fileCreator)
			throws UnsupportedDialectException {
		// Get the scanner info
		IScannerInfo scanInfo = null;
		IProject project = infoProvider.getProject();

		IScannerInfoProvider provider = CCorePlugin.getDefault()
				.getScannerInfoProvider(project);
		if (provider != null) {
			IScannerInfo buildScanInfo = provider
					.getScannerInformation(infoProvider);
			if (buildScanInfo != null)
				scanInfo = buildScanInfo;
			else
				scanInfo = new ScannerInfo();
		}

		CodeReader reader = fileCreator
				.createCodeReaderForTranslationUnit(filename);

		ParserLanguage l = getLanguage(filename, project);
		IScannerExtensionConfiguration scannerExtensionConfiguration = null;
		if (l == ParserLanguage.CPP)
			scannerExtensionConfiguration = CPP_GNU_SCANNER_EXTENSION;
		else
			scannerExtensionConfiguration = C_GNU_SCANNER_EXTENSION;

		IScanner scanner = new DOMScanner(reader, scanInfo,
				ParserMode.COMPLETION_PARSE, l, ParserFactory
						.createDefaultLogService(),
				scannerExtensionConfiguration, fileCreator);
		scanner.setContentAssistMode(offset);

		// assume GCC
		ISourceCodeParser parser = null;
		if (l == ParserLanguage.C)
			parser = new GNUCSourceParser(scanner, ParserMode.COMPLETION_PARSE,
					ParserUtil.getParserLogService(),
					new GCCParserExtensionConfiguration());
		else
			parser = new GNUCPPSourceParser(scanner,
					ParserMode.COMPLETION_PARSE, ParserUtil
							.getParserLogService(),
					new GPPParserExtensionConfiguration());

		// Run the parse and return the completion node
		parser.parse();
		ASTCompletionNode node = parser.getCompletionNode();
		if (node != null) {
			node.count = scanner.getCount();
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.IASTServiceProvider#getSupportedDialects()
	 */
	public String[] getSupportedDialects() {
		return dialects;
	}

	private ParserLanguage getLanguage(String filename, IProject project) {
		// FIXME: ALAIN, for headers should we assume CPP ??
		// The problem is that it really depends on how the header was included.
		String id = null;
		IContentType contentType = CCorePlugin
				.getContentType(project, filename);
		if (contentType != null) {
			id = contentType.getId();
		}
		if (id != null) {
			if (CCorePlugin.CONTENT_TYPE_CXXHEADER.equals(id)) {
				return ParserLanguage.CPP;
			} else if (CCorePlugin.CONTENT_TYPE_CXXSOURCE.equals(id)) {
				return ParserLanguage.CPP;
			} else if (CCorePlugin.CONTENT_TYPE_CHEADER.equals(id)) {
				return ParserLanguage.CPP; // <============== is that right ?
											// should not this be C ?
			} else if (CCorePlugin.CONTENT_TYPE_CSOURCE.equals(id)) {
				return ParserLanguage.C;
			}
		}
		return ParserLanguage.CPP;
	}

	public IASTTranslationUnit getTranslationUnit(IStorage fileToParse,
			IProject project, ICodeReaderFactory fileCreator)
			throws UnsupportedDialectException {
		return getTranslationUnit(fileToParse.getFullPath().toOSString(),
				project, fileCreator, null);
	}

	public IASTTranslationUnit getTranslationUnit(IStorage fileToParse,
			IProject project) throws UnsupportedDialectException {
		return getTranslationUnit(fileToParse.getFullPath().toOSString(),
				project, SavedCodeReaderFactory.getInstance(), null);
	}
}
