package com.superapp.sip;


import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.header.FromHeader;
import org.zoolu.sip.header.ToHeader;
import org.zoolu.sip.header.ViaHeader;

import android.os.Handler;



public class SipInfo {
	public static KeepAlive keepalive = new KeepAlive();
	public static Sip sip_provider;
	
	public static NameAddress From,To;
	
	public static ToHeader toh;   //SIP头的TO
	public static FromHeader fromh;  //SIP头的FROM
	public static ViaHeader viash;   //viaheader
	
	
	
	
	//public static String devid = "310023000600550001"; 
	public static String devid = "600000000000000001";
	public static String seed = "";
	public static  String serverip="101.69.255.130";
	public static String password = "";
	public static String time= "";
	public static String loginmeans="0";
	
	
	public static String zhujianumber="";
	public static String zhujianame="";
	public static String zhujianamequeren="";
	
	public static String fujianumber="";

	public static String dev_type="";
	
	
	public static String QueryMediaResponseXML="";
	public static String RequestMediaResponseXML="";
	public static String media_info_peer="";
	public static String media_info_ip="101.69.255.130";
	public static String media_info_port="";
	public static int serverport=0;
	public static byte [] media_info_magic=new byte[16];
	
	public static boolean first_register_step_succeccful =false;
	public static boolean reg_success =false;
	public static boolean timeout =false;
	public static boolean reg_state =false;
	public static boolean zhujiadenglu_state =false;
	public static boolean query_response = false;
	public static boolean request_media=false;
	
	
	
	public static boolean endView=false;
	
	
	
	
	//发送的几个常数
		public static byte[] NalBuf = new byte[50000];
		public static int nalfirst = 0; //0表示未收到首包，1表示收到
		public static int index = 0;
		public static int isDestroy = 0;
		
	
public static Handler register;
public static Handler notifymedia;
public static Handler xingongdan;



public static int pktNumber = 0;  //记录发送的RTP包的个数




}
