package com.superapp.view;

import java.io.File;


import com.superapp.main.R;
import com.superapp.tools.ImageCommon;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;



public class GridViewAdapter extends BaseAdapter
{
	  private static final String TAG = "GridViewAdapter";
	  private LayoutInflater mInflater;	  
	  private String[] rowid;
	  int flag=1; //sdcard
	  Context  c;
	  String album;
	  
	  public GridViewAdapter(Context context, int f, String name, String[] rows)
	  {
	    mInflater = LayoutInflater.from(context);
	    c=context;
	    rowid = rows;
	    flag=f;
	    album=name;
	  }
	  
	  public int getCount()
	  {
	    return rowid.length;
	  }

	  public Object getItem(int position)
	  {
		  return rowid[position];
	  }
	  
	  public long getItemId(int position)
	  {
	    return position;
	  }
	  

	  public View getView(int position,View convertView,ViewGroup parent)
	  {
		  Log.i(TAG, "getView Started! position="+position+"album="+album+"; name="+rowid[position]);
	      ImageView imageView;
	    
	      if(convertView == null)
	      {
	        convertView = mInflater.inflate(R.layout.gridr, null);  
	        imageView = (ImageView) convertView.findViewById(R.id.imageItem);
	        convertView.setTag(imageView);
	     }
	     else
	     {
	    	 imageView = (ImageView) convertView.getTag();
	     }

	      if(flag==0)
	      {
	    	  Log.i(TAG, "====code here====");
	    	    try
		        {
			    	Bitmap image=ImageCommon.readBitmaps(album+"_"+rowid[position]);
			    	if(image==null)
			    	{
			    		image=BitmapFactory.decodeResource(c.getResources(), R.drawable.icon);
			    	}
			    	imageView.setImageBitmap(image);
		        }
			    catch(Exception err)
			    {
			    	err.printStackTrace();
			    	Log.i(TAG, "get image by id had a unkonown error!");
			    }
	      }
	      else if(flag==1)  //sdcard
	      {
	    	  //imageView.setImageBitmap(ImageCommon.getFitSizePicture(new File(rowid[position])));
	    	 // imageView.setImageBitmap(decodeBitmap(rowid[position]));
	    	  imageView.setImageBitmap(Thumbnails.getThumbnail(c.getContentResolver(),Integer.valueOf(rowid[position]),Thumbnails.MICRO_KIND,new BitmapFactory.Options()));
	      }
	      else  //usbhost
	      {
	    	  Bitmap bitmap=ImageCommon.getFitSizePicture(new File(rowid[position]));
	    	  if(bitmap==null)
	    	  {
			       Resources res=c.getResources();
			       bitmap=BitmapFactory.decodeResource(res, R.drawable.icon);
	    	  }
			  imageView.setImageBitmap(bitmap);
			  
	      }
	    //  holder.icon.setImageBitmap(imageInfo.get(position).icon);
	  return convertView;
	  }
	  
	  
	  
	  public Bitmap decodeBitmap(String path)  
	    {  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inJustDecodeBounds = true;  
	        // 通过这个bitmap获取图片的宽和高         
	        Bitmap bitmap = BitmapFactory.decodeFile(path, options);  
	        if (bitmap == null)  
	        {  
	            System.out.println("bitmap为空");  
	        }  
	        float realWidth = options.outWidth;  
	        float realHeight = options.outHeight;  
	        System.out.println("真实图片高度：" + realHeight + "宽度:" + realWidth);  
	        // 计算缩放比         
	        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);  
	        if (scale <= 0)  
	        {  
	            scale = 1;  
	        }  
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。        
	        bitmap = BitmapFactory.decodeFile(path, options);  
//	        int w = bitmap.getWidth();  
//	        int h = bitmap.getHeight();  
//	        System.out.println("缩略图高度：" + h + "宽度:" + w);  
	        return bitmap;  
	    } 	
	  
	  
	  
	  
	  
}
