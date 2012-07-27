/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

public class BaseEntry {
	public String uuid;
	public String time;
	public String os;
	public String creater;
	public BaseEntry(String uuid, String time, String os, String creater) {
		this.uuid = uuid;
		this.time = time;
		this.os = os;
		this.creater = creater;
	}
}
