/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Sep 2, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast.complete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCodeScope;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.pst.IContainerSymbol;

/**
 * @author jcamelon
 * 
 */
public class ASTCodeScope extends ASTScope implements IASTCodeScope {

	private List declarations = null;

	private final IASTCodeScope ownerCodeScope;

	/**
	 * @param newScope
	 */
	public ASTCodeScope(IContainerSymbol newScope) {
		super(newScope);
		ownerCodeScope = (newScope.getContainingSymbol().getASTExtension()
				.getPrimaryDeclaration() instanceof IASTCodeScope) ? (IASTCodeScope) newScope
				.getContainingSymbol().getASTExtension()
				.getPrimaryDeclaration()
				: null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#acceptElement(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void acceptElement(ISourceElementRequestor requestor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#enterScope(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void enterScope(ISourceElementRequestor requestor) {
		try {
			requestor.enterCodeBlock(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate#exitScope(edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementRequestor)
	 */
	public void exitScope(ISourceElementRequestor requestor) {
		try {
			requestor.exitCodeBlock(this);
		} catch (Exception e) {
			/* do nothing */
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCodeScope#getOwnerCodeScope()
	 */
	public IASTCodeScope getOwnerCodeScope() {
		return ownerCodeScope;
	}

	public Iterator getDeclarations() {
		if (declarations != null)
			return declarations.iterator();
		return super.getDeclarations();
	}

	public void addDeclaration(IASTDeclaration declaration) {
		declarations.add(declaration);
	}

	public void initDeclarations() {
		declarations = new ArrayList(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCodeScope#getContainingFunction()
	 */
	public IASTFunction getContainingFunction() {
		IASTCodeScope i = getOwnerCodeScope();
		while ((i != null) && !(i instanceof IASTFunction))
			i = i.getOwnerCodeScope();
		return (IASTFunction) i;
	}

}
