/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import java.util.Iterator;
import java.util.Vector;

import org.simpleframework.xml.ElementList;

public class SpecSelection {
	@ElementList
	private Vector<Integer> page = new Vector<Integer>();
	@ElementList
	private Vector<Integer> top = new Vector<Integer>();
	@ElementList
	private Vector<Integer> bottom = new Vector<Integer>();
	@ElementList
	private Vector<Integer> left = new Vector<Integer>();
	@ElementList
	private Vector<Integer> right = new Vector<Integer>();
	@ElementList
	private Vector<String> pdftext = new Vector<String>();
	
	public void addPage(int page) {
		this.page.add(page);
	}
	public void clearPage() {
		page.clear();
	}
	public Vector<Integer> getPage(Vector<Integer> type) {
		return page;
	}
	public String getPage(String type) {
		String res = "";
		Iterator it = page.iterator();
		while(it.hasNext()) {
			Integer p = (Integer) it.next();
			res += p;
			if(it.hasNext()) {
				res += " ";
			}
		}
		return res;
	}
	public void addTop(int top) {
		this.top.add(top); 
	}
	public void clearTop() {
		this.top.clear();
	}
	public Vector<Integer> getTop() {
		return top;
	}
	public void addBottom(int bottom) {
		this.bottom.add(bottom);
	}
	public void clearBottom() {
		this.bottom.clear();
	}
	public Vector<Integer> getBottom() {
		return bottom;
	}
	public void addLeft(int left) {
		this.left.add(left);
	}
	public void clearLeft() {
		this.left.clear();
	}
	public Vector<Integer> getLeft() {
		return left;
	}
	public void addRight(int right) {
		this.right.add(right);
	}
	public void clearRight() {
		this.right.clear();
	}
	public Vector<Integer> getRight() {
		return right;
	}
	public void addPDFText(String pdftext) {
		this.pdftext.add(pdftext);			
	}
	public void clearPDFText() {
		this.pdftext.clear();
	}
	public Vector<String> getPDFText(Vector<String> type) {
		return pdftext;
	}
	public String getPDFText(String type) {
		String res = "";
		Iterator it = pdftext.iterator();
		while(it.hasNext()) {
			String text = (String) it.next();
			res += text;
			if(it.hasNext()) {
				res += " ";
			}
		}
		return res;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SpecSelection) {
			SpecSelection sel = (SpecSelection) o;
			if(page.size() != sel.getPage(new Vector<Integer>()).size()) {
				return false;
			}
			Iterator its = page.iterator();
			Iterator itd = sel.getPage(new Vector<Integer>()).iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = left.iterator();
			itd = sel.getLeft().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = right.iterator();
			itd = sel.getRight().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = top.iterator();
			itd = sel.getTop().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = bottom.iterator();
			itd = sel.getBottom().iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
			its = pdftext.iterator();
			itd = sel.getPDFText(new Vector<String>()).iterator();
			while(its.hasNext()) {
				if(!its.next().equals(itd.next())) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return page+"/"+left+"/"+top+"/"+right+"/"+bottom+"/"+pdftext;
	}
}
