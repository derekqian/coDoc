package edu.pdx.svl.coDoc.refexp.referencemodel;

import org.eclipse.jface.dialogs.MessageDialog;

public class AcrobatJNIStub {
	private static int i = 0;
	private static String txt = "AcrobatJNIStub call num: ";
	
	public int getTop() {
		++i;
		return i;
	}
	
    public int getBottom() {
    	++i;
		return i;
	}
    
    public int getLeft() {
    	++i;
		return i;
	}
    
    public int getRight() {
    	++i;
		return i;
	}
    
    public int getPage() {
    	++i;
		return i;
	}
    
    public String getSelectedText() {
		++i;
		txt += i;
		return txt;
	}
    
    public void selectText(int page, int top, int bottom, int left, int right) {
    	
		String selStr = "We are simulating selecting text in a PDF Viewer...\n";
		selStr += "page  : " + page   + "\n";
		selStr += "top   : " + top    + "\n";
		selStr += "bottom: " + bottom + "\n";
		selStr += "left  : " + left   + "\n";
		selStr += "right : " + right;
		
		MessageDialog.openError(null, "Alert",  selStr);
	}
    
}
