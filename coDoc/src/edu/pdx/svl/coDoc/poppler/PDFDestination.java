package edu.pdx.svl.coDoc.poppler;


public class PDFDestination {
	
	private int pagenum;
	
	private float left;
	private float right;
	private float top;
	private float bottom;
	private float zoom;
	 
	/**
	 * The constructor.
	 */
	public PDFDestination(int pagenum, float left, float right, float top, float bottom, float zoom) 
	{
		this.pagenum = pagenum;
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.zoom = zoom;
	}
	
	public int getPage()
	{
		return pagenum;
	}
	
	public float getLeft()
	{
		return left;
	}
	
	public float getRight()
	{
		return right;
	}
	
	public float getTop()
	{
		return top;
	}
	
	public float getBottom()
	{
		return bottom;
	}
	
	public float getZoom()
	{
		return zoom;
	}
}
