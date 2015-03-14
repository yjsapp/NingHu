package com.superapp.main;





import java.io.IOException;
import java.net.DatagramSocket;

import jlibrtp.DataFrame;
import jlibrtp.Participant;
import jlibrtp.RTPAppIntf;
import jlibrtp.RTPSession;
import com.superapp.G711.AudioInfo;
import com.superapp.G711.G711;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Calling extends Activity implements OnClickListener{
	
	private StringBuffer sb=new StringBuffer("");//��������������ֵ�sb���Ǻ� 
	private EditText phone;//�绰��������
	Button one1;
	Button two2;
	Button three3;
	Button four4;
	Button five5;
	Button six6;
	Button seven7;
	Button eight8;
	Button nine9;
	Button zero0;
	Button resetr;
	Button deleted;

	//��������
	
	RTPSession rtpSession = null;
    DatagramSocket rtpSocket = null;
	DatagramSocket rtcpSocket = null;
	ReceiveRTPData recRTPData = new ReceiveRTPData();
	int frameSizeG711 = 160;
	boolean G711Running = false;
	
	
	
	MediaPlayer mMediaPlayer=null;
	
	
	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    setContentView(R.layout.calling);
    
    
    if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
     
    AudioInfo.G711= new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			
			if(!(mMediaPlayer==null)){
				 mMediaPlayer.stop();
				 mMediaPlayer.release();
				 mMediaPlayer=null;}
				
			if(msg.arg1==1){
			
			Toast.makeText(getApplicationContext(), "����ɹ�", 
					Toast.LENGTH_SHORT).show();	
			}
			
			AudioInfo.IsTalking=true;
			
			
			
			InitRtp("101.69.255.130", 5552, 5553, Integer.valueOf(AudioInfo.audio_info_port).intValue(), Integer.valueOf(AudioInfo.audio_info_port).intValue()+1);
              rtpSession.RTPSessionRegister(recRTPData, null, null);
			if(G711Running) 
				return;	
			G711Running = true;
			G711.init();				
			G711_recored();
			track.play(); // ����������Ƶ	
			
			
			super.handleMessage(msg);
		}
		}; 
		
		AudioInfo.G711HujiaoError= new Handler(){
			public void handleMessage(android.os.Message msg) {
				
				if(!(mMediaPlayer==null)){
					mMediaPlayer.stop();
					mMediaPlayer.release();
					mMediaPlayer=null;}
					
				
				if(msg.arg1==1){
					 new AlertDialog.Builder(Calling.this)      
				        .setMessage("�����е��û�æ")      
//				        .setNegativeButton("ȡ��",      
//				                new DialogInterface.OnClickListener() {      
//				                    public void onClick(DialogInterface dialog,      
//				                            int which) {      
//				                    }      
//				                })      
				        .setPositiveButton("ȷ��",      
				                new DialogInterface.OnClickListener() {      
				                    public void onClick(DialogInterface dialog,      
				                            int whichButton) {      
//				                    	
				                    }      
				                }).show(); 
				}
				super.handleMessage(msg);
			}
		};
   
		AudioInfo.G711DuiFangJieTongGuaDuan= new Handler(){
			public void handleMessage(android.os.Message msg) {
				
				
			
					
				if(msg.arg1==1){
				
				Toast.makeText(getApplicationContext(), "�Է��Ҷ�", 
						Toast.LENGTH_SHORT).show();	
				}
				
				AudioInfo.IsTalking=false;
				
				
				
				G711Running = false;
				rtpSession.endSession();	
				rtpSession = null;
			    recRTPData = new ReceiveRTPData();
				track.stop();
				
				
				super.handleMessage(msg);
			}
			}; 
		
		
		
		
		
		
		
		
		
		
		init();
	}
	

