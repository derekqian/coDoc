package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;


import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.pdx.svl.coDoc.cdc.datacenter.CDCModel;
import edu.pdx.svl.coDoc.cdc.datacenter.MapEntry;



public class TableContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(Object element) {
		if(element instanceof Vector) {
			Vector<MapEntry> mapEntries = (Vector<MapEntry>) element;
			return mapEntries.toArray();
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
