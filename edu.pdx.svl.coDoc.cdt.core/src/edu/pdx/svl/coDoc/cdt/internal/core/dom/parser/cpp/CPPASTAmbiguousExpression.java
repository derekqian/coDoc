/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguousExpression;

public class CPPASTAmbiguousExpression extends CPPASTAmbiguity implements
		IASTAmbiguousExpression {

	private IASTExpression[] exp = new IASTExpression[2];

	private int expPos = -1;

	public void addExpression(IASTExpression e) {
		if (e != null) {
			expPos++;
			exp = (IASTExpression[]) ArrayUtil.append(IASTExpression.class,
					exp, e);
		}
	}

	public IASTExpression[] getExpressions() {
		exp = (IASTExpression[]) ArrayUtil.removeNullsAfter(
				IASTExpression.class, exp, expPos);
		return exp;
	}

	protected IASTNode[] getNodes() {
		return getExpressions();
	}

	public IType getExpressionType() {
		return CPPVisitor.getExpressionType(this);
	}

}
