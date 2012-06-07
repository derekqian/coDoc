package edu.pdx.svl.coDoc.refexp.referenceexplorer;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;
import org.eclipse.ui.navigator.resources.ProjectExplorer;


import edu.pdx.svl.coDoc.cdc.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.cdc.referencemodel.*;
import edu.pdx.svl.coDoc.cdc.view.ConfirmationWindow;
import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;
import edu.pdx.svl.coDoc.cdc.datacenter.CDCCachedFile;
import edu.pdx.svl.coDoc.cdc.datacenter.CDCDataCenter;
import edu.pdx.svl.coDoc.cdc.datacenter.CDCModel;
import edu.pdx.svl.coDoc.cdc.datacenter.CodeSelection;
import edu.pdx.svl.coDoc.cdc.datacenter.MapEntry;
import edu.pdx.svl.coDoc.cdc.datacenter.SpecSelection;
import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;
import edu.pdx.svl.coDoc.refexp.referenceexplorer.edit.EditComment;
import edu.pdx.svl.coDoc.refexp.referenceexplorer.provider.*;
import edu.pdx.svl.coDoc.refexp.referenceexplorer.provider.LabelProvider;
import edu.pdx.svl.coDoc.refexp.view.Help;

public class ReferenceExplorerView extends ViewPart implements ISelectionListener, Listener, IPartListener2, IResourceChangeListener, IReferenceExplorer {
	public static final String ID = "edu.pdx.svl.coDoc.refexp.referenceexplorer.ReferenceExplorerView";
	Composite parent;
	private String projectname = null;
	private final int NUM_HORIZONTAL_ELEMENTS = 5; // max num elements in a row
	Text searchText;
	String searchTextStr;
	Combo combo;
	Button checkButton;
	private static TableViewer tableViewer = null;
	private static TreeViewer treeViewer = null;
	
