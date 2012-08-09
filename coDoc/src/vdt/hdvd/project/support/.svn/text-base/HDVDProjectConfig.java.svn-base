/**
 * 
 */
package vdt.hdvd.project.support;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IProject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Juncao
 * 
 */
public class HDVDProjectConfig {

	public static final String PropertyName_SimpleSvdTrace = "SimpleSvdTrace";
	
	static 
	{
		HDVDProjectConfig.DefaultPropertyTable = new Hashtable<String, String>();
		HDVDProjectConfig.DefaultPropertyTable.put(HDVDProjectConfig.PropertyName_SimpleSvdTrace, "1");
	}
	
	// default property table 
	public static Hashtable<String, String> DefaultPropertyTable; 
	
	/**
	 * create default project config file in the given projectPath
	 * @param projectName the project name
	 * @return SvdProjectConfig if succeeded, null otherwise.
	 */
	public static HDVDProjectConfig createSvdProjectConfig(IProject project)
	{
		HDVDProjectConfig.writeConfigFile(project, DefaultPropertyTable);
		
		HDVDProjectConfig config = new HDVDProjectConfig();
		config.projectName = project.getName();
		
		if(!config.readConfigFile())
		{
			return null;
		}
		
		return config;
	}
	
	
	public String projectName = null;

	protected Hashtable<String, String> propertyTable = new Hashtable<String, String>();
	
	public String getProperty(String name) {
		// no space is allowed ...
		name = name.replace(" ", "");
				
		return this.propertyTable.get(name);
	}

	public void setProperty(String name, String value) {
		
		// no space is allowed ...
		name = name.replace(" ", "");
		
		if(this.propertyTable.get(name)!=null)
		{
			this.propertyTable.remove(name);
		}
		
		propertyTable.put(name, value);
		
		writeConfigFile(propertyTable);
	}
	
	protected static void writeConfigFile(
			IProject project, 
			Hashtable<String, String> properties) {
		try {
			String projectName = project.getName();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("SvdProject");
			doc.appendChild(rootElement);

			// set attribute to the root element
			Attr attr = doc.createAttribute("ProjectName");
			attr.setValue(projectName);
			rootElement.setAttributeNode(attr);

			// staff elements
			Element prop = null;
			
			for(String key : properties.keySet())
			{
				prop = doc.createElement(key);
				rootElement.appendChild(prop);
				prop.setTextContent(properties.get(key));
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			transformerFactory.setAttribute("indent-number", 4);
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(project.getLocation().toOSString(), ".svdproject"));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	protected void writeConfigFile(Hashtable<String, String> properties) {
		IProject project = HDVDWorkspace.getProject(projectName);
		
		if(project == null)
		{
			return;
		}
		
		HDVDProjectConfig.writeConfigFile(project, properties);
	}
	
	public boolean readConfigFile() {
		try {
			IProject project = HDVDWorkspace.getProject(this.projectName);
			
			if(project == null)
			{
				return false;
			}
			
			File fXmlFile = new File(project.getLocation().toOSString(), ".svdproject");
			
			if(!fXmlFile.exists())
			{
				return false;
			}
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
	 		NodeList list = doc.getDocumentElement().getChildNodes();
			for (int temp = 0; temp < list.getLength(); temp++) {
	 
			   Node nNode = list.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			      Element elem = (Element) nNode;
			      this.propertyTable.put(elem.getNodeName(), elem.getTextContent());
			   }			   
			}
			
			return true;
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
