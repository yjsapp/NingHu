package com.superapp.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import com.superapp.G711.AudioInfo;
import com.superapp.info.Contactp;
import com.superapp.info.Gongdan;
import com.superapp.info.ImageInfo;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;
import com.superapp.sqlite.SqliteManager;
import com.superapp.tools.GetThumbnailsPhotos;
import com.superapp.tools.ImageCommon;
import com.superapp.view.ContactpAdapter;
import com.superapp.view.GongdanAdapter;
import com.superapp.view.MyScrollLayout;
import com.superapp.view.OnViewChangeListener;
import com.superapp.view.SelectAddPopupWindow;
import com.superapp.view.SelectPicpopupWindow;
import com.superapp.view.WeiWanChengAdapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class WorkList extends Activity implements OnViewChangeListener,
OnClickListener,MediaScannerConnectionClient {
private final String TAG = "WorkListActivity";
private MyScrollLayout mScrollLayout;
private LinearLayout[] mImageViews;
private int mViewCount;
private int mCurSel;// 当前选择的界面
private ImageView set;
private ImageView add;

private TextView xingongdan;
private TextView weiwancheng;
private TextView yiwancheng;
private TextView newmessage;
SqliteManager sql=new SqliteManager();
GongdanAdapter newgongdan;


private boolean isOpen = false;

private ListView listview1;
private ListView listview2;
private ListView listview3;

// 自定义的弹出框类
SelectPicpopupWindow menuWindow; // 弹出框
SelectAddPopupWindow menuWindow2; // 弹出框



public static final String IMAGE_PATH = "My_Super_App";
private static String localTempImageFileName = "";
private static String localTempLuxiangFileName = "";
private static final int FLAG_CHOOSE_IMG = 5;
private static final int FLAG_CHOOSE_PHONE = 6;
private static final int FLAG_MODIFY_FINISH = 7;
public static final File FILE_SDCARD = Environment
        .getExternalStorageDirectory();
public static final File FILE_LOCAL = new File(FILE_SDCARD,IMAGE_PATH);
public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
        "images/qiangzhang");
public static final File FILE_PIC_Luxiang = new File(FILE_LOCAL,
        "/LuXiang/");





public String[] allFiles;
private String SCAN_PATH=Environment.getExternalStorageDirectory().getPath()+"/My_Super_App"+"images/qiangzhang/" ;
private static final String FILE_TYPE="image/*";
 
private MediaScannerConnection conn;

String path;
LinkedList<ImageInfo> bitmaps=null;

@SuppressLint("HandlerLeak")
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.worklist);

init();

}


@Override
protected void onResume() {
	// TODO 自动生成的方法存根
	
	System.out.println(">>>>>>>+resume");
	
	int s=Gongdan.New_gongdan.size();
	String ss=String.valueOf(s);
	if(s>0){
		
		newmessage.setVisibility(View.VISIBLE);
		newmessage.setText(ss);
	}

	else{
		newmessage.setVisibility(View.INVISIBLE);
		
		
	}
	
	
	
	
	
	
	super.onResume();
}





private void init() {
xingongdan = (TextView) findViewById(R.id. xingongdan);
weiwancheng = (TextView) findViewById(R.id. weiwancheng);
yiwancheng = (TextView) findViewById(R.id.yiwancheng);
newmessage=(TextView) findViewById(R.id.main_tab_unread_gd);
listview1 = (ListView) findViewById(R.id.listView1);
listview3 = (ListView) findViewById(R.id.listView3);
listview2 = (ListView) findViewById(R.id.listView2);





Gongdan.New_gongdan=sql.getInfo(WorkList.this, 0);
Gongdan.Weiwancheng_gongdan=sql.getInfo(WorkList.this, 1);
Gongdan.Yiwancheng_gongdan=sql.getInfo(WorkList.this, 2);
int s=Gongdan.New_gongdan.size();
String ss=String.valueOf(s);
if(s>0){
	
	newmessage.setVisibility(View.VISIBLE);
	newmessage.setText(ss);
}

else{
	newmessage.setVisibility(View.INVISIBLE);
	
	
}


//GongdanAdapter ha = new GongdanAdapter(this, getGongdan());
 newgongdan = new GongdanAdapter(this, Gongdan.New_gongdan);

listview1.setAdapter(newgongdan);
listview1.setCacheColorHint(0);
listview1.setOnItemClickListener(new OnItemClickListener() {
	
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
sql.setStateToOne(Gongdan.New_gongdan.get(arg2).getId(), WorkList.this);
		

		
		Intent intent=new Intent(WorkList.this,Zhixing.class);
		finish();
		Bundle bundle = new Bundle();
		   bundle.putSerializable("gongdan",Gongdan.New_gongdan.get(arg2) );
		   intent.putExtras(bundle);
		
	    startActivity(intent);		
		
//		arg1.setBackgroundResource(R.color.white);
		Toast.makeText(getApplicationContext(), arg2+"提示11", 
				Toast.LENGTH_SHORT).show();	
	}
});