	public void sniff() {
		System.out.println("ReferenceExplorerView.sniff()");
		projectname = null;
		IWorkbenchPart part = CDCEditor.getActivePart();
		if(part instanceof IEditorPart) {
			EntryEditor editorPart = (EntryEditor) CDCEditor.getActiveEntryEditor();
			if (editorPart != null) {
				projectname = editorPart.getProjectName();
			}
		} else if(part instanceof IViewPart) {
			IViewPart view = CDCEditor.findVisibleView(IPageLayout.ID_PROJECT_EXPLORER);
			if(view==null) {
				view = CDCEditor.findVisibleView(IPageLayout.ID_RES_NAV);
			}
			if(view!=null) {
				ISelectionProvider selProv = view.getViewSite().getSelectionProvider();
				ISelection selection = selProv.getSelection();
				if(selection != null) {
				    if(selection instanceof IStructuredSelection) {
				        for(Iterator it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				            Object element = it.next();
			            	if(element instanceof IResource) {
			            		IResource res = (IResource) element;
				            	if(CDCEditor.isCDCProject(res.getProject().getName())) {
					            	projectname = res.getProject().getName();
				            	}
				            }
				        }
				    }
				}
			}
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		
		//NH - I split the setup into several parts...
		createSearchBarAndButtons(parent);
		createTableViewer(parent);
		//createTreeViewer(parent);
		
		sniff();
		
		getSite().getWorkbenchWindow().getPartService().addPartListener(this);
		getSite().getPage().addSelectionListener(this);
		selectionChanged(getSite().getPart(), getSite().getPage().getSelection());
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		
		refresh();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		if(tableViewer != null) {
			tableViewer.getControl().setFocus();			
		}
		if(treeViewer != null) {
			treeViewer.getControl().setFocus();
		}
	}
	
	public String getProjectName() {
		return projectname;
	}
	
	public EntryEditor getActiveEntryEditor() {
		return (EntryEditor) CDCEditor.getActiveEntryEditor();
	}

	private void createSearchBarAndButtons(Composite parent) {
		GridLayout layout = new GridLayout(NUM_HORIZONTAL_ELEMENTS, false);
		parent.setLayout(layout);

		// setup search bar
		createSearchBar(parent);
		
		// setup buttons
		createSearchTypeComboBox(parent);
		createHelpButton(parent);
	}

	private void createSearchBar(Composite parent) {
		Label searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText("Search: ");
		searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		searchText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				//the enter/return key has the key code of 13
				if (e.keyCode == 13 || e.keyCode == 16777296) {
					searchTextStr = searchText.getText();
					displayListOfTextSelectionReferencesForSearchString();
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	private void createSearchTypeComboBox(Composite parent) {
		combo = new Combo (parent, SWT.READ_ONLY);
		combo.setItems (new String [] {"All Data", "Source Text", "Source File", "Project", "PDF File", "PDF Text", "PDF Page", "Comments"});
		combo.setToolTipText("Search Categories.");
		combo.select(0);
		combo.addSelectionListener( new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				searchTextStr = searchText.getText();
				displayListOfTextSelectionReferencesForSearchString();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void createHelpButton(Composite parent) {
		Button ok = new Button(parent, SWT.PUSH);
		ok.setText("Help");
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Help.openHelpBrowser();
			}
		});
	}
	
	public void refresh() {
		searchText.setText("");
		
		if(tableViewer != null) {
			tableViewer.setInput(CDCDataCenter.getInstance().getMapEntries(projectname));
			TableContentProvider tcp = (TableContentProvider)tableViewer.getContentProvider();
			tableViewer.refresh();
		}
		if(treeViewer != null) {
			treeViewer.setInput(CDCDataCenter.getInstance().getMapEntries(projectname));
			TreeContentProvider tcp = (TreeContentProvider)treeViewer.getContentProvider();
			if (checkButton.getSelection() == true) {
				tcp.allSources = true;
			} else {
				tcp.allSources = false;
			}
			treeViewer.refresh();
			treeViewer.expandToLevel(4);
		}
	}

	private void createTableViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.BORDER);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout tLayout = new TableLayout();
		table.setLayout(tLayout);
		
		tLayout.addColumnData(new ColumnWeightData(24));
		new TableColumn(table,SWT.NONE).setText("Source file");
		tLayout.addColumnData(new ColumnWeightData(60));
		new TableColumn(table,SWT.NONE).setText("Code");
		tLayout.addColumnData(new ColumnWeightData(24));
		new TableColumn(table,SWT.NONE).setText("PDF file");
		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table,SWT.NONE).setText("Page");
		tLayout.addColumnData(new ColumnWeightData(64));
		new TableColumn(table,SWT.NONE).setText("Specification Text");
		tLayout.addColumnData(new ColumnWeightData(52));
		new TableColumn(table,SWT.NONE).setText("Comments");

		tableViewer.setContentProvider(new TableContentProvider());
		tableViewer.setLabelProvider(new TableLabelProvider());

