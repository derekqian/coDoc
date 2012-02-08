package edu.pdx.svl.coDoc.poppler;

import java.io.File;
import java.util.Properties;

import org.eclipse.swt.graphics.Point;


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
