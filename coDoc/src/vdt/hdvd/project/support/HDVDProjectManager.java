/**
 * 
 */
package vdt.hdvd.project.support;

import java.util.Hashtable;

import org.eclipse.core.resources.IProject;

/**
 * @author Juncao
 * SVD project manager
 */
public class HDVDProjectManager {

	public static String ActiveProjectName = null;
	
	protected static Hashtable<String, HDVDProjectConfig> projectTable = new Hashtable<String, HDVDProjectConfig>();
	
	/**
	 * Write to a project's property
	 * @param projectName project name
	 * @param propertyName property name
	 * @param propertyValue property value
	 */
	public static void writeProjectProperty(String projectName, String propertyName, String propertyValue)
	{
		HDVDProjectConfig projConfg = projectTable.get(projectName);
		
		if(projConfg == null)
		{
			projConfg = new HDVDProjectConfig();
			projConfg.projectName = projectName;
			projConfg.readConfigFile();
			projectTable.put(projConfg.projectName, projConfg);
		}

		projConfg.setProperty(propertyName, propertyValue);
	}
	
	/**
	 * Read a project's property
	 * @param projectName project name
	 * @param propertyName property name
	 * @return property value or null if the property is not found
	 */
	public static String readProjectProperty(String projectName, String propertyName)
	{
		HDVDProjectConfig projConfg = projectTable.get(projectName);
		
		if(projConfg == null)
		{
			projConfg = new HDVDProjectConfig();
			projConfg.projectName = projectName;
			projConfg.readConfigFile();
			projectTable.put(projConfg.projectName, projConfg);
		}

		return projConfg.getProperty(propertyName);
	}
	
	
	/**
	 * create default project config file in the given projectPath
	 * @param project the project object
	 * @return SvdProjectConfig if succeeded, null otherwise.
	 */
	public static boolean createSvdProjectConfig(IProject project)
	{
		HDVDProjectConfig config = HDVDProjectConfig.createSvdProjectConfig(project);
		
		if(null != config)
		{
			projectTable.put(project.getName(), config);
			return true;
		}
		
		return false;
	} 
}
