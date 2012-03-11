package edu.pdx.svl.coDoc.refexp.referenceexplorer.edit;

import org.eclipse.jface.viewers.*;


import edu.pdx.svl.coDoc.cdc.Global;
import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;
import edu.pdx.svl.coDoc.cdc.referencemodel.*;

public class EditComment extends EditingSupport {

	TreeViewer viewer;
	
	public EditComment(TreeViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTree());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((Reference) element).getComment();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((Reference) element).setComment(String.valueOf(value));
		viewer.refresh();
		SimpleXML.write(Global.INSTANCE.entryEditor.getDocument());
	}

}
