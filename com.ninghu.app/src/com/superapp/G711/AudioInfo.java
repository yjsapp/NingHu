package com.superapp.G711;

import org.zoolu.sip.header.FromHeader;
import org.zoolu.sip.header.ToHeader;
import org.zoolu.sip.header.ViaHeader;
import org.zoolu.sip.message.Message;

import android.os.Handler;

public class AudioInfo {
	
	
	
	public static boolean IsSpeechResponse=false;
	public static boolean IsGuaDuanResponse=false;
	public static boolean IsTalking=false;//是否正在于通话中
	public static boolean IsCallCancel=false;
	public static boolean HaveCalled=false;//是否播过号码
	
	public static String audio_info_peer="";
	public static String audio_info_ip="101.69.255.130";
	public static String audio_info_port="";
	public static int  audio_serverport=0;
	
	public static String CallingFromNumber="5001";
	public static String CalledFromNumber="";
	
	public static String CallingToNumber="";
	public static String CalledToNumber="5001";
	
	public static ToHeader toh;   //SIP头的TO
	public static FromHeader fromh;  //SIP头的FROM
	public static ViaHeader viash;   //viaheader
	public static String AudioResponse_OK="";
	public static  String AudioResponse_Error="";
	
	public static String code="";
	public static int packet_got=0;
	public static byte [] audio_info_magic=new byte[16];
	
	public static Handler G711;
	public static Handler LaiDian;
	public static Handler ZhuJiaoGuaDuan;
	public static Handler G711HujiaoError;
	
	public static Handler G711DuiFangJieTongGuaDuan;
	
	public static Handler Called;
	
	
	public static Message message=new Message();

}
