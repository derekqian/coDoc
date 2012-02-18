package edu.pdx.svl.coDoc.cdt.internal.ui.editor;

import java.util.Iterator;

import edu.pdx.svl.coDoc.cdt.core.model.CoreModel;
import edu.pdx.svl.coDoc.cdt.core.model.ITranslationUnit;
import edu.pdx.svl.coDoc.cdt.core.model.IWorkingCopy;
import edu.pdx.svl.coDoc.cdt.internal.core.model.IBufferFactory;
import edu.pdx.svl.coDoc.cdt.ui.CUIPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class CDocumentProvider extends TextFileDocumentProvider
{
	/**
	 * Bundle of all required informations to allow working copy management.
	 */
	static protected class TranslationUnitInfo extends FileInfo {
		public IWorkingCopy fCopy;
	}
	
	/**
	 * Creates a translation unit from the given file.
	 * 
	 * @param file
	 *            the file from which to create the translation unit
	 */
	protected ITranslationUnit createTranslationUnit(IFile file) {
		Object element = CoreModel.getDefault().create(file);
		if (element instanceof ITranslationUnit) {
			return (ITranslationUnit) element;
		}
		return null;
	}
	
  /*
   * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#createEmptyFileInfo()
   */
  protected FileInfo createEmptyFileInfo() {
    return new TranslationUnitInfo();
  }
  
	/*
	 * @see org.eclipse.ui.editors.text.TextFileDocumentProvider#createFileInfo(java.lang.Object)
	 */
	protected FileInfo createFileInfo(Object element) throws CoreException {
		ITranslationUnit original = null;
		if (element instanceof IFileEditorInput) {
			/*
			IPath path = new Path("/test/code/sample.c");
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			//System.out.println(workspace.getClass().getName());
			//org.eclipse.core.internal.resources.Workspace

			IWorkspaceRoot workspaceroot = workspace.getRoot();
			IFile file = (IFile) workspaceroot.getFile(path);
			//IProject project = workspaceroot.getProject();
			//IFile file = (IFile) project.findMember(path);
			//System.out.println(file.getClass().getName());

			//System.out.println(input.getClass().getName());
			//org.eclipse.ui.part.FileEditorInput
			FileEditorInput input = new FileEditorInput(file);
			 */
			IFileEditorInput input = (IFileEditorInput)element;
			original = createTranslationUnit(input.getFile());
      System.out.println("Creating a TranslationUnit");
		}
		else return null;
    
		FileInfo info = super.createFileInfo(element);
		if (!(info instanceof TranslationUnitInfo))
			return null;
		TranslationUnitInfo tuInfo = (TranslationUnitInfo) info;
		setUpSynchronization(tuInfo);    
    
		IWorkingCopy copy = null;
		if (element instanceof IFileEditorInput) {
			IBufferFactory factory = CUIPlugin.getDefault().getBufferFactory();     
			copy = original.getSharedWorkingCopy(getProgressMonitor(), factory);
		}
    System.out.println("Creating a WorkingCopy");
		tuInfo.fCopy = copy;
		return tuInfo;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.ICompilationUnitDocumentProvider#getWorkingCopy(java.lang.Object)
	 */
	public IWorkingCopy getWorkingCopy(Object element) {
		FileInfo fileInfo = getFileInfo(element);
		if (fileInfo instanceof TranslationUnitInfo) {
			TranslationUnitInfo info = (TranslationUnitInfo) fileInfo;
			return info.fCopy;
		}
		return null;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.ICompilationUnitDocumentProvider#shutdown()
	 */
	public void shutdown() {
		// CUIPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(fPropertyListener);
		Iterator e = getConnectedElementsIterator();
		while (e.hasNext())
			disconnect(e.next());
	}
}
