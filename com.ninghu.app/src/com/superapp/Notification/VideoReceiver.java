package com.superapp.Notification;

import java.io.IOException;

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

import com.superapp.info.Info;
import com.superapp.main.H264Sending;
import com.superapp.main.MediaUpload;
import com.superapp.main.R;
import com.superapp.main.VideoUpload;
import com.superapp.main.WorkList;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;

public class VideoReceiver extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	private TextView video_name;
	
	MediaPlayer mMediaPlayer=new MediaPlayer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoreceiver);
		
		VideoAlerm();
		video_name=(TextView)findViewById(R.id.confirm_video_name);
		
//		if(!Info.IsFirstCast){
//			
//			
//			zhujianame.setText(SipInfo.zhujianame+"大妈"+" 你要进行道路\n维护吗？确定要去吗？");
//			
//			
//		}
		
		
		
		//dialog=new MyDialog(this);
		layout=(LinearLayout)findViewById(R.id.videoreceiver_layout);
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
	
	public void button_video_yes(View v) {
		
		
		 mMediaPlayer.stop();
     	
     	Intent intent = new Intent();
     intent.setClass(VideoReceiver.this,H264Sending.class);
    // intent.setClass(VideoReceiver.this,MediaUpload.class);
     	
     	startActivity(intent);      
		
    	this.finish();    	
      }  
	public void button_video_no(View v) {  
    	this.finish();
    

    	mMediaPlayer.stop();
    	
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
