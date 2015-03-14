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
		System.out.println("News Service ����");
		SipInfo.notifymedia= new Handler(){
			public void handleMessage(android.os.Message msg) {
				System.out.println("�յ��� ��Ƶ����");
				
				
				Intent intent=new Intent(NewsService.this,VideoReceiver.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
				
				super.handleMessage(msg);
			}
		};
		
		
		AudioInfo.Called= new Handler(){
			public void handleMessage(android.os.Message msg) {
				System.out.println("�յ���������������");
				
				
				Intent intent=new Intent(NewsService.this,AudioReceiver.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(intent);
				
				super.handleMessage(msg);
			}
		};	
		
		
		SipInfo.xingongdan= new Handler(){
			public void handleMessage(android.os.Message msg) {
				System.out.println("�յ����µĹ���");
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
 				//����һ��Notification
 				Notification notify = new Notification();
 				//ΪNotification����ͼ�꣬��ͼ����ʾ��״̬��
 				notify.icon = R.drawable.ninghu;
 				//ΪNotification�����ı����ݣ����ı�����ʾ��״̬��
 				notify.tickerText = "�¹���֪ͨ";
 				//ΪNotification���÷���ʱ��
 				notify.when = System.currentTimeMillis();
 				//ΪNotification����Ĭ��������Ĭ���񶯡�Ĭ�������
 				notify.defaults = Notification.DEFAULT_ALL;
 				//�����ر�notifycation
 				notify.flags=Notification.FLAG_AUTO_CANCEL;
 				//�����¼���Ϣ
 				notify.setLatestEventInfo(getApplicationContext(), "�յ�" +"1"+ "���¹���","����鿴", pi);
 				//��ȡϵͳ��NotificationManager����
 				notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
 				//����֪ͨ(id��ͬ��ֻ��ʾ���µ�notification)
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
		// TODO �Զ����ɵķ������
	
	 notificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}








@Override
public IBinder onBind(Intent arg0) {
	// TODO �Զ����ɵķ������
	return null;
}
	
}

