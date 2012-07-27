package edu.pdx.svl.coDoc.cdc.editor;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTProblem;

public interface IMyASTVisitor {
	public MyASTNode getAST();
	public void addPreprocessorProblems(IASTProblem[] problems);
	public void addPreprocessorStatements(IASTPreprocessorStatement[] statements);
	public void addComments(IASTComment[] comments);
	public void reformAST();
}
