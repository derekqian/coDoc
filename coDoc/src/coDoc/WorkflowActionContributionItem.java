/**
 * 
 */
package coDoc;

import java.util.Hashtable;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Juncao
 *
 * Used to customize the tool bar for different perspectives
 * 
 */
public class WorkflowActionContributionItem extends ActionContributionItem {

	private Hashtable<String, String> perspectiveTable = null;
	
	/**
	 * @param action
	 */
	public WorkflowActionContributionItem(Hashtable<String, String> perspectiveTable, IAction action) {
		super(action);
		// TODO Auto-generated constructor stub
		
		this.perspectiveTable = perspectiveTable;
	}
	
	/**
	 * @param action
	 */
	public WorkflowActionContributionItem(String perspectiveId, IAction action) {
		super(action);
		// TODO Auto-generated constructor stub
		if(null != perspectiveId)
		{
			this.perspectiveTable = new Hashtable<String, String>();
			this.perspectiveTable.put(perspectiveId, perspectiveId);
		}
	}
	
	@Override
	public boolean isVisible()
	{
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (workbenchWindow == null)
			return false;

		IWorkbenchPage activePage = workbenchWindow.getActivePage();
		if (activePage == null)
			return false;
		
		if (this.perspectiveTable.get(activePage.getPerspective().getId()) != null)
			return true;

		return false;
	}
}
