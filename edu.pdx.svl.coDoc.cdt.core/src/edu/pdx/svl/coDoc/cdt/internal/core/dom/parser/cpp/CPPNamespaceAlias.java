/*******************************************************************************
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
 * Created on Mar 15, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPBlockScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPDelegate;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespace;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceAlias;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceScope;
import org.eclipse.core.runtime.PlatformObject;

/**
 * @author aniefer
 */
public class CPPNamespaceAlias extends PlatformObject implements
		ICPPNamespaceAlias, ICPPInternalBinding {

	private ICPPNamespace namespace;

	private IASTName alias;

	/**
	 * 
	 */
	public CPPNamespaceAlias(IASTName aliasName, ICPPNamespace namespace) {
		super();
		this.namespace = namespace;
		this.alias = aliasName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespace#getNamespaceScope()
	 */
	public ICPPNamespaceScope getNamespaceScope() throws DOMException {
		return namespace.getNamespaceScope();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPDelegate#getDelegateType()
	 */
	public int getDelegateType() {
		return ICPPDelegate.NAMESPACE_ALIAS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPDelegate#getBinding()
	 */
	public IBinding getBinding() {
		return namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getName()
	 */
	public String getName() {
		return alias.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getNameCharArray()
	 */
	public char[] getNameCharArray() {
		return alias.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getFullyQualifiedName()
	 */
	public String[] getQualifiedName() {
		return CPPVisitor.getQualifiedName(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getFullyQualifiedNameCharArray()
	 */
	public char[][] getQualifiedNameCharArray() {
		return CPPVisitor.getQualifiedNameCharArray(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getScope()
	 */
	public IScope getScope() {
		return CPPVisitor.getContainingScope(alias);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPBinding#isGloballyQualified()
	 */
	public boolean isGloballyQualified() throws DOMException {
		IScope scope = getScope();
		while (scope != null) {
			if (scope instanceof ICPPBlockScope)
				return false;
			scope = scope.getParent();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#getDeclarations()
	 */
	public IASTNode[] getDeclarations() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#getDefinition()
	 */
	public IASTNode getDefinition() {
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#createDelegate(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
	 */
	public ICPPDelegate createDelegate(IASTName name) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#addDefinition(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode)
	 */
	public void addDefinition(IASTNode node) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.ICPPInternalBinding#addDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode)
	 */
	public void addDeclaration(IASTNode node) {
	}

	public void removeDeclaration(IASTNode node) {

	}

	public IBinding[] getMemberBindings() throws DOMException {
		return namespace.getMemberBindings();
	}
}
