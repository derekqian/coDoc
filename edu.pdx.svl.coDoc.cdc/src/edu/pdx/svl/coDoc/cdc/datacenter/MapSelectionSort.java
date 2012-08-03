package edu.pdx.svl.coDoc.cdc.datacenter;

public class MapSelectionSort {
	public static final int CODEFILE = 1;
	public static final int CODETEXT = 2;
	public static final int SPECFILE = 3;
	public static final int PDFPAGE = 4;
	public static final int SPECTEXT = 5;
	public static final int COMMENT = 6;

	private boolean needsort = false;
	private int key = 0;
	
	private boolean codefilenameinc = false;
	private boolean codefiletextinc = false;
	private boolean specfilenameinc = false;
	private boolean specfilepageinc = false;
	private boolean specfiletextinc = false;
	private boolean comments = false;
	
	public void setNeedsort() {
		needsort = true;
	}
	public void clearNeedsort() {
		needsort = false;
	}
	public boolean isNeedsort() {
		return needsort;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getKey() {
		return key;
	}
	public void toggleCodefilename() {
		codefilenameinc = !codefilenameinc;
		codefiletextinc = false;
		specfilenameinc = false;
		specfilepageinc = false;
		specfiletextinc = false;
		comments = false;
	}
	public boolean getCodefilename() {
		return codefilenameinc;
	}
	public void toggleCodefiletext() {
		codefilenameinc = false;
		codefiletextinc = !codefiletextinc;
		specfilenameinc = false;
		specfilepageinc = false;
		specfiletextinc = false;
		comments = false;
	}
	public boolean getCodefiletext() {
		return codefiletextinc;
	}
	public void toggleSpecfilename() {
		codefilenameinc = false;
		codefiletextinc = false;
		specfilenameinc = !specfilenameinc;
		specfilepageinc = false;
		specfiletextinc = false;
		comments = false;
	}
	public boolean getSpecfilename() {
		return specfilenameinc;
	}
	public void toggleSpecfilepage() {
		codefilenameinc = false;
		codefiletextinc = false;
		specfilenameinc = false;
		specfilepageinc = !specfilepageinc;
		specfiletextinc = false;
		comments = false;
	}
	public boolean getSpecfilepage() {
		return specfilepageinc;
	}
	public void toggleSpecfiletext() {
		codefilenameinc = false;
		codefiletextinc = false;
		specfilenameinc = false;
		specfilepageinc = false;
		specfiletextinc = !specfiletextinc;
		comments = false;
	}
	public boolean getSpecfiletext() {
		return specfiletextinc;
	}
	public void toggleComments() {
		codefilenameinc = false;
		codefiletextinc = false;
		specfilenameinc = false;
		specfilepageinc = false;
		specfiletextinc = false;
		comments = !comments;
	}
	public boolean getComments() {
		return comments;
	}
}
