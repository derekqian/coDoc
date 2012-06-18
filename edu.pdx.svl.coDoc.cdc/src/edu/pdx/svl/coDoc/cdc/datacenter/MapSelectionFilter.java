package edu.pdx.svl.coDoc.cdc.datacenter;

public class MapSelectionFilter {
	public static final int CODEFILE = 0;
	public static final int PDFFILE = 1;
	public static final int PDFFILEPAGE = 2;
	
	private int selector = 0;
	private String codeFileName = null;
	private String pdfFileName = null;
	private int pdfFilePage = 0;
	
	public MapSelectionFilter(int selector) {
		this.selector = selector;
	}
	public int getSelector() {
		return selector;
	}
	public void setCodeFileName(String filename) {
		codeFileName = filename;
	}
	public String getCodeFileName() {
		return codeFileName;
	}
	public void setPdfFileName(String filename) {
		pdfFileName = filename;
	}
	public String getPdfFileName() {
		return pdfFileName;
	}
	public void setPdfFilePage(int page) {
		pdfFilePage = page;
	}
	public int getPdfFilePage() {
		return pdfFilePage;
	}
}
