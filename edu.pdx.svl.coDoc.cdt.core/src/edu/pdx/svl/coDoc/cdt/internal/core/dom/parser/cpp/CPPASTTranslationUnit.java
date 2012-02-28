/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp;

import edu.pdx.svl.coDoc.cdt.core.CCorePlugin;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOM;
import edu.pdx.svl.coDoc.cdt.core.dom.IPDOMResolver;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.ASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.DOMException;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTArrayDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTArrayModifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTFileLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNode;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNodeLocation;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPointerOperator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorMacroDefinition;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTPreprocessorStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTProblem;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBasicType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IFunctionType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IMacroBinding;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IParameter;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.IType;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICASTDesignator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.CPPASTVisitor;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTConstructorChainInitializer;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTFunctionTryBlockDeclarator;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTOperatorName;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPNamespace;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPScope;
import edu.pdx.svl.coDoc.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import edu.pdx.svl.coDoc.cdt.core.model.ILanguage;
import edu.pdx.svl.coDoc.cdt.core.parser.ParserLanguage;
import edu.pdx.svl.coDoc.cdt.core.parser.ast.IASTEnumerator;
import edu.pdx.svl.coDoc.cdt.core.parser.util.ArrayUtil;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTNode;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.ASTPreprocessorSelectionResult;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IASTAmbiguityParent;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.IRequiresLocationInformation;
import edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.GCCBuiltinSymbolProvider.CPPBuiltinParameter;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.ILocationResolver;
import edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.InvalidPreprocessorNodeException;
import edu.pdx.svl.coDoc.cdt.internal.core.pdom.PDOM;
import org.eclipse.core.runtime.CoreException;

/**
 * @author jcamelon
 */
