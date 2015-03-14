package com.superapp.rtp;

import java.net.DatagramSocket;
import java.util.Random;

import com.superapp.sip.SipInfo;

import jlibrtp.Participant;
import jlibrtp.RTPSession;
import android.util.Log;

public class RTPSending {

	DatagramSocket rtpSocket1 = null;
	DatagramSocket rtcpSocket1 = null;
	public RTPSession rtpSession1 = null;
	public RTPSending() {
		
		try {
			//设置RTP会话的两个接口
			rtpSocket1 = new DatagramSocket(5000);
			rtcpSocket1 = new DatagramSocket(5001);
		} catch (Exception e) {
			System.out.println("RTPSession failed to obtain port");
		}
	
		//新建RTP会话，本地的rtp和rtcp端口号
		rtpSession1 = new RTPSession(rtpSocket1, rtcpSocket1);

		//建立会话参与者，这里输入的是对方的ip和rtpsession会话端口号
//		Participant p1 = new Participant("192.168.1.7", 5002, 5003);
		Participant p1 = new Participant(SipInfo.media_info_ip, Integer.valueOf(SipInfo.media_info_port).intValue(),Integer.valueOf(SipInfo.media_info_port).intValue()+1);
		//会话中加入参与者
		rtpSession1.addParticipant(p1);
		

		

	}	
	
	/*************************************************************
	 * 功能：实现发送RTP心跳保活包到服务器
	 * 参数：无
	 * 返回值：无
	 * 详细说明：
	 * 1）	心跳包的负载类型设为0x7a
	 * 2）	取Info.media_info_megic的后四组设为RTP的同步源码（Ssrc）；
	 * 3）	RTP包的内容是在Info.media_info_megic之前再加上0x00 0x01 0x00 0x10；
	 **************************************************************/
	public void SendActivePacket(){   //发送RTP心跳保活包到服务器
		byte msg[]=new byte[20];
		long Ssrc=0;
		msg[0]=0x00;
		msg[1]=0x01;
		msg[2]=0x00;
		msg[3]=0x10;
		try{
			System.arraycopy(SipInfo.media_info_magic, 0, msg, 4, 16);  //生成RTP心跳保活包，即在Info.media_info_megic之前再加上0x00 0x01 0x00 0x10
		}catch(Exception e){
			Log.d("ZR","System.arraycopy failed!");
		}
		this.rtpSession1.payloadType(0x62);	//设置心跳包的负载类型为0x7a	
		
		//取Info.media_info_megic的后四组设为RTP的同步源码（Ssrc）
		Ssrc=(long)((SipInfo.media_info_magic[15]&0x000000ff)|((SipInfo.media_info_magic[14]<<8)&0x0000ff00)|((SipInfo.media_info_magic[13]<<16)&0x00ff0000)|((SipInfo.media_info_magic[12]<<24)&0xff000000));
		this.rtpSession1.setSsrc(Ssrc);	
		
		//连续发送两次RTP激活包
		for(int i=0;i<2;i++){		
			this.rtpSession1.sendData(msg);
		}
	}

}
