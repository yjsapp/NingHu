package com.superapp.gps;



import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


public class GPSService extends Service{

	GpsUploadThread gpsupload=new GpsUploadThread();
	LocationManager locManager;
	
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		// 创建LocationManager对象
		locManager = (LocationManager) getSystemService
			(Context.LOCATION_SERVICE);
		// 从GPS获取最近的最近的定位信息
		Location location = locManager.getLastKnownLocation(
			LocationManager.GPS_PROVIDER);
		// 使用location根据EditText的显示
		updateGPS(location);
		// 设置每3秒获取一次GPS的定位信息
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
			, 10000, 20, new LocationListener()  //①
		{
			@Override
			public void onLocationChanged(Location location)
			{
				// 当GPS定位信息发生改变时，更新位置
				updateGPS(location);
			}

			@Override
			public void onProviderDisabled(String provider)
			{
				updateGPS(null);
			}

			@Override
			public void onProviderEnabled(String provider)
			{
				// 当GPS LocationProvider可用时，更新位置
				updateGPS(locManager
					.getLastKnownLocation(provider));
			}

			@Override
			public void onStatusChanged(String provider, int status,
				Bundle extras)
			{
			}
		});
	
	System.out.println("GPS Service 开启");
	
	gpsupload.startThread();
	}
	
	public void updateGPS(Location location){
		if(location!=null){
		double latitude = location.getLatitude();     //经度
		double longitude = location.getLongitude(); //纬度
		
		
		GPSInfo.Latitude=String.valueOf(latitude);
		GPSInfo.Longitude=String.valueOf(longitude);
		
		System.out.println("经度="+GPSInfo.Latitude+"---------"+"纬度"+GPSInfo.Longitude);}
		
		
		else{
			Toast.makeText(getApplicationContext(), "GPS暂不可用", Toast.LENGTH_LONG).show();
			
			
			
		}
		
		
		
	}







@Override
public IBinder onBind(Intent arg0) {
	// TODO 自动生成的方法存根
	return null;
}
	
	
	
	
	
	
	
	
}
