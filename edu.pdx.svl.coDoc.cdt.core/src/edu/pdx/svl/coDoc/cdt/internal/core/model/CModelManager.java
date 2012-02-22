package edu.pdx.svl.coDoc.cdt.internal.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ElementChangedEvent;
import edu.pdx.svl.coDoc.cdt.core.model.ICContainer;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.ICElementDelta;
import edu.pdx.svl.coDoc.cdt.core.model.ICModel;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.core.model.IElementChangedListener;
import edu.pdx.svl.coDoc.cdt.core.model.IParent;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceRoot;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

public class CModelManager implements IResourceChangeListener {
	/**
	 * Unique handle onto the CModel
	 */
	final CModel cModel = new CModel();

	/**
	 * Collection of listeners for C element deltas
	 */
	protected List fElementChangedListeners = Collections
			.synchronizedList(new ArrayList());

	/**
	 * Turns delta firing on/off. By default it is on.
	 */
	protected boolean fFire = true;

	/**
	 * Queue of deltas created explicily by the C Model that have yet to be
	 * fired.
	 */
	List fCModelDeltas = Collections.synchronizedList(new ArrayList());

	/**
	 * Queue of reconcile deltas on working copies that have yet to be fired.
	 * This is a table form IWorkingCopy to IJavaElementDelta
	 */
	HashMap reconcileDeltas = new HashMap();

	/**
	 * Used to convert <code>IResourceDelta</code>s into
	 * <code>ICElementDelta</code>s.
	 */
	protected DeltaProcessor fDeltaProcessor = new DeltaProcessor();

	static CModelManager factory = null;

	/*
	 * Temporary cache of newly opened elements
	 */
	private ThreadLocal temporaryCache = new ThreadLocal();

	public static final int DEFAULT_CHANGE_EVENT = 0; // must not collide with

	// ElementChangedEvent
	// event masks

	public static final boolean VERBOSE = false;

	/**
	 * Set of elements which are out of sync with their buffers.
	 */
	protected Map elementsOutOfSynchWithBuffers = new HashMap(11);

	/**
	 * A map from ITranslationUnit to IWorkingCopy of the shared working copies.
	 */
	public Map sharedWorkingCopies = new HashMap();

	/**
	 * This is a cache of the projects before any project addition/deletion has
	 * started.
	 */
	public ICProject[] cProjectsCache;

	/**
	 * Infos cache.
	 */
	protected CModelCache cache = new CModelCache();

	public void startup() {
	}

	public static CModelManager getDefault() {
		if (factory == null) {
			factory = new CModelManager();
			// Register to the workspace;
			ResourcesPlugin.getWorkspace().addResourceChangeListener(
					factory,
					IResourceChangeEvent.POST_CHANGE
							| IResourceChangeEvent.PRE_DELETE
							| IResourceChangeEvent.PRE_CLOSE);
		}
		return factory;
	}

	/**
	 * Returns the CModel for the given workspace, creating it if it does not
	 * yet exist.
	 */
	public ICModel getCModel(IWorkspaceRoot root) {
		return getCModel();
	}

