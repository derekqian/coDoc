package edu.pdx.svl.coDoc.refexp.view;

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


//import org.eclipse.cdt.internal.ui.editor.Spec2ModelEditor;

import edu.pdx.svl.coDoc.refexp.Global;
import edu.pdx.svl.coDoc.refexp.referencemodel.PDFSelection;
import edu.pdx.svl.coDoc.refexp.referencemodel.TextSelectionReference;


public class ConfirmationWindow extends Dialog {

	TextSelectionReference tsr;
	
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
	
		
//		Shell sh = this.getShell();
//		sh.setText("Confirm New Spec2Model Reference");
		
		getReferenceData();
	}

	public void getReferenceData() {
		tsr = Global.INSTANCE.referenceExplorerView.getCurrentTextSelectionReference();
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
		
		String txt = tsr.getText();
		if (txt != null) {
			codeText.setText(txt);
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
		
		PDFSelection pdfSel = tsr.getPdfSelection();
		if (pdfSel != null && pdfSel.getText().equals("") == false) {
			txt = tsr.getPdfSelection().getText();
		} else {
			txt = "missing PDF text data";
			this.close();
			MessageDialog.openError(new Shell(), "Alert", "No PDF data has been retrieved from Acrobat.\nHave you made a text selection in Acrobat?");
		}
		
		if (txt != null) {
			specStyledText.setText(txt);
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
		Global.INSTANCE.references.addReference(tsr, comTxt);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 605);
	}

}
