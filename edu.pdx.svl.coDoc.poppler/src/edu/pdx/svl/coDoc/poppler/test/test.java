package edu.pdx.svl.coDoc.poppler.test;


import java.awt.Dimension;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import edu.pdx.svl.coDoc.poppler.editor.PDFPageViewer;
import edu.pdx.svl.coDoc.poppler.lib.PopplerJNI;


public class test
{
	//private static PDFReader pv;
	
    public static void main(String[] args) 
    {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Hello World");
        shell.setSize(800, 600);
        
		String name = shell.getClass().getName();
		System.out.println(name);
		
        //shell.setLayout(new FillLayout());
        Button btn = new Button(shell, SWT.NONE);
        btn.addSelectionListener(new SelectionAdapter(){
        	public void widgetSelected(SelectionEvent e)
        	{
        		PopplerJNI poppler = new PopplerJNI();
        		poppler.document_new_from_file("file:///home/derek/Data Check and Restore Manual.pdf", null);
        		int pages = poppler.document_get_n_pages();
        		System.out.println("pages: "+pages+"\n");
        		
        		poppler.document_get_page(1);
        		
        		Dimension size = poppler.page_get_size();
        		
        		Rectangle[] rectArray = poppler.page_get_selected_region(1.0, new Rectangle(100,50,150,100));
        		String str = poppler.page_get_selected_text(1.0, new Rectangle(100,50,150,100));
        		
        		poppler.document_release_page();
        		
        		poppler.document_close();
        		return;
        	}
        });
        btn.setBounds(20,20,60,40);
        btn.setText("test");
        
        shell.open();
        while (!shell.isDisposed()) 
        {
            if (!display.readAndDispatch()) 
                display.sleep ();
        }
        display.dispose();
     } 

}
