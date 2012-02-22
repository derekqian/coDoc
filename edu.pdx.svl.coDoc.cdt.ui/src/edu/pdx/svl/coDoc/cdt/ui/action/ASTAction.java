package edu.pdx.svl.coDoc.cdt.ui.action;

import java.util.Iterator;
import java.util.Map;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.ASTNotImplementedException;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTCompilationUnit;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTFunction;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTLinkageSpecification;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNamespaceDefinition;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTTemplateDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTUsingDeclaration;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTVariable;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;
import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CDocumentProvider;
import edu.pdx.svl.coDoc.cdt.internal.ui.editor.CEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class ASTAction implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow window;

	public ASTAction() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		if (window.getActivePage().getActiveEditor() instanceof CEditor) {
			String output = "";
			try {
				IASTCompilationUnit unit = CCorePlugin.getCompilationUnit();
				Iterator iter = unit.getDeclarations();
				while (iter.hasNext()) {
					IASTDeclaration decl = (IASTDeclaration) iter.next();

					if (decl instanceof IASTFunction)
						output += "Function declaration: "
								+ ((IASTFunction) decl).getName() + "\n";

					else if (decl instanceof IASTLinkageSpecification)
						output += "Linkage declaration: "
								+ ((IASTLinkageSpecification) decl)
										.getLinkageString() + "\n";

					else if (decl instanceof IASTNamespaceDefinition)
						output += "Namespace definition: "
								+ ((IASTNamespaceDefinition) decl).getName()
								+ "\n";

					else if (decl instanceof IASTUsingDeclaration)
						output += "Using declaration: "
								+ ((IASTUsingDeclaration) decl).getName()
								+ "\n";

					else if (decl instanceof IASTVariable)
						output += "Variable declaration: "
								+ ((IASTVariable) decl).getName() + "\n";
				}
			} catch (ASTNotImplementedException e) {
				e.printStackTrace();
			}
			MessageDialog mb = new MessageDialog(window.getShell(),
					"AST Analysis Result", null, output,
					MessageDialog.INFORMATION, new String[] { "OK" }, 0);
			mb.open();
		}
	}

	public void dispose() {
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
