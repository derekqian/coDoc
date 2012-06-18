package edu.pdx.svl.coDoc.cdc.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
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

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
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
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.model.TranslationUnit;

public class CustomASTVisitor extends ASTVisitor {

	{
		//shouldVisitNames = true; //
		//shouldVisitDeclarations = true;
		//shouldVisitInitializers = true;
		//shouldVisitParameterDeclarations = true;
		//shouldVisitDeclarators = true;
		//shouldVisitDeclSpecifiers = true;
		//shouldVisitExpressions = true;
		shouldVisitStatements = true; //
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
	private TextSelection currentSyntaxSelection = null;
	
	public static CustomASTVisitor getInstance() {
		if(instance == null) {
			instance = new CustomASTVisitor();
		}
		return instance;
	}
	
	public String selection2Node(IFile file, TextSelection selection) {
		try {
			TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault().getCoreModel().create(file);
			IASTTranslationUnit ast = tu.getLanguage().getASTTranslationUnit(tu, ILanguage.AST_SKIP_IF_NO_BUILD_INFO);
			setMode(CustomASTVisitor.MODE_SELECTION_TO_NODE);
			setTextSelection(selection);
			ast.accept(this);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return getSelectedASTNode();
	}
	
	public TextSelection node2Selection(IFile file, String node) {
		try {
			TranslationUnit tu = (TranslationUnit) CCorePlugin.getDefault().getCoreModel().create(file);
			IASTTranslationUnit ast = tu.getLanguage().getASTTranslationUnit(tu, ILanguage.AST_SKIP_IF_NO_BUILD_INFO);
			setMode(CustomASTVisitor.MODE_NODE_TO_SELECTION);
			setSelectedASTNode(node);
			ast.accept(this);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		//ISelectionProvider selProv = workbenchPart.getSite().getSelectionProvider();
		return getTextSelection();
	}
	
	public void setMode(int mode) {
		this.mode = mode;
		if(mode != MODE_SELECTION_TO_NODE) {
			currentTextSelection = null;
		}
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
	
	public String getNameOfClass(Object obj) {
		String str = obj.getClass().getName();
		int index = str.lastIndexOf('.');
		return str.substring(index+1);
	}

	public int visit(IASTTranslationUnit tu) {
		System.out.println(tu);
		if(tu.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTName name) {
		if(mode == MODE_SELECTION_TO_NODE) {
			if(name.getParent().getParent() instanceof IASTFunctionDefinition) {
				IASTFunctionDefinition declaration = (IASTFunctionDefinition)name.getParent().getParent();
				currentSyntaxSelection = new TextSelection(((ASTNode)declaration).getOffset(),((ASTNode)declaration).getLength());
				// select double clicked token
				if(currentTextSelection.getLength() == 0) {
					((EntryEditor) CDCEditor.getActiveEntryEditor()).selectTextInTextEditor(null);
				} else if((currentTextSelection.getOffset() >= currentSyntaxSelection.getOffset())
						&&(currentTextSelection.getOffset()+currentTextSelection.getLength() <= currentSyntaxSelection.getOffset()+currentSyntaxSelection.getLength())) {
					((EntryEditor) CDCEditor.getActiveEntryEditor()).selectTextInTextEditor(currentSyntaxSelection);
					results = Integer.toHexString((name.getRawSignature().hashCode()));
					for(IASTNode node = name; node != null; node = node.getParent()) {
						results += "\\"+getNameOfClass(node);
					}
					currentTextSelection = new TextSelection(currentSyntaxSelection.getOffset(),currentSyntaxSelection.getLength());
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
			if(piece.equals(Integer.toHexString(name.getRawSignature().hashCode()))) {
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
		if(initializer.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTParameterDeclaration parameterDeclaration) {
		System.out.println(parameterDeclaration);
		if(parameterDeclaration.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTDeclarator declarator) {
		System.out.println(declarator);
		if(declarator.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTDeclSpecifier declSpec) {
		System.out.println(declSpec);
		if(declSpec.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTExpression expression) {
		System.out.println(expression);
		if(expression.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTStatement statement) {
		if(mode == MODE_SELECTION_TO_NODE) {
			if(statement.getParent().getParent() instanceof IASTFunctionDefinition) {
				currentSyntaxSelection = new TextSelection(((ASTNode)statement).getOffset(),((ASTNode)statement).getLength());
				// select double clicked token
				if(currentTextSelection.getLength() == 0) {
					((EntryEditor) CDCEditor.getActiveEntryEditor()).selectTextInTextEditor(null);
				} else if((currentTextSelection.getOffset() >= currentSyntaxSelection.getOffset())
						&&(currentTextSelection.getOffset()+currentTextSelection.getLength() <= currentSyntaxSelection.getOffset()+currentSyntaxSelection.getLength())) {
					((EntryEditor) CDCEditor.getActiveEntryEditor()).selectTextInTextEditor(currentSyntaxSelection);
					results = Integer.toHexString(statement.getRawSignature().hashCode());
					for(IASTNode node = statement; node != null; node = node.getParent()) {
						results += "\\"+getNameOfClass(node);
					}
					currentTextSelection = new TextSelection(currentSyntaxSelection.getOffset(),currentSyntaxSelection.getLength());
					System.out.println(statement.getRawSignature());
					System.out.println(results);
				}
			} else if(statement.getParent().getParent() instanceof IASTSimpleDeclaration) {
				//viewer.invalidateTextPresentation();
			} else {
				//viewer.invalidateTextPresentation();
				assert(false);
			}
		} else {
			IASTNode node = statement;
			String str = results;
			int index = str.indexOf('\\');
			String piece = str.substring(0, index);
			if(piece.equals(Integer.toHexString(statement.getRawSignature().hashCode()))) {
				for(node = statement; node != null; node = node.getParent()) {
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
				currentTextSelection = new TextSelection(((ASTNode)statement).getOffset(),((ASTNode)statement).getLength());
			}
		}
		
		if(statement.getParent() != null) {
		}
		try {
			if (statement instanceof IASTForStatement) {
				/*results += "FOR initializer: "
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
						+ "\n";*/
			}
			return PROCESS_CONTINUE;
		} catch (Throwable e) {
			return PROCESS_ABORT;
		}
	};

	public int visit(IASTTypeId typeId) {
		System.out.println(typeId);
		if(typeId.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTEnumerator enumerator) {
		System.out.println(enumerator);
		if(enumerator.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}

	public int visit(IASTProblem problem) {
		System.out.println(problem);
		if(problem.getParent() != null) {
		}
		return PROCESS_CONTINUE;
	}
}
