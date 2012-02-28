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
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import java.util.Arrays;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IProblemBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.CASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPVisitor;

public abstract class CASTAmbiguity extends CASTNode {

	protected static class CASTNameCollector extends CASTVisitor {
		private IASTName[] names = new IASTName[2];

		private int namesPos = -1;

		{
			shouldVisitNames = true;
		}

		public int visit(IASTName name) {
			if (name != null) {
				namesPos++;
				names = (IASTName[]) ArrayUtil.append(IASTName.class, names,
						name);
			}
			return PROCESS_CONTINUE;
		}

		public IASTName[] getNames() {
			names = (IASTName[]) ArrayUtil.removeNullsAfter(IASTName.class,
					names, namesPos);
			return names;
		}
	}

	protected abstract IASTNode[] getNodes();

	public boolean accept(ASTVisitor visitor) {
		IASTNode[] nodez = getNodes();
		int[] issues = new int[nodez.length];
		Arrays.fill(issues, 0);
		for (int i = 0; i < nodez.length; ++i) {
			IASTNode s = nodez[i];
			s.accept(visitor);
			CASTNameCollector resolver = new CASTNameCollector();
			s.accept(resolver);
			IASTName[] names = resolver.getNames();
			for (int j = 0; j < names.length; ++j) {
				try {
					IBinding b = names[j].resolveBinding();
					if (b == null || b instanceof IProblemBinding)
						++issues[i];
					IScope scope = CPPVisitor.getContainingScope(names[j]);
					if (scope != null) {
						try {
							scope.flushCache();
						} catch (DOMException e) {
						}
					}
				} catch (Throwable t) {
					++issues[i];
				}
			}
		}
		int bestIndex = 0;
		int bestValue = issues[0];
		for (int i = 1; i < issues.length; ++i) {
			if (issues[i] < bestValue) {
				bestIndex = i;
				bestValue = issues[i];
			}
		}

		IASTAmbiguityParent owner = (IASTAmbiguityParent) getParent();
		owner.replace(this, nodez[bestIndex]);
		return true;
	}

}
