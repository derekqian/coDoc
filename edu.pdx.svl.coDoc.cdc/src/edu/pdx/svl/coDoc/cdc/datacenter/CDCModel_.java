/* 
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


class Head_ {
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

class CodeFileEntry_ {
	@Element
	private String filename=" ";	
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
}

class CodeFiles_ {
	@ElementList
	private Vector<CodeFileEntry_> files = new Vector<CodeFileEntry_>();
}

class SpecFileEntry_ {
	@Element
	private String filename=" ";
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
}

class SpecFiles_ {
	@ElementList
	private Vector<SpecFileEntry_> files = new Vector<SpecFileEntry_>();
}

class CodeSelection_ {
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
	public CodeSelection toCodeSelection() {
		CodeSelection temp = new CodeSelection();
		temp.setSelCodePath(null);
		temp.setSelCodeText(null);
		temp.setSyntaxCodePath(codeselpath);
		temp.setSyntaxCodeText(codetext);
		return temp;
	}
}

class SpecSelection_ {
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
	public SpecSelection toSpecSelection() {
		SpecSelection temp = new SpecSelection();
		temp.setBottom(bottom);
		temp.setLeft(left);
		temp.setPage(page);
		temp.setPDFText(pdftext);
		temp.setRight(right);
		temp.setTop(top);
		return temp;
	}
}

class MapEntry_ {
	@Element
	private String codefilename=" ";
	@Element
	private CodeSelection_ codeselpath = new CodeSelection_();
	@Element
	private String specfilename=" ";
	@Element
	private SpecSelection_ specselpath = new SpecSelection_();
	@Element
	private String comment=" ";
	
	public void setCodefilename(String codefilename) {
		this.codefilename = codefilename;
	}
	public String getCodefilename() {
		return codefilename;
	}
	public void setCodeselpath(CodeSelection_ codeselpath) {
		this.codeselpath = codeselpath;
	}
	public CodeSelection_ getCodeselpath() {
		return codeselpath;
	}
	public void setSpecfilename(String specfilename) {
		this.specfilename = specfilename;
	}
	public String getSpecfilename() {
		return specfilename;
	}
	public void setSpecselpath(SpecSelection_ specselpath) {
		this.specselpath = specselpath;
	}
	public SpecSelection_ getSpecselpath() {
		return specselpath;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
}

class Maps_ {
	@ElementList
	private Vector<MapEntry_> maplist = new Vector<MapEntry_>();
	
	public Vector<MapEntry_> getMapList() {
		return maplist;
	}
}

class Body_ {
	@Element
	public CodeFiles_ codefiles = null;
	@Element
	public SpecFiles_ specfiles = null;
	@Element
	public Maps_ maps = null;
	
	public Body_() {
		codefiles = new CodeFiles_();
		specfiles = new SpecFiles_();
		maps = new Maps_();
	}
}

class OpEntry_ {
	@Element
	private String operation=" ";
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
}

class LastOpened_ {
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
class History_ {
	@Element
	private LastOpened_ lastOpened = new LastOpened_();
	@ElementList
	private Vector<OpEntry_> operations = new Vector<OpEntry_>();
	
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
	
	public Vector<OpEntry_> getOperationList() {
		return operations;
	}
}

@Root
public class CDCModel_ {
	@Element
	private Head_ head;
	@Element
	private Body_ body;
	@Element
	private History_ hist;
	
	private Date date;
	private SimpleDateFormat ft = null;
	
	private FolderMapTreeNode root = null;
	
	public CDCModel_() {
		Properties props=System.getProperties();
		ft = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS zzz");
		date = new Date();
		String time = ft.format(date);
		String os = props.getProperty("os.name");
		String creater = props.getProperty("user.name");
		head = new Head_();
		head.setFiletype("CDC");
		head.setCreater(creater);
		head.setOS(os);
		head.setCreatetime(time);
		body = new Body_();
		hist = new History_();
	}
	
	public Vector<MapEntry_> getMapEntries() {
		return body.maps.getMapList();
	}
	public String getLastOpenedCodeFilename() {
		return hist.getLastOpenedCodeFilename();
	}
	public String getLastOpenedSpecFilename() {
		return hist.getLastOpenedSpecFilename();
	}
	public Vector<OpEntry_> getOperationList() {
		return hist.getOperationList();
	}
	
	public Head_ getHead() {
		return head;
	}
	public Body_ getBody() {
		return body;
	}
	public History_ getHist() {
		return hist;
	}
	public static void main(String[] args) {
		CDCModel_ cdcModel_ = null;
		File cdcFile_ = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver1.cdc");
		Serializer serializer = new Persister();
		try {
			cdcModel_ = serializer.read(CDCModel_.class, cdcFile_);
		} catch (Exception e) {
			System.out.println("Unable to read cdcFile!");
			e.printStackTrace();
		}
		
		CDCModel cdcModel = new CDCModel();
		cdcModel.getHead().setCreater(cdcModel_.getHead().getCreater());
		cdcModel.getHead().setCreatetime(cdcModel_.getHead().getCreatetime());
		cdcModel.getHead().setFiletype(cdcModel_.getHead().getFiletype());
		cdcModel.getHead().setOS(cdcModel_.getHead().getOS());
		cdcModel.getMapIdTree();
		for(MapEntry_ entry : cdcModel_.getBody().maps.getMapList()) {
			String parentfolderuuid = cdcModel.getBody().folders.getFolderEntryId("/");
			cdcModel.addMapEntry(parentfolderuuid, entry.getCodefilename(), entry.getCodeselpath().toCodeSelection(), entry.getSpecfilename(), entry.getSpecselpath().toSpecSelection(), entry.getComment());
		}
		cdcModel.setLastOpenedCodeFilename(cdcModel_.getLastOpenedCodeFilename());
		cdcModel.setLastOpenedSpecFilename(cdcModel_.getLastOpenedSpecFilename());
		File cdcFile = new File("/home/derek/runtime-EclipseApplication/dio/dio.ver2.cdc");
		try {
			serializer.write(cdcModel, cdcFile);
		} catch (Exception e) {
			System.out.println("Unable to write cdcFile!");
			e.printStackTrace();
		}
		return;
	}
}
