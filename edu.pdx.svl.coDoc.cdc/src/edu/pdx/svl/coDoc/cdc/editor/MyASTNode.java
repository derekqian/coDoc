package edu.pdx.svl.coDoc.cdc.editor;

import java.util.ArrayList;

import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.parser.Keywords;
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
		StringBuffer buffer = new StringBuffer();
		String temp = data.getClass().getName();
		int index = temp.lastIndexOf('.');
		buffer.append(temp.substring(index+1));
		
		if(data instanceof IASTFunctionDefinition) {
			String name = getDeclaratorName(((IASTFunctionDefinition)data).getDeclarator());
			if (name != null) {
				buffer.append(":");
				buffer.append(name);
			}
		} else if(data instanceof IASTSimpleDeclaration ) {
			String name = null;
			IASTDeclarator[] decltors = ((IASTSimpleDeclaration)data).getDeclarators();
			if(decltors.length > 0) {
				buffer.append(":");
				for(int i=0; i<decltors.length; i++) {
					name = getDeclaratorName(decltors[i]);
					buffer.append(name);
					
					if(i+1<decltors.length)
						buffer.append(", ");
				}
			} else {
				IASTDeclSpecifier declspec = ((IASTSimpleDeclaration)data).getDeclSpecifier();
				if(declspec != null) {
					buffer.append(":");
				    buffer.append( getSignature(declspec));
				}
			}
		}
		
		return buffer.toString();
		//return (temp.substring(index+1)+" <-> ("+((ASTNode) data).getOffset()+", "+(((ASTNode) data).getOffset()+((ASTNode) data).getLength()-1)+")");
	}
	private String getSignature(IASTDeclSpecifier declSpec) {
		if(declSpec instanceof IASTCompositeTypeSpecifier) {
			StringBuilder buf= new StringBuilder();
			IASTCompositeTypeSpecifier comp = (IASTCompositeTypeSpecifier)declSpec;
			switch(comp.getKey()) {
			case IASTCompositeTypeSpecifier.k_struct:
				buf.append(Keywords.cSTRUCT);
				break;
			case IASTCompositeTypeSpecifier.k_union:
				buf.append(Keywords.cUNION);
				break;
			default:
				buf.append(Keywords.cCLASS);
				break;
			}
			buf.append(' ');
			buf.append(comp.getName().toString());
			return buf.toString();
		} else if(declSpec instanceof IASTEnumerationSpecifier) {
			StringBuilder buf= new StringBuilder();
			IASTEnumerationSpecifier comp = (IASTEnumerationSpecifier) declSpec;
			buf.append(Keywords.cENUM);
			buf.append(' ');
			buf.append(comp.getName().toString());
			return buf.toString();
		}
		String intermed= declSpec.getRawSignature();
		return intermed.replaceAll("\\s+", " ");
	}
	
	private String getDeclaratorName(IASTDeclarator decltor) {
		String name = "";
		while(decltor != null && decltor.getName() != null && decltor.getName().toString() == null) {
			decltor = decltor.getNestedDeclarator();
		}
		if (decltor != null && decltor.getName() != null) {
			name = decltor.getName().toString();
		}
		return name;
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