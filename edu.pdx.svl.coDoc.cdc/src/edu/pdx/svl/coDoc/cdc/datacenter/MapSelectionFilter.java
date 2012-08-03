package edu.pdx.svl.coDoc.cdc.datacenter;

public class MapSelectionFilter {
	public static final int ALLDATA = 0;
	public static final int CODEFILE = 1;
	public static final int CODETEXT = 2;
	public static final int SPECFILE = 3;
	public static final int PDFPAGE = 4;
	public static final int SPECTEXT = 5;
	public static final int COMMENT = 6;
	
	private String searchStr = null;
	
	private int selector = 0;
	private String codeFileName = null;
	private String pdfFileName = null;
	private int pdfFilePage = 0;
	
	public void setSelector(int selector) {
		this.selector = selector;
	}
	public int getSelector() {
		return selector;
	}
	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	public String getSearchStr() {
		return searchStr;
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
