package com.superapp.main;

import java.net.DatagramSocket;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;

import com.superapp.G711.AudioInfo;

import com.superapp.G711.G711;
import com.superapp.main.Calling.ReceiveRTPData;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Called extends Activity {
	RTPSession rtpSession = null;
    DatagramSocket rtpSocket = null;
	DatagramSocket rtcpSocket = null;
	ReceiveRTPData recRTPData = new ReceiveRTPData();
	int frameSizeG711 = 160;
	boolean G711Running = false;
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	@TargetApi(9)
	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    setContentView(R.layout.called);
    
    if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    
    
    
    AudioInfo.LaiDian= new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			
			AudioInfo.IsTalking=true;
			
			if(msg.arg1==1){
			
			Toast.makeText(getApplicationContext(), "�������", 
					Toast.LENGTH_SHORT).show();	}
			
	
			if(G711Running) 
				return;	
			G711Running = true;
			G711.init();				
			G711_recored();
			track.play(); // ����������Ƶ	
		super.handleMessage(msg);
		}
		}; 
     
    	AudioInfo.ZhuJiaoGuaDuan= new Handler(){
			public void handleMessage(android.os.Message msg) {
				AudioInfo.IsTalking=false;
				
				if(msg.arg1==1){
				
				Toast.makeText(getApplicationContext(), "�����Ѿ��Ҷ�", 
						Toast.LENGTH_SHORT).show();	}
				
				G711Running = false;
				if(!(rtpSession==null)){
				
				rtpSession.endSession();	
				rtpSession = null;
				}
				recRTPData = new ReceiveRTPData();
				
			    track.stop();
					
			super.handleMessage(msg);
			}
			}; 
    
    
    
    
    
			InitRtp(AudioInfo.audio_info_ip, 5552, 5553, Integer.valueOf(AudioInfo.audio_info_port).intValue(), Integer.valueOf(AudioInfo.audio_info_port).intValue()+1);
            rtpSession.RTPSessionRegister(recRTPData, null, null);
    
            android.os.Message LaiDian=new android.os.Message();	//����handler��Ϣ
			 LaiDian.arg1=1;
			AudioInfo.LaiDian.sendMessage(LaiDian);
    
    
	}
	
	
	
	
public void over(View view){
	
	AudioInfo.IsTalking=false;
	
	new Thread()
	{
		public void run()
		{
			try
			{
			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.ECalled_gua_duan, SipInfo.From, SipInfo.From)); 
			System.out.println("Calledguaduan");
			
				
	    		

			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
finally			
			{  					
				
	
	AudioInfo.IsTalking=false;
				
				} 			
		}				
	}.start(); 
	G711Running = false;
	if(!(rtpSession==null)){
		
		rtpSession.endSession();	
		rtpSession = null;
		}
    recRTPData = new ReceiveRTPData();
	track.stop();
	
	Toast.makeText(getApplicationContext(), "�Ҷϵ绰��", 
			Toast.LENGTH_SHORT).show();
	this.finish();
	
	
}	
	
public void InitRtp(String networkAddress, int localRtpPort, int localRtcpPort, int remoteRtpPort, int remoteRtcpPort) {
	try {
		rtpSocket = new DatagramSocket(localRtpPort);
		rtcpSocket = new DatagramSocket(localRtcpPort);
	} catch (Exception e) {
		System.out.println("RTPSession failed to obtain port");
	}
	
	rtpSession = new RTPSession(rtpSocket, rtcpSocket);
	Participant p = new Participant(networkAddress, remoteRtpPort, remoteRtcpPort);	
	rtpSession.addParticipant(p);
	rtpSession.naivePktReception(true);
}