		/*table.addListener (SWT.MeasureItem, new Listener () {
			public void handleEvent (Event event) {
				event.height = 10;
			}
		});*/
		table.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				EntryEditor editor = getActiveEntryEditor();
				if(editor == null) return;
				ISelection selection = tableViewer.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator<MapEntry> iterator = sel.iterator(); iterator.hasNext();) {
						MapEntry mp = iterator.next();
						editor.selectTextInAcrobat(mp);
						editor.selectTextInTextEditor(mp);
					}
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ISelection selection = tableViewer.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator<MapEntry> iterator = sel.iterator(); iterator.hasNext();) {
						MapEntry mp = iterator.next();
						String codeFilename = mp.getCodefilename();
						String specFilename = mp.getSpecfilename();
						IPath codepath = new Path(codeFilename);
						codepath = codepath.removeFirstSegments(1); // remove "project:"
						IPath specpath = new Path(specFilename);
						specpath = specpath.removeFirstSegments(1); // remove "project:"
					    CDCEditor.open(projectname, codepath, specpath);	  
					}
				}
				setFocus();
			}
		});

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 6;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		tableViewer.getControl().setLayoutData(gridData);
	}

	private void createTreeViewer(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| /* NH */SWT.FULL_SELECTION | SWT.BORDER);

		treeViewer.setAutoExpandLevel(4);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(false);

		TreeViewerColumn viewerColumn = createTreeViewerColumn(
				"Source References", 300);
		viewerColumn.setLabelProvider(new LabelProvider());

		viewerColumn = createTreeViewerColumn("Type", 90);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Reference) {
					return ((Reference)element).getType();
				} else {
					return "";
				}
			}
		});

		viewerColumn = createTreeViewerColumn("Specification Text", 320);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Reference) {
					Reference r = (Reference)element;
					return ""; //r.pdfDescription();
				} else {
					return "";
				}
			}
		});
		
		viewerColumn = createTreeViewerColumn("PDF File", 145);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Reference) {
					Reference r = (Reference)element;
					PDFFile pdfFile = r.getPdfFile();
					return pdfFile.getFileName();
				} else {
					return "";
				}
			}
		});

		viewerColumn = createTreeViewerColumn("Page", 50);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Reference) {
					Reference r = (Reference)element;
					return ""; //r.pdfPage();
				} else {
					return "";
				}
			}
		});

		viewerColumn = createTreeViewerColumn("Comments", 263);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Reference) {
					Reference r = (Reference)element;
					return r.getComment();
				} else {
					return "";
				}
			}
		});
		viewerColumn.setEditingSupport(new EditComment(treeViewer));
		// end NH

		TreeContentProvider contentProvider = new TreeContentProvider();
		treeViewer.setContentProvider(contentProvider);
		
