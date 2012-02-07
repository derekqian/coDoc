package coDocWizard;


import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

/**
 * The activator class controls the plug-in life cycle
 */
public class WizardMain extends Wizard implements INewWizard 
{
    private WelcomePage page1;
    private ProjectCreationPage page2;
    private IWorkbench workbench;
	private ISelection selection;
    
    public WizardMain() 
    {
         super();
         setNeedsProgressMonitor(true);
     }

	public void init(IWorkbench workbench, IStructuredSelection selection) 
    {
        this.workbench = workbench;
        this.selection = selection;
        setWindowTitle("Generate a new coDoc project");
    }

    /** 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() 
    {
        super.addPages();
        page1 = new WelcomePage();
        addPage(page1);
        page2 = new ProjectCreationPage(selection);
        addPage(page2);
    }

    /** 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish() 
    {
		final String containerName = page2.getContainerName();
		final String fileName = page2.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() 
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException 
			{
				try 
				{
					doFinish(containerName, fileName, monitor);
				} 
				catch (CoreException e) 
				{
					throw new InvocationTargetException(e);
				} 
				finally 
				{
					monitor.done();
				}
			}
		};
		try 
		{
			//run(boolean fork, boolean cancelable, IRunnableWithProgress runnable)
			getContainer().run(true, false, op);
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
		return true;
    }

	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException 
	{
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) 
		{
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try 
		{
			InputStream stream = openContentStream();
			if (file.exists()) 
			{
				file.setContents(stream, true, true, monitor);
			} 
			else 
			{
				file.create(stream, true, monitor);
			}
			stream.close();
		} 
		catch (IOException e) 
		{
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() 
			{
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try 
				{
					IDE.openEditor(page, file, true);
				} 
				catch (PartInitException e) 
				{
				}
			}
		});
		monitor.worked(1);
	}

	private InputStream openContentStream() 
	{
		String contents =
			"coDoc: This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException 
	{
		IStatus status = new Status(IStatus.ERROR, "NewFileWizard", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
