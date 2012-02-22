/*******************************************************************************
 * Copyright (c) 2000, 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.CCProjectNature;
import edu.pdx.svl.coDoc.cdt.core.CProjectNature;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceEntry;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceRoot;
import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

public class CProject extends Openable implements ICProject {

	public CProject(ICElement parent, IProject project) {
		super(parent, project, ICElement.C_PROJECT);
	}

	public IProject getProject() {
		return getUnderlyingResource().getProject();
	}

	public ICElement findElement(IPath path) throws CModelException {
		ICElement celem = null;
		if (path.isAbsolute()) {
			celem = CModelManager.getDefault().create(path);
		} else {
			IProject project = getProject();
			if (project != null) {
				IPath p = project.getFullPath().append(path);
				celem = CModelManager.getDefault().create(p);
			}
		}
		return celem;
	}

	public static boolean hasCNature(IProject p) {
		try {
			return p.hasNature(CProjectNature.C_NATURE_ID);
		} catch (CoreException e) {
			// throws exception if the project is not open.
		}
		return false;
	}

	public static boolean hasCCNature(IProject p) {
		try {
			return p.hasNature(CCProjectNature.CC_NATURE_ID);
		} catch (CoreException e) {
			// throws exception if the project is not open.
		}
		return false;
	}

	private boolean isCProject() {
		return hasCNature(getProject()) || hasCCNature(getProject());
	}

	/**
	 * Returns true if this handle represents the same C project as the given
	 * handle. Two handles represent the same project if they are identical or
	 * if they represent a project with the same underlying resource and
	 * occurrence counts.
	 * 
	 * @see CElement#equals(Object)
	 */
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof CProject))
			return false;

		CProject other = (CProject) o;
		return getProject().equals(other.getProject());
	}

	protected CElementInfo createElementInfo() {
		return new CProjectInfo(this);
	}

	// CHECKPOINT: CProjects will return the hash code of their underlying
	// IProject
	public int hashCode() {
		return getProject().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.core.model.ICProject#getSourceRoot(org.dworks.bbcdt.core.model.ISourceEntry)
	 */
	public ISourceRoot getSourceRoot(ISourceEntry entry) throws CModelException {
		IPath p = getPath();
		IPath sp = entry.getPath();
		if (p.isPrefixOf(sp)) {
			int count = sp.matchingFirstSegments(p);
			sp = sp.removeFirstSegments(count);
			IResource res = null;
			if (sp.isEmpty()) {
				res = getProject();
			} else {
				res = getProject().findMember(sp);
			}
			if (res != null) {
				return new SourceRoot(this, res, entry);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.core.model.ICProject#findSourceRoot()
	 */
	public ISourceRoot findSourceRoot(IResource res) {
		try {
			ISourceRoot[] roots = getSourceRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isOnSourceEntry(res)) {
					return roots[i];
				}
			}
		} catch (CModelException e) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.core.model.ICProject#findSourceRoot()
	 */
	public ISourceRoot findSourceRoot(IPath path) {
		try {
			ISourceRoot[] roots = getSourceRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].getPath().equals(path)) {
					return roots[i];
				}
			}
		} catch (CModelException e) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.core.model.ICProject#getSourceRoots()
	 */
	public ISourceRoot[] getSourceRoots() throws CModelException {
		Object[] children;

		children = getChildren();
		ArrayList roots = null;
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof ISourceRoot)
				roots.add(children[i]);
		}
		if (roots == null)
			return null;
		else
			return (ISourceRoot[]) roots.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.internal.core.model.Openable#buildStructure(org.dworks.bbcdt.internal.core.model.OpenableInfo,
	 *      org.eclipse.core.runtime.IProgressMonitor, java.util.Map,
	 *      org.eclipse.core.resources.IResource)
	 */
	protected boolean buildStructure(OpenableInfo info, IProgressMonitor pm,
			Map newElements, IResource underlyingResource)
			throws CModelException {
		boolean validInfo = false;
		try {
			IResource res = getResource();
			if (res != null && res.isAccessible()) {
				validInfo = computeSourceRoots(info, res);
			}
		} finally {
			if (!validInfo) {
				CModelManager.getDefault().removeInfo(this);
			}
		}
		return validInfo;
	}

	protected List computeSourceRoots() throws CModelException {
		ISourceRoot[] roots = getSourceRoots();
		if (roots == null)
			return null;
		ArrayList list = new ArrayList(roots.length);
		for (int i = 0; i < roots.length; i++) {
			if (roots[i] != null) {
				list.add(roots[i]);
			}
		}
		return list;
	}

	protected boolean computeSourceRoots(OpenableInfo info, IResource res)
			throws CModelException {
		// info.setChildren(computeSourceRoots());
		return true;
	}

	/*
	 * @see ICProject
	 */
	public boolean isOnSourceRoot(ICElement element) {
		try {
			ISourceRoot[] roots = getSourceRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isOnSourceEntry(element)) {
					return true;
				}
			}
		} catch (CModelException e) {
			// ..
		}
		return false;
	}

	/*
	 * @see ICProject
	 */
	public boolean isOnSourceRoot(IResource resource) {
		try {
			ISourceRoot[] roots = getSourceRoots();
			for (int i = 0; i < roots.length; i++) {
				if (roots[i].isOnSourceEntry(resource)) {
					return true;
				}
			}
		} catch (CModelException e) {
			//
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.core.model.ICElement#exists()
	 */
	public boolean exists() {
		if (!isCProject()) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dworks.bbcdt.internal.core.model.CElement#closing(java.lang.Object)
	 */
	protected void closing(Object info) throws CModelException {
		super.closing(info);
	}

	/*
	 * Resets this project's caches
	 */
	public void resetCaches() {
		CProjectInfo pinfo = (CProjectInfo) CModelManager.getDefault()
				.peekAtInfo(this);
		if (pinfo != null) {
			pinfo.resetCaches();
		}
	}
}
