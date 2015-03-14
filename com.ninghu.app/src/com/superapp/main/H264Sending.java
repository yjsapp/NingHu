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
	StreamBuf m_pVFragmentBuffer;       //RTP������
	SdCardStore Sd;
	final String TAG = H264Sending.class.getSimpleName();    //ȡ������
	NativeH264Encoder encode;
	/**�ֻ�����ͷ�ĸ���*/
	private int numCamera;       
	/**ǰ������ͷ��Id*/
    private int cameraId_front;   
    /**��������ͷ��Id*/
    private int cameraId_back;
    /**�ж�ǰ������ͷ�Ƿ���ڵı�־λ*/
    private boolean frontExist = false; 
    /**������͵������С����*/
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
        rtpsending = new RTPSending();  } //RTP�Ự��һЩ��������
       
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  //��ȡһ��windowmanager����
        
        //�Զ���ȡ��Ļ�ֱ���
	    Display display = manager.getDefaultDisplay();   //��ȡĬ����ʾ����
	    Point screenResolution = new Point(display.getWidth(), display.getHeight());  //ȡ����Ļ�ֱ���
	    
	    Log.e("TAG", "Screen resolution: " + screenResolution);
        
        

        
        m_surface = (SurfaceView)this.findViewById(R.id.h264suf);   //surface��������˳��ִ�� surfaceCreated��surfaceChanged���������б���ǰ��ʾ�ͽ���󲥷�
        // �õ�SurfaceHolder����
        SurfaceHolder holder= m_surface.getHolder();  //����m_surface�ı�ʾ���������
        //���ûص�����
		holder.addCallback(H264Sending.this);   //��ӻص��ӿ�
		//���÷��
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		

    }

    
    private Camera mCamera ;
    private final int PreviewFrameRate = 10;  //��ʾ֡��
    private final int PreviewWidth = 176;     //ˮƽ����
    private final int PreviewHeight = 144;     //��ֱ����
    
    

    /**
     * ��⵽surface�б仯ʱ���ô˺���������Ƶ����Ԥ��
     */
	@SuppressLint("NewApi")
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.e("", "surfaceChanged");
		NativeH264Encoder.InitEncoder(PreviewWidth, PreviewHeight, PreviewFrameRate);
		
		//��ȡ�ֻ�����ͷ�ĸ���
				numCamera = Camera.getNumberOfCameras();   
		        CameraInfo info = new CameraInfo();
		        for(int i = 0;i< numCamera;i++){
		        	Camera.getCameraInfo(i, info);
		        	if(info.facing == CameraInfo.CAMERA_FACING_BACK){
		        		cameraId_back = i;     //��ȡ��������ͷ��Id
		        	}
		        	if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
		        		cameraId_front = i;    //��ȡǰ������ͷ��Id
		        		frontExist = true;
		        	}
		        }
