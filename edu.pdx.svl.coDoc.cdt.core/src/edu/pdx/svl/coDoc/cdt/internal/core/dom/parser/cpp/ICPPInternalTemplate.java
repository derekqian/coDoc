/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 * /
 *******************************************************************************/
/*
 * Created on Apr 29, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPSpecialization;

/**
 * @author aniefer
 * 
 */
public interface ICPPInternalTemplate extends ICPPInternalBinding {

	public void addSpecialization(IType[] arguments,
			ICPPSpecialization specialization);

	public IBinding instantiate(IType[] arguments);

	public ICPPSpecialization deferredInstance(IType[] arguments);

	public ICPPSpecialization getInstance(IType[] arguments);
}
