package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;

import java.util.Vector;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;


//import edu.pdx.svl.coDoc.refexp.referenceexplorer.fakemodel.*;
import edu.pdx.svl.coDoc.refexp.Global;
import edu.pdx.svl.coDoc.refexp.referencemodel.*;

public class TreeContentProvider implements ITreeContentProvider {
	public boolean allSources = true;
	
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		this.model = (References) newInput;
//		viewer.refresh();
	}

	public Object[] getElements(Object inputElement) {
		/*
		 * This is when we have a list of references that we
		 * want to display rather than a tree.  For example,
		 * we did a search from a selection in a text editor
		 * and want to display only matching references.
		 */
		if (inputElement instanceof Vector) {
			return ((Vector) inputElement).toArray();
		}
		
		if (allSources == true) {
			return Global.INSTANCE.references.getProjects().toArray();
		} else {
			Object[] matchingSourceFile = new Object[1];
			
			References references = Global.INSTANCE.references;
			SourceFileReference sfr = references.findActiveSourceFileReference();
			
			if (sfr != null) {
				matchingSourceFile[0] = sfr;
			} else {
				matchingSourceFile[0] = "";
			}
			return matchingSourceFile;
		}
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Reference) {
			Reference ref = (Reference)parentElement;
			return ref.getChildrenList().toArray();
		}
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof Reference) {
			Reference ref = (Reference)element;
			if (ref.getChildrenList().size() > 0) {
				return true;
			}else {
				return false;
			}
		} else {
			return false;
		}
	}

}
