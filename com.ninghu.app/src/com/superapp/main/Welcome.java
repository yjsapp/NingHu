package com.superapp.main;


import java.util.Random;

import com.superapp.Notification.NewsService;
import com.superapp.info.Info;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends Activity {
	private static final String[] loginchoose = {"工号登录","手机号码登录"};//选择是否立即上传
	private Spinner  m_spinner;//选择登录的方式
	private ArrayAdapter<String>adapter;//适配器
	
	public static  EditText Number;//工号和手机号的输入框
	public static  TextView Loginmeans;//区分主驾和副架登录

	private CheckBox save_info;	//是否保存的点击按钮
	private CheckBox jump; //是否保护隐私的按钮
	private static int Positionchoose;//

	private ProgressDialog buffering;//缓冲条

	
	public static String VIA_ADDR = null;
	public static String[] PROTOCOL = {"udp"};
	public static  String ServiceUrl = "101.69.255.130";
	private Random sip_port = new Random();
	
	private ProgressDialog Registering;
	
	private Handler mHandler= new Handler();
	


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        
        init();
        
       

    	SipInfo.register= new Handler(){
    		public void handleMessage(android.os.Message msg) {
    			
    			
    			
    			
    			if(msg.arg1==1){
    			
    			Toast.makeText(getApplicationContext(), "注册成功", 
						Toast.LENGTH_SHORT).show();	
    			
    			 Intent intent=new Intent();
    			 intent.setAction("com.superapp.BroadcastReceiver.GPS");
    			 sendBroadcast(intent);
    			
    			System.out.println("发出广播");
    			
    			
    			
    			
    			}
    			
    			
    			if(msg.arg1==2){
        			
        			Toast.makeText(getApplicationContext(), "请输入正确的工号或者手机号", 
    						Toast.LENGTH_SHORT).show();	}
    			
    			
    			
    			
    			
    			
    			
    			super.handleMessage(msg);
    		}
    		
    		
    		
    		
    		
    	}; 
        
        
      
    }

    
    
    
    
    
    public void init(){
    	
    	Loginmeans=(TextView)findViewById(R.id.loginmeans_text);
    	Number=(EditText)findViewById(R.id.gonghao_et);
    
    	 save_info = (CheckBox) findViewById(R.id.forget_passwd);
    	 jump=(CheckBox) findViewById(R.id.jump_login);
    	
    m_spinner = (Spinner)findViewById(R.id.meansspinner);	
    		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,loginchoose);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		m_spinner.setAdapter(adapter);
    		
    		m_spinner.setSelection(0,true);
    		
    		m_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
    			
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
    					 SipInfo.loginmeans="0"; 
    					 
    					 ServiceUrl="101.69.255.130";
    					 
    					 
    					
    				 }  
    				 
    				if(position==1){    
    					SipInfo.loginmeans="1";
    					ServiceUrl="122.224.250.37";
    					
    					}  
    				
    				parent.setVisibility(View.VISIBLE);
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				}
    			});
    		
    		
    		
    		 
    }
    
    
    
    
    @Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		
		
		
		 Log.e("TAG", "执行到了吗？");
		System.out.print(Info.IsFirstCast);
		if(Info.IsFirstCast){
			
			Loginmeans.setText("副驾登录");}
	}



	@Override
	protected void onRestart() {
		// TODO 自动生成的方法存根
		super.onRestart();
	}






	public void register(){
    	
		

		
		
    	
    	final CharSequence strDialogTitle = getString(R.string.wait);
    	final CharSequence strDialogBody = getString(R.string.registering);  
    	Registering = ProgressDialog.show(Welcome.this, strDialogTitle, strDialogBody, true);
    	
    	new Thread("registering")
		{
			public void run()
			{
				try
				{
					SipInfo.sip_provider=new Sip(VIA_ADDR, sip_port.nextInt(5000)+2000, PROTOCOL,ServiceUrl );
				//  
				//SipInfo.From=Sip.SetFromTo("dev00055", SipInfo.devid,ServiceUrl, 6060);  
			SipInfo.From=Sip.SetFromTo("Android_test", SipInfo.devid,ServiceUrl, 6060); 
				SipInfo.To=Sip.SetFromTo("rvsup", "330100000010000090", ServiceUrl, 6060);
	//注册请求
				SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.ERegister_in, SipInfo.To, SipInfo.From)); 
				sleep(2000);
	    		int i=0;
	    		while(SipInfo.reg_success==false&&i<30){
	    			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.ERegister_in, SipInfo.To, SipInfo.From));
	    			sleep(300);      		    		
	    		    i++;
	    		
	    		}   	
					
		    		

				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}
				
				{  					
					Registering.dismiss();
					if(SipInfo.reg_success==true){   
						
						
						android.os.Message registermsg=new android.os.Message();	//创建handler消息
						registermsg.arg1=1;
						SipInfo.register.sendMessage(registermsg);	
						SipInfo.reg_success=false;
						
					
					}
					else{
						
						SipInfo.timeout=false;
						SipInfo.sip_provider.halt(); 
						mHandler.post(Error);
					}
   				} 
				
				
				
				
				
				
			}				
		}.start();   
    	
    }
    
    
    public void login_main(View v) {
    	
    	
//    	if(!Info.IsFirstCast){
//    		SipInfo.zhujianumber=Number.getText().toString();
//    		
//    	if(SipInfo.zhujianumber.equals("")){
//    		
//    		 new AlertDialog.Builder(Welcome.this)
// 			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
// 			.setTitle("工号或者手机号不能为空")
// 			.setMessage("请输入手机号或者工号")
// 			 .setNegativeButton("确定",      
//            new DialogInterface.OnClickListener() {      
//                public void onClick(DialogInterface dialog,      
//                        int which) {   
//             	   
//           
//                }      
//            })
// 			.create().show();
//    		}	
//    		
//    		
//    	else{	
//    		
//    	
//    		new Thread()
//    		{
//    			public void run()
//    			{
//    				try
//    				{
//    					
//    				SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia, SipInfo.To, SipInfo.From)); 
//   				sleep(2000);
//    	    		int i=0;
//    	    		while(SipInfo.zhujiadenglu_state==false&&i<30){
//    	    			SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia, SipInfo.To, SipInfo.From));
//    	    			sleep(300);      		    		
//    	    		    i++;
//    	    		
//    	    		}   	
//    					
//    		    		
//
//    				}
//    				catch(Exception e)
//    				{
//    					e.printStackTrace();
//    					
//    				}
//    	finally			
//   				{  					
//    					
//   					if(SipInfo.zhujiadenglu_state==true){   
//    						
//    						
//   					  Intent intent = new Intent();
//   			    	intent.setClass(Welcome.this,Confirm.class);
//   			    	startActivity(intent);
//    						
//    					
//    					}
//    					else{
//    						
////    						SipInfo.timeout=false;
////    						SipInfo.sip_provider.halt(); 
////    						mHandler.post(Error);
//    					}
//       				} 
//    				
//    				
//    				
//    				
//    				
//    				
//    			}				
//    		}.start(); 
//    		
//    		
//    		
//    		
//    	
//    		
//    		
//    		
//      
//    	
//    	
//    	}
//    	}
//    	
//    	else{
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		Intent intent = new Intent();
        	intent.setClass(Welcome.this,MainMenu.class);
        	startActivity(intent);
        	
    }
    		
    		
      
    
   // }
    	
    	
		 
		 

	     
	 
	
	 public void login_zhuce(View v) {     //注册设备
	      register();
	      }  
    
	 private Runnable Error = new Runnable()
     {
     	public void run() {
     		
     	if(!SipInfo.reg_success){
     		if(SipInfo.timeout)
         	{
     			Log.d("zz","超时");
         		new AlertDialog.Builder(Welcome.this)
     			.setTitle("连接服务器失败！")
     			.setMessage("请尝试重新注册")
     			.setPositiveButton("确定", null).show();            		
         	}
     		
     		else
     		{
     			Log.d("zz","其它");
     			new AlertDialog.Builder(Welcome.this)
     			.setTitle("注册失败！")
     			.setMessage("请尝试重新注册")
     			.setPositiveButton("确定", null).show();
     		}
     	}
     	}
     };	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
   
    
	 protected static final int MENU_ABOUT=Menu.FIRST;

	 protected static final int MENU_EXIT=Menu.FIRST+1;
	 //设置菜单按钮
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
	 	// TODO Auto-generated method stub
	 	super.onCreateOptionsMenu(menu);
	 	menu.add(0, MENU_ABOUT, 0, "关于");
	 	menu.add(0, MENU_EXIT, 0, "退出");
	 	return true;
	 }


	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
	 	// TODO Auto-generated method stub
	 	super.onOptionsItemSelected(item);
	 	switch (item.getItemId())
	 	{
	 	case MENU_ABOUT:
	 		//openOptionsDialog();
	 		new AlertDialog.Builder(this)      
	         .setMessage("普诺科技")      
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
	                     	System.exit(0);
	                     }      
	                 }).show(); 
	 		break;	
	 	}		
	 	return true;
	 }	

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {      
	      
	        // 按下键盘上返回按钮      
	        if (keyCode == KeyEvent.KEYCODE_BACK) {      
	      
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
	                         android.os.Process.killProcess(android.os.Process.myPid());  
	                             	   System.exit(0);      
	                             	   
	                                }      
	                            }).show();      
	      
	            return true;      
	        } else {      
	            return super.onKeyDown(keyCode, event);      
	        }      
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
