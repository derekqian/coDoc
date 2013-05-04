package edu.pdx.svl.coDoc.cdc.editor;

import java.util.*;

import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.ui.editor.CEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class RPE {
	public static void main(String[] args) {
		Set<String> firstLvlNodes = new HashSet<String>();
		
		CEditor cEditor = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceroot = workspace.getRoot();
		IPath codefilepath = new Path("/e100/code/eepro100.c");
		//IPath codefilepath = new Path(codefilename2);
		//codefilepath = codefilepath.removeFirstSegments(1); // remove "project:"
		IFile codefile = (IFile) workspaceroot.getFile(codefilepath);
		final FileEditorInput codeEditorInput = new FileEditorInput(codefile);
		try 
		{
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
			cEditor = (CEditor) workbenchPage.openEditor(codeEditorInput, "org.eclipse.cdt.ui.editor.CEditor");
		} 
		catch (PartInitException e) 
		{
			e.printStackTrace();
		}
		ITranslationUnit tu = (ITranslationUnit) cEditor.getInputCElement();
		MyASTTree myASTTree =  new MyASTTree(tu);
		MyASTNode myAST = myASTTree.getTree();
		
		for(MyASTNode node : myAST.getChildren()) {
			firstLvlNodes.add(node.toString());
		}
		// [CASTSimpleDeclaration, CASTFunctionDefinition, ASTFunctionStyleMacroDefinition, ASTElse, ASTInclusionStatement, CASTProblemDeclaration, ASTEndif, ASTMacroDefinition, ASTIfdef, ASTIf, ASTComment]
		System.out.println(firstLvlNodes);
		return;
	}
}
