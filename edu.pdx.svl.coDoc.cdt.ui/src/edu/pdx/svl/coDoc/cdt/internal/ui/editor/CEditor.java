package edu.pdx.svl.coDoc.cdt.internal.ui.editor;

import edu.pdx.svl.coDoc.cdt.ui.CUIPlugin;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.CTextTools;
import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CSourceViewer;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.CSourceViewerConfiguration;
import edu.pdx.svl.coDoc.cdt.ui.IWorkingCopyManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

public class CEditor extends TextEditor
{ 
	/* Source code language to display */
	public final static String LANGUAGE_CPP = "CEditor.language.cpp"; //$NON-NLS-1$

	public final static String LANGUAGE_C = "CEditor.language.c"; //$NON-NLS-1$	

  /**
   * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#initializeEditor()
   */
  protected void initializeEditor() 
  {
		CTextTools textTools = CUIPlugin.getDefault().getTextTools();
		setSourceViewerConfiguration(new CSourceViewerConfiguration(textTools,
				this));
    setDocumentProvider(CUIPlugin.getDefault().getDocumentProvider());
  }
  
  public void createPartControl(final Composite parent) {
	  System.out.println(parent.getClass().getName());
	  System.out.println(getEditorInput().getClass().getName());
		//parent.setLayout(new FillLayout());
	  super.createPartControl(parent);
		//ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
  }

	/*
	 * @see AbstractTextEditor#createSourceViewer(Composite, IVerticalRuler,
	 *      int)
	 */
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		// Figure out if this is a C or C++ source file
		IWorkingCopyManager mgr = CUIPlugin.getDefault()
				.getWorkingCopyManager();
		ITranslationUnit unit = mgr.getWorkingCopy(getEditorInput());
		String fileType = LANGUAGE_CPP;
		if (unit != null) {
			// default is C++ unless the project as C Nature Only
			// we can then be smarter.
			IProject p = unit.getCProject().getProject();
			if (!CoreModel.hasCCNature(p)) {
				fileType = unit.isCXXLanguage() ? LANGUAGE_CPP : LANGUAGE_C;
			}
		}

		fAnnotationAccess = createAnnotationAccess();

		ISharedTextColors sharedColors = CUIPlugin.getDefault()
				.getSharedTextColors();
		fOverviewRuler = createOverviewRuler(sharedColors);

		ISourceViewer sourceViewer = new CSourceViewer(this, parent, ruler,
				styles, fOverviewRuler, isOverviewRulerVisible(), fileType);
		return sourceViewer;
	}
}
