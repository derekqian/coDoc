
/*******************************************************************************
 * Copyright (c) 2000, 2004 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 * A C project represents a view of a project resource in terms of C elements
 * such as , ICContainer, ITranslationUnit ....
 * <code>CCore.create(project)</code>.
 * </p>
 * 
 * @see CCore#create(org.eclipse.core.resources.IProject)
 * @see IBuildEntry
 */
public interface ICProject extends IParent, IOpenable, ICElement {

	/**
	 * Returns the <code>ICElement</code> corresponding to the given path, or
	 * <code>null</code> if no such <code>ICElement</code> is found.
	 * 
	 * @exception CModelException
	 *                if the given path is <code>null</code> or absolute
	 */
	ICElement findElement(IPath path) throws CModelException;

	/**
	 * Returns the source root folders of the project.
	 * 
	 * <p>
	 * NOTE: This is equivalent to <code>getChildren()</code>.
	 * 
	 * @return ISourceRoot - root folders
	 * @exception CModelException
	 */
	ISourceRoot[] getSourceRoots() throws CModelException;

	/**
	 * 
	 * @param entry
	 * @return ISourceRoot
	 * @throws CModelException
	 */
	ISourceRoot getSourceRoot(ISourceEntry entry) throws CModelException;

	ISourceRoot findSourceRoot(IResource resource);

	ISourceRoot findSourceRoot(IPath path);

	/**
	 * 
	 * @return IProject
	 */
	IProject getProject();

}
