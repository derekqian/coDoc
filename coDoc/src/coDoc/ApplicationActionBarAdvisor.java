package coDoc;

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

import vdt.hdvd.actions.HDVDActions;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Cache the coolBar object 
	public static ICoolBarManager coolBar = null;
	
	// Cache the menuBar object 
	public static IMenuManager menuBar = null;
	
	private static ApplicationActionBarAdvisor instance = null;
	
	// file menu
    private IWorkbenchAction fileSaveAction;
    private IWorkbenchAction fileSaveAllAction;
    private IWorkbenchAction importAction;
    private IWorkbenchAction newAction;
    private IWorkbenchAction openNewWindowAction;
    private IWorkbenchAction exitAction;
    
    // edit menu
    private IWorkbenchAction selectAllAction;
    private IWorkbenchAction undoAction;
    private IWorkbenchAction redoAction;
    private IWorkbenchAction copyAction;
    private IWorkbenchAction cutAction;
    
    // search menu
    private IWorkbenchAction searchAction;
    
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

		// file menu
		openNewWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
		register(openNewWindowAction);
		fileSaveAction = ActionFactory.SAVE.create(window);
		register(fileSaveAction);
		fileSaveAllAction = ActionFactory.SAVE_ALL.create(window);	
		register(fileSaveAllAction);
		importAction = ActionFactory.IMPORT.create(window);
		register(importAction);
		newAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
		register(newAction);
		exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
		
		// edit menu
		selectAllAction = ActionFactory.SELECT_ALL.create(window);
		register(selectAllAction);
		undoAction = ActionFactory.UNDO.create(window);
		register(undoAction);
		redoAction = ActionFactory.REDO.create(window);
		register(redoAction);
		copyAction = ActionFactory.COPY.create(window);
		register(copyAction);
		cutAction = ActionFactory.CUT.create(window);
		register(cutAction);
		
		// search menu
		searchAction = ActionFactory.FIND.create(window);
		register(searchAction);
		
		// window menu
		maximizeAction = ActionFactory.MAXIMIZE.create(window);
		register(maximizeAction);
		minimizeAction = ActionFactory.MINIMIZE.create(window);
		register(maximizeAction);
		viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
	}
	
	protected void fillCoolBar(ICoolBarManager coolBar) {
		
		ApplicationActionBarAdvisor.coolBar = coolBar;
		
		coolBar.removeAll();
		
		// file tool bar
        IToolBarManager fileToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        fileToolbar.add(new WorkflowActionContributionItem(Perspective.ID, newAction));
        fileToolbar.add(new WorkflowActionContributionItem(Perspective.ID, fileSaveAction));
        fileToolbar.add(new WorkflowActionContributionItem(Perspective.ID, fileSaveAllAction));
        
        // Compilation tool bar
        IToolBarManager simulationToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        simulationToolbar.add(new WorkflowActionContributionItem(Perspective.ID, HDVDActions.LaunchVNC));

        /*        // VD Runtime tool bar
        IToolBarManager cosimToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        Hashtable<String, String> table = new Hashtable<String, String>();
        table.put(VDInsightPerspective.ID, VDInsightPerspective.ID);
		table.put(CoSimPerspective.ID, CoSimPerspective.ID);
        cosimToolbar.add(new WorkflowActionContributionItem(table, SvdActions.ExecuteSvd));
        cosimToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.StopSvd));
        
        // QEMU tool bar
        IToolBarManager qemuToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        qemuToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.SelectQemuImage));
        qemuToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.LaunchQemu));
        qemuToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.LaunchQemuImage));
        
        // SVD runtime control tool bar
        IToolBarManager runtimeControlToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        Global.svdCtl = new SvdControlCombo("SvdControlMode");
        runtimeControlToolbar.add(Global.svdCtl);
        runtimeControlToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.PauseSvd));
        runtimeControlToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.ResumeSvd));
      
        // SVD runtime report/tools 
        IToolBarManager runtimeReportToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        runtimeReportToolbar.add(new WorkflowActionContributionItem(CoSimPerspective.ID, SvdActions.LoadSvdCoverageReport));
        runtimeReportToolbar.add(new WorkflowActionContributionItem(CoverageReportPerspective.ID, SvdActions.LoadRequestFilter));
        runtimeReportToolbar.add(new WorkflowActionContributionItem(CoverageReportPerspective.ID, SvdActions.CreateCoverageReport));
        
        // SVD debug tool bar
        IToolBarManager debugToolbar = new ToolBarManager(SWT.FLAT | SWT.LEFT);
        debugToolbar.add(new WorkflowActionContributionItem(DebugPerspective.ID, SvdActions.StartDebugging));
        debugToolbar.add(new WorkflowActionContributionItem(DebugPerspective.ID, SvdActions.StopDebugging));
        
        // add to cool bar
        coolBar.add(new ToolBarContributionItem(fileToolbar, "fileToolbar"));  
        coolBar.add(new ToolBarContributionItem(vdinsightToolbar, "VDInsightToolbar"));   
        coolBar.add(new ToolBarContributionItem(cosimToolbar, "CoSimToolbar"));   
        coolBar.add(new ToolBarContributionItem(qemuToolbar, "QEMUToolbar"));   
        coolBar.add(new ToolBarContributionItem(runtimeControlToolbar, "RuntimeControlToolbar"));   
        coolBar.add(new ToolBarContributionItem(runtimeReportToolbar, "RuntimeReportToolbar"));
        coolBar.add(new ToolBarContributionItem(debugToolbar, "DebugToolbar"));*/

        coolBar.add(new ToolBarContributionItem(fileToolbar, "fileToolbar"));
        coolBar.add(new ToolBarContributionItem(simulationToolbar, "simulationToolbar"));
        coolBar.update(true);
        coolBar.setLockLayout(true);
    }

	protected void fillMenuBar(IMenuManager menuBar) {
		
		ApplicationActionBarAdvisor.menuBar = menuBar;
		
		// file menu
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		fileMenu.add(newAction);
		// fileMenu.add(openNewWindowAction);
		fileMenu.add(fileSaveAction);
		fileMenu.add(fileSaveAllAction);
		fileMenu.add(importAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);
		
		// edit menu
		MenuManager editMenu = new MenuManager("&Edit", IWorkbenchActionConstants.M_EDIT);
		editMenu.add(selectAllAction);
		editMenu.add(undoAction);
		editMenu.add(redoAction);
		editMenu.add(copyAction);
		editMenu.add(cutAction);
		
		// windows menu
		MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
		windowMenu.add(maximizeAction);
		windowMenu.add(minimizeAction);
		windowMenu.add(new Separator());
		
		MenuManager perspectiveMenu = new MenuManager("&Open Perspective", "openPerspective");
		perspectiveMenu.add(perspectiveList);
		windowMenu.add(perspectiveMenu);
		
		MenuManager viewMenu = new MenuManager("&Show View", "showView");
		viewMenu.add(viewList);
		windowMenu.add(viewMenu);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(windowMenu);
	}
}