WeiWanChengAdapter wei = new WeiWanChengAdapter(this, Gongdan.Weiwancheng_gongdan);
listview3.setAdapter(wei);
listview3.setCacheColorHint(0);
listview3.setOnItemClickListener(new OnItemClickListener() {
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
//触发的事件
		finish();
		Intent intent=new Intent(WorkList.this,Zhixing.class);
		
	    startActivity(intent);		
		
//		arg1.setBackgroundResource(R.color.white);
		Toast.makeText(getApplicationContext(), arg2+"提示11", 
				Toast.LENGTH_SHORT).show();	
	}
});






ContactpAdapter hc = new ContactpAdapter(this, getContact());
listview2.setAdapter(hc);
listview2.setCacheColorHint(0);

listview2.setOnItemClickListener(new OnItemClickListener() {
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
//触发的事件
		
		finish();
		Intent intent=new Intent(WorkList.this,Yiwanchenggd.class);
		
		    startActivity(intent);			
//		arg1.setBackgroundResource(R.color.white);
		Toast.makeText(getApplicationContext(), arg2+"提示2222", 
				Toast.LENGTH_SHORT).show();	
	}
});
mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
// 获得top2的子组件
LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lllayout);
mViewCount = mScrollLayout.getChildCount();
// 依次给top2子组件添加事件
mImageViews = new LinearLayout[mViewCount];
for (int i = 0; i < mViewCount; i++) {
	mImageViews[i] = (LinearLayout) linearLayout.getChildAt(i);
	mImageViews[i].setEnabled(true);
	mImageViews[i].setOnClickListener(this);
	mImageViews[i].setTag(i);
}
mCurSel = 1;
mImageViews[mCurSel].setEnabled(false);
mScrollLayout.SetOnViewChangeListener(this);

set = (ImageView) findViewById(R.id.set);
add = (ImageView) findViewById(R.id.add);

set.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View arg0) {
		//uploadImage(WorkList.this);
	}
});
add.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View arg0) {
		uploadImage2(WorkList.this);
	}
});
}
private ArrayList<Contactp> getContact() {
	ArrayList<Contactp> hcList = new ArrayList<Contactp>();
	Contactp c0 = new Contactp();
	c0.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c0.setName("浙A2378   沪宁181K 1车道");

	Contactp c1 = new Contactp();
	c1.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c1.setName("浙A2378   沪宁181K 1车道");

	Contactp c2 = new Contactp();
	c2.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c2.setName("浙A2378   沪宁181K 1车道");

	Contactp c3 = new Contactp();
	c3.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c3.setName("浙A2378   沪宁181K 1车道");

	Contactp c4 = new Contactp();
	c4.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c4.setName("浙A2378   沪宁181K 1车道");

	Contactp c5 = new Contactp();
	c5.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c5.setName("浙A2378   沪宁181K 1车道");

	Contactp c6 = new Contactp();
	c6.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c6.setName("浙A2378   沪宁181K 1车道");

	Contactp c7 = new Contactp();
	c7.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c7.setName("浙A2378   沪宁181K 1车道");

	Contactp c8 = new Contactp();
	c8.setTxPath(R.drawable.bind_mcontact_reco_friends+ "");
	c8.setName("浙A2378   沪宁181K 1车道");

	Contactp c9 = new Contactp();
	c9.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c9.setName("浙A2378   沪宁181K 1车道");

	Contactp c10 = new Contactp();
	c10.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c10.setName("浙A2378   沪宁181K 1车道");

	Contactp c11 = new Contactp();
	c11.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c11.setName("浙A2378   沪宁181K 1车道");

	Contactp c12 = new Contactp();
	c12.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c12.setName("浙A2378   沪宁181K 1车道");

	Contactp c13 = new Contactp();
	c13.setTxPath(R.drawable.bind_mcontact_reco_friends + "");
	c13.setName("浙A2378   沪宁181K 1车道");

	hcList.add(c0);
	hcList.add(c1);
	hcList.add(c2);
	hcList.add(c3);
	hcList.add(c4);
	hcList.add(c5);
	hcList.add(c6);
	hcList.add(c7);
	hcList.add(c8);
	hcList.add(c9);
	hcList.add(c10);
	hcList.add(c11);
	hcList.add(c12);
	hcList.add(c13);

	return hcList;
}





public void uploadImage(final Activity context) {
menuWindow = new SelectPicpopupWindow(WorkList.this, itemsOnClick);
// 显示窗口
View view = WorkList.this.findViewById(R.id.set);
// 计算坐标的偏移量
int xoffInPixels = menuWindow.getWidth() - view.getWidth() + 10;
menuWindow.showAsDropDown(view, -xoffInPixels, 0);
}

