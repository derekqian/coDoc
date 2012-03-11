package edu.pdx.svl.coDoc.cdc.view;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;

import edu.pdx.svl.coDoc.cdc.Global;



public class Help {
	public static void openHelpBrowser() {
		IWorkbenchBrowserSupport browserSupport = Global.INSTANCE.workbenchPart.getSite().getWorkbenchWindow().getWorkbench().getBrowserSupport();
		URL webUrl;
		try {
			webUrl = new URL("http://svl.cs.pdx.edu/");
		} catch (MalformedURLException e) {
			System.out.println("bad url: " + e.toString());
			return;
		}
		IWebBrowser browser;
		try {
			browser = browserSupport.createBrowser(IWorkbenchBrowserSupport.AS_EDITOR, null, "Google", "The Google Website");
			browser.openURL(webUrl);
		} catch (PartInitException e) {
			System.out.println("could not open browser: " + e.toString());
		}
	}


}
