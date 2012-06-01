/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.editor;

import java.util.Vector;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class SpecSelection {
	@Element
	private int page;
	@Element
	private int top;
	@Element
	private int bottom;
	@Element
	private int left;
	@Element
	private int right;
	@Element
	private String pdftext=" ";
	
	public void setPage(int page) {
		this.page = page;
	}
	public int getPage() {
		return page;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getTop() {
		return top;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getBottom() {
		return bottom;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getLeft() {
		return left;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getRight() {
		return right;
	}
	public void setPDFText(String pdftext) {
		this.pdftext = pdftext;
	}
	public String getPDFText() {
		return pdftext;
	}
}
