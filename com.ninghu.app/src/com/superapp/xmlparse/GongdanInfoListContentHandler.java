package com.superapp.xmlparse;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.superapp.info.GongdanInfo;



public class GongdanInfoListContentHandler extends DefaultHandler {
	
	
	
	private List<GongdanInfo> infos = null;
	private GongdanInfo gongdaninfo = null;
	private String tagName = null;

	public  GongdanInfoListContentHandler(List<GongdanInfo> infos) {
		super();
		this.infos = infos;
	}

	public List<GongdanInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<GongdanInfo> infos) {
		this.infos = infos;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch, start, length);
		if (tagName.equals("dev_id")) {
			gongdaninfo.setDev_id(temp);
		} 
		else if (tagName.equals("receiver")) {
			gongdaninfo.setReceiver(temp);
		} 
		else if (tagName.equals("receive_time")) {
			gongdaninfo.setReceive_time(temp);
		} 
		else if (tagName.equals("info_source")) {
			gongdaninfo.setInfo_source(temp);
		} 
		
		
		
		else if (tagName.equals("task_type")) {
			gongdaninfo.setTask_type(temp);
		} 
		else if (tagName.equals("task_id")) {
			gongdaninfo.setTask_id(temp);
		} 
		else if (tagName.equals("task_name")) {
			gongdaninfo.setTask_name(temp);
		} 
		else if (tagName.equals("order")) {
			gongdaninfo.setOrder(temp);
		}
		else if (tagName.equals("location")) {
			gongdaninfo.setLocation(temp);
		} 
		else if (tagName.equals("event_time")) {
			gongdaninfo.setEvent_time(temp);
		} 
		else if (tagName.equals("pic_path_down")) {
			gongdaninfo.setPic_path_down(temp);
		} 
		else if (tagName.equals("vehicle_fault_num")) {
			gongdaninfo.setVehicle_fault_num(temp);
		} 
		else if (tagName.equals("vehicle_fault_type")) {
			gongdaninfo.setVehicle_fault_type(temp);
		} 
		else if (tagName.equals("parking_position")) {
			gongdaninfo.setParking_position(temp);
		} 
		else if (tagName.equals("cargo")) {
			gongdaninfo.setCargo(temp);
		} 
		
		else if (tagName.equals("wrecker_num")) {
			gongdaninfo.setWrecker_num(temp);
		} 
		else if (tagName.equals("wrecker_type")) {
			gongdaninfo.setWrecker_type(temp);
		} 
		else if (tagName.equals("driver")) {
			gongdaninfo.setDriver(temp);
		} 
		else if (tagName.equals("co_driver")) {
			gongdaninfo.setCo_driver(temp);
		} 
			
		
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("task")) {
			infos.add(gongdaninfo);
		}
		tagName = "";

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		if (tagName.equals("task")) {
			gongdaninfo = new GongdanInfo();
		}
	}

}
