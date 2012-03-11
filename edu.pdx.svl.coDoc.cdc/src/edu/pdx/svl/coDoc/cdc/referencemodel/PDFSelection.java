package edu.pdx.svl.coDoc.cdc.referencemodel;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiEditor;
import org.simpleframework.xml.Element;

import edu.pdx.svl.coDoc.poppler.editor.PDFEditor;
import edu.pdx.svl.coDoc.poppler.editor.PDFPageViewer;



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
	
	static PDFEditor pdfEditor;
	static PDFPageViewer acrobatInterface;
//	static AcrobatJNIStub acrobatInterface;
	
	public PDFSelection() {
		if (acrobatInterface == null) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
			IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
			if(editorrefs.length != 0)
			{
				MultiEditor editor = (MultiEditor) editorrefs[0].getEditor(false);
				
				IEditorPart[] editors = editor.getInnerEditors();
				for(int i=0; i<editors.length; i++)
				{
					System.out.println(editors[i].getClass().getName());
					if(editors[i].getClass().getName().equals("edu.pdx.svl.coDoc.poppler.editor.PDFEditor"))
					{
						pdfEditor = (PDFEditor) editors[i];
					}
				}
				acrobatInterface = pdfEditor.getPDFPageViewer();
			}
			else
			{
				pdfEditor = null;
				acrobatInterface = null;
			}
//			acrobatInterface = new AcrobatJNIStub();
		}
	}
	
	public void fetchFromAcrobat(){
		page = acrobatInterface.getPage();
		Rectangle selection = acrobatInterface.getSelection();
		if(selection == null)
		{
			top = 0;
			bottom = 0;
			left = 0;
			right = 0;
		}
		else
		{
			top = selection.y;
			bottom = selection.y + selection.height;
			left = selection.x;
			right = selection.x + selection.width;
		}
		setText(acrobatInterface.getSelectedText());
	}
	
	public void selectTextInAcrobat() {
		if (acrobatInterface == null) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
			IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
			if(editorrefs.length != 0)
			{
				MultiEditor editor = (MultiEditor) editorrefs[0].getEditor(false);
				
				IEditorPart[] editors = editor.getInnerEditors();
				for(int i=0; i<editors.length; i++)
				{
					System.out.println(editors[i].getClass().getName());
					if(editors[i].getClass().getName().equals("edu.pdx.svl.coDoc.poppler.editor.PDFEditor"))
					{
						pdfEditor = (PDFEditor) editors[i];
					}
				}
				acrobatInterface = pdfEditor.getPDFPageViewer();
			}
			else
			{
				pdfEditor = null;
				acrobatInterface = null;
			}
		}
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
