package edu.pdx.svl.coDoc.cdc.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


//import edu.pdx.svl.coDoc.refexp.editorcontextmenu.editors.SelectionAndCursor;

import edu.pdx.svl.coDoc.cdc.preferences.PreferencesView;



public class Help implements IObjectActionDelegate {

	private Shell shell;
	
	
	/**
	 * Constructor for Action1.
	 */
	public Help() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
		URL webUrl;
		try {
			webUrl = new URL("http://web.cecs.pdx.edu/~dejun/coDoc/coDoc.html");
			// webUrl = new URL("http://svl.cs.pdx.edu/");
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

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
