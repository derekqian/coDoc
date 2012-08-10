package coDoc.actions;

import java.io.IOException;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import coDoc.Activator;

public class Actions
{
	public static Action LaunchVNC = new Action() {
		public void run()
		{
			String vnccmd = "vncviewer localhost";
			String[] cmdArray = {"/bin/sh", "-c", vnccmd};
			try {
				Runtime.getRuntime().exec(cmdArray);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	public static Action OnlineHELP = new Action() {
		public void run()
		{
			Help.openHelpBrowser();
		}
	};

	static
	{
		LaunchVNC.setEnabled(false);
		LaunchVNC.setText("Launch VNC");
		LaunchVNC.setToolTipText("Launch VNC");
		LaunchVNC.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/launch_vnc.gif"));
		OnlineHELP.setEnabled(true);
		OnlineHELP.setId("coDoc.onlinehelp");
		OnlineHELP.setActionDefinitionId("coDoc.onlinehelp");
		OnlineHELP.setText("Online Help");
		OnlineHELP.setToolTipText("Online Help");
		OnlineHELP.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/launch_vnc.gif"));
	}
}