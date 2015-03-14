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
	  
		MyImageView fit;//�������
		MyImageView query;//������Ϣ
		MyImageView update;//�������
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mainmenu);
	        
	        fit=(MyImageView) findViewById(R.id.c_idea);
	        query=(MyImageView) findViewById(R.id.c_joke);
	        update=(MyImageView) findViewById(R.id.c_constellation);
	     //��ʼ������һЩ�������и�ֵ
	       
	        
	        
//	        fit.setOnClickIntent(
//	        		new MyImageView.OnViewClick() {
//				
//				@Override
//				public void onClick() {
//					// TODO Auto-generated method stub
//					Toast.makeText(Welcome.this, "�¼�����", 1000).show();	
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
	  //���ò˵���ť
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) 
	  {
	  	
	  	super.onCreateOptionsMenu(menu);
	  	menu.add(0, MENU_ABOUT, 0, "����");
	  	menu.add(0, MENU_EXIT, 0, "�˳�");
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
	          .setMessage("��˶���")      
	          .setNegativeButton("ȷ��",      
	                  new DialogInterface.OnClickListener() {      
	                      public void onClick(DialogInterface dialog,      
	                              int which) {      
	                      }      
	                  }).show(); 
	  		break;
	  	case MENU_EXIT:
	  		new AlertDialog.Builder(this)      
	          .setMessage("ȷ���˳�������")      
	          .setNegativeButton("ȡ��",      
	                  new DialogInterface.OnClickListener() {      
	                      public void onClick(DialogInterface dialog,      
	                              int which) {      
	                      }      
	                  })      
	          .setPositiveButton("ȷ��",      
	                  new DialogInterface.OnClickListener() {      
	                      public void onClick(DialogInterface dialog,      
	                              int whichButton) { 
	                    	  
//	                    	  
//	                    		 Intent service = new Intent(Welcome.this, UpLoadInfoService.class); 
//	                   Welcome.this.stopService( service);
	                      	System.exit(0);
	                      	android.os.Process.killProcess(android.os.Process.myPid()); // �˳����� 
	                      }      
	                  }).show(); 
	  		break;	
	  	}		
	  	return true;
	  }	



	  @Override      
	  protected void onDestroy() {      
	     super.onDestroy();      
//	  // �����������ַ�ʽ      
//	     System.exit(0);      
//	  //����������      
//	   android.os.Process.killProcess(android.os.Process.myPid());      
	   }    

	 
	  
	    
	}