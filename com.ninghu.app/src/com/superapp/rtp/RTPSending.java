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
			//����RTP�Ự�������ӿ�
			rtpSocket1 = new DatagramSocket(5000);
			rtcpSocket1 = new DatagramSocket(5001);
		} catch (Exception e) {
			System.out.println("RTPSession failed to obtain port");
		}
	
		//�½�RTP�Ự�����ص�rtp��rtcp�˿ں�
		rtpSession1 = new RTPSession(rtpSocket1, rtcpSocket1);

		//�����Ự�����ߣ�����������ǶԷ���ip��rtpsession�Ự�˿ں�
//		Participant p1 = new Participant("192.168.1.7", 5002, 5003);
		Participant p1 = new Participant(SipInfo.media_info_ip, Integer.valueOf(SipInfo.media_info_port).intValue(),Integer.valueOf(SipInfo.media_info_port).intValue()+1);
		//�Ự�м��������
		rtpSession1.addParticipant(p1);
		

		

	}	
	
	/*************************************************************
	 * ���ܣ�ʵ�ַ���RTP�����������������
	 * ��������
	 * ����ֵ����
	 * ��ϸ˵����
	 * 1��	�������ĸ���������Ϊ0x7a
	 * 2��	ȡInfo.media_info_megic�ĺ�������ΪRTP��ͬ��Դ�루Ssrc����
	 * 3��	RTP������������Info.media_info_megic֮ǰ�ټ���0x00 0x01 0x00 0x10��
	 **************************************************************/
	public void SendActivePacket(){   //����RTP�����������������
		byte msg[]=new byte[20];
		long Ssrc=0;
		msg[0]=0x00;
		msg[1]=0x01;
		msg[2]=0x00;
		msg[3]=0x10;
		try{
			System.arraycopy(SipInfo.media_info_magic, 0, msg, 4, 16);  //����RTP���������������Info.media_info_megic֮ǰ�ټ���0x00 0x01 0x00 0x10
		}catch(Exception e){
			Log.d("ZR","System.arraycopy failed!");
		}
		this.rtpSession1.payloadType(0x62);	//�����������ĸ�������Ϊ0x7a	
		
		//ȡInfo.media_info_megic�ĺ�������ΪRTP��ͬ��Դ�루Ssrc��
		Ssrc=(long)((SipInfo.media_info_magic[15]&0x000000ff)|((SipInfo.media_info_magic[14]<<8)&0x0000ff00)|((SipInfo.media_info_magic[13]<<16)&0x00ff0000)|((SipInfo.media_info_magic[12]<<24)&0xff000000));
		this.rtpSession1.setSsrc(Ssrc);	
		
		//������������RTP�����
		for(int i=0;i<2;i++){		
			this.rtpSession1.sendData(msg);
		}
	}

}
