package edu.pdx.svl.coDoc.cdc.editor;

import org.eclipse.jface.viewers.ISelection;

import edu.pdx.svl.coDoc.cdc.referencemodel.References;

public interface IReferenceExplorer
{
	public void refresh();
	public ISelection getSelection();
	public void displayListOfTextSelectionReferencesForSelectionInActiveEditor();
	public String getProjectName();
}
