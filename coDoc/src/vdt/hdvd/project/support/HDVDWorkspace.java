/**
 * 
 */
package vdt.hdvd.project.support;

import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class HDVDWorkspace {

	/**
	 * Get the path of the current work space
	 * @return the path string
	 */
	public static String getWorkSpacePath()
	{
		return ResourcesPlugin.getWorkspace().getRoot().getFullPath().toOSString();
	}
	
	
	/**
	 * Get project by name
	 * @return the IProject instance of the project, null if not found
	 */
	public static IProject getProject(String name)
	{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if(null == projects || projects.length == 0)
		{
			return null;
		}	

		for(IProject proj : projects)
		{
			if(proj.getLocation().toFile().getName().equalsIgnoreCase(name))
			{
				return proj;
			}
		}		
		
		return null;
	}
	
	public static String getSelectedProjectName()
	{
		String name = null;
		
		IProject project = HDVDWorkspace.getSelectedProject();
		
		if(null != project)
		{
			return project.getLocation().toFile().getName();
		}
		
		return name;
	}
	
	/**
	 * Get all the projects in current workspace
	 * @return
	 */
	public static IProject[] getAllProjects()
	{
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}
	
	/**
	 * Get the currently active project
	 * @return the IProject instance of the currently selected project,
	 * null if there is no project selected.
	 */
	public static IProject getActiveProject()
	{
		IProject project = getOneOpenedProject();

/*		if((project = SvdWorkspace.getActiveFileProject()) != null)
		{
			Global.currentProject = project;
			return project;
		}
		
		if((project = SvdWorkspace.getSelectedProject()) != null)
		{
			Global.currentProject = project;
			return project;
		}
*/		
//		Global.currentProject = project;
		return project;
	}
	
	/**
	 * Get the only one opened projects
	 * @return the IProject instances of the currently opened project if there is only one project opened,
	 * null if there is no project opened or there are more than one project opened.
	 */
	private static IProject getOneOpenedProject()
	{
		IProject[] projects = getOpenedProjects();
		if(projects.length == 1)
			return projects[0];
		else if(projects.length > 1)
			MessageDialog.openInformation(null, "Error", "HDVD: Error: There are more than one projects opened!");
		return null;
	}
	
	/**
	 * Get the opened projects
	 * @return all the IProject instances,
	 * null if there is no project opened.
	 */
	private static IProject[] getOpenedProjects()
	{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		Vector<IProject> openedProjects = new Vector<IProject>();
		
		for(IProject project : projects)
		{
			if(project.isOpen())
			{
				openedProjects.add(project);
			}
		}
		
		return (IProject[]) openedProjects.toArray(new IProject[openedProjects.size()]);
	}
	
	/**
	 * Get the currently selected project
	 * @return the IProject instance of the currently selected project,
	 * null if there is no project selected.
	 */
	private static IProject getSelectedProject()
	{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if(null == projects || projects.length == 0)
		{
			return null;
		}	

		// Default return
		IProject active = null; 
		
		IWorkbenchWindow window =
			    PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(null == window)
		{
			return active;
		}
		
		IStructuredSelection selection = (IStructuredSelection) 
				window.getSelectionService().getSelection(IPageLayout.ID_PROJECT_EXPLORER);
		
		if(null == selection)
		{
			return active;
		}

		
		IPath selPath = null;
		Object obj = selection.getFirstElement();
		if(obj instanceof IProject)
		{
			selPath = ((IProject)selection.getFirstElement()).getLocation();
		} else if(obj instanceof IFile)
		{
			selPath = ((IFile) selection.getFirstElement()).getLocation();
		} else if(obj instanceof IFolder)
		{
			selPath = ((IFolder) selection.getFirstElement()).getLocation();
		} 
		
		if(null == selPath)
		{
			return active;
		}
		
		for(IProject proj : projects)
		{
			if(proj.getLocation().isPrefixOf(selPath))
			{
				return proj;
			}
		}
		
		return active;
	}
	
	/**
	 * get the project which the current edited file belongs to
	 * <--Added by Kai-->
	 * @return
	 */
	private static IProject getActiveFileProject()
	{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if(null == projects || projects.length == 0)
		{
			return null;
		}
		
		IWorkbenchWindow window =
			    PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if(null == window)
		{
			return null;
		}
		
		IWorkbenchPage page = window.getActivePage();
		if(null == page)
		{
			return null;
		}
		
		IEditorPart part = page.getActiveEditor();
		if(null == part)
		{
			return null;
		}

		IFile file = (IFile) part.getEditorInput().getAdapter(IFile.class);
		if(null == file)
		{
			return null;
		}
		
		IPath selPath = file.getLocation();
		if(null == selPath)
		{
			return null;
		}
		
		for(IProject proj : projects)
		{
			if(proj.getLocation().isPrefixOf(selPath))
			{
				return proj;
			}
		}
		
		return null;

	}

	/**
	 * Find a ViewPart with the given ID
	 * @param ID unique ID of the ViewPart to find
	 * @return The activated ViewPart or null if one is not found
	 */
    @SuppressWarnings("unchecked")
    public static <T> T findViewPart(String ID)
    {
        IWorkbench wb = PlatformUI.getWorkbench();
        if (null == wb) return null;
        for (IWorkbenchWindow wbw : wb.getWorkbenchWindows()) {
            for (IWorkbenchPage page : wbw.getPages()) {
                IViewPart vp = page.findView(ID);
                if (null != vp) {
                    page.activate(vp);
                    return (T) vp;
                }
            }
        }
        return null;
    }
}
