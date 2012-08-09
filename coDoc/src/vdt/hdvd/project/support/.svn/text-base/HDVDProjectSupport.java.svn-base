/**
 * 
 */
package vdt.hdvd.project.support;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class HDVDProjectSupport {
	
	public final static String VerilogSourceFolder = "hw_design"; //$NON-NLS-1$
	public final static String QEMUSourceFolder = "sw_design"; //$NON-NLS-1$
	
	/**
	 * 
	 * For this marvelous project we need to:
	 * - create the default Eclipse project
	 * - add the custom project nature
	 * - create the folder structure
	 * 
	 * @param projectName
	 * @param location
	 * @param natureId
	 * @return
	 */
	public static IProject createProject(String projectName, URI location) {

		Assert.isNotNull(projectName);
		// Assert.isTrue(projectName.trim().length() & gt; 0);

		IProject project = createBaseProject(projectName, location);
		
		try {
			addNature(project);
			String[] paths = {
					HDVDProjectSupport.VerilogSourceFolder, 
					HDVDProjectSupport.QEMUSourceFolder};
			addToProjectStructure(project, paths);
//			HDVDProjectManager.createSvdProjectConfig(project);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
			project = null;
		}

		return project;
	}

	/**
	 * Just do the basics: create a basic project.
	 * @param location
	 * @param projectName
	 */
	private static IProject createBaseProject(String projectName, URI location) {
		// it is acceptable to use the ResourcesPlugin class
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = newProject.getWorkspace()
					.newProjectDescription(newProject.getName());
			if (location != null
					&& ResourcesPlugin.getWorkspace().getRoot()
							.getLocationURI().equals(location)) {
				projectLocation = null;
			}
			desc.setLocationURI(projectLocation);

			try {
				newProject.create(desc, null);
				if (!newProject.isOpen()) {
					newProject.open(null);
				}

			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return newProject;
	}

	private static void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}

		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	/**
	 * 
	 * Create a folder structure with a parent root, overlay, and a few child
	 * folders.
	 * 
	 * @param newProject
	 * @param paths
	 * @throws CoreException
	 */

	private static void addToProjectStructure(IProject newProject,
			String[] paths) throws CoreException {
		for (String path : paths) {
			IFolder etcFolders = newProject.getFolder(path);
			createFolder(etcFolders);
		}
	}

	private static void addNature(IProject project) throws CoreException {
		if (!project.hasNature(HDVDProjectNature.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = HDVDProjectNature.NATURE_ID;
			description.setNatureIds(newNatures);
			IProgressMonitor monitor = null;
			project.setDescription(description, monitor);
		}
	}
}
