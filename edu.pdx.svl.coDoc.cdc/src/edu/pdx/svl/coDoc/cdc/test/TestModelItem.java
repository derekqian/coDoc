package edu.pdx.svl.coDoc.cdc.test;

public class TestModelItem {
	public String sourceReferences = "sourceReferences";
	public String type = "type";
	public String specificationReference = "specificationReference";
	public String page = "page";
	public String comments = "comments";
	
	public String description() {
		return sourceReferences;
	}
	
	public String getType() {
		return type;
	}
	
	public String pdfDescription() {
		return specificationReference;
	}
	
	public String pdfPage() {
		return page;
	}
	
	public String getComment() {
		return comments;
	}
}