//		        if(frontExist){
//		        	mCamera = Camera.open(cameraId_front);
//					DisplayToast("��ǰ������ͷ");
//				}else{
					mCamera = Camera.open(cameraId_back);
					DisplayToast("�򿪺�������ͷ");
				//}  
				try {
					mCamera.setPreviewDisplay(m_surfaceHolder);//����surface����ʵʱ��ʾ��m_surfaceHolder��ʾ��surface����ʾ��λ�ã�null��ʾ���
					//���ûؽк�����ÿ����ʾ֡��ʱ�򶼱�����
					 //����this�ǻؽ��źŶ������ÿ����ʾ֡�Ŀ�����null��ֹͣ���ջؽ��źš�
					mCamera.setPreviewCallback(this);  
					mCamera.setDisplayOrientation(90);
					//set camera 
					Camera.Parameters parame = mCamera.getParameters();    //��ȡ���ò�������
					parame.setPreviewFrameRate(PreviewFrameRate);    //����Camera����ʾ֡��
					parame.setPreviewSize(PreviewWidth, PreviewHeight);    //������Ļ�ֱ���
					parame.set("orientation", "portrait");
					//android2.3.3�Ժ������²�
					mCamera.setParameters(parame);
					//ͨ��SurfaceView��ʾȡ������
					//��ʼ����ʾ֡���в���ͻ�ͼ��surface
					mCamera.startPreview();   	
					// �Զ��Խ�
					mCamera.autoFocus(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
		

	}
/**
 * �˺�����surface��һ�δ���ʱ����ִ�У��ɶ�surface��һЩ������������
 */
	public void surfaceCreated(SurfaceHolder holder) {  
		Log.e("", "surfaceCreated");   
		m_surfaceHolder = holder;   //��������
	}

	/**
	 * ��һ��surface������֮ǰ����
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {  
		Log.e("", "surfaceDestroyed");
		if(mCamera != null)  //û�б�������ͷ�����
		{
			mCamera.setPreviewCallback(null);//must do this��ֹͣ���ջؽ��ź�
			mCamera.stopPreview();   //ֹͣ����ͻ�ͼ
			mCamera.release();   //�Ͽ�������ͷ�����ӣ����ͷ�����ͷ��Դ
		}
		this.Sd.Close();    //�رմ�����������ͷ�������йص�����ϵͳ��Դ
		NativeH264Encoder.DeinitEncoder();
		rtpsending=null;
	}

	final byte[] head = {0x00,0x00,0x00,0x01};
	int[]frame = new int[PreviewWidth*PreviewHeight];  //�½�֡���飬��С���ڷֱ��ʳ�*��
	static long time = System.currentTimeMillis();   //�Ժ�����ʽ���ص�ǰϵͳʱ��
	
	/**
	 * onPreviewFrame��һ���ص���������Ԥ��������ʾ��һ��Ԥ��֡ʱ���ã����¼��߳�open()��ʹ�õ�ʱ����У����Ž�����Ƶ
	 * @param
	 * data:��ʾ֮ǰ��ʾ֡
	 * camera������ͷ����
	 */
	
	public void onPreviewFrame(byte[] data, Camera camera) {    //��ʾ֡��ʾ�����

		byte[]encodeResult = NativeH264Encoder.EncodeFrame(data, time);  //���б��룬����������Ž�����
		time += 1000/PreviewFrameRate;    //�����һ֡���ķѵ�ʱ�䣬��λΪ����
		int encodeState = NativeH264Encoder.getLastEncodeStatus();    //��ȡ���ı���״̬��0������ʾ�ɹ�����
		
		if(SipInfo.endView == true){    //�յ�BYE����رյ�ǰ��Ƶ�ɼ����ܣ����»ص�ע��֮��ĵȴ��������
			SipInfo.keepalive.setMediaActive(false); 	    	
			SipInfo.nalfirst = 0; //0��ʾδ�յ��װ���1��ʾ�յ�
			SipInfo.index = 0;
			SipInfo.query_response = false;
			SipInfo.endView = false;
          	this.finish();
		}
		
		if(encodeState ==0 && encodeResult.length>0)
		{	
			Log.e(TAG,"encode len:"+encodeResult.length);//��ӡ�������ĳ���
			setSSRC_PAYLOAD();
			DivideAndSendNal(encodeResult);		
			//������д���ļ�����
			 
			
			Sd.Write(head);   //��head�е��ֽ�д�����������д���·���µ��ļ���
			Sd.Write(encodeResult);   //������д�������
		
		}

	}
	
	
	
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
			System.arraycopy(SipInfo.media_info_magic, 0, msg, 4, 16);  //����RTP���������������Info.media_info_megic֮ǰ�ټ���0x00 0x01 0x00 0x10
		}catch(Exception e){
			Log.d("ZR","System.arraycopy failed!");
		}
		rtpsending.rtpSession1.payloadType(0x7a);	//����RTP���ĸ�������Ϊ0x62
		
		//ȡInfo.media_info_megic�ĺ�������ΪRTP��ͬ��Դ�루Ssrc��
		Ssrc=(long)((SipInfo.media_info_magic[15]&0x000000ff)|((SipInfo.media_info_magic[14]<<8)&0x0000ff00)|((SipInfo.media_info_magic[13]<<16)&0x00ff0000)|((SipInfo.media_info_magic[12]<<24)&0xff000000));
		rtpsending.rtpSession1.setSsrc(Ssrc);
		
		
		for(int i=0;i<2;i++){		
			rtpsending.rtpSession1.sendData(msg);
		}
	}
	
	/**��Ƭ�����ͷ���*/
	public void DivideAndSendNal(byte[] h264){
		
		if(h264.length > 0){  //�����ݲŽ��з�Ƭ���Ͳ���
			if(h264.length > Info.divide_length){
				Info.dividingFrame = true;
				Info.status = true;
				Info.firstPktReceived = false;
				Info.pktflag = 0;

				while(Info.status){
					if(!Info.firstPktReceived){  //�װ�
						sendFirstPacket(h264);
					}
					else{
						if(h264.length - Info.pktflag > Info.divide_length){  //�а�
							sendMiddlePacket(h264);
						}
						else{   //ĩ��
							sendLastPacket(h264);
						}
					} //end of �װ�
				}//end of while
			}
			else{   //����Ƭ��
				sendCompletePacket(h264);
			}
		}
	}
	
	/**�����װ�*/
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
		//����RTP���ĸ�������Ϊ0x62
		rtpsending.rtpSession1.payloadType(0x62);
		//���ʹ������
		rtpsending.rtpSession1.sendData(rtppkt);   //���ʹ������
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**�����а�*/
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
		//����RTP���ĸ�������Ϊ0x62
rtpsending.rtpSession1.payloadType(0x62);
		//���ʹ������
		rtpsending.rtpSession1.sendData(rtppkt);   //���ʹ������   //���ʹ������
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**����ĩ��*/
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
		//����RTP���ĸ�������Ϊ0x62
rtpsending.rtpSession1.payloadType(0x62);	
		//���ʹ������
		rtpsending.rtpSession1.sendData(rtppktLast);   //���ʹ������  //���ʹ������
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Info.status = false;  //��������������һ�����н���
		Info.dividingFrame = false;  //һ֡��Ƭ�����ϣ�ʱ�������һ֡
	}
	
	/**����������*/
	public void sendCompletePacket(byte[] h264){
		//����RTP���ĸ�������Ϊ0x62
        rtpsending.rtpSession1.payloadType(0x62);
		//���ʹ������
		rtpsending.rtpSession1.sendData(h264);   //���ʹ������   //���ʹ������
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
	 		menu.add(0, MENU_RTPSEND, 0, "RTP������");
	 		return true;
	 	}
	 	

	 	@Override
	 	public boolean onOptionsItemSelected(MenuItem item) 
	 	{
	 		super.onOptionsItemSelected(item);
	 		switch (item.getItemId())
	 		{
	 		case MENU_RTPSEND:
	 			DisplayToast("RTP������"+SipInfo.pktNumber);
	 			break;
	 		
	 		}
	 		return true;
	 	}
		public void DisplayToast(String str)
		{
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		}
	
}
