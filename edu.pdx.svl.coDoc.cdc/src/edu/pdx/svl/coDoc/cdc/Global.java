package edu.pdx.svl.coDoc.cdc;

import java.io.File;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;

import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;
import edu.pdx.svl.coDoc.cdc.test.TestModel;



public enum Global {
	INSTANCE;
	
	public IWorkbenchPart workbenchPart;
	public EntryEditor entryEditor;
	public FileEditorInput activeFileEditorInput;
	public IReferenceExplorer referenceExplorerView;
	
	public TestModel testModel = new TestModel();
	
	Global() {
		workbenchPart = null;
		entryEditor = null;
		activeFileEditorInput = null;
		referenceExplorerView = null;
	}

	
	/*
	 * This only gets you the name of the first project.
	 */
	public static String getProjectName() {
		String name = null;
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		if (projects.length != 0) {
			IProject proj = projects[0];
			name = proj.getName();
			return name;
		} 
		return "no saved references in workspace";
	}
	
	public String getActiveProjectName(){
		String dir = null;
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		if (projects.length != 0) {
			IFile file = activeFileEditorInput.getFile();
			IProject project = file.getProject();
			String activeProjectName = project.getName();
			return activeProjectName;
		} else {
			return "";
		}
	}
	
	/*
	 * This give you the directory of the first project.
	 * 
	 * This is to be replaced by getActiveProjectDirectory()
	 */
	public static String getProjectDirectory() {
		String dir = null;
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		if (projects.length != 0) {
			IProject proj = projects[0];
			IPath path = proj.getFullPath();
			File filePath = path.toFile();
			String relativeDir = filePath.getPath() + '\\';
			String workspaceDir = workspaceRoot.getLocation().toFile().getAbsolutePath();
			dir = workspaceDir + relativeDir;
			return dir;
		} else {
			return "";
		}
	}
	
	public String getActiveProjectDirectory() {
		String dir = null;
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		if (projects.length != 0) {
			IFile file = activeFileEditorInput.getFile();
			IProject project = file.getProject();
			IPath path = project.getFullPath();
			File pathAsFile = path.toFile();
			String relativeDir = pathAsFile.getPath() + '\\';
			String workspaceDir = workspaceRoot.getLocation().toFile().getAbsolutePath();
			dir = workspaceDir + relativeDir;
			return dir;
		} else {
			return "";
		}
		
	}
	

}
