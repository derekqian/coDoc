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
 * Created on Nov 29, 2004
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;

/**
 * @author aniefer
 */
public class CPPNamespaceScope extends CPPScope implements ICPPNamespaceScope {
	IASTNode[] usings = null;

	public CPPNamespaceScope(IASTNode physicalNode) {
		super(physicalNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceScope#getUsingDirectives()
	 */
	public IASTNode[] getUsingDirectives() {
		return (IASTNode[]) ArrayUtil.trim(IASTNode.class, usings, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespaceScope#addUsingDirective(edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTUsingDirective)
	 */
	public void addUsingDirective(IASTNode directive) {
		usings = (IASTNode[]) ArrayUtil.append(IASTNode.class, usings,
				directive);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope#getScopeName()
	 */
	public IASTName getScopeName() {
		IASTNode node = getPhysicalNode();
		if (node instanceof ICPPASTNamespaceDefinition) {
			return ((ICPPASTNamespaceDefinition) node).getName();
		}
		return null;
	}
}
