/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import java.util.Vector;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;


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

public class MapEntry {
	@Element
	private String codefilename=" ";
	@Element
	private CodeSelection codeselpath = new CodeSelection();
	@Element
	private String specfilename=" ";
	@Element
	private SpecSelection specselpath = new SpecSelection();
	@Element
	private String comment=" ";
	
	public void setCodefilename(String codefilename) {
		this.codefilename = codefilename;
	}
	public String getCodefilename() {
		return codefilename;
	}
	public void setCodeselpath(CodeSelection codeselpath) {
		this.codeselpath = codeselpath;
	}
	public CodeSelection getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfilename(String specfilename) {
		this.specfilename = specfilename;
	}
	public String getSpecfilename() {
		return specfilename;
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
