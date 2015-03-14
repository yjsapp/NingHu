package com.superapp.main;

import org.zoolu.tools.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.overlayutil.DrivingRouteOvelray;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.superapp.baidumaps.GeoCode;
import com.superapp.baidumaps.MyDrivingRouteOverlay;

public class ChoosePath extends Activity implements SensorEventListener,OnGetRoutePlanResultListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	RoutePlanSearch mSearch = null;
	double latitude;
	double longitude;
    float degree;
	MapView mMapView;
	BaiduMap mBaiduMap;
	SensorManager mSensorManager;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	OverlayManager routeOverlay = null;
	Button requestLocButton;
	LinearLayout linear;
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosepath);
		mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
		requestLocButton = (Button) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("普通");
		
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfigeration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfigeration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfigeration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		linear=(LinearLayout)this.findViewById(R.id.radioGrouplinear);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.defaulticon) {
					// 传入null则，恢复默认图标
					mCurrentMarker = null;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfigeration(
									mCurrentMode, true, null));
				}
				if (checkedId == R.id.customicon) {
					// 修改为自定义marker
					mCurrentMarker = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_geo);
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfigeration(
									mCurrentMode, true, mCurrentMarker));
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);
		group.setVisibility(View.INVISIBLE);
		linear.setVisibility(View.INVISIBLE);
		

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		
		
		
		mSearch = RoutePlanSearch.newInstance();
	    mSearch.setOnGetRoutePlanResultListener(this);
		
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
//		mBaiduMap.
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		path();
		
		mCurrentMarker = null;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(
						mCurrentMode, true, null));
	}

	public void path(){
		
		GeoCode geo=new GeoCode();
		
		geo.getNameFromLL(latitude, longitude);
		
         //PlanNode stNode = PlanNode.withCityNameAndPlaceName("杭州",GeoCode.AddressName );
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("杭州", "浙江工大屏峰校区");
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("杭州","苏堤");
		
	     mSearch.drivingSearch((new DrivingRoutePlanOption())
                 .from(stNode)
                 .to(enNode));
	}
	
	public void NaVi(View v){
		
	//GeoCode navi=new GeoCode();
		
		//navi.getLLFromName("杭州", "浙江工大屏峰校区");
//		double fromlatitude=navi.latitude;
//		double fromlongitude=navi.longitude;
		//navi.getLLFromName("杭州","西湖");
//		double endlatitude=navi.latitude;
//		double endlongitude=navi.longitude;
		
		
		
		double fromlatitude=30.207033;
		double fromlongitude=120.088983;
		
		
		double endlatitude=30.242545;
		double endlongitude=120.146780;
		LatLng pt1 = new LatLng(latitude,longitude);
		LatLng pt2 = new LatLng(endlatitude,endlongitude);
		// 构建 导航参数
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "从这里开始";
		para.endPoint = pt2;
		para.endName = "到这里结束";
		try {

			BaiduMapNavigation.openBaiduMapNavi(para, this);

		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					BaiduMapNavigation.getLatestBaiduMapApp(ChoosePath.this);
				}
			});

			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.create().show();
		}
		
	
		
		
		
		
		
	}
	
	
	
	
	
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			
			//int ss=Random.nextInt(360);
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(degree).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			
			
			latitude=location.getLatitude();
			longitude=location.getLongitude();
				
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		
		
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		
		mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
		
		
		
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO 自动生成的方法存根
		int sensorType=event.sensor.getType();
		switch(sensorType){
		
		case Sensor.TYPE_ORIENTATION:
			
		 degree=event.values[0];
		 
			//System.out.println(degree);			
		}		
	}


	
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO 自动生成的方法存根
		 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(ChoosePath.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	            //result.getSuggestAddrInfo()
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            
	           
	            DrivingRouteOvelray overlay = new MyDrivingRouteOverlay(mBaiduMap);
	            routeOverlay = overlay;
	            mBaiduMap.setOnMarkerClickListener(overlay);
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	      
		
	        }	
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO 自动生成的方法存根
		
	}

}
