package edu.pdx.svl.coDoc.refexp.referencemodel;

import org.simpleframework.xml.Element;



public class PDFSelection implements IPDF {
	@Element
	protected int page		= 0;
	@Element
	protected int top		= 0;
	@Element
	protected int bottom	= 0;
	@Element
	protected int left		= 0;
	@Element
	protected int right	= 0;
	@Element
	protected String text	= "empty";
	
	static AcrobatJNI acrobatInterface;
//	static AcrobatJNIStub acrobatInterface;
	
	public PDFSelection() {
		if (acrobatInterface == null) {
			acrobatInterface = new AcrobatJNI();
//			acrobatInterface = new AcrobatJNIStub();
		}
	}
	
	public void fetchFromAcrobat(){
		page = acrobatInterface.getPage();
		top = acrobatInterface.getTop();
		bottom = acrobatInterface.getBottom();
		left = acrobatInterface.getLeft();
		right = acrobatInterface.getRight();
		setText(acrobatInterface.getSelectedText());
	}
	
	public void selectTextInAcrobat() {
		acrobatInterface.selectText(page, top, bottom, left, right);
	}
	
	public String toString() {
		String str = "  page = " + page +
						"\n  top = " + top +
						"\n  bottom = " + bottom +
						"\n  left = " + left +
						"\n  right = " + right +
						"\n  text = " + text;
		return str;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public String getText() {
		return text;
	}

	/**
	 * Acrobat adds junk to the first two characters of a 
	 * selection, so we are getting rid of that corrupt junk.
	 * @param text
	 */
	public void setText(String text) {
//		this.text = text.substring(2);
		this.text = text;
	}

	public String description() {
		
//		if (text.length() <= 50)
//			return text;
//		return text.substring(0, 50) + "...";
		
		String formattedText = text.replace('\n', ' ').replace('\t', ' ');
		return formattedText;
	}

	public String page() {
		int actualPage = 0;
		actualPage = page+1;//the first page is page 0
		return String.valueOf(actualPage);
	}
}
