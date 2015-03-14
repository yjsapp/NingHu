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
	private static final String[] loginchoose = {"���ŵ�¼","�ֻ������¼"};//ѡ���Ƿ������ϴ�
	private Spinner  m_spinner;//ѡ���¼�ķ�ʽ
	private ArrayAdapter<String>adapter;//������
	
	public static  EditText Number;//���ź��ֻ��ŵ������
	public static  TextView Loginmeans;//�������ݺ͸��ܵ�¼

	private CheckBox save_info;	//�Ƿ񱣴�ĵ����ť
	private CheckBox jump; //�Ƿ񱣻���˽�İ�ť
	private static int Positionchoose;//

	private ProgressDialog buffering;//������

	
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
    			
    			Toast.makeText(getApplicationContext(), "ע��ɹ�", 
						Toast.LENGTH_SHORT).show();	
    			
    			 Intent intent=new Intent();
    			 intent.setAction("com.superapp.BroadcastReceiver.GPS");
    			 sendBroadcast(intent);
    			
    			System.out.println("�����㲥");
    			
    			
    			
    			
    			}
    			
    			
    			if(msg.arg1==2){
        			
        			Toast.makeText(getApplicationContext(), "��������ȷ�Ĺ��Ż����ֻ���", 
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
		// TODO �Զ����ɵķ������
		super.onResume();
		
		
		
		 Log.e("TAG", "ִ�е�����");
		System.out.print(Info.IsFirstCast);
		if(Info.IsFirstCast){
			
			Loginmeans.setText("���ݵ�¼");}
	}



	@Override
	protected void onRestart() {
		// TODO �Զ����ɵķ������
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
	//ע������
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
						
						
						android.os.Message registermsg=new android.os.Message();	//����handler��Ϣ
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
// 			.setTitle("���Ż����ֻ��Ų���Ϊ��")
// 			.setMessage("�������ֻ��Ż��߹���")
// 			 .setNegativeButton("ȷ��",      
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
    	
    	
		 
		 

	     
	 
	
	 public void login_zhuce(View v) {     //ע���豸
	      register();
	      }  
    
	 private Runnable Error = new Runnable()
     {
     	public void run() {
     		
     	if(!SipInfo.reg_success){
     		if(SipInfo.timeout)
         	{
     			Log.d("zz","��ʱ");
         		new AlertDialog.Builder(Welcome.this)
     			.setTitle("���ӷ�����ʧ�ܣ�")
     			.setMessage("�볢������ע��")
     			.setPositiveButton("ȷ��", null).show();            		
         	}
     		
     		else
     		{
     			Log.d("zz","����");
     			new AlertDialog.Builder(Welcome.this)
     			.setTitle("ע��ʧ�ܣ�")
     			.setMessage("�볢������ע��")
     			.setPositiveButton("ȷ��", null).show();
     		}
     	}
     	}
     };	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
   
    
	 protected static final int MENU_ABOUT=Menu.FIRST;

	 protected static final int MENU_EXIT=Menu.FIRST+1;
	 //���ò˵���ť
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
	 	// TODO Auto-generated method stub
	 	super.onCreateOptionsMenu(menu);
	 	menu.add(0, MENU_ABOUT, 0, "����");
	 	menu.add(0, MENU_EXIT, 0, "�˳�");
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
	         .setMessage("��ŵ�Ƽ�")      
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
	                     	System.exit(0);
	                     }      
	                 }).show(); 
	 		break;	
	 	}		
	 	return true;
	 }	

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {      
	      
	        // ���¼����Ϸ��ذ�ť      
	        if (keyCode == KeyEvent.KEYCODE_BACK) {      
	      
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
	 // �����������ַ�ʽ      
//	    System.exit(0);      
//	 //����������      
//	  android.os.Process.killProcess(android.os.Process.myPid());      
	  }    


    
}
