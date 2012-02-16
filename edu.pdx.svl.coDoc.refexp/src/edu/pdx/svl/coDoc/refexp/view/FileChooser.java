package edu.pdx.svl.coDoc.refexp.view;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*; 
 
public class FileChooser {
 

	public static String getFilePath() {
	  String path = "";

	  FileDialog fileDialog = new FileDialog(new Shell(), SWT.OPEN);
	  
	  String[] filterNames = new String[] {"PDF", "All Files"};
	  fileDialog.setFilterNames(filterNames);
	  fileDialog.setFilterExtensions(new String[] { "*.pdf", "*.*" });
	  path = fileDialog.open();
	  
	  return path;
	}
	
	public static File getFile() {
		String filePath = getFilePath();
		if (filePath == null) {
			return null;
		}
		File f = new File(filePath);
		return f;
	}
		
 
}
 