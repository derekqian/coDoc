package edu.pdx.svl.coDoc.cdt.ui.action;

import edu.pdx.svl.coDoc.cdt.core.dom.IPDOMNode;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOMVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunction;
import org.eclipse.core.runtime.CoreException;

public class BBPDOMVisitor implements IPDOMVisitor {

	private int functions = 0;

	public String getOutput() {
		return "There are " + functions + " functions.";
	}

	public boolean visit(IPDOMNode node) throws CoreException {
		if (node instanceof IFunction) {
			functions++;
		}
		return true;
	}
}
