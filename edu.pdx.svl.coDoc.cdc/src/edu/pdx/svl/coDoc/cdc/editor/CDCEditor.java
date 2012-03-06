package edu.pdx.svl.coDoc.cdc.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


public class CDCEditor implements IEditorLauncher
{

	public CDCEditor() {
		super();
	}

	@Override
	public void open(IPath path) 
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceroot = workspace.getRoot();
		
		// change path to relative
		IPath workspacerootpath = workspaceroot.getLocation();
		path = path.makeRelativeTo(workspacerootpath);
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
		//IWorkbenchPage workbenchPage = getEditorSite().getPage();
		//IEditorReference[] editorrefs = workbenchPage.getEditorReferences();
		IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
		if(editorrefs.length == 0)
		{
			// first time
			if(path.getFileExtension().equals("cdc"))
			{
				IPath codepath = new Path("/test/code/example.c");
				IFile codefile = (IFile) workspaceroot.getFile(codepath);
				final FileEditorInput codeEditorInput = new FileEditorInput(codefile);
				IPath specpath = new Path("/test/spec/sample.pdf");
				IFile specfile = (IFile) workspaceroot.getFile(specpath);
				final FileEditorInput specEditorInput = new FileEditorInput(specfile);
				
				String editorID[] = {"edu.pdx.svl.coDoc.cdt.ui.editor.CEditor","edu.pdx.svl.coDoc.poppler.editor.PDFEditor"};
				IEditorInput editorInput[] = {codeEditorInput,specEditorInput};
				final EntryEditorInput entryEditorInput = new EntryEditorInput(editorID, editorInput);
				try 
				{
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
			}
			else if(path.getFileExtension().equals("pdf"))
			{
				IFile specfile = (IFile) workspaceroot.getFile(path);
				final FileEditorInput specEditorInput = new FileEditorInput(specfile);
				
				String editorID[] = {"edu.pdx.svl.coDoc.poppler.editor.PDFEditor"};
				IEditorInput editorInput[] = {specEditorInput};
				final EntryEditorInput entryEditorInput = new EntryEditorInput(editorID, editorInput);
				try 
				{
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				IFile codefile = (IFile) workspaceroot.getFile(path);
				final FileEditorInput codeEditorInput = new FileEditorInput(codefile);
				
				String editorID[] = {"edu.pdx.svl.coDoc.cdt.ui.editor.CEditor"};
				IEditorInput editorInput[] = {codeEditorInput};
				final EntryEditorInput entryEditorInput = new EntryEditorInput(editorID, editorInput);
				try 
				{
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			// already open
			IEditorPart editor = editorrefs[0].getEditor(false);
			
			workbenchPage.bringToTop(editor);
			
			if(path.getFileExtension().equals("cdc"))
			{
				IPath codepath = new Path("/test/code/example.c");
				IFile codefile = (IFile) workspaceroot.getFile(codepath);
				final FileEditorInput codeEditorInput = new FileEditorInput(codefile);
				IPath specpath = new Path("/test/spec/sample.pdf");
				IFile specfile = (IFile) workspaceroot.getFile(specpath);
				final FileEditorInput specEditorInput = new FileEditorInput(specfile);
				
				String editorID[] = {"edu.pdx.svl.coDoc.cdt.ui.editor.CEditor","edu.pdx.svl.coDoc.poppler.editor.PDFEditor"};
				IEditorInput editorInput[] = {codeEditorInput,specEditorInput};
				final EntryEditorInput entryEditorInput = new EntryEditorInput(editorID, editorInput);
				try 
				{
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
			}
			else if(path.getFileExtension().equals("pdf"))
			{
				IPath specpath = new Path("/test/spec/sample.pdf");
				IFile specfile = (IFile) workspaceroot.getFile(specpath);
				final FileEditorInput specEditorInput = new FileEditorInput(specfile);
				
				String editorID[] = {"edu.pdx.svl.coDoc.poppler.editor.PDFEditor"};
				IEditorInput editorInput[] = {specEditorInput};
				final EntryEditorInput entryEditorInput = new EntryEditorInput(editorID, editorInput);
				try 
				{
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				IPath codepath = new Path("/test/code/example.c");
				IFile codefile = (IFile) workspaceroot.getFile(codepath);
				final FileEditorInput codeEditorInput = new FileEditorInput(codefile);
				
				String editorID[] = {"edu.pdx.svl.coDoc.cdt.ui.editor.CEditor"};
				IEditorInput editorInput[] = {codeEditorInput};
				final EntryEditorInput entryEditorInput = new EntryEditorInput(editorID, editorInput);
				try 
				{
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				} 
				catch (PartInitException e) 
				{
					e.printStackTrace();
				}
			}

		 /* IEditorPart editor = workbenchPage.findEditor(entryEditorInput);
			if(editor != null)
			{
				workbenchPage.bringToTop(editor);
			}
			else
			{
				try
				{
					//workbenchPage.openEditor(codeEditorInput, "edu.pdx.svl.coDoc.cdt.ui.editor.CEditor");
					//workbenchPage.openEditor(specEditorInput, "edu.pdx.svl.coDoc.poppler.editor.PDFEditor");
					workbenchPage.openEditor(entryEditorInput, "edu.pdx.svl.coDoc.cdc.editor.EntryEditor");
				}
				catch(PartInitException e2)
				{
					e2.printStackTrace();
				}
			} */
		}
	}
}
