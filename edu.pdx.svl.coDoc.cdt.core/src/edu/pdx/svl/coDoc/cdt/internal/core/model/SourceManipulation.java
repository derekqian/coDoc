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

import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.IOpenable;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceManipulation;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceRange;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceReference;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Abstract class for C elements which implement ISourceReference.
 */

public class SourceManipulation extends Parent implements ISourceManipulation,
		ISourceReference {

	/**
	 * An empty list of Strings
	 */
	protected static final String[] fgEmptyStrings = {};

	public SourceManipulation(ICElement parent, String name, int type) {
		super(parent, name, type);
	}

	/**
	 * @see ISourceManipulation
	 */
	public void copy(ICElement container, ICElement sibling, String rename,
			boolean force, IProgressMonitor monitor) throws CModelException {
		if (container == null) {
			throw new IllegalArgumentException("operation.nullContainer"); //$NON-NLS-1$
		}
		ICElement[] elements = new ICElement[] { this };
		ICElement[] containers = new ICElement[] { container };
		ICElement[] siblings = null;
		if (sibling != null) {
			siblings = new ICElement[] { sibling };
		}
		String[] renamings = null;
		if (rename != null) {
			renamings = new String[] { rename };
		}
		getCModel().copy(elements, containers, siblings, renamings, force,
				monitor);
	}

	/**
	 * @see ISourceManipulation
	 */
	public void delete(boolean force, IProgressMonitor monitor)
			throws CModelException {
		ICElement[] elements = new ICElement[] { this };
		getCModel().delete(elements, force, monitor);
	}

	/**
	 * @see ISourceManipulation
	 */
	public void move(ICElement container, ICElement sibling, String rename,
			boolean force, IProgressMonitor monitor) throws CModelException {
		if (container == null) {
			throw new IllegalArgumentException("operation.nullContainer"); //$NON-NLS-1$
		}
		ICElement[] elements = new ICElement[] { this };
		ICElement[] containers = new ICElement[] { container };
		ICElement[] siblings = null;
		if (sibling != null) {
			siblings = new ICElement[] { sibling };
		}
		String[] renamings = null;
		if (rename != null) {
			renamings = new String[] { rename };
		}
		getCModel().move(elements, containers, siblings, renamings, force,
				monitor);
	}

	/**
	 * @see ISourceManipulation
	 */
	public void rename(String name, boolean force, IProgressMonitor monitor)
			throws CModelException {
		if (name == null) {
			throw new IllegalArgumentException("element.nullName"); //$NON-NLS-1$
		}
		ICElement[] elements = new ICElement[] { this };
		ICElement[] dests = new ICElement[] { this.getParent() };
		String[] renamings = new String[] { name };
		getCModel().rename(elements, dests, renamings, force, monitor);
	}

	/**
	 * @see IMember
	 */
	public ITranslationUnit getTranslationUnit() {
		try {
			return getSourceManipulationInfo().getTranslationUnit();
		} catch (CModelException e) {
			return null;
		}
	}

	/**
	 * Elements within compilation units and class files have no corresponding
	 * resource.
	 * 
	 * @see ICElement
	 */
	public IResource getCorrespondingResource() throws CModelException {
		return null;
	}

	/**
	 * Returns the first parent of the element that is an instance of IOpenable.
	 */
	public IOpenable getOpenableParent() {
		ICElement current = getParent();
		while (current != null) {
			if (current instanceof IOpenable) {
				return (IOpenable) current;
			}
			current = current.getParent();
		}
		return null;
	}

	/**
	 * @see ISourceReference
	 */
	public String getSource() throws CModelException {
		return getSourceManipulationInfo().getSource();
	}

	/**
	 * @see ISourceReference
	 */
	public ISourceRange getSourceRange() throws CModelException {
		return getSourceManipulationInfo().getSourceRange();
	}

	/**
	 * @see ICElement
	 */
	public IResource getUnderlyingResource() {
		return getParent().getUnderlyingResource();
	}

	public IResource getResource() {
		return null;
	}

	protected CElementInfo createElementInfo() {
		return new SourceManipulationInfo(this);
	}

	protected SourceManipulationInfo getSourceManipulationInfo()
			throws CModelException {
		return (SourceManipulationInfo) getElementInfo();
	}

	public boolean isIdentical(SourceManipulation other) throws CModelException {
		return (this.equals(other) && (this.getSourceManipulationInfo()
				.hasSameContentsAs(other.getSourceManipulationInfo())));
	}

	/*
	 * @see JavaElement#generateInfos
	 */
	protected void generateInfos(Object info, Map newElements,
			IProgressMonitor pm) throws CModelException {
		Openable openableParent = (Openable) getOpenableParent();
		if (openableParent == null) {
			return;
		}

		CElementInfo openableParentInfo = (CElementInfo) CModelManager
				.getDefault().getInfo(openableParent);
		if (openableParentInfo == null) {
			openableParent.generateInfos(openableParent.createElementInfo(),
					newElements, pm);
		}
		newElements.put(this, info);
	}

	public void setPos(int startPos, int length) {
		try {
			getSourceManipulationInfo().setPos(startPos, length);
		} catch (CModelException e) {
			//
		}
	}

	public void setIdPos(int startPos, int length) {
		try {
			getSourceManipulationInfo().setIdPos(startPos, length);
		} catch (CModelException e) {
			//
		}
	}

	public void setLines(int startLine, int endLine) {
		try {
			getSourceManipulationInfo().setLines(startLine, endLine);
		} catch (CModelException e) {
			//
		}
	}

}
