package com.superapp.main;

import com.superapp.info.Info;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Confirm extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	private TextView zhujianame;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_dialog);
		
		
		zhujianame=(TextView)findViewById(R.id.confirm_name);
		
		if(!Info.IsFirstCast){
			
			
			zhujianame.setText(SipInfo.zhujianame+"大妈"+" 你要进行道路\n维护吗？确定要去吗？");
			
			
		}
		
		
		
		//dialog=new MyDialog(this);
		layout=(LinearLayout)findViewById(R.id.confirm_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	public void button_yes(View v) {  
		Info.IsFirstCast=true;
		
		new Thread()
		{
			public void run()
			{
				try
				{
					
				 SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
				sleep(2000);
	    		int i=0;
	    		while(SipInfo.zhujianamequeren.equals("1")&&i<30){
	    			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From));
	    			sleep(300);      		    		
	    		    i++;
	    		
	    		}   	
					
		    		

				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}
				
				finally
				{  					
					
					if(SipInfo.zhujianamequeren.equals("0")){  
						
						
						
						
						
					}
					else{
						
						
					}
   				} 
				
				
				
				
				
				
			}				
		}.start(); 
			
		
    	this.finish();    	
      }  
	public void button_no(View v) {  
    	this.finish();
    
    	android.os.Message registermsg=new android.os.Message();	//创建handler消息
		registermsg.arg1=2;
		SipInfo.register.sendMessage(registermsg);	
		
    	
    	
    	
    	
    	
    	
      }  
	
}
