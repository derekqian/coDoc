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
package edu.pdx.svl.coDoc.editors;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;


import edu.pdx.svl.coDoc.poppler.OutlineNode;
import edu.pdx.svl.coDoc.poppler.PDFDestination;
import edu.pdx.svl.coDoc.poppler.GoToAction;
import edu.pdx.svl.coDoc.poppler.PDFAction;

import edu.pdx.svl.coDoc.editors.PDFPageViewer.IPDFEditor;

/**
 * A simple outline, which directly uses the outline of the pdf file.
 * 
 * @author Boris von Loesch
 *
 */
public class PDFFileOutline extends ContentOutlinePage {
	
	private OutlineNode input;
	private IPDFEditor editor;
	
	public PDFFileOutline(IPDFEditor editor) {
		super();
		this.editor = editor;
	}
	
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new PDFFileContentProvider());
		viewer.setLabelProvider(new PDFFileLabelProvider());
		//viewer.addSelectionChangedListener(this);
		if (input != null) viewer.setInput(input);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// if the selection is empty do nothing
				if(event.getSelection().isEmpty()) {
					return;
				}
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
					OutlineNode domain = (OutlineNode) iterator.next();

					PDFAction action = domain.getAction();
					if (action instanceof GoToAction) {
						PDFDestination dest = ((GoToAction) action).getDestination();
						if (dest == null) return;
						editor.gotoAction(dest);
					}
				}			       
			}
		});
	}

	public void setInput(OutlineNode n) {
		input = n;
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			viewer.setInput(input);
		}
	}
}
