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
import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.ICElementVisitor;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * @author Doug Schaefer
 * 
 */
public class PDOMFullReindex extends PDOMFullIndexerJob {

	public PDOMFullReindex(PDOMFullIndexer indexer) throws CoreException {
		super(indexer);
	}

	public void run(final IProgressMonitor monitor) {
		try {

			long start = System.currentTimeMillis();

			// First clear out the PDOM
			pdom.clear();

			// First index all the source files (i.e. not headers)
			IProject project = indexer.getProject().getProject();
			IResource[] resources = project.members();
			for (int i=0; i<resources.length; i++) {
				if ((resources[i] instanceof IFile) && (CoreModel.isTranslationUnit((IFile)resources[i]))) {
					try {
						TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault()
						.getCoreModel().create((IFile)resources[i]);
						addTU(tu);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
//			indexer.getProject().accept(new ICElementVisitor() {
//				public boolean visit(ICElement element) throws CoreException {
//					if (monitor.isCanceled())
//						throw new CoreException(Status.CANCEL_STATUS);
//					switch (element.getElementType()) {
//					case ICElement.C_UNIT:
//						ITranslationUnit tu = (ITranslationUnit) element;
//						if (tu.isSourceUnit()) {
//							try {
//								addTU(tu);
//							} catch (Throwable e) {
//								CCorePlugin.log(e);
//								if (++errorCount > MAX_ERRORS)
//									throw new CoreException(
//											Status.CANCEL_STATUS);
//							}
//						}
//						return false;
//					case ICElement.C_CCONTAINER:
//					case ICElement.C_PROJECT:
//						return true;
//					}
//					return false;
//				}
//			});

			// Now add in the header files but only if they aren't already
			// indexed
			indexer.getProject().accept(new ICElementVisitor() {
				public boolean visit(ICElement element) throws CoreException {
					if (monitor.isCanceled())
						throw new CoreException(Status.CANCEL_STATUS);
					switch (element.getElementType()) {
					case ICElement.C_UNIT:
						ITranslationUnit tu = (ITranslationUnit) element;
						if (tu.isHeaderUnit()) {
							IFile rfile = (IFile) tu.getUnderlyingResource();
							String filename = rfile.getLocation().toOSString();
							if (pdom.getFile(filename) == null) {
								try {
									addTU(tu);
								} catch (InterruptedException e) {
									CCorePlugin.log(e);
									if (++errorCount > MAX_ERRORS)
										throw new CoreException(
												Status.CANCEL_STATUS);
								}
							}
						}
						return false;
					case ICElement.C_CCONTAINER:
					case ICElement.C_PROJECT:
						return true;
					}
					return false;
				}
			});

			String showTimings = Platform.getDebugOption(CCorePlugin.PLUGIN_ID
					+ "/debug/pdomtimings"); //$NON-NLS-1$
			if (showTimings != null && showTimings.equalsIgnoreCase("true")) //$NON-NLS-1$
				System.out
						.println("PDOM Full Reindex Time: " + (System.currentTimeMillis() - start) //$NON-NLS-1$
								+ " " + indexer.getProject().getElementName()); //$NON-NLS-1$
		} catch (CoreException e) {
			if (e.getStatus() != Status.CANCEL_STATUS)
				CCorePlugin.log(e);
		}
	}

}
