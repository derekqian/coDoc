package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;


import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.pdx.svl.coDoc.cdc.editor.CDCModel;
import edu.pdx.svl.coDoc.cdc.editor.MapEntry;



public class TableContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(Object element) {
		if(element instanceof CDCModel) {
			CDCModel cdcModel = (CDCModel) element;
			Vector<MapEntry> mapEntries = cdcModel.getMapEntries();
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
