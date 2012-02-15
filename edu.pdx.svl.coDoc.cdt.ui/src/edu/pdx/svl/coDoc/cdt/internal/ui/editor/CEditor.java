package edu.pdx.svl.coDoc.cdt.internal.ui.editor;

import edu.pdx.svl.coDoc.cdt.ui.CUIPlugin;
import org.eclipse.ui.editors.text.TextEditor;

public class CEditor extends TextEditor
{ 
  /**
   * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#initializeEditor()
   */
  protected void initializeEditor() 
  {
    setDocumentProvider(CUIPlugin.getDefault().getDocumentProvider());
  }
}
