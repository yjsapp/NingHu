package com.superapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	final String CREATE_TABLE_SQL = "create table dict2 (_id integer primary key autoincrement , dev_id varchar(30) , receiver varchar(30) , receive_time varchar(30) , info_source varchar(30) , task_type varchar(30) , task_id varchar(30) , task_name varchar(30) , orders varchar(30) , location varchar(30) , event_time varchar(30) , pic_path_down varchar(30) , vehicle_fault_num varchar(30) , vehicle_fault_type varchar(300) , parking_position varchar(30) , cargo varchar(30), wrecker_num varchar(30) , wrecker_type varchar(30) , driver varchar(30) , co_driver varchar(30) , state varchar(30))"; 
	public MyDatabaseHelper(Context context, String name, CursorFactory factory,int version)
	{
		super(context,name,null,version);
	}
	public void onCreate(SQLiteDatabase db)
	{ 
		db.execSQL(CREATE_TABLE_SQL);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		System.out.println("onUpdate Called"+oldVersion+"--->"+newVersion);
	}
}