//		TableContentProvider contentProvider = new TableContentProvider();
//		treeViewer.setContentProvider(contentProvider);

		tree.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				EntryEditor editor = getActiveEntryEditor();
				ISelection selection = treeViewer.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator<Reference> iterator = sel.iterator(); iterator.hasNext();) {
						Reference ref = iterator.next();
					}
				}
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ISelection selection = treeViewer.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					for (Iterator<Reference> iterator = sel.iterator(); iterator.hasNext();) {
						Reference ref = iterator.next();
					}
				}
				setFocus();
			}
		});

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = NUM_HORIZONTAL_ELEMENTS;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		treeViewer.getControl().setLayoutData(gridData);
	}

	// NH
	private TreeViewerColumn createTreeViewerColumn(String title, int bound) {
		final TreeViewerColumn viewerColumn = new TreeViewerColumn(treeViewer,
				SWT.NONE);
		final TreeColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void handleEvent(Event event) {
		MessageDialog.openError(null, "Alert",  "handleEvent(Event event)");
	}
	
	public ISelection getSelection() {
		if(tableViewer != null) {
			return tableViewer.getSelection();			
		}
		if(treeViewer != null) {
			return treeViewer.getSelection();			
		}
		return null;
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println("<<<<<-------------------");
		System.out.println("ReferenceExplorerView.selectionChanged: " + part.getClass().getName());

		if (selection == null) return;
		System.out.println("ReferenceExplorerView.selectionChanged: " + selection.getClass().getName());
		
		IEditorReference[] ef = part.getSite().getPage().getEditorReferences();
		for(int i=0; i<ef.length; i++) {
			System.out.println("ReferenceExplorerView.selectionChanged: editors: " + ef[i].getEditor(false).getClass().getName());
		}
		if(part.getSite().getPage().getActiveEditor() != null)
			System.out.println("ReferenceExplorerView.selectionChanged: active editor: " + part.getSite().getPage().getActiveEditor().getClass().getName());
		System.out.println("------------------->>>>>>>>");
		
		if(part instanceof ProjectExplorer || part instanceof ResourceNavigator) {
		    if(selection instanceof IStructuredSelection) {
		        for(Iterator it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
		            Object element = it.next();
		            /* if (element instanceof IFile) {
		                IFile file = (IFile) element;
	                	projectname = file.getProject().getName();
		            } else if(element instanceof IFolder) {
		            	IFolder folder = (IFolder) element;
		            	projectname = folder.getProject().getName();
		            } else if(element instanceof IProject) {
		            	IProject project = (IProject) element;
		            	projectname = project.getName();
		            } */
	            	if(element instanceof IResource) {
	            		IResource res = (IResource) element;
	            		IProject proj = res.getProject();
		            	if(proj.isOpen() && CDCEditor.isCDCProject(proj.getName())) {
			            	projectname = res.getProject().getName();
		            	} else {
		            		projectname = null;
		            	}
		            } else {
		            	projectname = null;
		            }
		        }
		    }
		} else if(part instanceof IEditorPart) {
			EntryEditor editorPart = (EntryEditor) CDCEditor.getActiveEntryEditor();
			if (editorPart != null) {
				projectname = editorPart.getProjectName();
			} else {
				projectname = null;
			}
		}
	    refresh();
	
//		IEditorReference[] editors = part.getSite().getPage().getEditorReferences();
//		editors[0].getName();
//		
//		IViewReference[] views = part.getSite().getPage().getViewReferences();
//		views[0].getTitle();
		
//		IViewReference outlineView = views[6];
//		IViewPart ov = outlineView.getView(true);
//		ContentOutline contentOutline = (ContentOutline)ov;
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		System.out.println("partActivated!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		System.out.println("partBroughtToTop!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		System.out.println("partClosed!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		System.out.println("partDeactivated!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		System.out.println("partHidden!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		System.out.println("partInputChanged!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		System.out.println("partOpened!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		refresh();
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		System.out.println("partVisible!");
		System.out.println(partRef.getPart(false).getClass().getName());
		sniff();
		refresh();
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println("Resource Changed!");
		
		//some possible constants that a flag or kind could be...
//		int added = IResourceDelta.ADDED;
//		int changed = IResourceDelta.CHANGED;
//		int copiedFrom = IResourceDelta.COPIED_FROM;
//		int movedFrom = IResourceDelta.MOVED_FROM;
//		int movedTo = IResourceDelta.MOVED_TO;
//		int localChanged = IResourceDelta.LOCAL_CHANGED;
//		int noChange = IResourceDelta.NO_CHANGE;
//		int open = IResourceDelta.OPEN;
//		int removed = IResourceDelta.REMOVED;
//		int removedPhantom = IResourceDelta.REMOVED_PHANTOM;
//		int replaced = IResourceDelta.REPLACED;
		
		IResourceDelta workspaceIrd = event.getDelta(); //workspace
		if (workspaceIrd == null) return;
		
		IResourceDelta[] projectsChanged = workspaceIrd.getAffectedChildren();
		for (IResourceDelta projectIrd : projectsChanged) {
			int projectIrdKind = projectIrd.getKind(); //project
			int projectIrdFlag = projectIrd.getFlags();
			
			//project is renamed or moved
			if (projectIrdKind == IResourceDelta.REMOVED && projectIrdFlag == IResourceDelta.MOVED_TO) {
				IPath projectFullPath = projectIrd.getFullPath();
				IPath projectMovedToPath = projectIrd.getMovedToPath();
				break;
			}
			//project is removed
			else if (projectIrdKind == IResourceDelta.REMOVED && projectIrdFlag == IResourceDelta.NO_CHANGE) {
				IPath projectFullPath = projectIrd.getFullPath();
				//references.resourceDeltaRemoveProject(projectFullPath);
				break;
			}
			//new project imported, we need to scan to see if there are saved references
			else if (projectIrdKind == IResourceDelta.ADDED && projectIrdFlag == IResourceDelta.OPEN) {
				//((EntryEditor) CDCEditor.getActiveEntryEditor()).setDocument(SimpleXML.read());
				break;
			}
			
			IResourceDelta[] filesChanged = projectIrd.getAffectedChildren();
			for (IResourceDelta fileIrd : filesChanged) {
				int fileIrdKind = fileIrd.getKind(); //file
				int fileIrdFlag = fileIrd.getFlags();
				
				//old file renamed or moved
				if (fileIrdKind == IResourceDelta.REMOVED && fileIrdFlag == IResourceDelta.MOVED_TO) {
					IPath fileIrdFullPath = fileIrd.getFullPath();
					IPath fileIrdMovedToPath = fileIrd.getMovedToPath();
				}
				//file removed
				else if (fileIrdKind == IResourceDelta.REMOVED && fileIrdFlag == IResourceDelta.NO_CHANGE) {
					IPath fileIrdFullPath = fileIrd.getFullPath();
					//references.resourceDeltaRemoveFile(fileIrdFullPath);
				}
			}
		}
		refresh();
	}
	
	public void displayListOfTextSelectionReferencesForSelectionInActiveEditor() {
		EntryEditor editor = getActiveEntryEditor();
		References references = null;//((EntryEditor) CDCEditor.getActiveEntryEditor()).getDocument();
		TextSelectionReference currentTextSelection = null; //editor.getCurrentTextSelectionReference();
		Vector<Reference> matches = references.findReferencesContainingTextSelectionInActiveEditor(currentTextSelection);
		checkButton.setSelection(false);
		treeViewer.setInput(matches);
		treeViewer.refresh();
	}

	public void displayListOfTextSelectionReferencesForSearchString() {
		if (searchTextStr == "") {
			refresh();
			return;
		}
		References references = null;//((EntryEditor) CDCEditor.getActiveEntryEditor()).getDocument();
		Vector<Reference> matches = null;
		
		int comboSelection = combo.getSelectionIndex();
		if (comboSelection == 0) {
			if (checkButton.getSelection() == true ) {
				matches = references.findAllReferences(searchTextStr);
			} else {
				matches = references.findAllReferencesForActiveSourceFile(searchTextStr);
			}
		} else if (comboSelection == 1) {
			if (checkButton.getSelection() == true ) {
				matches = references.findSourceTextReferences(searchTextStr);
			} else {
				matches = references.findSourceTextReferencesForActiveSourceFile(searchTextStr);
			}
		} else if (comboSelection == 2) {
			matches = references.findSourceFileReferences(searchTextStr);
			checkButton.setSelection(true);
		} else if (comboSelection == 3) {
			matches = references.findProjectReferences(searchTextStr);
			checkButton.setSelection(true);
		} else if (comboSelection == 4) {
			if (checkButton.getSelection() == true ) {
				matches = references.findPDFFileReferences(searchTextStr);
			} else {
				matches = references.findPDFFileReferencesForActiveSourceFile(searchTextStr);
			}
		} else if (comboSelection == 5) {
			if (checkButton.getSelection() == true ) {
				matches = references.findPDFTextReferences(searchTextStr);
			} else {
				matches = references.findPDFTextReferencesForActiveSourceFile(searchTextStr);
			}
		} else if (comboSelection == 6) {
			if (checkButton.getSelection() == true ) {
				matches = references.findPDFPageReferences(searchTextStr);
			} else {
				matches = references.findPDFPageReferencesForActiveSourceFile(searchTextStr);
			}
		} else {
			if (checkButton.getSelection() == true ) {
				matches = references.findCommentReferences(searchTextStr);
			} else {
				matches = references.findCommentReferencesForActiveSourceFile(searchTextStr);
			}
		}
		
//		checkButton.setSelection(false);
		treeViewer.setInput(matches);
		treeViewer.refresh();
		treeViewer.setAutoExpandLevel(4);
	}
}
