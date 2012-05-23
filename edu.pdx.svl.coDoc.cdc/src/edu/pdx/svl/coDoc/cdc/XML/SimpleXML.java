package edu.pdx.svl.coDoc.cdc.XML;

import java.io.File;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import edu.pdx.svl.coDoc.cdc.Global;
import edu.pdx.svl.coDoc.cdc.editor.CDCModel;
import edu.pdx.svl.coDoc.cdc.preferences.PreferenceValues;
import edu.pdx.svl.coDoc.cdc.referencemodel.ProjectReference;
import edu.pdx.svl.coDoc.cdc.referencemodel.Reference;
import edu.pdx.svl.coDoc.cdc.referencemodel.References;
import edu.pdx.svl.coDoc.cdc.referencemodel.SourceFileReference;
import edu.pdx.svl.coDoc.cdc.referencemodel.TextSelectionReference;


public class SimpleXML {
	private static String xmlFile = "Spec2ModelReferences.xml";
	
//	private static void printSourceFileInfo() {
//		String projectDirectory = Spec2ModelEditor.SINGLETON.getProjectDirectory();
//		String projectName = Spec2ModelEditor.SINGLETON.getProjectName();
//		String cFileName = Spec2ModelEditor.SINGLETON.getCFileName();
//		
//		System.out.println("Project Directory: " + projectDirectory);
//		System.out.println("Project Name     : " + projectName);
//		System.out.println("C File Name      : " + cFileName);
//	}
	
	public static CDCModel readCDCModel(String filepath) {
		CDCModel cdcModel = null;
		File cdcFile = new File(filepath);
		Serializer serializer = new Persister();
		try {
			cdcModel = serializer.read(CDCModel.class, cdcFile);
		} catch (Exception e) {
			System.out.println("Unable to read " + filepath + "!");
			e.printStackTrace();
		}
		return cdcModel;
	}
	
	public static void writeCDCModel(CDCModel cdcModel, String filepath) {
		Serializer serializer = new Persister();
		File cdcFile = new File(filepath);
		try {
			serializer.write(cdcModel, cdcFile);
		} catch (Exception e) {
			System.out.println("Unable to write " + filepath + "!");
			e.printStackTrace();
		}
	}
	
	public static References read() {
		File source = null;
		ProjectReference projectReference = null;
		String xmlFilePath = Global.getProjectDirectory() + xmlFile;
		
		References refs = new References();
		Serializer serializer = new Persister();
		
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		for (IProject proj : projects) {
			IPath path = proj.getFullPath();
			File pathAsFile = path.toFile();
			String relativeDir = pathAsFile.getPath() + '\\';
			String workspaceDir = workspaceRoot.getLocation().toFile().getAbsolutePath();
			String projDir = workspaceDir + relativeDir;
			xmlFilePath = projDir + xmlFile;
			source = new File(xmlFilePath);
			if (source.exists() == true) {
				try {
					projectReference = serializer.read(ProjectReference.class, source);
					refs.getProjects().add(projectReference);
				} catch (Exception e) {
					System.out.println("Unable to read Spec2ModelReferences.xml file!\n");
					e.printStackTrace();
				}
			}
		}
		
		
		
		/**
		 * populating the parents
		 */
		Vector<Reference> projs = refs.getProjects();
		for (Reference proj : projs) {
			projectReference = (ProjectReference)proj;
			Vector<Reference> sfrs = projectReference.getChildrenList();
			for (Reference sfr : sfrs) {
				SourceFileReference sourceFileReference = (SourceFileReference)sfr;
				sourceFileReference.setProjectReference(projectReference);
				Vector<Reference> tsrs = sourceFileReference.getChildrenList();
				for (Reference tsr : tsrs) {
					TextSelectionReference textSelectionReference = (TextSelectionReference)tsr;
					textSelectionReference.setSourceFile(sourceFileReference);
				}
			}
		}
		
		
		return refs;
	}
	
	public static void write(References refs) {
		Vector<Reference> projects = refs.getProjects();
		for (Reference r : projects) {
			ProjectReference pr = (ProjectReference)r;
			String projDir = pr.getProjectDirectory();
			String xmlFilePath = projDir + xmlFile;
			Serializer serializer = new Persister();
			File result = new File(xmlFilePath);
			try {
				serializer.write(pr, result);
			} catch (Exception e) {
				System.out.println("Unable to write " + xmlFilePath + " (XML FILE)");
				e.printStackTrace();
			}
		}
		
	}
	
	public static void delete(ProjectReference projRef) {
		String projDir = projRef.getProjectDirectory();
		String xmlFilePath = projDir + xmlFile;
		File delFile = new File(xmlFilePath);
		delFile.delete();
		return;
	}
	
	public static PreferenceValues read(String preferenceFilePath) {
		PreferenceValues pvs = null;
		File prefFile = new File(preferenceFilePath);
		Serializer serializer = new Persister();
		try {
			pvs = serializer.read(PreferenceValues.class, prefFile);
		} catch (Exception e) {
			System.out.println("Unable to read Spec2ModelPreferences.xml file!");
			e.printStackTrace();
		}
		return pvs;
	}
	
	public static void write(PreferenceValues pvs, String preferenceFilePath) {
		Serializer serializer = new Persister();
		File prefFile = new File(preferenceFilePath);
		try {
			serializer.write(pvs, prefFile);
		} catch (Exception e) {
			System.out.println("Unable to write Spec2ModelPreferences.xml file!");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CDCModel cdcModel = new CDCModel();
		cdcModel.addCodeFileEntry("project:///test/code/example.c");
		cdcModel.addSpecFileEntry("project:///test/spec/test.pdf");
		cdcModel.addCodeFileEntry("project:///test/code/test1.c");
		cdcModel.addSpecFileEntry("project:///test/spec/test1.pdf");
		cdcModel.addCodeFileEntry("project:///test/code/test2.c");
		cdcModel.addSpecFileEntry("project:///test/spec/test3.pdf");
		cdcModel.addMapEntry("project:///test/code/example.c", "int i;/Node", "project:///test/spec/sample.pdf", "page/left/top/right/bottom", "comment");
		cdcModel.addMapEntry("project:///test/code/test.c", "double i;/Node", "project:///test/spec/test.pdf", "page/left/top/right/bottom", "comment");
		cdcModel.addMapEntry("project:///test/code/test2.c", "double i;/Node", "project:///test/spec/test.pdf", "page/left/top/right/bottom", "comment");
		String filepath = "/home/derek/runtime-EclipseApplication/test/sample.cdc";
		SimpleXML.writeCDCModel(cdcModel, filepath);
		CDCModel cdcModel1 = SimpleXML.readCDCModel(filepath);
		return;
	}
}
