package edu.pdx.svl.coDoc.cdc.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.part.MultiEditorInput;

@SuppressWarnings("restriction")
public class EntryEditorInput extends MultiEditorInput
{
	public EntryEditorInput(String[] editorIDs, IEditorInput[] innerEditors) {
		super(editorIDs, innerEditors);
		// TODO Auto-generated constructor stub
	}
	public boolean exists()
	{
		return true;
	}
	public ImageDescriptor getImageDescriptor()
	{
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_HELP_SEARCH);
	}
	public String getName()
	{
		return "EntryEditor";
	}
	public String getToolTipText()
	{
		return "This is the container editor for coDoc project";
	}
	public IPersistableElement getPersistable()
	{
		return null;
	}
	public Object getAdapter(Class adapter)
	{
		return null;
	}
}
