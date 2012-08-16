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
package edu.pdx.svl.coDoc.poppler.handler;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.eclipse.ui.part.AbstractMultiEditor;

import edu.pdx.svl.coDoc.poppler.editor.PDFEditor;
import edu.pdx.svl.coDoc.poppler.editor.PDFPageViewer;

public class PageSelectCombo extends WorkbenchWindowControlContribution {
	private static PageSelectCombo instance = null;
	private Combo combo;
	private boolean indirect = false;
	
	public static PageSelectCombo getInstance() {
		return instance;
	}
	
	public PageSelectCombo() {
	}
	
	@Override
	protected Control createControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout(1, false);
	    layout.marginTop = -1;
	    layout.marginHeight = 0;
	    layout.marginWidth = 0;
	    container.setLayout(layout);
	    GridData grid = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
	    grid.widthHint = 60;
	    //grid.minimumWidth = 100;
	    combo = new Combo(container, SWT.BORDER | SWT.READ_ONLY | SWT.DROP_DOWN);
		combo.setTextLimit(5);
	    combo.setLayoutData(grid);
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(!indirect) {
					IEditorPart editor = null;
					PDFPageViewer pageviewer = null;
					IWorkbench workbench = PlatformUI.getWorkbench();
					if(workbench == null) return;
					IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
					if(workbenchwindow == null) return;
					IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
					if(workbenchPage == null) return;
					IEditorPart activeeditor = workbenchPage.getActiveEditor();
					if(activeeditor == null) return;
					if(activeeditor instanceof AbstractMultiEditor) {
						editor = activeeditor;
					} else {
						// check if it's the child of EntryEditor
						IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
						for(IEditorReference er : editorrefs) {
							IEditorPart innerEditors[] = ((AbstractMultiEditor) er.getEditor(false)).getInnerEditors();
							for(IEditorPart ep : innerEditors) {
								if(ep == activeeditor) {
									editor = er.getEditor(false);
									break;
								}
							}
							if(editor != null) {
								break;
							}
						}
					}
					if(editor == null) return;
					
					IEditorPart innerEditors[] = ((AbstractMultiEditor) editor).getInnerEditors();
					for(IEditorPart ep : innerEditors) {
						if(ep instanceof PDFEditor) {
							editor = ep;
							break;
						}
					}
					
					pageviewer = ((PDFEditor) editor).getPDFPageViewer();
					int page = combo.getSelectionIndex() + 1;
					pageviewer.showPage(page);
				}
			}
		});
		
		instance = this;
	
	    return container;
	}
	
	public void add(String string) {
		combo.add(string);
	}
	public void removeAll() {
		combo.removeAll();
	}
	
	/**
	 * Only change the selection display. This action will not trigger the actions defined in the Listener.
	 * @param index
	 */
	public void select(int index) {
		indirect = true;
		combo.select(index);
		indirect = false;
	}
	
	public void redraw() {
		combo.redraw();
	}
	
	//@Override
	//protected int computeWidth(Control control) {
	//    return 300;
	//}
}

/*
// for RCP
public class PageSelectComboToolbarContribution extends ControlContribution {
	Combo combo;

	public PageSelectComboToolbarContribution(String str) {
	super(str);
	}

	@Override
	protected Control createControl(Composite parent) {
		combo = new Combo(parent, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.add("String 1");
		combo.add("String 2");
		combo.add("String 3");
		combo.add("String 4");
		combo.setTextLimit(10);
		combo.select(0);
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				MessageDialog.openInformation(null, "My App", "Item at " + combo.getSelectionIndex() + " clicked.");
			}
		});
		return combo;
	}

	public void setValue(int index) {
		combo.select(index);
	}
}*/