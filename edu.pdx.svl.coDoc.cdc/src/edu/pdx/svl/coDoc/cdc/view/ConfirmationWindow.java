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


public class ConfirmationWindow extends Dialog {
	
	StyledText commentStyledText;
	
	public ConfirmationWindow() {
		this(new Shell());
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfirmationWindow(Shell parentShell) {
		super(parentShell);
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
		
		Label lblCode = new Label(container, SWT.NONE);
		lblCode.setBounds(5, 5, 100, 15);
		lblCode.setText("Code:");
		
		
		TextViewer textViewer = new TextViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		StyledText codeText = textViewer.getTextWidget();
		codeText.setDoubleClickEnabled(false);
		codeText.setEditable(false);
//		codeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		codeText.setBounds(5, 25, 584, 148);
		
		String codetext = ((EntryEditor) CDCEditor.getActiveEntryEditor()).getSelectionInTextEditor().getCodeText();
		if (codetext != null) {
			codeText.setText(codetext);
		}
		
		
		Label lblSpecification = new Label(container, SWT.NONE);
		lblSpecification.setBounds(5, 178, 100, 15);
		lblSpecification.setText("Specification:");
		
		TextViewer specTextViewer = new TextViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		StyledText specStyledText = specTextViewer.getTextWidget();
		specStyledText.setDoubleClickEnabled(false);
		specStyledText.setEditable(false);
//		specStyledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		specStyledText.setBounds(5, 198, 584, 148);
		
		String spectext = ((EntryEditor) CDCEditor.getActiveEntryEditor()).getSelectionInAcrobat().getPDFText();
		if (spectext != null) {
			specStyledText.setText(spectext);
		} else {
			spectext = "missing PDF text data";
			this.close();
			MessageDialog.openError(new Shell(), "Alert", "No PDF data has been retrieved from Acrobat.\nHave you made a text selection in Acrobat?");
		}
		
		Label lblComment = new Label(container, SWT.NONE);
		lblComment.setBounds(5, 351, 100, 15);
		lblComment.setText("Comments:");
		
		TextViewer commentTextViewer = new TextViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		commentStyledText = commentTextViewer.getTextWidget();
//		commentStyledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		commentStyledText.setBounds(5, 371, 584, 150);

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
				okButtonPressed(evt);
			}
		});
		
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	public void okButtonPressed(MouseEvent evt) {
		String comTxt = commentStyledText.getText();
		CDCEditor.addReference(comTxt);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 605);
	}

}
