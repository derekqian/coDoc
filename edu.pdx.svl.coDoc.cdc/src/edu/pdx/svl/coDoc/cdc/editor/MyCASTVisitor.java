package edu.pdx.svl.coDoc.cdc.editor;

import java.util.LinkedList;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTProblem;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import org.eclipse.cdt.core.dom.ast.c.ICASTDesignator;
import org.eclipse.cdt.internal.core.dom.parser.ASTNode;

public class MyCASTVisitor extends ASTVisitor implements IMyASTVisitor {
	{
		shouldVisitNames          = true;
		shouldVisitDeclarations   = true;
		shouldVisitInitializers   = true;
		shouldVisitParameterDeclarations = true;
		shouldVisitDeclarators    = true;
		shouldVisitDeclSpecifiers = true;
		shouldVisitDesignators 	  = true;
		shouldVisitExpressions    = true;
		shouldVisitStatements     = true;
		shouldVisitTypeIds        = true;
		shouldVisitEnumerators    = true;
	}
	private MyASTNode root = null;
	public MyCASTVisitor(IASTTranslationUnit tu) {
		root = new MyASTNode(tu);
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
	public int visit(IASTDeclaration declaration) {
		MyASTNode temp = addRoot(declaration);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTDeclarator declarator) {
		MyASTNode temp = addRoot(declarator);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	public int visit(ICASTDesignator designator) {
		MyASTNode temp = addRoot(designator);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTDeclSpecifier declSpec) {
		MyASTNode temp = addRoot(declSpec);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTEnumerator enumerator) {
		MyASTNode temp = addRoot(enumerator);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTExpression expression) {
		MyASTNode temp = addRoot(expression);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTInitializer initializer) {
		MyASTNode temp = addRoot(initializer);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTName name) {
		MyASTNode temp = addRoot(name);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTParameterDeclaration parameterDeclaration) {
		MyASTNode temp = addRoot(parameterDeclaration);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTStatement statement) {
		MyASTNode temp = addRoot(statement);
		if(temp == null) {
			return PROCESS_ABORT;
		} else {
			return PROCESS_CONTINUE;
		}
	}
	@Override
	public int visit(IASTTypeId typeId) {
		MyASTNode temp = addRoot(typeId);
		if(temp == null) {
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
