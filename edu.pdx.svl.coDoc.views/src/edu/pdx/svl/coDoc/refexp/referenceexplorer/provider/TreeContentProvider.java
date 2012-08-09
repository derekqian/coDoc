package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import edu.pdx.svl.coDoc.cdc.datacenter.EntryNode;

public class TreeContentProvider implements ITreeContentProvider {
	
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		this.model = (References) newInput;
//		viewer.refresh();
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EntryNode) {
			EntryNode node = (EntryNode) inputElement;
			return node.getChildren();
		} else {
			return null;			
		}
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof EntryNode) {
			EntryNode ref = (EntryNode)parentElement;
			return ref.getChildren();
		} else {
			return null;			
		}
	}

	public Object getParent(Object element) {
		if (element instanceof EntryNode) {
			EntryNode ref = (EntryNode)element;
			return ref.getParent();
		} else {
			return null;			
		}
	}

	public boolean hasChildren(Object element) {
		if (element instanceof EntryNode) {
			EntryNode ref = (EntryNode)element;
			return ref.hasChildren();
		} else {
			return false;
		}
	}
}
