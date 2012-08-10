package edu.pdx.svl.coDoc.views.link;

import org.eclipse.jface.viewers.*;


import edu.pdx.svl.coDoc.cdc.datacenter.EntryNode;
import edu.pdx.svl.coDoc.cdc.datacenter.LinkEntry;

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
		EntryNode node = (EntryNode) element;
		LinkEntry mp = (LinkEntry) node.getData();
		return mp.comment;
	}

	@Override
	protected void setValue(Object element, Object value) {
		EntryNode node = (EntryNode) element;
		LinkEntry mp = (LinkEntry) node.getData();
		mp.comment = String.valueOf(value);
		viewer.refresh();
	}

}
