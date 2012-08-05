package edu.pdx.svl.coDoc.cdc.editor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.ASTNode;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.TextSelection;

public class MyASTTree {
	private ITranslationUnit tu = null;
	private MyASTNode myAST = null;
	private MyASTNode[] myASTLeaf = null;
	
	public MyASTTree(ITranslationUnit tu) {
		this.tu = tu;
		myAST = buildMyAST(tu);
		myASTLeaf = buildMyASTLeaf();
	}
	public MyASTNode getTree() {
		return myAST;
	}
	public MyASTNode[] getLeaf() {
		return myASTLeaf;
	}
	
	private MyASTNode buildMyAST(ITranslationUnit tu) {
		IASTTranslationUnit aTu = null;
		ASTVisitor astVisitor = null;
		try {
			aTu = tu.getAST();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if(aTu instanceof CPPASTTranslationUnit) {
			astVisitor = new MyCPPASTVisitor(aTu);
		} else if(aTu instanceof CASTTranslationUnit) {
			astVisitor = new MyCASTVisitor(aTu);
		}
		aTu.accept(astVisitor);
		//IASTProblem[] prob = aTu.getPreprocessorProblems();
		//((IMyASTVisitor) astVisitor).addPreprocessorProblems(prob);
		IASTComment[] comments = aTu.getComments();
		((IMyASTVisitor) astVisitor).addComments(comments);
		IASTPreprocessorStatement[] prepStatement = aTu.getAllPreprocessorStatements();
		((IMyASTVisitor) astVisitor).addPreprocessorStatements(prepStatement);
		((IMyASTVisitor) astVisitor).reformAST();
		return ((IMyASTVisitor) astVisitor).getAST();
	}
	
	private MyASTNode[] buildMyASTLeaf() {
		if(myAST  == null) {
			return null;			
		}
		MyASTNode[] leafArray = null;
		Vector<MyASTNode> astLeaf = new Vector<MyASTNode>();
		Stack<MyASTNode> stack = new Stack<MyASTNode>();
		stack.push(myAST);
		while(!stack.empty()) {
			MyASTNode node = stack.pop();
			if(node.hasChildren()) {
				MyASTNode[] children = node.getChildren();
				for(int i=children.length-1; i>=0; i--) {
					stack.push(children[i]);
				}
			} else {
				astLeaf.add(node);
			}
		}
		leafArray = new MyASTNode[astLeaf.size()];
		System.arraycopy(astLeaf.toArray(), 0, leafArray, 0, astLeaf.size());
		for(int i=0; i<leafArray.length; i++) {
			System.out.println(leafArray[i].getData().getFileLocation().getNodeOffset() + " -> " + (leafArray[i].getData().getFileLocation().getNodeOffset()+leafArray[i].getData().getFileLocation().getNodeLength()) + leafArray[i].getData().getRawSignature());
		}
		return leafArray;
	}
	
	public TextSelection adjustSelection0(TextSelection selection) {
		int i;
		MyASTNode parent = myAST;
		MyASTNode[] nodes = myAST.getChildren();
		for(i=0; i<nodes.length; i++) {
			//System.out.println(selection.getOffset() + "  <-->  " + (selection.getOffset()+selection.getLength()));
			//System.out.println(((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() + "  <-->  " + (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()));
			if((((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() <= selection.getOffset())
			 &&((selection.getOffset()+selection.getLength()) <= (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()))) {
				parent = nodes[i];
				nodes = nodes[i].getChildren();
				i = -1;
				continue;
			}
		}
		return new TextSelection(((ASTNode) parent.getData()).getFileLocation().getNodeOffset(), ((ASTNode) parent.getData()).getFileLocation().getNodeLength());
	}
	public TextSelection adjustSelection1(TextSelection selection) {
		int begin = selection.getOffset();
		int end = selection.getOffset() + selection.getLength() -1;

		System.out.printf("adjustSelection1: enter with (%d, %d)\n", begin, end);
		if(end < ((ASTNode) myASTLeaf[0].getData()).getFileLocation().getNodeOffset()) {
			// before the first node
			System.out.println("adjustSelection1: <S,S>(XXX)");
			return selection;
		} else if((((ASTNode) myASTLeaf[myASTLeaf.length-1].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[myASTLeaf.length-1].getData()).getFileLocation().getNodeLength()) <= begin) {
			// after the last node
			System.out.println("adjustSelection1: (XXX)<S,S>");
			return selection;
		} else {
			int i, j;
			// begin
			for(i=0; i<myASTLeaf.length; i++) {
				if(begin < ((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset()) {
					// XXXX(___), begin isn't inside any node, keep it as is.
					System.out.println("adjustSelection1: <begin,(XXX)");
					break;
				} else if((((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset() <= begin)
				&& (begin < (((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeLength()))) {
					// ____(XXXX), begin is inside this node, adjust it to the left boundary
					System.out.println("adjustSelection1: (X<begin,XX)");
					begin = ((ASTNode) myASTLeaf[i].getData()).getFileLocation().getNodeOffset();
					break;
				}
			}
			// end
			for(j=myASTLeaf.length-1; j>=i; j--) {
				if((((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeLength()) <= end) {
					// (___)XXXX, end isn't inside any node, keep it as is.
					System.out.println("adjustSelection1: (XXX),end>");
					break;
				} else if((((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset() <= end)
				&& (end < (((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeLength()))) {
					// (XXXX)____, end is inside this node, adjust it to the left boundary
					System.out.println("adjustSelection1: (XX,end>X)");
					end = ((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeOffset()+((ASTNode) myASTLeaf[j].getData()).getFileLocation().getNodeLength()-1;
					break;
				}
			}
		}
		System.out.printf("adjustSelection1: quit with (%d, %d)", begin, end);		

		return new TextSelection(begin, end-begin+1);
	}
	
	public String selection2Node0(TextSelection selection) {
		int i;
		String retstr = new String();
		MyASTNode parent = myAST;
		retstr += parent;
		MyASTNode[] nodes = myAST.getChildren();
		for(i=0; i<nodes.length; i++) {
			//System.out.println(selection.getOffset() + "  <-->  " + (selection.getOffset()+selection.getLength()));
			//System.out.println(((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() + "  <-->  " + (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()));
			if((((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset() <= selection.getOffset())
			 &&((selection.getOffset()+selection.getLength()) <= (((ASTNode) nodes[i].getData()).getFileLocation().getNodeOffset()+((ASTNode) nodes[i].getData()).getFileLocation().getNodeLength()))) {
				parent = nodes[i];
				retstr = parent + "\\" + retstr;
				nodes = nodes[i].getChildren();
				i = -1;
				continue;
			}
		}
		//selectTextInTextEditor(new TextSelection(((ASTNode) parent.getData()).getOffset(), ((ASTNode) parent.getData()).getLength()));
		retstr = Integer.toHexString(parent.getData().getRawSignature().hashCode()) + "\\" + retstr;
		return retstr;
		// return Integer.toString(selection.getOffset())+"\\"+Integer.toString(selection.getLength());
	}
	public String selection2Node1(TextSelection selection) {
		int i,j;
		int begin, end;
		int offset1=0, offset2=0;
		String retstr = null;
		
		begin = selection.getOffset();
		end = selection.getOffset() + selection.getLength() - 1;
		if(end < myASTLeaf[0].getData().getFileLocation().getNodeOffset()) {
			offset1 = begin - myASTLeaf[0].getData().getFileLocation().getNodeOffset();
			offset2 = end - myASTLeaf[0].getData().getFileLocation().getNodeOffset();
			retstr = Integer.toString(offset1);
			retstr += "\\";
			retstr += Integer.toString(offset2);
		} if(begin >= myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeOffset()+myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeLength()) {
			offset1 = begin - (myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeOffset()+myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeLength()-1);
			offset2 = end - (myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeOffset()+myASTLeaf[myASTLeaf.length-1].getData().getFileLocation().getNodeLength()-1);
			retstr = Integer.toString(offset1);
			retstr += "\\";
			retstr += Integer.toString(offset2);
		} else {
			// begin
			for(i=0; i<myASTLeaf.length; i++) {
				if(begin < myASTLeaf[i].getData().getFileLocation().getNodeOffset()) {
					offset1 = begin - myASTLeaf[i].getData().getFileLocation().getNodeOffset();
					break;
				} else if((begin >= myASTLeaf[i].getData().getFileLocation().getNodeOffset())
						&&(begin < myASTLeaf[i].getData().getFileLocation().getNodeOffset()+myASTLeaf[i].getData().getFileLocation().getNodeLength())) {
					offset1 = 0;
					break;
				}
			}
			if(i == myASTLeaf.length) {
				try {
					throw new Exception("Can't find a value for offset1");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// end
			for(j=myASTLeaf.length-1; j>=i; j--) {
				if(end >= myASTLeaf[j].getData().getFileLocation().getNodeOffset()+myASTLeaf[j].getData().getFileLocation().getNodeLength()) {
					offset2 = end - (myASTLeaf[j].getData().getFileLocation().getNodeOffset()+myASTLeaf[j].getData().getFileLocation().getNodeLength()-1);
					break;
				} else if((end >= myASTLeaf[j].getData().getFileLocation().getNodeOffset())
						&&(end < myASTLeaf[j].getData().getFileLocation().getNodeOffset()+myASTLeaf[j].getData().getFileLocation().getNodeLength())) {
					offset2 = 0;
					break;
				}
			}
			if(j < i) {
				try {
					throw new Exception("Can't find a value for offset2");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// generate return string
			retstr = Integer.toString(offset1);
			retstr += "\\";
			retstr += Integer.toString(offset2);
			for(int k=i; k<=j; k++) {
				retstr += "#";
				MyASTNode node = myASTLeaf[k];
				retstr += Integer.toHexString(node.getData().getRawSignature().hashCode());
				do {
					retstr += "\\";
					retstr += node;
					node = node.getParent();
				} while(node != null);
			}
		}
		return retstr;
	}
	
	public TextSelection node2Selection(String nodestr) {
		if(nodestr == null) return null;
		int index = nodestr.lastIndexOf('\\');
		return new TextSelection(Integer.valueOf(nodestr.substring(0,index)),Integer.valueOf(nodestr.substring(index+1)));
	}
	public TextSelection node2Selection0(String nodestr) {
		Queue<MyASTNode> queue = new LinkedList<MyASTNode>();
		queue.add(myAST);
		while(!queue.isEmpty()) {
			MyASTNode head = queue.remove();
			MyASTNode node = head;
			String str = nodestr;
			int index = str.indexOf('\\');
			String piece = str.substring(0, index);
			System.out.println(Integer.toHexString(node.getData().getRawSignature().hashCode()));
			if(piece.equals(Integer.toHexString(node.getData().getRawSignature().hashCode()))) {
				for(; node != null; node = node.getParent()) {
					str = str.substring(index+1);
					index = str.indexOf('\\');
					if(index == -1) {
						piece = str;
					} else {
						piece = str.substring(0,index);
					}
					if(!piece.equals(node.toString())) {
						break;
					}
				}
			}
			if(node == null) {
				return new TextSelection(((ASTNode)head.getData()).getFileLocation().getNodeOffset(),((ASTNode)head.getData()).getFileLocation().getNodeLength());
			}
			if(head.hasChildren()) {
				for(MyASTNode n : head.getChildren()) {
					queue.add(n);
				}
			}
		}
		//ISelectionProvider selProv = workbenchPart.getSite().getSelectionProvider();
		return null;
	}
	public TextSelection node2Selection1(String nodestr) {
		TextSelection sel = null;
		Vector<String> strvec = new Vector<String>();
		while(nodestr.lastIndexOf('#') != -1) {
			int index = nodestr.lastIndexOf('#');
			strvec.add(0, nodestr.substring(index+1));
			nodestr = nodestr.substring(0, index);
		}
		strvec.add(0,nodestr);
		
		String temp = strvec.get(0);
		int index = temp.indexOf('\\');
		int offset1 = Integer.valueOf(temp.substring(0,index));
		int offset2 = Integer.valueOf(temp.substring(index+1));
		
		if(offset1<0 && offset2<0) {
			IASTFileLocation loc = myASTLeaf[0].getData().getFileLocation();
			sel = new TextSelection(loc.getNodeOffset()+offset1,loc.getNodeOffset()+offset2);
		} else if(offset1>0 && offset2>0) {
			IASTFileLocation loc = myASTLeaf[myASTLeaf.length-1].getData().getFileLocation();
			sel = new TextSelection(loc.getNodeOffset()+loc.getNodeLength()+offset1-1, loc.getNodeOffset()+loc.getNodeLength()+offset2-1);
		} else {
			int i,j;
			for(i=0; i<(myASTLeaf.length-(strvec.size()-1)+1); i++) {
				if(i == 114) {
					i=114;
				}
				for(j=1; j<strvec.size(); j++) {
					temp = strvec.get(j);
					MyASTNode node = myASTLeaf[i+j-1];
					String s = Integer.toHexString(node.getData().getRawSignature().hashCode());
					while(node != null) {
						s += "\\";
						s += node;
						node = node.getParent();
					}
					if(!s.equals(temp)) {
						break;
					}
				}
				if(j == strvec.size()) {
					break;
				}
			}
			if(i != (myASTLeaf.length-(strvec.size()-1)+1)) {
				IASTFileLocation loc1 = myASTLeaf[i].getData().getFileLocation();
				IASTFileLocation loc2 = myASTLeaf[i+(strvec.size()-1)-1].getData().getFileLocation();
				sel = new TextSelection(loc1.getNodeOffset()+offset1, loc2.getNodeOffset()+loc2.getNodeLength()+offset2-loc1.getNodeOffset()-offset1);
			}
		}
		return sel;
	}
}