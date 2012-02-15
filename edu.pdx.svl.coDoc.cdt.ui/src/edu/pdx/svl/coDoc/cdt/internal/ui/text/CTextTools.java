package edu.pdx.svl.coDoc.cdt.internal.ui.text;

import edu.pdx.svl.coDoc.cdt.internal.ui.text.CCodeScanner;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.CCommentScanner;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.CppCodeScanner;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.FastCPartitionScanner;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.ICColorConstants;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.SingleTokenCScanner;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.util.CColorManager;
import edu.pdx.svl.coDoc.cdt.ui.CUIPlugin;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.ICPartitions;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;

public class CTextTools {

	private static final String DEFAULT_PARTITIONING = "__c_partitioning"; //$NON-NLS-1$

	/** The color manager */
	private CColorManager fColorManager;

	/** The C source code scanner */
	private CCodeScanner fCodeScanner;

	/** The C++ source code scanner */
	private CppCodeScanner fCppCodeScanner;

	/** The C partitions scanner */
	private FastCPartitionScanner fPartitionScanner;

	/** The Java multiline comment scanner */
	private CCommentScanner fMultilineCommentScanner;

	/** The Java singleline comment scanner */
	private CCommentScanner fSinglelineCommentScanner;

	/** The Java string scanner */
	private SingleTokenCScanner fStringScanner;

	/** The document partitioning used for the C partitioner */
	private String fDocumentPartitioning = DEFAULT_PARTITIONING;

	/**
	 * Creates a new C text tools collection and eagerly creates and initializes
	 * all members of this collection.
	 */
	public CTextTools() {
		fColorManager = new CColorManager();
		fCodeScanner = new CCodeScanner(fColorManager);
		fCppCodeScanner = new CppCodeScanner(fColorManager);
		fPartitionScanner = new FastCPartitionScanner();

		fMultilineCommentScanner = new CCommentScanner(fColorManager,
				ICColorConstants.C_MULTI_LINE_COMMENT);
		fSinglelineCommentScanner = new CCommentScanner(fColorManager,
				ICColorConstants.C_SINGLE_LINE_COMMENT);
		fStringScanner = new SingleTokenCScanner(fColorManager,
				ICColorConstants.C_STRING);
	}

	/**
	 * Disposes all members of this tools collection.
	 */
	public void dispose() {

		fCodeScanner = null;
		fPartitionScanner = null;

		fMultilineCommentScanner = null;
		fSinglelineCommentScanner = null;
		fStringScanner = null;

		if (fColorManager != null) {
			fColorManager.dispose();
			fColorManager = null;
		}
	}

	/**
	 * Gets the color manager.
	 */
	public CColorManager getColorManager() {
		return fColorManager;
	}

	/**
	 * Gets the code scanner used.
	 */
	public RuleBasedScanner getCCodeScanner() {
		return fCodeScanner;
	}

	/**
	 * Gets the code scanner used.
	 */
	public RuleBasedScanner getCppCodeScanner() {
		return fCppCodeScanner;
	}

	/**
	 * Returns a scanner which is configured to scan C-specific partitions,
	 * which are multi-line comments, and regular C source code.
	 * 
	 * @return a C partition scanner
	 */
	public IPartitionTokenScanner getPartitionScanner() {
		return fPartitionScanner;
	}

	/**
	 * Gets the document provider used.
	 */
	public IDocumentPartitioner createDocumentPartitioner() {

		String[] types = new String[] { ICPartitions.C_MULTILINE_COMMENT,
				ICPartitions.C_SINGLE_LINE_COMMENT, ICPartitions.C_STRING,
				ICPartitions.C_CHARACTER };

		return new FastPartitioner(getPartitionScanner(), types);
	}

	/**
	 * Returns a scanner which is configured to scan C multiline comments.
	 * 
	 * @return a C multiline comment scanner
	 */
	public RuleBasedScanner getMultilineCommentScanner() {
		return fMultilineCommentScanner;
	}

	/**
	 * Returns a scanner which is configured to scan C singleline comments.
	 * 
	 * @return a C singleline comment scanner
	 */
	public RuleBasedScanner getSinglelineCommentScanner() {
		return fSinglelineCommentScanner;
	}

	/**
	 * Returns a scanner which is configured to scan Java strings.
	 * 
	 * @return a Java string scanner
	 */
	public RuleBasedScanner getStringScanner() {
		return fStringScanner;
	}

	/**
	 * Sets up the document partitioner for the given document for the given
	 * partitioning.
	 * 
	 * @param document
	 *            the document to be set up
	 * @param partitioning
	 *            the document partitioning
	 * @since 3.0
	 */
	public void setupCDocumentPartitioner(IDocument document,
			String partitioning) {
		IDocumentPartitioner partitioner = createDocumentPartitioner();
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(partitioning, partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
		partitioner.connect(document);
	}

	/**
	 * Sets up the given document for the default partitioning.
	 * 
	 * @param document
	 *            the document to be set up
	 * @since 3.0
	 */
	public void setupCDocument(IDocument document) {
		setupCDocumentPartitioner(document, fDocumentPartitioning);
	}

	/**
	 * Get the document partitioning used for the C partitioner.
	 * 
	 * @return the document partitioning used for the C partitioner
	 * @since 3.1
	 */
	public String getDocumentPartitioning() {
		return fDocumentPartitioning;
	}

	/**
	 * Set the document partitioning to be used for the C partitioner.
	 * 
	 * @since 3.1
	 */
	public void setDocumentPartitioning(String documentPartitioning) {
		fDocumentPartitioning = documentPartitioning;
	}
}