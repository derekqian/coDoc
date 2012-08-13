/** 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 
 * @author derek
 *
 */
class HeadV2 {
	@Element
	private String filetype=" ";
	@Element
	private String creater=" ";
	@Element
	private String createtime=" ";
	@Element
	private String os=" ";
	
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setOS(String os) {
		this.os = os;
	}
	public String getOS() {
		return os;
	}
}

class CodeSpecFilesV2 {
	@ElementMap
	private Map<String, String> files = new HashMap<String, String>();
	
	public String getFilename(String uuid) {
		return files.get(uuid);
	}
	public String getFileEntryId(String filename) {
		Set<String> uuids = files.keySet();
		Iterator it = uuids.iterator();
		while(it.hasNext()) {
			String uuid = (String) it.next();
			if(files.get(uuid).equals(filename)) {
				return uuid;
			}
		}
		return null;
	}
	public String addFileEntry(String filename) {
		String uuid = getFileEntryId(filename);
		if(null == uuid) {
			uuid = UUID.randomUUID().toString();
			files.put(uuid, filename);
		}
		return uuid;
	}
	public void deleteFileEntry(String uuid) {
		files.remove(uuid);
	}
}

class CodeSelectionV2 {
	@Element
	private String syntaxcodepath=" ";
	@Element
	private String selcodepath=" ";
	@Element
	private String syntaxcodetext=" ";
	@Element
	private String selcodetext=" ";
	
	public void setSyntaxCodePath(String syntaxcodepath) {
		if(syntaxcodepath != null) {
			this.syntaxcodepath = syntaxcodepath;			
		}
	}
	public String getSyntaxCodePath() {
		return syntaxcodepath.equals(" ")?null:syntaxcodepath;
	}
	public void setSelCodePath(String selcodepath) {
		if(selcodepath != null) {
			this.selcodepath = selcodepath;			
		}
	}
	public String getSelCodePath() {
		return selcodepath.equals(" ")?null:selcodepath;
	}
	public void setSyntaxCodeText(String codetext) {
		if(codetext != null) {
			this.syntaxcodetext = codetext;			
		}
	}
	public String getSyntaxCodeText() {
		return syntaxcodetext.equals(" ")?null:syntaxcodetext;
	}
	public void setSelCodeText(String codetext) {
		if(codetext != null) {
			this.selcodetext = codetext;			
		}
	}
	public String getSelCodeText() {
		return selcodetext.equals(" ")?null:selcodetext;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof CodeSelectionV2) {
			CodeSelectionV2 sel = (CodeSelectionV2) o;
			boolean b1 = (sel.getSelCodePath() == null)?selcodepath.equals(" "):selcodepath.equals(sel.getSelCodePath());
			boolean b2 = (sel.getSelCodeText() == null)?selcodetext.equals(" "):selcodetext.equals(sel.getSelCodeText());
			boolean b3 = (sel.getSyntaxCodePath() == null)?syntaxcodepath.equals(" "):syntaxcodepath.equals(sel.getSyntaxCodePath());
			boolean b4 = (sel.getSyntaxCodeText() == null)?syntaxcodetext.equals(" "):syntaxcodetext.equals(sel.getSyntaxCodeText());
			return b1 && b2 && b3 && b4;
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return syntaxcodetext+"/"+syntaxcodepath;
	}
	public CodeSelection toCodeSelection() {
		CodeSelection temp = new CodeSelection();
		temp.setSyntaxCodePath(syntaxcodepath);
		temp.setSelCodePath(selcodepath);
		temp.setSyntaxCodeText(syntaxcodetext);
		temp.setSelCodeText(selcodetext);
		return temp;
	}
}

class SpecSelectionV2 {
	@Element
	private int page;
	@Element
	private int top;
	@Element
	private int bottom;
	@Element
	private int left;
	@Element
	private int right;
	@Element
	private String pdftext=" ";
	
