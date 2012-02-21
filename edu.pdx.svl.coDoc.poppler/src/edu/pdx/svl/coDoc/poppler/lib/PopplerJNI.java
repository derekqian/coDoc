package edu.pdx.svl.coDoc.poppler.lib;

import java.awt.Dimension;
import java.io.File;
import java.util.Properties;

import org.eclipse.swt.graphics.Rectangle;


public class PopplerJNI {

	static
	{
		System.out.println(System.getProperty("java.library.path"));
		try
		{
			String popperLibPath = null;
			File currentWorkingDirFile = new File(".");
			String currentWorkingDir = currentWorkingDirFile.getAbsolutePath();
			Properties props=System.getProperties();
			String osName = props.getProperty("os.name");
			if(osName.equals("Linux"))
			{
				popperLibPath = currentWorkingDir + "/bin/libPopplerJNI.so";
				popperLibPath = "/home/derek/Research/coDoc/edu.pdx.svl.coDoc.poppler/bin/libPopplerJNI.so";
			} 
			else if(osName.equals("Windows"))
			{
			}
			System.load(popperLibPath);
		}
		catch(UnsatisfiedLinkError e)
		{
			try
			{
				System.loadLibrary("PopplerJNI");
			}
			catch (Exception e2)
			{
				System.err.println("Cannot load PopplerJNI library:" + e2.toString());
			}
		}
	}

	/**
	 * The constructor.
	 */
	public PopplerJNI() 
	{
	}

	public OutlineNode getOutline() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// return pointer to document
	public native int document_new_from_file(String uri, String password);
	public native int document_close();
	
	// get number of pages
	public native int document_get_n_pages();
	
	// return pointer to page
	public native int document_get_page(int index);
	public native int document_release_page();
	
	public native Dimension page_get_size();
	public native int page_get_index();

	public native int page_render(byte[] data);
	public native int page_render_selection(byte[] data, Rectangle oldselection, Rectangle selection);
	
	public native Rectangle[] page_get_selected_region(double scale, Rectangle selection);
	public native String page_get_selected_text(double scale, Rectangle selection);
}
