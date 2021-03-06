/*******************************************************************************
 * Copyright (c) 2000, 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.pdx.svl.coDoc.cdt.core.model.CModelException;
import edu.pdx.svl.coDoc.cdt.core.model.ICElement;
import edu.pdx.svl.coDoc.cdt.core.model.IField;
import edu.pdx.svl.coDoc.cdt.core.model.IMethodDeclaration;
import edu.pdx.svl.coDoc.cdt.core.model.IStructure;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTAccessVisibility;

public class Structure extends StructureDeclaration implements IStructure {

	Map superClassesNames = new TreeMap();

	public Structure(ICElement parent, int kind, String name) {
		super(parent, name, kind);
	}

	public IField[] getFields() throws CModelException {
		List fields = new ArrayList();
		fields.addAll(getChildrenOfType(ICElement.C_FIELD));
		return (IField[]) fields.toArray(new IField[fields.size()]);
	}

	public IField getField(String name) {
		try {
			IField[] fields = getFields();
			for (int i = 0; i < fields.length; i++) {
				IField field = fields[i];
				if (field.getElementName().equals(name)) {
					return field;
				}
			}
		} catch (CModelException e) {
		}
		return null;
	}

	public IMethodDeclaration[] getMethods() throws CModelException {
		List methods = new ArrayList();
		methods.addAll(getChildrenOfType(ICElement.C_METHOD_DECLARATION));
		methods.addAll(getChildrenOfType(ICElement.C_METHOD));
		return (IMethodDeclaration[]) methods
				.toArray(new IMethodDeclaration[methods.size()]);
	}

	public IMethodDeclaration getMethod(String name) {
		try {
			IMethodDeclaration[] methods = getMethods();
			for (int i = 0; i < methods.length; i++) {
				IMethodDeclaration method = methods[i];
				if (method.getElementName().equals(name)) {
					return method;
				}
			}
		} catch (CModelException e) {
		}
		return null;
	}

	public boolean isAbstract() throws CModelException {
		IMethodDeclaration[] methods = getMethods();
		for (int i = 0; i < methods.length; i++) {
			IMethodDeclaration method = methods[i];
			if (method.isPureVirtual())
				return true;
		}
		return false;
	}

	public String[] getSuperClassesNames() {
		return (String[]) superClassesNames.keySet().toArray(
				new String[superClassesNames.keySet().size()]);
	}

	public ASTAccessVisibility getSuperClassAccess(String name) {
		return (ASTAccessVisibility) superClassesNames.get(name);
	}

	public void addSuperClass(String name) {
		superClassesNames.put(name, ASTAccessVisibility.PUBLIC);
	}

	public void addSuperClass(String name, ASTAccessVisibility access) {
		superClassesNames.put(name, access);
	}

}
