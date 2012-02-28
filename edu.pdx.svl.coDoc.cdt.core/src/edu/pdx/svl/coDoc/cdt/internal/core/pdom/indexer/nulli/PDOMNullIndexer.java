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

package edu.pdx.svl.coDoc.cdt.internal.core.pdom.indexer.nulli;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOMIndexer;
import edu.pdx.svl.coDoc.cdt.core.model.ICElementDelta;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * @author Doug Schaefer
 * 
 * The Null Indexer which does nothing.
 */
public class PDOMNullIndexer implements IPDOMIndexer {

	public static final String ID = "edu.pdx.svl.coDoc.cdt.core.nullindexer";

	private ICProject project;

	public ICProject getProject() {
		return project;
	}

	public void setProject(ICProject project) {
		this.project = project;
	}

	public void handleDelta(ICElementDelta delta) {
	}

	private class Reindex extends Job {
		public Reindex() {
			super("Null Reindex"); //$NON-NLS-1$
			setSystem(true);
		}

		protected IStatus run(IProgressMonitor monitor) {
			try {
				PDOM pdom = (PDOM) CCorePlugin.getPDOMManager()
						.getPDOM(project);
				try {
					pdom.acquireWriteLock();
					pdom.clear();
					return Status.OK_STATUS;
				} catch (CoreException e) {
					return e.getStatus();
				} catch (InterruptedException e) {
					return Status.CANCEL_STATUS;
				} finally {
					pdom.releaseWriteLock();
				}
			} catch (CoreException e) {
				return e.getStatus();
			}
		}
	}

	public void reindex() throws CoreException {
		new Reindex().schedule();
	}

}
