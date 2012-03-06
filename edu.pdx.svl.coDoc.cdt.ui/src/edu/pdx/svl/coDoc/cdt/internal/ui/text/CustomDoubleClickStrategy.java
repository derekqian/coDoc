/*******************************************************************************
 * Copyright (c) 2002, 2004 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX Software Systems - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.ui.text;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;

/**
 * CustomDoubleClickStrategy
 */
//public class CustomDoubleClickStrategy implements ITextDoubleClickStrategy {
public class CustomDoubleClickStrategy extends DefaultTextDoubleClickStrategy {
	private IEditorPart editor;
	
	@Override
	public void doubleClicked(final ITextViewer viewer) {
		super.doubleClicked(viewer);
		// get doc
		IDocument doc = viewer.getDocument();
		
		// get double click position
		final int offset = viewer.getSelectedRange().x;
		try {
			if((doc.getChar(offset) == '\n') && (offset != 0)){
				//offset--;
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
		editor = workbenchPage.getActiveEditor();
		IFile inputFile = ((FileEditorInput) editor.getEditorInput()).getFile();
		try {
			TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault().getCoreModel().create(inputFile);
			IASTTranslationUnit ast = tu.getLanguage().getASTTranslationUnit(tu,
					ILanguage.AST_SKIP_IF_NO_BUILD_INFO);
			ast.accept(new ASTVisitor(){
				{
					shouldVisitDeclarations = true;
				}
				public int visit(IASTDeclaration declaration) {
					// select double clicked token
					if((offset >= ((ASTNode)declaration).getOffset())&&(offset < ((ASTNode)declaration).getOffset()+((ASTNode)declaration).getLength()))
					{
						//viewer.setTextColor(new Color(null, 255, 0, 0), ((ASTNode)declaration).getOffset(), ((ASTNode)declaration).getLength(), true);
						TextPresentation presentation = new TextPresentation();
						TextAttribute attr = new TextAttribute(new Color(null, 0, 0, 0),
							      new Color(null, 128, 128, 128), TextAttribute.STRIKETHROUGH);
						presentation.addStyleRange(new StyleRange(((ASTNode)declaration).getOffset(), ((ASTNode)declaration).getLength(), attr.getForeground(),
							      attr.getBackground()));
						viewer.changeTextPresentation(presentation, true);
						//viewer.setSelectedRange(((ASTNode)declaration).getOffset(), ((ASTNode)declaration).getLength());
					}
						
					return PROCESS_CONTINUE;
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
