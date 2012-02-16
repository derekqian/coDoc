package edu.pdx.svl.coDoc.refexp.test;

import java.util.ArrayList;
import java.util.List;

public enum TestTableContentProvider {
	INSTANCE;
	
	private List<TestModelItem> testModels;
	
	private TestTableContentProvider() {
		testModels = new ArrayList<TestModelItem>();
		
		//5 rows
		testModels.add(new TestModelItem());
		testModels.add(new TestModelItem());
		testModels.add(new TestModelItem());
		testModels.add(new TestModelItem());
		testModels.add(new TestModelItem());
	}
	
	public List<TestModelItem> getTestModels(){
		return testModels;
	}
}
