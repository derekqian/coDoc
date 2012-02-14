package edu.pdx.svl.coDoc.poppler;

import java.awt.geom.Rectangle2D;


public class LinkAnnotation {
	
	private PDFAction action;
	 
	/**
	 * The constructor.
	 */
	public LinkAnnotation(PDFAction action) 
	{
		this.action = action;
	}
	
	public PDFAction getAction()
	{
		return action;
	}

	public Rectangle2D getRect() {
		// TODO Auto-generated method stub
		return null;
	}
}
