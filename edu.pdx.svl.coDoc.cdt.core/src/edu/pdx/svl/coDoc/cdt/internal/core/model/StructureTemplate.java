/*******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.model;

import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.IStructureTemplate;

public class StructureTemplate extends Structure implements IStructureTemplate {

	protected Template fTemplate;

	public StructureTemplate(ICElement parent, int kind, String name) {
		super(parent, kind, name);
		fTemplate = new Template(name);
	}

	/**
	 * Returns the parameterTypes.
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ITemplate#getParameters()
	 * @return String[]
	 */
	public String[] getTemplateParameterTypes() {
		return fTemplate.getTemplateParameterTypes();
	}

	/**
	 * Sets the fParameterTypes.
	 * 
	 * @param fParameterTypes
	 *            The fParameterTypes to set
	 */
	public void setTemplateParameterTypes(String[] templateParameterTypes) {
		fTemplate.setTemplateParameterTypes(templateParameterTypes);
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ITemplate#getNumberOfTemplateParameters()
	 */
	public int getNumberOfTemplateParameters() {
		return fTemplate.getNumberOfTemplateParameters();
	}

	/**
	 * @see edu.pdx.svl.coDoc.cdt.core.model.ITemplate#getTemplateSignature()
	 */
	public String getTemplateSignature() {
		return fTemplate.getTemplateSignature();
	}

}
