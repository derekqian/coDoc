package edu.pdx.svl.coDoc.cdc.editor;

import java.util.LinkedList;

import org.eclipse.cdt.core.dom.ast.ASTGenericVisitor;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTProblem;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.ASTNode;

public class MyCPPASTVisitor extends ASTGenericVisitor implements IMyASTVisitor {
	private MyASTNode root = null;
	public MyCPPASTVisitor(IASTTranslationUnit tu) {
		super(true);
		root = new MyASTNode(tu);
		shouldVisitTranslationUnit= false;
	}
	public MyASTNode getAST() {
		return root;
	}
	private MyASTNode addRoot(IASTNode node) {
		if(node == null) return null;
		LinkedList<IASTNode> ancients = new LinkedList<IASTNode>();
		IASTNode parent = node.getParent();
		while((parent != null) && !(parent instanceof IASTTranslationUnit)){
			ancients.addLast(parent);
			parent = parent.getParent();
		}
		
		Object[] parents = ancients.toArray();
		
		MyASTNode[] candidates = root.getChildren();
		int j = candidates.length-1;
		outerLoop: for(int i=parents.length-1; i>=0; i--) {
			if(parents[i] != null) {
				for(;j>=0;j--) {
					if(node.getParent() == candidates[j].getData()) {
						MyASTNode temp = new MyASTNode(node);
						candidates[j].addChild(temp);
						return temp;
					}
					if(parents[i] == candidates[j].getData()) {
						candidates = candidates[j].getChildren();
						j = candidates.length - 1;
						continue outerLoop;
					}
				}
			}
		}
		
		MyASTNode temp = new MyASTNode(node);
		root.addChild(temp);
		return temp;
	}
	@Override
	public int genericVisit(IASTNode node) {
		if(addRoot(node) == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public void addPreprocessorProblems(IASTProblem[] problems) {
		for(IASTProblem prob : problems) {
			if(prob instanceof ASTNode) {
				addRoot(prob);
			}
		}
	}
	@Override
	public void addComments(IASTComment[] comments) {
		for(IASTComment comment : comments) {
			if(comment instanceof ASTNode) {
				addRoot(comment);
			}
		}
	}
	@Override
	public void addPreprocessorStatements(IASTPreprocessorStatement[] statements) {
		MyASTNode[] stmtNodes = new MyASTNode[statements.length];
		for(int i=0; i<statements.length; i++) {
			if(statements[i] instanceof ASTNode) {
				stmtNodes[i] = addRoot(statements[i]);
			}
		}
		for(MyASTNode temp : stmtNodes) {
			if(temp.getData() instanceof IASTPreprocessorIncludeStatement) {
				final String path = ((IASTPreprocessorIncludeStatement) temp.getData()).getPath();
				MyASTNode[] candidates = root.getChildren();
				for(MyASTNode node : candidates) {
					if(node!=null && node.getData().getContainingFilename().equals(path)) {
						root.removeChild(node);
						// temp.addChild(node);
					}
				}
			}
		}
	}
	@Override
	public void reformAST() {
		MyASTNode[] nodes = root.getChildren();
		
		for(int i=0; i<nodes.length; i++) {
			if(nodes[i] != null) {
				int j;
				MyASTNode parent = root;
				MyASTNode[] parents = root.getChildren();
				for(j=0; j<parents.length; j++) {
					if(nodes[i].inside(parents[j])) {
						parent = parents[j];
						parents = parents[j].getChildren();
						j = -1;
						continue;
					}
				}
				if(j == parents.length) {
					// System.out.println(nodes[i].toString()+"    -->    "+parent.toString());
					MyASTNode preparent = nodes[i].getParent();
					parent.addChild(nodes[i]);
					preparent.removeChild(nodes[i]);
				}
			}
		}
	}
}
