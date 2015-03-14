package com.superapp.main;

import android.graphics.Bitmap;

public interface ImageGetterCallback {
	
	
	 public void imageLoaded(int pos, int offset, Bitmap bitmap,
             boolean isThumb);
public boolean wantsThumbnail(int pos, int offset);
public boolean wantsFullImage(int pos, int offset);
public int fullImageSizeToUse(int pos, int offset);
public void completed();
public int [] loadOrder();

}
