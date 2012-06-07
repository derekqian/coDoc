/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import java.util.Vector;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class CodeSelection {
	@Element
	private String codeselpath=" ";
	@Element
	private String codetext=" ";
	
	public void setCodeSelPath(String codeselpath) {
		this.codeselpath = codeselpath;
	}
	public String getCodeSelPath() {
		return codeselpath;
	}
	public void setCodeText(String codetext) {
		this.codetext = codetext;
	}
	public String getCodeText() {
		return codetext;
	}
	public String toString() {
		return codetext+"/"+codeselpath;
	}
}
