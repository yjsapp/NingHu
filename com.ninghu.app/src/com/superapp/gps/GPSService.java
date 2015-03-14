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
		
		// ����LocationManager����
		locManager = (LocationManager) getSystemService
			(Context.LOCATION_SERVICE);
		// ��GPS��ȡ���������Ķ�λ��Ϣ
		Location location = locManager.getLastKnownLocation(
			LocationManager.GPS_PROVIDER);
		// ʹ��location����EditText����ʾ
		updateGPS(location);
		// ����ÿ3���ȡһ��GPS�Ķ�λ��Ϣ
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
			, 10000, 20, new LocationListener()  //��
		{
			@Override
			public void onLocationChanged(Location location)
			{
				// ��GPS��λ��Ϣ�����ı�ʱ������λ��
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
				// ��GPS LocationProvider����ʱ������λ��
				updateGPS(locManager
					.getLastKnownLocation(provider));
			}

			@Override
			public void onStatusChanged(String provider, int status,
				Bundle extras)
			{
			}
		});
	
	System.out.println("GPS Service ����");
	
	gpsupload.startThread();
	}
	
	public void updateGPS(Location location){
		if(location!=null){
		double latitude = location.getLatitude();     //����
		double longitude = location.getLongitude(); //γ��
		
		
		GPSInfo.Latitude=String.valueOf(latitude);
		GPSInfo.Longitude=String.valueOf(longitude);
		
		System.out.println("����="+GPSInfo.Latitude+"---------"+"γ��"+GPSInfo.Longitude);}
		
		
		else{
			Toast.makeText(getApplicationContext(), "GPS�ݲ�����", Toast.LENGTH_LONG).show();
			
			
			
		}
		
		
		
	}







@Override
public IBinder onBind(Intent arg0) {
	// TODO �Զ����ɵķ������
	return null;
}
	
	
	
	
	
	
	
	
}
