package edu.pdx.svl.coDoc.cdc.referencemodel;

import java.io.File;
import java.util.Vector;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;

import edu.pdx.svl.coDoc.cdc.referencemodel.Reference;
import edu.pdx.svl.coDoc.cdc.Global;
import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;
import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;
import edu.pdx.svl.coDoc.cdc.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.cdc.referencemodel.ProjectReference;
import edu.pdx.svl.coDoc.cdc.referencemodel.SourceFileReference;
import edu.pdx.svl.coDoc.cdc.referencemodel.TextSelectionReference;
import edu.pdx.svl.coDoc.cdc.view.ConfirmationWindow;



public class References {
	private Vector<Reference> projects;
	
	private String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();
	
	public References() {
		projects = new Vector<Reference>();
//		createSampleData();
	}
	
	
	public void deleteReference(Reference refToDelete) {
		deleteReferenceRecursive(this.projects, refToDelete);
		SimpleXML.write(this);
		deleteProjectReference(refToDelete);
	}

	private void deleteReferenceRecursive(Vector<Reference>refs, Reference refToDelete) {
		for (Reference r: refs) {
			if (r == refToDelete) {
				refs.remove(r);
				return;
			}
			deleteReferenceRecursive(r.getChildrenList(), refToDelete);
		}
	}
	
	//takes care of getting rid of XML file if we are deleting a project reference
	//this only happens if the reference we are trying to delete is a project reference
	private void deleteProjectReference(Reference refToDelete) {
		if (refToDelete instanceof ProjectReference) {
			ProjectReference pr = (ProjectReference)refToDelete;
			SimpleXML.delete(pr);
		}
	}
	
	public Vector<Reference> getProjects() {
		return projects;
	}
	
	public void addProject() {
		ProjectReference proj = new ProjectReference();
		String projectName = CDCEditor.getActiveProjectName();
		String projectDirectory = CDCEditor.getActiveProjectDirectory();
		proj.setProjectDirectory(projectDirectory);
		proj.setProjectName(projectName);
		projects.add(proj);
	}
	
	public SourceFileReference findActiveSourceFileReference() {
		SourceFileReference matchingSourceFile = null;
		IEditorPart editorPart = CDCEditor.workbenchPart.getSite().getPage().getActiveEditor();
		String editorFileName = editorPart.getEditorInput().getName();
		
		for (Reference r : projects) {
			ProjectReference pr = (ProjectReference)r;
			Vector<Reference> allSourceFiles = pr.getChildrenList();
			for (Reference ref : allSourceFiles) {
				SourceFileReference sfr = (SourceFileReference)ref;
				String sourceFileName = sfr.getFileName();
				if (sourceFileName.equals(editorFileName)) {
					matchingSourceFile = sfr;
					break;
				}
			}
		}
		return matchingSourceFile;
	}
	
	private Vector<Reference> createTreeOfRefernecesOnlyApplyingToActiveSourceFile() {
		ProjectReference matchingProject = null;
		SourceFileReference matchingSourceFile = null;
		
		IEditorPart editorPart = CDCEditor.workbenchPart.getSite().getPage().getActiveEditor();
		String editorFileName = editorPart.getEditorInput().getName();
		
		for (Reference r : projects) {
			matchingProject = (ProjectReference)r;
			Vector<Reference> allSourceFiles = matchingProject.getChildrenList(); //it is actually a potentially matching project
			for (Reference ref : allSourceFiles) {
				SourceFileReference sfr = (SourceFileReference)ref;
				String sourceFileName = sfr.getFileName();
				if (sourceFileName.equals(editorFileName)) {
					matchingSourceFile = sfr;
					break;
				}
			}
		}
		
		ProjectReference newProjRef = matchingProject.copyWithNoChildren();
		newProjRef.getChildrenList().add(matchingSourceFile);
		
		Vector<Reference> newTree = new Vector<Reference>();
		newTree.add(newProjRef);
		
		return newTree;
	}
	
