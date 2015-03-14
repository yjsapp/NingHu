package com.superapp.main;



import java.io.IOException;

import org.opencore.avch264.NativeH264Encoder;
import com.superapp.info.Info;
import com.superapp.rtp.RTPSending;
import com.superapp.sip.SipInfo;
import com.superapp.tools.SdCardStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;

public class H264Sending extends Activity implements Callback, PreviewCallback{
	
	SurfaceView 	m_surface;
	SurfaceHolder   m_surfaceHolder;
	public static RTPSending rtpsending=null;
	StreamBuf m_pVFragmentBuffer;       //RTP缓存类
	SdCardStore Sd;
	final String TAG = H264Sending.class.getSimpleName();    //取得类名
	NativeH264Encoder encode;
	/**手机摄像头的个数*/
	private int numCamera;       
	/**前置摄像头的Id*/
    private int cameraId_front;   
    /**后置摄像头的Id*/
    private int cameraId_back;
    /**判断前置摄像头是否存在的标志位*/
    private boolean frontExist = false; 
    /**打包发送的数组大小定义*/
	byte[] rtppkt = new byte[Info.divide_length + 2];
	
    /** Called when the activity is first created. */
    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h264sending);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(rtpsending!=null){
        	
        	rtpsending=null;
        }
        
        Sd=new SdCardStore();
		Sd.createSDCardDir();
		Sd.Creat();
        
       if(rtpsending==null){
        rtpsending = new RTPSending();  } //RTP会话的一些参数设置
       
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  //获取一个windowmanager对象
        
        //自动获取屏幕分辨率
	    Display display = manager.getDefaultDisplay();   //获取默认演示对象
	    Point screenResolution = new Point(display.getWidth(), display.getHeight());  //取得屏幕分辨率
	    
	    Log.e("TAG", "Screen resolution: " + screenResolution);
        
        

        
        m_surface = (SurfaceView)this.findViewById(R.id.h264suf);   //surface建立，按顺序执行 surfaceCreated和surfaceChanged函数，进行编码前演示和解码后播放
        // 得到SurfaceHolder对象
        SurfaceHolder holder= m_surface.getHolder();  //返回m_surface的表示符（句柄）
        //设置回调函数
		holder.addCallback(H264Sending.this);   //添加回调接口
		//设置风格
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		

    }

    
    private Camera mCamera ;
    private final int PreviewFrameRate = 10;  //演示帧率
    private final int PreviewWidth = 176;     //水平像素
    private final int PreviewHeight = 144;     //垂直像素
    
    

    /**
     * 检测到surface有变化时调用此函数，对视频进行预览
     */
	@SuppressLint("NewApi")
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.e("", "surfaceChanged");
		NativeH264Encoder.InitEncoder(PreviewWidth, PreviewHeight, PreviewFrameRate);
		
		//获取手机摄像头的个数
				numCamera = Camera.getNumberOfCameras();   
		        CameraInfo info = new CameraInfo();
		        for(int i = 0;i< numCamera;i++){
		        	Camera.getCameraInfo(i, info);
		        	if(info.facing == CameraInfo.CAMERA_FACING_BACK){
		        		cameraId_back = i;     //获取后置摄像头的Id
		        	}
		        	if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
		        		cameraId_front = i;    //获取前置摄像头的Id
		        		frontExist = true;
		        	}
		        }
