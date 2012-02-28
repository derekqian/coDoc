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

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguousStatement;

public class CPPASTAmbiguousStatement extends CPPASTAmbiguity implements
		IASTAmbiguousStatement {

	private IASTStatement[] stmts = new IASTStatement[2];

	private int stmtsPos = -1;

	public void addStatement(IASTStatement s) {
		if (s != null) {
			stmtsPos++;
			stmts = (IASTStatement[]) ArrayUtil.append(IASTStatement.class,
					stmts, s);
		}
	}

	public IASTStatement[] getStatements() {
		stmts = (IASTStatement[]) ArrayUtil.removeNullsAfter(
				IASTStatement.class, stmts, stmtsPos);
		return stmts;
	}

	protected IASTNode[] getNodes() {
		return getStatements();
	}
}
