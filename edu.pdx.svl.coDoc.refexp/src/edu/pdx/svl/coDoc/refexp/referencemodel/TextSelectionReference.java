package edu.pdx.svl.coDoc.refexp.referencemodel;



import javax.swing.JOptionPane;

import org.simpleframework.xml.Element;


public class TextSelectionReference extends Reference {
	@Element
	protected Integer offset;
	
	@Element
	protected Integer length;
	
	@Element
	protected String selectednode = "";
	
	@Element
	protected String text = "";
	
	protected SourceFileReference sourceFile;
	public void setSourceFile(SourceFileReference sfr) {
		sourceFile = sfr;
	}
	public SourceFileReference getSourceFileReference() {
		return sourceFile;
	}

	/**
	 * If text is selected in a PDF, we will have that associated with the new
	 * source code text selection reference. If nothing is selected in a PDF,
	 * then we are just going to have it be associated with the currently
	 * selected PDF File. This is taken care of by default in the constructor of
	 * Reference, the parent class of TextSelectionReference.
	 */
	public TextSelectionReference() {
//		fetchAcrobatData();
	}
	
	public void fetchAcrobatData() {
//		if (PDFManager.INSTANCE.isAcrobatOpened() == true) {
			if (pdfSelection == null) {
				pdfSelection = new PDFSelection();
			}
			pdfSelection.fetchFromAcrobat();
			String text = pdfSelection.getText();
//			if (text.length() == 0) {
//				javax.swing.JOptionPane.showMessageDialog(null, "Warning: You have not selected any text in your PDF document!", "No PDF Selection", JOptionPane.WARNING_MESSAGE);
//			}
//		}
	}

	@Override
	public String getType() {
		return "Text Selection";
	}

	@Override
	public String description() {
//		if (text.length() < 50) {
//			return text;
//		} else {
//			return text.substring(0,50);
//		}
		String formattedText = text.replace('\n', ' ').replace('\t', ' ').replace('\r', ' ');
		return formattedText;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setSelectedNode(String selectednode) {
		this.selectednode = selectednode;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getSelectedNode() {
		return selectednode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
