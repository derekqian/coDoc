package edu.pdx.svl.coDoc.cdc.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import edu.pdx.svl.coDoc.cdc.datacenter.CDCDataCenter;
import edu.pdx.svl.coDoc.cdc.datacenter.MapEntry;
import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;
import edu.pdx.svl.coDoc.cdc.referencemodel.Reference;


public class EditView extends Dialog {

	StyledText commentText;
	MapEntry refToEdit;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EditView(Shell parentShell, MapEntry ref) {
		super(parentShell);
		refToEdit = ref;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblComment = new Label(container, SWT.NONE);
		lblComment.setText("Comments:");
		
		commentText = new StyledText(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		commentText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		String commentFromRef = refToEdit.getComment();
		if (commentFromRef != null) {
			commentText.setText(refToEdit.getComment());
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
				String txt = commentText.getText();
				IReferenceExplorer view = (IReferenceExplorer)CDCEditor.findView("edu.pdx.svl.coDoc.refexp.referenceexplorer.ReferenceExplorerView");
				MapEntry newMap = new MapEntry();
				newMap.setCodefilename(refToEdit.getCodefilename());
				newMap.setCodeselpath(refToEdit.getCodeselpath());
				newMap.setSpecfilename(refToEdit.getSpecfilename());
				newMap.setSpecselpath(refToEdit.getSpecselpath());
				newMap.setComment(txt);
				CDCDataCenter.getInstance().deleteMapEntry(view.getProjectName(),refToEdit.getCodefilename(), refToEdit.getCodeselpath(), refToEdit.getSpecfilename(), refToEdit.getSpecselpath(), refToEdit.getComment());
				CDCDataCenter.getInstance().addMapEntry(view.getProjectName(),newMap.getCodefilename(), newMap.getCodeselpath(), newMap.getSpecfilename(), newMap.getSpecselpath(), newMap.getComment());
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}

