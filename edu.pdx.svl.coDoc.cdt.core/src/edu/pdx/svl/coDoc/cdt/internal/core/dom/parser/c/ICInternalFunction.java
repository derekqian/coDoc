/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * Created on Jun 6, 2005
 */
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDeclarator;

/**
 * @author aniefer
 * 
 */
public interface ICInternalFunction extends ICInternalBinding {
	public void setFullyResolved(boolean resolved);

	public void addDeclarator(IASTFunctionDeclarator fnDeclarator);
}
