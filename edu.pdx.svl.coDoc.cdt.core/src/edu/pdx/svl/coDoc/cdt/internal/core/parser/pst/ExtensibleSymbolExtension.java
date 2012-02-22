/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corp. - Rational Software - initial implementation
 *******************************************************************************/
/*
 * Created on Apr 21, 2004
 */
package edu.pdx.svl.coDoc.cdt.internal.core.parser.pst;

import java.util.Iterator;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTSymbol;

/**
 * @author aniefer
 */
public class ExtensibleSymbolExtension implements ISymbolASTExtension {
	protected final IExtensibleSymbol extensibleSymbol;

	protected final ASTNode primaryDeclaration;

	public ExtensibleSymbolExtension(IExtensibleSymbol symbol, IASTNode node) {
		primaryDeclaration = (ASTNode) node;
		extensibleSymbol = symbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbolASTExtension#getPrimaryDeclaration()
	 */
	public ASTNode getPrimaryDeclaration() {
		return primaryDeclaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbolASTExtension#getAllDefinitions()
	 */
	public Iterator getAllDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbolASTExtension#addDefinition(edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete.ASTSymbol)
	 */
	public void addDefinition(ASTSymbol definition) throws ExtensionException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.ISymbolOwner#getSymbol()
	 */
	public ISymbol getSymbol() {
		if (extensibleSymbol instanceof ISymbol)
			return (ISymbol) extensibleSymbol;
		return null;
	}

	public IExtensibleSymbol getExtensibleSymbol() {
		return extensibleSymbol;
	}
}
