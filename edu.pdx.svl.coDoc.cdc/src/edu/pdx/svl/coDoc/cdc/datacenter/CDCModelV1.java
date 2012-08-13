/**
 * CDCModelV1 is the first version of CDC file.
 * In this version, no category information is supported.
 *  
 * project:///testproj/code/example.c
 * absolute:///home/derek/testproj/code.example.c
 */

package edu.pdx.svl.coDoc.cdc.datacenter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Stack;
import java.util.UUID;
import java.util.Vector;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


class HeadV1 {
	@Element
	private String filetype=" ";
	@Element
	private String creater=" ";
	@Element
	private String createtime=" ";
	@Element
	private String os=" ";
	
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setOS(String os) {
		this.os = os;
	}
	public String getOS() {
		return os;
	}
}

class CodeFileEntryV1 {
	@Element
	private String filename=" ";	
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
}

class CodeFilesV1 {
	@ElementList
	private Vector<CodeFileEntryV1> files = new Vector<CodeFileEntryV1>();
}

class SpecFileEntryV1 {
	@Element
	private String filename=" ";
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
}

class SpecFilesV1 {
	@ElementList
	private Vector<SpecFileEntryV1> files = new Vector<SpecFileEntryV1>();
}

class CodeSelectionV1 {
	@Element
	private String codeselpath=" ";
	@Element
	private String codetext=" ";
	
	public void setCodeSelPath(String codeselpath) {
		this.codeselpath = codeselpath;
	}
	public String getCodeSelPath() {
		return codeselpath;
	}
	public void setCodeText(String codetext) {
		this.codetext = codetext;
	}
	public String getCodeText() {
		return codetext;
	}
	public String toString() {
		return codetext+"/"+codeselpath;
	}
	public CodeSelectionV2 toCodeSelection() {
		CodeSelectionV2 temp = new CodeSelectionV2();
		temp.setSelCodePath(null);
		temp.setSelCodeText(null);
		temp.setSyntaxCodePath(codeselpath);
		temp.setSyntaxCodeText(codetext);
		return temp;
	}
}

class SpecSelectionV1 {
	@Element
	private int page;
	@Element
	private int top;
	@Element
	private int bottom;
	@Element
	private int left;
	@Element
	private int right;
	@Element
	private String pdftext=" ";
	
