package com.superapp.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.superapp.info.ImageInfo;
import com.superapp.info.Info;
import com.superapp.tools.GetThumbnailsPhotos;
import com.superapp.tools.ImageCommon;
import com.superapp.view.SelectAddPopupWindow;

public class YangHu extends Activity {
	private static final String[] fangxiangchoose = {"沪宁方向","宁沪方向"};//选择是否立即上传
	private Spinner  fangxiang_spinner;//选择登录的方式
	private ArrayAdapter<String>fangxiangadapter;//适配器
	
	
	
	private static final String[] chedaochoose = {"路中间","1车道","2车道","3车道","4车道","停车带","路测",};//选择是否立即上传
	private Spinner  chedao_spinner;//选择登录的方式
	private ArrayAdapter<String>chedaoadapter;//适配器
	
	
	private static final String[] bingduchoose = {"坑槽松散","沉陷翻浆","路面污染","护栏缺损","路肩损坏","其他"};//选择是否立即上传
	private Spinner  bingdu_spinner;//选择登录的方式
	private ArrayAdapter<String>bingduadapter;//适配器
	
	
	SelectAddPopupWindow menuWindow2;
	
	

	
	private static int Positionchoose;//

	private ProgressDialog buffering;//缓冲条

	
	public static final String IMAGE_PATH = "My_Super_App";
	private static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
	        .getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD,IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
	        "images/yanghu");
	
	private static String localTempLuxiangFileName = "";
	
	public static final File FILE_PIC_Luxiang = new File(FILE_LOCAL,
	        "/LuXiang/");
	

Cursor cursor=null;

int flag=1;  //sd
String path;

LinkedList<ImageInfo> bitmaps=null;

List list;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yanghu);
        
        
        init();
    }

    
    
    
    
    
    public void init(){
    	
//    	Loginmeans=(TextView)findViewById(R.id.loginmeans_text);
//    	Number=(EditText)findViewById(R.id.gonghao_et);
//    
    	
    	
    fangxiang_spinner = (Spinner)findViewById(R.id.yanghuspinner);	
    		fangxiangadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fangxiangchoose);
    		fangxiangadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		fangxiang_spinner.setAdapter(fangxiangadapter);
    		
    		fangxiang_spinner.setSelection(0,true);
    		
    		fangxiang_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
    			
    			@Override
    			public void onItemSelected(AdapterView<?> parent, View arg1, int position,
    					long id) {
    				// TODO Auto-generated method stub
    				 String str=parent.getItemAtPosition(position).toString();
    				 System.out.println(position);
    				 Positionchoose = position;		
    				 System.out.println(Positionchoose);
    				 Log.e("TAG", str); 
    				
    				 
    				 if(position==0){           
    					
    				 }
    				 
    				if(position==1){          
    					
    					}  
    				
    				parent.setVisibility(View.VISIBLE);
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				}
    			});
    		
    		chedao_spinner = (Spinner)findViewById(R.id.chedaospinner);	
    		chedaoadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,chedaochoose);
    		chedaoadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		chedao_spinner.setAdapter(chedaoadapter);
    		
    		chedao_spinner.setSelection(0,true);
    		
    		chedao_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
    			
    			@Override
    			public void onItemSelected(AdapterView<?> parent, View arg1, int position,
    					long id) {
    				// TODO Auto-generated method stub
    				 String str=parent.getItemAtPosition(position).toString();
    				 System.out.println(position);
    				 Positionchoose = position;		
    				 System.out.println(Positionchoose);
    				 Log.e("TAG", str); 
    				
    				 
    				 if(position==0){           
    					
    				 }
    				 
    				if(position==1){          
    					
    					}  
    				
    				parent.setVisibility(View.VISIBLE);
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				}
    			});
//    		if(Info.IsFirstCast){
//    			
//    			Loginmeans.setText("副驾登录");
//    			
//    			
//    		}
//    		
    		bingdu_spinner = (Spinner)findViewById(R.id.bingduspinner);	
    		bingduadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,bingduchoose);
    		bingduadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		bingdu_spinner.setAdapter(bingduadapter);
    		
    		bingdu_spinner.setSelection(0,true);
    		
    		bingdu_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
    			
    			@Override
    			public void onItemSelected(AdapterView<?> parent, View arg1, int position,
    					long id) {
    				// TODO Auto-generated method stub
    				 String str=parent.getItemAtPosition(position).toString();
    				 System.out.println(position);
    				 Positionchoose = position;		
    				 System.out.println(Positionchoose);
    				 Log.e("TAG", str); 
    				
    				 
    				 if(position==0){           
    					
    				 }
    				 
    				if(position==1){          
    					
    					}  
    				
    				parent.setVisibility(View.VISIBLE);
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				}
    			});
    		
    		
    	
    	
    
    }
    
    
    public void yanghu_btn_back(View v) {
		 
    	this.finish();
    	    		
    		
    	}
    	
    	
    public void yanghu_menu(View v) {
		 
		 
    	  uploadImage2(YangHu.this);
		
	}
    public void yanghu_shangbao(View v) {
		 
		 
		
		
	}	 
		 

	     
	 
	
//	 public void login_back(View v) {     //标题栏 返回按钮
//	      	this.finish();
//	      	System.exit(0);
//	      }  
    
   
    public void uploadImage2(final Activity context) {
 	   menuWindow2 = new SelectAddPopupWindow(YangHu.this, itemsOnClick2);
 	   // 显示窗口
 	   View view = YangHu.this.findViewById(R.id.yanghu111);
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
 			
 			
 			
 			Toast.makeText(getApplicationContext(), "chaxunxinxi", 
 					Toast.LENGTH_SHORT).show();	
 			
 			GetThumbnailsPhotos getphotos=new GetThumbnailsPhotos(this,1);
 			     getphotos.getThumbnailsPhotosInfo("LuXiang");
 			     String name="LuXiang";
 				 String path="/sdcard/My_Super_App/LuXiang";
 			
 				 Intent intent = new Intent();  
 				 intent.setClass(YangHu.this, VideosPlay.class);  
 				 intent.putExtra("name",name); 
 				 intent.putExtra("path",path); 
 				 intent.putExtra("flag",1); 
 				 intent.putExtra("data", (String[])getphotos.list.toArray(new String[getphotos.list.size()]));
 				 YangHu.this.startActivity(intent); 					
 		}

 		public void yuyinhujiao(View v){
 			
 			
 			 Intent intent = new Intent();
 			intent.setClass(YangHu.this,Calling.class);
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
 			
 			
 		}

 		public void zhaopianshangchuan(View v){		    
 			
GetThumbnailsPhotos getphotos=new GetThumbnailsPhotos(this,0);
	
   getphotos.getThumbnailsPhotosInfo("yanghu");
 			    
 			    String name="yanghu";
 				 String path="/sdcard/My_Super_App/images/yanghu";
 				
 				
 				 Intent intent = new Intent();  
 				 intent.setClass(YangHu.this, ImageGridView.class);  
 				 intent.putExtra("name",name); 
 				 intent.putExtra("path",path); 
 				 intent.putExtra("flag",1); 
 				
 				 intent.putExtra("data", (String[])getphotos.list.toArray(new String[getphotos.list.size()]));
 				 YangHu.this.startActivity(intent); 
 			
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
// 							Toast.makeText(mCon, "图片没找到", 0).show();
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
// 					head.setImageBitmap(b);
 					 
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
	 protected void onDestroy() {      
	    super.onDestroy();      
	 // 或者下面这种方式      
//	    System.exit(0);      
//	 //建议用这种      
//	  android.os.Process.killProcess(android.os.Process.myPid());      
	  }    


    
}


