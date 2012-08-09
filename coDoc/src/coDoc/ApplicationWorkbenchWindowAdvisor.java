package coDoc;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle("VDT - HDVD");
        configurer.setShowPerspectiveBar(false);
    }
    
    public void postWindowOpen() {
    	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        page.hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation");
        page.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
        page.hideActionSet("org.eclipse.ui.edit.text.actionSet.gotoLastEditPosition");
        page.hideActionSet("org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo");

        page.hideActionSet("org.eclipse.ui.edit.text.toggleBlockSelectionMode");
        page.hideActionSet("org.eclipse.ui.edit.text.toggleShowWhitespaceCharacters");

        page.hideActionSet("org.eclipse.update.ui.softwareUpdates");
        page.hideActionSet("org.eclipse.ui.edit.text.actionSet.presentation");
    }
}
