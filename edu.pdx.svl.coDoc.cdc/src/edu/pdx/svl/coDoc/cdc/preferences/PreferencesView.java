package edu.pdx.svl.coDoc.cdc.preferences;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;


import edu.pdx.svl.coDoc.cdc.Global;
import edu.pdx.svl.coDoc.cdc.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.cdc.referencemodel.PDFFile;
import edu.pdx.svl.coDoc.cdc.referencemodel.PDFManager;
import edu.pdx.svl.coDoc.cdc.view.FileChooser;


public class PreferencesView extends Dialog {
	private Text currentlyActivePDFTxtBox;
	Button selectPDFButton;
	Button confirmButton;
	Button directlyAddReferenceButton;
	
	public PreferencesView() {
		this(new Shell());
	}
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PreferencesView(Shell parentShell) {
		super(parentShell);
//		parentShell.setText("Preferences");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblCurrentlyActivePdf = new Label(container, SWT.NONE);
		lblCurrentlyActivePdf.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblCurrentlyActivePdf.setText("Currently Active PDF");
		
		currentlyActivePDFTxtBox = new Text(container, SWT.BORDER);
		PDFFile pdfFile = PDFManager.INSTANCE.getCurrentPdfFile();
		String activeFilePath = pdfFile.description();
		currentlyActivePDFTxtBox.setText(activeFilePath);
		currentlyActivePDFTxtBox.setToolTipText("Press the Select PDF Button,\n or type a PDF file path and press enter");
		currentlyActivePDFTxtBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		currentlyActivePDFTxtBox.setEditable(false);
		//TODO setup ability to select a pdf file by directly entering a path
//		currentlyActivePDFTxtBox.setEditable(true);
//		KeyListener keyListener = new KeyListener() {
//			
//			@Override
//			public void keyReleased(KeyEvent e) {
//
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.character == '\r') {
//					String enteredPath = currentlyActivePDFTxtBox.getText();
//					javax.swing.JOptionPane.showMessageDialog(null, enteredPath);
//				}
//			}
//		};
//		currentlyActivePDFTxtBox.addKeyListener(keyListener);
		
		selectPDFButton = new Button(container, SWT.NONE);
		selectPDFButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		selectPDFButton.setText("Select PDF");
		new Label(container, SWT.NONE);

		selectPDFButton.setToolTipText("Current PDF:\n" + 
				PDFManager.INSTANCE.getCurrentPdfFile().description());
		
		selectPDFButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				File f = FileChooser.getFile();
				PDFManager.INSTANCE.openFileInAcrobat(f);
				selectPDFButton.setToolTipText("Current PDF:\n" +
						PDFManager.INSTANCE.getCurrentPdfFile().description());
				currentlyActivePDFTxtBox.setText(f.getPath());
			}
		});
		
		Group grpConfirmationWindow = new Group(container, SWT.NONE);
		grpConfirmationWindow.setToolTipText("If \"Confirm New Reference Dialog\" is selected, \r\nthen you will be shown a preview of the new \r\nreference you are about to make.");
		grpConfirmationWindow.setLayout(new GridLayout(2, false));
		GridData gd_grpConfirmationWindow = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpConfirmationWindow.widthHint = 472;
		grpConfirmationWindow.setLayoutData(gd_grpConfirmationWindow);
		grpConfirmationWindow.setText("Confirmation Window");
		
		confirmButton = new Button(grpConfirmationWindow, SWT.RADIO);
		confirmButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				confirmButton.setSelection(true);
				directlyAddReferenceButton.setSelection(false);
				PreferenceValues.getInstance().setUseConfirmationWindow(true);
			}
		});
		if (PreferenceValues.getInstance().isUseConfirmationWindow() == true) {
			confirmButton.setSelection(true);
		} else {
			confirmButton.setSelection(false);
		}
		
		confirmButton.setText("Confirm New Reference Dialog");
		
		directlyAddReferenceButton = new Button(grpConfirmationWindow, SWT.RADIO);
		directlyAddReferenceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				confirmButton.setSelection(false);
				directlyAddReferenceButton.setSelection(true);
				PreferenceValues.getInstance().setUseConfirmationWindow(false);
			}
		});
		if (PreferenceValues.getInstance().isUseConfirmationWindow() == true) {
			directlyAddReferenceButton.setSelection(false);
		} else {
			directlyAddReferenceButton.setSelection(true);
		}
		directlyAddReferenceButton.setText("Directly Add New Reference");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 250);
	}
}
