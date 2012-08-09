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

	static
	{
		LaunchVNC.setEnabled(false);
		LaunchVNC.setText("Launch VNC");
		LaunchVNC.setToolTipText("Launch VNC");
		LaunchVNC.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/launch_vnc.gif"));
	}
}