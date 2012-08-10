package edu.pdx.svl.coDoc.views.link;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import edu.pdx.svl.coDoc.cdc.datacenter.EntryNode;
import edu.pdx.svl.coDoc.cdc.datacenter.LinkEntry;


public class TableLabelProvider implements ITableLabelProvider {
	@Override
	public String getColumnText(Object element, int col) {
		String text = "";
		String temp;
		if(element instanceof EntryNode) {
			EntryNode node = (EntryNode) element;
			LinkEntry mp = (LinkEntry) node.getData();
			switch(col) {
			case 0: // source file
				temp = mp.codefilename;
				text = temp.substring(temp.lastIndexOf('/')+1);
				break;
			case 1: // code
				text = mp.codeselpath.getSyntaxCodeText();
				break;
			case 2: // PDF file
				temp = mp.specfilename;
				text = temp.substring(temp.lastIndexOf('/')+1);
				break;
			case 3: // page
				text = String.valueOf(mp.specselpath.getPage());
				break;
			case 4: // spec text
				text = mp.specselpath.getPDFText();
				break;
			case 5: // comment
				text = mp.comment;
				break;
			default:
				break;
			}
		}
		return text;
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
	}
	@Override
	public void removeListener(ILabelProviderListener arg0) {
	}
	@Override
	public void dispose() {
	}
	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}
}