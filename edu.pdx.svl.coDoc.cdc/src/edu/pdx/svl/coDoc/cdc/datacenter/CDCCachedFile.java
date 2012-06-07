package edu.pdx.svl.coDoc.cdc.datacenter;

import java.util.Vector;
import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;



public class CDCCachedFile {
	private String fileName;
	private CDCModel cdcModel;

	
	public CDCCachedFile() {
		fileName = null;
		cdcModel = null;
	}
	
	public void open(String filename) {
		fileName = filename;
		if(fileName != null) {
			cdcModel = SimpleXML.readCDCModel(fileName);
		}
	}
	public void close() {
	}
	
	public void refresh() {
		if(fileName!=null) {
			cdcModel = SimpleXML.readCDCModel(fileName);
		}
	}
	public void flush() {
		if(fileName!=null && cdcModel!=null) {
			SimpleXML.writeCDCModel(cdcModel, fileName);
		}
	}
	
	public void addCodeFileEntry(String filename) {
		cdcModel.addCodeFileEntry(filename);
		flush();
	}
	
	public void deleteCodeFileEntry(String filename) {
		cdcModel.deleteCodeFileEntry(filename);
		flush();
	}
	
	public void addSpecFileEntry(String filename) {
		cdcModel.addSpecFileEntry(filename);
		flush();
	}
	
	public void deleteSpecFileEntry(String filename) {
		cdcModel.deleteSpecFileEntry(filename);
		flush();
	}
	
	public void addMapEntry(String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		cdcModel.addMapEntry(codefilename, codeselpath, specfilename, specselpath, comment);
		flush();
	}
	
	public void deleteMapEntry(String codefilename, CodeSelection codeselpath, String specfilename, SpecSelection specselpath, String comment) {
		cdcModel.deleteMapEntry(codefilename, codeselpath, specfilename, specselpath, comment);
		flush();
	}
	
	public Vector<MapEntry> getMapEntries() {
		return cdcModel.getMapEntries();
	}
	
	public void setLastOpenedCodeFilename(String codeFilename) {
		cdcModel.setLastOpenedCodeFilename(codeFilename);
		flush();
	}
	public String getLastOpenedCodeFilename() {
		return cdcModel.getLastOpenedCodeFilename();
	}
	
	public void setLastOpenedSpecFilename(String specFilename) {
		cdcModel.setLastOpenedSpecFilename(specFilename);
		flush();
	}
	public String getLastOpenedSpecFilename() {
		return cdcModel.getLastOpenedSpecFilename();
	}
}
