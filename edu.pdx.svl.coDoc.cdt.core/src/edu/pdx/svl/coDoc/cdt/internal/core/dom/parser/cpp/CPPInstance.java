/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
/*
 * Created on Mar 28, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateInstance;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ObjectMap;

/**
 * @author aniefer
 */
public abstract class CPPInstance extends CPPSpecialization implements
		ICPPTemplateInstance {
	private IType[] arguments;

	public CPPInstance(ICPPScope scope, IBinding orig, ObjectMap argMap,
			IType[] arguments) {
		super(orig, scope, argMap);
		this.arguments = arguments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPInstance#getTemplate()
	 */
	public ICPPTemplateDefinition getTemplateDefinition() {
		return (ICPPTemplateDefinition) getSpecializedBinding();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPInstance#getArgumentMap()
	 */
	public ObjectMap getArgumentMap() {
		return argumentMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPTemplateInstance#getArguments()
	 */
	public IType[] getArguments() {
		return arguments;
	}
}
