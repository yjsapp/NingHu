package com.superapp.info;

import java.util.ArrayList;
import java.util.List;

import org.zoolu.sip.header.FromHeader;
import org.zoolu.sip.header.ToHeader;
import org.zoolu.sip.header.ViaHeader;

public class Gongdan {
	

	
	public static  List<GongdanInfo> gongdans = new ArrayList<GongdanInfo>();
	public static ToHeader toh;   //SIPͷ��TO
	public static FromHeader fromh;  //SIPͷ��FROM
	public static ViaHeader viash;   //viaheader
	
	
	public static String GongDanResponse_OK="";
	public static List<GongdanInfo> New_gongdan = new ArrayList<GongdanInfo>(); 
	public static List<GongdanInfo> Weiwancheng_gongdan = new ArrayList<GongdanInfo>();
	public static List<GongdanInfo> Yiwancheng_gongdan = new ArrayList<GongdanInfo>();
	
	
	private String weixinID1;
	private String weixinID2;
	private String name1;
	private String name2;
	private String lastContent;
	private String lastTime;
	private String TxPath;
	private String isHaveNew;
	private String msgNum;
	public String getWeixinID1() {
		return weixinID1;
	}
	public void setWeixinID1(String weixinID1) {
		this.weixinID1 = weixinID1;
	}
	public String getWeixinID2() {
		return weixinID2;
	}
	public void setWeixinID2(String weixinID2) {
		this.weixinID2 = weixinID2;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getLastContent() {
		return lastContent;
	}
	public void setLastContent(String lastContent) {
		this.lastContent = lastContent;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getTxPath() {
		return TxPath;
	}
	public void setTxPath(String txPath) {
		TxPath = txPath;
	}
	public String getIsHaveNew() {
		return isHaveNew;
	}
	public void setIsHaveNew(String isHaveNew) {
		this.isHaveNew = isHaveNew;
	}
	public String getMsgNum() {
		return msgNum;
	}
	public void setMsgNum(String msgNum) {
		this.msgNum = msgNum;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
