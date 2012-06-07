package edu.pdx.svl.coDoc.cdc.preferences;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import edu.pdx.svl.coDoc.cdc.XML.SimpleXML;
import edu.pdx.svl.coDoc.cdc.referencemodel.PDFFile;

@Root
public class PreferenceValues {
	private static PreferenceValues instance;
	public static PreferenceValues getInstance() {
		if (instance == null) {
			instance = SimpleXML.read(preferencesFilePath);
			if (instance == null) {
				instance = new PreferenceValues();
			}
		}
		return instance;
	}
	
	public static final String preferencesFilePath = "Spec2ModelPreferences.xml";
	
	@Element
	private boolean useConfirmationWindow;
	@Element
	private PDFFile currentPdfFile;
	
	private PreferenceValues() {
		useConfirmationWindow = true;
	}
	
	public boolean isUseConfirmationWindow() {
		return useConfirmationWindow;
	}

	public void setUseConfirmationWindow(boolean useConfirmationWindow) {
		this.useConfirmationWindow = useConfirmationWindow;
		SimpleXML.write(this, preferencesFilePath);
	}

	public PDFFile getCurrentPdfFile() {
		return currentPdfFile;
	}
	
	public void setCurrentPdfFile(PDFFile currentPdfFile) {
		this.currentPdfFile = currentPdfFile;
		SimpleXML.write(this, preferencesFilePath);
	}
	
}
