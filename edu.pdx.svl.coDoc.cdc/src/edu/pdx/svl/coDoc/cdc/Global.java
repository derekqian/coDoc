package edu.pdx.svl.coDoc.cdc;

import java.io.File;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;

import edu.pdx.svl.coDoc.cdc.editor.EntryEditor;
import edu.pdx.svl.coDoc.cdc.editor.IReferenceExplorer;
import edu.pdx.svl.coDoc.cdc.test.TestModel;



public enum Global {
	INSTANCE;
	
	public int a;
	
	public TestModel testModel = new TestModel();
	
	Global() {
		a = 0;
	}
}
