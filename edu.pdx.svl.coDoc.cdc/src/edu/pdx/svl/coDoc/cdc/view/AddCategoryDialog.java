package edu.pdx.svl.coDoc.cdc.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


//import org.eclipse.cdt.internal.ui.editor.Spec2ModelEditor;

import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.referencemodel.TextSelectionReference;
import edu.pdx.svl.coDoc.cdc.Global;


public class AddCategoryDialog extends Dialog {
	
	StyledText catStyledText;
	String categorypath;
	String catTxt = null;
	
	public AddCategoryDialog(String categorypath) {
		this(new Shell(), categorypath);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddCategoryDialog(Shell parentShell, String categorypath) {
		super(parentShell);
		this.categorypath = categorypath;
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
//		container.setLayout(new GridLayout(1, false));
		container.setLayout(null);
		
		Label lblCategory = new Label(container, SWT.NONE);
		lblCategory.setBounds(5, 25, 300, 20);
		lblCategory.setText("Parent Category: " + categorypath);
		
		Label lblCode = new Label(container, SWT.NONE);
		lblCode.setBounds(5, 55, 200, 20);
		lblCode.setText("New Category Name:");
						
		TextViewer commentTextViewer = new TextViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		catStyledText = commentTextViewer.getTextWidget();
//		commentStyledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		catStyledText.setBounds(5, 75, 584, 130);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		
		button.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent evt) {
				catTxt = catStyledText.getText();
			}
		});
		
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	public String getCatText() {
		return catTxt;
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 280);
	}

}
