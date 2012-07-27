package edu.pdx.svl.coDoc.cdc.datacenter;

import java.io.File;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;


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
	
	public CDCDataCenter() {
		projects = new Hashtable<String, CDCCachedFile>();
	}
	
	private CDCCachedFile getCDCCachedFile(String projname) {
		if(projname==null) {
			return null;
		}
		if(projects.get(projname)==null) {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject[] projs = workspaceRoot.getProjects();
			for(IProject proj : projs) {
				if(proj.getName().equals(projname)) {
					CDCCachedFile cdcCachedFile = new CDCCachedFile();
					String filename = CDCEditor.proj2cdcName(proj);
					File tempFile = new File(filename);
					if(tempFile.exists()) {
						cdcCachedFile.open(filename);
						projects.put(projname, cdcCachedFile);
						break;						
					}
				}
			}
		}
		return projects.get(projname);
	}
	
	public void refresh(String projname) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.refresh();
		}
	}
	public void flush(String projname) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.flush();
		}
	}
	
	public void addCodeFileEntry(String projname, String filename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.addCodeFileEntry(filename);
		}
	}
	
	public void deleteCodeFileEntry(String projname, String uuid) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteCodeFileEntry(uuid);
		}
	}
	
	public void addSpecFileEntry(String projname, String filename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.addSpecFileEntry(filename);
		}
	}
	
	public void deleteSpecFileEntry(String projname, String uuid) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteSpecFileEntry(uuid);
		}
	}
	
	public void addCategoryEntry(String projname, String parentfolderuuid, String foldername) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.addFolderEntry(parentfolderuuid, foldername);
		}
	}
	
	public CategoryEntry getCategoryEntry(String projname, String uuid) {
		CDCCachedFile f = getCDCCachedFile(projname);
		FolderEntry entry = null;
		if(f!=null) {
			entry = f.getFolderEntry(uuid);
		}
		if(entry != null) {
			return new CategoryEntry(uuid, entry.getTime(), entry.getOS(), ((FolderEntry) entry).getFoldername(), entry.getCreater(), entry.getFolderpath());
		}
		return null;
	}
	
	public void deleteCategoryEntry(String projname, String uuid) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteFolderEntry(uuid);
		}
	}
	
	/*public void addLinkEntry(String projname, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		CDCCachedFile f = getCDCCachedFile(projname);
		String parentfolderuuid = f.getMapIdTree().getData();
		if(f!=null) {
			f.addMapEntry(parentfolderuuid, codefilename, codeselpath, specfilename, specselpath, comment);
		}
	}*/
	
	public void addLinkEntry(String projname, String parentfolderuuid, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.addMapEntry(parentfolderuuid, codefilename, codeselpath, specfilename, specselpath, comment);
		}
	}
	
	public LinkEntry getLinkEntry(String projname, String uuid) {
		CDCCachedFile f = getCDCCachedFile(projname);
		MapEntry entry = null;
		if(f!=null) {
			entry = f.getMapEntry(uuid);
		}
		if(entry != null) {
			return new LinkEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), f.getCodeFilename(entry.getCodefileUUID()), entry.getCodeselpath(), f.getSpecFilename(entry.getSpecfileUUID()), entry.getSpecselpath(), entry.getComment());
		}
		return null;
	}
	
	public void deleteLinkEntry(String projname, String uuid) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteMapEntry(uuid);
		}
	}
	
	public void setLastOpenedCodeFilename(String projname, String codeFilename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.setLastOpenedCodeFilename(codeFilename);
		}
	}
	public String getLastOpenedCodeFilename(String projname) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			return f.getLastOpenedCodeFilename();
		} else {
			return null;
		}
	}
	
	public void setLastOpenedSpecFilename(String projname, String specFilename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.setLastOpenedSpecFilename(specFilename);
		}
	}
	public String getLastOpenedSpecFilename(String projname) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			return f.getLastOpenedSpecFilename();
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param projname
	 * @param filter
	 * @return The list of uuids of maps.
	 */
	public EntryNode getLinkTable(String projname, MapSelectionFilter filter) {
		invisibleroot.clearChildren();
		if(projname==null) {
			return invisibleroot;
		}
		CDCCachedFile f = getCDCCachedFile(projname);
		Stack<FolderMapTreeNode> stack = new Stack<FolderMapTreeNode>();
		stack.push(f.getMapIdTree());
		while(!stack.empty()) {
			FolderMapTreeNode node = stack.pop();
			String uuid = node.getData();
			if(getCategoryEntry(projname, uuid) != null) {
				// this is a folder
				FolderMapTreeNode[] children = node.getChildren();
				for(int i=children.length-1; i>=0; i--) {
					stack.push(children[i]);
				}
			} else {
				// this is a map
				MapEntry entry = f.getMapEntry(uuid);
				EntryNode en = new EntryNode(new LinkEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), f.getCodeFilename(entry.getCodefileUUID()), entry.getCodeselpath(), f.getSpecFilename(entry.getSpecfileUUID()), entry.getSpecselpath(), entry.getComment()));
				invisibleroot.addChildZ(en);
				en.setParent(invisibleroot);
			}
		}
		return invisibleroot;
	}
	
	public EntryNode getLinkTree(String projname, MapSelectionFilter filter) {
		invisibleroot.clearChildren();
		if(projname==null) {
			return null;
		}
		CDCCachedFile f = getCDCCachedFile(projname);
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
			if(getCategoryEntry(projname, uuid) != null) {
				// this is a folder
				FolderMapTreeNode[] children = node.getChildren();
				for(int i=children.length-1; i>=0; i--) {
					stack.push(children[i]);
					uuid = children[i].getData();
					entry = f.getFolderEntry(uuid);
					if(entry != null) {
						childdes = new EntryNode(new CategoryEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), ((FolderEntry) entry).getFoldername(), ((FolderEntry) entry).getFolderpath()));
					} else {
						entry = f.getMapEntry(uuid);
						childdes = new EntryNode(new LinkEntry(uuid, entry.getTime(), entry.getOS(), entry.getCreater(), f.getCodeFilename(((MapEntry) entry).getCodefileUUID()), ((MapEntry) entry).getCodeselpath(), f.getSpecFilename(((MapEntry) entry).getSpecfileUUID()), ((MapEntry) entry).getSpecselpath(), ((MapEntry) entry).getComment()));
					}
					stackdes.push(childdes);
					parentdes.addChildA(childdes);
					childdes.setParent(parentdes);
				}
			}
		}
		return invisibleroot;
	}
}
