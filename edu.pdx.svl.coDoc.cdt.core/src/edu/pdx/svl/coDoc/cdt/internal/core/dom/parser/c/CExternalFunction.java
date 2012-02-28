/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * Created on Jan 26, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunction;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunctionType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICExternalBinding;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPFunctionType;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPSemantics;

/**
 * @author aniefer
 */
public class CExternalFunction extends CFunction implements IFunction,
		ICExternalBinding {
	private IASTName name = null;

	private IASTTranslationUnit tu = null;

	public CExternalFunction(IASTTranslationUnit tu, IASTName name) {
		super(null);
		this.name = name;
		this.tu = tu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunction#getType()
	 */
	public IFunctionType getType() {
		IFunctionType t = super.getType();
		if (t == null) {
			type = new CPPFunctionType(CPPSemantics.VOID_TYPE,
					IType.EMPTY_TYPE_ARRAY);
		}
		return type;
	}

	protected IASTTranslationUnit getTranslationUnit() {
		return tu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getName()
	 */
	public String getName() {
		return name.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		return name.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getScope()
	 */
	public IScope getScope() {
		return tu.getScope();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunction#isExtern()
	 */
	public boolean isExtern() {
		return true;
	}
}
