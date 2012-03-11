package edu.pdx.svl.coDoc.cdc.referencemodel;

import java.util.Vector;

import org.eclipse.ui.IEditorPart;

import edu.pdx.svl.coDoc.cdc.Global;


public class ReferencesForActiveSourceFile {
	private Vector<Reference> allProjects;
	private SourceFileReference activeSourceFile;
	private Vector<Reference> referencesInActiveSourceFile;
	
	public ReferencesForActiveSourceFile() {
		allProjects = Global.INSTANCE.entryEditor.getDocument().getProjects();
		IEditorPart editorPart = Global.INSTANCE.workbenchPart.getSite().getPage().getActiveEditor();
		String editorFileName = editorPart.getEditorInput().getName();
		for (Reference r : allProjects) {
			Vector<Reference> sfrs = r.getChildrenList();
			for (Reference ref : sfrs) {
				SourceFileReference sfr = (SourceFileReference)ref;
				if (sfr.getFileName().equals(editorFileName)) {
					activeSourceFile = sfr;
					referencesInActiveSourceFile = activeSourceFile.getChildrenList();
				}
			}
		}
	}
	
	
	
	
	public Vector<Reference> getAllProjects() {
		return allProjects;
	}
	public void setAllProjects(Vector<Reference> allProjects) {
		this.allProjects = allProjects;
	}
	public SourceFileReference getActiveSourceFile() {
		return activeSourceFile;
	}
	public void setActiveSourceFile(SourceFileReference activeSourceFile) {
		this.activeSourceFile = activeSourceFile;
	}
	public Vector<Reference> getReferencesInActiveSourceFile() {
		return referencesInActiveSourceFile;
	}
	public void setReferencesInActiveSourceFile(
			Vector<Reference> referencesInActiveSourceFile) {
		this.referencesInActiveSourceFile = referencesInActiveSourceFile;
	}
}
