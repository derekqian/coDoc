/*******************************************************************************
 * Copyright (c) 2011 Boris von Loesch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Boris von Loesch - initial API and implementation
 ******************************************************************************/
package edu.pdx.svl.coDoc.cdc.handles;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.pdx.svl.coDoc.cdc.Global;
import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;
import edu.pdx.svl.coDoc.cdc.referencemodel.Reference;
import edu.pdx.svl.coDoc.cdc.referencemodel.References;
import edu.pdx.svl.coDoc.cdc.view.EditView;


/**
 * Triggers a forward search in all open pdf editors. Opens the first
 * editor for which the search was successful.
 * 
 * @author Boris von Loesch
 *
 */
public class EditReference extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean refNotSelected = true;
		// IEditorPart e = HandlerUtil.getActiveEditor(event);
		EntryEditor editor = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
		IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
		if(editorrefs.length != 0)
		{
			editor = (EntryEditor) editorrefs[0].getEditor(false);
		}
		IReferenceExplorer view = (IReferenceExplorer)editor.getSite().getPage().findView("edu.pdx.svl.coDoc.refexp.referenceexplorer.ReferenceExplorerView");
		ISelection selection = view.getSelection();
		
		if (selection != null && selection instanceof IStructuredSelection) {
			References refs = Global.INSTANCE.entryEditor.getDocument();
			IStructuredSelection sel = (IStructuredSelection) selection;
			
			for (Iterator<Reference> iterator = sel.iterator(); iterator.hasNext();) {
				Reference refToEdit = iterator.next();
				refNotSelected = false;
				(new EditView(new Shell(), refToEdit)).open();
			}
			view.setInput(refs);
			view.refresh();
			
		}
		if (refNotSelected == true) {
			MessageDialog.openError(null, "Alert",  "You must select a reference to be able to edit it!");
		}
		return null;
	}
}
