package com.superapp.main;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zoolu.sip.header.ViaHeader;
import org.zoolu.sip.message.BaseMessageFactory;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.provider.SipStack;

import com.superapp.info.GongdanInfo;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML;
import com.superapp.tools.GetThumbnailsPhotos;
import com.superapp.view.MyScrollLayout;
import com.superapp.view.OnViewChangeListener;
import com.superapp.view.SelectAddPopupWindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Zhixing extends Activity implements OnViewChangeListener,
OnClickListener{
    
	SelectAddPopupWindow menuWindow2;
	
	
	private MyScrollLayout mScrollLayout;
	private LinearLayout[] mImageViews;
	private int mViewCount;
	private int mCurSel;// 当前选
	
	public int flag=0;
	private TextView receiver;
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
	
	
	
	
	//private TextView 
	
	//public static TextView  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhixing);
        
        GongdanInfo gongdan = (GongdanInfo)getIntent().getSerializableExtra("gongdan");
	  
	
//	  Log.e("TAG", TelephoneInfo.StageNumber); 
        	}
    
   
    	       



		
//        初始化控件
		   
   public  void Init() {
	  
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

	
	  
	   
	   }
   public void btn_back(View v) {     //标题栏 返回按钮
     	this.finish();
     } 
   public void menu(View v) {  
	   
	   uploadImage2(Zhixing.this);
    	
    } 
     
   
   
 public void zhixing(View v) { 
	 Button zhixing_button=(Button)findViewById(R.id.zhixingbutton);
	 
	 switch(flag)
	   {
	   case 0:
		   zhixing_button.setText("执行");
		   flag=1;
		   break;
	   case 1:
		   
		   
		   zhixing_button.setText("开始出车");
		   flag=2;
		   
		   Intent intent = new Intent();
			intent.setClass(Zhixing.this,ChoosePath.class);
			startActivity(intent);
		   
		   
		   break;
	   case 2:
		   zhixing_button.setText("到达现场");
		   flag=3;
		   
		   new AlertDialog.Builder(this)      
	         .setMessage("是否误报空载？")      
	         .setNegativeButton("取消",      
	                 new DialogInterface.OnClickListener() {      
	                     public void onClick(DialogInterface dialog,      
	                             int which) {      
	                     }      
	                 })      
	         .setPositiveButton("确定",      
	                 new DialogInterface.OnClickListener() {      
	                     public void onClick(DialogInterface dialog,      
	                             int whichButton) {      
	                     	
	                     }      
	                 }).show(); 
		   
		   
		   break;
	   case 3:
		   
		   zhixing_button.setText("撤离现场");
		   flag=4;
		   break;
	   case 4:
		   
		   zhixing_button.setText("解拖完成");
		   flag=5;
		   break;
	   case 5:
		   zhixing_button.setText("收费");
		   flag=6;
		   break;
	   case 6:
		   
		   zhixing_button.setText("满意度调查");
		   flag=7;
		   break;
	   case 7:
		   zhixing_button.setText("工单完成");
		   flag=0;
		   break;
		   

	   default:
	   break; 
	   }
	 
	 
	 
	 
	 
	 
	 
	 
	
//((Button) v).setText("开始出车");
	 Toast.makeText(getApplicationContext(), "zhixing", 
				Toast.LENGTH_SHORT).show();	
 } 
 
 public void hulue(View v) {  
	 Button zhixing_button=(Button)findViewById(R.id.zhixingbutton);
	  
	 
	 switch(flag)
	   {
	   case 0:
		   zhixing_button.setText("开始出车");
		   flag=1;
		   break;
	   case 1:
		   
		   
		   zhixing_button.setText("到达现场");
		   flag=2;
		   
		   
		   
		   new AlertDialog.Builder(this)      
	         .setMessage("是否误报空载？")      
	         .setNegativeButton("取消",      
	                 new DialogInterface.OnClickListener() {      
	                     public void onClick(DialogInterface dialog,      
	                             int which) {      
	                     }      
	                 })      
	         .setPositiveButton("确定",      
	                 new DialogInterface.OnClickListener() {      
	                     public void onClick(DialogInterface dialog,      
	                             int whichButton) {      
	                     	
	                     }      
	                 }).show(); 
		   
		   
		   
		   
		   break;
	   case 2:
		   zhixing_button.setText("撤离现场");
		   flag=3;
		   break;
	   case 3:
		   
		   zhixing_button.setText("解拖完成");
		   flag=4;
		   break;
	   case 4:
		   
		   zhixing_button.setText("收费");
		   flag=5;
		   break;
	   case 5:
		   zhixing_button.setText("满意度调查");
		   flag=6;
		   break;
	   case 6:
		   
		   zhixing_button.setText("工单完成");
		   flag=7;
		   break;
	   case 7:
		  // zhixing_button.setText("工单完成");
		   flag=0;
		   break;
		   

	   default:
	   break; 
	   }
	 Toast.makeText(getApplicationContext(), "hulue", 
				Toast.LENGTH_SHORT).show();	
 	
 } 
   public void uploadImage2(final Activity context) {
	   menuWindow2 = new SelectAddPopupWindow(Zhixing.this, itemsOnClick2);
	   // 显示窗口
	   View view = Zhixing.this.findViewById(R.id.zhixing111);
	   // 计算坐标的偏移量
	   int xoffInPixels = menuWindow2.getWidth() - view.getWidth() + 10;
	   menuWindow2.showAsDropDown(view, -xoffInPixels, 0);
	   }
	 		  
		
   private OnClickListener itemsOnClick2 = new OnClickListener() {

	   public void onClick(View v) {
	   	menuWindow2.dismiss();
	   }
	   };

	   public void luxiangchaxun(View v){
			
		   GetThumbnailsPhotos getphotos=new GetThumbnailsPhotos(this,1);
		     getphotos.getThumbnailsPhotosInfo("LuXiang");
		     String name="LuXiang";
			 String path="/sdcard/My_Super_App/LuXiang";
		
			 Intent intent = new Intent();  
			 intent.setClass(Zhixing.this, VideosPlay.class);  
			 intent.putExtra("name",name); 
			 intent.putExtra("path",path); 
			 intent.putExtra("flag",1); 
			 intent.putExtra("data", (String[])getphotos.list.toArray(new String[getphotos.list.size()]));
			 Zhixing.this.startActivity(intent); 
			
			Toast.makeText(getApplicationContext(), "chaxunxinxi", 
					Toast.LENGTH_SHORT).show();	
			
			
		}

		public void yuyinhujiao(View v){
			
			
			
			 Intent intent = new Intent();
	 			intent.setClass(Zhixing.this,Calling.class);
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
			
			Toast.makeText(getApplicationContext(), "yijianluxiang", 
					Toast.LENGTH_SHORT).show();	
			
			
		}

		public void zhaopianshangchuan(View v){
			
			  GetThumbnailsPhotos getphotos=new GetThumbnailsPhotos(this,0);
             	
         	    getphotos.getThumbnailsPhotosInfo("qiangzhang");
        	    
        	         String name="qiangzhang";
        		 String path="/sdcard/My_Super_App/images/qiangzhang";
        	
        		 Intent intent = new Intent();  
        		 intent.setClass(Zhixing.this, ImageGridView.class);  
        		 intent.putExtra("name",name); 
        		 intent.putExtra("path",path); 
        		 intent.putExtra("flag",1); 
        		 intent.putExtra("data", (String[])getphotos.list.toArray(new String[getphotos.list.size()]));
        		 Zhixing.this.startActivity(intent);  
			
			
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
//							Toast.makeText(mCon, "图片没找到", 0).show();
							return;
						}
						cursor.moveToFirst();
						String path = cursor.getString(cursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
						Log.i("TAG","path=" + path);
						Intent intent = new Intent(this, Photos.class);
						intent.putExtra("path", path);
						startActivityForResult(intent, FLAG_MODIFY_FINISH);
					} else {
						Log.i("TAG","path=" + uri.getPath());
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
					Log.i("TAG", "截取到的图片路径是 = " + path);
					Bitmap b = BitmapFactory.decodeFile(path);
//					head.setImageBitmap(b);
					 
				}
			}
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
public void onClick(View arg0) {
	// TODO 自动生成的方法存根
		
}


@Override
public void OnViewChange(int view) {
	// TODO 自动生成的方法存根
	
	
}   



@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {      
     
       // 按下键盘上返回按钮      
       if (keyCode == KeyEvent.KEYCODE_BACK) {      
     
        System.out.println("------tuichu ");
        
        
        this.finish();
		Intent intent=new Intent(Zhixing.this,WorkList.class);
		
	    startActivity(intent);	
     
           return true;      
       } else {      
           return super.onKeyDown(keyCode, event);      
       }      
   }      



















}
