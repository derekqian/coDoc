package edu.pdx.svl.coDoc.views.ast;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.ui.part.*;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.views.navigator.ResourceNavigator;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import org.eclipse.cdt.internal.core.dom.parser.ASTNode;

import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.editor.MyASTNode;
import edu.pdx.svl.coDoc.cdc.editor.MyASTTree;


public class ASTView extends ViewPart implements IPartListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "edu.pdx.svl.coDoc.views.ast.ASTView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action showAsTreeAction;
	private boolean showAsTree = true;
	private Action doubleClickAction;

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
		private MyASTNode invisibleRoot = null;
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			parent = CDCEditor.getMyAST();
			if (parent != null) {
				invisibleRoot = new MyASTNode(null);
				if(showAsTree) {
					invisibleRoot.addChild(((MyASTTree) parent).getTree());
				} else {
					MyASTNode[] nodes = ((MyASTTree) parent).getLeaf();
					for(int i=0; i<nodes.length; i++) {
						invisibleRoot.addChild(nodes[i]);
					}
					//((MyASTTree) parent).adjustSelection1(new TextSelection(8,1));
				}
				return invisibleRoot.getChildren();
			} else {
				return new Object[0];			
			}
		}
		public Object getParent(Object child) {
			if (child instanceof MyASTNode) {
				return ((MyASTNode)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof MyASTNode) {
				return ((MyASTNode)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof MyASTNode)
				return ((MyASTNode)parent).hasChildren();
			return false;
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			String temp = obj.toString();
			temp = temp + "(" + Integer.toHexString(((MyASTNode) obj).getData().getRawSignature().hashCode()) + ")";
			return temp;
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ASTView() 
	{
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setSorter(new NameSorter());
		viewer.setInput(this);
		
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "coDoc.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ASTView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(showAsTreeAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(showAsTreeAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(showAsTreeAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		showAsTreeAction = new Action("showAsTree", IAction.AS_CHECK_BOX) {
			public void run() {
				showAsTree = isChecked();
				viewer.refresh();
			}
		};
		showAsTreeAction.setText("showAsTree");
		showAsTreeAction.setChecked(true);
		showAsTreeAction.setToolTipText("showAsTree");
		showAsTreeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_DEF_VIEW));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() 
	{
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if((selection != null) && (selection instanceof IStructuredSelection)) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for(Iterator<MyASTNode> it = sel.iterator(); it.hasNext();) {
						MyASTNode astNode = it.next();
						IASTNode node = astNode.getData();
						IEditorPart editor = CDCEditor.getActiveCEditorChild((EntryEditor) CDCEditor.getActiveEntryEditor());
						if((editor!=null) && (editor instanceof AbstractTextEditor)) {
							IASTFileLocation f = node.getFileLocation();
							((AbstractTextEditor) editor).selectAndReveal(f.getNodeOffset(), f.getNodeLength());
						}
						/*ASTNode node = (ASTNode) astNode.getData();
						IEditorPart editor = CDCEditor.getActiveCEditorChild((EntryEditor) CDCEditor.getActiveEntryEditor());
						if((editor!=null) && (editor instanceof AbstractTextEditor)) {
							((AbstractTextEditor) editor).selectAndReveal(node.getOffset(), node.getLength());
						}*/
					}
				}
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) 
			{
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) 
	{
		MessageDialog.openInformation(viewer.getControl().getShell(), "Property View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() 
	{
		viewer.getControl().setFocus();
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
	}
}