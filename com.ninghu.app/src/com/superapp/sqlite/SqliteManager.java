package com.superapp.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.superapp.info.GongdanInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class SqliteManager {
	MyDatabaseHelper db =null;
	SQLiteDatabase sDatabase = null;
	String dev_id;
	String receiver;
	String receive_time;
	String info_source;
	String task_type;
	String task_id;
	String task_name;
	String orders;
	String location;
	String event_time;
	String pic_path_down;
	String vehicle_fault_num;
	String vehicle_fault_type;
	String parking_position;
	String cargo;
	String wrecker_num;
	String wrecker_type;
	String driver;
	String co_driver;
	String state;

	public SqliteManager() 
	{
	}
	
	public void insertInfo(Context context,GongdanInfo info) /*把GongdanInfo类中的数据插入数据库中。第一个参数为context，第二个参数为要插入的工单类的实例*/
	{
		db = new MyDatabaseHelper(context,"dict2",null,1);  
		sDatabase = db.getWritableDatabase();
		dev_id =info.getDev_id();
	    receiver=info.getReceiver();
		receive_time=info.getReceive_time();
		info_source=info.getInfo_source();
		task_type=info.getTask_type();
		task_id=info.getTask_id();
		task_name=info.getTask_name();
		orders=info.getOrder();
	    location=info.getLocation();
		event_time=info.getEvent_time();
	    pic_path_down=info.getPic_path_down();
	    vehicle_fault_num=info.getVehicle_fault_num();
	    vehicle_fault_type=info.getVehicle_fault_type();
	    parking_position=info.getParking_position();
	    cargo=info.getCargo();
	    wrecker_num=info.getWrecker_num();
	    wrecker_type=info.getWrecker_type();
		driver=info.getDriver();
	    co_driver=info.getCo_driver();
		String insertStr1 = "insert into dict2(dev_id,receiver,receive_time,info_source,task_type,task_id,task_name,orders,location,event_time,pic_path_down,vehicle_fault_num,vehicle_fault_type,parking_position,cargo,wrecker_num,wrecker_type,driver,co_driver,state) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String[] insertValue1_1 = { dev_id,receiver,receive_time,info_source,task_type,task_id,task_name,orders,location,event_time,pic_path_down,vehicle_fault_num,vehicle_fault_type,parking_position,cargo,wrecker_num,wrecker_type,driver,co_driver,"0"};
		sDatabase.execSQL(insertStr1, insertValue1_1); 
	    sDatabase.close();
	}
	
	public List <GongdanInfo> getInfo(Context context,int state) /*从数据库中获得新工单，已完成工单和未完成工单。第一个参数为context，第二个参数为0，1，2分别表示取出新工单，未完成工单和已完成工单，返回值为选定类型的所有工单，以list形式返回*/
	{
		List <GongdanInfo> info= new ArrayList<GongdanInfo>();
		
		Cursor cursor =null;	
		db = new MyDatabaseHelper(context,"dict2",null,1);    
		sDatabase = db.getWritableDatabase();		
		if(state==0)
		{
		String selectStr = "select _id,dev_id,receiver,receive_time,info_source,task_type,task_id,task_name,orders,location,event_time,pic_path_down,vehicle_fault_num,vehicle_fault_type,parking_position,cargo,wrecker_num,wrecker_type,driver,co_driver,state from dict2 where state='0'";	
		cursor = sDatabase.rawQuery(selectStr, null);	
	   }
		else if(state==1)
		{
	    String selectStr = "select _id,dev_id,receiver,receive_time,info_source,task_type,task_id,task_name,orders,location,event_time,pic_path_down,vehicle_fault_num,vehicle_fault_type,parking_position,cargo,wrecker_num,wrecker_type,driver,co_driver,state from dict2 where state='1'";
	    cursor = sDatabase.rawQuery(selectStr, null);
	    }
		else if(state==2) 
		{
		String selectStr = "select _id,dev_id,receiver,receive_time,info_source,task_type,task_id,task_name,orders,location,event_time,pic_path_down,vehicle_fault_num,vehicle_fault_type,parking_position,cargo,wrecker_num,wrecker_type,driver,co_driver,state from dict2 where state='2'";
	    cursor = sDatabase.rawQuery(selectStr, null);
	    }
		while (cursor.moveToNext())
		{	
		GongdanInfo infoTest =new GongdanInfo();
			infoTest.setId(cursor.getString(0));
			infoTest.setDev_id(cursor.getString(1));
			infoTest.setReceiver(cursor.getString(2));
			infoTest.setReceive_time(cursor.getString(3));
			infoTest.setInfo_source(cursor.getString(4));
			infoTest.setTask_type(cursor.getString(5));
			infoTest.setTask_id(cursor.getString(6));
			infoTest.setTask_name(cursor.getString(7));
			infoTest.setOrder(cursor.getString(8));
			infoTest.setLocation(cursor.getString(9));
			infoTest.setEvent_time(cursor.getString(10));
			infoTest.setPic_path_down(cursor.getString(11));
			infoTest.setVehicle_fault_num(cursor.getString(12));
			infoTest.setVehicle_fault_type(cursor.getString(13));
			infoTest.setParking_position(cursor.getString(14));
			infoTest.setCargo(cursor.getString(15));
			infoTest.setWrecker_num(cursor.getString(16));
			infoTest.setWrecker_type(cursor.getString(17));
			infoTest.setDriver(cursor.getString(18));
			infoTest.setCo_driver(cursor.getString(19));
			infoTest.setState(cursor.getString(20));
			info.add(infoTest);
		}
		sDatabase.close();
		return info;
		/*while (cursor.moveToNext())
    	{
    		Map<String,String>map = new HashMap<String, String>();
    		map.put("_id", cursor.getString(0));	
    		map.put("dev_id", cursor.getString(1));
    		map.put("receiver", cursor.getString(2));
    		map.put("receive_time", cursor.getString(3));
    		map.put("info_source", cursor.getString(4));
    		map.put("task_type", cursor.getString(5));
    		map.put("task_id", cursor.getString(6));
    		map.put("task_name", cursor.getString(7));
    		map.put("orders", cursor.getString(8));
    		map.put("location", cursor.getString(9));
    		map.put("event_time", cursor.getString(10));
    		map.put("pic_path_down", cursor.getString(11));
    		map.put("vehicle_fault_num", cursor.getString(12));
    		map.put("vehicle_fault_type", cursor.getString(13));
    		map.put("parking_position", cursor.getString(14));
    		map.put("cargo", cursor.getString(15));
    		map.put("wrecker_num", cursor.getString(16));
    		map.put("wrecker_type", cursor.getString(17));
    		map.put("driver", cursor.getString(18));
    		map.put("co_driver", cursor.getString(19));
    		list.add(map);
    	    sDatabase.close();
    	}
    		return info;	*/
	}

	public List <GongdanInfo> getRowInfo(String dev_id, String receive_time,Context context) /*从数据库中取出单个工单项目，第一个参数为要取出工单的dev_id，第二个参数为要取出工单的receive_time，第三个参数为context，返回值为包含整个工单的list*/
	{   List<GongdanInfo> info =new ArrayList<GongdanInfo>();
		db = new MyDatabaseHelper(context,"dict2",null,1);   
		sDatabase = db.getWritableDatabase();
		Cursor cursor =sDatabase.rawQuery("select * from dict2 where dev_id like ? and receive_time like ?", new String[] {"%" + dev_id + "%","%" + receive_time + "%"});
		GongdanInfo infoTest =new GongdanInfo();
    	cursor.moveToNext();
    	infoTest.setId(cursor.getString(0));
		infoTest.setDev_id(cursor.getString(1));
		infoTest.setReceiver(cursor.getString(2));
		infoTest.setReceive_time(cursor.getString(3));
		infoTest.setInfo_source(cursor.getString(4));
		infoTest.setTask_type(cursor.getString(5));
		infoTest.setTask_id(cursor.getString(6));
		infoTest.setTask_name(cursor.getString(7));
		infoTest.setOrder(cursor.getString(8));
		infoTest.setLocation(cursor.getString(9));
		infoTest.setEvent_time(cursor.getString(10));
		infoTest.setPic_path_down(cursor.getString(11));
		infoTest.setVehicle_fault_num(cursor.getString(12));
		infoTest.setVehicle_fault_type(cursor.getString(13));
		infoTest.setParking_position(cursor.getString(14));
		infoTest.setCargo(cursor.getString(15));
		infoTest.setWrecker_num(cursor.getString(16));
		infoTest.setWrecker_type(cursor.getString(17));
		infoTest.setDriver(cursor.getString(18));
		infoTest.setCo_driver(cursor.getString(19));
		infoTest.setState(cursor.getString(20));
	    info.add(infoTest);
		sDatabase.close();
    	return  info;
    	
	}
	
	public void setStateToOne(String id,Context context) /*把数据库内state的状态进行更改*/
	{ 
	    ContentValues cv = new ContentValues();  
        cv.put("state", 1);  
		db = new MyDatabaseHelper(context,"dict2",null,1);   
		sDatabase = db.getWritableDatabase();
		sDatabase.update("dict2", cv, "_id=?", new String[]{id});
		sDatabase.close();
	}
	
	public void setStateToTwo(String id,Context context) 
	{ 
	    ContentValues cv = new ContentValues();  
        cv.put("state", 2);  
		db = new MyDatabaseHelper(context,"dict2",null,1);   
		sDatabase = db.getWritableDatabase();
		sDatabase.update("dict2", cv, "_id=?", new String[]{id});
		sDatabase.close();
	}
}
