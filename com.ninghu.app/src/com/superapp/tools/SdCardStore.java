package com.superapp.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.os.Environment;



public class SdCardStore {
	
	
	
	
	FileOutputStream TestInfoFile;   //���������Ϣ���ļ�
	 Calendar calendar = Calendar.getInstance();  //��ȡ��ǰ��ʱ��
     int year = calendar.get(Calendar.YEAR);  
     int month = calendar.get(Calendar.MONTH)+1;  
     int day = calendar.get(Calendar.DAY_OF_MONTH);  
     int hour = calendar.get(Calendar.HOUR_OF_DAY);  
     int minute = calendar.get(Calendar.MINUTE);  
     int second = calendar.get(Calendar.SECOND); 
     
     
   
     
     
     //ת��֮��ı�׼ʱ���ʽ
     String CurrentTime = 
    		 year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day)  +"-"+
    		 String.format("%02d", hour) + "@" + String.format("%02d", minute) + "@" + String.format("%02d", second);
     File AppInfopath;//�洢���ļ�����
     String mainpath;//��·����
     
     
    
   //��SD���ϴ���һ���ļ���
     public void createSDCardDir(){
      if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
             // ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
              File sdcardDir =Environment.getExternalStorageDirectory();
            //�õ�һ��·����������sdcard���ļ���·��������
              String path=sdcardDir.getPath()+"/My_Super_App/videos/";
              
              AppInfopath = new File(path);
             if (!AppInfopath.exists()) {
              //�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
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
     	//����һ���ļ�����
    	 TestInfoFile =  new FileOutputStream (FilePath);
		} 
		 catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 	
     }
	
     
    
     
     
     //�ļ�д��
     public void Write(byte[] xml){
     
     try {
    	 TestInfoFile.write(xml);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
     
     }
     //�ر��ļ�д��
  public void Close(){   
     try {
    	 TestInfoFile.close();
 	} catch (IOException e) {
 		e.printStackTrace();
 	} 
      }
	
	
	
	
	
	
	
	
	

}
