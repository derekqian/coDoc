package edu.pdx.svl.coDoc.refexp.referencemodel;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


public class AcrobatJNI {
	public native int getTop();
    public native int getBottom();
    public native int getLeft();
    public native int getRight();
    public native int getPage();
    public native String getSelectedText();
    public native void selectText(int page, int top, int bottom, int left, int right);
    
    
    static
    {
    	//TODO get this proper way of finding the dll in plugins folder to work
//    	Bundle bundle = Platform.getBundle("Spec2Model");
//    	Path path = new Path("plugins/DDEClient.dll");
//    	URL fileURL = FileLocator.find(bundle, path, null);
//    	String ddeClientPath = fileURL.getPath();
// 
    	try {
    		File currentWorkingDirectory = new File (".");
    	    String currentWorkingDirectoryStr = currentWorkingDirectory.getAbsolutePath();
    	    String dllAbsolutePath = currentWorkingDirectoryStr + "\\plugins\\DDEClientWithMFC.dll";
    	    System.load(dllAbsolutePath);
    	} catch (UnsatisfiedLinkError e) {
    		try {
    			System.loadLibrary("DDEClientWithMFC");
    		} catch (Exception e2) {
    			System.out.println("unable to load DDEClientWithMFC.dll");
    		}
    	}
    }
    
    public static void main(String [] argv) throws InterruptedException {
    	AcrobatJNI test = new AcrobatJNI();

    	//TEST 1
//    	int p,t,b,l,r;
//    	p=274;
//    	t=34675927;
//    	b=33161073;
//    	l=5904138;
//    	r=32899057;
//    	test.selectText(p,t,b,l,r);
    	
    	
    	
    	
    	//test workflow
    	int top = test.getTop();
    	int bottom = test.getBottom();
    	int left = test.getLeft();
    	int right = test.getRight();
    	int page = test.getPage();
    	String selectedText = test.getSelectedText();
    	System.out.println("In Java, Selection 1...");
    	System.out.println("page:         " + page);
    	System.out.println("top:          " + top);
    	System.out.println("bottom:       " + bottom);
    	System.out.println("left:         " + left);
    	System.out.println("right:        " + right);
    	System.out.println("selectedText: " + selectedText);
    	System.out.println("SLEEPING 5 SECS(1).  Make another selection.");
		Thread.sleep(5000);	//sleep for 5 seconds
		
		int top2 = test.getTop();
    	int bottom2 = test.getBottom();
    	int left2 = test.getLeft();
    	int right2 = test.getRight();
    	int page2 = test.getPage();
    	String selectedText2 = test.getSelectedText();
    	System.out.println("In Java, Selection 2...");
    	System.out.println("page:         " + page2);
    	System.out.println("top:          " + top2);
    	System.out.println("bottom:       " + bottom2);
    	System.out.println("left:         " + left2);
    	System.out.println("right:        " + right2);
    	System.out.println("selectedText: " + selectedText2);
    	
    	
    	System.out.println("showing first selection in acrobat...");
    	test.selectText(page,top,bottom,left,right);
    	System.out.println("SLEEPING 5 SECS(2)...");
    	Thread.sleep(5000);
    	
    	System.out.println("showing second selection in acrobat");
    	test.selectText(page2,top2,bottom2,left2,right2);
    	System.out.println("DONE");
    }
}
