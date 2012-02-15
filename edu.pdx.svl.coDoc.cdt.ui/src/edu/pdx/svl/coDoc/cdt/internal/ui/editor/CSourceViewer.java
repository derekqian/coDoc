package edu.pdx.svl.coDoc.cdt.internal.ui.editor;

import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;

public class CSourceViewer extends ProjectionViewer implements
		ITextViewerExtension {

	/** Editor. */
	private final CEditor editor;

	private String fDisplayLanguage;

	public CSourceViewer(CEditor editor, Composite parent,
			IVerticalRuler ruler, int styles, IOverviewRuler fOverviewRuler,
			boolean isOverviewRulerShowing, String language) {
		super(parent, ruler, fOverviewRuler, isOverviewRulerShowing, styles);
		this.editor = editor;
		fDisplayLanguage = language;
	}

	public void setDisplayLanguage(String language) {
		fDisplayLanguage = language;
	}

	public String getDisplayLanguage() {
		return fDisplayLanguage;
	}

}
