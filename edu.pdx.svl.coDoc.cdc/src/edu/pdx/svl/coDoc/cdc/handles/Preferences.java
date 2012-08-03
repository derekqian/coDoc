/*******************************************************************************
 * Copyright (c) 2011 Boris von Loesch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Boris von Loesch - initial API and implementation
 ******************************************************************************/
package edu.pdx.svl.coDoc.cdc.handles;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;


/**
 * Triggers a forward search in all open pdf editors. Opens the first
 * editor for which the search was successful.
 * 
 * @author Boris von Loesch
 *
 */
public class Preferences extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CDCEditor.convertCDCFile();

		return null;
	}
}
