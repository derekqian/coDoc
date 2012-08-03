package edu.pdx.svl.coDoc.cdc.editor;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTProblem;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.ASTNode;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import org.eclipse.cdt.internal.core.model.TranslationUnit;
import org.eclipse.cdt.internal.ui.editor.CEditor;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.internal.PartService;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.part.AbstractMultiEditor;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiEditor;
import org.eclipse.ui.part.MultiEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.themes.ITheme;

import edu.pdx.svl.coDoc.cdc.datacenter.CodeSelection;
import edu.pdx.svl.coDoc.cdc.datacenter.LinkEntry;
import edu.pdx.svl.coDoc.cdc.datacenter.SpecSelection;
import edu.pdx.svl.coDoc.cdc.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.cdc.referencemodel.References;
import edu.pdx.svl.coDoc.poppler.editor.PDFEditor;
import edu.pdx.svl.coDoc.poppler.editor.PDFPageViewer;

public class EntryEditor extends MultiEditor implements IReusableEditor, ISelectionListener
{
	private static final String CONTEXT_ID = "edu.pdx.svl.coDoc.cdc.editor.EntryEditor.contextid";
	private Composite container;
	private SashForm sashForm;
	private CLabel innerEditorTitle[];
	public References references;
	private IPath cdcFilepath;
	private String projectname = null;
	private IPath codeFilepath = null;
	private IPath specFilepath = null;
	private MyASTNode myAST = null;
	private MyASTNode[] myASTLeaf = null;
	private String curcategoryid = null;
	
	public void setInput(IEditorInput input) {
		super.setInput(input);
		IEditorInput[] editorinputs = ((EntryEditorInput) input).getInput();
		// IPath path = ((FileEditorInput) editorinputs[0]).getPath();
		IPath path = ((FileEditorInput) editorinputs[0]).getFile().getFullPath();
		if(!path.getFileExtension().equals("pdf")) {
			codeFilepath = path;
		}
		path = ((FileEditorInput) editorinputs[editorinputs.length-1]).getFile().getFullPath();
		if(path.getFileExtension().equals("pdf")) {
			specFilepath = path;
		}
		firePropertyChange(IEditorPart.PROP_INPUT);
		return;
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		System.out.println("EntryEditor.init\n");
		super.init(site, input);
		cdcFilepath = CDCEditor.getLatestPath();
		projectname = CDCEditor.getLatestProjectName();
		setPartName(projectname);
		setSite(site);
		setInput(input);
		
		createWorkbenchListener();
	}
	
	public IPath getCDCFilepath() {
		return cdcFilepath;
	}
	public IPath getCodeFilepath() {
		return codeFilepath;
	}
	public IPath getSpecFilepath() {
		return specFilepath;
	}
	public String getProjectName() {
		return projectname;
	}
	public String getCDCFilename() {
		return CDCEditor.projname2cdcName(projectname);
	}
	
	public void createPartControl(Composite parent)
	{
		System.out.println("EntryEditor.createPartControl\n");
		//container = new Composite(parent, SWT.BORDER);
		//container.setLayout(new FillLayout());
		sashForm = new SashForm(parent, SWT.HORIZONTAL);
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
			if(e instanceof CEditor) {
				((CEditor)e).getDocumentProvider().getDocument(e.getEditorInput()).addDocumentListener(new IDocumentListener(){

					@Override
					public void documentAboutToBeChanged(DocumentEvent arg0) {
					}

					@Override
					public void documentChanged(DocumentEvent arg0) {
						myAST = buildMyAST();
						myASTLeaf = buildMyASTLeaf();
					}
				});
				myAST = buildMyAST();
				myASTLeaf = buildMyASTLeaf();
			}
		}
		
