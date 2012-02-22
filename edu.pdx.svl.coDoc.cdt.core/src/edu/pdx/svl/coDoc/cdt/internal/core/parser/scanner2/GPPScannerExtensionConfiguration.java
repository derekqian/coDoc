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
package edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2;

import edu.pdx.svl.coDoc.cdt.core.parser.GCCKeywords;
import edu.pdx.svl.coDoc.cdt.core.parser.IGCCToken;
import edu.pdx.svl.coDoc.cdt.core.parser.IToken;
import edu.pdx.svl.coDoc.cdt.core.parser.Keywords;
import edu.pdx.svl.coDoc.cdt.core.parser.util.CharArrayIntMap;

/**
 * @author jcamelon
 */
public class GPPScannerExtensionConfiguration extends
		GNUScannerExtensionConfiguration implements
		IScannerExtensionConfiguration {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.IScannerConfiguration#supportMinAndMaxOperators()
	 */
	public boolean supportMinAndMaxOperators() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.IScannerConfiguration#getAdditionalKeywords()
	 */
	public CharArrayIntMap getAdditionalKeywords() {
		CharArrayIntMap additionalCPPKeywords = new CharArrayIntMap(8, -1);
		additionalCPPKeywords.put(GCCKeywords.cp__ALIGNOF__,
				IGCCToken.t___alignof__);
		additionalCPPKeywords.put(GCCKeywords.cpTYPEOF, IGCCToken.t_typeof);
		additionalCPPKeywords.put(GCCKeywords.cp__ATTRIBUTE__,
				IGCCToken.t__attribute__);
		additionalCPPKeywords.put(Keywords.cRESTRICT, IToken.t_restrict);
		additionalCPPKeywords.put(Keywords.c_COMPLEX, IToken.t__Complex);
		additionalCPPKeywords.put(Keywords.c_IMAGINARY, IToken.t__Imaginary);
		return additionalCPPKeywords;
	}

}
