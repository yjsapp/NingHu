package com.superapp.xmlparse;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.superapp.info.GongdanInfo;

public class Xmlparse {
	
	
	
	
	
	
	public  List<GongdanInfo> parsegongdan(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<GongdanInfo> gongdaninfos = new ArrayList<GongdanInfo>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			GongdanInfoListContentHandler gongdaninfoListContentHandler = new GongdanInfoListContentHandler(
					gongdaninfos);
			xmlReader.setContentHandler(gongdaninfoListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator iterator =  gongdaninfos.iterator(); iterator.hasNext();) {
				GongdanInfo gongdanInfo = (GongdanInfo) iterator.next();
				System.out.println(gongdanInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  gongdaninfos;
	}
	
	
	
	
	

}