		initKeyBindingContext();
	}
	
	public MyASTNode getMyAST() {
		return myAST;
	}

	private void initKeyBindingContext() {
		final IContextService service = (IContextService)getSite().getService(IContextService.class);

		//container.addFocusListener(new FocusListener() {
		sashForm.addFocusListener(new FocusListener() {
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

	// Draw the gradient for the specified editor.
	@Override
	protected void drawGradient(IEditorPart innerEditor, Gradient g) 
	{
		CLabel label = innerEditorTitle[getIndex(innerEditor)];
		if ((label == null) || label.isDisposed())
			return;

		label.setForeground(g.fgColor);
		label.setBackground(g.bgColors, g.bgPercents);
	}
	
	// Create the label for each inner editor.
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
	
	// Update the tab for an editor. This is typically called by a site when the tab title changes.
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
		return;
	}
	public boolean isSaveAsAllowed()
	{
		return false;
	}
	public void doSaveAs()
	{
		return;
	}
	public boolean isDirty()
	{
		return false;
	}
	public void setFocus()
	{
		//super.setFocus();
		//sashForm.setFocus();
		//container.setFocus();
		System.out.println("get focus");
	}
	public void enableCodeEditorCaret() {
		IEditorPart cEditor = CDCEditor.getActiveCEditorChild(this);
		ITextOperationTarget target = (ITextOperationTarget)cEditor.getAdapter(ITextOperationTarget.class);
		if (target instanceof ITextViewer) {
			((ITextViewer) target).getTextWidget().getCaret().setVisible(true);
	    }
	}
	
	private void createWorkbenchListener() {
		getSite().getPage().addSelectionListener(this);
		//selectionChanged(getSite().getPart(), getSite().getPage().getSelection());
	}
	
	public String getCurCategoryId() {
		return curcategoryid;
	}
	public void setCurCategoryId(String curcategoryid) {
		this.curcategoryid = curcategoryid;
	}
	
	public TextSelection adjustSelection(TextSelection selection) {
		int i;
		MyASTNode parent = myAST;
		MyASTNode[] nodes = myAST.getChildren();
		for(i=0; i<nodes.length; i++) {
			//System.out.println(selection.getOffset() + "  <-->  " + (selection.getOffset()+selection.getLength()));
			//System.out.println(((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() + "  <-->  " + (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()));
			if((((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() <= selection.getOffset())
			 &&((selection.getOffset()+selection.getLength()) <= (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()))) {
				parent = nodes[i];
				nodes = nodes[i].getChildren();
				i = -1;
				continue;
			}
		}
		return new TextSelection(((ASTNode) parent.getData()).getFileLocation().getNodeOffset(), ((ASTNode) parent.getData()).getFileLocation().getNodeLength());
	}
	public TextSelection adjustSelection1(TextSelection selection) {
		int begin = selection.getOffset();
		int end = selection.getOffset() + selection.getLength() -1;

		System.out.printf("adjustSelection1: enter with (%d, %d)\n", begin, end);
		if(end < ((ASTNode) myASTLeaf[0].getData()).getFileLocation().getNodeOffset()) {
			// before the first node
			System.out.println("adjustSelection1: <S,S>(XXX)");
			return selection;
		} else if((((ASTNode) myASTLeaf[myASTLeaf.length-1].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[myASTLeaf.length-1].getData()).getFileLocation().getNodeLength()) <= begin) {
			// after the last node
			System.out.println("adjustSelection1: (XXX)<S,S>");
			return selection;
		} else {
			int i, j;
			// begin
			for(i=0; i<myASTLeaf.length; i++) {
				if(begin < ((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset()) {
					// XXXX(___), begin isn't inside any node, keep it as is.
					System.out.println("adjustSelection1: <begin,(XXX)");
					break;
				} else if((((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset() <= begin)
				&& (begin < (((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeLength()))) {
					// ____(XXXX), begin is inside this node, adjust it to the left boundary
					System.out.println("adjustSelection1: (X<begin,XX)");
					begin = ((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset();
					break;
				}
			}
			// end
			for(j=myASTLeaf.length-1; j>=i; j--) {
				if((((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeLength()) <= end) {
					// (___)XXXX, end isn't inside any node, keep it as is.
					System.out.println("adjustSelection1: (XXX),end>");
					break;
				} else if((((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset() <= end)
				&& (end < (((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeLength()))) {
					// (XXXX)____, end is inside this node, adjust it to the left boundary
					System.out.println("adjustSelection1: (XX,end>X)");
					end = ((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeLength()-1;
					break;
				}
			}
		}
		System.out.printf("adjustSelection1: quit with (%d, %d)", begin, end);		

		return new TextSelection(begin, end-begin+1);
	}
	
	public String selection2Node(TextSelection selection) {
		int i;
		String retstr = new String();
		MyASTNode parent = myAST;
		retstr += parent;
		MyASTNode[] nodes = myAST.getChildren();
		for(i=0; i<nodes.length; i++) {
			//System.out.println(selection.getOffset() + "  <-->  " + (selection.getOffset()+selection.getLength()));
			//System.out.println(((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() + "  <-->  " + (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()));
			if((((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() <= selection.getOffset())
			 &&((selection.getOffset()+selection.getLength()) <= (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()))) {
				parent = nodes[i];
				retstr = parent + "\\" + retstr;
				nodes = nodes[i].getChildren();
				i = -1;
				continue;
			}
		}
		//selectTextInTextEditor(new TextSelection(((ASTNode) parent.getData()).getOffset(), ((ASTNode) parent.getData()).getLength()));
		retstr = Integer.toHexString(parent.getData().getRawSignature().hashCode()) + "\\" + retstr;
		return retstr;
		// return Integer.toString(selection.getOffset())+"\\"+Integer.toString(selection.getLength());
	}
	public String selection2Node1(TextSelection selection) {
		int i,j;
		int begin, end;
		int offset1=0, offset2=0;
		String retstr = null;
		
		begin = selection.getOffset();
		end = selection.getOffset() + selection.getLength() - 1;
		if(end < myASTLeaf[0].getData().getFileLocation().getNodeOffset()) {
			offset1 = begin - myASTLeaf[0].getData().getFileLocation().getNodeOffset();
			offset2 = end - myASTLeaf[0].getData().getFileLocation().getNodeOffset();
			retstr = Integer.toString(offset1);
			retstr += "\\";
			retstr += Integer.toString(offset2);
		} if(begin >= myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeOffset()+myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeLength()) {
			offset1 = begin - (myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeOffset()+myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeLength()-1);
			offset2 = end - (myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeOffset()+myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeLength()-1);
			retstr = Integer.toString(offset1);
			retstr += "\\";
			retstr += Integer.toString(offset2);
		} else {
			// begin
			for(i=0; i<myASTLeaf.length; i++) {
				if(begin < myASTLeaf[i].getData().getFileLocation().getNodeOffset()) {
					offset1 = begin - myASTLeaf[i].getData().getFileLocation().getNodeOffset();
					break;
				} else if((begin >= myASTLeaf[i].getData().getFileLocation().getNodeOffset())
						&&(begin < myASTLeaf[i].getData().getFileLocation().getNodeOffset()+myASTLeaf[i].getData().getFileLocation().getNodeLength())) {
					offset1 = 0;
					break;
				}
			}
			if(i == myASTLeaf.length) {
				try {
					throw new Exception("Can't find a value for offset1");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// end
			for(j=myASTLeaf.length-1; j>=i; j--) {
				if(end >= myASTLeaf[j].getData().getFileLocation().getNodeOffset()+myASTLeaf[j].getData().getFileLocation().getNodeLength()) {
					offset2 = end - (myASTLeaf[j].getData().getFileLocation().getNodeOffset()+myASTLeaf[j].getData().getFileLocation().getNodeLength()-1);
					break;
				} else if((end >= myASTLeaf[j].getData().getFileLocation().getNodeOffset())
						&&(end < myASTLeaf[j].getData().getFileLocation().getNodeOffset()+myASTLeaf[j].getData().getFileLocation().getNodeLength())) {
					offset2 = 0;
					break;
				}
			}
			if(j < i) {
				try {
					throw new Exception("Can't find a value for offset2");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// generate return string
			retstr = Integer.toString(offset1);
			retstr += "\\";
			retstr += Integer.toString(offset2);
			for(int k=i; k<=j; k++) {
				retstr += "#";
				MyASTNode node = myASTLeaf[k];
				retstr += Integer.toHexString(node.getData().getRawSignature().hashCode());
				do {
					retstr += "\\";
					retstr += node;
					node = node.getParent();
				} while(node != null);
			}
		}
		return retstr;
	}
	
	public TextSelection node2Selection(String nodestr) {
		Queue<MyASTNode> queue = new LinkedList<MyASTNode>();
		queue.add(myAST);
		while(!queue.isEmpty()) {
			MyASTNode head = queue.remove();
			MyASTNode node = head;
			String str = nodestr;
			int index = str.indexOf('\\');
			String piece = str.substring(0, index);
			System.out.println(Integer.toHexString(node.getData().getRawSignature().hashCode()));
			if(piece.equals(Integer.toHexString(node.getData().getRawSignature().hashCode()))) {
				for(; node != null; node = node.getParent()) {
					str = str.substring(index+1);
					index = str.indexOf('\\');
					if(index == -1) {
						piece = str;
					} else {
						piece = str.substring(0,index);
					}
					if(!piece.equals(node.toString())) {
						break;
					}
				}
			}
			if(node == null) {
				return new TextSelection(((ASTNode)head.getData()).getFileLocation().getNodeOffset(),((ASTNode)head.getData()).getFileLocation().getNodeLength());
			}
			if(head.hasChildren()) {
				for(MyASTNode n : head.getChildren()) {
					queue.add(n);
				}
			}
		}
		//ISelectionProvider selProv = workbenchPart.getSite().getSelectionProvider();
		return null;
	}
	public TextSelection node2Selection1(String nodestr) {
		TextSelection sel = null;
		Vector<String> strvec = new Vector<String>();
		while(nodestr.lastIndexOf('#') != -1) {
			int index = nodestr.lastIndexOf('#');
			strvec.add(0, nodestr.substring(index+1));
			nodestr = nodestr.substring(0, index);
		}
		strvec.add(0,nodestr);
		
		String temp = strvec.get(0);
		int index = temp.indexOf('\\');
		int offset1 = Integer.valueOf(temp.substring(0,index));
		int offset2 = Integer.valueOf(temp.substring(index+1));
		
		if(offset1<0 && offset2<0) {
			IASTFileLocation loc = myASTLeaf[0].getData().getFileLocation();
			sel = new TextSelection(loc.getNodeOffset()+offset1,loc.getNodeOffset()+offset2);
		} else if(offset1>0 && offset2>0) {
			IASTFileLocation loc = myASTLeaf[myASTLeaf.length-1].getData().getFileLocation();
			sel = new TextSelection(loc.getNodeOffset()+loc.getNodeLength()+offset1-1, loc.getNodeOffset()+loc.getNodeLength()+offset2-1);
		} else {
			int i,j;
			for(i=0; i<(myASTLeaf.length-(strvec.size()-1)); i++) {
				for(j=1; j<strvec.size(); j++) {
					temp = strvec.get(j);
					MyASTNode node = myASTLeaf[i+j-1];
					String s = Integer.toHexString(node.getData().getRawSignature().hashCode());
					while(node != null) {
						s += "\\";
						s += node;
						node = node.getParent();
					}
					if(!s.equals(temp)) {
						break;
					}
				}
				if(j == strvec.size()) {
					break;
				}
			}
			if(i != (myASTLeaf.length-(strvec.size()-1))) {
				IASTFileLocation loc1 = myASTLeaf[i].getData().getFileLocation();
				IASTFileLocation loc2 = myASTLeaf[i+(strvec.size()-1)-1].getData().getFileLocation();
				sel = new TextSelection(loc1.getNodeOffset()+offset1, loc2.getNodeOffset()+loc2.getNodeLength()+offset2-loc1.getNodeOffset()-offset1);
			}
		}
		return sel;
	}
	
	private TextSelection currentRawSelection;
	private TextSelection currentSyntaxSelection;
	public CodeSelection getSelectionInTextEditor() {
		if (currentRawSelection == null) {
			MessageDialog.openError(null, "Alert",  "Warning:\nYou have not selected any text in your source file.\nNo reference has been saved.");
			return null;
		}
		int length = currentRawSelection.getLength();
		if (length == 0) {
			MessageDialog.openError(null, "Alert",  "Warning:\nYou have not selected any text in your source file.\nNo reference has been saved.");
			return null;
		}
		CodeSelection selection = new CodeSelection();
		selection.setSelCodePath(Integer.toString(currentRawSelection.getOffset())+"\\"+Integer.toString(currentRawSelection.getLength()));
		selection.setSyntaxCodePath(selection2Node1(currentSyntaxSelection));
		ITextEditor editor = (ITextEditor) CDCEditor.getActiveCEditorChild(this);
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		try {
			selection.setSelCodeText(doc.get(currentRawSelection.getOffset(), currentRawSelection.getLength()));
			selection.setSyntaxCodeText(doc.get(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength()));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return selection;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println("<<<<<-------------------");
		System.out.println("EntryEditor.selectionChanged: " + part.getClass().getName());

		if (selection == null) return;
		System.out.println("EntryEditor.selectionChanged: " + selection.getClass().getName());
		
		IEditorReference[] ef = part.getSite().getPage().getEditorReferences();
		for(int i=0; i<ef.length; i++) {
			if(ef[i].getEditor(false) != null)
				System.out.println("EntryEditor.selectionChanged: editors: " + ef[i].getEditor(false).getClass().getName());
		}
		if(part.getSite().getPage().getActiveEditor() != null)
			System.out.println("EntryEditor.selectionChanged: active editor: " + part.getSite().getPage().getActiveEditor().getClass().getName());
		System.out.println("------------------->>>>>>>>");

		if(part instanceof IEditorPart) {
			EntryEditor editorPart = (EntryEditor) CDCEditor.getActiveEntryEditor();
			if (editorPart != null) {
				if (selection instanceof TextSelection) {
					if(((TextSelection) selection).getLength() == 0) {
						currentRawSelection = null;
						currentSyntaxSelection = null;
						selectTextInTextEditor(null);						
					} else {
						currentRawSelection = (TextSelection) selection;
						currentSyntaxSelection = adjustSelection1((TextSelection) selection);
						selectTextInTextEditor(currentSyntaxSelection);						
					}
				}
			}
		}
		
//		IViewReference outlineView = views[6];
//		IViewPart ov = outlineView.getView(true);
//		ContentOutline contentOutline = (ContentOutline)ov;
	}

	TextSelection oldSel = null;
	public void selectTextInTextEditor(TextSelection newSelection) {
		//new way in which we open the right source file	
		/*MapEntry mapEntry = null;
		File fileToOpen = new File(mapEntry.getCodefilename());
		 
		if (fileToOpen.exists() && fileToOpen.isFile()) {
		    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 
		    try {
		        IDE.openEditorOnFileStore( page, fileStore );
		    } catch ( PartInitException e ) {
		        //Put your exception handler here if you wish to
		    }
		}
		
		IEditorPart cEditor = CDCEditor.getActiveCEditorChild(this);
		ISelectionProvider selProv = cEditor.getEditorSite().getSelectionProvider();
		selProv.setSelection(newSelection);*/
		IEditorPart cEditor = CDCEditor.getActiveCEditorChild(this);
		ITextOperationTarget target = (ITextOperationTarget)cEditor.getAdapter(ITextOperationTarget.class);
		if(newSelection!=null) {
			if (target instanceof ITextViewer) {
				//viewer.setTextColor(new Color(null, 255, 0, 0), currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength(), true);
				//viewer.setSelectedRange(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength());
				TextPresentation presentation = new TextPresentation();
				TextAttribute attr = new TextAttribute(new Color(null, 0, 0, 0),
					      new Color(null, 255, 184, 134), TextAttribute.STRIKETHROUGH);
				presentation.addStyleRange(new StyleRange(newSelection.getOffset(), newSelection.getLength(), attr.getForeground(),
					      attr.getBackground()));
				((ITextViewer) target).changeTextPresentation(presentation, false);
		    }	
			oldSel = newSelection;
		} else {
			if(oldSel!=null) {
				if (target instanceof ITextViewerExtension2) {
					((ITextViewerExtension2) target).invalidateTextPresentation(oldSel.getOffset(), oldSel.getLength());
			    } 			
				oldSel = null;
			}
		}
	}
	
	public void showCodeAnchors(Vector<LinkEntry> mapEntries) {
		if(mapEntries.isEmpty()) return;
		IEditorPart cEditor = CDCEditor.getActiveCEditorChild(this);
		ITextOperationTarget target = (ITextOperationTarget)cEditor.getAdapter(ITextOperationTarget.class);
		if (target instanceof ITextViewer) {
			Iterator it = mapEntries.iterator();
			while(it.hasNext()) {
				CodeSelection sel = ((LinkEntry) it.next()).codeselpath;
				if(sel!=null) {
					String selectednode = sel.getSyntaxCodePath();
					TextSelection newSelection = node2Selection1(selectednode);
					if (newSelection!=null) {
						//viewer.setTextColor(new Color(null, 255, 0, 0), currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength(), true);
						//viewer.setSelectedRange(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength());
						TextPresentation presentation = new TextPresentation();
						TextAttribute attr = new TextAttribute(new Color(null, 0, 0, 0),
							      new Color(null, 232, 242, 254), TextAttribute.STRIKETHROUGH);
						presentation.addStyleRange(new StyleRange(newSelection.getOffset(), newSelection.getLength(), attr.getForeground(),
							      attr.getBackground()));
						((ITextViewer) target).changeTextPresentation(presentation, false);
				    } 			
				}
			}
		}
	}	
	public void hideCodeAnchors(CodeSelection[] codesels) {
		if(codesels==null) return;
		IEditorPart cEditor = CDCEditor.getActiveCEditorChild(this);
		ITextOperationTarget target = (ITextOperationTarget)cEditor.getAdapter(ITextOperationTarget.class);
		for(CodeSelection sel : codesels) {
			if(sel!=null) {
				String selectednode = sel.getSyntaxCodePath();
				TextSelection newSelection = node2Selection1(selectednode);
				if (target instanceof ITextViewerExtension2) {
					((ITextViewerExtension2) target).invalidateTextPresentation(newSelection.getOffset(), newSelection.getLength());
			    } 			
			}
		}
	}
	public void hideAllCodeAnchors() {
		IEditorPart innerEditors[] = getInnerEditors();
		ITextOperationTarget target = (ITextOperationTarget)innerEditors[0].getAdapter(ITextOperationTarget.class);
		if (target instanceof ITextViewer) {
			((ITextViewer) target).invalidateTextPresentation();
	    } 		
	}
	LinkEntry oldMapCode = null;
	public void highlightCodeAnchor(LinkEntry me) {
		IEditorPart cEditor = CDCEditor.getActiveCEditorChild(this);
		ITextOperationTarget target = (ITextOperationTarget)cEditor.getAdapter(ITextOperationTarget.class);
		if (target instanceof ITextViewer) {
			if(oldMapCode!=null) {
				CodeSelection sel = oldMapCode.codeselpath;
				if(sel!=null) {
					String selectednode = sel.getSyntaxCodePath();
					TextSelection newSelection = node2Selection1(selectednode);
					if (newSelection!=null) {
						//viewer.setTextColor(new Color(null, 255, 0, 0), currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength(), true);
						//viewer.setSelectedRange(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength());
						TextPresentation presentation = new TextPresentation();
						TextAttribute attr = new TextAttribute(new Color(null, 0, 0, 0),
							      new Color(null, 232, 242, 254), TextAttribute.STRIKETHROUGH);
						presentation.addStyleRange(new StyleRange(newSelection.getOffset(), newSelection.getLength(), attr.getForeground(),
							      attr.getBackground()));
						((ITextViewer) target).changeTextPresentation(presentation, false);
				    } 			
				}						
			}
			oldMapCode = me;
			if(me!=null) {
				CodeSelection sel = oldMapCode.codeselpath;
				if(sel!=null) {
					String selectednode = sel.getSyntaxCodePath();
					TextSelection newSelection = node2Selection1(selectednode);
					if (newSelection!=null) {
						//viewer.setTextColor(new Color(null, 255, 0, 0), currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength(), true);
						//viewer.setSelectedRange(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength());
						TextPresentation presentation = new TextPresentation();
						TextAttribute attr = new TextAttribute(new Color(null, 255, 255, 255),
							      new Color(null, 0, 0, 0), TextAttribute.STRIKETHROUGH);
						presentation.addStyleRange(new StyleRange(newSelection.getOffset(), newSelection.getLength(), attr.getForeground(),
							      attr.getBackground()));
						((ITextViewer) target).changeTextPresentation(presentation, false);
						((CEditor) cEditor).getViewer().revealRange(newSelection.getOffset(), newSelection.getLength());			
				    } 			
				}						
			}
		}
	}
	
	public SpecSelection getSelectionInAcrobat() {
		PDFPageViewer acrobatInterface = ((PDFEditor) CDCEditor.getActivePDFEditorChild(this)).getPDFPageViewer();
		if (CDCEditor.getActivePDFEditorChild(this) == null) {
			MessageDialog.openError(null, "Alert", "Warning:\nYou do not have an open PDF file in Acrobat from which you will create a reference.\nNo reference has been saved.");
			return null;
		}
		String pdftext = acrobatInterface.getSelectedText();
		if(pdftext == null || pdftext.equals("")) {
			MessageDialog.openError(null, "Alert", "Warning:\nYou have not selected any text in your PDF file.\nNo reference has been saved.");
			return null;
		}
		pdftext = pdftext.replace('\n', ' ').replace('\t', ' ');
		
		int page = acrobatInterface.getPage();
		Rectangle sel = acrobatInterface.getSelection();
		int top = sel.y;
		int bottom = sel.y + sel.height;
		int left = sel.x;
		int right = sel.x + sel.width;
		
		SpecSelection selection = new SpecSelection();
		selection.setPage(page);
		selection.setTop(top);
		selection.setBottom(bottom);
		selection.setLeft(left);
		selection.setRight(right);
		selection.setPDFText(pdftext);
		
		return selection;
	}
	
	public void selectTextInAcrobat(SpecSelection sel) {
		PDFPageViewer acrobatInterface;
		acrobatInterface = ((PDFEditor) CDCEditor.getActivePDFEditorChild(this)).getPDFPageViewer();

		if(sel!=null) {
			acrobatInterface.selectText(sel.getPage(), sel.getTop(), sel.getBottom(), sel.getLeft(), sel.getRight());			
		} else {
			acrobatInterface.selectText(-1, 0, 0, 0, 0);
		}
	}
	
	private MyASTNode buildMyAST() {
		ITranslationUnit tu = null;
		CEditor cEditor = (CEditor) CDCEditor.getActiveCEditorChild(this);
		ICElement cElement = cEditor.getInputCElement();
		if(cElement instanceof ITranslationUnit) {
			tu = (ITranslationUnit) cElement;
			return buildMyAST(tu);
		} else {
			return null;
		}
	}
	
	private MyASTNode buildMyAST(ITranslationUnit tu) {
		IASTTranslationUnit aTu = null;
		ASTVisitor astVisitor = null;
		try {
			aTu = tu.getAST();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if(aTu instanceof CPPASTTranslationUnit) {
			astVisitor = new MyCPPASTVisitor(aTu);
		} else if(aTu instanceof CASTTranslationUnit) {
			astVisitor = new MyCASTVisitor(aTu);
		}
		aTu.accept(astVisitor);
		//IASTProblem[] prob = aTu.getPreprocessorProblems();
		//((IMyASTVisitor) astVisitor).addPreprocessorProblems(prob);
		IASTComment[] comments = aTu.getComments();
		((IMyASTVisitor) astVisitor).addComments(comments);
		IASTPreprocessorStatement[] prepStatement = aTu.getAllPreprocessorStatements();
		((IMyASTVisitor) astVisitor).addPreprocessorStatements(prepStatement);
		((IMyASTVisitor) astVisitor).reformAST();
		return ((IMyASTVisitor) astVisitor).getAST();
	}
	
	private MyASTNode[] buildMyASTLeaf() {
		if(myAST  == null) {
			return null;			
		}
		MyASTNode[] leafArray = null;
		Vector<MyASTNode> astLeaf = new Vector<MyASTNode>();
		Stack<MyASTNode> stack = new Stack<MyASTNode>();
		stack.push(myAST);
		while(!stack.empty()) {
			MyASTNode node = stack.pop();
			if(node.hasChildren()) {
				MyASTNode[] children = node.getChildren();
				for(int i=children.length-1; i>=0; i--) {
					stack.push(children[i]);
				}
			} else {
				astLeaf.add(node);
			}
		}
		leafArray = new MyASTNode[astLeaf.size()];
		System.arraycopy(astLeaf.toArray(), 0, leafArray, 0, astLeaf.size());
		for(int i=0; i<leafArray.length-1; i++) {
			System.out.println(leafArray[i].getData().getFileLocation().getNodeOffset() + " -> " + (leafArray[i].getData().getFileLocation().getNodeOffset()+leafArray[i].getData().getFileLocation().getNodeLength()) + leafArray[i].getData().getRawSignature());
		}
		return leafArray;
	}
}