/*******************************************************************************
 * Copyright (c) 2000 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.ui.text;

import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CEditor;
import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CSourceViewer;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.CReconcilingStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Configuration for an <code>SourceViewer</code> which shows C code.
 */
public class CSourceViewerConfiguration extends TextSourceViewerConfiguration {

	private CTextTools fTextTools;

	private CEditor fEditor;

	/**
	 * Creates a new C source viewer configuration for viewers in the given
	 * editor using the given C tools collection.
	 * 
	 * @param tools
	 *            the C text tools collection to be used
	 * @param editor
	 *            the editor in which the configured viewer will reside
	 */
	public CSourceViewerConfiguration(CTextTools tools, CEditor editor) {
		super();
		fTextTools = tools;
		fEditor = editor;
	}

	/**
	 * Returns the C multiline comment scanner for this configuration.
	 * 
	 * @return the C multiline comment scanner
	 */
	protected RuleBasedScanner getMultilineCommentScanner() {
		return fTextTools.getMultilineCommentScanner();
	}

	/**
	 * Returns the C singleline comment scanner for this configuration.
	 * 
	 * @return the C singleline comment scanner
	 */
	protected RuleBasedScanner getSinglelineCommentScanner() {
		return fTextTools.getSinglelineCommentScanner();
	}

	/**
	 * Returns the C string scanner for this configuration.
	 * 
	 * @return the C string scanner
	 */
	protected RuleBasedScanner getStringScanner() {
		return fTextTools.getStringScanner();
	}

	/**
	 * Returns the color manager for this configuration.
	 * 
	 * @return the color manager
	 */
	protected IColorManager getColorManager() {
		return fTextTools.getColorManager();
	}

	/**
	 * Returns the editor in which the configured viewer(s) will reside.
	 * 
	 * @return the enclosing editor
	 */
	protected ITextEditor getEditor() {
		return fEditor;
	}

	/**
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {

		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		RuleBasedScanner scanner;

		if (sourceViewer instanceof CSourceViewer) {
			String language = ((CSourceViewer) sourceViewer)
					.getDisplayLanguage();
			if (language != null && language.equals(CEditor.LANGUAGE_CPP)) {
				scanner = fTextTools.getCppCodeScanner();
			} else {
				scanner = fTextTools.getCCodeScanner();
			}
		} else {
			scanner = fTextTools.getCCodeScanner();
		}

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);

		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		// TextAttribute attr = new
		// TextAttribute(manager.getColor(ICColorConstants.C_DEFAULT));

		dr = new DefaultDamagerRepairer(getSinglelineCommentScanner());
		reconciler.setDamager(dr, ICPartitions.C_SINGLE_LINE_COMMENT);
		reconciler.setRepairer(dr, ICPartitions.C_SINGLE_LINE_COMMENT);

		dr = new DefaultDamagerRepairer(getMultilineCommentScanner());
		reconciler.setDamager(dr, ICPartitions.C_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, ICPartitions.C_MULTILINE_COMMENT);

		dr = new DefaultDamagerRepairer(getStringScanner());
		reconciler.setDamager(dr, ICPartitions.C_STRING);
		reconciler.setRepairer(dr, ICPartitions.C_STRING);

		dr = new DefaultDamagerRepairer(getStringScanner());
		reconciler.setDamager(dr, ICPartitions.C_CHARACTER);
		reconciler.setRepairer(dr, ICPartitions.C_CHARACTER);

		return reconciler;
	}

	/**
	 * @see SourceViewerConfiguration#getReconciler(ISourceViewer)
	 */
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		if (fEditor != null && fEditor.isEditable()) {
			// Delay changed and non-incremental reconciler used due to
			// PR 130089
			MonoReconciler reconciler = new MonoReconciler(
					new CReconcilingStrategy(fEditor), false);
			reconciler.setDelay(500);
			return reconciler;
		}
		return null;
	}

	/**
	 * @see SourceViewerConfiguration#getConfiguredContentTypes(ISourceViewer)
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				ICPartitions.C_MULTILINE_COMMENT,
				ICPartitions.C_SINGLE_LINE_COMMENT, ICPartitions.C_STRING,
				ICPartitions.C_CHARACTER };
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return fTextTools.getDocumentPartitioning();
	}
}
