package edu.pdx.svl.coDoc.poppler.editor;

public class PDFSelection
{
	private int page;
	private int top;
	private int bottom;
	private int left;
	private int right;

	public PDFSelection() 
	{
		page = 0;
		top = 0;
		bottom = 0;
		left = 0;
		right = 0;
	}
	public PDFSelection(int page, int left, int top, int right, int bottom) {
		this.page = page;
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
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
	public int getRigth() {
		return right;
	}
}
