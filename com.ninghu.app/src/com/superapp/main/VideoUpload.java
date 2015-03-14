package com.superapp.main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.superapp.rtp.RTPSending;
import com.superapp.sip.SipInfo;
import com.superapp.tools.SdCardStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;


@SuppressLint("NewApi")
public class VideoUpload extends Activity implements Callback, Runnable{

	private static final String TAG = "VideoCamera";
	
	public static RTPSending rtpsending;
	private Camera c;
	StreamBuf m_pVFragmentBuffer;       //RTP缓存类
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        
      
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        
        
        
        
      
        
        setContentView(R.layout.video_upload);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        m_pVFragmentBuffer = new StreamBuf(200,5); 
        rtpsending = new RTPSending();
        InitSurfaceView();
        InitMediaSharePreference();
        
        
        
       new Thread (sending).start();   
    }

    //初始化SurfaceView
    private SurfaceView mSurfaceView;
	private void InitSurfaceView() {
		mSurfaceView = (SurfaceView) this.findViewById(R.id.surface_camera);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.setVisibility(View.VISIBLE);
	}
	
	//初始化，记录mdat开始位置的参数
	SharedPreferences sharedPreferences;
	private final String mediaShare = "media";
    private void InitMediaSharePreference() {
		sharedPreferences = this.getSharedPreferences(mediaShare, MODE_PRIVATE);		
	}


    private SurfaceHolder mSurfaceHolder;
    private boolean mMediaRecorderRecording = false;
    
	public void surfaceCreated(SurfaceHolder holder) {
		mSurfaceHolder = holder;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mSurfaceHolder = holder;
		if(!mMediaRecorderRecording) {
			InitLocalSocket();
			getSPSAndPPS();
			initializeVideo();
			startVideoRecording();
		}
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		
		 try {
			dataInput.close();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		
		
		
	}
	
	//初始化LocalServerSocket LocalSocket
	LocalServerSocket lss;
	LocalSocket receiver, sender;
	
	private void InitLocalSocket(){
		try {
			lss = new LocalServerSocket("H264");
			receiver = new LocalSocket();
			
			receiver.connect(new LocalSocketAddress("H264"));
			receiver.setReceiveBufferSize(500000);
			receiver.setSendBufferSize(50000);
			
			sender = lss.accept();
			sender.setReceiveBufferSize(500000);
			sender.setSendBufferSize(50000);
			
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			this.finish();
			return;
		}
		
	}
	
	//得到序列参数集SPS和图像参数集PPS,如果已经存储在本地
	private void getSPSAndPPS(){
		StartMdatPlace = sharedPreferences.getInt(
				String.format("mdata_%d%d.mdat", videoWidth, videoHeight), -1);
		
		if(StartMdatPlace != -1) {
			byte[] temp = new byte[100];
			try {
				FileInputStream file_in = VideoUpload.this.openFileInput(
						String.format("%d%d.sps", videoWidth,videoHeight));
				
				int index = 0;
				int read=0;
				while(true)
				{
					read = file_in.read(temp,index,10);
					if(read==-1) break;
					else index += read;
				}
				Log.e(TAG, "sps length:"+index);
				SPS = new byte[index];
				System.arraycopy(temp, 0, SPS, 0, index);
				               
				file_in.close();
				
				index =0;
				//read PPS
				file_in = VideoUpload.this.openFileInput(
						String.format("%d%d.pps", videoWidth,videoHeight));
				while(true)
				{
					read = file_in.read(temp,index,10);
					if(read==-1) break;
					else index+=read;
				}
				Log.e(TAG, "pps length:"+index);
				PPS = new byte[index];
				System.arraycopy(temp, 0, PPS, 0, index);
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				//e.printStackTrace();
				Log.e(TAG, e.toString());
			}
		} else {
			SPS = null;
			PPS = null;
		}
	}
	
	//初始化MediaRecorder
	private MediaRecorder mMediaRecorder = null;
	private int videoWidth = 176;
	private int videoHeight = 144;
	private int videoRate = 10;
	
	private String fd = "data/data/com.superapp.main/h264.3gp";
	
	private boolean initializeVideo(){
		if(mSurfaceHolder == null) {
			return false;
		}
		
		mMediaRecorderRecording = true;
		
		if(mMediaRecorder == null) {
			mMediaRecorder = new MediaRecorder();
		} else {
			mMediaRecorder.reset();
		}
		   c = Camera.open();
           c.setDisplayOrientation(90);
           c.unlock();
       mMediaRecorder.setCamera(c);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setVideoFrameRate(videoRate);
		mMediaRecorder.setVideoSize(videoWidth, videoHeight);
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		mMediaRecorder.setMaxDuration(0);
		mMediaRecorder.setMaxFileSize(0);
		if(SPS==null)
		{
			mMediaRecorder.setOutputFile(fd);
		}
		else
		{
			mMediaRecorder.setOutputFile(sender.getFileDescriptor());
		}

		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			releaseMediaRecorder();
		}
		
		return true;
	}

	//释放MediaRecorder资源
	private void releaseMediaRecorder(){
		if(mMediaRecorder != null) {
			if(mMediaRecorderRecording) {
				mMediaRecorder.stop();
				mMediaRecorderRecording = false;
			}
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
			c.release();
			c=null;
		}
	}
	
	//开始录像，启动线程
	private void startVideoRecording() {
		new Thread(this).start();
	}
	DataInputStream dataInput;
	private final int MAXFRAMEBUFFER = 20480;//20K
	private byte[] h264frame = new byte[MAXFRAMEBUFFER];
	private final byte[] head = new byte[]{0x00,0x00,0x00,0x01};
	//private RandomAccessFile file_test;
	@SuppressLint("SdCardPath")
	public void run() {
		try {
			
			if(SPS == null) {
				Log.e(TAG, "Rlease MediaRecorder and get SPS and PPS");
				Thread.sleep(1000);
				//释放MediaRecorder资源
				releaseMediaRecorder();
				//从已采集的视频数据中获取SPS和PPS
				findSPSAndPPS();
				//找到后重新初始化MediaRecorder
				initializeVideo();
			}			
			
			 dataInput = new DataInputStream(receiver.getInputStream());
			//先读取ftpy box and mdat box, 目的是skip ftpy and mdat data,(decisbe by phone)
			dataInput.read(h264frame, 0, StartMdatPlace);
			
			//try {
				SdCardStore Sd=new SdCardStore();
				Sd.createSDCardDir();
				Sd.Creat();
				
				//File file = new File("/sdcard/encoder.h264");
			
				
			
				
				
				
//				if (file.exists())
//					file.delete();
				//file_test = new RandomAccessFile(file, "rw");
			//} catch (Exception ex) {
				//Log.v("System.out", ex.toString());
			//}
			Sd.Write(head);
			Sd.Write(SPS);//write sps
			printResult("SPS",SPS,SPS.length);
			
			
			Sd.Write(head);
			Sd.Write(PPS);//write pps
			printResult("PPS",PPS,PPS.length);
		
			int h264length =0;
			
			while(mMediaRecorderRecording) {
				h264length = dataInput.readInt();
				Log.e(TAG, "h264 length :" + h264length);
//				int number=0 , num=0;
//				int frame_size = 1024;
//				file_test.write(head);
//				while(number<h264length)
//				{
//					int lost=h264length-number;
//					num = dataInput.read(h264frame,0,frame_size<lost?frame_size:lost);
//					Log.d(TAG,String.format("H264 %d,%d,%d", h264length,number,num));
//					number+=num;
//					file_test.write(h264frame, 0, num);
//				}
				ReadSize(h264length, dataInput);
				
				byte[] h264 = new byte[h264length];
				System.arraycopy(h264frame, 0, h264, 0, h264length);
				
				Sd.Write(head);
				Sd.Write(h264);//write selice
				
				DivideNal(h264);	
				
//				 new Thread (sending).start();   
				
			}
			 
			Sd.Close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void ReadSize(int h264length,DataInputStream dataInput) throws IOException, InterruptedException{
		int read = 0;
		int temp = 0;
		while(read<h264length)
		{
			temp= dataInput.read(h264frame, read, h264length-read);
			Log.e(TAG, String.format("h264frame %d,%d,%d", h264length,read,h264length-read));
			if(temp==-1)
			{
				Log.e(TAG, "no data get wait for data coming.....");
				Thread.sleep(2000);
				continue;
			}
			read += temp;
		}
	}
	
	//从 fd文件中找到SPS And PPS
	private byte[] SPS;
	private byte[] PPS;
	private int StartMdatPlace = 0;
	private void findSPSAndPPS() throws Exception{
		File file = new File(fd);
		FileInputStream fileInput = new FileInputStream(file);
		
		int length = (int)file.length();
		byte[] data = new byte[length];
		
		fileInput.read(data);
		
		final byte[] mdat = new byte[]{0x6D,0x64,0x61,0x74};
		final byte[] avcc = new byte[]{0x61,0x76,0x63,0x43};
		
		for(int i=0 ; i<length; i++){
			if(data[i] == mdat[0] && data[i+1] == mdat[1] && data[i+2] == mdat[2] && data[i+3] == mdat[3]){
				StartMdatPlace = i+4;//find mdat
				break;
			}
		}
		Log.e(TAG, "StartMdatPlace:"+StartMdatPlace);
		//记录到xml文件里
		String mdatStr = String.format("mdata_%d%d.mdat",videoWidth,videoHeight);
		Editor editor = sharedPreferences.edit();
		editor.putInt(mdatStr, StartMdatPlace);
		editor.commit();
		
		for(int i=0 ; i<length; i++){
			if(data[i] == avcc[0] && data[i+1] == avcc[1] && data[i+2] == avcc[2] && data[i+3] == avcc[3]){
				int sps_start = i+3+7;//其中i+3指到avcc的c，再加7跳过6位AVCDecoderConfigurationRecord参数
				
				//sps length and sps data
				byte[] sps_3gp = new byte[2];//sps length
				sps_3gp[1] = data[sps_start];
				sps_3gp[0] = data[sps_start + 1];
				int sps_length = bytes2short(sps_3gp);
				Log.e(TAG, "sps_length :" + sps_length);
				
				sps_start += 2;//skip length
				SPS = new byte[sps_length];
				System.arraycopy(data, sps_start, SPS, 0, sps_length);
				//save sps
				FileOutputStream file_out = VideoUpload.this.openFileOutput(
						String.format("%d%d.sps",videoWidth,videoHeight), 
						Context.MODE_PRIVATE);
				file_out.write(SPS);
				file_out.close();
				
				//pps length and pps data
				int pps_start = sps_start + sps_length + 1;
				byte[] pps_3gp =new byte[2];
				pps_3gp[1] = data[pps_start];
				pps_3gp[0] =data[pps_start+1];
				int pps_length = bytes2short(pps_3gp);
				Log.e(TAG, "PPS LENGTH:"+pps_length);
				
				pps_start+=2;
				
				PPS = new byte[pps_length];
				System.arraycopy(data, pps_start, PPS,0,pps_length);
				
				
				//Save PPS
				file_out = VideoUpload.this.openFileOutput(
						String.format("%d%d.pps",videoWidth,videoHeight),
						Context.MODE_PRIVATE);
				file_out.write(PPS);
				file_out.close();
				break;
			}
		}
		
	}
	
	//计算长度
	public short bytes2short(byte[] b)
    {
	            short mask=0xff;
	            short temp=0;
	            short res=0;
	            for(int i=0;i<2;i++)
	            {
	                res<<=8;
	                temp=(short)(b[1-i]&mask);
	                res|=temp;
	            }
	            return res;
    }



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
			new Thread()
		{
			public void run()
			{
				try
				{
					
					if(mMediaRecorderRecording)
					{
						releaseMediaRecorder();
						mMediaRecorderRecording = false;
						
						try {
							lss.close();
							receiver.close();
							sender.close();
						} catch (IOException e) {
							Log.e(TAG, e.toString());
						}
						
						
					}
		    		

				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}		
			}				
		}.start(); 
		this.finish();
	}

	
	
	/**
	 * 将每个NAL分片打包，放入缓存 
	 */
	public void DivideNal(byte[] h264){

		if(h264.length>0)
		{	
			
			boolean firstPktReceived = false;  //每一个新的NAL设置首包打包状态为false，即没有打包首包
			int pktflag = 0; //记录打包分片的索引
			boolean status = true;   //若未打包到末包，则此状态一直为true
			int divide_length = 1024;

		
			Log.e("VideoCameraActivity","encode len:"+h264.length);//打印编码结果的长度
			if(h264.length > divide_length){
				int i = 0;    //标记一个nal包的序列号
				while(status){    //循环，直到一帧打包完为止
					if(!firstPktReceived){   //首包
						Log.d("ZR","首包");
						byte[] rtppkt = new byte[divide_length + 2];
						rtppkt[0] = (byte) (h264[0] & 0xe0);
						rtppkt[0] = (byte) (rtppkt[0] + 0x1c);
						rtppkt[1] = (byte) (0x80 + (h264[0]&0x1f));
						
						try{
							System.arraycopy(h264,0,rtppkt, 2,divide_length);	
						}catch(Exception e){
							e.printStackTrace();
						}
						pktflag = pktflag + divide_length;
						firstPktReceived = true;
						i++;
						Log.e("VideoCameraActivity","sequence:"+i);						
						
						/************************************************************************/
						StreamBufNode pRtpFrameNode = new StreamBufNode(rtppkt); //把RTP包存入pRtpFrameNode节点	
						m_pVFragmentBuffer.AddToBuf(pRtpFrameNode); 
						/************************************************************************/
						
						status = true;
					}
					else{
						if(h264.length - pktflag > divide_length){
							Log.d("ZR","中包");
							//中包
							byte[] rtppkt = new byte[divide_length + 2];
							rtppkt[0] = (byte) (h264[0] & 0xe0);
							rtppkt[0] = (byte) (rtppkt[0] + 0x1c);
							rtppkt[1] = (byte) (0x00 + (h264[0]&0x1f));
							
							try{
								System.arraycopy(h264,pktflag,rtppkt, 2,divide_length);	
							}catch(Exception e){
								e.printStackTrace();
							}
							pktflag = pktflag + divide_length;
							firstPktReceived = true;
							
							/************************************************************************/
							StreamBufNode pRtpFrameNode = new StreamBufNode(rtppkt); //把RTP包存入pRtpFrameNode节点
							m_pVFragmentBuffer.AddToBuf(pRtpFrameNode); 
							/************************************************************************/
							
							i++;
							Log.e("VideoCameraActivity","sequence:"+i);
							status = true;
						}
						else{
							Log.d("ZR","末包");
							//末包
							byte[] rtppkt = new byte[h264.length - pktflag + 2];
							rtppkt[0] = (byte) (h264[0] & 0xe0);
							rtppkt[0] = (byte) (rtppkt[0] + 0x1c);
							rtppkt[1] = (byte) (0x40 + (h264[0]&0x1f));
							try{
								System.arraycopy(h264,pktflag,rtppkt, 2,h264.length - pktflag);	
							}catch(Exception e){
								e.printStackTrace();
							}

							/************************************************************************/
							StreamBufNode pRtpFrameNode = new StreamBufNode(rtppkt); //把RTP包存入pRtpFrameNode节点
							m_pVFragmentBuffer.AddToBuf(pRtpFrameNode); 
							/************************************************************************/
							
							i++;
							Log.e("VideoCameraActivity","sequence:"+i);
							status = false;  //打包组包结束，下一步进行解码
						}
					}
				
				}
			}
			else{   //编码结果长度比较小，不分片
				Log.d("ZR","不分片");
				byte[] rtppkt = new byte[h264.length];
				//打包
				try{
					System.arraycopy(h264,0,rtppkt, 0,h264.length);	
				}catch(Exception e){
					e.printStackTrace();
				}

				/************************************************************************/
				StreamBufNode pRtpFrameNode = new StreamBufNode(rtppkt); //把RTP包存入pRtpFrameNode节点
				m_pVFragmentBuffer.AddToBuf(pRtpFrameNode); 
				/************************************************************************/
				
			}
							
		}

	}
	
	private void printResult(String type, byte[] bt, int len) { 
        System.out.println(type + "长度为：" + len); 
        String cont = type + "的内容为："; 
        System.out.print(cont); 
        for (int ix = 0; ix < len; ++ix) { 
            System.out.printf("%02x ", bt[ix]); 
        } 
        System.out.println("\n----------"); 
    } 
	
	
	
	
	
	
	
	/**数据包发送线程*/
	private Runnable sending = new Runnable(){
		public void run() {
			while(true){
				
				if(m_pVFragmentBuffer.IsEmpty()){
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}		
				else{
//					if(Info.pktNumber <= 5000){
//						System.out.println("发送数据！");
//						StreamBufNode pStreamBufNode;
//						pStreamBufNode = m_pVFragmentBuffer.GetFromBuf();
//						byte[] RTPPacket = pStreamBufNode.GetData();
//						setSSRC_PAYLOAD();   //设置ssrc与payload
//						test.rtpSession1.sendData(RTPPacket);   //发送打包数据
//						Info.pktNumber ++;
//					}
					System.out.println("发送数据！");
					StreamBufNode pStreamBufNode;
					pStreamBufNode = m_pVFragmentBuffer.GetFromBuf();
					byte[] RTPPacket = pStreamBufNode.GetData();
					setSSRC_PAYLOAD();   //设置ssrc与payload
					rtpsending.rtpSession1.sendData(RTPPacket);   //发送打包数据
					SipInfo.pktNumber ++;
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
			}
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
			System.arraycopy(SipInfo.media_info_magic, 0, msg, 4, 16);  //生成RTP心跳保活包，即在Info.media_info_megic之前再加上0x00 0x01 0x00 0x10
		}catch(Exception e){
			Log.d("ZR","System.arraycopy failed!");
		}
		rtpsending.rtpSession1.payloadType(0x62);	//设置RTP包的负载类型为0x62
		
		//取Info.media_info_megic的后四组设为RTP的同步源码（Ssrc）
		Ssrc=(long)((SipInfo.media_info_magic[15]&0x000000ff)|((SipInfo.media_info_magic[14]<<8)&0x0000ff00)|((SipInfo.media_info_magic[13]<<16)&0x00ff0000)|((SipInfo.media_info_magic[12]<<24)&0xff000000));
		rtpsending.rtpSession1.setSsrc(Ssrc);	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
