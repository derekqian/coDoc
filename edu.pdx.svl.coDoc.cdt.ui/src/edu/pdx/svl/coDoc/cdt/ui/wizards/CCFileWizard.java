package edu.pdx.svl.coDoc.cdt.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * This class implements the interface required by the desktop for creating C
 * Files.
 * 
 * @author mscarpino
 */
public class CCFileWizard extends Wizard implements INewWizard {
	private IStructuredSelection selection;

	private IWorkbench workbench;

	private CCFilePage mainPage;

	/**
	 * (non-Javadoc) Method declared on Wizard.
	 */
	public void addPages() {
		mainPage = new CCFilePage(workbench, selection);
		addPage(mainPage);
	}

	/**
	 * (non-Javadoc) Method declared on IWorkbenchWizard
	 */
	public void init(IWorkbench workbench1, IStructuredSelection selection1) {
		this.workbench = workbench1;
		this.selection = selection1;
		setWindowTitle("C++ File Creation Wizard");
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(
				NewCProjectWizard.class, "sheet.gif"));
	}

	/**
	 * (non-Javadoc) Performs the finish() method of the <code>CCFilePage</code>
	 */
	public boolean performFinish() {
		return mainPage.finish();
	}
}
