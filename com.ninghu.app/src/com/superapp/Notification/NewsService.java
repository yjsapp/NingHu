package com.superapp.Notification;



import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import com.superapp.G711.AudioInfo;
import com.superapp.info.Gongdan;
import com.superapp.info.GongdanInfo;
import com.superapp.main.Confirm;
import com.superapp.main.R;
import com.superapp.main.WorkList;
import com.superapp.sip.SipInfo;
import com.superapp.sqlite.MyDatabaseHelper;
import com.superapp.sqlite.SqliteManager;
import com.superapp.xmlparse.Xmlparse;


public class NewsService extends Service{

	MyDatabaseHelper db =null;
	SQLiteDatabase sDatabase = null;
	private NotificationManager notificationManager;
	static int NOTIFICATION_ID = 0x1123;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		System.out.println("News Service 开启");
		SipInfo.notifymedia= new Handler(){
			public void handleMessage(android.os.Message msg) {
				System.out.println("收到了 视频邀请");
				
				
				Intent intent=new Intent(NewsService.this,VideoReceiver.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
				
				super.handleMessage(msg);
			}
		};
		
		
		AudioInfo.Called= new Handler(){
			public void handleMessage(android.os.Message msg) {
				System.out.println("收到了语音呼叫请求");
				
				
				Intent intent=new Intent(NewsService.this,AudioReceiver.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
				
				super.handleMessage(msg);
			}
		};	
		
		
		SipInfo.xingongdan= new Handler(){
			public void handleMessage(android.os.Message msg) {
				System.out.println("收到了新的工单");
				   Bundle b = msg.getData();
				 String gongdan = b.getString("GongDan");
				 System.out.println("Service"+gongdan);
				 
				 Xmlparse xmlparse=new Xmlparse();
				 Gongdan.gongdans=xmlparse.parsegongdan(gongdan);
				 SqliteManager sq=new SqliteManager();
				 sq.insertInfo(NewsService.this, Gongdan.gongdans.get(0));
				 System.out.println(sq.getInfo(NewsService.this, 0).toString());
				
				 NewsVideo newsvideo =new NewsVideo(NewsService.this,false);
				 newsvideo.VideoAlerm();
	
				 Intent intent = new Intent(getApplicationContext(), WorkList.class);
 				PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent , 0);
 				//创建一个Notification
 				Notification notify = new Notification();
 				//为Notification设置图标，该图标显示在状态栏
 				notify.icon = R.drawable.ninghu;
 				//为Notification设置文本内容，该文本会显示在状态栏
 				notify.tickerText = "新工单通知";
 				//为Notification设置发送时间
 				notify.when = System.currentTimeMillis();
 				//为Notification设置默认声音、默认振动、默认闪光灯
 				notify.defaults = Notification.DEFAULT_ALL;
 				//点击后关闭notifycation
 				notify.flags=Notification.FLAG_AUTO_CANCEL;
 				//设置事件信息
 				notify.setLatestEventInfo(getApplicationContext(), "收到" +"1"+ "条新工单","点击查看", pi);
 				//获取系统的NotificationManager服务
 				notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
 				//发送通知(id相同，只显示最新的notification)
 				notificationManager.notify(NOTIFICATION_ID, notify); 
				 
			
				 
//				 Intent intent=new Intent(NewsService.this,GongdanReceiver.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//				startActivity(intent);
				
				super.handleMessage(msg);
			}
		};	
	}
	
	
	
	
	
	
	
	
@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
	
	 notificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}








@Override
public IBinder onBind(Intent arg0) {
	// TODO 自动生成的方法存根
	return null;
}
	
}

