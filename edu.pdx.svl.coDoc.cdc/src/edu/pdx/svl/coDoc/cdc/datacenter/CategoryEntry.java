/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

public class CategoryEntry extends BaseEntry {
	public String name;
	public String path;
	public CategoryEntry(String uuid, String time, String os, String creater, String name, String path) {
		super(uuid, time, os, creater);
		this.name = name;
		this.path = path;
	}
}
