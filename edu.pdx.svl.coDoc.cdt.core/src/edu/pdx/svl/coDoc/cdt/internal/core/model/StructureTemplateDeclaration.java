/*******************************************************************************
 * Copyright (c) 2005 QnX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Qnx Software Systems - initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.model;

import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.IStructureTemplateDeclaration;

public class StructureTemplateDeclaration extends StructureDeclaration
		implements IStructureTemplateDeclaration {

	Template fTemplate;

	public StructureTemplateDeclaration(ICElement parent, int kind, String name) {
		super(parent, name, kind);
		fTemplate = new Template(name);
	}

	public String[] getTemplateParameterTypes() {
		return fTemplate.getTemplateParameterTypes();
	}

	public void setTemplateParameterTypes(String[] templateParameterTypes) {
		fTemplate.setTemplateParameterTypes(templateParameterTypes);
	}

	public String getTemplateSignature() throws CModelException {
		return fTemplate.getTemplateSignature();
	}

	public int getNumberOfTemplateParameters() {
		return fTemplate.getNumberOfTemplateParameters();
	}

}
