package edu.pdx.svl.coDoc.refexp.referencemodel;

import javax.xml.bind.annotation.*;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import edu.pdx.svl.coDoc.refexp.Global;


@Root
public class ProjectReference extends Reference {
	@Element
	protected String projectDirectory;
	@Element
	protected String projectName;
	
	public ProjectReference() {
		projectName = "Default Project Name";
		projectDirectory = "C:\\someDirectory";
		pdfFile = PDFManager.INSTANCE.getCurrentPdfFile();
	}
	
	@Override
	public String getType() {
		return "Project";
	}


	@Override
	public String description() {
		return projectName;
	}

	public String getProjectDirectory() {
		return projectDirectory;
	}


	public void setProjectDirectory(String projectDirectory) {
		this.projectDirectory = projectDirectory;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public ProjectReference copyWithNoChildren() {
		ProjectReference newProjRef = new ProjectReference();
		newProjRef.setComment(comment);
		newProjRef.setPdfFile(pdfFile);
		newProjRef.setProjectDirectory(projectDirectory);
		newProjRef.setProjectName(projectName);
		
		return newProjRef;
	}

}
