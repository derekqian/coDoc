package edu.pdx.svl.coDoc.poppler;


public class UriAction extends PDFAction {
	
	 private String uri;
	 
	/**
	 * The constructor.
	 */
	public UriAction(String uri) 
	{
		super("URI");
		this.uri = uri;
	}
	
	public String getUri()
	{
		return uri;
	}
}