public void uploadImage2(final Activity context) {
menuWindow2 = new SelectAddPopupWindow(WorkList.this, itemsOnClick2);
// 显示窗口
View view = WorkList.this.findViewById(R.id.set);
// 计算坐标的偏移量
int xoffInPixels = menuWindow2.getWidth() - view.getWidth() + 10;
menuWindow2.showAsDropDown(view, -xoffInPixels, 0);
}

// 为弹出窗口实现监听类
private OnClickListener itemsOnClick = new OnClickListener() {

public void onClick(View v) {
	menuWindow.dismiss();
}
};

// 为弹出窗口实现监听类
private OnClickListener itemsOnClick2 = new OnClickListener() {

public void onClick(View v) {
	menuWindow2.dismiss();
}

};

private void setCurPoint(int index) {
if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
	return;
}
mImageViews[mCurSel].setEnabled(true);
mImageViews[index].setEnabled(false);
mCurSel = index;

if (index == 0) {
	xingongdan.setTextColor(0xff228B22);
	weiwancheng.setTextColor(Color.BLACK);
	yiwancheng.setTextColor(Color.BLACK);
} else if (index == 1) {
	xingongdan.setTextColor(Color.BLACK);
	weiwancheng.setTextColor(0xff228B22);
	yiwancheng.setTextColor(Color.BLACK);
} else {
	xingongdan.setTextColor(Color.BLACK);
	weiwancheng.setTextColor(Color.BLACK);
	yiwancheng.setTextColor(0xff228B22);
}
}

@Override
public void OnViewChange(int view) {
// TODO Auto-generated method stub
setCurPoint(view);
}

@Override
public void onClick(View v) {
// TODO Auto-generated method stub

int pos = (Integer) (v.getTag());
setCurPoint(pos);
mScrollLayout.snapToScreen(pos);
}






public void luxiangchaxun(View v){
	

	Toast.makeText(getApplicationContext(), "chaxunxinxi", 
			Toast.LENGTH_SHORT).show();	
	
	GetThumbnailsPhotos getphotos=new GetThumbnailsPhotos(this,1);
	     getphotos.getThumbnailsPhotosInfo("LuXiang");
	     String name="LuXiang";
		 String path="/sdcard/My_Super_App/LuXiang";
	
		 Intent intent = new Intent();  
		 intent.setClass(WorkList.this, VideosPlay.class);  
		 intent.putExtra("name",name); 
		 intent.putExtra("path",path); 
		 intent.putExtra("flag",1); 
		 intent.putExtra("data", (String[])getphotos.list.toArray(new String[getphotos.list.size()]));
		 WorkList.this.startActivity(intent);  
}

public void yuyinhujiao(View v){
	
	
	
	 Intent intent = new Intent();
	intent.setClass(WorkList.this,Calling.class);
	startActivity(intent);
	
	Toast.makeText(getApplicationContext(), "yuyinhujiao", 
			Toast.LENGTH_SHORT).show();	
	
	
}

public void yijianluxiang(View v){
	
	String status = Environment.getExternalStorageState();
	if (status.equals(Environment.MEDIA_MOUNTED)) {
		try {
			localTempLuxiangFileName = "";
			localTempLuxiangFileName = String.valueOf((new Date())
					.getTime()) + ".mp4";
			File filePath = FILE_PIC_Luxiang;
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			File f = new File(filePath, localTempLuxiangFileName);
			// localTempImgDir和localTempImageFileName是自己定义的名字
			Uri u = Uri.fromFile(f);
			//intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, 10);
		} catch (ActivityNotFoundException e) {
			
		}
	}
	
	
	
	
	
	
	
	
	
	
//	 Intent intent = new Intent();
//    	intent.setClass(WorkList.this,VideoUpload.class);
//    	startActivity(intent);
	
	Toast.makeText(getApplicationContext(), "yijianluxiang", 
			Toast.LENGTH_SHORT).show();	
	
	
}

public void zhaopianshangchuan(View v){ 
                   GetThumbnailsPhotos getphotos=new GetThumbnailsPhotos(this,0);
               	
           	    getphotos.getThumbnailsPhotosInfo("qiangzhang");
          	    
          	         String name="qiangzhang";
          		 String path="/sdcard/My_Super_App/images/qiangzhang";
          	
          		 Intent intent = new Intent();  
          		 intent.setClass(WorkList.this, ImageGridView.class);  
          		 intent.putExtra("name",name); 
          		 intent.putExtra("path",path); 
          		 intent.putExtra("flag",1); 
          		 intent.putExtra("data", (String[])getphotos.list.toArray(new String[getphotos.list.size()]));
          		 WorkList.this.startActivity(intent);  
	
	Toast.makeText(getApplicationContext(), "zhaopianshangchuan", 
			Toast.LENGTH_SHORT).show();	
	
	
}


