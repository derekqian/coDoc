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

/**
 * A C Folder Resource.
 */
public interface ICContainer extends ICElement, IParent, IOpenable {

	/**
	 * Returns all of the translation units in this ccontainer.
	 * 
	 * @exception CModelException
	 *                if this element does not exist or if an exception occurs
	 *                while accessing its corresponding resource.
	 * @return all of the translation units in this ccontainer
	 */
	ITranslationUnit[] getTranslationUnits() throws CModelException;;

	/**
	 * Returns the tranlation unit with the specified name in this container
	 * (for example, <code>"foobar.c"</code>). The name has to be a valid
	 * translation unit name. This is a handle-only operation. The celement may
	 * or may not exist.
	 * 
	 * @param name
	 *            the given name
	 * @return the translation unit with the specified name in this container
	 */
	ITranslationUnit getTranslationUnit(String name);

	/**
	 * Return all the child containers of this container.
	 * 
	 * @return
	 * @throws CModelException @
	 */
	ICContainer[] getCContainers() throws CModelException;

	/**
	 * Returns the container with the given name. An empty string indicates the
	 * default package. This is a handle-only operation. The celement may or may
	 * not exist.
	 * 
	 * @param name
	 *            the given container
	 * @return the container with the given name
	 */
	ICContainer getCContainer(String name);

}
