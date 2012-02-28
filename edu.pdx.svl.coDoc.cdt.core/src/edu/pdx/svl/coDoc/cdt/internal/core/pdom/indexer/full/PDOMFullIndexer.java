/*******************************************************************************
 * Copyright (c) 2006 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.pdom.indexer.full;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOMIndexer;
import edu.pdx.svl.coDoc.cdt.core.model.ICElementDelta;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import org.eclipse.core.runtime.CoreException;

/**
 * The Full indexer does full parsing in order to gather index information. It
 * has good accuracy but is relatively slow.
 * 
 * @author Doug Schaefer
 * 
 */
public class PDOMFullIndexer implements IPDOMIndexer {

	private ICProject project;

	public ICProject getProject() {
		return project;
	}

	public void setProject(ICProject project) {
		this.project = project;
	}

	public void handleDelta(ICElementDelta delta) throws CoreException {
		CCorePlugin.getPDOMManager().enqueue(
				new PDOMFullHandleDelta(this, delta));
	}

	public void reindex() throws CoreException {
		CCorePlugin.getPDOMManager().enqueue(new PDOMFullReindex(this));
	}

}
