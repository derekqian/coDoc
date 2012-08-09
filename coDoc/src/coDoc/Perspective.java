package coDoc;

//import hdvd.views.DualConsole;
//import hdvd.views.SimControl;
//import hdvd.views.SoloConsole;
//import hdvd.views.SpecControl;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class Perspective implements IPerspectiveFactory {
	
	public final static String ID = "coDoc.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		defineLayout(layout);
	}
	
	public void defineLayout(IPageLayout layout) {
		layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.30f, editorArea);
		left.addView(IPageLayout.ID_PROJECT_EXPLORER);
		
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.70f, editorArea);
		right.addView(IPageLayout.ID_OUTLINE);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.70f, editorArea);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
	}
}