public void init(){
	phone = (EditText) findViewById(R.id.phone);
	one1=(Button)findViewById(R.id.one);
	two2=(Button)findViewById(R.id.two);
	three3=(Button)findViewById(R.id.three);
	four4=(Button)findViewById(R.id.four);
	five5=(Button)findViewById(R.id.five);
	six6=(Button)findViewById(R.id.six);
	seven7=(Button)findViewById(R.id.seven);
	eight8=(Button)findViewById(R.id.eight);
	nine9=(Button)findViewById(R.id.nine);
	zero0=(Button)findViewById(R.id.zero);
	resetr=(Button)findViewById(R.id.reset);
	deleted=(Button)findViewById(R.id.delete);
	
	
	one1.setOnClickListener(this);
    two2.setOnClickListener(this);
    three3.setOnClickListener(this);
    four4.setOnClickListener(this);
    five5.setOnClickListener(this);
    six6.setOnClickListener(this);
    seven7.setOnClickListener(this);
    eight8.setOnClickListener(this);
    nine9.setOnClickListener(this);
    resetr.setOnClickListener(this);
    deleted.setOnClickListener(this);
    zero0.setOnClickListener(this);
   
	  phone.setFocusable(false);//���ø�edittext��ý���
	  phone.setOnTouchListener(new OnTouchListener() {
	   public boolean onTouch(View v, MotionEvent event) {
	    phone.setInputType(InputType.TYPE_NULL); // �ر�����̣������������edittext��ʱ�򣬲��ᵯ��ϵͳ�Դ������뷨
	    return false;
	   }
	  
	  });}
	
public void onClick(View v) {

 switch (v.getId()) {


 case R.id.one:
	 
	
  sb.append("1");
  phone.setText(sb.toString().trim());
  break;
 case R.id.two:
  sb.append("2");
  phone.setText(sb.toString().trim());
  break;
 case R.id.three:
  sb.append("3");
  phone.setText(sb.toString().trim());
  break;
 case R.id.four:
  sb.append("4");
  phone.setText(sb.toString().trim());
  break;
 case R.id.five:
  sb.append("5");
  phone.setText(sb.toString().trim());
  break;
 case R.id.six:
  sb.append("6");
  phone.setText(sb.toString().trim());
  break;
 case R.id.seven:
  sb.append("7");
  phone.setText(sb.toString().trim());
  break;
 case R.id.eight:
  sb.append("8");
  phone.setText(sb.toString().trim());
  break;
 case R.id.nine:
  sb.append("9");
  phone.setText(sb.toString().trim());
  break;
 case R.id.zero:
  sb.append("0");
  phone.setText(sb.toString().trim());
  break;
 case R.id.delete:
  if (sb.length() - 1 >= 0) {
   sb.delete(sb.length() - 1, sb.length());
   phone.setText(sb.toString().trim());
  }
  break;
 case R.id.reset:
  sb.replace(0, sb.length(), "");
  phone.setText(sb.toString().trim());
  break;
 default:
  break;
 }
}


public void hujiao(View v){
	
	
	AudioInfo.HaveCalled=true;
	
	String userTel = (String) phone.getText().toString();
  if (userTel != null && !"".equals(userTel.toString().trim())
    && !"null".equals(userTel.toString().trim().toLowerCase())) {
	  
  
  AudioInfo.CallingToNumber=userTel;
 
	VideoAlerm();
	
	
	new Thread()
	{
		public void run()
		{
			try
			{
				
			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ehujiao, SipInfo.From, SipInfo.From)); 
			System.out.println("hujiao");
			
			
			
			sleep(10000);
    		int i=0;
    		while(AudioInfo.IsSpeechResponse==false&&i<1){
    			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ehujiao, SipInfo.From, SipInfo.From));
    			sleep(900);      		    		
    		    i++;
    		
    		}   	
				
	    		

			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
finally			
			{  					
				
				if(AudioInfo.IsSpeechResponse==true){   
					
//
//					 InitRtp(AudioInfo.audio_info_ip, 5552, 5553, Integer.valueOf(AudioInfo.audio_info_port).intValue(), Integer.valueOf(AudioInfo.audio_info_port).intValue()+1);
//
//			        rtpSession.RTPSessionRegister(recRTPData, null, null);
			       					
					AudioInfo.IsSpeechResponse=false;
			        	
				}
				else{
					
					if(!(mMediaPlayer==null)){
						mMediaPlayer.stop();
						mMediaPlayer.release();
						mMediaPlayer=null;}
					
System.out.println("bo  --da---  shi   bai  ");
				}
				} 			
		}				
	}.start(); 
	
  
  
  }

  
  
  else{
	  
	  
	  new AlertDialog.Builder(Calling.this)      
      .setMessage("��������Ҫ����ĵ�������")      
//      .setNegativeButton("ȡ��",      
//              new DialogInterface.OnClickListener() {      
//                  public void onClick(DialogInterface dialog,      
//                          int which) {      
//                  }      
//              })      
      .setPositiveButton("ȷ��",      
              new DialogInterface.OnClickListener() {      
                  public void onClick(DialogInterface dialog,      
                          int whichButton) {      
//                  	
                  }      
              }).show(); 
	  
	  
	  
  }
	
}

