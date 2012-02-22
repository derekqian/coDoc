/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.ui;

import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import edu.pdx.svl.coDoc.cdt.core.model.IBuffer;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.core.model.WorkingCopy;
import org.eclipse.core.runtime.CoreException;

public class CFileElementWorkingCopy extends WorkingCopy {

	ITranslationUnit unit;

	/**
	 * Creates a working copy of this element
	 */
	public CFileElementWorkingCopy(ITranslationUnit unit) throws CoreException {
		super(unit.getParent(), unit.getPath(), unit.getContentTypeId(), null);
		this.unit = unit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.model.IOpenable#getBuffer()
	 */
	public IBuffer getBuffer() throws CModelException {
		return unit.getBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.model.IWorkingCopy#getOriginalElement()
	 */
	public ITranslationUnit getOriginalElement() {
		return unit;
	}

}