	public void setPage(int page) {
		this.page = page;
	}
	public int getPage() {
		return page;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getTop() {
		return top;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getBottom() {
		return bottom;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getLeft() {
		return left;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getRight() {
		return right;
	}
	public void setPDFText(String pdftext) {
		this.pdftext = pdftext;
	}
	public String getPDFText() {
		return pdftext;
	}
	public String toString() {
		return page+"/"+left+"/"+top+"/"+right+"/"+bottom+"/"+pdftext;
	}
	public SpecSelectionV2 toSpecSelection() {
		SpecSelectionV2 temp = new SpecSelectionV2();
		temp.setBottom(bottom);
		temp.setLeft(left);
		temp.setPage(page);
		temp.setPDFText(pdftext);
		temp.setRight(right);
		temp.setTop(top);
		return temp;
	}
}

class MapEntryV1 {
	@Element
	private String codefilename=" ";
	@Element
	private CodeSelectionV1 codeselpath = new CodeSelectionV1();
	@Element
	private String specfilename=" ";
	@Element
	private SpecSelectionV1 specselpath = new SpecSelectionV1();
	@Element
	private String comment=" ";
	
	public void setCodefilename(String codefilename) {
		this.codefilename = codefilename;
	}
	public String getCodefilename() {
		return codefilename;
	}
	public void setCodeselpath(CodeSelectionV1 codeselpath) {
		this.codeselpath = codeselpath;
	}
	public CodeSelectionV1 getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfilename(String specfilename) {
		this.specfilename = specfilename;
	}
	public String getSpecfilename() {
		return specfilename;
	}
	public void setSpecselpath(SpecSelectionV1 specselpath) {
		this.specselpath = specselpath;
	}
	public SpecSelectionV1 getSpecselpath() {
		return specselpath;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
}

class MapsV1 {
	@ElementList
	private Vector<MapEntryV1> maplist = new Vector<MapEntryV1>();
	
	public Vector<MapEntryV1> getMapList() {
		return maplist;
	}
}

class BodyV1 {
	@Element
	public CodeFilesV1 codefiles = null;
	@Element
	public SpecFilesV1 specfiles = null;
	@Element
	public MapsV1 maps = null;
	
	public BodyV1() {
		codefiles = new CodeFilesV1();
		specfiles = new SpecFilesV1();
		maps = new MapsV1();
	}
}

class OpEntryV1 {
	@Element
	private String operation=" ";
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
}

class LastOpenedV1 {
	@Element
	private String codeFilename=" ";
	@Element
	private String specFilename=" ";
	
	public void setCodeFilename(String codeFilename) {
		this.codeFilename = (codeFilename==null)?" ":codeFilename;
	}
	public String getCodeFilename() {
		return codeFilename.equals(" ")?null:codeFilename;
	}
	
	public void setSpecFilename(String specFilename) {
		this.specFilename = (specFilename==null)?" ":specFilename;
	}
	public String getSpecFilename() {
		return specFilename.equals(" ")?null:specFilename;
	}
}
class HistoryV1 {
	@Element
	private LastOpenedV1 lastOpened = new LastOpenedV1();
	@ElementList
	private Vector<OpEntryV1> operations = new Vector<OpEntryV1>();
	
	public void setLastOpenedCodeFilename(String codeFilename) {
		lastOpened.setCodeFilename(codeFilename);
	}
	public String getLastOpenedCodeFilename() {
		return lastOpened.getCodeFilename();
	}
	
	public void setLastOpenedSpecFilename(String specFilename) {
		lastOpened.setSpecFilename(specFilename);
	}
	public String getLastOpenedSpecFilename() {
		return lastOpened.getSpecFilename();
	}
	
	public Vector<OpEntryV1> getOperationList() {
		return operations;
	}
}

@Root
public class CDCModelV1 {
	@Element
	private HeadV1 head;
	@Element
	private BodyV1 body;
	@Element
	private HistoryV1 hist;
	
	private Date date;
	private SimpleDateFormat ft = null;
	
	private FolderMapTreeNode root = null;
	
	public CDCModelV1() {
		Properties props=System.getProperties();
		ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS zzz");
		date = new Date();
		String time = ft.format(date);
		String os = props.getProperty("os.name");
		String creater = props.getProperty("user.name");
		head = new HeadV1();
		head.setFiletype("CDC");
		head.setCreater(creater);
		head.setOS(os);
		head.setCreatetime(time);
		body = new BodyV1();
		hist = new HistoryV1();
	}
	
	public Vector<MapEntryV1> getMapEntries() {
		return body.maps.getMapList();
	}
	public String getLastOpenedCodeFilename() {
		return hist.getLastOpenedCodeFilename();
	}
	public String getLastOpenedSpecFilename() {
		return hist.getLastOpenedSpecFilename();
	}
	public Vector<OpEntryV1> getOperationList() {
		return hist.getOperationList();
	}
	
	public HeadV1 getHead() {
		return head;
	}
	public BodyV1 getBody() {
		return body;
	}
	public HistoryV1 getHist() {
		return hist;
	}
	public static void main(String[] args) {
		toV2();
	}
	private static void toV2() {
		CDCModelV1 cdcModelV1 = null;
		File cdcFileV1 = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver1.cdc");
		Serializer serializer = new Persister();
		try {
			cdcModelV1 = serializer.read(CDCModelV1.class, cdcFileV1);
		} catch (Exception e) {
			System.out.println("Unable to read cdcFileV1!");
			e.printStackTrace();
		}
		
		CDCModelV2 cdcModelV2 = new CDCModelV2();
		cdcModelV2.getHead().setCreater(cdcModelV1.getHead().getCreater());
		cdcModelV2.getHead().setCreatetime(cdcModelV1.getHead().getCreatetime());
		cdcModelV2.getHead().setFiletype(cdcModelV1.getHead().getFiletype());
		cdcModelV2.getHead().setOS(cdcModelV1.getHead().getOS());
		cdcModelV2.getMapIdTree();
		for(MapEntryV1 entry : cdcModelV1.getBody().maps.getMapList()) {
			String parentfolderuuid = cdcModelV2.getBody().folders.getFolderEntryId("/");
			cdcModelV2.addMapEntry(parentfolderuuid, entry.getCodefilename(), entry.getCodeselpath().toCodeSelection(), entry.getSpecfilename(), entry.getSpecselpath().toSpecSelection(), entry.getComment());
		}
		cdcModelV2.setLastOpenedCodeFilename(cdcModelV1.getLastOpenedCodeFilename());
		cdcModelV2.setLastOpenedSpecFilename(cdcModelV1.getLastOpenedSpecFilename());
		File cdcFileV2 = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver2.cdc");
		try {
			serializer.write(cdcModelV2, cdcFileV2);
		} catch (Exception e) {
			System.out.println("Unable to write cdcFileV2!");
			e.printStackTrace();
		}		
	}
}
