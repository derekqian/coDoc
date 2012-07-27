/* 
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import java.util.Vector;

public class EntryNode {
	private Object data = null;
	private EntryNode parent = null;
	private Vector<EntryNode> children = new Vector<EntryNode>();
	
	public EntryNode(Object data) {
		this.data = data;
	}
	public Object getData() {
		return data;
	}
	public void setParent(EntryNode parent) {
		this.parent = parent;
	}
	public EntryNode getParent() {
		return parent;
	}
	/**
	 * Add a child at the beginning.
	 */
	public void addChildA(EntryNode child) {
		children.add(0, child);
		child.setParent(this);
	}
	/**
	 * Add a child at the end.
	 */
	public void addChildZ(EntryNode child) {
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(EntryNode child) {
		children.remove(child);
	}
	/**
	 * Remove all the children.
	 */
	public void clearChildren() {
		children.clear();
	}
	public boolean hasChildren() {
		return (!children.isEmpty());
	}
	public EntryNode[] getChildren() {
		EntryNode[] temp = new EntryNode[children.size()];
		System.arraycopy(children.toArray(), 0, temp, 0, children.size());
		return temp;
	}
}
