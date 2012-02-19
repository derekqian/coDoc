package edu.pdx.svl.coDoc.cdc.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WelcomePage extends WizardPage 
{
	//private ISelection selection;

	public WelcomePage() 
	{
		super("wizardPage");
		setTitle("Create a new coDoc project");
		setDescription("This is description");
		//can also set image here：setImageDescription
		setPageComplete(true);
		//this.selection = selection;
		// workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) 
	{
		initializeDialogUnits(parent);

		// create container
		final Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridlayout = new GridLayout();
		gridlayout.marginWidth = 10;
		gridlayout.marginHeight = 10;
		gridlayout.numColumns = 3;
		composite.setLayout(gridlayout);

		//
		Label label = new Label(composite, 64);
		label.setText("Derek.QIAN -> This is the wizard to create a coDoc project. A coDoc project usually contains code and specifications.");
		GridData griddata = new GridData();
		griddata.widthHint = convertWidthInCharsToPixels(80);
		griddata.horizontalSpan = 3;
		label.setLayoutData(griddata);

        /*
        Text text=new Text(container,SWT.BORDER);
        text.setText("设定名称");
        text.addModifyListener(new ModifyListener() {
               @Override
               public void modifyText(ModifyEvent e) {
                      // TODO Auto-generated method stub
                      Text text=(Text)e.getSource();
                      name=text.getText();
                      if(name==null||name=="")
                             updateStatus("请输入名字");
                      else
                             updateStatus(null);
               }
        });
        Combo combo = new Combo(container,SWT.NONE);
        combo.setText("设定类别");
        combo.add("普通");
        combo.add("同事");
        combo.add("商业");
        combo.add("朋友");
        */

		setControl(composite);
		// Dialog.applyDialogFont(composite);
	}
    /*private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }*/
}