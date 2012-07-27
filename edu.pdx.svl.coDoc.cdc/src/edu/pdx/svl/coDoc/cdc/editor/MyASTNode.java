package edu.pdx.svl.coDoc.cdc.editor;

import java.util.ArrayList;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.ASTNode;

public class MyASTNode {
	private IASTNode data = null;
	private MyASTNode parent = null;
	private ArrayList<MyASTNode> children = new ArrayList<MyASTNode>();
	
	public MyASTNode(IASTNode data) {
		this.data = data;
	}
	public IASTNode getData() {
		return data;
	}
	private void setParent(MyASTNode parent) {
		this.parent = parent;
	}
	public MyASTNode getParent() {
		return parent;
	}
	public void addChild(MyASTNode node) {
		if(children.isEmpty()) {
			children.add(node);			
		} else {
			int a = 0;
			int z = children.size()-1;
			int x = (a+z)/2;
			while(a<x) {
				if(((ASTNode) node.getData()).getOffset() <= ((ASTNode) children.get(x).getData()).getOffset()) {
					z = x;
				} else {
					a = x;
				}
				x = (a+z)/2;
			}
			if(((ASTNode) node.getData()).getOffset() <= ((ASTNode) children.get(a).getData()).getOffset()) {
				children.add(a, node);
			} else if(((ASTNode) node.getData()).getOffset() <= ((ASTNode) children.get(z).getData()).getOffset()) {
				children.add(z, node);
			} else {
				children.add(node);
			}
		}
		node.setParent(this);
	}
	public void removeChild(MyASTNode node) {
		children.remove(node);
	}
	public boolean hasChildren() {
		return (!children.isEmpty());
	}
	public MyASTNode[] getChildren() {
		MyASTNode[] temp = new MyASTNode[children.size()];
		System.arraycopy(children.toArray(), 0, temp, 0, children.size());
		return temp;
	}
	@Override
	public String toString() {
		String temp = data.getClass().getName();
		int index = temp.lastIndexOf('.');
		return temp.substring(index+1);
		//return (temp.substring(index+1)+" <-> ("+((ASTNode) data).getOffset()+", "+(((ASTNode) data).getOffset()+((ASTNode) data).getLength()-1)+")");
	}
	public boolean inside(MyASTNode node) {
		if((((ASTNode) node.getData()).getOffset() < ((ASTNode) data).getOffset()) 
		&& (((ASTNode) data).getOffset()+((ASTNode) data).getLength() < ((ASTNode) node.getData()).getOffset()+((ASTNode) node.getData()).getLength())) {
			return true;
		} else {
			return false;
		}
	}
}