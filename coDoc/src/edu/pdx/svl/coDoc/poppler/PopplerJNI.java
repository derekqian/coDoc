package edu.pdx.svl.coDoc.poppler;

import org.eclipse.swt.graphics.Point;


public class PopplerJNI {

	static
	{
		System.out.println(System.getProperty("java.library.path"));
		try
		{
			System.loadLibrary("PopplerJNI");
		}
		catch(UnsatisfiedLinkError e)
		{
			System.err.println("Cannot load PopplerJNI library:" + e.toString());
		}
	}

	/**
	 * The constructor.
	 */
	public PopplerJNI() 
	{
	}
	
	// return pointer to document
	public native int document_new_from_file(String uri, String password);
	public native int document_close();
	
	// get number of pages
	public native int document_get_n_pages();
	
	// return pointer to page
	public native int document_get_page(int index);
	public native int document_release_page();
	
	public native Point page_get_size();
	
	public native int page_render(byte[] data);
}
