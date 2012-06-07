package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import edu.pdx.svl.coDoc.cdc.datacenter.MapEntry;


public class TableLabelProvider implements ITableLabelProvider {
	@Override
	public String getColumnText(Object element, int col) {
		String text = "";
		String temp;
		if(element instanceof MapEntry) {
			MapEntry mp = (MapEntry) element;
			switch(col) {
			case 0: // source file
				temp = mp.getCodefilename();
				text = temp.substring(temp.lastIndexOf('/')+1);
				break;
			case 1: // code
				text = mp.getCodeselpath().getCodeText();
				break;
			case 2: // PDF file
				temp = mp.getSpecfilename();
				text = temp.substring(temp.lastIndexOf('/')+1);
				break;
			case 3: // page
				text = String.valueOf(mp.getSpecselpath().getPage());
				break;
			case 4: // spec text
				text = mp.getSpecselpath().getPDFText();
				break;
			case 5: // comment
				text = mp.getComment();
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