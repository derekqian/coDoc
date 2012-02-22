package edu.pdx.svl.coDoc.cdt.core.model;

import java.util.ArrayList;

import edu.pdx.svl.coDoc.cdt.core.CCProjectNature;
import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.CProjectNature;
import edu.pdx.svl.coDoc.cdt.internal.core.model.CModel;
import edu.pdx.svl.coDoc.cdt.internal.core.model.CModelManager;
import edu.pdx.svl.coDoc.cdt.core.model.IElementChangedListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.content.IContentType;

public class CoreModel {
	private static CoreModel cmodel = null;

	private static CModelManager manager = CModelManager.getDefault();

	/**
	 * Return the singleton.
	 */
	public static CoreModel getDefault() {
		if (cmodel == null) {
			cmodel = new CoreModel();
		}
		return cmodel;
	}

	/**
	 * Returns the default ICModel.
	 */
	public ICModel getCModel() {
		return manager.getCModel();
	}

	/**
	 * @see Plugin#startup
	 */
	public void startup() {
		manager.startup();
	}

	/**
	 * Creates an ICElement from an IPath. Returns null if not found.
	 */
	public ICElement create(IPath path) {
		return manager.create(path);
	}

	/**
	 * Creates an ICElement from an IFile. Returns null if not found.
	 */
	public ICElement create(IFile file) {
		return manager.create(file, null);
	}

	/**
	 * Creates an ICElement from an IFolder. Returns null if not found.
	 */
	public ICContainer create(IFolder folder) {
		return manager.create(folder, null);
	}

	/**
	 * Creates an ICElement from an IProject. Returns null if not found.
	 */
	public ICProject create(IProject project) {
		if (project == null) {
			return null;
		}
		CModel cModel = manager.getCModel();
		return cModel.getCProject(project);
	}

	/**
	 * Creates an ICElement from an IResource. Returns null if not found.
	 */
	public ICElement create(IResource resource) {
		return manager.create(resource, null);
	}

	/**
	 * Returns the C model.
	 * 
	 * @param root
	 *            the given root
	 * @return the C model, or <code>null</code> if the root is null
	 */
	public static ICModel create(IWorkspaceRoot root) {
		if (root == null) {
			return null;
		}
		return manager.getCModel();
	}

	/**
	 * Return true if project has C nature.
	 */
	public static boolean hasCNature(IProject project) {
		boolean ok = false;
		try {
			ok = (project.isOpen() && project
					.hasNature(CProjectNature.C_NATURE_ID));
		} catch (CoreException e) {
			// throws exception if the project is not open.
			// System.out.println (e);
			// e.printStackTrace();
		}
		return ok;
	}

	/**
	 * Return true if project has C++ nature.
	 */
	public static boolean hasCCNature(IProject project) {
		boolean ok = false;
		try {
			ok = (project.isOpen() && project
					.hasNature(CCProjectNature.CC_NATURE_ID));
		} catch (CoreException e) {
			// throws exception if the project is not open.
			// System.out.println (e);
			// e.printStackTrace();
		}
		return ok;
	}

	/**
	 * Return true if name is a valid name for a translation unit.
	 */
	public static boolean isValidTranslationUnitName(IProject project,
			String name) {
		IContentType contentType = CCorePlugin.getContentType(project, name);
		if (contentType != null) {
			String id = contentType.getId();
			return CCorePlugin.CONTENT_TYPE_CHEADER.equals(id)
					|| CCorePlugin.CONTENT_TYPE_CXXHEADER.equals(id)
					|| CCorePlugin.CONTENT_TYPE_CSOURCE.equals(id)
					|| CCorePlugin.CONTENT_TYPE_CXXSOURCE.equals(id);
		}
		return false;
	}

	/**
	 * Return an array of the register contentTypes.
	 * 
	 * @return String[] ids
	 */
	public static String[] getRegistedContentTypeIds() {
		ArrayList a = new ArrayList();
		a.add(CCorePlugin.CONTENT_TYPE_CHEADER);
		a.add(CCorePlugin.CONTENT_TYPE_CSOURCE);
		a.add(CCorePlugin.CONTENT_TYPE_CXXHEADER);
		a.add(CCorePlugin.CONTENT_TYPE_CXXSOURCE);
		String[] result = new String[a.size()];
		a.toArray(result);
		return result;
	}

	/**
	 * Return the registed content type id, for example:
	 * <ul>
	 * <li>CONTENT_TYPE_CHEADER
	 * <li>CONTENT_TYPE_CXXHEADER
	 * <li>CONTENT_TYPE_CSOURCE
	 * <li>CONTENT_TYPE_CXXSOURCE
	 * </ul>
	 * or null is return if no id match the list
	 * 
	 * @param file
	 * @return the know id or null
	 */
	public static String getRegistedContentTypeId(IProject project, String name) {
		IContentType contentType = CCorePlugin.getContentType(project, name);
		if (contentType != null) {
			String id = contentType.getId();
			String[] ids = getRegistedContentTypeIds();
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(id)) {
					return id;
				}
			}
		}
		return null;
	}

	public void shutdown() {
		manager.shutdown();
	}

	public void addElementChangedListener(IElementChangedListener listener) {
		manager.addElementChangedListener(listener);
	}

	/**
	 * Removes the given element changed listener. Has no affect if an identical
	 * listener is not registered.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeElementChangedListener(IElementChangedListener listener) {
		manager.removeElementChangedListener(listener);
	}
}
