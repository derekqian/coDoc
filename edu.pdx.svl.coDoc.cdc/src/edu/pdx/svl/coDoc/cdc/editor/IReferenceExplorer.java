package edu.pdx.svl.coDoc.cdc.editor;

import edu.pdx.svl.coDoc.cdc.referencemodel.References;

public interface IReferenceExplorer
{
	public void setInput(References refs);
	public void refresh();
	public void displayListOfTextSelectionReferencesForSelectionInActiveEditor();
}