	public void setPage(int page) {
		this.page = page;
	}
	public int getPage() {
		return page;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getTop() {
		return top;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getBottom() {
		return bottom;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getLeft() {
		return left;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getRight() {
		return right;
	}
	public void setPDFText(String pdftext) {
		this.pdftext = pdftext;
	}
	public String getPDFText() {
		return pdftext;
	}
	public String toString() {
		return page+"/"+left+"/"+top+"/"+right+"/"+bottom+"/"+pdftext;
	}
	public SpecSelection toSpecSelection() {
		SpecSelection temp = new SpecSelection();
		temp.addBottom(bottom);
		temp.addLeft(left);
		temp.addPage(page);
		temp.addPDFText(pdftext);
		temp.addRight(right);
		temp.addTop(top);
		return temp;
	}
}

class FolderMapEntryV2 {
	@Element
	private String time=" ";
	@Element
	private String os=" ";
	@Element
	private String creater=" ";
	
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	public void setOS(String os) {
		this.os = os;
	}
	public String getOS() {
		return os;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreater() {
		return creater;
	}
}

class FolderEntryV2 extends FolderMapEntryV2 {
	@Element
	private String folderpath=" ";
	
	public void setFolderpath(String folderpath) {
		if(folderpath != null) {
			this.folderpath = folderpath;			
		}
	}
	public String getFolderpath() {
		return (" "==folderpath)?null:folderpath;
	}
	public String getFoldername() {
		String name = getFolderpath();
		if(name != null) {
			name = name.substring(name.lastIndexOf('/')+1);
			name = (name.equals(""))?"/":name;
		}
		return name;
	}
}

class FoldersV2 {
	@ElementMap
	private Map<String, FolderEntryV2> folders = new HashMap<String, FolderEntryV2>();
	
	public FolderEntryV2 getFolderEntry(String uuid) {
		return folders.get(uuid);
	}
	public String getFolderEntryId(String folderpath) {
		Set<String> uuids = folders.keySet();
		Iterator it = uuids.iterator();
		while(it.hasNext()) {
			String uuid = (String) it.next();
			if(folders.get(uuid).getFolderpath().equals(folderpath)) {
				return uuid;
			}
		}
		return null;
	}
	public Set<String> getAllFolderEntryId() {
		return folders.keySet();
	}
	public String addFolderEntry(String time, String os, String creater, String folderpath) {
		String uuid = getFolderEntryId(folderpath);
		if(null == uuid) {
			uuid = UUID.randomUUID().toString();
			FolderEntryV2 entry = new FolderEntryV2();
			entry.setTime(time);
			entry.setOS(os);
			entry.setCreater(creater);
			entry.setFolderpath(folderpath);
			folders.put(uuid, entry);
		}
		return uuid;
	}
	public void deleteFolderEntry(String uuid) {
		folders.remove(uuid);
	}
	public boolean isEmpty() {
		return folders.isEmpty();
	}
}

class MapEntryV2 extends FolderMapEntryV2 {
	@Element
	private String codefileuuid=" ";
	@Element
	private CodeSelectionV2 codeselpath = new CodeSelectionV2();
	@Element
	private String specfileuuid=" ";
	@Element
	private SpecSelectionV2 specselpath = new SpecSelectionV2();
	@Element
	private String comment=" ";
	
	public void setCodefileUUID(String codefileuuid) {
		this.codefileuuid = codefileuuid;
	}
	public String getCodefileUUID() {
		return codefileuuid;
	}
	public void setCodeselpath(CodeSelectionV2 codeselpath) {
		this.codeselpath = codeselpath;
	}
	public CodeSelectionV2 getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfileUUID(String specfileuuid) {
		this.specfileuuid = specfileuuid;
	}
	public String getSpecfileUUID() {
		return specfileuuid;
	}
	public void setSpecselpath(SpecSelectionV2 specselpath) {
		this.specselpath = specselpath;
	}
	public SpecSelectionV2 getSpecselpath() {
		return specselpath;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
}

class MapsV2 {
	@ElementMap
	private Map<String, MapEntryV2> maplist = new HashMap<String, MapEntryV2>();
	
	public MapEntryV2 getMapEntry(String uuid) {
		return maplist.get(uuid);
	}
	public String getMapEntryId(String codefileuuid, CodeSelectionV2 codeselpath, String specfileuuid, SpecSelectionV2 specselpath, String comment) {
		Set<String> uuids = maplist.keySet();
		Iterator it = uuids.iterator();
		while(it.hasNext()) {
			String uuid = (String) it.next();
			MapEntryV2 entry = maplist.get(uuid);
			if(codefileuuid.equals(entry.getCodefileUUID()) && codeselpath.equals(entry.getCodeselpath())
					                                        && specfileuuid.equals(entry.getSpecfileUUID())
					                                        && specselpath.equals(entry.getSpecselpath())
					                                        && comment.equals(entry.getComment())) {
				return uuid;
			}
		}
		return null;
	}
	public Set<String> getAllMapEntryId() {
		return maplist.keySet();
	}
	public String addMapEntry(String time, String os, String creater, String codefileuuid, CodeSelectionV2 codeselpath, String specfileuuid, SpecSelectionV2 specselpath, String comment) {
		String uuid = getMapEntryId(codefileuuid, codeselpath, specfileuuid, specselpath, comment);
		if(null == uuid) {
			uuid = UUID.randomUUID().toString();
			MapEntryV2 entry = new MapEntryV2();
			entry.setTime(time);
			entry.setOS(os);
			entry.setCreater(creater);
			entry.setCodefileUUID(codefileuuid);
			entry.setCodeselpath(codeselpath);
			entry.setSpecfileUUID(specfileuuid);
			entry.setSpecselpath(specselpath);
			entry.setComment(comment);
			maplist.put(uuid, entry);
		}
		return uuid;
	}
	public void deleteMapEntry(String uuid) {
		maplist.remove(uuid);
	}
}

class RelationEntryV2 {
	@Element
	private String uuid = " ";
	@Element
	private String parentuuid = " ";
	
	public void setUUID(String uuid) {
		if(uuid != null) {
			this.uuid = uuid;
		}
	}
	public String getUUID() {
		return (uuid.equals(" "))?null:uuid;
	}
	public void setParentUUID(String parentuuid) {
		if(parentuuid != null) {
			this.parentuuid = parentuuid;			
		}
	}
	public String getParentUUID() {
		return (parentuuid.equals(" "))?null:parentuuid;
	}
}

class RelationsV2 {
	@ElementList
	private Vector<RelationEntryV2> relations = new Vector<RelationEntryV2>();
	
	public Vector<RelationEntryV2> getAllRelationEntries() {
		return relations;
	}
	public RelationEntryV2 getRelationEntry(String uuid) {
		for(RelationEntryV2 entry : relations) {
			if(entry.getUUID().equals(uuid)) {
				return entry;
			}
		}
		return null;
	}
	public void addRelation(String uuid, String parentuuid) {
		if(null == getRelationEntry(uuid)) {
			RelationEntryV2 entry = new RelationEntryV2();
			entry.setUUID(uuid);
			entry.setParentUUID(parentuuid);
			relations.add(entry);			
		}
	}
	public void removeRelation(String uuid) {
		RelationEntryV2 entry = getRelationEntry(uuid);
		if(null != entry) {
			relations.removeElement(entry);
		}
	}
}

class BodyV2 {
	@Element
	public CodeSpecFilesV2 codefiles = null;
	@Element
	public CodeSpecFilesV2 specfiles = null;
	@Element
	public FoldersV2 folders = null;
	@Element
	public MapsV2 maps = null;
	@Element
	public RelationsV2 relations = null;
	
	public BodyV2() {
		codefiles = new CodeSpecFilesV2();
		specfiles = new CodeSpecFilesV2();
		folders = new FoldersV2();
		maps = new MapsV2();
		relations = new RelationsV2();
	}
}

class OpEntryV2 {
	@Element
	private String operation=" ";
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
}

class LastOpenedV2 {
	@Element
	private String codeFileuuid=" ";
	@Element
	private String specFileuuid=" ";
	
	public void setCodeFileuuid(String codeFileuuid) {
		this.codeFileuuid = (codeFileuuid==null)?" ":codeFileuuid;
	}
	public String getCodeFileuuid() {
		return codeFileuuid.equals(" ")?null:codeFileuuid;
	}
	
	public void setSpecFileuuid(String specFileuuid) {
		this.specFileuuid = (specFileuuid==null)?" ":specFileuuid;
	}
	public String getSpecFileuuid() {
		return specFileuuid.equals(" ")?null:specFileuuid;
	}
}
class HistoryV2 {
	@Element
	private LastOpenedV2 lastOpened = new LastOpenedV2();
	@ElementList
	private Vector<OpEntryV2> operations = new Vector<OpEntryV2>();
	
	public void setLastOpenedCodeFileuuid(String codeFileuuid) {
		lastOpened.setCodeFileuuid(codeFileuuid);
	}
	public String getLastOpenedCodeFileuuid() {
		return lastOpened.getCodeFileuuid();
	}
	
	public void setLastOpenedSpecFileuuid(String specFileuuid) {
		lastOpened.setSpecFileuuid(specFileuuid);
	}
	public String getLastOpenedSpecFileuuid() {
		return lastOpened.getSpecFileuuid();
	}
	
	public void addOperation(String op) {
		OpEntryV2 opentry = new OpEntryV2();
		opentry.setOperation(op);
		operations.add(opentry);
	}
	
	public void deleteOperation(String op) {
		for(OpEntryV2 entry : operations) {
			if(entry.getOperation().equals(op)) {
				operations.removeElement(entry);
				break;
			}
		}
	}
	
	public Vector<OpEntryV2> getOperationList() {
		return operations;
	}
}

class FolderMapTreeNodeV2 {
	private String uuid = null;
	private FolderMapTreeNodeV2 parent = null;
	private Vector<FolderMapTreeNodeV2> children = new Vector<FolderMapTreeNodeV2>();
	
	public FolderMapTreeNodeV2(String uuid) {
		this.uuid = uuid;
	}
	public String getData() {
		return uuid;
	}
	public void setData(String uuid) {
		this.uuid = uuid;
	}
	public void setParent(FolderMapTreeNodeV2 parent) {
		this.parent = parent;
	}
	public FolderMapTreeNodeV2 getParent() {
		return parent;
	}
	public void addChild(FolderMapTreeNodeV2 child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(FolderMapTreeNodeV2 child) {
		children.remove(child);
	}
	public boolean hasChildren() {
		return (!children.isEmpty());
	}
	public FolderMapTreeNodeV2[] getChildren() {
		FolderMapTreeNodeV2[] temp = new FolderMapTreeNodeV2[children.size()];
		System.arraycopy(children.toArray(), 0, temp, 0, children.size());
		return temp;
	}
}

@Root
public class CDCModelV2 {
	@Element
	private HeadV2 head;
	@Element
	private BodyV2 body;
	@Element
	private HistoryV2 hist;
	
	private Date date;
	private SimpleDateFormat ft = null;
	
	private FolderMapTreeNodeV2 root = null;
	
	public CDCModelV2() {
		Properties props=System.getProperties();
		ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS zzz");
		date = new Date();
		String time = ft.format(date);
		String os = props.getProperty("os.name");
		String creater = props.getProperty("user.name");
		head = new HeadV2();
		head.setFiletype("CDC");
		head.setCreater(creater);
		head.setOS(os);
		head.setCreatetime(time);
		body = new BodyV2();
		hist = new HistoryV2();
		
		if(body.folders.isEmpty()) {
			body.folders.addFolderEntry(time, os, creater, "/");
		}
	}
	
	public void addCodeFileEntry(String filename) {
		if(body.codefiles.getFileEntryId(filename) == null) {
			Properties props=System.getProperties();
			String uuid = body.codefiles.addFileEntry(filename);
			date = new Date();
			hist.addOperation(ft.format(date)+"#"+props.getProperty("os.name")+"#"+props.getProperty("user.name")+"#add#codefileentry#"+uuid);			
		}
	}
	
	public String getCodeFilename(String uuid) {
		return body.codefiles.getFilename(uuid);
	}
	
	public void deleteCodeFileEntry(String uuid) {
		Properties props=System.getProperties();
		if(uuid == null) return;
		body.codefiles.deleteFileEntry(uuid);
		date = new Date();
		hist.addOperation(ft.format(date)+"#"+props.getProperty("os.name")+"#"+props.getProperty("user.name")+"#del#codefileentry#"+uuid);
	}
	
	public void addSpecFileEntry(String filename) {
		if(body.specfiles.getFileEntryId(filename) == null) {
			Properties props=System.getProperties();
			String uuid = body.specfiles.addFileEntry(filename);
			date = new Date();
			hist.addOperation(ft.format(date)+"#"+props.getProperty("os.name")+"#"+props.getProperty("user.name")+"#add#specfileentry#"+uuid);			
		}
	}
	
	public String getSpecFilename(String uuid) {
		return body.specfiles.getFilename(uuid);
	}
	
	public void deleteSpecFileEntry(String uuid) {
		Properties props=System.getProperties();
		if(uuid == null) return;
		body.specfiles.deleteFileEntry(uuid);
		date = new Date();
		hist.addOperation(ft.format(date)+"#"+props.getProperty("os.name")+"#"+props.getProperty("user.name")+"#del#specfileentry#"+uuid);
	}
	
	public FolderMapTreeNodeV2 getMapIdTree() {
		Map<String, FolderMapTreeNodeV2> created = new HashMap<String, FolderMapTreeNodeV2>();
		FolderMapTreeNodeV2 child = null;
		FolderMapTreeNodeV2 parent = null;
		if(root == null) {
			String rootuuid = body.folders.getFolderEntryId("/");
			root = new FolderMapTreeNodeV2(rootuuid);
			created.put(rootuuid, root);
			// only for edges, so rootuuid can't be in uuid field.
			for(RelationEntryV2 entry : body.relations.getAllRelationEntries()) {
				String uuid = entry.getUUID();
				String parentuuid = entry.getParentUUID();
				if(created.containsKey(uuid)) {
					child = created.get(uuid);
				} else {
					child = new FolderMapTreeNodeV2(uuid);
					created.put(uuid, child);
				}
				if(created.containsKey(parentuuid)) {
					parent = created.get(parentuuid);
				} else {
					parent = new FolderMapTreeNodeV2(parentuuid);
					created.put(parentuuid, parent);
				}
				child.setParent(parent);
				parent.addChild(child);					
			}
		}
		return root;
	}
	
	private FolderMapTreeNodeV2 getMapIdTreeNode(String uuid) {
		Stack<FolderMapTreeNodeV2> stack = new Stack<FolderMapTreeNodeV2>();
		if(root == null) {
			getMapIdTree();
		}
		stack.push(root);
		while(!stack.empty()) {
			FolderMapTreeNodeV2 node = stack.pop();
			if(node.getData().equals(uuid)) {
				return node;
			}
			FolderMapTreeNodeV2[] children = node.getChildren();
			for(FolderMapTreeNodeV2 n : children) {
				stack.push(n);
			}
		}
		return null;
	}
	
	public void addFolderEntry(String parentfolderuuid, String foldername) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		addFolderEntry(time, os, creater, parentfolderuuid, foldername);
	}
	public void addFolderEntry(String time, String os, String creater, String parentfolderuuid, String foldername) {
		FolderEntryV2 folderentry = body.folders.getFolderEntry(parentfolderuuid);
		String folderpath = folderentry.getFolderpath();
		if(!folderentry.getFoldername().equals("/")) {
			folderpath += "/";
		}
		folderpath += foldername;
		String uuid = body.folders.addFolderEntry(time, os, creater, folderpath);
		FolderMapTreeNodeV2 parent = getMapIdTreeNode(parentfolderuuid);
		FolderMapTreeNodeV2 child = new FolderMapTreeNodeV2(uuid);
		child.setParent(parent);
		parent.addChild(child);
		body.relations.addRelation(uuid, parentfolderuuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#add#folderentry#"+uuid+"#"+parentfolderuuid);
	}
	
	public FolderEntryV2 getFolderEntry(String uuid) {
		return body.folders.getFolderEntry(uuid);
	}
	
	public void deleteFolderEntry(String uuid) {
		Properties props=System.getProperties();
		date = new Date();
		deleteFolderEntry(ft.format(date), props.getProperty("os.name"), props.getProperty("user.name"), uuid);
	}
	public void deleteFolderEntry(String time, String os, String creater, String uuid) {
		String foldername = body.folders.getFolderEntry(uuid).getFoldername();
		if(foldername.equals("/")) {
			return;
		}
		FolderMapTreeNodeV2 child = getMapIdTreeNode(uuid);
		if(child.hasChildren()) {
			return;
		}
		String parentfolderuuid = body.relations.getRelationEntry(uuid).getParentUUID();
		FolderMapTreeNodeV2 parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		body.relations.removeRelation(uuid);
		// body.folders.deleteFolderEntry(uuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#del#folderentry#"+uuid);
	}
	
	public void addMapEntry(String parentfolderuuid, String codefilename, CodeSelectionV2 codeselpath, String specfilename, SpecSelectionV2 specselpath, String comment) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		addMapEntry(time, os, creater, parentfolderuuid, codefilename, codeselpath, specfilename, specselpath, comment);
	}
	public void addMapEntry(String time, String os, String creater, String parentfolderuuid, String codefilename, CodeSelectionV2 codeselpath, String specfilename, SpecSelectionV2 specselpath, String comment) {
		addCodeFileEntry(codefilename);
		addSpecFileEntry(specfilename);
		String codefileuuid = body.codefiles.getFileEntryId(codefilename);
		String specfileuuid = body.specfiles.getFileEntryId(specfilename);
		String uuid = body.maps.addMapEntry(time, os, creater, codefileuuid, codeselpath, specfileuuid, specselpath, comment);
		FolderMapTreeNodeV2 parent = getMapIdTreeNode(parentfolderuuid);
		FolderMapTreeNodeV2 child = new FolderMapTreeNodeV2(uuid);
		child.setParent(parent);
		parent.addChild(child);
		body.relations.addRelation(uuid, parentfolderuuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#add#mapentry#"+uuid+"#"+parentfolderuuid);
	}
	
	public void editMapEntry(String olduuid, String codefilename, CodeSelectionV2 codeselpath, String specfilename, SpecSelectionV2 specselpath, String comment) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		editMapEntry(time, os, creater, olduuid, codefilename, codeselpath, specfilename, specselpath, comment);
	}
	public void editMapEntry(String time, String os, String creater, String olduuid, String codefilename, CodeSelectionV2 codeselpath, String specfilename, SpecSelectionV2 specselpath, String comment) {
		addCodeFileEntry(codefilename);
		addSpecFileEntry(specfilename);
		String codefileuuid = body.codefiles.getFileEntryId(codefilename);
		String specfileuuid = body.specfiles.getFileEntryId(specfilename);
		String uuid = body.maps.addMapEntry(time, os, creater, codefileuuid, codeselpath, specfileuuid, specselpath, comment);		
		FolderMapTreeNodeV2 child = getMapIdTreeNode(olduuid);
		child.setData(uuid);
		RelationEntryV2 rentry = body.relations.getRelationEntry(olduuid);
		rentry.setUUID(uuid);		
		hist.addOperation(time+"#"+os+"#"+creater+"#edt#mapentry#"+olduuid+"#"+uuid);
	}
	
	public MapEntryV2 getMapEntry(String uuid) {
		return body.maps.getMapEntry(uuid);
	}
	
	public void deleteMapEntry(String uuid) {
		Properties props=System.getProperties();
		date = new Date();
		deleteMapEntry(ft.format(date), props.getProperty("os.name"), props.getProperty("user.name"), uuid);
	}
	public void deleteMapEntry(String time, String os, String creater, String uuid) {
		String parentfolderuuid = body.relations.getRelationEntry(uuid).getParentUUID();
		FolderMapTreeNodeV2 child = getMapIdTreeNode(uuid);
		FolderMapTreeNodeV2 parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		body.relations.removeRelation(uuid);
		// body.maps.deleteMapEntry(uuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#del#mapentry#"+uuid);
	}
	
	public boolean parentOf(String childuuid, String parentuuid) {
		FolderMapTreeNodeV2 child = getMapIdTreeNode(childuuid);
		FolderMapTreeNodeV2 parent = child.getParent();
		while(parent != null) {
			if(parent.getData().equals(parentuuid)) {
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}
	
	public void moveEntry(String sourceuuid, String destuuid) {
		Properties props=System.getProperties();
		date = new Date();
		moveEntry(ft.format(date), props.getProperty("os.name"), props.getProperty("user.name"), sourceuuid, destuuid);
	}
	public void moveEntry(String time, String os, String creater, String sourceuuid, String destuuid) {
		String parentfolderuuid = body.relations.getRelationEntry(sourceuuid).getParentUUID();
		if(destuuid.equals(parentfolderuuid)) {
			return;
		}
		FolderMapTreeNodeV2 child = getMapIdTreeNode(sourceuuid);
		FolderMapTreeNodeV2 parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		parent = getMapIdTreeNode(destuuid);
		child.setParent(parent);
		parent.addChild(child);
		if(body.folders.getFolderEntry(sourceuuid) != null) {
			FolderEntryV2 childfolder = body.folders.getFolderEntry(sourceuuid);
			FolderEntryV2 parentfolder = body.folders.getFolderEntry(destuuid);
			childfolder.setFolderpath(parentfolder.getFolderpath()+childfolder.getFoldername());
		}
		body.relations.removeRelation(sourceuuid);
		// body.maps.deleteMapEntry(uuid);
		body.relations.addRelation(sourceuuid, destuuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#mov#entry#"+sourceuuid+"#"+destuuid);
	}
	
	public void setLastOpenedCodeFilename(String codeFilename) {
		addCodeFileEntry(codeFilename);
		String uuid = body.codefiles.getFileEntryId(codeFilename);
		hist.setLastOpenedCodeFileuuid(uuid);
	}
	public String getLastOpenedCodeFilename() {
		String uuid = hist.getLastOpenedCodeFileuuid();
		return (uuid == null)?null:body.codefiles.getFilename(uuid);
	}
	
	public void setLastOpenedSpecFilename(String specFilename) {
		addSpecFileEntry(specFilename);
		String uuid = body.specfiles.getFileEntryId(specFilename);
		hist.setLastOpenedSpecFileuuid(uuid);
	}
	public String getLastOpenedSpecFilename() {
		String uuid = hist.getLastOpenedSpecFileuuid();
		return (uuid==null)?null:body.specfiles.getFilename(uuid);
	}
	
	public HeadV2 getHead() {
		return head;
	}
	public BodyV2 getBody() {
		return body;
	}
	public HistoryV2 getHist() {
		return hist;
	}
	public static void main(String[] args) {
		toV3();
	}
	private static void toV3() {
		CDCModelV2 cdcModelV2 = null;
		File cdcFileV2 = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver2-2.cdc");
		Serializer serializer = new Persister();
		try {
			cdcModelV2 = serializer.read(CDCModelV2.class, cdcFileV2);
		} catch (Exception e) {
			System.out.println("Unable to read cdcFileV2!");
			e.printStackTrace();
		}
		
		CDCModel cdcModel = new CDCModel();
		cdcModel.getHead().setCreater(cdcModelV2.getHead().getCreater());
		cdcModel.getHead().setCreatetime(cdcModelV2.getHead().getCreatetime());
		cdcModel.getHead().setFiletype(cdcModelV2.getHead().getFiletype());
		cdcModel.getHead().setOS(cdcModelV2.getHead().getOS());
		cdcModel.getMapIdTree();
		
		Vector<OpEntryV2> ops = cdcModelV2.getHist().getOperationList();
		Iterator it = ops.iterator();
		while(it.hasNext()) {
			OpEntryV2 op = (OpEntryV2) it.next();
			String tempstr = op.getOperation();
			Vector<String> opstr = new Vector<String>();
			while(tempstr.lastIndexOf('#') != -1) {
				int index = tempstr.lastIndexOf('#');
				opstr.add(0, tempstr.substring(index+1));
				tempstr = tempstr.substring(0,index);
			}
			opstr.add(0,tempstr);
			
			if(opstr.get(3).equals("add") && opstr.get(4).equals("mapentry")) {
				String uuid = opstr.get(5);
				String parentuuid = opstr.get(6);
				String folderPath = cdcModelV2.getFolderEntry(parentuuid).getFolderpath();
				parentuuid = cdcModel.getBody().folders.getFolderEntryId(folderPath);
				MapEntryV2 entry = cdcModelV2.getMapEntry(uuid);
				String codefilename = cdcModelV2.getCodeFilename(entry.getCodefileUUID());
				String specfilename = cdcModelV2.getSpecFilename(entry.getSpecfileUUID());
				cdcModel.addMapEntry(parentuuid, codefilename, entry.getCodeselpath().toCodeSelection(), specfilename, entry.getSpecselpath().toSpecSelection(), entry.getComment());
			} else if(opstr.get(3).equals("edt") && opstr.get(4).equals("mapentry")) {
				String olduuid = opstr.get(5);
				String uuid = opstr.get(6);
				MapEntryV2 oldentry = cdcModelV2.getMapEntry(olduuid);
				String oldcodefilename = cdcModelV2.getBody().codefiles.getFilename(oldentry.getCodefileUUID());
				String oldcodefileuuid = cdcModel.getBody().codefiles.getFileEntryId(oldcodefilename);
				CodeSelectionV2 codeselpath = oldentry.getCodeselpath();
				String oldspecfilename = cdcModelV2.getBody().specfiles.getFilename(oldentry.getSpecfileUUID());
				String oldspecfileuuid = cdcModel.getBody().specfiles.getFileEntryId(oldspecfilename);
				SpecSelectionV2 specselpath = oldentry.getSpecselpath();
				String comment = oldentry.getComment();
				olduuid = cdcModel.getBody().maps.getMapEntryId(oldcodefileuuid, codeselpath.toCodeSelection(), oldspecfileuuid, specselpath.toSpecSelection(), comment);
				MapEntryV2 entry = cdcModelV2.getMapEntry(uuid);
				String codefilename = cdcModelV2.getCodeFilename(entry.getCodefileUUID());
				String specfilename = cdcModelV2.getSpecFilename(entry.getSpecfileUUID());
				cdcModel.editMapEntry(olduuid, codefilename, entry.getCodeselpath().toCodeSelection(), specfilename, entry.getSpecselpath().toSpecSelection(), entry.getComment());
			} else if(opstr.get(3).equals("add") && opstr.get(4).equals("codefileentry")) {
			} else if(opstr.get(3).equals("add") && opstr.get(4).equals("specfileentry")) {
			} else {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "Unsupported operation:\n"+op.getOperation());
				return;
			}
		}

		cdcModel.setLastOpenedCodeFilename(cdcModelV2.getLastOpenedCodeFilename());
		cdcModel.setLastOpenedSpecFilename(cdcModelV2.getLastOpenedSpecFilename());
		File cdcFile = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver3.cdc");
		try {
			serializer.write(cdcModel, cdcFile);
		} catch (Exception e) {
			System.out.println("Unable to write cdcFile!");
			e.printStackTrace();
		}		
	}
}
