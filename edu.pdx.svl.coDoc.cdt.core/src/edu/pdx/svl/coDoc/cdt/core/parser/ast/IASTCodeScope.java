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
package edu.pdx.svl.coDoc.cdt.core.parser.ast;

import edu.pdx.svl.coDoc.cdt.core.parser.ISourceElementCallbackDelegate;

/**
 * @author jcamelon
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IASTCodeScope extends IASTScope,
		ISourceElementCallbackDelegate {

	public IASTCodeScope getOwnerCodeScope();

	public IASTFunction getContainingFunction();

}
