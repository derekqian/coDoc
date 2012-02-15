package edu.pdx.svl.coDoc.cdt.ui.wizards;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

/**
 * This class takes care of the initialization and creation of the user's
 * desired C file. It determines whether a source (*.bbc) or header (*.bbh) file
 * should be created, and initializes it with a basic main() function. 
 */
public class CFilePage extends WizardNewFileCreationPage 
{
  private IWorkbench workbench;
  private Button sectionCheckbox;
  private Button subsectionCheckbox;
  private String suffix = new String(".bbc");
  private static int nameCounter = 1;

  public CFilePage(IWorkbench workbench, IStructuredSelection selection) 
  {
    super("File Creation Page", selection);
    setTitle("Create a new C file");
	this.workbench = workbench;
  }

  /**
   * Creates the overall visual aspect of the page.
   * 
   * @param parent The <code>Composite</code> containing the page
   */
  public void createControl(Composite parent) 
  {
    // inherit default container and name specification widgets
    super.createControl(parent);
    Composite composite = (Composite)getControl();
    final Button[] radios = new Button[2];
    final String[] fileTypes = new String[] {".bbc", ".bbh"};

    SelectionListener RadioListener = new SelectionAdapter() 
    {
      public void widgetSelected(SelectionEvent e) 
      {
        for (int i=0; i<radios.length; i++)
        if (radios[i].getSelection())
        {
          suffix = fileTypes[i];
          for (int j=0; j<i; j++)
            radios[j].setSelection(false);
          for (int j=i+1; j<radios.length; j++)
            radios[j].setSelection(false);
        }
      }
    };
	
    // sample section generation group
    new Label(composite, SWT.NONE).setText("");
    Group group = new Group(composite, SWT.NONE);
    group.setLayout(new GridLayout());
    group.setText("Choose the desired type:");
    group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

    radios[0] = new Button(group, SWT.RADIO);
    radios[0].setSelection(true);
    radios[0].setText("Source file (*.bbc)");
    radios[0].pack();

    radios[1] = new Button(group, SWT.RADIO);
    radios[1].setText("Header file (*.bbh)");
    radios[1].pack();

    for (int i=0; i<radios.length; i++) 
    {
      radios[i].pack();
      radios[i].addSelectionListener(RadioListener);
    }
    setPageComplete(validatePage());
  }

  /**
   * Creates a new file resource as requested by the user. If everything
   * is OK then answer true. If not, false will cause the dialog
   * to stay open.
   *
   * @return whether creation was successful
   */
  public boolean finish() 
  {
    // create the new file resource
    String name = this.getFileName();
    int index = name.lastIndexOf(".");
    if (index != -1) 
    {
      name = name.substring(0, index) + suffix;
	} 
    else 
    {
      name = name + suffix;
    }
    this.setFileName(name);	

    IFile newFile = createNewFile();

    // Since the file resource was created, open it for editing
	// if requested by the user
    try 
    {
      // if (openFileCheckbox.getSelection()) {
      IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
      IWorkbenchPage page = dwindow.getActivePage();
      if (page != null) 
      {
        IDE.openEditor(page, newFile, true);
      }
      System.out.println("Creating a source file.");
      //}
	} 
    catch (PartInitException e) 
    {
      e.printStackTrace();
      return false;
    }
	nameCounter++;
	return true;
  }

  /** 
   * The implementation of this <code>WizardNewFileCreationPage</code> method 
   * generates sample headings for sections and subsections in the
   * newly-created C file according to the selections of checkbox widgets
   */
  protected InputStream getInitialContents() 
  {
    return null;
  }

  protected String getNewFileLabel() 
  {
    return "New File Label"; //$NON-NLS-1$
  }

  /** (non-Javadoc)
   * Method declared on WizardNewFileCreationPage.
   */
  public void handleEvent(Event e) 
  {
	Widget source = e.widget;

    if (source == sectionCheckbox) 
    {
      if (!sectionCheckbox.getSelection())
        subsectionCheckbox.setSelection(false);
      subsectionCheckbox.setEnabled(sectionCheckbox.getSelection());
    }
	
    super.handleEvent(e);
  }
}
