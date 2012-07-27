package edu.pdx.svl.coDoc.cdc.handles;

import org.eclipse.cdt.core.dom.IPDOMNode;
import org.eclipse.cdt.core.dom.IPDOMVisitor;
import org.eclipse.cdt.core.dom.ast.IFunction;
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

	@Override
	public void leave(IPDOMNode node) throws CoreException {
		// TODO Auto-generated method stub
		
	}
}
