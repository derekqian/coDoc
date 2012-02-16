package edu.pdx.svl.coDoc.poppler.test;


import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPage;

import edu.pdx.svl.coDoc.editors.PDFPageViewer;
import edu.pdx.svl.coDoc.poppler.PopplerJNI;

class A
{
	public A()
	{
		System.out.println("Constructor for A");
	}
}
class B extends A
{
	public B()
	{
		System.out.println("Constructor for B");
	}
}
class C extends B
{
	public C()
	{
		System.out.println("Constructor for C");
	}
}
public class test
{
	private static ScrolledComposite sc;
	private static PDFReader pv;
	
    public static void main(String[] args) 
    {
		A a = new C();
		String name = a.getClass().getName();
		System.out.println(name);
		
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Hello World");
        shell.setSize(800, 600);
        
        shell.setLayout(new FillLayout());
        sc = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
        pv = new PDFReader(sc);
        pv.showPage(null);
        ScrollBar vBar = sc.getVerticalBar();
        vBar.setIncrement(10);
        
        shell.open();
        while (!shell.isDisposed()) 
        {
            if (!display.readAndDispatch()) 
                display.sleep ();
        }
        display.dispose();
     } 

}
