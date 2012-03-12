package edu.pdx.svl.coDoc.cdc.editor;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import edu.pdx.svl.coDoc.cdc.referencemodel.PDFSelection;

class Head {
	@Element
	private final String filetype = "CDC";
	@Element
	private String creater = "Derek";
	@Element
	private String createtime = "2012/03/11/15:47";
	
	public Head() {
	}
}

class Body {
	@Element
	private PDFSelection pdfselection = null;
	
	public Body() {
		pdfselection = new PDFSelection();
	}
}

@Root
public class CDCModel {
	@Element
	private Head head;
	@Element
	private Body body;
	
	public CDCModel() {
		head = null;
		body = null;
	}
	
	public CDCModel(Head head, Body body) {
		this.head = head;
		this.body = body;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Head getHead() {
		return head;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Body getBody() {
		return body;
	}
}
