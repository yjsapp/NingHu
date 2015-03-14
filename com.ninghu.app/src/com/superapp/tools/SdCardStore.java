package com.superapp.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.os.Environment;



public class SdCardStore {
	
	
	
	
	FileOutputStream TestInfoFile;   //保存测试信息的文件
	 Calendar calendar = Calendar.getInstance();  //获取当前的时间
     int year = calendar.get(Calendar.YEAR);  
     int month = calendar.get(Calendar.MONTH)+1;  
     int day = calendar.get(Calendar.DAY_OF_MONTH);  
     int hour = calendar.get(Calendar.HOUR_OF_DAY);  
     int minute = calendar.get(Calendar.MINUTE);  
     int second = calendar.get(Calendar.SECOND); 
     
     
   
     
     
     //转换之后的标准时间格式
     String CurrentTime = 
    		 year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day)  +"-"+
    		 String.format("%02d", hour) + "@" + String.format("%02d", minute) + "@" + String.format("%02d", second);
     File AppInfopath;//存储的文件夹名
     String mainpath;//主路径名
     
     
    
   //在SD卡上创建一个文件夹
     public void createSDCardDir(){
      if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
             // 创建一个文件夹对象，赋值为外部存储器的目录
              File sdcardDir =Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
              String path=sdcardDir.getPath()+"/My_Super_App/videos/";
              
              AppInfopath = new File(path);
             if (!AppInfopath.exists()) {
              //若不存在，创建目录，可以在应用启动的时候创建
            	 AppInfopath.mkdirs();
             
            }
             }
      else{
      
       return;

     }

     }
  
   
     

     
     
     
     public void Creat(){
     
     try {
    	 
    	 
    	
    	 
    	 
    	 String FilePath = "/sdcard/"+"/My_Super_App/videos/"+ CurrentTime + ".264";
     	//创建一个文件对象
    	 TestInfoFile =  new FileOutputStream (FilePath);
		} 
		 catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 	
     }
	
     
    
     
     
     //文件写入
     public void Write(byte[] xml){
     
     try {
    	 TestInfoFile.write(xml);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
     
     }
     //关闭文件写入
  public void Close(){   
     try {
    	 TestInfoFile.close();
 	} catch (IOException e) {
 		e.printStackTrace();
 	} 
      }
	
	
	
	
	
	
	
	
	

}
