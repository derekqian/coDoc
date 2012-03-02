/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser;

import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.ILocationResolver;

/**
 * @author jcamelon
 */
public interface IRequiresLocationInformation {

	/**
	 * @param locationResolver
	 */
	void setLocationResolver(ILocationResolver resolver);

}