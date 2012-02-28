/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation 
 *******************************************************************************/

package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTCompoundStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTLabelStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ILabel;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.CASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICFunctionScope;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;

/**
 * Created on Nov 8, 2004
 * 
 * @author aniefer
 */
public class CFunctionScope extends CScope implements ICFunctionScope {
	public CFunctionScope(IASTFunctionDefinition function) {
		super(function);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICFunctionScope#getBinding(char[])
	 */
	public IBinding getBinding(char[] name) {
		return super.getBinding(NAMESPACE_TYPE_OTHER, name);
	}

	public IScope getBodyScope() {
		IASTNode node = getPhysicalNode();
		IASTStatement statement = ((IASTFunctionDefinition) node).getBody();
		if (statement instanceof IASTCompoundStatement) {
			return ((IASTCompoundStatement) statement).getScope();
		}
		return null;
	}

	public ILabel[] getLabels() {
		FindLabelsAction action = new FindLabelsAction();

		getPhysicalNode().accept(action);

		ILabel[] result = null;
		if (action.labels != null) {
			for (int i = 0; i < action.labels.length
					&& action.labels[i] != null; i++) {
				IASTLabelStatement labelStatement = action.labels[i];
				IBinding binding = labelStatement.getName().resolveBinding();
				if (binding != null)
					result = (ILabel[]) ArrayUtil.append(ILabel.class, result,
							binding);
			}
		}
		return (ILabel[]) ArrayUtil.trim(ILabel.class, result);
	}

	static private class FindLabelsAction extends CASTVisitor {
		public IASTLabelStatement[] labels = null;

		public FindLabelsAction() {
			shouldVisitStatements = true;
		}

		public int visit(IASTStatement statement) {
			if (statement instanceof IASTLabelStatement) {
				labels = (IASTLabelStatement[]) ArrayUtil.append(
						IASTLabelStatement.class, labels, statement);
			}
			return PROCESS_CONTINUE;
		}
	}
}
