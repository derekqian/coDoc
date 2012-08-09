package vdt.hdvd.project.wizard;


import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import vdt.hdvd.project.support.HDVDProjectSupport;

public class HDVDProjectWizard extends Wizard implements INewWizard, IExecutableExtension
{
	private WizardNewProjectCreationPage pageOne = null;
	private static final String WIZARD_NAME = "New HDVD Project";
	private static final String PAGE_NAME = "HDVDNewProjectPage";
	private IConfigurationElement configurationElement = null;
	
	/**
	 * 
	 */
	public HDVDProjectWizard() 
	{
		// TODO Auto-generated constructor stub
		setWindowTitle(WIZARD_NAME);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		String name = pageOne.getProjectName();
		if (!name.matches("^[_0-9a-zA-Z]+$")) {
		    pageOne.setErrorMessage(
		            "Project name contains invalid characters.");
		    return false;
		}
		URI location = null;
		if (!pageOne.useDefaults()) {
			location = pageOne.getLocationURI();
		} // else location == null

		HDVDProjectSupport.createProject(name, location);

		return true;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		// TODO Auto-generated method stub
		this.configurationElement = config;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addPages() {
	  super.addPages();
	  pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
	  pageOne.setTitle("Project Name");
	  pageOne.setDescription("Please specify your project name:");
	  addPage(pageOne);
	}
}