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
class HeadV3 {
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

class CodeSpecFilesV3 {
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

class CodeSelectionV3 {
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
		if(o instanceof CodeSelectionV3) {
			CodeSelectionV3 sel = (CodeSelectionV3) o;
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

class SpecSelectionV3 {
	@ElementList
	private Vector<Integer> page = new Vector<Integer>();
	@ElementList
	private Vector<Integer> top = new Vector<Integer>();
	@ElementList
	private Vector<Integer> bottom = new Vector<Integer>();
	@ElementList
	private Vector<Integer> left = new Vector<Integer>();
	@ElementList
	private Vector<Integer> right = new Vector<Integer>();
	@ElementList
	private Vector<String> pdftext = new Vector<String>();
	
	public void addPage(int page) {
		this.page.add(page);
	}
	public void clearPage() {
		page.clear();
	}
	public Vector<Integer> getPage(Vector<Integer> type) {
		return page;
	}
	public String getPage(String type) {
		String res = "";
		Iterator it = page.iterator();
		while(it.hasNext()) {
			Integer p = (Integer) it.next();
			res += p;
			if(it.hasNext()) {
				res += " ";
			}
		}
		return res;
	}
	public void addTop(int top) {
		this.top.add(top); 
	}
	public void clearTop() {
		this.top.clear();
	}
	public Vector<Integer> getTop() {
		return top;
	}
	public void addBottom(int bottom) {
		this.bottom.add(bottom);
	}
	public void clearBottom() {
		this.bottom.clear();
	}
	public Vector<Integer> getBottom() {
		return bottom;
	}
	public void addLeft(int left) {
		this.left.add(left);
	}
	public void clearLeft() {
		this.left.clear();
	}
	public Vector<Integer> getLeft() {
		return left;
	}
	public void addRight(int right) {
		this.right.add(right);
	}
	public void clearRight() {
		this.right.clear();
	}
	public Vector<Integer> getRight() {
		return right;
	}
	public void addPDFText(String pdftext) {
		this.pdftext.add(pdftext);			
	}
	public void clearPDFText() {
		this.pdftext.clear();
	}
	public Vector<String> getPDFText(Vector<String> type) {
		return pdftext;
	}
	public String getPDFText(String type) {
		String res = "";
		Iterator it = pdftext.iterator();
		while(it.hasNext()) {
			String text = (String) it.next();
			res += text;
			if(it.hasNext()) {
				res += " ";
			}
		}
		return res;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SpecSelectionV3) {
			SpecSelectionV3 sel = (SpecSelectionV3) o;
			if(page.size() != sel.getPage(new Vector<Integer>()).size()) {
				return false;
			}
			Iterator its = page.iterator();
			Iterator itd = sel.getPage(new Vector<Integer>()).iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = left.iterator();
			itd = sel.getLeft().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = right.iterator();
			itd = sel.getRight().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = top.iterator();
			itd = sel.getTop().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = bottom.iterator();
			itd = sel.getBottom().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = pdftext.iterator();
			itd = sel.getPDFText(new Vector<String>()).iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return page+"/"+left+"/"+top+"/"+right+"/"+bottom+"/"+pdftext;
	}
	public SpecSelection toSpecSelection() {
		SpecSelection temp = new SpecSelection();
		Iterator it = bottom.iterator();
		while(it.hasNext()) {
			temp.addBottom(((Integer)it.next()));
		}
		it = left.iterator();
		while(it.hasNext()) {
			temp.addLeft(((Integer)it.next()));
		}
		it = page.iterator();
		while(it.hasNext()) {
			temp.addPage(((Integer)it.next()));
		}
		it = pdftext.iterator();
		while(it.hasNext()) {
			temp.addPDFText(((String)it.next()));
		}
		it = right.iterator();
		while(it.hasNext()) {
			temp.addRight(((Integer)it.next()));
		}
		it = top.iterator();
		while(it.hasNext()) {
			temp.addTop(((Integer)it.next()));
		}
		return temp;
	}
}

class FolderMapEntryV3 {
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

class FolderEntryV3 extends FolderMapEntryV3 {
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

class FoldersV3 {
	@ElementMap
	private Map<String, FolderEntryV3> folders = new HashMap<String, FolderEntryV3>();
	
	public FolderEntryV3 getFolderEntry(String uuid) {
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
			FolderEntryV3 entry = new FolderEntryV3();
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

class MapEntryV3 extends FolderMapEntryV3 {
	@Element
	private String codefileuuid=" ";
	@Element
	private CodeSelectionV3 codeselpath = new CodeSelectionV3();
	@Element
	private String specfileuuid=" ";
	@Element
	private SpecSelectionV3 specselpath = new SpecSelectionV3();
	@Element
	private String comment=" ";
	
	public void setCodefileUUID(String codefileuuid) {
		this.codefileuuid = codefileuuid;
	}
	public String getCodefileUUID() {
		return codefileuuid;
	}
	public void setCodeselpath(CodeSelectionV3 codeselpath2) {
		this.codeselpath = codeselpath2;
	}
	public CodeSelectionV3 getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfileUUID(String specfileuuid) {
		this.specfileuuid = specfileuuid;
	}
	public String getSpecfileUUID() {
		return specfileuuid;
	}
	public void setSpecselpath(SpecSelectionV3 specselpath) {
		this.specselpath = specselpath;
	}
	public SpecSelectionV3 getSpecselpath() {
		return specselpath;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
}

class MapsV3 {
	@ElementMap
	private Map<String, MapEntryV3> maplist = new HashMap<String, MapEntryV3>();
	
	public MapEntryV3 getMapEntry(String uuid) {
		return maplist.get(uuid);
	}
	public String getMapEntryId(String codefileuuid, CodeSelectionV3 codeSelectionV3, String specfileuuid, SpecSelectionV3 specSelectionV3, String comment) {
		Set<String> uuids = maplist.keySet();
		Iterator it = uuids.iterator();
		while(it.hasNext()) {
			String uuid = (String) it.next();
			MapEntryV3 entry = maplist.get(uuid);
			if(codefileuuid.equals(entry.getCodefileUUID()) && codeSelectionV3.equals(entry.getCodeselpath())
					                                        && specfileuuid.equals(entry.getSpecfileUUID())
					                                        && specSelectionV3.equals(entry.getSpecselpath())
					                                        && comment.equals(entry.getComment())) {
				return uuid;
			}
		}
		return null;
	}
	public Set<String> getAllMapEntryId() {
		return maplist.keySet();
	}
	public String addMapEntry(String time, String os, String creater, String codefileuuid, CodeSelectionV3 codeselpath, String specfileuuid, SpecSelectionV3 specselpath, String comment) {
		String uuid = getMapEntryId(codefileuuid, codeselpath, specfileuuid, specselpath, comment);
		if(null == uuid) {
			uuid = UUID.randomUUID().toString();
			MapEntryV3 entry = new MapEntryV3();
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

class RelationEntryV3 {
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

class RelationsV3 {
	@ElementList
	private Vector<RelationEntryV3> relations = new Vector<RelationEntryV3>();
	
	public Vector<RelationEntryV3> getAllRelationEntries() {
		return relations;
	}
	public RelationEntryV3 getRelationEntry(String uuid) {
		for(RelationEntryV3 entry : relations) {
			if(entry.getUUID().equals(uuid)) {
				return entry;
			}
		}
		return null;
	}
	public void addRelation(String uuid, String parentuuid) {
		if(null == getRelationEntry(uuid)) {
			RelationEntryV3 entry = new RelationEntryV3();
			entry.setUUID(uuid);
			entry.setParentUUID(parentuuid);
			relations.add(entry);			
		}
	}
	public void removeRelation(String uuid) {
		RelationEntryV3 entry = getRelationEntry(uuid);
		if(null != entry) {
			relations.removeElement(entry);
		}
	}
}

class BodyV3 {
	@Element
	public CodeSpecFilesV3 codefiles = null;
	@Element
	public CodeSpecFilesV3 specfiles = null;
	@Element
	public FoldersV3 folders = null;
	@Element
	public MapsV3 maps = null;
	@Element
	public RelationsV3 relations = null;
	
	public BodyV3() {
		codefiles = new CodeSpecFilesV3();
		specfiles = new CodeSpecFilesV3();
		folders = new FoldersV3();
		maps = new MapsV3();
		relations = new RelationsV3();
	}
}

class OpEntryV3 {
	@Element
	private String operation=" ";
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
}

class LastOpenedV3 {
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
class HistoryV3 {
	@Element
	private LastOpenedV3 lastOpened = new LastOpenedV3();
	@ElementList
	private Vector<OpEntryV3> operations = new Vector<OpEntryV3>();
	
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
		OpEntryV3 opentry = new OpEntryV3();
		opentry.setOperation(op);
		operations.add(opentry);
	}
	
	public void deleteOperation(String op) {
		for(OpEntryV3 entry : operations) {
			if(entry.getOperation().equals(op)) {
				operations.removeElement(entry);
				break;
			}
		}
	}
	
	public Vector<OpEntryV3> getOperationList() {
		return operations;
	}
}

class FolderMapTreeNodeV3 {
	private String uuid = null;
	private FolderMapTreeNodeV3 parent = null;
	private Vector<FolderMapTreeNodeV3> children = new Vector<FolderMapTreeNodeV3>();
	
	public FolderMapTreeNodeV3(String uuid) {
		this.uuid = uuid;
	}
	public String getData() {
		return uuid;
	}
	public void setData(String uuid) {
		this.uuid = uuid;
	}
	public void setParent(FolderMapTreeNodeV3 parent) {
		this.parent = parent;
	}
	public FolderMapTreeNodeV3 getParent() {
		return parent;
	}
	public void addChild(FolderMapTreeNodeV3 child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(FolderMapTreeNodeV3 child) {
		children.remove(child);
	}
	public boolean hasChildren() {
		return (!children.isEmpty());
	}
	public FolderMapTreeNodeV3[] getChildren() {
		FolderMapTreeNodeV3[] temp = new FolderMapTreeNodeV3[children.size()];
		System.arraycopy(children.toArray(), 0, temp, 0, children.size());
		return temp;
	}
}

@Root
public class CDCModelV3 {
	@Element
	private HeadV3 head;
	@Element
	private BodyV3 body;
	@Element
	private HistoryV3 hist;
	
	private Date date;
	private SimpleDateFormat ft = null;
	
	private FolderMapTreeNodeV3 root = null;
	
	public CDCModelV3() {
		Properties props=System.getProperties();
		ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS zzz");
		date = new Date();
		String time = ft.format(date);
		String os = props.getProperty("os.name");
		String creater = props.getProperty("user.name");
		head = new HeadV3();
		head.setFiletype("CDC");
		head.setCreater(creater);
		head.setOS(os);
		head.setCreatetime(time);
		body = new BodyV3();
		hist = new HistoryV3();
		
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
	
	public FolderMapTreeNodeV3 getMapIdTree() {
		Map<String, FolderMapTreeNodeV3> created = new HashMap<String, FolderMapTreeNodeV3>();
		FolderMapTreeNodeV3 child = null;
		FolderMapTreeNodeV3 parent = null;
		if(root == null) {
			String rootuuid = body.folders.getFolderEntryId("/");
			root = new FolderMapTreeNodeV3(rootuuid);
			created.put(rootuuid, root);
			// only for edges, so rootuuid can't be in uuid field.
			for(RelationEntryV3 entry : body.relations.getAllRelationEntries()) {
				String uuid = entry.getUUID();
				String parentuuid = entry.getParentUUID();
				if(created.containsKey(uuid)) {
					child = created.get(uuid);
				} else {
					child = new FolderMapTreeNodeV3(uuid);
					created.put(uuid, child);
				}
				if(created.containsKey(parentuuid)) {
					parent = created.get(parentuuid);
				} else {
					parent = new FolderMapTreeNodeV3(parentuuid);
					created.put(parentuuid, parent);
				}
				child.setParent(parent);
				parent.addChild(child);					
			}
		}
		return root;
	}
	
	private FolderMapTreeNodeV3 getMapIdTreeNode(String uuid) {
		Stack<FolderMapTreeNodeV3> stack = new Stack<FolderMapTreeNodeV3>();
		if(root == null) {
			getMapIdTree();
		}
		stack.push(root);
		while(!stack.empty()) {
			FolderMapTreeNodeV3 node = stack.pop();
			if(node.getData().equals(uuid)) {
				return node;
			}
			FolderMapTreeNodeV3[] children = node.getChildren();
			for(FolderMapTreeNodeV3 n : children) {
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
		FolderEntryV3 folderentry = body.folders.getFolderEntry(parentfolderuuid);
		String folderpath = folderentry.getFolderpath();
		if(!folderentry.getFoldername().equals("/")) {
			folderpath += "/";
		}
		folderpath += foldername;
		String uuid = body.folders.addFolderEntry(time, os, creater, folderpath);
		FolderMapTreeNodeV3 parent = getMapIdTreeNode(parentfolderuuid);
		FolderMapTreeNodeV3 child = new FolderMapTreeNodeV3(uuid);
		child.setParent(parent);
		parent.addChild(child);
		body.relations.addRelation(uuid, parentfolderuuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#add#folderentry#"+uuid+"#"+parentfolderuuid);
		return uuid;
	}
	
	public FolderEntryV3 getFolderEntry(String uuid) {
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
		FolderMapTreeNodeV3 child = getMapIdTreeNode(uuid);
		if(child.hasChildren()) {
			return;
		}
		String parentfolderuuid = body.relations.getRelationEntry(uuid).getParentUUID();
		FolderMapTreeNodeV3 parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		body.relations.removeRelation(uuid);
		// body.folders.deleteFolderEntry(uuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#del#folderentry#"+uuid);
	}
	
	public String addMapEntry(String parentfolderuuid, String codefilename, CodeSelectionV3 codeSelectionV3, String specfilename, SpecSelectionV3 specSelectionV3, String comment) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		return addMapEntry(time, os, creater, parentfolderuuid, codefilename, codeSelectionV3, specfilename, specSelectionV3, comment);
	}
	public String addMapEntry(String time, String os, String creater, String parentfolderuuid, String codefilename, CodeSelectionV3 codeselpath, String specfilename, SpecSelectionV3 specselpath, String comment) {
		addCodeFileEntry(codefilename);
		addSpecFileEntry(specfilename);
		String codefileuuid = body.codefiles.getFileEntryId(codefilename);
		String specfileuuid = body.specfiles.getFileEntryId(specfilename);
		String uuid = body.maps.getMapEntryId(codefileuuid, codeselpath, specfileuuid, specselpath, comment);
		if(uuid == null) {
			uuid = body.maps.addMapEntry(time, os, creater, codefileuuid, codeselpath, specfileuuid, specselpath, comment);
			FolderMapTreeNodeV3 parent = getMapIdTreeNode(parentfolderuuid);
			FolderMapTreeNodeV3 child = new FolderMapTreeNodeV3(uuid);
			child.setParent(parent);
			parent.addChild(child);
			body.relations.addRelation(uuid, parentfolderuuid);
			hist.addOperation(time+"#"+os+"#"+creater+"#add#mapentry#"+uuid+"#"+parentfolderuuid);
		}
		return uuid;
	}
	
	public String editMapEntry(String olduuid, String codefilename, CodeSelectionV3 codeSelectionV3, String specfilename, SpecSelectionV3 specSelectionV3, String comment) {
		Properties props=System.getProperties();
		date = new Date();
		String time = ft.format(date);
		String creater = props.getProperty("user.name");
		String os = props.getProperty("os.name");
		return editMapEntry(time, os, creater, olduuid, codefilename, codeSelectionV3, specfilename, specSelectionV3, comment);
	}
	public String editMapEntry(String time, String os, String creater, String olduuid, String codefilename, CodeSelectionV3 codeselpath, String specfilename, SpecSelectionV3 specselpath, String comment) {
		addCodeFileEntry(codefilename);
		addSpecFileEntry(specfilename);
		String codefileuuid = body.codefiles.getFileEntryId(codefilename);
		String specfileuuid = body.specfiles.getFileEntryId(specfilename);
		String uuid = body.maps.addMapEntry(time, os, creater, codefileuuid, codeselpath, specfileuuid, specselpath, comment);		
		FolderMapTreeNodeV3 child = getMapIdTreeNode(olduuid);
		child.setData(uuid);
		RelationEntryV3 rentry = body.relations.getRelationEntry(olduuid);
		rentry.setUUID(uuid);		
		hist.addOperation(time+"#"+os+"#"+creater+"#edt#mapentry#"+olduuid+"#"+uuid);
		return uuid;
	}
	
	public MapEntryV3 getMapEntry(String uuid) {
		return body.maps.getMapEntry(uuid);
	}
	
	public void deleteMapEntry(String uuid) {
		Properties props=System.getProperties();
		date = new Date();
		deleteMapEntry(ft.format(date), props.getProperty("os.name"), props.getProperty("user.name"), uuid);
	}
	public void deleteMapEntry(String time, String os, String creater, String uuid) {
		String parentfolderuuid = body.relations.getRelationEntry(uuid).getParentUUID();
		FolderMapTreeNodeV3 child = getMapIdTreeNode(uuid);
		FolderMapTreeNodeV3 parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		body.relations.removeRelation(uuid);
		// body.maps.deleteMapEntry(uuid);
		hist.addOperation(time+"#"+os+"#"+creater+"#del#mapentry#"+uuid);
	}
	
	public boolean parentOf(String childuuid, String parentuuid) {
		FolderMapTreeNodeV3 child = getMapIdTreeNode(childuuid);
		FolderMapTreeNodeV3 parent = child.getParent();
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
		FolderMapTreeNodeV3 child = getMapIdTreeNode(sourceuuid);
		FolderMapTreeNodeV3 parent = getMapIdTreeNode(parentfolderuuid);
		parent.removeChild(child);
		parent = getMapIdTreeNode(destuuid);
		child.setParent(parent);
		parent.addChild(child);
		if(body.folders.getFolderEntry(sourceuuid) != null) {
			FolderEntryV3 childfolder = body.folders.getFolderEntry(sourceuuid);
			FolderEntryV3 parentfolder = body.folders.getFolderEntry(destuuid);
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
	
	public HeadV3 getHead() {
		return head;
	}
	public BodyV3 getBody() {
		return body;
	}
	public HistoryV3 getHist() {
		return hist;
	}
	public static void main(String[] args) {
		toV4();
	}
	private static void toV4() {
		CDCModelV3 cdcModelV3 = null;
		File cdcFileV3 = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver3.cdc");
		Serializer serializer = new Persister();
		try {
			cdcModelV3 = serializer.read(CDCModelV3.class, cdcFileV3);
		} catch (Exception e) {
			System.out.println("Unable to read cdcFileV3!");
			e.printStackTrace();
		}
		
		CDCModel cdcModel = new CDCModel();
		cdcModel.getHead().setCreater(cdcModelV3.getHead().getCreater());
		cdcModel.getHead().setCreatetime(cdcModelV3.getHead().getCreatetime());
		cdcModel.getHead().setFiletype(cdcModelV3.getHead().getFiletype());
		cdcModel.getHead().setOS(cdcModelV3.getHead().getOS());
		cdcModel.getMapIdTree();
		
		Vector<OpEntryV3> ops = cdcModelV3.getHist().getOperationList();
		Iterator it = ops.iterator();
		while(it.hasNext()) {
			OpEntryV3 op = (OpEntryV3) it.next();
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
				String folderPath = cdcModelV3.getFolderEntry(parentuuid).getFolderpath();
				parentuuid = cdcModel.getBody().folders.getFolderEntryId(folderPath);
				MapEntryV3 entry = cdcModelV3.getMapEntry(uuid);
				String codefilename = cdcModelV3.getCodeFilename(entry.getCodefileUUID());
				String specfilename = cdcModelV3.getSpecFilename(entry.getSpecfileUUID());
				cdcModel.addMapEntry(parentuuid, codefilename, entry.getCodeselpath().toCodeSelection(), specfilename, entry.getSpecselpath().toSpecSelection(), entry.getComment());
			} else if(opstr.get(3).equals("edt") && opstr.get(4).equals("mapentry")) {
				String olduuid = opstr.get(5);
				String uuid = opstr.get(6);
				MapEntryV3 oldentry = cdcModelV3.getMapEntry(olduuid);
				String oldcodefilename = cdcModelV3.getBody().codefiles.getFilename(oldentry.getCodefileUUID());
				String oldcodefileuuid = cdcModel.getBody().codefiles.getFileEntryId(oldcodefilename);
				CodeSelectionV3 codeselpath = oldentry.getCodeselpath();
				String oldspecfilename = cdcModelV3.getBody().specfiles.getFilename(oldentry.getSpecfileUUID());
				String oldspecfileuuid = cdcModel.getBody().specfiles.getFileEntryId(oldspecfilename);
				SpecSelectionV3 specselpath = oldentry.getSpecselpath();
				String comment = oldentry.getComment();
				olduuid = cdcModel.getBody().maps.getMapEntryId(oldcodefileuuid, codeselpath.toCodeSelection(), oldspecfileuuid, specselpath.toSpecSelection(), comment);
				MapEntryV3 entry = cdcModelV3.getMapEntry(uuid);
				String codefilename = cdcModelV3.getCodeFilename(entry.getCodefileUUID());
				String specfilename = cdcModelV3.getSpecFilename(entry.getSpecfileUUID());
				cdcModel.editMapEntry(olduuid, codefilename, entry.getCodeselpath().toCodeSelection(), specfilename, entry.getSpecselpath().toSpecSelection(), entry.getComment());
			} else if(opstr.get(3).equals("add") && opstr.get(4).equals("codefileentry")) {
			} else if(opstr.get(3).equals("add") && opstr.get(4).equals("specfileentry")) {
			} else {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "Unsupported operation:\n"+op.getOperation());
				return;
			}
		}

		cdcModel.setLastOpenedCodeFilename(cdcModelV3.getLastOpenedCodeFilename());
		cdcModel.setLastOpenedSpecFilename(cdcModelV3.getLastOpenedSpecFilename());
		File cdcFile = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver4.cdc");
		try {
			serializer.write(cdcModel, cdcFile);
		} catch (Exception e) {
			System.out.println("Unable to write cdcFile!");
			e.printStackTrace();
		}		
	}
}
