package edu.pdx.svl.coDoc.refexp;

import java.io.File;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;

import edu.pdx.svl.coDoc.refexp.XML.SimpleXML;
import edu.pdx.svl.coDoc.refexp.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.refexp.referenceexplorer.ReferenceExplorerView;
import edu.pdx.svl.coDoc.refexp.referencemodel.References;
import edu.pdx.svl.coDoc.refexp.referencemodel.TextSelectionReference;
import edu.pdx.svl.coDoc.refexp.test.TestModel;
import edu.pdx.svl.coDoc.refexp.test.TestModelItem;



public enum Global {
	INSTANCE;
	
	public IWorkbenchPart workbenchPart;
	public References references;
	public ReferenceExplorerView referenceExplorerView;
	public FileEditorInput activeFileEditorInput;
	
	public TestModel testModel = new TestModel();
	
	private Global() {
		references = SimpleXML.read();
		if (references == null) {
			references = new References();
			references.addProject();
		}
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
