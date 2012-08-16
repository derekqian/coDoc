/** 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * 
 * @author derek
 *
 */
class Head {
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
	public void setCreater(String creater) {
		this.creater = creater;
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

class CodeSpecFiles {
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

class FolderMapEntry {
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

class FolderEntry extends FolderMapEntry {
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

class Folders {
	@ElementMap
	private Map<String, FolderEntry> folders = new HashMap<String, FolderEntry>();
	
	public FolderEntry getFolderEntry(String uuid) {
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
			FolderEntry entry = new FolderEntry();
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

class MapEntry extends FolderMapEntry {
	@Element
	private String codefileuuid=" ";
	@Element
	private CodeSelection codeselpath = new CodeSelection();
	@Element
	private String specfileuuid=" ";
	@Element
	private SpecSelection specselpath = new SpecSelection();
	@Element
	private String comment=" ";
	
	public void setCodefileUUID(String codefileuuid) {
		this.codefileuuid = codefileuuid;
	}
	public String getCodefileUUID() {
		return codefileuuid;
	}
	public void setCodeselpath(CodeSelection codeselpath) {
		this.codeselpath = codeselpath;
	}
	public CodeSelection getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfileUUID(String specfileuuid) {
		this.specfileuuid = specfileuuid;
	}
	public String getSpecfileUUID() {
		return specfileuuid;
	}
	public void setSpecselpath(SpecSelection specselpath) {
		this.specselpath = specselpath;
	}
	public SpecSelection getSpecselpath() {
		return specselpath;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
}

class Maps {
	@ElementMap
	private Map<String, MapEntry> maplist = new HashMap<String, MapEntry>();
	
	public MapEntry getMapEntry(String uuid) {
		return maplist.get(uuid);
	}
	public String getMapEntryId(String codefileuuid, CodeSelection codeselpath, String specfileuuid, SpecSelection specselpath, String comment) {
		Set<String> uuids = maplist.keySet();
		Iterator it = uuids.iterator();
		while(it.hasNext()) {
			String uuid = (String) it.next();
			MapEntry entry = maplist.get(uuid);
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
	public String addMapEntry(String time, String os, String creater, String codefileuuid, CodeSelection codeselpath, String specfileuuid, SpecSelection specselpath, String comment) {
		String uuid = getMapEntryId(codefileuuid, codeselpath, specfileuuid, specselpath, comment);
		if(null == uuid) {
			uuid = UUID.randomUUID().toString();
			MapEntry entry = new MapEntry();
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

class RelationEntry {
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

class Relations {
	@ElementList
	private Vector<RelationEntry> relations = new Vector<RelationEntry>();
	
	public Vector<RelationEntry> getAllRelationEntries() {
		return relations;
	}
	public RelationEntry getRelationEntry(String uuid) {
		for(RelationEntry entry : relations) {
			if(entry.getUUID().equals(uuid)) {
				return entry;
			}
		}
		return null;
	}
	public void addRelation(String uuid, String parentuuid) {
		if(null == getRelationEntry(uuid)) {
			RelationEntry entry = new RelationEntry();
			entry.setUUID(uuid);
			entry.setParentUUID(parentuuid);
			relations.add(entry);			
		}
	}
	public void removeRelation(String uuid) {
		RelationEntry entry = getRelationEntry(uuid);
		if(null != entry) {
			relations.removeElement(entry);
		}
	}
}

class Body {
	@Element
	public CodeSpecFiles codefiles = null;
	@Element
	public CodeSpecFiles specfiles = null;
	@Element
	public Folders folders = null;
	@Element
	public Maps maps = null;
	@Element
	public Relations relations = null;
	
	public Body() {
		codefiles = new CodeSpecFiles();
		specfiles = new CodeSpecFiles();
		folders = new Folders();
		maps = new Maps();
		relations = new Relations();
	}
}

class OpEntry {
	@Element
	private String operation=" ";
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
}

class LastOpened {
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
class History {
	@Element
	private LastOpened lastOpened = new LastOpened();
	@ElementList
	private Vector<OpEntry> operations = new Vector<OpEntry>();
	
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
		OpEntry opentry = new OpEntry();
		opentry.setOperation(op);
		operations.add(opentry);
	}
	
	public void deleteOperation(String op) {
		for(OpEntry entry : operations) {
			if(entry.getOperation().equals(op)) {
				operations.removeElement(entry);
				break;
			}
		}
	}
	
	public Vector<OpEntry> getOperationList() {
		return operations;
	}
}

class FolderMapTreeNode {
	private String uuid = null;
	private FolderMapTreeNode parent = null;
	private Vector<FolderMapTreeNode> children = new Vector<FolderMapTreeNode>();
	
	public FolderMapTreeNode(String uuid) {
		this.uuid = uuid;
	}
	public String getData() {
		return uuid;
	}
	public void setData(String uuid) {
		this.uuid = uuid;
	}
	public void setParent(FolderMapTreeNode parent) {
		this.parent = parent;
	}
	public FolderMapTreeNode getParent() {
		return parent;
	}
	public void addChild(FolderMapTreeNode child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(FolderMapTreeNode child) {
		children.remove(child);
	}
	public boolean hasChildren() {
		return (!children.isEmpty());
	}
	public FolderMapTreeNode[] getChildren() {
		FolderMapTreeNode[] temp = new FolderMapTreeNode[children.size()];
		System.arraycopy(children.toArray(), 0, temp, 0, children.size());
		return temp;
	}
}

@Root
public class CDCModel {
	@Element
	private Head head;
	@Element
	private Body body;
	@Element
	private History hist;
	
	private Date date;
	private SimpleDateFormat ft = null;
	
	private FolderMapTreeNode root = null;
	
	public CDCModel() {
		Properties props=System.getProperties();
		ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS zzz");
		date = new Date();
		String time = ft.format(date);
		String os = props.getProperty("os.name");
		String creater = props.getProperty("user.name");
		head = new Head();
		head.setFiletype("CDC");
		head.setCreater(creater);
		head.setOS(os);
		head.setCreatetime(time);
		body = new Body();
		hist = new History();
		
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
	
	public FolderMapTreeNode getMapIdTree() {
		Map<String, FolderMapTreeNode> created = new HashMap<String, FolderMapTreeNode>();
		FolderMapTreeNode child = null;
		FolderMapTreeNode parent = null;
		if(root == null) {
			String rootuuid = body.folders.getFolderEntryId("/");
			root = new FolderMapTreeNode(rootuuid);
			created.put(rootuuid, root);
			// only for edges, so rootuuid can't be in uuid field.
			for(RelationEntry entry : body.relations.getAllRelationEntries()) {
				String uuid = entry.getUUID();
				String parentuuid = entry.getParentUUID();
				if(created.containsKey(uuid)) {
					child = created.get(uuid);
				} else {
					child = new FolderMapTreeNode(uuid);
					created.put(uuid, child);
				}
				if(created.containsKey(parentuuid)) {
					parent = created.get(parentuuid);
				} else {
					parent = new FolderMapTreeNode(parentuuid);
					created.put(parentuuid, parent);
				}
				child.setParent(parent);
				parent.addChild(child);					
			}
		}
		return root;
	}
	
	private FolderMapTreeNode getMapIdTreeNode(String uuid) {
		Stack<FolderMapTreeNode> stack = new Stack<FolderMapTreeNode>();
		if(root == null) {
			getMapIdTree();
		}
		stack.push(root);
		while(!stack.empty()) {
			FolderMapTreeNode node = stack.pop();
			if(node.getData().equals(uuid)) {
				return node;
			}
			FolderMapTreeNode[] children = node.getChildren();
			for(FolderMapTreeNode n : children) {
				stack.push(n);
			}
		}
		return null;
	}
	
	public String addFolderEntry(String parentfolderuuid, String foldername) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		return addFolderEntry(time, os, creater, parentfolderuuid, foldername);
	}
	public String addFolderEntry(String time, String os, String creater, String parentfolderuuid, String foldername) {
		FolderEntry folderentry = body.folders.getFolderEntry(parentfolderuuid);
		String folderpath = folderentry.getFolderpath();
		if(!folderentry.getFoldername().equals("/")) {
			folderpath += "/";
		}
		folderpath += foldername;
		String uuid = body.folders.addFolderEntry(time, os, creater, folderpath);
		FolderMapTreeNode parent = getMapIdTreeNode(parentfolderuuid);
		FolderMapTreeNode child = new FolderMapTreeNode(uuid);
		child.setParent(parent);
		parent.addChild(child);
		body.relations.addRelation(uuid, parentfolderuuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#add#folderentry#"+uuid+"#"+parentfolderuuid);
		return uuid;
	}
	
	public FolderEntry getFolderEntry(String uuid) {
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
		FolderMapTreeNode child = getMapIdTreeNode(uuid);
		if(child.hasChildren()) {
			return;
		}
		String parentfolderuuid = body.relations.getRelationEntry(uuid).getParentUUID();
		FolderMapTreeNode parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		body.relations.removeRelation(uuid);
		// body.folders.deleteFolderEntry(uuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#del#folderentry#"+uuid);
	}
	
	public String addMapEntry(String parentfolderuuid, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		return addMapEntry(time, os, creater, parentfolderuuid, codefilename, codeselpath, specfilename, specselpath, comment);
	}
	public String addMapEntry(String time, String os, String creater, String parentfolderuuid, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		addCodeFileEntry(codefilename);
		addSpecFileEntry(specfilename);
		String codefileuuid = body.codefiles.getFileEntryId(codefilename);
		String specfileuuid = body.specfiles.getFileEntryId(specfilename);
		String uuid = body.maps.addMapEntry(time, os, creater, codefileuuid, codeselpath, specfileuuid, specselpath, comment);
		FolderMapTreeNode parent = getMapIdTreeNode(parentfolderuuid);
		FolderMapTreeNode child = new FolderMapTreeNode(uuid);
		child.setParent(parent);
		parent.addChild(child);
		body.relations.addRelation(uuid, parentfolderuuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#add#mapentry#"+uuid+"#"+parentfolderuuid);
		return uuid;
	}
	
	public void editMapEntry(String olduuid, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		editMapEntry(time, os, creater, olduuid, codefilename, codeselpath, specfilename, specselpath, comment);
	}
	public void editMapEntry(String time, String os, String creater, String olduuid, String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		addCodeFileEntry(codefilename);
		addSpecFileEntry(specfilename);
		String codefileuuid = body.codefiles.getFileEntryId(codefilename);
		String specfileuuid = body.specfiles.getFileEntryId(specfilename);
		String uuid = body.maps.addMapEntry(time, os, creater, codefileuuid, codeselpath, specfileuuid, specselpath, comment);		
		FolderMapTreeNode child = getMapIdTreeNode(olduuid);
		child.setData(uuid);
		RelationEntry rentry = body.relations.getRelationEntry(olduuid);
		rentry.setUUID(uuid);		
		hist.addOperation(time+"#"+os+"#"+creater+"#edt#mapentry#"+olduuid+"#"+uuid);
	}
	
	public MapEntry getMapEntry(String uuid) {
		return body.maps.getMapEntry(uuid);
	}
	
	public void deleteMapEntry(String uuid) {
		Properties props=System.getProperties();
		date = new Date();
		deleteMapEntry(ft.format(date), props.getProperty("os.name"), props.getProperty("user.name"), uuid);
	}
	public void deleteMapEntry(String time, String os, String creater, String uuid) {
		String parentfolderuuid = body.relations.getRelationEntry(uuid).getParentUUID();
		FolderMapTreeNode child = getMapIdTreeNode(uuid);
		FolderMapTreeNode parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		body.relations.removeRelation(uuid);
		// body.maps.deleteMapEntry(uuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#del#mapentry#"+uuid);
	}
	
	public boolean parentOf(String childuuid, String parentuuid) {
		FolderMapTreeNode child = getMapIdTreeNode(childuuid);
		FolderMapTreeNode parent = child.getParent();
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
		FolderMapTreeNode child = getMapIdTreeNode(sourceuuid);
		FolderMapTreeNode parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		parent = getMapIdTreeNode(destuuid);
		child.setParent(parent);
		parent.addChild(child);
		if(body.folders.getFolderEntry(sourceuuid) != null) {
			FolderEntry childfolder = body.folders.getFolderEntry(sourceuuid);
			FolderEntry parentfolder = body.folders.getFolderEntry(destuuid);
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
	
	public Head getHead() {
		return head;
	}
	public Body getBody() {
		return body;
	}
	public History getHist() {
		return hist;
	}
}