	public CModel getCModel() {
		return cModel;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getSource() instanceof IWorkspace) {
			IResourceDelta delta = event.getDelta();
			try {
				if (delta != null) {
					ICElementDelta[] translatedDeltas = fDeltaProcessor
							.processResourceDelta(delta);
					if (translatedDeltas.length > 0) {
						for (int i = 0; i < translatedDeltas.length; i++) {
							registerCModelDelta(translatedDeltas[i]);
						}
					}
					fire(ElementChangedEvent.POST_CHANGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void fire(int eventType) {
		fire(null, eventType);
	}

	/**
	 * Fire C Model deltas, flushing them after the fact. If the firing mode has
	 * been turned off, this has no effect.
	 */
	void fire(ICElementDelta customDeltas, int eventType) {
		if (fFire) {
			ICElementDelta deltaToNotify;
			if (customDeltas == null) {
				deltaToNotify = mergeDeltas(this.fCModelDeltas);
			} else {
				deltaToNotify = customDeltas;
			}

			IElementChangedListener[] listeners;
			int listenerCount;
			int[] listenerMask;
			// Notification
			synchronized (fElementChangedListeners) {
				listeners = new IElementChangedListener[fElementChangedListeners
						.size()];
				fElementChangedListeners.toArray(listeners);
				listenerCount = listeners.length;
				listenerMask = null;
			}
			// firePostChangeDelta(deltaToNotify, listeners, listenerMask,
			// listenerCount);
			// fireReconcileDelta(listeners, listenerMask, listenerCount);
		}
	}

	private ICElementDelta mergeDeltas(Collection deltas) {

		synchronized (deltas) {
			if (deltas.size() == 0)
				return null;
			if (deltas.size() == 1)
				return (ICElementDelta) deltas.iterator().next();
			if (deltas.size() <= 1)
				return null;

			Iterator iterator = deltas.iterator();
			ICElement cRoot = getCModel();
			CElementDelta rootDelta = new CElementDelta(cRoot);
			boolean insertedTree = false;
			while (iterator.hasNext()) {
				CElementDelta delta = (CElementDelta) iterator.next();
				ICElement element = delta.getElement();
				if (cRoot.equals(element)) {
					ICElementDelta[] children = delta.getAffectedChildren();
					for (int j = 0; j < children.length; j++) {
						CElementDelta projectDelta = (CElementDelta) children[j];
						rootDelta.insertDeltaTree(projectDelta.getElement(),
								projectDelta);
						insertedTree = true;
					}
					IResourceDelta[] resourceDeltas = delta.getResourceDeltas();
					if (resourceDeltas != null) {
						for (int i = 0, length = resourceDeltas.length; i < length; i++) {
							rootDelta.addResourceDelta(resourceDeltas[i]);
							insertedTree = true;
						}
					}
				} else {
					rootDelta.insertDeltaTree(element, delta);
					insertedTree = true;
				}
			}
			if (insertedTree) {
				return rootDelta;
			}
			return null;
		}
	}

	public ICElement create(IPath path) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// Assume it is fullpath relative to workspace
		IResource res = root.findMember(path);
		if (res == null) {
			IPath rootPath = root.getLocation();
			if (path.equals(rootPath)) {
				return getCModel(root);
			}
			res = root.getContainerForLocation(path);
			if (res == null || !res.exists()) {
				res = root.getFileForLocation(path);
			}
			if (res != null && !res.exists()) {
				res = null;
			}
		}

		// In case this is an external resource see if we can find
		// a file for it.
		if (res == null) {
			IFile[] files = root.findFilesForLocation(path);
			if (files.length > 0) {
				res = files[0];
			}
		}
		return create(res, null);
	}

	public ICElement create(IResource resource, ICProject cproject) {
		if (resource == null) {
			return null;
		}

		int type = resource.getType();
		switch (type) {
		case IResource.PROJECT:
			return create((IProject) resource);
		case IResource.FILE:
			return create((IFile) resource, cproject);
		case IResource.FOLDER:
			return create((IFolder) resource, cproject);
		case IResource.ROOT:
			return getCModel((IWorkspaceRoot) resource);
		default:
			return null;
		}
	}

	public ICProject create(IProject project) {
		if (project == null) {
			return null;
		}
		return cModel.getCProject(project);
	}

	public ICContainer create(IFolder folder, ICProject cproject) {
		if (folder == null) {
			return null;
		}
		if (cproject == null) {
			cproject = create(folder.getProject());
		}
		ICContainer celement = null;
		IPath resourcePath = folder.getFullPath();
		try {
			ISourceRoot[] roots = cproject.getSourceRoots();
			for (int i = 0; i < roots.length; ++i) {
				ISourceRoot root = roots[i];
				IPath rootPath = root.getPath();
				if (rootPath.equals(resourcePath)) {
					celement = root;
					break; // We are done.
				} else if (root.isOnSourceEntry(folder)) {
					IPath path = resourcePath.removeFirstSegments(rootPath
							.segmentCount());
					String[] segments = path.segments();
					ICContainer cfolder = root;
					for (int j = 0; j < segments.length; j++) {
						cfolder = cfolder.getCContainer(segments[j]);
					}
					celement = cfolder;
				}
			}
		} catch (CModelException e) {
			//
		}
		return celement;
	}

	public ICElement create(IFile file, ICProject cproject) {
		if (file == null) {
			return null;
		}
		if (cproject == null) {
			cproject = create(file.getProject());
		}
		ICElement celement = null;
		IPath rootPath = cproject.getPath();
		IPath resourcePath = file.getFullPath();
		IPath path = resourcePath.removeFirstSegments(rootPath.segmentCount());
		String fileName = path.lastSegment();
		if (CoreModel.isValidTranslationUnitName(cproject.getProject(),
				fileName)) {
			IFile newFile = cproject.getProject().getFile(fileName);
			String id = CoreModel.getRegistedContentTypeId(cproject
					.getProject(), newFile.getName());
			celement = new TranslationUnit(cproject, file, id);
		}
		return celement;
	}

	/**
	 * Registers the given delta with this manager. This API is to be used to
	 * registerd deltas that are created explicitly by the C Model. Deltas
	 * created as translations of <code>IResourceDeltas</code> are to be
	 * registered with <code>#registerResourceDelta</code>.
	 */
	public void registerCModelDelta(ICElementDelta delta) {
		fCModelDeltas.add(delta);
	}

	/**
	 * Returns the set of elements which are out of synch with their buffers.
	 */
	protected Map getElementsOutOfSynchWithBuffers() {
		return this.elementsOutOfSynchWithBuffers;
	}

	public void releaseCElement(ICElement celement) {

		// Guard.
		if (celement == null)
			return;

		// System.out.println("RELEASE " + celement.getElementName());

		// Remove from the containers.
		if (celement instanceof IParent) {
			CElementInfo info = (CElementInfo) peekAtInfo(celement);
			if (info != null) {
				ICElement[] children = info.getChildren();
				for (int i = 0; i < children.length; i++) {
					releaseCElement(children[i]);
				}
			}

			// Make sure any object specifics not part of the children be
			// destroy
			// For example the CProject needs to destroy the BinaryContainer and
			// ArchiveContainer
			if (celement instanceof CElement) {
				try {
					((CElement) celement).closing(info);
				} catch (CModelException e) {
					//
				}
			}
		}

		// Remove the child from the parent list.
		// Parent parent = (Parent)celement.getParent();
		// if (parent != null && peekAtInfo(parent) != null) {
		// parent.removeChild(celement);
		// }
		removeInfo(celement);
	}

	/*
	 * Puts the infos in the given map (keys are ICElements and values are
	 * CElementInfos) in the C model cache in an atomic way. First checks that
	 * the info for the opened element (or one of its ancestors) has not been
	 * added to the cache. If it is the case, another thread has opened the
	 * element (or one of its ancestors). So returns without updating the cache.
	 */
	protected synchronized void putInfos(ICElement openedElement,
			Map newElements) {
		// remove children
		Object existingInfo = this.cache.peekAtInfo(openedElement);
		if (openedElement instanceof IParent
				&& existingInfo instanceof CElementInfo) {
			ICElement[] children = ((CElementInfo) existingInfo).getChildren();
			for (int i = 0, size = children.length; i < size; ++i) {
				CElement child = (CElement) children[i];
				try {
					child.close();
				} catch (CModelException e) {
					// ignore
				}
			}
		}

		Iterator iterator = newElements.keySet().iterator();
		while (iterator.hasNext()) {
			ICElement element = (ICElement) iterator.next();
			Object info = newElements.get(element);
			this.cache.putInfo(element, info);
		}
	}

	/**
	 * Returns the info for the element.
	 */
	public synchronized Object getInfo(ICElement element) {
		HashMap tempCache = (HashMap) this.temporaryCache.get();
		if (tempCache != null) {
			Object result = tempCache.get(element);
			if (result != null) {
				return result;
			}
		}
		return this.cache.getInfo(element);
	}

	/**
	 * Returns the info for this element without disturbing the cache ordering.
	 */
	protected synchronized Object peekAtInfo(ICElement element) {
		HashMap tempCache = (HashMap) this.temporaryCache.get();
		if (tempCache != null) {
			Object result = tempCache.get(element);
			if (result != null) {
				return result;
			}
		}
		return this.cache.peekAtInfo(element);
	}

	/**
	 * Removes the info of this model element.
	 */
	protected synchronized void removeInfo(ICElement element) {
		this.cache.removeInfo(element);
	}

	/**
	 * Removes all cached info from the C Model, including all children, but
	 * does not close this element.
	 */
	protected synchronized void removeChildrenInfo(ICElement openedElement) {
		// remove children
		Object existingInfo = this.cache.peekAtInfo(openedElement);
		if (openedElement instanceof IParent
				&& existingInfo instanceof CElementInfo) {
			ICElement[] children = ((CElementInfo) existingInfo).getChildren();
			for (int i = 0, size = children.length; i < size; ++i) {
				CElement child = (CElement) children[i];
				try {
					child.close();
				} catch (CModelException e) {
					// ignore
				}
			}
		}
	}

	/*
	 * Returns the temporary cache for newly opened elements for the current
	 * thread. Creates it if not already created.
	 */
	public HashMap getTemporaryCache() {
		HashMap result = (HashMap) this.temporaryCache.get();
		if (result == null) {
			result = new HashMap();
			this.temporaryCache.set(result);
		}
		return result;
	}

	/*
	 * Returns whether there is a temporary cache for the current thread.
	 */
	public boolean hasTemporaryCache() {
		return this.temporaryCache.get() != null;
	}

	/*
	 * Resets the temporary cache for newly created elements to null.
	 */
	public void resetTemporaryCache() {
		this.temporaryCache.set(null);
	}

	public void shutdown() {
		// Do any shutdown of services.
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(factory);
	}

	/**
	 * addElementChangedListener method comment.
	 */
	public void addElementChangedListener(IElementChangedListener listener) {
		synchronized (fElementChangedListeners) {
			if (!fElementChangedListeners.contains(listener)) {
				fElementChangedListeners.add(listener);
			}
		}
	}

	/**
	 * removeElementChangedListener method comment.
	 */
	public void removeElementChangedListener(IElementChangedListener listener) {
		synchronized (fElementChangedListeners) {
			int i = fElementChangedListeners.indexOf(listener);
			if (i != -1) {
				fElementChangedListeners.remove(i);
			}
		}
	}
}
