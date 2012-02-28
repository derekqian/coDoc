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

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.CASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.gnu.c.IGCCASTArrayRangeDesignator;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;

/**
 * @author jcamelon
 */
public class CASTArrayRangeDesignator extends CASTNode implements
		IGCCASTArrayRangeDesignator, IASTAmbiguityParent {

	private IASTExpression floor, ceiling;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.gcc.IGCCASTArrayRangeDesignator#getRangeFloor()
	 */
	public IASTExpression getRangeFloor() {
		return this.floor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.gcc.IGCCASTArrayRangeDesignator#setRangeFloor(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setRangeFloor(IASTExpression expression) {
		floor = expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.gcc.IGCCASTArrayRangeDesignator#getRangeCeiling()
	 */
	public IASTExpression getRangeCeiling() {
		return ceiling;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.c.gcc.IGCCASTArrayRangeDesignator#setRangeCeiling(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
	 */
	public void setRangeCeiling(IASTExpression expression) {
		ceiling = expression;
	}

	public boolean accept(ASTVisitor action) {
		if (action instanceof CASTVisitor
				&& ((CASTVisitor) action).shouldVisitDesignators) {
			switch (((CASTVisitor) action).visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
		if (floor != null)
			if (!floor.accept(action))
				return false;
		if (ceiling != null)
			if (!ceiling.accept(action))
				return false;
		return true;
	}

	public void replace(IASTNode child, IASTNode other) {
		if (child == floor) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			floor = (IASTExpression) other;
		}
		if (child == ceiling) {
			other.setPropertyInParent(child.getPropertyInParent());
			other.setParent(child.getParent());
			ceiling = (IASTExpression) other;
		}
	}

}
