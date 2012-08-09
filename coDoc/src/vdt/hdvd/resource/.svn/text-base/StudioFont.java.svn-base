/**
 * 
 */
package vdt.hdvd.resource;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class StudioFont {

	public static StudioFont Instance = new StudioFont(); 
	
	public Font FontNormal = null;
	
	// 10 font size
	public Font FontNormal_10 = null;
	public Font FontBold = null;
	public Font FontItalic = null;
	
	public Font FontPathGroup = null;
	public Font FontInterfaceFunction = null;
	public Font FontLogView = null;
	
	
	protected StudioFont()
	{
		FontData fontData = new FontData();
		fontData.setStyle(SWT.NONE);
		this.FontNormal = new Font(Display.getCurrent(), fontData);
		
		fontData = new FontData();
		fontData.setStyle(SWT.NONE);
		fontData.setHeight(10);
		FontNormal_10 = new Font(Display.getCurrent(), fontData);
		
		fontData = new FontData();
		fontData.setStyle(SWT.BOLD);
		this.FontBold = new Font(Display.getCurrent(), fontData);
		
		fontData = new FontData();
		fontData.setStyle(SWT.ITALIC);
		this.FontItalic = new Font(Display.getCurrent(), fontData);
		
		// Specialized font
		fontData = new FontData();
		fontData.setStyle(SWT.BOLD | SWT.CENTER);
		fontData.setHeight(14);
		this.FontPathGroup = new Font(Display.getCurrent(), fontData);
	
	    fontData = new FontData();
		fontData.setStyle(SWT.BOLD | SWT.CENTER);
		fontData.setHeight(10);
		this.FontInterfaceFunction = new Font(Display.getCurrent(), fontData);
		
		this.FontLogView = JFaceResources.getFont(JFaceResources.TEXT_FONT);
	}
	
	@Override
	protected void finalize()
	{
		this.FontNormal.dispose();
		this.FontBold.dispose();
		this.FontPathGroup.dispose();
		this.FontInterfaceFunction.dispose();
		this.FontItalic.dispose();
		this.FontLogView.dispose();
	}
}
