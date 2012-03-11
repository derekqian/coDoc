package edu.pdx.svl.coDoc.cdc.referencemodel;

import java.io.File;

import org.simpleframework.xml.Element;


public class PDFFile implements IPDF {
	public static final String DEFAULT_PDF_FILE = "";

	File f;
	
	@Element(required=false)
	String fileName;
	
	@Element(required=false)
	String filePath;

	public PDFFile(File f) {
		this.f = f;
		fileName = f.getName();
		filePath = f.getAbsolutePath();
	}
	public PDFFile() {
		this(new File(DEFAULT_PDF_FILE));
	}

	public String description() {
//		return f.getPath();
//		return f.getName();
		return filePath;
	}
	public String page() {
		return "";
	}

	public File getFile() {
		if (f == null || f.getPath() == "" || f.getPath() == null) {
			f = new File(filePath);
		}
		return f;
	}
	
	public String getFileName() {
		return fileName;
	}
}
