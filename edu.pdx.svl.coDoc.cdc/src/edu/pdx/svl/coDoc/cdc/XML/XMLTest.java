package edu.pdx.svl.coDoc.cdc.XML;


import java.io.File;
import java.util.Vector;




import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import edu.pdx.svl.coDoc.cdc.referencemodel.References;


public class XMLTest {
	References refs;
	
	public void test() throws Exception {
		refs = new References();
		refs.createSampleData();
		refs.printTree();
		
		//!!!!!!!!!!!!!!!JAXB SUCKS!!!!!!!!!!!!!!
		
//		JAXBContext context = JAXBContext.newInstance(References.class);
//		Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
////        JAXBElement<References> jaxbElement = new JAXBElement<References>(new QName("references"), References.class, refs);
//        marshaller.marshal(refs, System.out);
		
		/*
		 * Oh, and so does XStream and SAXP...
		 */
		
		Serializer serializer = new Persister();
		File result = new File("xmlTest.xml");
		serializer.write(refs, result);
		
		System.out.println("read xml data back in...");
		Serializer serializer2 = new Persister();
		File source = new File("xmlTest.xml");
		References refs2 = serializer2.read(References.class, source);
		refs2.printTree();
	}
	
	public static void main(String[] args) {
		XMLTest j = new XMLTest();
		try {
			j.test();
		} catch (Exception e) {
			System.out.println("oops");
			e.printStackTrace();
		}
	}
}
