package com.superapp.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.superapp.info.ImageInfo;

public class GetThumbnailsPhotos {
	
	public Context context;
	public int ResourceType;
	Cursor cursor=null;
	int flag=1;
	public List list=new ArrayList();
	String path="/sdcard/";
	
	public GetThumbnailsPhotos(Context context,int resourcetype){
		
		this.context =context;
		ResourceType=resourcetype;	
	} 
	
	
	
	public void getThumbnailsPhotosInfo(String phototype)
	{

			 
			  try
			  {
				  
			if(ResourceType==0){	  
				  
	cursor= context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
			}
				   
			else{
				
				
    cursor= context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);			
				
				
			}
	
	
	
	
	
	if(cursor==null)
	  	     {
	  		    
	  		     return;
	  	      }
			  }
			  catch(Exception err)
			  {
				  if(cursor!=null)
				     cursor.close();
			    
			      return;
			  }
		  
		  
	    
	    
	    HashMap<String, LinkedList<String>> albums=ImageCommon.getAlbumsInfo(flag, cursor);
	    cursor.close();
	    
	    for(Iterator<?> it = albums.entrySet().iterator(); it.hasNext(); )
	    {
	  	   Map.Entry e = (Map.Entry)it.next();
	  	
	  	   LinkedList<String> album=(LinkedList<String>)e.getValue();
	  	   
	  	   if(album!=null&&album.size()>0)
	  	   {
	  		   String ss=(String)e.getKey();
	  		   //info=new ImageInfo();
	  		   //info.displayName=(String)e.getKey();
	  		   if(ss.equals(phototype)){
	  			  ImageInfo info=new ImageInfo();
	  			   info.displayName=(String)e.getKey();
	  			   
		    	   info.picturecount=String.valueOf(album.size());
		    	   
		    	   //鎻愬彇绗�涓褰曚腑鐨勪俊鎭�
	  		   String id=album.get(0).split("&")[0];
	  		   String albumpath=album.get(0).split("&")[1];
	  		   
	  		   String name=albumpath.substring(albumpath.lastIndexOf("/")+1);
	  		   albumpath=albumpath.substring(0, albumpath.lastIndexOf("/"));
	  		   
info.icon=Thumbnails.getThumbnail(context.getContentResolver(),Integer.valueOf(id),Thumbnails.MICRO_KIND,new BitmapFactory.Options());
	  		   
	  		   info.path=albumpath;
	  		   
	  		 
			       for(String str: album)
			       {
				      list.add(str);
				     
			       }
			       info.tag=list;
	  		  
	  		   
	  		   }
	  	   }
	    }
	    cursor.close();        
	  }		
}
