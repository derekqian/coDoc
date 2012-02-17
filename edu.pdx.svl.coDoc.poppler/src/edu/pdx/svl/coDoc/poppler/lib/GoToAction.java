package edu.pdx.svl.coDoc.poppler.lib;


public class GoToAction extends PDFAction {
	
	private PDFDestination dest;
	 
	/**
	 * The constructor.
	 */
	public GoToAction(PDFDestination dest) 
	{
		super("GOTO");
		this.dest = dest;
	}
	
	public PDFDestination getDestination()
	{
		return dest;
	}
}