public class CPPASTTranslationUnit extends CPPASTNode implements
		ICPPASTTranslationUnit, IRequiresLocationInformation,
		IASTAmbiguityParent {
	private IASTDeclaration[] decls = new IASTDeclaration[32];

	private ICPPNamespace binding = null;

	private ICPPScope scope = null;

	private ILocationResolver resolver;

	private IPDOM pdom;

	private static final IASTPreprocessorStatement[] EMPTY_PREPROCESSOR_STATEMENT_ARRAY = new IASTPreprocessorStatement[0];

	private static final IASTNodeLocation[] EMPTY_PREPROCESSOR_LOCATION_ARRAY = new IASTNodeLocation[0];

	private static final IASTPreprocessorMacroDefinition[] EMPTY_PREPROCESSOR_MACRODEF_ARRAY = new IASTPreprocessorMacroDefinition[0];

	private static final IASTPreprocessorIncludeStatement[] EMPTY_PREPROCESSOR_INCLUSION_ARRAY = new IASTPreprocessorIncludeStatement[0];

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final IASTProblem[] EMPTY_PROBLEM_ARRAY = new IASTProblem[0];

	private static final IASTName[] EMPTY_NAME_ARRAY = new IASTName[0];

	public IASTTranslationUnit getTranslationUnit() {
		return this;
	}

	public void addDeclaration(IASTDeclaration d) {
		decls = (IASTDeclaration[]) ArrayUtil.append(IASTDeclaration.class,
				decls, d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getDeclarations()
	 */
	public IASTDeclaration[] getDeclarations() {
		if (decls == null)
			return IASTDeclaration.EMPTY_DECLARATION_ARRAY;
		return (IASTDeclaration[]) ArrayUtil.removeNulls(IASTDeclaration.class,
				decls);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getScope()
	 */
	public IScope getScope() {
		if (scope == null) {
			scope = new CPPNamespaceScope(this);
			addBuiltinOperators(scope);
		}

		return scope;
	}

	private void addBuiltinOperators(IScope theScope) {
		// void
		IType cpp_void = new CPPBasicType(IBasicType.t_void, 0);
		// void *
		IType cpp_void_p = new GPPPointerType(new CPPQualifierType(
				new CPPBasicType(IBasicType.t_void, 0), false, false),
				new GPPASTPointer());
		// size_t // assumed: unsigned long int
		IType cpp_size_t = new CPPBasicType(IBasicType.t_int,
				CPPBasicType.IS_LONG & CPPBasicType.IS_UNSIGNED);

		// void * operator new (std::size_t);
		IBinding temp = null;
		IType[] newParms = new IType[1];
		newParms[0] = cpp_size_t;
		IFunctionType newFunctionType = new CPPFunctionType(cpp_void_p,
				newParms);
		IParameter[] newTheParms = new IParameter[1];
		newTheParms[0] = new CPPBuiltinParameter(newParms[0]);
		temp = new CPPImplicitFunction(ICPPASTOperatorName.OPERATOR_NEW,
				theScope, newFunctionType, newTheParms, false);
		try {
			theScope.addBinding(temp);
		} catch (DOMException de) {
		}

		// void * operator new[] (std::size_t);
		temp = null;
		temp = new CPPImplicitFunction(ICPPASTOperatorName.OPERATOR_NEW_ARRAY,
				theScope, newFunctionType, newTheParms, false);
		try {
			theScope.addBinding(temp);
		} catch (DOMException de) {
		}

		// void operator delete(void*);
		temp = null;
		IType[] deleteParms = new IType[1];
		deleteParms[0] = cpp_size_t;
		IFunctionType deleteFunctionType = new CPPFunctionType(cpp_void,
				deleteParms);
		IParameter[] deleteTheParms = new IParameter[1];
		deleteTheParms[0] = new CPPBuiltinParameter(deleteParms[0]);
		temp = new CPPImplicitFunction(ICPPASTOperatorName.OPERATOR_DELETE,
				theScope, deleteFunctionType, deleteTheParms, false);
		try {
			theScope.addBinding(temp);
		} catch (DOMException de) {
		}

		// void operator delete[](void*);
		temp = null;
		temp = new CPPImplicitFunction(
				ICPPASTOperatorName.OPERATOR_DELETE_ARRAY, theScope,
				deleteFunctionType, deleteTheParms, false);
		try {
			theScope.addBinding(temp);
		} catch (DOMException de) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getDeclarations(edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding)
	 */
	public IASTName[] getDeclarations(IBinding b) {
		if (b instanceof IMacroBinding) {
			if (resolver == null)
				return EMPTY_NAME_ARRAY;
			return resolver.getDeclarations((IMacroBinding) b);
		}
		IASTName[] names = CPPVisitor.getDeclarations(this, b);
		if (names.length == 0 && pdom != null) {
			try {
				b = ((PDOM) pdom).getLinkage(getLanguage()).adaptBinding(b);
				if (binding != null)
					names = ((IPDOMResolver) pdom
							.getAdapter(IPDOMResolver.class))
							.getDeclarations(b);
			} catch (CoreException e) {
				CCorePlugin.log(e);
				return names;
			}
		}

		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getDefinitions(edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding)
	 */
	public IASTName[] getDefinitions(IBinding binding) {
		if (binding instanceof IMacroBinding) {
			if (resolver == null)
				return EMPTY_NAME_ARRAY;
			return resolver.getDeclarations((IMacroBinding) binding);
		}

		IASTName[] names = CPPVisitor.getDeclarations(this, binding);
		for (int i = 0; i < names.length; i++) {
			if (!names[i].isDefinition())
				names[i] = null;
		}
		names = (IASTName[]) ArrayUtil.removeNulls(IASTName.class, names);

		if (names.length == 0 && pdom != null) {
			try {
				binding = ((PDOM) pdom).getLinkage(getLanguage()).adaptBinding(
						binding);
				if (binding != null)
					names = ((IPDOMResolver) pdom
							.getAdapter(IPDOMResolver.class))
							.getDefinitions(binding);
			} catch (CoreException e) {
				CCorePlugin.log(e);
				return names;
			}
		}

		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getReferences(edu.pdx.svl.coDoc.cdt.core.dom.ast.IBinding)
	 */
	public IASTName[] getReferences(IBinding b) {
		if (b instanceof IMacroBinding) {
			if (resolver == null)
				return EMPTY_NAME_ARRAY;
			return resolver.getReferences((IMacroBinding) b);
		}
		return CPPVisitor.getReferences(this, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getLocationInfo(int,
	 *      int)
	 */
	public IASTNodeLocation[] getLocationInfo(int offset, int length) {
		if (resolver == null)
			return EMPTY_PREPROCESSOR_LOCATION_ARRAY;
		return resolver.getLocations(offset, length);
	}

	private class CPPFindNodeForOffsetAction extends CPPASTVisitor {
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
			shouldVisitBaseSpecifiers = true;
			shouldVisitNamespaces = true;
		}

		IASTNode foundNode = null;

		int offset = 0;

		int length = 0;

		/**
		 * 
		 */
		public CPPFindNodeForOffsetAction(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}

		public int processNode(IASTNode node) {
			if (foundNode != null)
				return PROCESS_ABORT;

			if (node instanceof ASTNode
					&& ((ASTNode) node).getOffset() == offset
					&& ((ASTNode) node).getLength() == length) {
				foundNode = node;
				return PROCESS_ABORT;
			}

			// skip the rest of this node if the selection is outside of its
			// bounds
			// TODO take out fix below for bug 86993 check for: !(node
			// instanceof ICPPASTLinkageSpecification)
			if (node instanceof ASTNode
					&& !(node instanceof ICPPASTLinkageSpecification)
					&& offset > ((ASTNode) node).getOffset()
							+ ((ASTNode) node).getLength())
				return PROCESS_SKIP;

			return PROCESS_CONTINUE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPVisitor.CPPBaseVisitorAction#processDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclaration)
		 */
		public int visit(IASTDeclaration declaration) {
			// use declarations to determine if the search has gone past the
			// offset (i.e. don't know the order the visitor visits the nodes)
			// TODO take out fix below for bug 86993 check for: !(declaration
			// instanceof ICPPASTLinkageSpecification)
			if (declaration instanceof ASTNode
					&& !(declaration instanceof ICPPASTLinkageSpecification)
					&& ((ASTNode) declaration).getOffset() > offset)
				return PROCESS_ABORT;

			return processNode(declaration);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.cpp.CPPVisitor.CPPBaseVisitorAction#processDeclarator(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclarator)
		 */
		public int visit(IASTDeclarator declarator) {
			int ret = processNode(declarator);

			IASTPointerOperator[] ops = declarator.getPointerOperators();
			for (int i = 0; i < ops.length; i++)
				processNode(ops[i]);

			if (declarator instanceof IASTArrayDeclarator) {
				IASTArrayModifier[] mods = ((IASTArrayDeclarator) declarator)
						.getArrayModifiers();
				for (int i = 0; i < mods.length; i++)
					processNode(mods[i]);
			}

			if (declarator instanceof ICPPASTFunctionDeclarator) {
				ICPPASTConstructorChainInitializer[] chainInit = ((ICPPASTFunctionDeclarator) declarator)
						.getConstructorChain();
				for (int i = 0; i < chainInit.length; i++) {
					processNode(chainInit[i]);
				}

				if (declarator instanceof ICPPASTFunctionTryBlockDeclarator) {
					ICPPASTCatchHandler[] catchHandlers = ((ICPPASTFunctionTryBlockDeclarator) declarator)
							.getCatchHandlers();
					for (int i = 0; i < catchHandlers.length; i++) {
						processNode(catchHandlers[i]);
					}
				}
			}

			return ret;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processDesignator(edu.pdx.svl.coDoc.cdt.core.dom.ast.c.ICASTDesignator)
		 */
		public int processDesignator(ICASTDesignator designator) {
			return processNode(designator);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processDeclSpecifier(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTDeclSpecifier)
		 */
		public int visit(IASTDeclSpecifier declSpec) {
			return processNode(declSpec);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processEnumerator(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator)
		 */
		public int visit(IASTEnumerator enumerator) {
			return processNode((IASTNode) enumerator);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processExpression(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTExpression)
		 */
		public int visit(IASTExpression expression) {
			return processNode(expression);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processInitializer(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTInitializer)
		 */
		public int visit(IASTInitializer initializer) {
			return processNode(initializer);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processName(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTName)
		 */
		public int visit(IASTName name) {
			if (name.toString() != null)
				return processNode(name);
			return PROCESS_CONTINUE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processParameterDeclaration(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTParameterDeclaration)
		 */
		public int visit(IASTParameterDeclaration parameterDeclaration) {
			return processNode(parameterDeclaration);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processStatement(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTStatement)
		 */
		public int visit(IASTStatement statement) {
			return processNode(statement);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.pdx.svl.coDoc.cdt.internal.core.dom.parser.c.CVisitor.CBaseVisitorAction#processTypeId(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTypeId)
		 */
		public int visit(IASTTypeId typeId) {
			return processNode(typeId);
		}

		public IASTNode getNode() {
			return foundNode;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getNodeForLocation(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNodeLocation)
	 */
	public IASTNode selectNodeForLocation(String path, int realOffset,
			int realLength) {
		IASTNode node = null;
		ASTPreprocessorSelectionResult result = null;
		int globalOffset = 0;

		try {
			result = resolver.getPreprocessorNode(path, realOffset, realLength);
		} catch (InvalidPreprocessorNodeException ipne) {
			globalOffset = ipne.getGlobalOffset();
		}

		if (result != null && result.getSelectedNode() != null) {
			node = result.getSelectedNode();
		} else {
			// use the globalOffset to get the node from the AST if it's valid
			globalOffset = result == null ? globalOffset : result
					.getGlobalOffset();
			if (globalOffset >= 0) {
				CPPFindNodeForOffsetAction nodeFinder = new CPPFindNodeForOffsetAction(
						globalOffset, realLength);
				accept(nodeFinder);
				node = nodeFinder.getNode();
			}
		}

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getMacroDefinitions()
	 */
	public IASTPreprocessorMacroDefinition[] getMacroDefinitions() {
		if (resolver == null)
			return EMPTY_PREPROCESSOR_MACRODEF_ARRAY;
		IASTPreprocessorMacroDefinition[] result = resolver
				.getMacroDefinitions();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getIncludeDirectives()
	 */
	public IASTPreprocessorIncludeStatement[] getIncludeDirectives() {
		if (resolver == null)
			return EMPTY_PREPROCESSOR_INCLUSION_ARRAY;
		IASTPreprocessorIncludeStatement[] result = resolver
				.getIncludeDirectives();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getAllPreprocessorStatements()
	 */
	public IASTPreprocessorStatement[] getAllPreprocessorStatements() {
		if (resolver == null)
			return EMPTY_PREPROCESSOR_STATEMENT_ARRAY;
		IASTPreprocessorStatement[] result = resolver
				.getAllPreprocessorStatements();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.internal.core.parser2.IRequiresLocationInformation#setLocationResolver(edu.pdx.svl.coDoc.cdt.internal.core.parser.scanner2.ILocationResolver)
	 */
	public void setLocationResolver(ILocationResolver resolver) {
		this.resolver = resolver;
		resolver.setRootNode(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.cpp.ICPPASTTranslationUnit#resolveBinding()
	 */
	public IBinding resolveBinding() {
		if (binding == null)
			binding = new CPPNamespace(this);
		return binding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getPreprocesorProblems()
	 */
	public IASTProblem[] getPreprocessorProblems() {
		if (resolver == null)
			return EMPTY_PROBLEM_ARRAY;
		IASTProblem[] result = resolver.getScannerProblems();
		for (int i = 0; i < result.length; ++i) {
			IASTProblem p = result[i];
			p.setParent(this);
			p.setPropertyInParent(IASTTranslationUnit.SCANNER_PROBLEM);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getUnpreprocessedSignature(edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTNodeLocation[])
	 */
	public String getUnpreprocessedSignature(IASTNodeLocation[] locations) {
		if (resolver == null)
			return EMPTY_STRING;
		return new String(resolver.getUnpreprocessedSignature(locations));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pdx.svl.coDoc.cdt.core.dom.ast.IASTTranslationUnit#getFilePath()
	 */
	public String getFilePath() {
		if (resolver == null)
			return EMPTY_STRING;
		return new String(resolver.getTranslationUnitPath());
	}

	public boolean accept(ASTVisitor action) {
		if (action.shouldVisitTranslationUnit) {
			switch (action.visit(this)) {
			case ASTVisitor.PROCESS_ABORT:
				return false;
			case ASTVisitor.PROCESS_SKIP:
				return true;
			default:
				break;
			}
		}
		IASTDeclaration[] ds = getDeclarations();
		for (int i = 0; i < ds.length; i++) {
			if (!ds[i].accept(action))
				return false;
		}
		return true;
	}

	public IASTFileLocation flattenLocationsToFile(
			IASTNodeLocation[] nodeLocations) {
		if (resolver == null)
			return null;
		return resolver.flattenLocations(nodeLocations);
	}

	public IASTName[] getMacroExpansions() {
		if (resolver == null)
			return EMPTY_NAME_ARRAY;
		return resolver.getMacroExpansions();
	}

	public IDependencyTree getDependencyTree() {
		if (resolver == null)
			return null;
		return resolver.getDependencyTree();
	}

	public String getContainingFilename(int offset) {
		if (resolver == null)
			return EMPTY_STRING;
		return resolver.getContainingFilename(offset);
	}

	public void replace(IASTNode child, IASTNode other) {
		if (decls == null)
			return;
		for (int i = 0; i < decls.length; ++i) {
			if (decls[i] == null)
				continue;
			if (decls[i] == child) {
				other.setParent(child.getParent());
				other.setPropertyInParent(child.getPropertyInParent());
				decls[i] = (IASTDeclaration) other;
			}
		}
	}

	public ParserLanguage getParserLanguage() {
		return ParserLanguage.CPP;
	}

	public ILanguage getLanguage() {
		// Assuming gnu C++ for now.
		return new GPPLanguage();
	}

	public IPDOM getIndex() {
		return pdom;
	}

	public void setIndex(IPDOM pdom) {
		this.pdom = pdom;
	}

}