	public Vector<Reference> findReferencesContainingTextSelectionInActiveEditor(TextSelectionReference tsrInEditor) {
		Vector<Reference> matches = new Vector<Reference>();
		
		SourceFileReference activeSourceFile = findActiveSourceFileReference();
		Vector<Reference> tsrs = activeSourceFile.getChildrenList();
		for (Reference r: tsrs) {
			TextSelectionReference tsr = (TextSelectionReference)r;
			
			int tsrInEditorOffset = tsrInEditor.getOffset();
			int tsrInEditorLength = tsrInEditor.getLength();
			int tsrOffset = tsr.getOffset();
			int tsrLength = tsr.getLength();
			
			int tsrInEditorMinOffset = tsrInEditorOffset;
			int tsrInEditorMaxOffset = tsrInEditorOffset + tsrInEditorLength;
			int tsrMinOffset = tsrOffset;
			int tsrMaxOffset = tsrOffset + tsrLength;
			
			
			if ( 	(tsrInEditorMaxOffset >= tsrMinOffset && tsrInEditorMinOffset <= tsrMinOffset) ||
					(tsrMaxOffset >= tsrInEditorMinOffset && tsrMinOffset <= tsrInEditorMinOffset)) {
				matches.add(tsr);
			}
		}
		
		return matches;
	}
	
