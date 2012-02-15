package edu.pdx.svl.coDoc.cdt.core;

import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.core.runtime.content.IContentTypeMatcher;
import org.osgi.framework.BundleContext;

public class CCorePlugin extends Plugin
{
	public static final String PLUGIN_ID = "edu.pdx.svl.coDoc.cdt.core"; //$NON-NLS-1$
	
	public static final int STATUS_CDTPROJECT_EXISTS = 1;
	public static final int STATUS_CDTPROJECT_MISMATCH = 2;
	public static final int CDT_PROJECT_NATURE_ID_MISMATCH = 3;
	/**
	 * IContentType id for C Source Unit
	 */
	public final static String CONTENT_TYPE_CSOURCE =  "edu.pdx.svl.coDoc.cdt.core.cSource"; //$NON-NLS-1$
	/**
	 * IContentType id for C Header Unit
	 */
	public final static String CONTENT_TYPE_CHEADER =  "edu.pdx.svl.coDoc.cdt.core.cHeader"; //$NON-NLS-1$
	/**
	 * IContentType id for C++ Source Unit
	 */
	public final static String CONTENT_TYPE_CXXSOURCE = "edu.pdx.svl.coDoc.cdt.core.cxxSource"; //$NON-NLS-1$
	/**
	 * IContentType id for C++ Header Unit
	 */
	public final static String CONTENT_TYPE_CXXHEADER = "edu.pdx.svl.coDoc.cdt.core.cxxHeader"; //$NON-NLS-1$
	
	
  private static CCorePlugin fgCPlugin;
  private CoreModel fCoreModel;
  
  public CCorePlugin() {
    super();
    fgCPlugin = this;
  }
  
  public static CCorePlugin getDefault() {
    return fgCPlugin;
  }
  
  /**
   * @see Plugin#startup
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    // Fired up the model.
    System.out.println("Starting the Core Plugin");
    fCoreModel = CoreModel.getDefault();
    fCoreModel.startup();
  }
  
  /**
   * @see Plugin#shutdown
   */
  public void stop(BundleContext context) throws Exception {
    try {
      if (fCoreModel != null) {
        fCoreModel.shutdown();
      }
    } finally {
      super.stop(context);
    }
  }

	/**
	 * Helper function, returning the contenttype for a filename
	 * Same as: <p><p>
	 * 	getContentType(null, filename)
	 * <br>
	 * @param project
	 * @param name
	 * @return
	 */
	public static IContentType getContentType(String filename) {
		return getContentType(null, filename);
	}
	
	/**
	 * Helper function, returning the contenttype for a filename
	 * @param project
	 * @param name
	 * @return
	 */
	public static IContentType getContentType(IProject project, String filename) {
		// Always try in the workspace (for multi-language support)
		// try with the project settings
		if (project != null) {
			try {
				IContentTypeMatcher matcher = project.getContentTypeMatcher();
				IContentType ct = matcher.findContentTypeFor(filename);
				if (ct != null) return ct;
			} catch (CoreException e) {
				// ignore. 
			}
		}
		// Try in the workspace.
		IContentTypeManager manager = Platform.getContentTypeManager();
		return manager.findContentTypeFor(filename);
	}
  
/**
 * Creates a C project resource given the project handle and description.
 *
 * @param description the project description to create a project resource for
 * @param projectHandle the project handle to create a project resource for
 * @param monitor the progress monitor to show visual progress with
 *
 * @exception CoreException if the operation fails
 * @exception OperationCanceledException if the operation is canceled
 */
public IProject createCProject(
    final IProjectDescription description,
    final IProject projectHandle,
    IProgressMonitor monitor,
    final String projectID)
throws CoreException, OperationCanceledException {
  
  ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
    public void run(IProgressMonitor monitor) throws CoreException {
      try {
        if (monitor == null) {
          monitor = new NullProgressMonitor();
        }
        monitor.beginTask("Creating C Project...", 3); //$NON-NLS-1$
        if (!projectHandle.exists()) {
          projectHandle.create(description, new SubProgressMonitor(monitor, 1));
        }

        if (monitor.isCanceled()) {
          throw new OperationCanceledException();
        }

        // Open first.
        projectHandle.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1));

        // Add C Nature ... does not add duplicates
        
        CProjectNature.addCNature(projectHandle, new SubProgressMonitor(monitor, 1));
        if (projectID == "CC")
          convertProjectFromCtoCC(projectHandle, monitor);
      } finally {
        monitor.done();
      }
    }
  }, ResourcesPlugin.getWorkspace().getRoot(), 0, monitor);
  return projectHandle;
}

/**
 * Method convertProjectFromCtoCC converts
 * a C Project to a C++ Project
 * The newProject MUST, not be null, already have a C Nature 
 * && must NOT already have a C++ Nature
 * 
 * @param projectHandle
 * @param monitor
 * @throws CoreException
 */

public void convertProjectFromCtoCC(IProject projectHandle, IProgressMonitor monitor) throws CoreException {
  if ((projectHandle != null)
    && projectHandle.hasNature(CProjectNature.C_NATURE_ID)
    && !projectHandle.hasNature(CCProjectNature.CC_NATURE_ID)) {
    // Add C++ Nature ... does not add duplicates        
    CCProjectNature.addCCNature(projectHandle, monitor);
  }
}
}
