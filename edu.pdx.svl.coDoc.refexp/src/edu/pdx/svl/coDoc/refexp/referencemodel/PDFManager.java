package edu.pdx.svl.coDoc.refexp.referencemodel;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

import edu.pdx.svl.coDoc.refexp.FileCopy;
import edu.pdx.svl.coDoc.refexp.Global;
import edu.pdx.svl.coDoc.refexp.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.refexp.view.FileChooser;



public enum PDFManager {
	INSTANCE;
	
//	private boolean acrobatOpened = false;
	private PDFFile currentPdfFile;
	private Vector<PDFFile> openPdfFiles = new Vector<PDFFile>();
	
	private PDFManager() {
		currentPdfFile = PreferenceValues.getInstance().getCurrentPdfFile();
		if (currentPdfFile == null) {
			currentPdfFile = new PDFFile();
		}
	}
	
	public void setCurrentPdfFile(PDFFile currentPdfFile) {
		this.currentPdfFile = currentPdfFile;
		PreferenceValues.getInstance().setCurrentPdfFile(currentPdfFile);
	}
	
	public PDFFile getCurrentPdfFile() {
		return currentPdfFile;
	}

	public void openFileInAcrobat() {
		File f = FileChooser.getFile();
		openFileInAcrobat(f);
	}
	
	public void openActivePDFInAcrobat() {
		if (currentPdfFile == null) {
			openFileInAcrobat();
			return;
		}
//		acrobatOpened = true;
		/**
		 * opens default application linked to given file extension
		 */
		String acrobatPath = "rundll32 url.dll,FileProtocolHandler " + currentPdfFile.getFile();
		try {
			Runtime.getRuntime().exec( acrobatPath );
		} catch (IOException e) {
			System.out.println("unable to open acrobat");
			e.printStackTrace();
		}
	}
	
	public void openFileInAcrobat(File pdf) {
		PDFFile newPdfFile = new PDFFile(pdf);
		setCurrentPdfFile(newPdfFile);
		
//		acrobatOpened = true;
		
		copyCurrentFileToProject();
		
		/**
		 * opens default application linked to given file extension
		 */
		String acrobatPath = "rundll32 url.dll,FileProtocolHandler " + currentPdfFile.getFile();
		try {
			Runtime.getRuntime().exec( acrobatPath );
		} catch (IOException e) {
			System.out.println("unable to open acrobat");
			e.printStackTrace();
		}
	}
	
	public void openFileInAcrobatForced(PDFFile pdf) {
		File f = pdf.getFile();
		String acrobatPath = "rundll32 url.dll,FileProtocolHandler " + f;
		try {
			Runtime.getRuntime().exec( acrobatPath );
//			acrobatOpened = true;
		} catch (IOException e) {
			System.out.println("unable to open acrobat");
			e.printStackTrace();
		}
	}
	
//	public void openFileInAcrobat() {
//		acrobatOpened = true;
//		
//		/**
//		 * opens default application linked to given file extension
//		 */
//		String acrobatPath = "rundll32 url.dll,FileProtocolHandler " + getCurrentPdfFile().getFile();
//		try {
//			Runtime.getRuntime().exec( acrobatPath );
//		} catch (IOException e) {
//			System.out.println("unable to open acrobat");
//			e.printStackTrace();
//		}
//	}
	
//	public boolean isAcrobatOpened() {
//		if (acrobatOpened == true)
//			return true;
//		return false;
//	}
	
//	public String getProjectDirectory() {
//		String dir = null;
//		
//		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
//		IProject[] projects = workspaceRoot.getProjects();
//		if (projects.length != 0) {
//			IProject proj = projects[0];
//			IPath path = proj.getFullPath();
//			File filePath = path.toFile();
//			String relativeDir = filePath.getPath() + '\\';
//			String workspaceDir = workspaceRoot.getLocation().toFile().getAbsolutePath();
//			dir = workspaceDir + relativeDir;
//			return dir;
//		} else {
//			return "";
//		}
//	}

	private void copyCurrentFileToProject() {
		String dir = Global.INSTANCE.getActiveProjectDirectory();
		String fileName = getCurrentPdfFile().getFileName();
		String projectPDFPath = dir + fileName;
		
		if (isPDFInProject() == true) {
			boolean overwrite = promptUserForOverwrite();
			
			try {
				FileCopy.copy(getCurrentPdfFile().getFile().getAbsolutePath(), projectPDFPath, overwrite);
				File newFile = new File(projectPDFPath);
				PDFFile newPDFFile = new PDFFile(newFile);
				setCurrentPdfFile(newPDFFile);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} else {
			
			try {
				FileCopy.copy(getCurrentPdfFile().getFile().getAbsolutePath(), projectPDFPath, true);
				File newFile = new File(projectPDFPath);
				PDFFile newPDFFile = new PDFFile(newFile);
				setCurrentPdfFile(newPDFFile);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor() );
		} catch (CoreException e) {
			System.out.println("Could not refresh the Project Explorer");
			e.printStackTrace();
		}
	}
	
	
	
	private boolean promptUserForOverwrite(){
		boolean result = MessageDialog.openQuestion(null, "Overwrite Question", "Do you want to overwrite the existing \"" + 
				getCurrentPdfFile().getFileName() + "?");
		
		if (result == true) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isPDFInProject() {
		String dir = Global.INSTANCE.getActiveProjectDirectory();
		String fileName = getCurrentPdfFile().getFileName();
		String fullPath = dir + fileName;
		
		File projFile = new File(fullPath);
		if (projFile.exists()) {
			return true;
		}
		return false;
	}
}
