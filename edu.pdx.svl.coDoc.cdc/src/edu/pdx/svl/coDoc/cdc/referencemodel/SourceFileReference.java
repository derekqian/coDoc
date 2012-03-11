package edu.pdx.svl.coDoc.cdc.referencemodel;

import org.simpleframework.xml.Element;


public class SourceFileReference extends Reference {
	@Element
	protected String fileName;
	@Element
	protected String filePath;
	
	protected ProjectReference projectReference;
	public void setProjectReference(ProjectReference pr) {
		projectReference = pr;
	}
	public ProjectReference getProjectReference() {
		return projectReference;
	}
	
	public SourceFileReference() {
		fileName = "DefaultFile.mc";
		filePath = "C:\\DefaultFileFolder\\DefaultFile.mc";
	}
	
	@Override
	public String getType() {
		return "File";
	}

	@Override
	public String description() {
		return fileName;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}