public void yijianpaizhao(View v){
	
	
	String status = Environment.getExternalStorageState();
	if (status.equals(Environment.MEDIA_MOUNTED)) {
		try {
			
			Calendar calendar = Calendar.getInstance();  //获取当前的时间
		     int year = calendar.get(Calendar.YEAR);  
		     int month = calendar.get(Calendar.MONTH)+1;  
		     int day = calendar.get(Calendar.DAY_OF_MONTH);  
		     int hour = calendar.get(Calendar.HOUR_OF_DAY);  
		     int minute = calendar.get(Calendar.MINUTE);  
		     int second = calendar.get(Calendar.SECOND); 
			
			String firsttime=year+String.format("%02d", month)+String.format("%02d", day)+"/";
		     String CurrentTime = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day)  +"-"+
		    		 String.format("%02d", hour) + "-" + String.format("%02d", minute) + "-" + String.format("%02d", second);
			
	
			localTempImageFileName = "";
			//localTempImageFileName = SipInfo.devid+"/Pic"+firsttime +SipInfo.devid+"_"+ CurrentTime+".jpg";
			//localTempImageFileName =CurrentTime+".jpg";
			localTempImageFileName =CurrentTime+".jpg";
			System.out.println(localTempImageFileName);
			
			File filePath = FILE_PIC_SCREENSHOT;
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			File f = new File(filePath, localTempImageFileName);
			// localTempImgDir和localTempImageFileName是自己定义的名字
			Uri u = Uri.fromFile(f);
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, FLAG_CHOOSE_PHONE);
		} catch (ActivityNotFoundException e) {
			//
		}
	}
	Toast.makeText(getApplicationContext(), "yijianpaizhao", 
			Toast.LENGTH_SHORT).show();	
	}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
		if (data != null) {
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {
				Cursor cursor = getContentResolver().query(uri,
						new String[] { MediaStore.Images.Media.DATA },
						null, null, null);
				if (null == cursor) {
//					Toast.makeText(mCon, "图片没找到", 0).show();
					return;
				}
				cursor.moveToFirst();
				String path = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA));
				cursor.close();
				Log.i(TAG,"path=" + path);
				Intent intent = new Intent(this, Photos.class);
				intent.putExtra("path", path);
				startActivityForResult(intent, FLAG_MODIFY_FINISH);
			} else {
				Log.i(TAG,"path=" + uri.getPath());
				Intent intent = new Intent(this, Photos.class);
				intent.putExtra("path", uri.getPath());
				startActivityForResult(intent, FLAG_MODIFY_FINISH);
			}
		}
	} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
		File f = new File(FILE_PIC_SCREENSHOT,localTempImageFileName);
		Intent intent = new Intent(this, Photos.class);
		intent.putExtra("path", f.getAbsolutePath());
		startActivityForResult(intent, FLAG_MODIFY_FINISH);
	}
	
	else if (requestCode == 10 && resultCode == RESULT_OK) {
		File f = new File(FILE_PIC_Luxiang,localTempLuxiangFileName);
		 sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, exportToVideo(f.getAbsolutePath())));
	        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"    
	                + Environment.getExternalStorageDirectory()))); 
		
	}
	else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
		if (data != null) {
			final String path = data.getStringExtra("path");
			Log.i(TAG, "截取到的图片路径是 = " + path);
			Bitmap b = BitmapFactory.decodeFile(path);
//			head.setImageBitmap(b);
			 
		}
	}
}


public void sdScan(){     
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"    
                + Environment.getExternalStorageDirectory())));     
} 



private Uri exportToVideo(String filename) {
    // Save the name and description of a video in a ContentValues map.
    final ContentValues values = new ContentValues(2);
    values.put(MediaStore.Video.Media.MIME_TYPE, "video/*");
    values.put(MediaStore.Video.Media.DATA, filename);
    // Add a new record (identified by uri)
    final Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            values);
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://"+ filename)));
    return uri;
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
if ((keyCode == KeyEvent.KEYCODE_MENU)) {
	return true;
}
return super.onKeyDown(keyCode, event);
}

@Override
public void onMediaScannerConnected() {
	// TODO 自动生成的方法存根
//        for(File f : FILE_PIC_SCREENSHOT)
//        {
//            conn.scanFile(f.getAbsolutePath(), "image/*");
//        }

	conn.scanFile(SCAN_PATH, FILE_TYPE);
}

@Override
public void onScanCompleted(String arg0, Uri arg1) {
	try {
        Log.d("onScanCompleted",arg1 + "success"+conn);
        System.out.println("URI " + arg1);             
        if (arg1 != null) 
        {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(arg1);
        startActivity(intent);
        }
        } finally 
        {
        conn.disconnect();
        conn = null;
        }
	// TODO 自动生成的方法存根
	
}

}

