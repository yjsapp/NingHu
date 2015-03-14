package com.superapp.Notification;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class NewsVideo {
	
	
	MediaPlayer mMediaPlayer;
	
	private Context context;
	private boolean IsLooping;
	
	public NewsVideo(Context context ,boolean IsLooping){
		
		this.context=context;
		this.IsLooping=IsLooping;
		
		
	}
	
	
public void VideoAlerm(){
		
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		  mMediaPlayer = new MediaPlayer();
		     try {
		         mMediaPlayer.setDataSource(context, alert);
		     } catch (Exception e) {
		         e.printStackTrace();
		     }
		     final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		     if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
		                 mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
		                 mMediaPlayer.setLooping(IsLooping);
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
	
	public void stop(){
		
		mMediaPlayer.stop();
		
		
	}

}
