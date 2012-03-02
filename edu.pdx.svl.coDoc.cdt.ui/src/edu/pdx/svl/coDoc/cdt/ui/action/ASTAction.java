package edu.pdx.svl.coDoc.cdt.ui.action;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.DOMSearchUtil;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;

public class ASTAction implements IEditorActionDelegate {

	IEditorPart editor;

	public ASTAction() {
	}

	public void run(IAction action) {
		IFile inputFile = ((FileEditorInput) editor.getEditorInput()).getFile();
		BBASTVisitor astVisitor = new BBASTVisitor();
		BBPDOMVisitor pdomVisitor = new BBPDOMVisitor();

		if (CoreModel.isTranslationUnit(inputFile)) {
			try {
				// Get the TranslationUnit and use the BBASTVisitor to search
				// through it
				TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault()
						.getCoreModel().create(inputFile);
				IASTTranslationUnit ast = tu.getLanguage()
						.getASTTranslationUnit(tu,
								ILanguage.AST_SKIP_ALL_HEADERS);
				ast.accept(astVisitor);

				ICProject project = tu.getCProject();
				CCorePlugin.getPDOMManager().getIndexer(project).reindex();
				IPDOM pdom = CCorePlugin.getPDOMManager().getPDOM(project);
				pdom.accept(pdomVisitor);

			} catch (CoreException e) {
				e.printStackTrace();
			}
			IWorkbenchWindow window = editor.getEditorSite()
					.getWorkbenchWindow();
			String output = astVisitor.getText() + "\n"
					+ pdomVisitor.getOutput();
			MessageDialog mb = new MessageDialog(window.getShell(),
					"Analysis Result", null, output, MessageDialog.INFORMATION,
					new String[] { "OK" }, 0);
			mb.open();
		}
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.editor = targetEditor;
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}
