package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;


import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.pdx.svl.coDoc.refexp.Global;



public class TableContentProvider implements ITreeContentProvider {
	
	public TableContentProvider() {

	}
	
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public Object[] getElements(Object inputElement) {
		return Global.INSTANCE.testModel.getTestModelItems();
	}

	public Object[] getChildren(Object parentElement) {
		return null;
	}

	public Object getParent(Object element) {
		return null;
		
	}

	public boolean hasChildren(Object element) {
		return false;
	}

}
