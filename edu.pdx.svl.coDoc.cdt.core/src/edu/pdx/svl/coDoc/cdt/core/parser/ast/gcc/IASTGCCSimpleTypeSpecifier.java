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

package edu.pdx.svl.coDoc.cdt.core.parser.ast.gcc;

import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTSimpleTypeSpecifier;

/**
 * @author jcamelon
 * 
 */
public interface IASTGCCSimpleTypeSpecifier extends IASTSimpleTypeSpecifier {

	public static class Type extends IASTSimpleTypeSpecifier.Type {
		public static final Type TYPEOF = new Type(LAST_TYPE + 1);

		/**
		 * @param enumValue
		 */
		protected Type(int enumValue) {
			super(enumValue);
		}

	}

	public static final String TYPEOF_EXRESSION = "TYPEOF EXPRESSION"; //$NON-NLS-1$

	public IASTExpression getTypeOfExpression();
}
