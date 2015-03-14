package com.superapp.Notification;

import java.io.IOException;

import org.zoolu.sip.message.BaseMessageFactory;
import org.zoolu.sip.message.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.G711.AudioInfo;
import com.superapp.main.Called;
import com.superapp.main.H264Sending;
import com.superapp.main.R;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML;
import com.superapp.sip.XML.MSG_TYPE;

public class AudioReceiver extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	private TextView audio_name;
	
	MediaPlayer mMediaPlayer=new MediaPlayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audioreceiver);
		
		VideoAlerm();
		
		
		
		audio_name=(TextView)findViewById(R.id.audio_name);
		
//		if(!Info.IsFirstCast){
//			
//			
//			zhujianame.setText(SipInfo.zhujianame+"大妈"+" 你要进行道路\n维护吗？确定要去吗？");
//			
//			
//		}
		
		
		
		//dialog=new MyDialog(this);
		layout=(LinearLayout)findViewById(R.id.audioreceiver_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		mMediaPlayer.stop();
		
		//等同于 拒绝的
		finish();
		return true;
	}
	
	public void audio_yes(View v) {
		 mMediaPlayer.stop();
		
		new Thread()
		{
			public void run()
			{
				try
				{
					
							
	 AudioInfo.AudioResponse_OK= XML.Create_XML_AudioResponse_OK();
Message calledresponse=BaseMessageFactory.createResponse1(AudioInfo.message, 200, "OK", null,"OK");
	SipInfo.sip_provider.sendMessage(calledresponse);
				
				
				
				
		    		

				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}
	finally			
				{  					
		Intent intent = new Intent();
     	intent.setClass(AudioReceiver.this,Called.class);
     	startActivity(intent); 
					
					} 			
			}				
		}.start(); 
		
   	     
		
    	this.finish();    	
      }  
	public void audio_no(View v) { 
		
		
		 mMediaPlayer.stop();
		
		new Thread()
		{
			public void run()
			{
				try
				{
					
							
	 AudioInfo.AudioResponse_Error= XML.Create_XML_AudioResponse_Error();
Message calledresponse=BaseMessageFactory.createResponse1(AudioInfo.message, 200, "OK", null,"Error");
	SipInfo.sip_provider.sendMessage(calledresponse);
				
				
				
				
		    		

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
		
   	     
		
    	this.finish();
    

    	
    	
      }  
	
	
	public void VideoAlerm(){
		
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
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
	
	
	
	
	
	
	
	
}
