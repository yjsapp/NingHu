package com.superapp.BroadcastReceiver;

import com.superapp.Notification.NewsService;
import com.superapp.gps.GPSService;
import com.superapp.main.Welcome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class GpsReceiver extends BroadcastReceiver {  
    
@Override  
public void onReceive(Context context, Intent intent){  
         
	
	System.out.println(" ’µΩGPSπ„≤•");
	 Intent service = new Intent(context, GPSService.class);  
	     context.startService(service); 
	       
	     Intent service1 = new Intent(context, NewsService.class);  
	     context.startService(service1); 
     
       
}  
 
}  

