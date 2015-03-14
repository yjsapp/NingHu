package com.superapp.main;

import java.io.File;

import com.superapp.Notification.AudioReceiver;
import com.superapp.Notification.GongdanReceiver;
import com.superapp.Notification.VideoReceiver;
import com.superapp.view.MyImageView;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class MainMenu extends Activity {
	  
		MyImageView fit;//软件设置
		MyImageView query;//测试信息
		MyImageView update;//软件更新
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mainmenu);
	        
	        fit=(MyImageView) findViewById(R.id.c_idea);
	        query=(MyImageView) findViewById(R.id.c_joke);
	        update=(MyImageView) findViewById(R.id.c_constellation);
	     //初始化并对一些参数进行赋值
	       
	        
	        
//	        fit.setOnClickIntent(
//	        		new MyImageView.OnViewClick() {
//				
//				@Override
//				public void onClick() {
//					// TODO Auto-generated method stub
//					Toast.makeText(Welcome.this, "事件触发", 1000).show();	
//				}
//			});
	         }
	    
	  
	   
	  
	   
	   public void  base (View v){
			 

		 
		   
//		   Intent intent=new Intent(MainMenu.this,GongdanReceiver.class);
//			
//		    startActivity(intent);
	}
	   
	   
	    public void  fit (View v){
	       	
	    	Intent intent=new Intent(MainMenu.this,Fit.class);
			
	    startActivity(intent);
	  	 }  
	    
	  public void  query (View v){
	       	
		
    		
	    			Intent intent=new Intent(MainMenu.this,MainWork.class);
				
   		    startActivity(intent);	
	    	
	    	
	    	
	    	
}   
	    
	  
	  
	  
	  public void  update (View v){
	     	
	  	Intent intent=new Intent(MainMenu.this,SoftUpdate.class);
			
		    startActivity(intent);
		 }  
	  
	  
	  
	  protected static final int MENU_ABOUT=Menu.FIRST;

	  protected static final int MENU_EXIT=Menu.FIRST+1;
	  //设置菜单按钮
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) 
	  {
	  	
	  	super.onCreateOptionsMenu(menu);
	  	menu.add(0, MENU_ABOUT, 0, "关于");
	  	menu.add(0, MENU_EXIT, 0, "退出");
	  	return true;
	  }


	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) 
	  {
	  	
	  	super.onOptionsItemSelected(item);
	  	switch (item.getItemId())
	  	{
	  	case MENU_ABOUT:
	  		//openOptionsDialog();
	  		new AlertDialog.Builder(this)      
	          .setMessage("金硕软件")      
	          .setNegativeButton("确定",      
	                  new DialogInterface.OnClickListener() {      
	                      public void onClick(DialogInterface dialog,      
	                              int which) {      
	                      }      
	                  }).show(); 
	  		break;
	  	case MENU_EXIT:
	  		new AlertDialog.Builder(this)      
	          .setMessage("确定退出程序吗？")      
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
	                    	  
//	                    	  
//	                    		 Intent service = new Intent(Welcome.this, UpLoadInfoService.class); 
//	                   Welcome.this.stopService( service);
	                      	System.exit(0);
	                      	android.os.Process.killProcess(android.os.Process.myPid()); // 退出程序 
	                      }      
	                  }).show(); 
	  		break;	
	  	}		
	  	return true;
	  }	



	  @Override      
	  protected void onDestroy() {      
	     super.onDestroy();      
//	  // 或者下面这种方式      
//	     System.exit(0);      
//	  //建议用这种      
//	   android.os.Process.killProcess(android.os.Process.myPid());      
	   }    

	 
	  
	    
	}