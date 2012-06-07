package edu.pdx.svl.coDoc.cdc.wizard;


import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;

import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.datatransfer.FileSystemImportWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;
import edu.pdx.svl.coDoc.cdc.datacenter.CDCModel;

/**
 * The activator class controls the plug-in life cycle
 */
public class WizardMain extends Wizard implements INewWizard, IExecutableExtension
{
    private WelcomePage page1;
    private WizardNewProjectCreationPage page2;
    private CodeImportPage page3;
    private SpecImportPage page4;
	private IConfigurationElement config;
    private IWorkbench workbench;
	private IStructuredSelection selection;
	private IProject project;
    
    public WizardMain() 
    {
    	super();
    }

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) 
    {
        this.workbench = workbench;
        this.selection = selection;
        setWindowTitle("Generate a new coDoc project");
    }
	
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException 
	{
		this.config = config;
	}

    /** 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
	public void addPages() 
    {
        super.addPages();
        page1 = new WelcomePage();
        addPage(page1);
        page2 = new WizardNewProjectCreationPage("page2");
        page2.setTitle("Create a new coDoc project");
        page2.setDescription("Create a new coDoc project.");
        addPage(page2);
        //page3 = new CodeImportPage();
        //addPage(page3);
        //page4 = new SpecImportPage();
        //addPage(page4);
    }

    /** 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
	public boolean performFinish() 
    {
		if (project != null) 
		{
			return true;
		}

		final IProject projectHandle = page2.getProjectHandle();

		URI projectURI = (!page2.useDefaults()) ? page2.getLocationURI() : null;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		final IProjectDescription desc = workspace.newProjectDescription(projectHandle.getName());

		desc.setLocationURI(projectURI);

		/*
		 * Just like the NewFileWizard, but this time with an operation object
		 * that modifies workspaces.
		 */
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException 
			{
				createProject(desc, projectHandle, monitor);
			}
		};

		/*
		 * This isn't as robust as the code in the BasicNewProjectResourceWizard
		 * class. Consider beefing this up to improve error handling.
		 */
		try 
		{
			getContainer().run(true, true, op);
		} 
		catch (InterruptedException e) 
		{
			return false;
		} 
		catch (InvocationTargetException e) 
		{
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}

		project = projectHandle;

		if (project == null) 
		{
			return false;
		}

		// switching to the specified perspective.
		// plugin.xml: finalPerspective="coDoc.perspectives.Perspective"
		BasicNewProjectResourceWizard.updatePerspective(config);
		BasicNewResourceWizard.selectAndReveal(project, workbench.getActiveWorkbenchWindow());
		
		//page3.finish();
		//page4.finish();

		return true;
    }
    
	void createProject(IProjectDescription description, IProject proj, IProgressMonitor monitor) throws CoreException, OperationCanceledException 
	{
		try 
		{
			monitor.beginTask("", 2000);

			proj.create(description, new SubProgressMonitor(monitor, 1000));

			if (monitor.isCanceled()) 
			{
				throw new OperationCanceledException();
			}

			proj.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));

			/*
			 * Okay, now we have the project and we can do more things with it
			 * before updating the perspective.
			 */
			IContainer container = proj;

			/* Add an readme file */
			addFileToProject(container, new Path("readme.txt"), openContentStream(), monitor);
			/* Add an cdc file */
			InputStream resourceStream1 = this.getClass().getResourceAsStream("templates/cdc-template.resource");
			String cdcFilename = "."+proj.getName()+".cdc";
			final IFile file1 = container.getFile(new Path(cdcFilename));
			if (file1.exists()) 
			{
				file1.setContents(resourceStream1, true, true, monitor);
			} 
			else 
			{
				file1.create(resourceStream1, true, monitor);
			}
			resourceStream1.close();
			
    		CDCModel cdcModel = new CDCModel();
    		String filepath = proj.getLocation().toString()+File.separatorChar+cdcFilename;
    		SimpleXML.writeCDCModel(cdcModel, filepath);
			
			/* Add the style folder and the site.css file to it */
			final IFolder specFolder = container.getFolder(new Path("spec"));
			specFolder.create(true, true, monitor);
			/* InputStream resourceStream2 = this.getClass().getResourceAsStream("templates/pdf-sample.resource");
			final IFile file2 = container.getFile(new Path(specFolder.getName() + IPath.SEPARATOR + "sample.pdf"));
			if (file2.exists()) 
			{
				file2.setContents(resourceStream2, true, true, monitor);
			} 
			else 
			{
				file2.create(resourceStream2, true, monitor);
			}
			resourceStream2.close(); */
			
			/*
			 * Add the code folder.
			 */
			IFolder codeFolder = container.getFolder(new Path("code"));
			codeFolder.create(true, true, monitor);
			/* InputStream resourceStream3 = this.getClass().getResourceAsStream("templates/c-sample.resource");
			addFileToProject(container, new Path(codeFolder.getName() + IPath.SEPARATOR + "sample.c"), resourceStream3, monitor);
			resourceStream3.close(); */

			monitor.worked(1);
			monitor.setTaskName("Opening file for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() 
				{
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try 
					{
						IDE.openEditor(page, file1, true);
					} 
					catch (PartInitException e) 
					{
					}
				}
			});
			monitor.worked(1);
		} 
		catch (IOException ioe) 
		{
			IStatus status = new Status(IStatus.ERROR, "coDocWizard", IStatus.OK, ioe.getLocalizedMessage(), null);
			throw new CoreException(status);
		} 
		finally 
		{
			monitor.done();
		}
	}
	
	private void addFileToProject(IContainer container, Path path, InputStream contentStream, IProgressMonitor monitor) throws CoreException 
	{
		final IFile file = container.getFile(path);

		if (file.exists()) 
		{
			file.setContents(contentStream, true, true, monitor);
		} 
		else 
		{
			file.create(contentStream, true, monitor);
		}
	}

	private InputStream openContentStream() 
	{
		String contents =
			"coDoc: readmefile.";
		return new ByteArrayInputStream(contents.getBytes());
	}
}
