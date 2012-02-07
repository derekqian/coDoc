/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.jdt.ui.JavaUI;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class Perspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public Perspective() 
	{
		super();
	}

	public void createInitialLayout(IPageLayout factory) 
	{
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	@SuppressWarnings("deprecation")
	private void addViews() 
	{
		// Creates the overall folder layout. 
		// Note that each new Folder uses a percentage of the remaining EditorArea.

		//addStandaloneView(String viewId, boolean showTitle, int relationship, float ratio, String refId)
		//factory.addStandaloneView("topLeft", true, IPageLayout.LEFT, 0.20f, factory.getEditorArea());
		IFolderLayout topLeft = factory.createFolder("topLeft", IPageLayout.LEFT, 0.20f, factory.getEditorArea());
		topLeft.addView(IPageLayout.ID_RES_NAV);
		//topLeft.addView("org.eclipse.jdt.junit.ResultView");
		
		IFolderLayout bottom = factory.createFolder("bottomRight", IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
		bottom.addView("edu.pdx.svl.coDoc.views.PropertyView");
		//bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView("org.eclipse.team.ui.GenericHistoryView");
		//reserve the place for console view.
		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		
		factory.addFastView("org.eclipse.pde.runtime.LogView");
		//factory.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f); //NON-NLS-1
		//factory.addFastView("org.eclipse.team.sync.views.SynchronizeView", 0.50f); //NON-NLS-1
	}

	private void addActionSets() 
	{
		/*factory.addActionSet("org.eclipse.debug.ui.launchActionSet");
		factory.addActionSet("org.eclipse.debug.ui.debugActionSet");
		factory.addActionSet("org.eclipse.debug.ui.profileActionSet");
		factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet");
		factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet");
		factory.addActionSet("org.eclipse.team.ui.actionSet");
		factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet");
		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation");
		factory.addActionSet(JavaUI.ID_ACTION_SET);
		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);*/
	}

	private void addPerspectiveShortcuts() 
	{
		//factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective");
		//factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective");
		factory.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
	}

	private void addNewWizardShortcuts() 
	{
		factory.addNewWizardShortcut("edu.pdx.svl.coDocWizard.wizard");
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
	}

	private void addViewShortcuts() 
	{
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		/*factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView");
		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView");
		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView");
		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView");
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView");
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);*/
	}

}
