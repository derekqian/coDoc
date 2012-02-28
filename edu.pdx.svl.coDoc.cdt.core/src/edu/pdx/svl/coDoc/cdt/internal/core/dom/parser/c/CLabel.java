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
 * Created on Nov 17, 2004
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTLabelStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ILabel;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ProblemBinding;
import org.eclipse.core.runtime.PlatformObject;

/**
 * @author aniefer
 */
public class CLabel extends PlatformObject implements ILabel {

	public static class CLabelProblem extends ProblemBinding implements ILabel {
		public CLabelProblem(IASTNode node, int id, char[] arg) {
			super(node, id, arg);
		}

		public IASTLabelStatement getLabelStatement() throws DOMException {
			throw new DOMException(this);
		}
	}

	private final IASTName labelStatement;

	public CLabel(IASTName statement) {
		labelStatement = statement;
		statement.setBinding(this);
	}

	public IASTNode getPhysicalNode() {
		return labelStatement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.ILabel#getLabelStatement()
	 */
	public IASTLabelStatement getLabelStatement() {
		return (IASTLabelStatement) labelStatement.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getName()
	 */
	public String getName() {
		return labelStatement.toString();
	}

	public char[] getNameCharArray() {
		return labelStatement.toCharArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding#getScope()
	 */
	public IScope getScope() {
		return CVisitor.getContainingScope(labelStatement.getParent());
	}

}