	public Vector<Reference> findSourceTextReferences(String searchString) {
		return findSourceTextReferences(projects, searchString);
	}
	public Vector<Reference> findSourceTextReferencesForActiveSourceFile(String searchString) {
		return findSourceTextReferences( createTreeOfRefernecesOnlyApplyingToActiveSourceFile(), searchString);
	}
	private Vector<Reference> findSourceTextReferences(Vector<Reference> projTree, String searchString) {
		Vector<Reference> matches = new Vector<Reference>();
		
		for (Reference r: projTree) {
			ProjectReference proj = (ProjectReference)r;
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference r3 : tsrs) {
					TextSelectionReference tsr = (TextSelectionReference)r3;
					if ( tsr.getText().contains(searchString) ) {
						matches.add(tsr);
					}
				}
			}
		}
		return matches;
	}
	
	
	
	public Vector<Reference> findSourceFileReferences(String searchString) {
		Vector<Reference> matches = new Vector<Reference>();
		
		for (Reference r: projects) {
			ProjectReference proj = (ProjectReference)r;
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				if (	sourceFileReference.getFileName().contains(searchString) ) {
					matches.add(sourceFileReference);
				}
			}
		}
		return matches;
	}
	
	public Vector<Reference> findProjectReferences(String searchString) {
		String comment;
		Vector<Reference> matches = new Vector<Reference>();
		Vector<Reference> projects = null;//((EntryEditor) CDCEditor.getActiveEntryEditor()).getDocument().getProjects();
		for (Reference r: projects) {
			ProjectReference proj = (ProjectReference)r;
			comment = proj.getComment();
			if (comment == null) {
				comment = "";
			}
			if (	proj.projectName.contains(searchString) ||
					proj.projectDirectory.contains(searchString) ) {
				matches.add(proj);
			}
		}
		return matches;
	}
	
	public Vector<Reference> findPDFFileReferences(String searchString) {
		return findPDFFileReferences(projects, searchString);
	}
	public Vector<Reference> findPDFFileReferencesForActiveSourceFile(String searchString) {
		return findPDFFileReferences(createTreeOfRefernecesOnlyApplyingToActiveSourceFile(), searchString);
	}
	private Vector<Reference> findPDFFileReferences(Vector<Reference> projRefs, String searchString) {
		Vector<Reference> matches = new Vector<Reference>();
		
		for (Reference r: projRefs) {
			ProjectReference proj = (ProjectReference)r;
			if (	proj.getPdfFile().getFileName().contains(searchString) ) {
				matches.add(proj);
			}
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				if (	sourceFileReference.getPdfFile().getFileName().contains(searchString) ) {
					matches.add(sourceFileReference);
				}
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference r3 : tsrs) {
					TextSelectionReference tsr = (TextSelectionReference)r3;
					if (	tsr.getPdfFile().getFileName().contains(searchString) ) {
						matches.add(tsr);
					}
				}
			}
		}
		return matches;
	}
	
	public Vector<Reference> findPDFTextReferences(String searchString) {
		return findPDFTextReferences(projects, searchString);
	}
	public Vector<Reference> findPDFTextReferencesForActiveSourceFile(String searchString) {
		return findPDFTextReferences(createTreeOfRefernecesOnlyApplyingToActiveSourceFile(), searchString);
	}
	private Vector<Reference> findPDFTextReferences(Vector<Reference> projRefs, String searchString) {
		Vector<Reference> matches = new Vector<Reference>();
		for (Reference r: projRefs) {
			ProjectReference proj = (ProjectReference)r;
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference r3 : tsrs) {
					TextSelectionReference tsr = (TextSelectionReference)r3;
					if ( true/*tsr.getPdfSelection().getText().contains(searchString)*/ ) {
						matches.add(tsr);
					}
				}
			}
		}
		return matches;
	}
	
	public Vector<Reference> findPDFPageReferences(String searchString) {
		return findPDFPageReferences(projects, searchString);
	}
	public Vector<Reference> findPDFPageReferencesForActiveSourceFile(String searchString) {
		return findPDFPageReferences(createTreeOfRefernecesOnlyApplyingToActiveSourceFile(), searchString);
	}
	private Vector<Reference> findPDFPageReferences(Vector<Reference> projRefs, String searchString) {
		Vector<Reference> matches = new Vector<Reference>();
		for (Reference r: projRefs) {
			ProjectReference proj = (ProjectReference)r;
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference r3 : tsrs) {
					TextSelectionReference tsr = (TextSelectionReference)r3;
					int pageNum = 0; //tsr.getPdfSelection().getPage();
					++pageNum;	//the stored page number works as an array. e.g., the first page is 0
					if (	Integer.valueOf(searchString).equals(pageNum) 	) {
						matches.add(tsr);
					}
				}
			}
		}
		return matches;
	}
	
	public Vector<Reference> findCommentReferences(String searchString) {
		return findCommentReferences(projects, searchString);
	}
	public Vector<Reference> findCommentReferencesForActiveSourceFile(String searchString) {
		return findCommentReferences(createTreeOfRefernecesOnlyApplyingToActiveSourceFile(), searchString);
	}
	private Vector<Reference> findCommentReferences(Vector<Reference> projRefs, String searchString) {
		String comment;
		Vector<Reference> matches = new Vector<Reference>();
		for (Reference r: projRefs) {
			ProjectReference proj = (ProjectReference)r;
			comment = proj.getComment();
			if (comment == null) {
				comment = "";
			}
			if (	comment.contains(searchString) ) {
				matches.add(proj);
			}
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				comment = sourceFileReference.getComment();
				if (comment == null) {
					comment = "";
				}
				if (	comment.contains(searchString) ) {
					matches.add(sourceFileReference);
				}
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference r3 : tsrs) {
					TextSelectionReference tsr = (TextSelectionReference)r3;
					comment = tsr.getComment();
					if (comment == null) {
						comment = "";
					}
					if (	comment.contains(searchString) ) {
						matches.add(tsr);
					}
				}
			}
		}
		return matches;
	}
	
	
	public Vector<Reference> findAllReferences(String searchString) {
		return findAllReferences(projects, searchString);
	}
	public Vector<Reference> findAllReferencesForActiveSourceFile(String searchString) {
		return findAllReferences(createTreeOfRefernecesOnlyApplyingToActiveSourceFile(), searchString);
	}
	private Vector<Reference> findAllReferences(Vector<Reference> projRefs, String searchString) {
		String comment;
		Vector<Reference> matches = new Vector<Reference>();
		for (Reference r: projRefs) {
			ProjectReference proj = (ProjectReference)r;
			comment = proj.getComment();
			if (comment == null) {
				comment = "";
			}
			if (	proj.projectName.contains(searchString) ||
					proj.projectDirectory.contains(searchString) ||
					proj.getPdfFile().getFileName().contains(searchString) ||
					comment.contains(searchString) ) {
				matches.add(proj);
			}
			Vector<Reference> fileRefs = proj.getChildrenList();
			for(Reference ref : fileRefs) {
				SourceFileReference sourceFileReference = (SourceFileReference)ref;
				comment = sourceFileReference.getComment();
				if (comment == null) {
					comment = "";
				}
				if (	sourceFileReference.getFileName().contains(searchString) ||
						comment.contains(searchString) ||
						sourceFileReference.getPdfFile().getFileName().contains(searchString) ) {
					matches.add(sourceFileReference);
				}
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference r3 : tsrs) {
					TextSelectionReference tsr = (TextSelectionReference)r3;
					comment = tsr.getComment();
					if (comment == null) {
						comment = "";
					}
					int pageNum = 0; //tsr.getPdfSelection().getPage();
					++pageNum;	//the stored page number works as an array. e.g., the first page is 0.
					String pageNumStr = String.valueOf(pageNum);
					if (	tsr.getText().contains(searchString) ||
							comment.contains(searchString) ||
							tsr.getPdfFile().getFileName().contains(searchString) ||
							//tsr.getPdfSelection().getText().contains(searchString) ||
							pageNumStr.contains(searchString) 	) {
						matches.add(tsr);
					}
				}
			}
		}
		return matches;
	}
	
	
	public void resourceDeltaMoveProject(IPath oldP, IPath newP) {
		String oldPath = workspacePath + oldP.toFile().getPath() + "\\";
		String newPath = workspacePath + newP.toFile().getPath() + "\\";
		String newName = newP.toFile().getName();
		
		for (Reference r : projects) {
			ProjectReference project = (ProjectReference)r;
			String projectDirectory = project.getProjectDirectory();
			if (projectDirectory.equals(oldPath)) {
				project.setProjectDirectory(newPath);
				project.setProjectName(newName);
				SimpleXML.write(this);
				return;
			}
		}
	}
	
	public void resourceDeltaRemoveProject(IPath projToDel) {
		String projectToDelete = workspacePath + projToDel.toFile().getPath() + "\\";
		
		for (Reference r : projects) {
			ProjectReference project = (ProjectReference)r;
			String projectDirectory = project.getProjectDirectory();
			if (projectDirectory.equals(projectToDelete)) {
				deleteReference(project);
				return;
			}
		}
	}
	
	public void resourceDeltaMoveFile(IPath oldF, IPath newF) {
		File oldFile = oldF.toFile();
		File newFile = newF.toFile();
		
		String oldPath = workspacePath + oldFile.getPath();
		String newPath = workspacePath + newFile.getPath();
		String newName = newFile.getName();
		
		String oldFileProjDir = oldFile.getParent().substring(1); //we dont want the extra / in the beginning of the string
		String newFileProjDir = newFile.getParent().substring(1);
		
		if (oldFileProjDir.equals(newFileProjDir)) {
			
			for (Reference proj : projects) {
				Vector<Reference> files = proj.getChildrenList();
				for (Reference f : files) {
					SourceFileReference sfr = (SourceFileReference)f;
					if (sfr.getFilePath().equals(oldPath)) {
						sfr.setFileName(newName);
						sfr.setFilePath(newPath);
						SimpleXML.write(this);
						return;
					}
				}
			}
			
		} else { //in this case, we are moving the file to a different project
			
			//first, we rename the file name itself (files can be renamed and moved at once...)
			for (Reference r : projects) {
				Vector<Reference> files = r.getChildrenList();
				for (Reference f : files) {
					SourceFileReference sfr = (SourceFileReference)f;
					if (sfr.getFilePath().equals(oldPath)) {
						sfr.setFileName(newName);
						sfr.setFilePath(newPath);
						
						//Now, we take this file out of the children of the old project it was in.
						files.remove(sfr);
						
						//Now, we add the file to the project it will be in.
						for (Reference r2 : projects) {
							ProjectReference pRef = (ProjectReference)r2;
							String pRefName = pRef.getProjectName();
							if (pRefName.equals(newFileProjDir)) {
								pRef.getChildrenList().add(sfr);
								sfr.setProjectReference(pRef);
								SimpleXML.write(this);
								return;
							}
						}
						
						//If we are here, then a ProjectReference has not yet been made
						ProjectReference newProjRef = new ProjectReference();
						newProjRef.setPdfFile(sfr.getPdfFile());
						newProjRef.setProjectName(newFileProjDir); //newFileProjDir is not the complete path, just the project name
						newProjRef.setProjectDirectory(newFile.getParentFile().getAbsolutePath());
						newProjRef.getChildrenList().add(sfr);
						projects.add(newProjRef);
						
						SimpleXML.write(this);
						return;
					}
				}
			}
		}
	}
	
	public void resourceDeltaRemoveFile(IPath fileToDel) {
		String fileToDelete = workspacePath + fileToDel.toFile().getPath();
		
		for (Reference proj : projects) {
			Vector<Reference> files = proj.getChildrenList();
			for (Reference f : files) {
				SourceFileReference sfr = (SourceFileReference)f;
				String sfrPath = sfr.getFilePath();
				if (sfrPath.equals(fileToDelete)) {
					deleteReference(sfr);
					return;
				}
			}
		}
	}
	
	/*
	 * TEST CODE
	 */
	
	public void printTree() {
		System.out.println("==TREE==");
		for (Reference pr : projects) {
			System.out.println("ProjectReference: " + pr.description());
			Vector<Reference> filesList = pr.getChildrenList();
			for (Reference r : filesList) {
				System.out.println(" SourceFileReference: " + r.description());
				Vector<Reference> textSelectionList = r.getChildrenList();
				for (Reference r2 : textSelectionList) {
					System.out.println("  TextSelectionReference: " + r2.description());
				}
			}
		}
		System.out.println("==END_TREE==");
	}
	
	public void createSampleData() {
		PDFFile f = new PDFFile(new File("C:\\simple.pdf:"));
		
		ProjectReference pr1 = new ProjectReference();
		ProjectReference pr2 = new ProjectReference();
		SourceFileReference sfr1 = new SourceFileReference();
		SourceFileReference sfr2 = new SourceFileReference();
		TextSelectionReference tsr1 = new TextSelectionReference();
		TextSelectionReference tsr2 = new TextSelectionReference();
		
		pr1.setComment("project1 comment");
		pr1.setPdfFile(new PDFFile());
		pr1.setProjectDirectory("D:\\dir1\\dir2");
		pr1.setProjectName("Project1");
		
		pr2.setComment("project2 comment");
		pr2.setPdfFile(f);
		pr2.setProjectDirectory("D:\\dir3\\dir4");
		pr2.setProjectName("Project2");
		
		sfr1.setPdfFile(new PDFFile());
		sfr1.setComment("source file 1 comment");
		sfr1.setFileName("Something1.mc");
		sfr1.setFilePath("D:\\dir1\\dir2\\Something1.mc");
		
		sfr2.setPdfFile(f);
		sfr2.setComment("source file 2 comment");
		sfr2.setFileName("Something2.mc");
		sfr2.setFilePath("D:\\dir3\\dir4\\Something2.mc");
		
		tsr1.setPdfFile(new PDFFile());
		tsr1.setText("text selection 1");
		tsr1.setComment("text selection 1 comment");
		tsr1.setOffset(1);
		tsr1.setLength(2);
		
		tsr2.setPdfFile(f);
		tsr2.setText("text selection 2");
		tsr2.setComment("text selection 2 comment");
		tsr2.setOffset(3);
		tsr2.setLength(4);
		
		pr1.getChildrenList().add(sfr1);
		pr2.getChildrenList().add(sfr2);
		
		sfr1.getChildrenList().add(tsr1);
		sfr2.getChildrenList().add(tsr2);
		
		projects.add(pr1);
		projects.add(pr2);
	}
	
	
}
