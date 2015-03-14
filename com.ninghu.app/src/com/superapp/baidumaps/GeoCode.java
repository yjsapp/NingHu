package com.superapp.baidumaps;

import android.widget.Toast;



import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class GeoCode implements OnGetGeoCoderResultListener {
	
	public static String AddressName="";
	public double latitude;
	public double longitude;
	

	GeoCoder mSearch = null;
	
	public GeoCode(){
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);	
	}
	
	public void getNameFromLL(double latitude ,double longitude ){
		
		
		LatLng ptCenter = new LatLng(latitude, longitude);
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(ptCenter));
		
	}
	
public void getLLFromName(String city ,String addressname){
		
		
	mSearch.geocode(new GeoCodeOption().city(city).address(
			addressname));
		
	}
	
	

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO 自动生成的方法存根
		
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			System.out.println("ERROR");
		}
		
		 AddressName=result.getAddress();
		 }
	
	
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			System.out.println("ERROR");
			
		}
		
		
			latitude=result.getLocation().latitude;
			longitude=result.getLocation().longitude;
			
			
			
			System.out.println(latitude+"-----"+longitude);
		
	}
	
	
	
	
	
	
	
	
	
	

}
