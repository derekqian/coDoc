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
package edu.pdx.svl.coDoc.cdt.internal.core.model;

import edu.pdx.svl.coDoc.cdt.core.model.ISourceRoot;
/** 
 * Info for ICProject.
 */

class CProjectInfo extends OpenableInfo {

	ISourceRoot[] sourceRoots;

	/**
	 */
	public CProjectInfo(CElement element) {
		super(element);
	}

	public void resetCaches() {
		sourceRoots = null;
	}
}
