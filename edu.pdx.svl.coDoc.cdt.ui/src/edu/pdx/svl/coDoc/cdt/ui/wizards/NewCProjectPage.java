package edu.pdx.svl.coDoc.cdt.ui.wizards;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewCProjectPage extends WizardNewProjectCreationPage
{
  IWorkbench workbench;
  String nature;
  
  public NewCProjectPage(String pageName)
  {
    super("Project Creation Page");
    setTitle("Create a new C project");
  }
}
