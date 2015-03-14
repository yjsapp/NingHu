package com.superapp.main;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import com.superapp.info.ImageInfo;
import com.superapp.view.VideosViewAdapter;

public class VideosPlay extends Activity{

	 private static final String TAG = "ImageGridView";
	 LinkedList<ImageInfo> bitImages=null;
	 String[] albums=null;
	 int flag=2;
	 @Override  
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	    setContentView(R.layout.gridv);  
	    GridView gView = (GridView) this.findViewById(R.id.gridview);  
	    bitImages=new  LinkedList<ImageInfo>();   
	    Intent intent = getIntent();  
      final String path = intent.getStringExtra("path"); 
      String name = intent.getStringExtra("name");  
      flag=intent.getIntExtra("flag",1);
      albums=intent.getStringArrayExtra("data");
      Log.i("ImageGridView_onCreate", "flag="+flag+";path="+path+"; name="+name);
      
//      setTitle((String) getText(R.string.album)+":"+name+"     "
//      		+(String) getText(+R.string.path)+":"+path);

      gView.setAdapter(new VideosViewAdapter(this,flag, name, getNames(flag, albums)));
      
      
	    gView.setOnItemClickListener(new OnItemClickListener(){  
	           public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) 
	           {  
	                 //Log.i("GridView.setOnItemClickListener", "position="+position);
	                 //String path=images.get(position).path;
	        		 //Log.i("ImageListView_onListItemClick", "the path="+path);

	        		 ArrayList<String> pathArray=new ArrayList<String>();
//	        		 for(int i=0; i<albums.length; i++)
//	        		 {
//	        			 if(flag==2)
//	        			 {
//	        				 pathArray.add(albums[i]);
//	        			 }
//	        			 else
//	        			 {
//	        			     String absolutePath=albums[i].split("&")[1];
//	        			     Log.i(TAG, "absolutePath="+absolutePath);
//	        			     pathArray.add(absolutePath);
//	        			 }
//	        		 }
	        		 
	        		 String absolutePath=albums[position].split("&")[1]; 
	        		          System.out.println(absolutePath);
	        		   Intent intent=getVideoFileIntent(absolutePath);
	        		   startActivity(intent);

	           }    
	     });  
	 }

	private int[] getRowIds(String[] albums)
	{
		int[] ids=new int[albums.length];
		for(int i=0; i<albums.length; i++)
		{
			
			String id=albums[i].split("&")[0];
			ids[i]=Integer.valueOf(id);
		}
		return ids;
	}
	private Intent getVideoFileIntent(String video_path)

	{File  videoFile=new File(video_path);

		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		intent.putExtra("oneshot", 0);

        intent.putExtra("configchange", 0);

        Uri uri = Uri.fromFile(videoFile);

		intent.setDataAndType(uri, "video/*");

		return intent;

	}
	private String[] getNames(int flag, String[] albums)
	{
		if(flag==0)
		{
			Log.i(TAG, "----code comes to here----");
		   String[] paths=new String[albums.length];
		   String path=null;
		   String name=null;
		   for(int i=0; i<albums.length; i++)
		   {
			 path=albums[i].split("&")[1];
			 name=path.substring(path.lastIndexOf("/")+1);
			 Log.i(TAG, "path="+path+"; name="+name);
			 paths[i]=name;
		   }
		   return paths;
		}
		else if(flag==1)
		{
			String[] ids=new String[albums.length];
			for(int i=0; i<albums.length; i++)
			{
				String id=albums[i].split("&")[0];
				
				ids[i]=id;
			}
			return ids;
		}
		else 
			return albums;
	}
}
