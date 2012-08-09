package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.pdx.svl.coDoc.cdc.datacenter.EntryNode;



public class TableContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EntryNode) {
			EntryNode node = (EntryNode) inputElement;
			return node.getChildren();
		} else {
			return null;
		}
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	@Override
	public void dispose() {
	}
}