// 4��RTP���ݽ���
//ͨ��RTPAppIntf�ӿ�ʵ�ֵģ��ýӿ���3������receiveData��userEvent��frameSize
public class ReceiveRTPData implements RTPAppIntf {	
	RTPSession rtpSession = null;	//rtpSession     �½�RTP�Ự��
	public void receiveData(DataFrame frame, Participant p) {
		AudioInfo.packet_got ++;   //��¼�յ�����Ƶ������
		if(G711Running) {
			byte[] audioBuffer = new byte[frameSizeG711];
			short[] audioData = new short [frameSizeG711];
			audioBuffer = frame.getConcatenatedData();				
			//decode ����, ��G.711��G.729���ݻ�ԭ��ԭʼPCM����
			G711.alaw2linear(audioBuffer, audioData, frameSizeG711);
			//��д����,(������Ƶ����),audioData��Ų������ݵ�����
			track.write(audioData, 0, frameSizeG711);
			
//			++count;
//			totalTimeAfter += System.currentTimeMillis();
//			Log.v("zlj","zzzzzzzzzzzz711"+"    packet:"+count+"    time"+totalTimeAfter);
			
			Log.v("zlj","zzzzzzzzzzzz711");
						}
		
    }  
	public void SendActivePacket(){   //����RTP�����������������
		byte msg[]=new byte[20];
		long Ssrc=0;
		msg[0]=0x00;
		msg[1]=0x01;
		msg[2]=0x00;
		msg[3]=0x10;
		try{
			System.arraycopy(AudioInfo.audio_info_magic, 0, msg, 4, 16);  //����RTP���������������Info.media_info_megic֮ǰ�ټ���0x00 0x01 0x00 0x10
		}catch(Exception e){
			Log.d("TAG","System.arraycopy failed!");
		}
		this.rtpSession.payloadType(0x7a);	//�����������ĸ�������Ϊ0x7a	
		
		//ȡInfo.media_info_megic�ĺ�������ΪRTP��ͬ��Դ�루Ssrc��
		Ssrc=(long)((AudioInfo.audio_info_magic[15]&0x000000ff)|((AudioInfo.audio_info_magic[14]<<8)&0x0000ff00)|((AudioInfo.audio_info_magic[13]<<16)&0x00ff0000)|((AudioInfo.audio_info_magic[12]<<24)&0xff000000));
		this.rtpSession.setSsrc(Ssrc);	
		
		//������������RTP������
		for(int i=0;i<2;i++){		
		this.rtpSession.sendData(msg);
		}
	}
    public void userEvent(int type, Participant[] participant) {
		//Do nothing
	}
	public int frameSize(int payloadType) {
		return 1;
	}			
}

/**
 * g711
 */
private void G711_recored(){
	new Thread (G711_encode).start();
}


/**
 * �����߳�
 */
Runnable G711_encode = new Runnable(){
	public void run() {			
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
		
		AudioRecord record = getAudioRecord();		
		//int frame_size = 160;
		short [] audioData = new short [frameSizeG711];
		byte  [] encodeData = new byte[frameSizeG711];
		int numRead = 0;
		
		while(G711Running) 
		{
			//��ȡ PCM(¼�� ���ݣ��� ������ audioData
			numRead = record.read(audioData, 0, frameSizeG711);
			if(numRead<=0) continue;
			
			calc2(audioData,0,numRead);		// ?		
//����pcmu����
//linear2ulaw(short[] lin, int offset, byte[] ulaw, int frames)
//lin�����PCM���ݵ����飨���룩,offset��PCMƫ����,ulaw�����G.711���ݵ����飨�����,frames��ָʾ�ж��ٸ�short�����������
		G711.linear2alaw(audioData, 0, encodeData, numRead);
		//RTP����,����jlibrtp����RTP��ʽ���ݰ���ѹ�����G.711��G.729���ݴ������
		
		
		
		         setSSRC_PAYLOAD();
			rtpSession.sendData(encodeData);
			Log.v("zlj","G711_encodeing!");
		}
		
//		// �����ӳ٣�2012-10-09
//		for(int i = 1; i <= 500; ++i) {
//			numRead = record.read(audioData, 0, frameSizeG711);
//			if(numRead<=0) continue;
//			calc2(audioData,0,numRead);					
//			//����pcmu����
//			G711.linear2ulaw(audioData, 0, encodeData, numRead);
//			totalTimeBefore += System.currentTimeMillis();
//			rtpSession.sendData(encodeData);
//			Log.v("zlj","G711 sending..."+"    packet:"+i+"    time:"+totalTimeBefore);
//		}
		record.release();
		Log.v("zlj","G711_encode stopped!");
	}
};   


/**
 * 
 *����ssrc��payload
 */
