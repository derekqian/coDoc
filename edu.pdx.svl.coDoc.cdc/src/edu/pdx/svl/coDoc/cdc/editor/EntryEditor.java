package edu.pdx.svl.coDoc.cdc.editor;

import java.io.File;
import java.util.Iterator;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiEditor;

import edu.pdx.svl.coDoc.cdc.Global;
import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;
import edu.pdx.svl.coDoc.cdc.referencemodel.PDFManager;
import edu.pdx.svl.coDoc.cdc.referencemodel.PDFSelection;
import edu.pdx.svl.coDoc.cdc.referencemodel.Reference;
import edu.pdx.svl.coDoc.cdc.referencemodel.References;
import edu.pdx.svl.coDoc.cdc.referencemodel.TextSelectionReference;
import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;

public class EntryEditor extends MultiEditor implements ISelectionListener
{
	private static final String CONTEXT_ID = "edu.pdx.svl.coDoc.cdc.editor.EntryEditor.contextid";
	private Composite container;
	private CLabel innerEditorTitle[];
	public References references;

	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		System.out.println("EntryEditor.init\n");
		setSite(site);
		setInput(input);
		createWorkbenchListener();
		
		Global.INSTANCE.entryEditor = this;
		//Global.INSTANCE.activeFileEditorInput = input;
		references = SimpleXML.read();
		if (references == null) {
			references = new References();
			references.addProject();
		}
	}
	
	public References getDocument() {
		return references;
	}
	public void setDocument(References refs) {
		references = refs;
	}
	
	public void createPartControl(Composite parent)
	{
		System.out.println("EntryEditor.createPartControl\n");
		container = new Composite(parent, SWT.BORDER);
		container.setLayout(new FillLayout());
		SashForm sashForm = new SashForm(container, SWT.HORIZONTAL);
		IEditorPart innerEditors[] = getInnerEditors();
		for (int i = 0; i < innerEditors.length; i++) 
		{
			final IEditorPart e = innerEditors[i];
			ViewForm viewForm = new ViewForm(sashForm, SWT.NONE);
			viewForm.marginWidth = 0;
			viewForm.marginHeight = 0;

			createInnerEditorTitle(i, viewForm);

			Composite content = createInnerPartControl(viewForm, e);

			viewForm.setContent(content);
			updateInnerEditorTitle(e, innerEditorTitle[i]);

			final int index = i;
			e.addPropertyListener(new IPropertyListener() 
			{
				public void propertyChanged(Object source, int property) 
				{
					if (property == IEditorPart.PROP_DIRTY || property == IWorkbenchPart.PROP_TITLE)
						if (source instanceof IEditorPart)
							updateInnerEditorTitle((IEditorPart) source, innerEditorTitle[index]);
				}
			});
		}
		
		initKeyBindingContext();
	}

	private void initKeyBindingContext() {
		final IContextService service = (IContextService)getSite().getService(IContextService.class);

		container.addFocusListener(new FocusListener() {
			IContextActivation currentContext = null;
			public void focusGained(FocusEvent e) {
				if (currentContext == null)
					currentContext = service.activateContext(CONTEXT_ID);
			}

			public void focusLost(FocusEvent e) {
				if (currentContext != null) {
					service.deactivateContext(currentContext);
					currentContext = null;
				}
			}
		});
	}	

	/**
	 * Draw the gradient for the specified editor.
	 */
	@Override
	protected void drawGradient(IEditorPart innerEditor, Gradient g) 
	{
		CLabel label = innerEditorTitle[getIndex(innerEditor)];
		if ((label == null) || label.isDisposed())
			return;

		label.setForeground(g.fgColor);
		label.setBackground(g.bgColors, g.bgPercents);
	}
	
	/*
	 * Create the label for each inner editor.
	 */
	protected void createInnerEditorTitle(int index, ViewForm parent) 
	{
		CLabel titleLabel = new CLabel(parent, SWT.SHADOW_NONE);
		// hookFocus(titleLabel);
		titleLabel.setAlignment(SWT.LEFT);
		titleLabel.setBackground(null, null);
		parent.setTopLeft(titleLabel);
		if (innerEditorTitle == null)
			innerEditorTitle = new CLabel[getInnerEditors().length];
		innerEditorTitle[index] = titleLabel;
	}
	
	/*
	 * Update the tab for an editor. This is typically called by a site when the
	 * tab title changes.
	 */
	public void updateInnerEditorTitle(IEditorPart editor, CLabel label) 
	{
		if ((label == null) || label.isDisposed())
			return;
		String title = editor.getTitle();
		if (editor.isDirty())
			title = "*" + title; //$NON-NLS-1$
		label.setText(title);
		Image image = editor.getTitleImage();
		if (image != null)
			if (!image.equals(label.getImage()))
				label.setImage(image);
		label.setToolTipText(editor.getTitleToolTip());
	}

	/*
	 * 
	 */
	protected int getIndex(IEditorPart editor) 
	{
		IEditorPart innerEditors[] = getInnerEditors();
		for (int i = 0; i < innerEditors.length; i++) 
		{
			if (innerEditors[i] == editor)
				return i;
		}
		return -1;
	}

	public void doSave(IProgressMonitor monitor)
	{
	}
	public boolean isSaveAsAllowed()
	{
		return false;
	}
	public void doSaveAs()
	{
	}
	public boolean isDirty()
	{
		return false;
	}
	public void setFocus()
	{
	}
	
	private TextSelection currentTextSelection;
	public TextSelectionReference getCurrentTextSelectionReference() {
		if (currentTextSelection == null) {
			MessageDialog.openError(null, "Alert",  "Warning:\nYou have not selected any text in your source file.\nNo reference has been saved.");
			return null;
		}

		int length = currentTextSelection.getLength();
		if (length == 0) {
			MessageDialog.openError(null, "Alert",  "Warning:\nYou have not selected any text in your source file.\nNo reference has been saved.");
			return null;
		}
		int offset = currentTextSelection.getOffset();
		String text = currentTextSelection.getText();
		
		TextSelectionReference tsr = new TextSelectionReference();
		tsr.setOffset(offset);
		tsr.setLength(length);
		tsr.setSelectedNode(CustomASTVisitor.getInstance().getSelectedASTNode());
		tsr.setText(text);
		tsr.fetchAcrobatData();
		
		PDFSelection pdfSel = tsr.getPdfSelection();
		if (pdfSel == null) {
			MessageDialog.openError(null, "Alert", "Warning:\nYou do not have an open PDF file in Acrobat from which you will create a reference.\nNo reference has been saved.");
			return null;
		}
		String pdfTxt = pdfSel.getText();
		if (pdfTxt == null || pdfTxt.equals("")) {
			MessageDialog.openError(null, "Alert", "Warning:\nYou have not selected any text in your PDF file.\nNo reference has been saved.");
			return null;
		}
		
		tsr.setPdfFile(PDFManager.INSTANCE.getCurrentPdfFile());

		return tsr;
	}
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		Global.INSTANCE.workbenchPart = part;

		if (selection == null) return;
		
