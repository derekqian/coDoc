/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.IBuffer;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.ICModelStatusConstants;
import edu.pdx.svl.coDoc.cdt.core.model.IParent;
import edu.pdx.svl.coDoc.cdt.core.model.ISourceReference;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy;
import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import edu.pdx.svl.coDoc.cdt.core.model.IProblemRequestor;
import edu.pdx.svl.coDoc.cdt.internal.core.model.IBufferFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Implementation of a working copy translation unit. A working copy maintains
 * the timestamp of the resource it was created from.
 */
public class WorkingCopy extends TranslationUnit implements IWorkingCopy {

	/**
	 * If set, this is the factory that will be used to create the buffer.
	 */
	protected IBufferFactory bufferFactory;

	/**
	 * A counter of the number of time clients have asked for this working copy.
	 * It is set to 1, if the working copy is not managed. When destroyed, this
	 * counter is set to 0. Once destroyed, this working copy cannot be opened
	 * and non-handle info can not be accessed. This is never true if this
	 * translation unit is not a working copy.
	 */
	protected int useCount = 1;

	/**
	 * Creates a working copy of this element
	 */
	public WorkingCopy(ICElement parent, IFile file, String id,
			IBufferFactory bufferFactory) {
		super(parent, file, id);
		this.bufferFactory = bufferFactory == null ? getBufferManager()
				: bufferFactory;
	}

	public WorkingCopy(ICElement parent, IFile file, String id,
			IBufferFactory bufferFactory, IProblemRequestor requestor) {
		super(parent, file, id);
		this.bufferFactory = bufferFactory == null ? getBufferManager()
				: bufferFactory;
		problemRequestor = requestor;
	}