//		        if(frontExist){
//		        	mCamera = Camera.open(cameraId_front);
//					DisplayToast("打开前置摄像头");
//				}else{
					mCamera = Camera.open(cameraId_back);
					DisplayToast("打开后置摄像头");
				//}  
				try {
					mCamera.setPreviewDisplay(m_surfaceHolder);//设置surface进行实时演示，m_surfaceHolder表示在surface上演示的位置，null表示清楚
					//设置回叫函数，每个演示帧的时候都被呼叫
					 //参数this是回叫信号对象接收每个演示帧的拷贝，null则停止接收回叫信号。
					mCamera.setPreviewCallback(this);  
					mCamera.setDisplayOrientation(90);
					//set camera 
					Camera.Parameters parame = mCamera.getParameters();    //获取配置参数对象
					parame.setPreviewFrameRate(PreviewFrameRate);    //设置Camera的演示帧率
					parame.setPreviewSize(PreviewWidth, PreviewHeight);    //设置屏幕分辨率
					parame.set("orientation", "portrait");
					//android2.3.3以后无需下步
					mCamera.setParameters(parame);
					//通过SurfaceView显示取景画面
					//开始对演示帧进行捕获和绘图到surface
					mCamera.startPreview();   	
					// 自动对焦
					mCamera.autoFocus(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
		

	}
/**
 * 此函数在surface第一次创建时立即执行，可对surface的一些参数进行设置
 */
	public void surfaceCreated(SurfaceHolder holder) {  
		Log.e("", "surfaceCreated");   
		m_surfaceHolder = holder;   //参数设置
	}

	/**
	 * 在一个surface被销毁之前调用
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {  
		Log.e("", "surfaceDestroyed");
		if(mCamera != null)  //没有背面摄像头的情况
		{
			mCamera.setPreviewCallback(null);//must do this，停止接收回叫信号
			mCamera.stopPreview();   //停止捕获和绘图
			mCamera.release();   //断开与摄像头的连接，并释放摄像头资源
		}
		this.Sd.Close();    //关闭此输出流，并释放与此流有关的所有系统资源
		NativeH264Encoder.DeinitEncoder();
		rtpsending=null;
	}

	final byte[] head = {0x00,0x00,0x00,0x01};
	int[]frame = new int[PreviewWidth*PreviewHeight];  //新建帧数组，大小等于分辨率长*宽
	static long time = System.currentTimeMillis();   //以毫秒形式返回当前系统时间
	
	/**
	 * onPreviewFrame是一个回调函数，当预览画面演示第一个预览帧时调用，在事件线程open()被使用的时候呼叫，播放解码视频
	 * @param
	 * data:表示之前演示帧
	 * camera：摄像头对象
	 */
	
	public void onPreviewFrame(byte[] data, Camera camera) {    //演示帧演示后调用

		byte[]encodeResult = NativeH264Encoder.EncodeFrame(data, time);  //进行编码，将编码结果存放进数组
		time += 1000/PreviewFrameRate;    //计算出一帧所耗费的时间，单位为毫秒
		int encodeState = NativeH264Encoder.getLastEncodeStatus();    //获取最后的编码状态，0——表示成功！！
		
		if(SipInfo.endView == true){    //收到BYE命令，关闭当前视频采集功能，重新回到注册之后的等待邀请界面
			SipInfo.keepalive.setMediaActive(false); 	    	
			SipInfo.nalfirst = 0; //0表示未收到首包，1表示收到
			SipInfo.index = 0;
			SipInfo.query_response = false;
			SipInfo.endView = false;
          	this.finish();
		}
		
		if(encodeState ==0 && encodeResult.length>0)
		{	
			Log.e(TAG,"encode len:"+encodeResult.length);//打印编码结果的长度
			setSSRC_PAYLOAD();
			DivideAndSendNal(encodeResult);		
			//编码结果写入文件保存
			 
			
			Sd.Write(head);   //将head中的字节写入输出流，即写入此路径下的文件中
			Sd.Write(encodeResult);   //编码结果写入输出流
		
		}

	}
	
	
	
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
		rtpsending.rtpSession1.payloadType(0x7a);	//设置RTP包的负载类型为0x62
		
		//取Info.media_info_megic的后四组设为RTP的同步源码（Ssrc）
		Ssrc=(long)((SipInfo.media_info_magic[15]&0x000000ff)|((SipInfo.media_info_magic[14]<<8)&0x0000ff00)|((SipInfo.media_info_magic[13]<<16)&0x00ff0000)|((SipInfo.media_info_magic[12]<<24)&0xff000000));
		rtpsending.rtpSession1.setSsrc(Ssrc);
		
		
		for(int i=0;i<2;i++){		
			rtpsending.rtpSession1.sendData(msg);
		}
	}
	
	/**分片、发送方法*/
	public void DivideAndSendNal(byte[] h264){
		
		if(h264.length > 0){  //有数据才进行分片发送操作
			if(h264.length > Info.divide_length){
				Info.dividingFrame = true;
				Info.status = true;
				Info.firstPktReceived = false;
				Info.pktflag = 0;

				while(Info.status){
					if(!Info.firstPktReceived){  //首包
						sendFirstPacket(h264);
					}
					else{
						if(h264.length - Info.pktflag > Info.divide_length){  //中包
							sendMiddlePacket(h264);
						}
						else{   //末包
							sendLastPacket(h264);
						}
					} //end of 首包
				}//end of while
			}
			else{   //不分片包
				sendCompletePacket(h264);
			}
		}
	}
	
	/**发送首包*/
	public void sendFirstPacket(byte[] h264){

		rtppkt[0] = (byte) (h264[0] & 0xe0);
		rtppkt[0] = (byte) (rtppkt[0] + 0x1c);
		rtppkt[1] = (byte) (0x80 + (h264[0]&0x1f));
		try{
			System.arraycopy(h264,0,rtppkt, 2,Info.divide_length);	
		}catch(Exception e){
			e.printStackTrace();
		}
		Info.pktflag = Info.pktflag + Info.divide_length;
		Info.firstPktReceived = true;					
		//设置RTP包的负载类型为0x62
		rtpsending.rtpSession1.payloadType(0x62);
		//发送打包数据
		rtpsending.rtpSession1.sendData(rtppkt);   //发送打包数据
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**发送中包*/
	public void sendMiddlePacket(byte[] h264){

		rtppkt[0] = (byte) (h264[0] & 0xe0);
		rtppkt[0] = (byte) (rtppkt[0] + 0x1c);
		rtppkt[1] = (byte) (0x00 + (h264[0]&0x1f));
		
		try{
			System.arraycopy(h264,Info.pktflag,rtppkt, 2,Info.divide_length);	
		}catch(Exception e){
			e.printStackTrace();
		}
		Info.pktflag = Info.pktflag + Info.divide_length;
		//设置RTP包的负载类型为0x62
rtpsending.rtpSession1.payloadType(0x62);
		//发送打包数据
		rtpsending.rtpSession1.sendData(rtppkt);   //发送打包数据   //发送打包数据
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**发送末包*/
	public static void sendLastPacket(byte[] h264){
		byte[] rtppktLast = new byte[h264.length - Info.pktflag + 2];
		rtppktLast[0] = (byte) (h264[0] & 0xe0);
		rtppktLast[0] = (byte) (rtppktLast[0] + 0x1c);
		rtppktLast[1] = (byte) (0x40 + (h264[0]&0x1f));
		try{
			System.arraycopy(h264,Info.pktflag,rtppktLast, 2,h264.length - Info.pktflag);	
		}catch(Exception e){
			e.printStackTrace();
		}
		//设置RTP包的负载类型为0x62
rtpsending.rtpSession1.payloadType(0x62);	
		//发送打包数据
		rtpsending.rtpSession1.sendData(rtppktLast);   //发送打包数据  //发送打包数据
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Info.status = false;  //打包组包结束，下一步进行解码
		Info.dividingFrame = false;  //一帧分片打包完毕，时间戳改下一帧
	}
	
	/**发送完整包*/
	public void sendCompletePacket(byte[] h264){
		//设置RTP包的负载类型为0x62
        rtpsending.rtpSession1.payloadType(0x62);
		//发送打包数据
		rtpsending.rtpSession1.sendData(h264);   //发送打包数据   //发送打包数据
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	 
	 protected static final int MENU_RTPSEND=Menu.FIRST;
	 	@Override
	 	public boolean onCreateOptionsMenu(Menu menu) 
	 	{
	 		super.onCreateOptionsMenu(menu);
	 		menu.add(0, MENU_RTPSEND, 0, "RTP包发送");
	 		return true;
	 	}
	 	

	 	@Override
	 	public boolean onOptionsItemSelected(MenuItem item) 
	 	{
	 		super.onOptionsItemSelected(item);
	 		switch (item.getItemId())
	 		{
	 		case MENU_RTPSEND:
	 			DisplayToast("RTP包发送"+SipInfo.pktNumber);
	 			break;
	 		
	 		}
	 		return true;
	 	}
		public void DisplayToast(String str)
		{
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		}
	
}
