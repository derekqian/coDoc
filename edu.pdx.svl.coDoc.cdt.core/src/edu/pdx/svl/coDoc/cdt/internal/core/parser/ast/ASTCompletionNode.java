/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.parser.ast;

import java.util.Iterator;
import java.util.Set;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTScope;

/**
 * @author jcamelon
 */
public class ASTCompletionNode implements IASTCompletionNode {

	private final String prefix;

	private final IASTNode context;

	private final IASTScope scope;

	private final CompletionKind kind;

	private final Set keywordSet;

	private final String functionName;

	private final IASTExpression parameterListExpression;

	public ASTCompletionNode(CompletionKind kind, IASTScope scope,
			IASTNode context, String prefix, Set keywords, String functionName,
			IASTExpression expression) {
		this.kind = kind;
		this.context = context;
		this.scope = scope;
		this.prefix = prefix;
		this.keywordSet = keywords;
		this.functionName = functionName;
		this.parameterListExpression = expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getCompletionKind()
	 */
	public CompletionKind getCompletionKind() {
		return kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getCompletionScope()
	 */
	public IASTScope getCompletionScope() {
		return scope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getCompletionContext()
	 */
	public IASTNode getCompletionContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getCompletionPrefix()
	 */
	public String getCompletionPrefix() {
		return prefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getKeywords()
	 */
	public Iterator getKeywords() {
		if (keywordSet == null)
			return EmptyIterator.EMPTY_ITERATOR;
		return keywordSet.iterator();
	}

	public Set getKeywordSet() {
		return keywordSet;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getFunctionName()
	 */
	public String getFunctionName() {
		return functionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompletionNode#getFunctionParameters()
	 */
	public IASTExpression getFunctionParameters() {
		return parameterListExpression;
	}

}
