package edu.pdx.svl.coDoc.cdt.ui.action;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTForStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;

public class BBASTVisitor extends ASTVisitor {

	{
		shouldVisitNames = true;
		shouldVisitDeclarations = true;
		shouldVisitInitializers = true;
		shouldVisitParameterDeclarations = true;
		shouldVisitDeclarators = true;
		shouldVisitDeclSpecifiers = true;
		shouldVisitExpressions = true;
		shouldVisitStatements = true;
		shouldVisitTypeIds = true;
		shouldVisitEnumerators = true;
		shouldVisitTranslationUnit = true;
		shouldVisitProblems = true;
	}

	private String results = "";

	public String getText() {
		return results;
	}

	public int visit(IASTTranslationUnit tu) {
		//System.out.println(tu.getClass().getName());
		System.out.println(tu.toString()+"("+((ASTNode)tu).getOffset()+","+((ASTNode)tu).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTName name) {
		//System.out.println(name.getClass().getName());
		System.out.println(name.getParent().toString());
		System.out.println(name.toString()+"("+((ASTNode)name).getOffset()+","+((ASTNode)name).getLength()+")");
		try {
			results += "NAME: " + name.getRawSignature() + "\n";
			return PROCESS_CONTINUE;
		} catch (Throwable e) {
			return PROCESS_ABORT;
		}
	}

	public int visit(IASTDeclaration declaration) {
		//System.out.println(declaration.getClass().getName());
		System.out.println(declaration.getParent().toString());
		System.out.println(declaration.toString()+"("+((ASTNode)declaration).getOffset()+","+((ASTNode)declaration).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTInitializer initializer) {
		//System.out.println(initializer.getClass().getName());
		System.out.println(initializer.getParent().toString());
		System.out.println(initializer.toString()+"("+((ASTNode)initializer).getOffset()+","+((ASTNode)initializer).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTParameterDeclaration parameterDeclaration) {
		//System.out.println(parameterDeclaration.getClass().getName());
		System.out.println(parameterDeclaration.getParent().toString());
		System.out.println(parameterDeclaration.toString()+"("+((ASTNode)parameterDeclaration).getOffset()+","+((ASTNode)parameterDeclaration).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTDeclarator declarator) {
		//System.out.println(declarator.getClass().getName());
		System.out.println(declarator.getParent().toString());
		System.out.println(declarator.toString()+"("+((ASTNode)declarator).getOffset()+","+((ASTNode)declarator).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTDeclSpecifier declSpec) {
		//System.out.println(declSpec.getClass().getName());
		System.out.println(declSpec.getParent().toString());
		System.out.println(declSpec.toString()+"("+((ASTNode)declSpec).getOffset()+","+((ASTNode)declSpec).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTExpression expression) {
		//System.out.println(expression.getClass().getName());
		System.out.println(expression.getParent().toString());
		System.out.println(expression.toString()+"("+((ASTNode)expression).getOffset()+","+((ASTNode)expression).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTStatement statement) {
		//System.out.println(statement.getClass().getName());
		System.out.println(statement.getParent().toString());
		System.out.println(statement.toString()+"("+((ASTNode)statement).getOffset()+","+((ASTNode)statement).getLength()+")");
		try {
			if (statement instanceof IASTForStatement) {
				results += "FOR initializer: "
						+ ((IASTForStatement) statement)
								.getInitializerStatement().getRawSignature()
						+ "\n";
				results += "FOR condition: "
						+ ((IASTForStatement) statement)
								.getConditionExpression().getRawSignature()
						+ "\n";
				results += "FOR iteration: "
						+ ((IASTForStatement) statement)
								.getIterationExpression().getRawSignature()
						+ "\n";
			}
			return PROCESS_CONTINUE;
		} catch (Throwable e) {
			return PROCESS_ABORT;
		}
	}

	public int visit(IASTTypeId typeId) {
		//System.out.println(typeId.getClass().getName());
		System.out.println(typeId.getParent().toString());
		System.out.println(typeId.toString()+"("+((ASTNode)typeId).getOffset()+","+((ASTNode)typeId).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTEnumerator enumerator) {
		//System.out.println(enumerator.getClass().getName());
		System.out.println(enumerator.getParent().toString());
		System.out.println(enumerator.toString()+"("+((ASTNode)enumerator).getOffset()+","+((ASTNode)enumerator).getLength()+")");
		return PROCESS_CONTINUE;
	}

	public int visit(IASTProblem problem) {
		//System.out.println(problem.getClass().getName());
		System.out.println(problem.getParent().toString());
		System.out.println(problem.toString()+"("+((ASTNode)problem).getOffset()+","+((ASTNode)problem).getLength()+")");
		return PROCESS_CONTINUE;
	}
}
