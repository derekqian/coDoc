package edu.pdx.svl.coDoc.cdc.popup.actions;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


//import edu.pdx.svl.coDoc.refexp.editorcontextmenu.editors.SelectionAndCursor;

import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.referencemodel.Reference;
import edu.pdx.svl.coDoc.cdc.referencemodel.References;
import edu.pdx.svl.coDoc.cdc.Global;



public class DeleteReference implements IObjectActionDelegate {

	private Shell shell;
	
	
	/**
	 * Constructor for Action1.
	 */
	public DeleteReference() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
		EntryEditor editor = (EntryEditor)workbenchPage.getActiveEditor();
		IViewPart view = workbenchPage.findView("");
		ISelection selection = view.getViewSite().getSelectionProvider().getSelection();
		//TreeViewer treeViewer = Global.INSTANCE.referenceExplorerView.getTreeViewer();
		//ISelection selection = treeViewer.getSelection();
		
		if (selection != null && selection instanceof IStructuredSelection) {
			References refs = Global.INSTANCE.entryEditor.getDocument();
			IStructuredSelection sel = (IStructuredSelection) selection;
			
			for (Iterator<Reference> iterator = sel.iterator(); iterator.hasNext();) {
				Reference refToDelete = iterator.next();
				refs.deleteReference(refToDelete);
				
			}
			((ISelectionListener)view).selectionChanged(editor, new TextSelection(0,0));
			//view.setInput();
			//view.refresh();
			
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