public void gua_duan(View v){
	
	if(AudioInfo.IsTalking){
	G711Running = false;
	track.stop();
	rtpSession.endSession();		
	
	
	
	new Thread()
	{
		public void run()
		{
			try
			{
			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Egua_duan, SipInfo.From, SipInfo.From)); 
			System.out.println("guaduan");
			sleep(3000);
    		int i=0;
    		while(AudioInfo.IsGuaDuanResponse==false&&i<1){
    			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Egua_duan, SipInfo.From, SipInfo.From));
    			sleep(300);      		    		
    		    i++;
    		
    		}   	
				
	    		

			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
finally			
			{  					
				
	
	AudioInfo.IsTalking=false;
				if(AudioInfo.IsGuaDuanResponse==true){   
					
					
					
//					Toast.makeText(getApplicationContext(), "ͨ������", 
//							Toast.LENGTH_SHORT).show();	
					AudioInfo.IsGuaDuanResponse=false;
			        	
				}
				else{
					
System.out.println("fuwuqi mang ");
				}
				} 			
		}				
	}.start(); 
	}
	
	
	else{
		
		if(!(mMediaPlayer==null)){
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer=null;
		}
		
		
		if(AudioInfo.HaveCalled){
		
			AudioInfo.HaveCalled=false;
		new Thread()
		{
			public void run()
			{
				try
				{
					
				SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.ESpeech_cancel, SipInfo.From, SipInfo.From)); 
				System.out.println("qu xiao  hujiao");
				
				
				
			// ������  ��Ҫ���   ȡ�������ǲ��ǳɹ�   �ɹ��򲻴���  ���ɹ�  ������ȴ�   �������ظ�  200code 
					
		    		

				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}
	finally			
				{  					
					

				} 			
			
			
			
			
			
			
			
			}				
		}.start(); 
		
		
		
		
		}
		
		
		else{
			
		System.out.println("buzhi  xing renhe dongzuo ");	
			
			
			
		}
		
		
		
		
		//    ����ȡ�����е�ָ��
			
	}
	}

public void VideoAlerm(){
	
	Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
	
	  mMediaPlayer = new MediaPlayer();
	     try {
	         mMediaPlayer.setDataSource(this, alert);
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	     final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	     if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
	                 mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
	                 mMediaPlayer.setLooping(true);
	                 try {
	                     mMediaPlayer.prepare();
	                 } catch (IllegalStateException e) {
	                     e.printStackTrace();
	                 } catch (IOException e) {
	                     e.printStackTrace();
	                 }
	               //  isStart = true ;
	                 mMediaPlayer.start();
	       }
	
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
		this.rtpSession.payloadType(0x08);	//�����������ĸ�������Ϊ0x7a	
		
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
	//Log.e("TAG", "min buffer size:"+min);
	
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

@Override
	protected void onDestroy() {
		super.onDestroy();
	
		G711Running = false;
		
		if(!(rtpSession==null)){
			
			rtpSession.endSession();	
			rtpSession = null;
			}	
		
		if(!(track==null)){
		track.release();
		
		
		}
		
	}

}
	
