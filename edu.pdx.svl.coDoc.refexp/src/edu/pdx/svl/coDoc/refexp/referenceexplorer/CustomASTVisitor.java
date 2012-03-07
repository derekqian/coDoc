package edu.pdx.svl.coDoc.refexp.referenceexplorer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiEditor;

import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTForStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFunctionDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTSimpleDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;
import edu.pdx.svl.coDoc.refexp.Global;

public class CustomASTVisitor extends ASTVisitor {

	{
		shouldVisitNames = true; //
		//shouldVisitDeclarations = true;
		//shouldVisitInitializers = true;
		//shouldVisitParameterDeclarations = true;
		//shouldVisitDeclarators = true;
		//shouldVisitDeclSpecifiers = true;
		//shouldVisitExpressions = true;
		//shouldVisitStatements = true; //
		//shouldVisitTypeIds = true;
		//shouldVisitEnumerators = true;
		//shouldVisitTranslationUnit = true;
		//shouldVisitProblems = true;
	}
	
	public static final int MODE_UNDEFINE = 0;
	public static final int MODE_SELECTION_TO_NODE = 1;
	public static final int MODE_NODE_TO_SELECTION = 2;
	
	private static CustomASTVisitor instance = null;

	private String results = "";
	
	private int mode = MODE_UNDEFINE;
	
	private TextSelection currentTextSelection = null;
	private ITextOperationTarget target = null;
	private ITextViewer viewer = null;
	private TextSelection currentSyntaxSelection = null;
	
	private String name = "/home/derek/Research/ast.dot";
	private File dotfile = null;
	private FileOutputStream os = null;
	
	public static CustomASTVisitor getInstance() {
		if(instance == null) {
			instance = new CustomASTVisitor();
		}
		return instance;
	}
	
	public void getEnviroment() {
		IEditorPart cEditor = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchwindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchwindow.getActivePage();
		if(workbenchPage == null) return;
		IEditorReference[] editorrefs = workbenchPage.findEditors(null,"edu.pdx.svl.coDoc.cdc.editor.EntryEditor",IWorkbenchPage.MATCH_ID);
		if(editorrefs.length != 0)
		{
			MultiEditor editor = (MultiEditor) editorrefs[0].getEditor(false);
			
			IEditorPart[] editors = editor.getInnerEditors();
			for(int i=0; i<editors.length; i++)
			{
				System.out.println(editors[i].getClass().getName());
				if(editors[i].getClass().getName().equals("edu.pdx.svl.coDoc.cdt.internal.ui.editor.CEditor"))
				{
					cEditor = editors[i];
				}
			}
		}
		target = (ITextOperationTarget)cEditor.getAdapter(ITextOperationTarget.class);
		if (target instanceof ITextViewer) {
	        viewer = (ITextViewer)target;
	    } 
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setTextSelection(TextSelection currentTextSelection) {
		this.currentTextSelection = currentTextSelection;
	}
	
	public TextSelection getTextSelection() {
		return currentTextSelection;
	}
	
	public void setSelectedASTNode(String selectednode) {
		results = selectednode;
	}
	
	public String getSelectedASTNode() {
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
		if(viewer == null) return PROCESS_CONTINUE;
		if(mode == MODE_SELECTION_TO_NODE) {
			if(name.getParent().getParent() instanceof IASTFunctionDefinition) {
				IASTFunctionDefinition declaration = (IASTFunctionDefinition)name.getParent().getParent();
				currentSyntaxSelection = new TextSelection(((ASTNode)declaration).getOffset(),((ASTNode)declaration).getLength());
				// select double clicked token
				if(currentTextSelection.getLength() == 0) {
					viewer.invalidateTextPresentation();
				} else if((currentTextSelection.getOffset() >= currentSyntaxSelection.getOffset())
						&&(currentTextSelection.getOffset()+currentTextSelection.getLength() < currentSyntaxSelection.getOffset()+currentSyntaxSelection.getLength())) {
					viewer.invalidateTextPresentation();
					//viewer.setTextColor(new Color(null, 255, 0, 0), currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength(), true);
					TextPresentation presentation = new TextPresentation();
					TextAttribute attr = new TextAttribute(new Color(null, 0, 0, 0),
						      new Color(null, 128, 128, 128), TextAttribute.STRIKETHROUGH);
					presentation.addStyleRange(new StyleRange(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength(), attr.getForeground(),
						      attr.getBackground()));
					viewer.changeTextPresentation(presentation, true);
					//viewer.setSelectedRange(currentSyntaxSelection.getOffset(), currentSyntaxSelection.getLength());
					
					results = name.getRawSignature();
					for(IASTNode node = name; node != null; node = node.getParent()) {
						results += "\\"+getNameOfClass(node);
					}
					System.out.println(results);
				}
			} else if(name.getParent().getParent() instanceof IASTSimpleDeclaration) {
				//viewer.invalidateTextPresentation();
			} else {
				//viewer.invalidateTextPresentation();
				assert(false);
			}
		} else {
			IASTNode node = name;
			String str = results;
			int index = str.indexOf('\\');
			String piece = str.substring(0, index);
			if(piece.equals(name.getRawSignature())) {
				for(node = name; node != null; node = node.getParent()) {
					str = str.substring(index+1);
					index = str.indexOf('\\');
					if(index == -1) {
						piece = str;
					} else {
						piece = str.substring(0,index);
					}
					if(!piece.equals(getNameOfClass(node))) {
						break;
					}
				}
			}
			if(node == null) {
				IASTFunctionDefinition declaration = (IASTFunctionDefinition)name.getParent().getParent();
				currentTextSelection = new TextSelection(((ASTNode)declaration).getOffset(),((ASTNode)declaration).getLength());
			}
		}

		return PROCESS_CONTINUE;
	};

	public int visit(IASTDeclaration declaration) {
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
