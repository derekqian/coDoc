/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

public class LinkEntry extends BaseEntry {
	public String codefilename;
	public CodeSelection codeselpath;
	public String specfilename;
	public SpecSelection specselpath;
	public String comment;
	public LinkEntry(String uuid, String time, String os, String creater, 
			String codefilename, CodeSelection codeselpath, String specfilename, 
			SpecSelection specselpath, String comment) {
		super(uuid, time, os, creater);
		this.codefilename = codefilename;
		this.codeselpath = codeselpath;
		this.specfilename = specfilename;
		this.specselpath = specselpath;
		this.comment = comment;
	}
}
