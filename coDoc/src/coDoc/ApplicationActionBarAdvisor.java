package coDoc;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

import coDoc.actions.Actions;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Cache the coolBar object 
	public static ICoolBarManager coolBar = null;
	
	// Cache the menuBar object 
	public static IMenuManager menuBar = null;
	
	private static ApplicationActionBarAdvisor instance = null;
	
    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction newAction;
    private IWorkbenchAction importAction;
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    
    // window menu
    private IWorkbenchAction maximizeAction;
    private IWorkbenchAction minimizeAction;
	private IContributionItem viewList;
	private IContributionItem perspectiveList;
    
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		instance = this;
	}

	public static ApplicationActionBarAdvisor getInstance()
	{
		return instance;
	}
	
	public static IStatusLineManager getStatusLineManager()
	{
		return ApplicationActionBarAdvisor.getInstance().getActionBarConfigurer().getStatusLineManager();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
	 * Make the actions ...
	 */
	protected void makeActions(IWorkbenchWindow window) {

        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

    	// file menu
		newAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
		register(newAction);
		importAction = ActionFactory.IMPORT.create(window);
		register(importAction);
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
		
		// window menu
		maximizeAction = ActionFactory.MAXIMIZE.create(window);
		register(maximizeAction);
		minimizeAction = ActionFactory.MINIMIZE.create(window);
		register(maximizeAction);
		viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        
        removeDuplicateAction();
	}
	
	protected void fillCoolBar(ICoolBarManager coolBar) {
		
		ApplicationActionBarAdvisor.coolBar = coolBar;
		
		coolBar.removeAll();
		
		// file tool bar
        IToolBarManager fileToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        // fileToolbar.add(new ActionContributionItem(newAction));
        fileToolbar.add(new WorkflowActionContributionItem(Perspective.ID, newAction));
        fileToolbar.add(new WorkflowActionContributionItem(Perspective.ID, importAction));
        
        // Compilation tool bar
        IToolBarManager simulationToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        simulationToolbar.add(new WorkflowActionContributionItem(Perspective.ID, Actions.LaunchVNC));

        coolBar.add(new ToolBarContributionItem(fileToolbar, "fileToolbar"));
        coolBar.add(new ToolBarContributionItem(simulationToolbar, "simulationToolbar"));
        
        coolBar.update(true);
        coolBar.setLockLayout(true);
    }

	protected void fillMenuBar(IMenuManager menuBar) {
		ApplicationActionBarAdvisor.menuBar = menuBar;
		
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);

        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(windowMenu);
        menuBar.add(helpMenu);
		
        // file menu
        fileMenu.add(newAction);
        fileMenu.add(new Separator());
        fileMenu.add(importAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
		
		// windows menu
		windowMenu.add(maximizeAction);
		windowMenu.add(minimizeAction);
		windowMenu.add(new Separator());
		
		MenuManager perspectiveMenu = new MenuManager("&Open Perspective", "openPerspective");
		perspectiveMenu.add(perspectiveList);
		windowMenu.add(perspectiveMenu);
		
		MenuManager viewMenu = new MenuManager("&Show View", "showView");
		viewMenu.add(viewList);
		windowMenu.add(viewMenu);
		        
        // Help
        helpMenu.add(aboutAction);
	}

    // remove unexpected menue and toolbar by (org.eclipse.ui.ide)
    @SuppressWarnings("restriction")
    public void removeDuplicateAction() {
        listActionSetId();
        String[] actionSetId = new String[]{"org.eclipse.ui.edit.text.actionSet.navigation", // removing annoying gotoLastPosition Message.
        									"org.eclipse.ui.edit.text.actionSet.annotationNavigation",
        									"org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo", // Removing convert line delimiters menu.   
        									"org.eclipse.ui.actionSet.openFiles",
        									"org.eclipse.search.searchActionSet",
        									"org.eclipse.cdt.ui.edit.text.c.toggleMarkOccurrences"};
       
        ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();   
        IActionSetDescriptor[] actionSets = reg.getActionSets();   
        for(int i=0; i<actionSetId.length; i++) {
			for(int j=0; j<actionSets.length; j++) {
	            if(actionSets[j].getId().equals(actionSetId[i])) {
		            IExtension ext = actionSets[j].getConfigurationElement().getDeclaringExtension();   
		            reg.removeExtension(ext, new Object[]{actionSets[j]});   
	            }
			}
        }
        //IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        //page.hideActionSet("org.eclipse.ui.WorkingSetActionSet");
    }    
    private void listActionSetId() {
	    IExtensionRegistry registry = Platform.getExtensionRegistry();
	    IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.ui.actionSets");
	    IExtension[] extensions = extensionPoint.getExtensions();
	    for(int i=0; i<extensions.length; i++) {
	    	IConfigurationElement elements[] = extensions[i].getConfigurationElements();
	    	for(int j=0; j<elements.length; j++) {
	    		String pluginId = elements[j].getNamespaceIdentifier();
	    		if(pluginId.indexOf("org.eclipse")>-1){ //$NON-NLS-1$
	    			IConfigurationElement[] subElements = elements[j].getChildren("action");
	    			for(int m=0; m<subElements.length; m++) {
	    				if(pluginId.contains("cdt") && subElements[m].getAttribute("id").contains("factor"))
	    				System.out.println("Plugin: " + pluginId + "  Id: " + subElements[m].getAttribute("id"));
	    			}
	    		}
	    	}
	    }
    }
}
