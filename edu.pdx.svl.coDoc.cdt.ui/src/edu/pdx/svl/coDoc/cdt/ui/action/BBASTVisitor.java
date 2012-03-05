package edu.pdx.svl.coDoc.cdt.ui.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;

public class BBASTVisitor extends ASTVisitor {

	{
		shouldVisitNames = true; //
		shouldVisitDeclarations = true;
		shouldVisitInitializers = true;
		shouldVisitParameterDeclarations = true;
		shouldVisitDeclarators = true;
		shouldVisitDeclSpecifiers = true;
		shouldVisitExpressions = true;
		shouldVisitStatements = true; //
		shouldVisitTypeIds = true;
		shouldVisitEnumerators = true;
		shouldVisitTranslationUnit = true;
		shouldVisitProblems = true;
	}

	private String results = "";
	
	private String name = "/home/derek/Research/ast.dot";
	private File dotfile = null;
	private FileOutputStream os = null;

	public String getText() {
		return results;
	}
	
	public void writeHead(String head) {
		try {
			File dotfile = new File(name);
			FileOutputStream os = null;
			if(dotfile.exists()) {
				os = new FileOutputStream(dotfile,false);
				os.write(head.getBytes());
				os.close();
			}
			os = null;
			dotfile = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void writeBody(String body) {
		try {
			File dotfile = new File(name);
			FileOutputStream os = null;
			if(dotfile.exists()) {
				os = new FileOutputStream(dotfile,true);
				os.write(body.getBytes());
				os.close();
			}
			os = null;
			dotfile = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void writeTail(String tail) {
		try {
			File dotfile = new File(name);
			FileOutputStream os = null;
			if(dotfile.exists()) {
				os = new FileOutputStream(dotfile,true);
				os.write(tail.getBytes());
				os.close();
			}
			os = null;
			dotfile = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public String getNameOfClass(Object obj) {
		String str = obj.getClass().getName();
		int index = str.lastIndexOf('.');
		return str.substring(index+1);
	}

	public int visit(IASTTranslationUnit tu) {
		System.out.println(tu);
		writeBody("node"+Integer.toHexString(tu.hashCode())
				+"[label="+getNameOfClass(tu)+"_"+((ASTNode)tu).getOffset()+"_"+((ASTNode)tu).getLength()+"];\n");
		if(tu.getParent() != null) {
			writeBody("node"+Integer.toHexString(tu.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(tu.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTName name) {
		System.out.println(name);
		writeBody("node"+Integer.toHexString(name.hashCode())
				+"[label="+getNameOfClass(name)+"];\n");
		if(name.getParent() != null) {
			writeBody("node"+Integer.toHexString(name.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(name.hashCode())+";\n");
		}
		try {
			results += "NAME: " + name.getRawSignature() + "\n";
			return PROCESS_CONTINUE;
		} catch (Throwable e) {
			return PROCESS_ABORT;
		}
	};

	public int visit(IASTDeclaration declaration) {
		System.out.println(declaration);
		writeBody("node"+Integer.toHexString(declaration.hashCode())
				+"[label="+getNameOfClass(declaration)+"_"+((ASTNode)declaration).getOffset()+"_"+((ASTNode)declaration).getLength()+"];\n");
		if(declaration.getParent() != null) {
			writeBody("node"+Integer.toHexString(declaration.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(declaration.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTInitializer initializer) {
		System.out.println(initializer);
		writeBody("node"+Integer.toHexString(initializer.hashCode())+"[label="+getNameOfClass(initializer)+"];\n");
		if(initializer.getParent() != null) {
			writeBody("node"+Integer.toHexString(initializer.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(initializer.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTParameterDeclaration parameterDeclaration) {
		System.out.println(parameterDeclaration);
		writeBody("node"+Integer.toHexString(parameterDeclaration.hashCode())+"[label="+getNameOfClass(parameterDeclaration)+"];\n");
		if(parameterDeclaration.getParent() != null) {
			writeBody("node"+Integer.toHexString(parameterDeclaration.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(parameterDeclaration.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTDeclarator declarator) {
		System.out.println(declarator);
		writeBody("node"+Integer.toHexString(declarator.hashCode())+"[label="+getNameOfClass(declarator)+"];\n");
		if(declarator.getParent() != null) {
			writeBody("node"+Integer.toHexString(declarator.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(declarator.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTDeclSpecifier declSpec) {
		System.out.println(declSpec);
		writeBody("node"+Integer.toHexString(declSpec.hashCode())+"[label="+getNameOfClass(declSpec)+"];\n");
		if(declSpec.getParent() != null) {
			writeBody("node"+Integer.toHexString(declSpec.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(declSpec.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTExpression expression) {
		System.out.println(expression);
		writeBody("node"+Integer.toHexString(expression.hashCode())+"[label="+getNameOfClass(expression)+"];\n");
		if(expression.getParent() != null) {
			writeBody("node"+Integer.toHexString(expression.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(expression.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTStatement statement) {
		System.out.println(statement);
		writeBody("node"+Integer.toHexString(statement.hashCode())+"[label="+getNameOfClass(statement)+"];\n");
		if(statement.getParent() != null) {
			writeBody("node"+Integer.toHexString(statement.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(statement.hashCode())+";\n");
		}
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
	};

	public int visit(IASTTypeId typeId) {
		System.out.println(typeId);
		writeBody("node"+Integer.toHexString(typeId.hashCode())+"[label="+getNameOfClass(typeId)+"];\n");
		if(typeId.getParent() != null) {
			writeBody("node"+Integer.toHexString(typeId.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(typeId.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTEnumerator enumerator) {
		System.out.println(enumerator);
		writeBody("node"+Integer.toHexString(enumerator.hashCode())+"[label="+getNameOfClass(enumerator)+"];\n");
		if(enumerator.getParent() != null) {
			writeBody("node"+Integer.toHexString(enumerator.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(enumerator.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTProblem problem) {
		System.out.println(problem);
		writeBody("node"+Integer.toHexString(problem.hashCode())+"[label="+getNameOfClass(problem)+"];\n");
		if(problem.getParent() != null) {
			writeBody("node"+Integer.toHexString(problem.getParent().hashCode())+" -> "
					+"node"+Integer.toHexString(problem.hashCode())+";\n");
		}
		return PROCESS_CONTINUE;
	}
}
