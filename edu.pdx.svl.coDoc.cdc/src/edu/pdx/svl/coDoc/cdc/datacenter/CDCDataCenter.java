package edu.pdx.svl.coDoc.cdc.datacenter;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import edu.pdx.svl.coDoc.cdc.editor.CDCEditor;


public class CDCDataCenter {
	private static CDCDataCenter instance = null;
	
	private Hashtable<String, CDCCachedFile> projects;

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
	
	public void deleteCodeFileEntry(String projname, String filename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteCodeFileEntry(filename);
		}
	}
	
	public void addSpecFileEntry(String projname, String filename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.addSpecFileEntry(filename);
		}
	}
	
	public void deleteSpecFileEntry(String projname, String filename) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteSpecFileEntry(filename);
		}
	}
	
	public void addMapEntry(String projname, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.addMapEntry(codefilename, codeselpath, specfilename, specselpath, comment);
		}
	}
	
	public void deleteMapEntry(String projname, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			f.deleteMapEntry(codefilename, codeselpath, specfilename, specselpath, comment);
		}
	}
	
	public Vector<MapEntry> getAllMapEntries(String projname) {
		CDCCachedFile f = getCDCCachedFile(projname);
		if(f!=null) {
			return f.getMapEntries();
		} else {
			return null;
		}
	}
	
	public Vector<MapEntry> getMapEntries(String projname, MapSelectionFilter filter) {
		Vector<MapEntry> entry = new Vector<MapEntry>();
		for(MapEntry me : getCDCCachedFile(projname).getMapEntries()) {
			switch(filter.getSelector()) {
			case MapSelectionFilter.CODEFILE:
				if(me.getCodefilename().equals(filter.getCodeFileName())) {
					entry.add(me);
				}
				break;
			case MapSelectionFilter.PDFFILE:
				if(me.getSpecfilename().equals(filter.getPdfFileName())) {
					entry.add(me);
				}
				break;
			case MapSelectionFilter.PDFFILEPAGE:
				break;
			}
		}
		return entry;
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
}