//		if (selection instanceof TreeSelection) {
//			MessageDialog.openInformation(null, "Tree Selection", "Tree Selection");
//			System.out.println("Tree Selection");
//			TreeSelection ts = (TreeSelection)selection;
//		}
		
		if (selection instanceof TextSelection) {
			currentTextSelection = (TextSelection)selection;
		}
		
		IEditorPart editor = part.getSite().getPage().getActiveEditor();
		IFile inputFile = ((FileEditorInput) editor.getEditorInput()).getFile();
		try {
			TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault().getCoreModel().create(inputFile);
			IASTTranslationUnit ast = tu.getLanguage().getASTTranslationUnit(tu,
					ILanguage.AST_SKIP_IF_NO_BUILD_INFO);
			CustomASTVisitor astvisitor = CustomASTVisitor.getInstance();
			astvisitor.getEnviroment();
			astvisitor.setMode(CustomASTVisitor.MODE_SELECTION_TO_NODE);
			astvisitor.setTextSelection(currentTextSelection);
			ast.accept(astvisitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		IEditorReference[] editors = part.getSite().getPage().getEditorReferences();
//		editors[0].getName();
//		
//		IViewReference[] views = part.getSite().getPage().getViewReferences();
//		views[0].getTitle();
		
//		IViewReference outlineView = views[6];
//		IViewPart ov = outlineView.getView(true);
//		ContentOutline contentOutline = (ContentOutline)ov;
	}
	
	private void createWorkbenchListener() {
		getSite().getPage().addSelectionListener(this);
		//selectionChanged(getSite().getPart(), getSite().getPage().getSelection());
	}

	public void selectTextInTextEditor(TextSelectionReference tsr) {

		int offset = tsr.getOffset();
		int length = tsr.getLength();
		
		
		//new way in which we open the right source file	
		File fileToOpen = new File(tsr.getSourceFileReference().getFilePath());
		 
		if (fileToOpen.exists() && fileToOpen.isFile()) {
		    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 
		    try {
		        IDE.openEditorOnFileStore( page, fileStore );
		    } catch ( PartInitException e ) {
		        //Put your exception handler here if you wish to
		    }
		}
		
		IEditorPart cEditor = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
		IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
		if(editorrefs.length != 0)
		{
			MultiEditor editor = (MultiEditor) editorrefs[0].getEditor(false);
			
			IEditorPart[] editors = editor.getInnerEditors();
			for(int i=0; i<editors.length; i++)
			{
				System.out.println(editors[i].getClass().getName());
				if(editors[i].getClass().getName().equals("edu.pdx.svl.coDoc.cdt.internal.ui.editor.CEditor"))
				{
					cEditor = editors[i];
				}
			}
		}
		
		String selectednode = tsr.getSelectedNode();
		CustomASTVisitor astvisitor = CustomASTVisitor.getInstance();
		
		IFile inputFile = ((FileEditorInput) cEditor.getEditorInput()).getFile();
		try {
			TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault().getCoreModel().create(inputFile);
			IASTTranslationUnit ast = tu.getLanguage().getASTTranslationUnit(tu,
					ILanguage.AST_SKIP_IF_NO_BUILD_INFO);
			astvisitor.getEnviroment();
			astvisitor.setMode(CustomASTVisitor.MODE_NODE_TO_SELECTION);
			astvisitor.setSelectedASTNode(selectednode);
			ast.accept(astvisitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//ISelectionProvider selProv = workbenchPart.getSite().getSelectionProvider();
		ISelectionProvider selProv = cEditor.getEditorSite().getSelectionProvider();
		//TextSelection newSelection = new TextSelection(offset,length);
		TextSelection newSelection = astvisitor.getTextSelection();
		selProv.setSelection(newSelection);
		
	}
	
	public void selectTextInAcrobat(Reference ref) {

		PDFSelection pdfs = ref.getPdfSelection();
		if (pdfs != null) {
			pdfs.selectTextInAcrobat();
		}
	}
}
