package edu.pdx.svl.coDoc.cdt.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import edu.pdx.svl.coDoc.cdt.internal.core.model.IBufferFactory;
import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CDocumentProvider;
import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CustomBufferFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class CUIPlugin extends AbstractUIPlugin
{
	private static CUIPlugin fgCPlugin;
	private IBufferFactory fBufferFactory;
	private CDocumentProvider fDocumentProvider;
	
	private static ResourceBundle fgResourceBundle;
	public static final String PLUGIN_ID = "edu.pdx.svl.coDoc.cdt.ui"; //$NON-NLS-1$
	
  public CUIPlugin() {
    fgCPlugin = this;
    fDocumentProvider = null;
  }
  
	public static CUIPlugin getDefault() {
		return fgCPlugin;
	}
	
  public void start(BundleContext context) throws Exception {
    super.start(context);
    System.out.println("Starting the UI Plugin");
  }
  
  /* (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext context) throws Exception {     
    if (fDocumentProvider != null) {
      fDocumentProvider= null;
    }
  }
  
	public synchronized IBufferFactory getBufferFactory() {
		if (fBufferFactory == null)
			fBufferFactory= new CustomBufferFactory();
		return fBufferFactory;
	}
	
  /**
   * Returns the used document provider
   */
  public synchronized CDocumentProvider getDocumentProvider() {
    if (fDocumentProvider == null) {
      fDocumentProvider = new CDocumentProvider();
    }
    return fDocumentProvider;
  }
  
	public static String getResourceString(String key) {
		try {
			return fgResourceBundle.getString(key);
		}
		catch (MissingResourceException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (NullPointerException e) {
			return "#" + key + "#"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	* Utility method with conventions
	*/
	public static void errorDialog(Shell shell, String title, String message, IStatus s, boolean logError) {
		
		// if the 'message' resource string and the IStatus' message are the same,
		// don't show both in the dialog
		if (s != null && message.equals(s.getMessage())) {
			message = null;
		}
		ErrorDialog.openError(shell, title, message, s);
	}
	
	/**
	* Utility method with conventions
	*/
	public static void errorDialog(Shell shell, String title, String message, Throwable t, boolean logError) {
		
		IStatus status;
		if (t instanceof CoreException) {
			status = ((CoreException) t).getStatus();
			// if the 'message' resource string and the IStatus' message are the same,
			// don't show both in the dialog
			if (status != null && message.equals(status.getMessage())) {
				message = null;
			}
		} else {
			status = new Status(IStatus.ERROR, CUIPlugin.PLUGIN_ID, -1, "Internal Error: ", t); //$NON-NLS-1$	
		}
		ErrorDialog.openError(shell, title, message, status);
	}
}