public void setSSRC_PAYLOAD(){
	byte msg[]=new byte[20];
	long Ssrc=0;
	msg[0]=0x00;
	msg[1]=0x01;
	msg[2]=0x00;
	msg[3]=0x10;
	try{
		System.arraycopy(AudioInfo.audio_info_magic, 0, msg, 4, 16);  //����RTP���������������Info.media_info_megic֮ǰ�ټ���0x00 0x01 0x00 0x10
	}catch(Exception e){
		Log.d("ZR","System.arraycopy failed!");
	}
	rtpSession.payloadType(0x08);	//����RTP���ĸ�������Ϊ0x62
	
	//ȡInfo.media_info_megic�ĺ�������ΪRTP��ͬ��Դ�루Ssrc��
	Ssrc=(long)((AudioInfo.audio_info_magic[15]&0x000000ff)|((AudioInfo.audio_info_magic[14]<<8)&0x0000ff00)|((AudioInfo.audio_info_magic[13]<<16)&0x00ff0000)|((AudioInfo.audio_info_magic[12]<<24)&0xff000000));
	rtpSession.setSsrc(Ssrc);	
}
void calc2(short[] lin,int off,int len) {
	int i,j;
	
	for (i = 0; i < len; i++)
	{
		j = lin[i+off];
		lin[i+off] = (short)(j>>1); // ?
	}
}

/**
 * PCM���ݲɼ�����
 */
    // 1��ԭʼPCM���ݲɼ������ֻ�Ӳ������˷磩��ȡԭʼ��PCM��Ƶ����
private  AudioRecord getAudioRecord(){
	int samp_rate =  8000 ;
	// ��ȡ��С���泤��min
	int min = AudioRecord.getMinBufferSize(samp_rate, 
			AudioFormat.CHANNEL_CONFIGURATION_MONO, 
			AudioFormat.ENCODING_PCM_16BIT);
	Log.e("TAG", "min buffer size:"+min);
	
	// ����һ���µ�AudioRecord��record
	AudioRecord record = null;
			record = new AudioRecord(
			MediaRecorder.AudioSource.MIC,          //��ƵԴ��MIC
			samp_rate,                              //����Ƶ�ʣ�һ��Ϊ8000hz/s 
			AudioFormat.CHANNEL_CONFIGURATION_MONO, // ������������
			AudioFormat.ENCODING_PCM_16BIT,         // PCM����λ����16λ
			min                                     // ��С���泤�ȣ�min
			);
	//  ��ʼ�ɼ�ԭʼPCM��Ƶ����
	record.startRecording();
	
	return record;
}

 // 7��PCM���ݲ���,����Androidϵͳ�ӿڣ�����PCM��Ƶ����
	int samp_rate = 8000 ;
	int maxjitter = AudioTrack.getMinBufferSize(samp_rate, 
			AudioFormat.CHANNEL_CONFIGURATION_MONO, 
			AudioFormat.ENCODING_PCM_16BIT);
	// �������ͣ���Ͳ����
	AudioTrack track = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
			samp_rate,
			AudioFormat.CHANNEL_CONFIGURATION_MONO, 
			AudioFormat.ENCODING_PCM_16BIT,
			maxjitter, 
			AudioTrack.MODE_STREAM
			);	


public static byte[] shortArrayToByteArray(short[] values) {
	byte[] s = new byte[values.length*2];
	for(int q=0; q<values.length;q++) {
		byte[] bytes = shortToBytes(values[q]);
		s[2*q] = bytes[0];
		s[2*q+1] = bytes[1];
	}
	return s;
}
public static byte[] shortToBytes(int myInt) {
	byte[] bytes = new byte[2];
	int hexBase = 0xff;
	bytes[0] = (byte) (hexBase & myInt);
	bytes[1] = (byte) (((hexBase << 8)& myInt) >> 8);
	return bytes;
}

public byte[] shortArray2byteArray(short[] data, int items) { 
	byte[] a = new byte[items*2]; 
    for (int i = 0; i < items; i++) {   
        a[i * 2] = (byte) ((data[i] >> 8) & 0xFF);   
        a[i * 2 + 1] = (byte) (data[i]  & 0xff);   
    }  
	return a;    
	}

public short bytesToShort(byte byte1, byte byte2) {
	return (short)(0xffff&((0xff&byte1) | ((0xff&byte2)<<8)));
}

public short[] byteArrayToShortArray(byte[] bytes) {
	short[] s = new short[bytes.length/2];
	for(int q=0; q<s.length;q++) {
		s[q] = bytesToShort(bytes[2*q],bytes[2*q+1]);
	}
	return s;
}
protected void onDestroy() {
	super.onDestroy();
	AudioInfo.IsTalking=false;

//	G711Running = false;
//	
//	rtpSession.endSession();		
//	track.release();
	
}  
		
		
}
		
	
