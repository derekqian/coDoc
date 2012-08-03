package edu.pdx.svl.coDoc.cdc.datacenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;
import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;


public class CDCDataCenter {
	private static CDCDataCenter instance = null;	
	private Hashtable<String, CDCCachedFile> projects;
	// eclipse TreeViewer has a bug, use invisible root as a workaround.
	private EntryNode invisibleroot = new EntryNode(null);

	public static CDCDataCenter getInstance() {
		if(instance == null) {
			instance = new CDCDataCenter();
		}
		return instance;
	}
	
	private CDCDataCenter() {
		projects = new Hashtable<String, CDCCachedFile>();
	}
	
	private CDCCachedFile getCDCCachedFile(String cdcfilename) {
		if(cdcfilename==null) {
			return null;
		}
		if(projects.get(cdcfilename)==null) {
			CDCCachedFile cdcCachedFile = new CDCCachedFile();
			File tempFile = new File(cdcfilename);
			if(tempFile.exists()) {
				cdcCachedFile.open(cdcfilename);
				projects.put(cdcfilename, cdcCachedFile);
			}
		}
		return projects.get(cdcfilename);
	}
	
	public void refresh(String cdcfilename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.refresh();
		}
	}
	public void flush(String cdcfilename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.flush();
		}
	}
	
	public void addCodeFileEntry(String cdcfilename, String filename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.addCodeFileEntry(filename);
		}
	}
	
	public void deleteCodeFileEntry(String cdcfilename, String uuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.deleteCodeFileEntry(uuid);
		}
	}
	
	public void addSpecFileEntry(String cdcfilename, String filename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.addSpecFileEntry(filename);
		}
	}
	
	public void deleteSpecFileEntry(String cdcfilename, String uuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.deleteSpecFileEntry(uuid);
		}
	}
	
	public void addCategoryEntry(String cdcfilename, String parentfolderuuid, String foldername) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.addFolderEntry(parentfolderuuid, foldername);
		}
	}
	
	public CategoryEntry getCategoryEntry(String cdcfilename, String uuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		FolderEntry entry = null;
		if(f!=null) {
			entry = f.getFolderEntry(uuid);
		}
		if(entry != null) {
			return new CategoryEntry(uuid, entry.getTime(), entry.getOS(), ((FolderEntry) entry).getFoldername(), entry.getCreater(), entry.getFolderpath());
		}
		return null;
	}
	
	public void deleteCategoryEntry(String cdcfilename, String uuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.deleteFolderEntry(uuid);
		}
	}
	
	/*public void addLinkEntry(String cdcfilename, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		String parentfolderuuid = f.getMapIdTree().getData();
		if(f!=null) {
			f.addMapEntry(parentfolderuuid, codefilename, codeselpath, specfilename, specselpath, comment);
		}
	}*/
	
	public void addLinkEntry(String cdcfilename, String parentfolderuuid, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.addMapEntry(parentfolderuuid, codefilename, codeselpath, specfilename, specselpath, comment);
		}
	}
	
	public LinkEntry getLinkEntry(String cdcfilename, String uuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		MapEntry entry = null;
		if(f!=null) {
			entry = f.getMapEntry(uuid);
		}
		if(entry != null) {
			return new LinkEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), f.getCodeFilename(entry.getCodefileUUID()), entry.getCodeselpath(), f.getSpecFilename(entry.getSpecfileUUID()), entry.getSpecselpath(), entry.getComment());
		}
		return null;
	}
	
	public void deleteLinkEntry(String cdcfilename, String uuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.deleteMapEntry(uuid);
		}
	}
	
	public boolean parentOf(String cdcfilename, String childuuid, String parentuuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			return f.parentOf(childuuid, parentuuid);
		}
		return false;
	}
	
	public void moveEntry(String cdcfilename, String sourceuuid, String destuuid) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.moveEntry(sourceuuid, destuuid);
		}
	}
	
	public void setLastOpenedCodeFilename(String cdcfilename, String codeFilename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.setLastOpenedCodeFilename(codeFilename);
		}
	}
	public String getLastOpenedCodeFilename(String cdcfilename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			return f.getLastOpenedCodeFilename();
		} else {
			return null;
		}
	}
	
	public void setLastOpenedSpecFilename(String cdcfilename, String specFilename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			f.setLastOpenedSpecFilename(specFilename);
		}
	}
	public String getLastOpenedSpecFilename(String cdcfilename) {
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		if(f!=null) {
			return f.getLastOpenedSpecFilename();
		} else {
			return null;
		}
	}
	
	private boolean isEntryMatch(LinkEntry entry, MapSelectionFilter filter) {
		boolean res = false;
		switch(filter.getSelector()) {
		case MapSelectionFilter.ALLDATA:
			if((entry.codefilename.contains(filter.getSearchStr()))
				|| (entry.codeselpath.getSyntaxCodeText().contains(filter.getSearchStr()))
				|| (entry.specfilename.contains(filter.getSearchStr()))
				|| (Integer.toString(entry.specselpath.getPage()).equals(filter.getSearchStr()))
				|| (entry.specselpath.getPDFText().contains(filter.getSearchStr()))
				|| (entry.comment.contains(filter.getSearchStr()))) {
				res = true;
			}
			break;
		case MapSelectionFilter.CODEFILE:
			if(entry.codefilename.contains(filter.getSearchStr())) {
				res = true;
			}
			break;
		case MapSelectionFilter.CODETEXT:
			if(entry.codeselpath.getSyntaxCodeText().contains(filter.getSearchStr())) {
				res = true;
			}
			break;
		case MapSelectionFilter.SPECFILE:
			if(entry.specfilename.contains(filter.getSearchStr())) {
				res = true;
			}
			break;
		case MapSelectionFilter.PDFPAGE:
			if(Integer.toString(entry.specselpath.getPage()).equals(filter.getSearchStr())) {
				res = true;
			}
			break;
		case MapSelectionFilter.SPECTEXT:
			if(entry.specselpath.getPDFText().contains(filter.getSearchStr())) {
				res = true;
			}
			break;
		case MapSelectionFilter.COMMENT:
			if(entry.comment.contains(filter.getSearchStr())) {
				res = true;
			}
			break;
		}
		return res;
	}
	/**
	 * 
	 * @param cdcfilename
	 * @param filter
	 * @return The list of uuids of maps.
	 */
	public EntryNode getLinkTable(String cdcfilename, MapSelectionFilter filter) {
		invisibleroot.clearChildren();
		if(cdcfilename==null) {
			return invisibleroot;
		}
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		Stack<FolderMapTreeNode> stack = new Stack<FolderMapTreeNode>();
		stack.push(f.getMapIdTree());
		while(!stack.empty()) {
			FolderMapTreeNode node = stack.pop();
			String uuid = node.getData();
			if(getCategoryEntry(cdcfilename, uuid) != null) {
				// this is a folder
				FolderMapTreeNode[] children = node.getChildren();
				for(int i=children.length-1; i>=0; i--) {
					stack.push(children[i]);
				}
			} else {
				// this is a map
				MapEntry entry = f.getMapEntry(uuid);
				LinkEntry le = new LinkEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), f.getCodeFilename(entry.getCodefileUUID()), entry.getCodeselpath(), f.getSpecFilename(entry.getSpecfileUUID()), entry.getSpecselpath(), entry.getComment());
				if(isEntryMatch(le, filter)) {
					EntryNode en = new EntryNode(le);
					invisibleroot.addChildZ(en);
					en.setParent(invisibleroot);					
				}
			}
		}
		return invisibleroot;
	}
	public EntryNode sortLinkTable(String cdcfilename, MapSelectionSort sorter) {
		sortTree(invisibleroot,sorter);
		return invisibleroot;
	}
	
	public EntryNode getLinkTree(String cdcfilename, MapSelectionFilter filter) {
		invisibleroot.clearChildren();
		if(cdcfilename==null) {
			return null;
		}
		CDCCachedFile f = getCDCCachedFile(cdcfilename);
		Stack<FolderMapTreeNode> stack = new Stack<FolderMapTreeNode>();
		stack.push(f.getMapIdTree());
		Stack<EntryNode> stackdes = new Stack<EntryNode>();
		String uuid = f.getMapIdTree().getData();
		FolderMapEntry entry = f.getFolderEntry(uuid);
		EntryNode childdes = new EntryNode(new CategoryEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), ((FolderEntry) entry).getFoldername(), ((FolderEntry) entry).getFolderpath()));
		stackdes.push(childdes);
		invisibleroot.addChildA(childdes);
		childdes.setParent(invisibleroot);
		while(!stack.empty()) {
			FolderMapTreeNode node = stack.pop();
			EntryNode parentdes = stackdes.pop();
			uuid = node.getData();
			if(getCategoryEntry(cdcfilename, uuid) != null) {
				// this is a folder
				FolderMapTreeNode[] children = node.getChildren();
				for(int i=children.length-1; i>=0; i--) {
					stack.push(children[i]);
					uuid = children[i].getData();
					entry = f.getFolderEntry(uuid);
					if(entry != null) {
						childdes = new EntryNode(new CategoryEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), ((FolderEntry) entry).getFoldername(), ((FolderEntry) entry).getFolderpath()));
						parentdes.addChildA(childdes);
						childdes.setParent(parentdes);
					} else {
						entry = f.getMapEntry(uuid);
						LinkEntry le = new LinkEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), f.getCodeFilename(((MapEntry) entry).getCodefileUUID()), ((MapEntry) entry).getCodeselpath(), f.getSpecFilename(((MapEntry) entry).getSpecfileUUID()), ((MapEntry) entry).getSpecselpath(), ((MapEntry) entry).getComment());
						childdes = new EntryNode(le);
						if(isEntryMatch(le, filter)) {
							parentdes.addChildA(childdes);
							childdes.setParent(parentdes);
						}
					}
					stackdes.push(childdes);
				}
			}
		}
		return invisibleroot;
	}
	public EntryNode sortLinkTree(String cdcfilename, MapSelectionSort sorter) {
		sortTree(invisibleroot,sorter);
		return invisibleroot;
	}
	private void sortTree(EntryNode root, MapSelectionSort sorter) {
		Stack<EntryNode> stack = new Stack<EntryNode>();
		stack.push(root);
		while(!stack.empty()) {
			EntryNode parent = stack.pop();
			if(parent.hasChildren()) {
				EntryNode[] children = parent.getChildren();
				for(int i=children.length-1; i>0; i--) {
					for(int j=0; j<i; j++) {
						boolean needSwap = false;
						switch(sorter.getKey()) {
						case MapSelectionSort.CODEFILE:
							needSwap = less(children[j],children[j+1],sorter) ^ sorter.getCodefilename();
							break;
						case MapSelectionSort.CODETEXT:
							needSwap = less(children[j],children[j+1],sorter) ^ sorter.getCodefiletext();
							break;
						case MapSelectionSort.SPECFILE:
							needSwap = less(children[j],children[j+1],sorter) ^ sorter.getSpecfilename();
							break;
						case MapSelectionSort.PDFPAGE:
							needSwap = less(children[j],children[j+1],sorter) ^ sorter.getSpecfilepage();
							break;
						case MapSelectionSort.SPECTEXT:
							needSwap = less(children[j],children[j+1],sorter) ^ sorter.getSpecfiletext();
							break;
						case MapSelectionSort.COMMENT:
							needSwap = less(children[j],children[j+1],sorter) ^ sorter.getComments();
							break;
						}
						if(needSwap) {
							EntryNode temp = children[j];
							children[j] = children[j+1];
							children[j+1] = temp;
						}
					}
				}
				parent.clearChildren();
				for(int i=0;i<children.length;i++) {
					parent.addChildZ(children[i]);
					stack.push(children[i]);
				}
			}
		}
	}
	private boolean less(EntryNode node1, EntryNode node2, MapSelectionSort sorter) {
		if((node1.getData() instanceof CategoryEntry) && (node2.getData() instanceof LinkEntry)) {
			return true;
		} else if((node1.getData() instanceof LinkEntry) && (node2.getData() instanceof CategoryEntry)) {
			return false;
		} else if((node1.getData() instanceof CategoryEntry) && (node2.getData() instanceof CategoryEntry)) {
			CategoryEntry e1 = (CategoryEntry) node1.getData();
			CategoryEntry e2 = (CategoryEntry) node2.getData();
			if(e1.name.compareToIgnoreCase(e2.name) > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			LinkEntry e1 = (LinkEntry) node1.getData();
			LinkEntry e2 = (LinkEntry) node2.getData();
			if(sorter.isNeedsort()) {
				switch(sorter.getKey()) {
				case MapSelectionSort.CODEFILE:
					if(e1.codefilename.compareToIgnoreCase(e2.codefilename) > 0) {
						return false;
					} else {
						return true;
					}
				case MapSelectionSort.CODETEXT:
					if(e1.codeselpath.getSyntaxCodeText().compareToIgnoreCase(e2.codeselpath.getSyntaxCodeText()) > 0) {
						return false;
					} else {
						return true;
					}
				case MapSelectionSort.SPECFILE:
					if(e1.specfilename.compareToIgnoreCase(e2.specfilename) > 0) {
						return false;
					} else {
						return true;
					}
				case MapSelectionSort.PDFPAGE:
					if(e1.specselpath.getPage() > e2.specselpath.getPage()) {
						return false;
					} else {
						return true;
					}
				case MapSelectionSort.SPECTEXT:
					if(e1.specselpath.getPDFText().compareToIgnoreCase(e2.specselpath.getPDFText()) > 0) {
						return false;
					} else {
						return true;
					}
				case MapSelectionSort.COMMENT:
					if(e1.comment.compareToIgnoreCase(e2.comment) > 0) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		return true;
	}
	
	public static void convertCDCFile0() {
		CDCModel_ cdcModel1 = null;
		File cdcFile1 = new File("/home/derek/runtime-EclipseApplication/dio/dio.raw.cdc");
		Serializer serializer = new Persister();
		try {
			cdcModel1 = serializer.read(CDCModel_.class, cdcFile1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		CDCModel cdcModel2 = new CDCModel();
		cdcModel2.getHead().setCreatetime(cdcModel1.getHead().getCreatetime());
		cdcModel2.getHead().setOS(cdcModel1.getHead().getOS());
		cdcModel2.getHead().setCreater(cdcModel1.getHead().getCreater());
		cdcModel2.getHead().setFiletype(cdcModel1.getHead().getFiletype());
		cdcModel2.setLastOpenedCodeFilename(cdcModel1.getLastOpenedCodeFilename());
		cdcModel2.setLastOpenedSpecFilename(cdcModel1.getLastOpenedSpecFilename());
		Vector<MapEntry_> maps1 = cdcModel1.getMapEntries();
		Vector<OpEntry_> ops1 = cdcModel1.getOperationList();
		Iterator it1 = maps1.iterator();
		while(it1.hasNext()) {
			MapEntry_ entry1 = (MapEntry_) it1.next();
			String codefilename1 = entry1.getCodefilename();
			CodeSelection_ codeselpath1 = entry1.getCodeselpath();
			String specfilename1 = entry1.getSpecfilename();
			SpecSelection_ specselpath1 = entry1.getSpecselpath();
			String comment1 = entry1.getComment();
			
			FolderMapTreeNode root = cdcModel2.getMapIdTree();
			String parentfolderuuid = root.getData();
			String codefilename2 = codefilename1;
			CodeSelection codeselpath2 = new CodeSelection();
			codeselpath2.setSelCodePath(codeselpath1.getCodeSelPath());
			codeselpath2.setSelCodeText(codeselpath1.getCodeText());
			String specfilename2 = specfilename1;
			SpecSelection specselpath2 = new SpecSelection();
			specselpath2.setLeft(specselpath1.getLeft());
			specselpath2.setRight(specselpath1.getRight());
			specselpath2.setTop(specselpath1.getTop());
			specselpath2.setBottom(specselpath1.getBottom());
			specselpath2.setPage(specselpath1.getPage());
			specselpath2.setPDFText(specselpath1.getPDFText());
			String comment2 = comment1;
			cdcModel2.addMapEntry(parentfolderuuid, codefilename2, codeselpath2, specfilename2, specselpath2, comment2);
		}
		File cdcFile2 = new File("/home/derek/runtime-EclipseApplication/dio/dio.new.cdc");
		try {
			serializer.write(cdcModel2, cdcFile2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}
	
	public static void convertCDCFile() {
		CDCModel_ cdcModel1 = null;
		File cdcFile1 = new File("/home/derek/runtime-EclipseApplication/dio/dio.raw.cdc");
		Serializer serializer = new Persister();
		try {
			cdcModel1 = serializer.read(CDCModel_.class, cdcFile1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		CDCModel cdcModel2 = new CDCModel();
		cdcModel2.getHead().setCreatetime(cdcModel1.getHead().getCreatetime());
		cdcModel2.getHead().setOS(cdcModel1.getHead().getOS());
		cdcModel2.getHead().setCreater(cdcModel1.getHead().getCreater());
		cdcModel2.getHead().setFiletype(cdcModel1.getHead().getFiletype());
		cdcModel2.setLastOpenedCodeFilename(cdcModel1.getLastOpenedCodeFilename());
		cdcModel2.setLastOpenedSpecFilename(cdcModel1.getLastOpenedSpecFilename());
		Vector<MapEntry_> maps1 = cdcModel1.getMapEntries();
		Vector<OpEntry_> ops1 = cdcModel1.getOperationList();
		Iterator it1 = ops1.iterator();
		while(it1.hasNext()) {
			OpEntry_ entry1 = (OpEntry_) it1.next();
			String tempstr = entry1.getOperation();
			System.out.println(tempstr);
			Vector<String> operations = new Vector<String>();
			while(tempstr.lastIndexOf('#') != -1) {
				int index = tempstr.lastIndexOf('#');
				if(tempstr.charAt(index-1) == '#') {
					index--;
				}
				operations.add(0, tempstr.substring(index+1));
				tempstr = tempstr.substring(0, index);
			}
			operations.add(0, tempstr);
			if(operations.size() != 10) {
				try {
					throw new Exception("invalid operation record!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String time = operations.get(0); // time
			String os = operations.get(1); // os
			String creater = operations.get(2); // author
			if(operations.get(3).equals("add") && operations.get(4).equals("mapentry")) {
				FolderMapTreeNode root = cdcModel2.getMapIdTree();
				String parentfolderuuid = root.getData();
				String codefilename2 = operations.get(5);
				CodeSelection codeselpath2 = new CodeSelection();
				String codepath = operations.get(6);
				int index = codepath.lastIndexOf('/');
				codeselpath2.setSelCodeText(codepath.substring(0,index));
				codepath = codepath.substring(index+1);
				// index = codepath.indexOf('\\');
				codeselpath2.setSelCodePath(codepath);
				String specfilename2 = operations.get(7);
				SpecSelection specselpath2 = new SpecSelection();
				String specpath = operations.get(8);
				index = specpath.indexOf('/');
				specselpath2.setPage(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setLeft(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setTop(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setRight(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setBottom(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				specselpath2.setPDFText(specpath);
				String comment2 = operations.get(9);
				
				IPath codefilepath = new Path(codefilename2);
				codefilepath = codefilepath.removeFirstSegments(1); // remove "project:"
				IPath specfilepath = new Path(specfilename2);
				specfilepath = specfilepath.removeFirstSegments(1); // remove "project:"
				CDCEditor.open("dio", codefilepath, specfilepath);
				EntryEditor editor = (EntryEditor) CDCEditor.getActiveEntryEditor();
				IEditorPart cEditor = CDCEditor.getActiveCEditorChild(editor);
				ISelectionProvider selProv = cEditor.getEditorSite().getSelectionProvider();
				index = codepath.indexOf('\\');
				selProv.setSelection(new TextSelection(Integer.valueOf(codepath.substring(0,index)), Integer.valueOf(codepath.substring(index+1))));
				//editor.selection2Node1(new TextSelection());
				
				cdcModel2.addMapEntry(time, os, creater, parentfolderuuid, codefilename2, codeselpath2, specfilename2, specselpath2, comment2);
			} else if(operations.get(3).equals("del") && operations.get(4).equals("mapentry")) {
				String codefilename2 = operations.get(5);
				String codefileuuid2 = cdcModel2.getBody().codefiles.getFileEntryId(codefilename2);
				CodeSelection codeselpath2 = new CodeSelection();
				String codepath = operations.get(6);
				int index = codepath.indexOf('/');
				codeselpath2.setSelCodeText(codepath.substring(0,index));
				codepath = codepath.substring(index+1);
				// index = codepath.indexOf('\\');
				codeselpath2.setSelCodePath(codepath);
				String specfilename2 = operations.get(7);
				String specfileuuid2 = cdcModel2.getBody().specfiles.getFileEntryId(specfilename2);
				SpecSelection specselpath2 = new SpecSelection();
				String specpath = operations.get(8);
				index = specpath.indexOf('/');
				specselpath2.setPage(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setLeft(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setTop(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setRight(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				index = specpath.indexOf('/');
				specselpath2.setBottom(Integer.valueOf(specpath.substring(0,index)));
				specpath = specpath.substring(index+1);
				specselpath2.setPDFText(specpath);
				String comment2 = operations.get(9);
				String uuid = cdcModel2.getBody().maps.getMapEntryId(codefileuuid2, codeselpath2, specfileuuid2, specselpath2, comment2);
				cdcModel2.deleteMapEntry(time, os, creater, uuid);
			} else {
				try {
					throw new Exception("unsupported operation record!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		File cdcFile2 = new File("/home/derek/runtime-EclipseApplication/dio/dio.new.cdc");
		try {
			serializer.write(cdcModel2, cdcFile2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}
}
