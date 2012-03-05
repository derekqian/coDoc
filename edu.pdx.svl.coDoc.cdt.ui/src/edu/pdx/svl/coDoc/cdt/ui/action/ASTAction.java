package edu.pdx.svl.coDoc.cdt.ui.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.DOMSearchUtil;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.core.model.IInclude;
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
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
				// Create dot file
				astVisitor.writeHead("digraph ast {\n");
				
				// Get the TranslationUnit and use the BBASTVisitor to search
				// through it
				TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault()
						.getCoreModel().create(inputFile);
				IASTTranslationUnit ast = tu.getLanguage()
						.getASTTranslationUnit(tu,
								ILanguage.AST_SKIP_IF_NO_BUILD_INFO);
				ast.accept(astVisitor);
				
				astVisitor.writeTail("}\n");
				
				//IASTPreprocessorIncludeStatement[] inc = ast.getIncludeDirectives();
				//IInclude[] include = tu.getIncludes();

				ICProject project = tu.getCProject();
				CCorePlugin.getPDOMManager().getIndexer(project).reindex();
				IPDOM pdom = CCorePlugin.getPDOMManager().getPDOM(project);
				pdom.accept(pdomVisitor);
				
				try {
					Runtime.getRuntime().exec("dot -Grankdir=TB -Gratio=0.7727 -Tps -o /home/derek/Research/ast.ps /home/derek/Research/ast.dot");
					Runtime.getRuntime().exec("evince -w /home/derek/Research/ast.ps");
					/*Runtime.getRuntime().exec("ps2pdf /home/derek/Research/ast.ps /home/derek/Research/ast.pdf");
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					IWorkspaceRoot workspaceroot = workspace.getRoot();
					//IFile specfile = (IFile) workspaceroot.getFile("/home/derek/Research/ast.ps");
					//File specfile = new File("/home/derek/Research/ast.ps");
					IPath path = new Path("/home/derek/Research/ast.pdf");
					IFile specfile = (IFile) workspaceroot.getFile(path);
					final FileEditorInput specEditorInput = new FileEditorInput(specfile);
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
					IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
					try 
					{
						workbenchPage.openEditor(specEditorInput, "edu.pdx.svl.coDoc.poppler.editor.PDFEditor");
					} 
					catch (PartInitException e) 
					{
						e.printStackTrace();
					}*/
				} catch (IOException e) {
					e.printStackTrace();
				}
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
