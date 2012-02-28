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
 * Created on Nov 23, 2004
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IEnumeration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IEnumerator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ProblemBinding;
import org.eclipse.core.runtime.PlatformObject;

/**
 * @author aniefer
 */
public class CEnumerator extends PlatformObject implements IEnumerator {
	public static class CEnumeratorProblem extends ProblemBinding implements
			IEnumerator {
		public CEnumeratorProblem(IASTNode node, int id, char[] arg) {
			super(node, id, arg);
		}

		public IType getType() throws DOMException {
			throw new DOMException(this);
		}
	}

	private final IASTName enumeratorName;

	public CEnumerator(IASTEnumerator enumtor) {
		this.enumeratorName = enumtor.getName();
		enumeratorName.setBinding(this);
	}

	public IASTNode getPhysicalNode() {
		return enumeratorName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getName()
	 */
	public String getName() {
		return enumeratorName.toString();
	}

	public char[] getNameCharArray() {
		return enumeratorName.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getScope()
	 */
	public IScope getScope() {
		return CVisitor.getContainingScope(enumeratorName.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IEnumerator#getType()
	 */
	public IType getType() {
		IASTEnumerator etor = (IASTEnumerator) enumeratorName.getParent();
		IASTEnumerationSpecifier enumSpec = (IASTEnumerationSpecifier) etor
				.getParent();
		IEnumeration enumeration = (IEnumeration) enumSpec.getName()
				.resolveBinding();
		return enumeration;
	}

}
