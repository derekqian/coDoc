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

import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.IFunctionTemplate;

public class FunctionTemplate extends Function implements IFunctionTemplate {

	protected Template fTemplate;

	public FunctionTemplate(ICElement parent, String name) {
		super(parent, name, ICElement.C_TEMPLATE_FUNCTION);
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
	/*
	 * The signature in the outline view will be: The class X followed by its
	 * template parameters, then the scope resolution, then the function name,
	 * followed by its template parameters, folowed by its normal parameter
	 * list, then a colon then the function's return type.
	 */
	public String getTemplateSignature() throws CModelException {
		StringBuffer sig = new StringBuffer(fTemplate.getTemplateSignature());
		sig.append(this.getParameterClause());
		if (isConst()) {
			sig.append(" const"); //$NON-NLS-1$
		}
		if (isVolatile()) {
			sig.append(" volatile"); //$NON-NLS-1$
		}
		if ((this.getReturnType() != null)
				&& (this.getReturnType().length() > 0)) {
			sig.append(" : "); //$NON-NLS-1$
			sig.append(this.getReturnType());
		}
		return sig.toString();
	}

}
