/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import org.simpleframework.xml.Element;

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
		if(pdftext != null) {
			this.pdftext = pdftext;			
		}
	}
	public String getPDFText() {
		return pdftext.equals(" ")?null:pdftext;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SpecSelection) {
			SpecSelection sel = (SpecSelection) o;
			return (sel.getPage()==page) && (sel.getLeft()==left)
			                             && (sel.getRight()==right)
			                             && (sel.getTop()==top)
			                             && (sel.getBottom()==bottom)
			                             && pdftext.equals(sel.getPDFText());
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return page+"/"+left+"/"+top+"/"+right+"/"+bottom+"/"+pdftext;
	}
}
