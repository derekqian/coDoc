/**
 * 
 */
package vdt.hdvd.Logging;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author Juncao
 * Logging for VDTS
 */
public class Logger 
{
	public static void WriteLine(final String message)
	{
		// TODO: do something ...
		// use this for now ...
		Display.getDefault().syncExec(new Runnable() {
			public void run()
			{
				MessageDialog.openInformation(null, "Error", message);
			}
		});
	}
	
	public static void WriteException(final Exception e)
	{
		// TODO: do something ...
		// use this for now ...
		Display.getDefault().syncExec(new Runnable() {
			public void run()
			{
				MessageDialog.openInformation(null, "Error", e.toString());
				e.printStackTrace();
			}
		});
	}

}
