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
			
			Toast.makeText(getApplicationContext(), "来电接起", 
					Toast.LENGTH_SHORT).show();	}
			
	
			if(G711Running) 
				return;	
			G711Running = true;
			G711.init();				
			G711_recored();
			track.play(); // 启动播放音频	
		super.handleMessage(msg);
		}
		}; 
     
    	AudioInfo.ZhuJiaoGuaDuan= new Handler(){
			public void handleMessage(android.os.Message msg) {
				AudioInfo.IsTalking=false;
				
				if(msg.arg1==1){
				
				Toast.makeText(getApplicationContext(), "主叫已经挂断", 
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
    
            android.os.Message LaiDian=new android.os.Message();	//创建handler消息
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
	
	Toast.makeText(getApplicationContext(), "挂断电话了", 
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


// 4、RTP数据接收
//通过RTPAppIntf接口实现的，该接口有3个函数receiveData、userEvent、frameSize
public class ReceiveRTPData implements RTPAppIntf {	
	RTPSession rtpSession = null;	//rtpSession     新建RTP会话类
	public void receiveData(DataFrame frame, Participant p) {
		AudioInfo.packet_got ++;   //记录收到的音频包数据
		if(G711Running) {
			byte[] audioBuffer = new byte[frameSizeG711];
			short[] audioData = new short [frameSizeG711];
			audioBuffer = frame.getConcatenatedData();				
			//decode 解码, 将G.711、G.729数据还原成原始PCM数据
			G711.alaw2linear(audioBuffer, audioData, frameSizeG711);
			//　写缓存,(播放音频数据),audioData存放播放数据的数组
			track.write(audioData, 0, frameSizeG711);
			
//			++count;
//			totalTimeAfter += System.currentTimeMillis();
//			Log.v("zlj","zzzzzzzzzzzz711"+"    packet:"+count+"    time"+totalTimeAfter);
			
			Log.v("zlj","zzzzzzzzzzzz711");
						}
		
    }  
	public void SendActivePacket(){   //发送RTP心跳保活包到服务器
		byte msg[]=new byte[20];
		long Ssrc=0;
		msg[0]=0x00;
		msg[1]=0x01;
		msg[2]=0x00;
		msg[3]=0x10;
		try{
			System.arraycopy(AudioInfo.audio_info_magic, 0, msg, 4, 16);  //生成RTP心跳保活包，即在Info.media_info_megic之前再加上0x00 0x01 0x00 0x10
		}catch(Exception e){
			Log.d("TAG","System.arraycopy failed!");
		}
		this.rtpSession.payloadType(0x7a);	//设置心跳包的负载类型为0x7a	
		
		//取Info.media_info_megic的后四组设为RTP的同步源码（Ssrc）
		Ssrc=(long)((AudioInfo.audio_info_magic[15]&0x000000ff)|((AudioInfo.audio_info_magic[14]<<8)&0x0000ff00)|((AudioInfo.audio_info_magic[13]<<16)&0x00ff0000)|((AudioInfo.audio_info_magic[12]<<24)&0xff000000));
		this.rtpSession.setSsrc(Ssrc);	
		
		//连续发送两次RTP心跳包
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
 * 编码线程
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
			//读取 PCM(录音 数据）， 保存在 audioData
			numRead = record.read(audioData, 0, frameSizeG711);
			if(numRead<=0) continue;
			
			calc2(audioData,0,numRead);		// ?		
//进行pcmu编码
//linear2ulaw(short[] lin, int offset, byte[] ulaw, int frames)
//lin：存放PCM数据的数组（输入）,offset：PCM偏移量,ulaw：存放G.711数据的数组（输出）,frames：指示有多少个short数据送入编码
		G711.linear2alaw(audioData, 0, encodeData, numRead);
		//RTP发送,调用jlibrtp库用RTP格式数据包将压缩后的G.711或G.729数据打包发送
		
		
		
		         setSSRC_PAYLOAD();
			rtpSession.sendData(encodeData);
			Log.v("zlj","G711_encodeing!");
		}
		
//		// 测试延迟，2012-10-09
//		for(int i = 1; i <= 500; ++i) {
//			numRead = record.read(audioData, 0, frameSizeG711);
//			if(numRead<=0) continue;
//			calc2(audioData,0,numRead);					
//			//进行pcmu编码
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
 *设置ssrc与payload
 */
public void setSSRC_PAYLOAD(){
	byte msg[]=new byte[20];
	long Ssrc=0;
	msg[0]=0x00;
	msg[1]=0x01;
	msg[2]=0x00;
	msg[3]=0x10;
	try{
		System.arraycopy(AudioInfo.audio_info_magic, 0, msg, 4, 16);  //生成RTP心跳保活包，即在Info.media_info_megic之前再加上0x00 0x01 0x00 0x10
	}catch(Exception e){
		Log.d("ZR","System.arraycopy failed!");
	}
	rtpSession.payloadType(0x08);	//设置RTP包的负载类型为0x62
	
	//取Info.media_info_megic的后四组设为RTP的同步源码（Ssrc）
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
 * PCM数据采集部分
 */
    // 1、原始PCM数据采集，从手机硬件（麦克风）获取原始的PCM音频数据
private  AudioRecord getAudioRecord(){
	int samp_rate =  8000 ;
	// 获取最小缓存长度min
	int min = AudioRecord.getMinBufferSize(samp_rate, 
			AudioFormat.CHANNEL_CONFIGURATION_MONO, 
			AudioFormat.ENCODING_PCM_16BIT);
	Log.e("TAG", "min buffer size:"+min);
	
	// 创建一个新的AudioRecord类record
	AudioRecord record = null;
			record = new AudioRecord(
			MediaRecorder.AudioSource.MIC,          //音频源，MIC
			samp_rate,                              //采样频率，一般为8000hz/s 
			AudioFormat.CHANNEL_CONFIGURATION_MONO, // 声道，单声道
			AudioFormat.ENCODING_PCM_16BIT,         // PCM量化位数，16位
			min                                     // 最小缓存长度，min
			);
	//  开始采集原始PCM音频数据
	record.startRecording();
	
	return record;
}

 // 7、PCM数据播放,调用Android系统接口，播放PCM音频数据
	int samp_rate = 8000 ;
	int maxjitter = AudioTrack.getMinBufferSize(samp_rate, 
			AudioFormat.CHANNEL_CONFIGURATION_MONO, 
			AudioFormat.ENCODING_PCM_16BIT);
	// 播放类型，听筒播放
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
		
	
