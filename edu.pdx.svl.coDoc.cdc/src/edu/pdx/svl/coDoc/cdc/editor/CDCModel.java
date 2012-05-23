/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.editor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

class Head {
	@Element
	private String filetype=" ";
	@Element
	private String creater=" ";
	@Element
	private String createtime=" ";
	
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
}

class CodeFileEntry {
	@Element
	private String filename=" ";	
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
}

class CodeFiles {
	@ElementList
	private Vector<CodeFileEntry> files = new Vector<CodeFileEntry>();
	
	public void addFileEntry(String filename) {
		CodeFileEntry fileentry = new CodeFileEntry();
		fileentry.setFilename(filename);
		files.add(fileentry);
	}
	
	public void deleteFileEntry(String filename) {
		for(CodeFileEntry entry : files) {
			if(entry.getFilename().equals(filename)) {
				files.removeElement(entry);
				break;
			}
		}
	}
}

class SpecFileEntry {
	@Element
	private String filename=" ";
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
}

class SpecFiles {
	@ElementList
	private Vector<SpecFileEntry> files = new Vector<SpecFileEntry>();
	
	public void addFileEntry(String filename) {
		SpecFileEntry fileentry = new SpecFileEntry();
		fileentry.setFilename(filename);
		files.add(fileentry);
	}
	
	public void deleteFileEntry(String filename) {
		for(SpecFileEntry entry : files) {
			if(entry.getFilename().equals(filename)) {
				files.removeElement(entry);
				break;
			}
		}
	}
}

class MapEntry {
	@Element
	private String codefilename=" ";
	@Element
	private String codeselpath=" ";
	@Element
	private String specfilename=" ";
	@Element
	private String specselpath=" ";
	@Element
	private String comment=" ";
	
	public void setCodefilename(String codefilename) {
		this.codefilename = codefilename;
	}
	public String getCodefilename() {
		return codefilename;
	}
	public void setCodeselpath(String codeselpath) {
		this.codeselpath = codeselpath;
	}
	public String getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfilename(String specfilename) {
		this.specfilename = specfilename;
	}
	public String getSpecfilename() {
		return specfilename;
	}
	public void setSpecselpath(String specselpath) {
		this.specselpath = specselpath;
	}
	public String getSpecselpath() {
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
	@ElementList
	private Vector<MapEntry> maplist = new Vector<MapEntry>();
	
	public void addMapEntry(String codefilename, String codeselpath, String specfilename, String specselpath, String comment) {
		MapEntry mapentry = new MapEntry();
		mapentry.setCodefilename(codefilename);
		mapentry.setCodeselpath(codeselpath);
		mapentry.setSpecfilename(specfilename);
		mapentry.setSpecselpath(specselpath);
		mapentry.setComment(comment);
		maplist.add(mapentry);
	}
	
	public void deleteMapEntry(String codefilename, String codeselpath, String specfilename, String specselpath, String comment) {
		for(MapEntry entry : maplist) {
			if(entry.getCodefilename().equals(codefilename) && entry.getCodeselpath().equals(codeselpath)
					                                        && entry.getSpecfilename().equals(specfilename)
					                                        && entry.getSpecselpath().equals(specselpath)
					                                        && entry.getComment().equals(comment)) {
				maplist.removeElement(entry);
				break;
			}
		}
	}
	
	public Vector<MapEntry> getMapList() {
		return maplist;
	}
}

class Body {
	@Element
	public CodeFiles codefiles = null;
	@Element
	public SpecFiles specfiles = null;
	@Element
	public Maps maps = null;
	
	public Body() {
		codefiles = new CodeFiles();
		specfiles = new SpecFiles();
		maps = new Maps();
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
	private String codeFilename=" ";
	@Element
	private String specFilename=" ";
	
	public void setCodeFilename(String codeFilename) {
		this.codeFilename = (codeFilename==null)?" ":codeFilename;
	}
	public String getCodeFilename() {
		return codeFilename.equals(" ")?null:codeFilename;
	}
	
	public void setSpecFilename(String specFilename) {
		this.specFilename = (specFilename==null)?" ":specFilename;
	}
	public String getSpecFilename() {
		return specFilename.equals(" ")?null:specFilename;
	}
}
class History {
	@Element
	private LastOpened lastOpened = new LastOpened();
	@ElementList
	private Vector<OpEntry> operations = new Vector<OpEntry>();
	
	public void setLastOpenedCodeFilename(String codeFilename) {
		lastOpened.setCodeFilename(codeFilename);
	}
	public String getLastOpenedCodeFilename() {
		return lastOpened.getCodeFilename();
	}
	
	public void setLastOpenedSpecFilename(String specFilename) {
		lastOpened.setSpecFilename(specFilename);
	}
	public String getLastOpenedSpecFilename() {
		return lastOpened.getSpecFilename();
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
	
	public CDCModel() {
		ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS zzz");
		head = new Head();
		head.setFiletype("CDC");
		head.setCreater("Derek");
		date = new Date();
		head.setCreatetime(ft.format(date));
		body = new Body();
		hist = new History();
	}
	
	public void addCodeFileEntry(String filename) {
		body.codefiles.addFileEntry(filename);
		date = new Date();
		hist.addOperation(ft.format(date)+"#add#codefileentry#"+filename);
	}
	
	public void deleteCodeFileEntry(String filename) {
		body.codefiles.deleteFileEntry(filename);
		date = new Date();
		hist.addOperation(ft.format(date)+"#del#codefileentry#"+filename);
	}
	
	public void addSpecFileEntry(String filename) {
		body.specfiles.addFileEntry(filename);
		date = new Date();
		hist.addOperation(ft.format(date)+"#add#specfileentry#"+filename);
	}
	
	public void deleteSpecFileEntry(String filename) {
		body.specfiles.deleteFileEntry(filename);
		date = new Date();
		hist.addOperation(ft.format(date)+"#del#specfileentry#"+filename);
	}
	
	public void addMapEntry(String codefilename, String codeselpath, String specfilename, String specselpath, String comment) {
		body.maps.addMapEntry(codefilename, codeselpath, specfilename, specselpath, comment);
		date = new Date();
		hist.addOperation(ft.format(date)+"#add#mapentry#"+codefilename+"#"+codeselpath+"#"+specfilename+"#"+specselpath+"#"+comment);
	}
	
	public void deleteMapEntry(String codefilename, String codeselpath, String specfilename, String specselpath, String comment) {
		body.maps.deleteMapEntry(codefilename, codeselpath, specfilename, specselpath, comment);
		date = new Date();
		hist.deleteOperation(ft.format(date)+"#del#mapentry#"+codefilename+"#"+codeselpath+"#"+specfilename+"#"+specselpath+"#"+comment);
	}
	
	public MapEntry getFirstMapEntry() {
		return body.maps.getMapList().firstElement();
	}
	
	public void setLastOpenedCodeFilename(String codeFilename) {
		hist.setLastOpenedCodeFilename(codeFilename);
	}
	public String getLastOpenedCodeFilename() {
		return hist.getLastOpenedCodeFilename();
	}
	
	public void setLastOpenedSpecFilename(String specFilename) {
		hist.setLastOpenedSpecFilename(specFilename);
	}
	public String getLastOpenedSpecFilename() {
		return hist.getLastOpenedSpecFilename();
	}
}
