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

import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ICContainer;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceRoot;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

public class CContainer extends Openable implements ICContainer {
	CModelManager factory = CModelManager.getDefault();

	public CContainer(ICElement parent, IResource res) {
		this(parent, res, ICElement.C_CCONTAINER);
	}

	public CContainer(ICElement parent, IResource res, int type) {
		super(parent, res, type);
	}

	/**
	 * @see ICContainer#getTranslationUnits()
	 */
	public ITranslationUnit[] getTranslationUnits() throws CModelException {
		List list = getChildrenOfType(C_UNIT);
		ITranslationUnit[] array = new ITranslationUnit[list.size()];
		list.toArray(array);
		return array;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ICContainer#getTranslationUnit(java.lang.String)
	 */
	public ITranslationUnit getTranslationUnit(String name) {
		IFile file = getContainer().getFile(new Path(name));
		return getTranslationUnit(file);
	}

	public ITranslationUnit getTranslationUnit(IFile file) {
		String id = CoreModel.getRegistedContentTypeId(file.getProject(), file.getName());
		return new TranslationUnit(this, file, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ICContainer#getCContainers()
	 */
	public ICContainer[] getCContainers() throws CModelException {
		List list = getChildrenOfType(C_CCONTAINER);
		ICContainer[] array = new ICContainer[list.size()];
		list.toArray(array);
		return array;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ICContainer#getCContainer(java.lang.String)
	 */
	public ICContainer getCContainer(String name) {
		IFolder folder = getContainer().getFolder(new Path(name));
		return getCContainer(folder);
	}

	public ICContainer getCContainer(IFolder folder) {
		return new CContainer(this, folder);
	}

	public IContainer getContainer() {
		return (IContainer) getResource();
	}

	protected CElementInfo createElementInfo() {
		return new CContainerInfo(this);
	}

	// CHECKPOINT: folders will return the hash code of their path
	public int hashCode() {
		return getPath().hashCode();
	}

	/**
	 * @see Openable
	 */
	protected boolean buildStructure(OpenableInfo info, IProgressMonitor pm, Map newElements, IResource underlyingResource)
			 {
		boolean validInfo = false;
		try {
			IResource res = getResource();
			if (res != null && res.isAccessible()) {
				validInfo = computeChildren(info, res);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} finally {
			if (!validInfo) {
				CModelManager.getDefault().removeInfo(this);
			}
		}
		return validInfo;
	}

	protected boolean computeChildren(OpenableInfo info, IResource res) throws CoreException  {
		ArrayList vChildren = new ArrayList();
			IResource[] resources = null;
			if (res instanceof IContainer) {
				//System.out.println (" Resource: " +
				// res.getFullPath().toOSString());
				IContainer container = (IContainer) res;
				resources = container.members(false);
			}
			if (resources != null) {
				ICProject cproject = getCProject();
				ISourceRoot sroot = getSourceRoot();
				for (int i = 0; i < resources.length; i++) {
					if (sroot.isOnSourceEntry(resources[i])) {
						// Check for Valid C Element only.
						ICElement celement = computeChild(resources[i], cproject);
						if (celement != null) {
							vChildren.add(celement);
						}
					}
				}
			}
		info.setChildren(vChildren);
		return true;
	}

	protected ICElement computeChild(IResource res, ICProject cproject)  {
		ICElement celement = null;
		switch (res.getType()) {
			case IResource.FILE : {
				IFile file = (IFile) res;
				String id = CoreModel.getRegistedContentTypeId(file.getProject(), file.getName());
				if (id != null) {
					celement = new TranslationUnit(this, file, id);
				}
				break;
			}
			case IResource.FOLDER :
				celement = new CContainer(this, res);
				break;
		}
		return celement;
	}
}

