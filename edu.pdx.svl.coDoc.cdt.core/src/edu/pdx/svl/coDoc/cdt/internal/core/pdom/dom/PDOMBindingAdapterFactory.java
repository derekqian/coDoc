/*******************************************************************************
 * Copyright (c) 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.pdom.dom;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ICProject;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * @author Doug Schaefer This factory adapts IBinding object to PDOMBinding
 *         object
 */
public class PDOMBindingAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof PDOMBinding)
			return adaptableObject;

		try {
			IBinding binding = (IBinding) adaptableObject;
			ICProject[] projects = CoreModel.getDefault().getCModel()
					.getCProjects();
			for (int i = 0; i < projects.length; ++i) {
				IPDOM ipdom = CCorePlugin.getPDOMManager().getPDOM(projects[i]);
				if (ipdom == null || !(ipdom instanceof PDOM))
					continue;
				PDOM pdom = (PDOM) ipdom;

				for (PDOMLinkage linkage = pdom.getFirstLinkage(); linkage != null; linkage = linkage
						.getNextLinkage()) {
					PDOMBinding pdomBinding = linkage.adaptBinding(binding);
					if (binding != null)
						return pdomBinding;
				}
			}
			return null;
		} catch (DOMException e) {
			CCorePlugin.log(e);
			return null;
		} catch (CoreException e) {
			CCorePlugin.log(e);
			return null;
		}
	}

	private static Class[] adapterList = { PDOMBinding.class };

	public Class[] getAdapterList() {
		return adapterList;
	}

}
