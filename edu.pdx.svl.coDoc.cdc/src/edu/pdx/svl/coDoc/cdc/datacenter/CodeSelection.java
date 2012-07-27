/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import org.simpleframework.xml.Element;

public class CodeSelection {
	@Element
	private String syntaxcodepath=" ";
	@Element
	private String selcodepath=" ";
	@Element
	private String syntaxcodetext=" ";
	@Element
	private String selcodetext=" ";
	
	public void setSyntaxCodePath(String syntaxcodepath) {
		if(syntaxcodepath != null) {
			this.syntaxcodepath = syntaxcodepath;			
		}
	}
	public String getSyntaxCodePath() {
		return syntaxcodepath.equals(" ")?null:syntaxcodepath;
	}
	public void setSelCodePath(String selcodepath) {
		if(selcodepath != null) {
			this.selcodepath = selcodepath;			
		}
	}
	public String getSelCodePath() {
		return selcodepath.equals(" ")?null:selcodepath;
	}
	public void setSyntaxCodeText(String codetext) {
		if(codetext != null) {
			this.syntaxcodetext = codetext;			
		}
	}
	public String getSyntaxCodeText() {
		return syntaxcodetext.equals(" ")?null:syntaxcodetext;
	}
	public void setSelCodeText(String codetext) {
		if(codetext != null) {
			this.selcodetext = codetext;			
		}
	}
	public String getSelCodeText() {
		return selcodetext.equals(" ")?null:selcodetext;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof CodeSelection) {
			CodeSelection sel = (CodeSelection) o;
			return selcodepath.equals(sel.getSelCodePath()) && selcodetext.equals(sel.getSelCodeText())
			                                                && syntaxcodepath.equals(sel.getSyntaxCodePath())
			                                                && syntaxcodetext.equals(sel.getSyntaxCodeText());
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return syntaxcodetext+"/"+syntaxcodepath;
	}
}
