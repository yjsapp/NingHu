package com.superapp.main;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;



public class MainWork  extends Activity {
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    setContentView(R.layout.mainwork);
	}
	
	 public void qingzhang_button(View v) {  
		 
			Intent intent=new Intent(MainWork.this,WorkList.class);
			
   		    startActivity(intent);	
	    	
		 
		 
	      
	
	      }  
	    public void yanghu_button(View v) {  
	    	
	    	
	    	
Intent intent=new Intent(MainWork.this,YangHu.class);
			
   		    startActivity(intent);	
	    

	      }  
		
		
		
		
}
		
	