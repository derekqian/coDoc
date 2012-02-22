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
package edu.pdx.svl.coDoc.cdt.internal.core.parser;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.ICLogConstants;
import edu.pdx.svl.coDoc.cdt.core.parser.IParserLogService;
import edu.pdx.svl.coDoc.cdt.internal.core.model.Util;
import edu.pdx.svl.coDoc.cdt.internal.core.model.IDebugLogConstants.DebugLogConstant;

/**
 * @author jcamelon
 * 
 */
public class ParserLogService implements IParserLogService {

	final DebugLogConstant topic;

	/**
	 * @param constant
	 */
	public ParserLogService(DebugLogConstant constant) {
		topic = constant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.IParserLogService#traceLog(java.lang.String)
	 */
	public void traceLog(String message) {
		Util.debugLog(message, topic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.parser.IParserLogService#errorLog(java.lang.String)
	 */
	public void errorLog(String message) {
		Util.log(message, ICLogConstants.CDT);
	}

	public boolean isTracing() {
		if (CCorePlugin.getDefault() == null)
			return false;

		return (CCorePlugin.getDefault().isDebugging() && Util.isActive(topic));
	}
}
