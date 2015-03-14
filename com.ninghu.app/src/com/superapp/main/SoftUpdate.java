package com.superapp.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;



public class SoftUpdate extends Activity {
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    setContentView(R.layout.update);
   
	}
	//检测更新的事件按钮
	 public void gengxin(View v) {  
		 
		 
		//进行FTP下载新版本    然后启动系统管理器进行软件更新 
		 
		 
//		 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+TelephoneInfo.StageNumber));
//			startActivity(intent);
	      }  
//	    public void telephone_common(View v) {  
//	    	Intent intent =new Intent();  
//	    	  
//            intent.setAction("android.intent.action.CALL_BUTTON");  
//  
//            startActivity(intent);
//
//	      }  
		
		
		
		
}
		
	
		
			


		
		
		