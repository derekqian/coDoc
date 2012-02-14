package edu.pdx.svl.coDoc.poppler;

import javax.swing.tree.DefaultMutableTreeNode;

import com.sun.pdfview.action.GoToAction;


public class OutlineNode extends DefaultMutableTreeNode {
	
	 private String title;
	 
	/**
	 * The constructor.
	 */
	public OutlineNode(String arg0) 
	{
		title = arg0;
	}
	
	public PDFAction getAction()
	{
		Object obj = getUserObject();
		
		assert(obj instanceof PDFAction);

		return (PDFAction)obj;
	}
	
	public void setAction(PDFAction action)
	{
		setUserObject(action);
		return;
	}
	
	public String toString()
	{
		return title;
	}
}
