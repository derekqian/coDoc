package edu.pdx.svl.coDoc.cdc.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import edu.pdx.svl.coDoc.cdc.datacenter.CDCDataCenter;
import edu.pdx.svl.coDoc.cdc.datacenter.LinkEntry;
import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;


public class EditLinkDialog extends Dialog {

	StyledText commentText;
	LinkEntry refToEdit;
	String codetext;
	String spectext;
	private String newComTxt = null;
	
	public EditLinkDialog(String codetext, String spectext, LinkEntry ref) {
		this(new Shell(), codetext, spectext, ref);
	}
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EditLinkDialog(Shell parentShell, String codetext, String spectext, LinkEntry ref) {
		super(parentShell);
		refToEdit = ref;
		this.codetext = codetext;
		this.spectext = spectext;
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
		lblCode.setBounds(5, 5, 100, 20);
		lblCode.setText("Code:");
		
		
		TextViewer textViewer = new TextViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		StyledText codeText = textViewer.getTextWidget();
		codeText.setDoubleClickEnabled(false);
		codeText.setEditable(false);
//		codeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		codeText.setBounds(5, 25, 584, 130);
		
		if (codetext != null) {
			codeText.setText(codetext);
		}
		
		
		Label lblSpecification = new Label(container, SWT.NONE);
		lblSpecification.setBounds(5, 165, 100, 20);
		lblSpecification.setText("Specification:");
		
		TextViewer specTextViewer = new TextViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		StyledText specStyledText = specTextViewer.getTextWidget();
		specStyledText.setDoubleClickEnabled(false);
		specStyledText.setEditable(false);
//		specStyledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		specStyledText.setBounds(5, 185, 584, 130);
		
		if (spectext != null) {
			specStyledText.setText(spectext);
		} else {
			spectext = "missing PDF text data";
			this.close();
			MessageDialog.openError(new Shell(), "Alert", "No PDF data has been retrieved from Acrobat.\nHave you made a text selection in Acrobat?");
		}
		
		Label lblComment = new Label(container, SWT.NONE);
		lblComment.setBounds(5, 325, 100, 20);
		lblComment.setText("Comments:");
		
		commentText = new StyledText(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		commentText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		commentText.setBounds(5, 345, 584, 130);
		String commentFromRef = refToEdit.comment;
		if (commentFromRef != null) {
			commentText.setText(commentFromRef);
		}

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
			@Override
			public void mouseUp(MouseEvent e) {
				newComTxt = commentText.getText();
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	public String getNewCommentText() {
		return newComTxt;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 575);
	}

}

