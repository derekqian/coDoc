package edu.pdx.svl.coDoc.refexp.popup.actions;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


//import edu.pdx.svl.coDoc.refexp.editorcontextmenu.editors.SelectionAndCursor;

import edu.pdx.svl.coDoc.refexp.Global;
import edu.pdx.svl.coDoc.refexp.referenceexplorer.*;
import edu.pdx.svl.coDoc.refexp.referencemodel.*;
import edu.pdx.svl.coDoc.refexp.view.ConfirmationWindow;



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
		TreeViewer treeViewer = Global.INSTANCE.referenceExplorerView.getTreeViewer();
		ISelection selection = treeViewer.getSelection();
		
		if (selection != null && selection instanceof IStructuredSelection) {
			References refs = Global.INSTANCE.references;
			IStructuredSelection sel = (IStructuredSelection) selection;
			
			for (Iterator<Reference> iterator = sel.iterator(); iterator.hasNext();) {
				Reference refToDelete = iterator.next();
				refs.deleteReference(refToDelete);
				
			}
			treeViewer.refresh();
			
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
