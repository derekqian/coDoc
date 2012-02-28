/*************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on June 14, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPConstructor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ObjectMap;

/**
 * @author aniefer
 * 
 */
public class CPPConstructorTemplateSpecialization extends
		CPPMethodTemplateSpecialization implements ICPPConstructor {

	/**
	 * @param specialized
	 * @param scope
	 * @param argumentMap
	 */
	public CPPConstructorTemplateSpecialization(IBinding specialized,
			ICPPScope scope, ObjectMap argumentMap) {
		super(specialized, scope, argumentMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPConstructor#isExplicit()
	 */
	public boolean isExplicit() throws DOMException {
		return ((ICPPConstructor) getSpecializedBinding()).isExplicit();
	}

}