	public WorkingCopy(ICElement parent, IPath path, String id,
			IBufferFactory bufferFactory) {
		super(parent, path, id);
		this.bufferFactory = bufferFactory == null ? getBufferManager()
				: bufferFactory;
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy#commit(boolean,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void commit(boolean force, IProgressMonitor monitor)
			throws CModelException {
		ITranslationUnit original = this.getOriginalElement();
		if (original.exists()) {
			CommitWorkingCopyOperation op = new CommitWorkingCopyOperation(
					this, force);
			op.runOperation(monitor);
		} else {
			String contents = this.getSource();
			if (contents == null)
				return;
			try {
				IFile originalRes = (IFile) original.getResource();
				String encoding = null;
				try {
					encoding = originalRes.getCharset();
				} catch (CoreException ce) {
					// use no encoding
				}
				byte[] bytes = encoding == null ? contents.getBytes()
						: contents.getBytes(encoding);
				ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
				if (originalRes.exists()) {
					originalRes.setContents(stream, force ? IResource.FORCE
							| IResource.KEEP_HISTORY : IResource.KEEP_HISTORY,
							null);
				} else {
					originalRes.create(stream, force, monitor);
				}
			} catch (IOException e) {
				throw new CModelException(e,
						ICModelStatusConstants.IO_EXCEPTION);
			} catch (CoreException e) {
				throw new CModelException(e);
			}
		}
	}

	/**
	 * Returns a new element info for this element.
	 */
	protected CElementInfo createElementInfo() {
		return new WorkingCopyInfo(this);
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy#destroy()
	 */
	public void destroy() {
		if (--this.useCount > 0) {
			return;
		}
		try {
			DestroyWorkingCopyOperation op = new DestroyWorkingCopyOperation(
					this);
			op.runOperation(null);
		} catch (CModelException e) {
			// do nothing
		}
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ICElement#exists()
	 */
	public boolean exists() {
		// working copy always exists in the model until it is detroyed
		return this.useCount != 0;
	}

	/**
	 * Answers custom buffer factory
	 */
	public IBufferFactory getBufferFactory() {

		return this.bufferFactory;
	}

	/**
	 * Working copies must be identical to be equal.
	 * 
	 * @see Object#equals
	 */
	public boolean equals(Object o) {
		// working copy always exists in the model until it is detroyed
		return this.useCount != 0;
	}

	/**
	 * Returns the original element the specified working copy element was
	 * created from, or <code>null</code> if this is not a working copy
	 * element.
	 * 
	 * @param workingCopyElement
	 *            the specified working copy element
	 * @return the original element the specified working copy element was
	 *         created from, or <code>null</code> if this is not a working
	 *         copy element
	 */
	public ICElement getOriginal(ICElement workingCopyElement) {
		// It has to come from the same workingCopy, meaning ours.
		if (workingCopyElement instanceof ISourceReference) {
			ITranslationUnit wunit = ((ISourceReference) workingCopyElement)
					.getTranslationUnit();
			if (!wunit.equals(this)) {
				return null;
			}
		} else {
			return null;
		}
		ITranslationUnit tu = getOriginalElement();
		if (tu == null) {
			return null; // oops !!
		}

		// look for it.
		ICElement element = workingCopyElement;
		ArrayList children = new ArrayList();
		while (element != null && element.getElementType() != ICElement.C_UNIT) {
			children.add(element);
			element = element.getParent();
		}
		ICElement current = tu;
		for (int i = children.size() - 1; i >= 0; i--) {
			ICElement child = (ICElement) children.get(i);
			if (current instanceof IParent) {
				try {
					ICElement[] celems = ((IParent) current).getChildren();
					current = null;
					for (int j = 0; j < celems.length; ++j) {
						if (celems[j].getElementName().equals(
								child.getElementName())
								&& celems[j].getElementType() == child
										.getElementType()) {
							current = celems[j];
							break;
						}
					}
				} catch (CModelException e) {
					current = null;
				}
			} else {
				current = null;
			}
		}
		return current;
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy#getOriginalElement()
	 */
	public ITranslationUnit getOriginalElement() {
		String id = CoreModel.getRegistedContentTypeId(getCProject()
				.getProject(), getElementName());
		return new TranslationUnit(getParent(), getFile(), id);
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit#getSharedWorkingCopy(IProgressMonitor,
	 *      IBufferFactory)
	 */
	public IWorkingCopy getSharedWorkingCopy(IProgressMonitor monitor,
			IBufferFactory factory) throws CModelException {
		return this;
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit#getWorkingCopy()
	 */
	public IWorkingCopy getWorkingCopy() {
		return this;
	}

	/**
	 * @see IWorkingCopy
	 */
	public IWorkingCopy getWorkingCopy(IProgressMonitor monitor,
			IBufferFactory factory) {
		return this;
	}

	/**
	 * @see IWorkingCopy
	 */
	public boolean isBasedOn(IResource resource) {
		if (resource.getType() != IResource.FILE) {
			return false;
		}
		if (this.useCount == 0) {
			return false;
		}
		try {
			// if resource got deleted, then #getModificationStamp() will answer
			// IResource.NULL_STAMP, which is always different from the cached
			// timestamp
			return ((TranslationUnitInfo) getElementInfo()).fTimestamp == ((IFile) resource)
					.getModificationStamp();
		} catch (CModelException e) {
			return false;
		}
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit#isWorkingCopy()
	 */
	public boolean isWorkingCopy() {
		return true;
	}

	/**
	 * @see ICFile
	 * @see IWorkingCopy
	 * 
	 * @exception CModelException
	 *                attempting to open a read only element for something other
	 *                than navigation or if this is a working copy being opened
	 *                after it has been destroyed.
	 */
	public void open(IProgressMonitor monitor) throws CModelException {
		if (this.useCount == 0) { // was destroyed
			throw newNotPresentException();
		}
		super.open(monitor);
		// if (monitor != null && monitor.isCanceled()) return;
		// if (this.problemRequestor != null &&
		// this.problemRequestor.isActive()){
		// this.problemRequestor.beginReporting();
		// TranslationUnitProblemFinder.process(this, this.problemRequestor,
		// monitor);
		// this.problemRequestor.endReporting();
		// }
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.model.CFile#openBuffer(IProgressMonitor)
	 */
	protected IBuffer openBuffer(IProgressMonitor pm) throws CModelException {

		// create buffer - working copies may use custom buffer factory
		System.out.println("Opening the buffer!");
		IBuffer buffer = getBufferFactory().createBuffer(this);
		if (buffer == null) {
			return null;
		}

		// set the buffer source if needed
		if (buffer.getCharacters() == null) {
			ITranslationUnit original = this.getOriginalElement();
			IBuffer originalBuffer = null;
			originalBuffer = original.getBuffer();
			if (originalBuffer != null) {
				char[] originalContents = originalBuffer.getCharacters();
				if (originalContents != null) {
					buffer.setContents((char[]) originalContents.clone());
				}
			} else {
				// initialize buffer
				buffer.setContents(new char[0]);
			}
		}

		// add buffer to buffer cache
		this.getBufferManager().addBuffer(buffer);

		// listen to buffer changes
		buffer.addBufferChangedListener(this);

		return buffer;
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy#reconcile()
	 */
	public IMarker[] reconcile() throws CModelException {
		reconcile(false, null);
		return null;
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy#reconcile(boolean,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void reconcile(boolean forceProblemDetection,
			IProgressMonitor monitor) throws CModelException {
		ReconcileWorkingCopyOperation op = new ReconcileWorkingCopyOperation(
				this, forceProblemDetection);
		op.runOperation(monitor);
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy#restore()
	 */
	public void restore() throws CModelException {
		System.out.println("Restoring!");
		if (this.useCount == 0)
			throw newNotPresentException(); // was destroyed

		TranslationUnit original = (TranslationUnit) getOriginalElement();
		IBuffer buffer = this.getBuffer();
		if (buffer == null)
			return;
		buffer.setContents(original.getContents());
		updateTimeStamp(original);
		makeConsistent(null);
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ICFile#save(IProgressMonitor, boolean)
	 */
	public void save(IProgressMonitor pm, boolean force) throws CModelException {
		// computes fine-grain deltas in case the working copy is being
		// reconciled already (if not it would miss one iteration of deltas).
		this.reconcile();
	}

	/**
	 * @param original @
	 */
	protected void updateTimeStamp(TranslationUnit original)
			throws CModelException {
		long timeStamp = ((IFile) original.getResource())
				.getModificationStamp();
		((TranslationUnitInfo) getElementInfo()).fTimestamp = timeStamp;
	}

}
