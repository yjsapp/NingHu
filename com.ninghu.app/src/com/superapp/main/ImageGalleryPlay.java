package com.superapp.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import com.superapp.tools.GetHandler;
import com.superapp.tools.ImageGetter;

import com.superapp.view.GalleryAdapter;
import com.superapp.view.ImageViewTouchBase;
import com.superapp.view.RotateBitmap;


import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;



public class ImageGalleryPlay extends Activity{
	
	private static final String TAG = "ImageGalleryView";
    private static final String PREF_SLIDESHOW_REPEAT =
        "pref_gallery_slideshow_repeat_key";
    
	Gallery gallery=null;
	Animation anim=null;
	String[] paths=null;
	List<String> mImagePathList;
	private ImageGetter mGetter;
	private final Random mRandom = new Random(System.currentTimeMillis());
	
	boolean mPaused = true;
	private boolean mSlideShowLoop = false;
    private int mSlideShowInterval=1000;
    private int mLastSlideShowImage;
	int mCurrentPosition = 0;
	
    // represents which style animation to use
    private int mAnimationIndex;
    private Animation [] mSlideShowInAnimation;
    private Animation [] mSlideShowOutAnimation;
    
    private SharedPreferences mPrefs;
    
    // Choices for what adjacents to load.
    private static final int[] sOrderAdjacents = new int[] {0, 1, -1};
    private static final int[] sOrderSlideshow = new int[] {0};
    
    GestureDetector mGestureDetector;
	final GetHandler mHandler = new GetHandler();	
    static final int MODE_NORMAL = 1;
    static final int MODE_SLIDESHOW = 2;
    private int mMode = MODE_NORMAL;
    // The image view displayed for normal mode.
    private ImageViewTouch mCurrentImageView;
    private Button mSaveConfirm;
    private Button mSaveCancel;
    // This is the cache for thumbnail bitmaps.
    private BitmapCache mCache;
	
	String path=null;
	GalleryAdapter galleryAdapter=null;
    private float scaleWidth=1, scaleHeight=1;
    
    private int mSlideShowImageCurrent = 0;
    private final ImageViewTouchBase [] mSlideShowImageViews =
            new ImageViewTouchBase[2];
    
    private Bitmap currentBitmap;
    private Bitmap effectsBitmap;
    private String currentPath;
    private int currentPosition;
    private int displayWidth, displayHeight;
    //FLING PARMS
    public static final int FLING_MIN_DISTANCE=100;
    public static final int FLING_MIN_VELOCITY=200;
    
    
    private void setupOnTouchListeners(View rootView) {
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

        OnTouchListener rootListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                // We do not use the return value of
                // mGestureDetector.onTouchEvent because we will not receive
                // the "up" event if we return false for the "down" event.
                return true;
            }
        };
        rootView.setOnTouchListener(rootListener);
    }

    private class MyGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            //if (mPaused) return false;
            ImageViewTouch imageView = mCurrentImageView;
            if (imageView.getScale() > 1F) {
                imageView.postTranslateCenter(-distanceX, -distanceY);
            }
            return true;
        }

        @Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
        	if((e1.getX()-e2.getX()>FLING_MIN_DISTANCE)&&
        			Math.abs(velocityX)>FLING_MIN_VELOCITY)
        	{
        		moveNextOrPrevious(1);
        	}
        	else if((e2.getX()-e1.getX()>FLING_MIN_DISTANCE)&&
        			Math.abs(velocityX)>FLING_MIN_VELOCITY)
			{
        		moveNextOrPrevious(-1);
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
        public boolean onSingleTapUp(MotionEvent e) {
            //if (mPaused) return false;
            //setMode(MODE_NORMAL);
            //openOptionsMenu();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //if (mPaused) return false;
            //showOnScreenControls();
            //scheduleDismissOnScreenControls();
        	setMode(MODE_NORMAL);
           // openOptionsMenu();
            Log.e(TAG, "danjishijian1111111111111111111111111122222222222222222222" );
            Intent intent =new Intent(ImageGalleryPlay.this,SendImage.class);
            startActivity(intent);
            return true;
        }

        @Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return super.onDown(e);
		}

		@Override
        public boolean onDoubleTap(MotionEvent e) {
            //if (mPaused) return false;
            ImageViewTouch imageView = mCurrentImageView;

            // Switch between the original scale and 3x scale.
            if (imageView.getScale() > 2F) {
                mCurrentImageView.zoomTo(1f);
            } else {
              //  mCurrentImageView.zoomToPoint(3f, e.getX(), e.getY());
            }
            return true;
        }
    }
    
    private Animation makeInAnimation(int id) {
        Animation inAnimation = AnimationUtils.loadAnimation(this, id);
        return inAnimation;
    }

    private Animation makeOutAnimation(int id) {
        Animation outAnimation = AnimationUtils.loadAnimation(this, id);
        return outAnimation;
    }
    
    private static int getPreferencesInteger(
            SharedPreferences prefs, String key, int defaultValue) {
        String value = prefs.getString(key, null);
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            Log.e(TAG, "couldn't parse preference: " + value, ex);
            return defaultValue;
        }
    }
    void setMode(int mode) {
        if (mMode == mode) {
            return;
        }
        View slideshowPanel = findViewById(R.id.slideShowContainer);
        View normalPanel = findViewById(R.id.abs);

        Window win = getWindow();
        mMode = mode;
        if (mode == MODE_SLIDESHOW) {
        	slideshowPanel.setVisibility(View.VISIBLE);
            normalPanel.setVisibility(View.GONE);

            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mCurrentImageView.clear();
            slideshowPanel.getRootView().requestLayout();

            // The preferences we want to read:
            //   mUseShuffleOrder
            //   mSlideShowLoop
            //   mAnimationIndex
            //   mSlideShowInterval
            mSlideShowLoop = mPrefs.getBoolean(PREF_SLIDESHOW_REPEAT, false);
          //  mAnimationIndex = getPreferencesInteger(
          //          mPrefs, "pref_gallery_slideshow_transition_key", -1);
            Log.i(TAG, "mAnimationIndex="+mAnimationIndex);
            mSlideShowInterval = getPreferencesInteger(
                    mPrefs, "pref_gallery_slideshow_interval_key", 3) * 1000;
        } else {
            slideshowPanel.setVisibility(View.GONE);
            normalPanel.setVisibility(View.VISIBLE);

            win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);            
            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (mGetter != null) {
                mGetter.cancelCurrent();
            }
            for (ImageViewTouchBase ivt : mSlideShowImageViews) {
                ivt.clear();
            }
            // mGetter null is a proxy for being paused
            if (mGetter != null) {
                setImage(mCurrentPosition, true);
            }
        }
    }

/**
 * 鍔犺浇骞荤伅鐗囨挱鏀惧浘鐗?
 * @param requestedPos
 * @param delay
 * @param firstCall
 */
    private void loadNextImage(final int requestedPos, final long delay,
                               final boolean firstCall) {

        final long targetDisplayTime = System.currentTimeMillis() + delay;

        ImageGetterCallback cb = new ImageGetterCallback() {
            public void completed() {
            }

            public boolean wantsThumbnail(int pos, int offset) {
                return true;
            }

            public boolean wantsFullImage(int pos, int offset) {
                return false;
            }

            public int [] loadOrder() {
                return sOrderSlideshow;
            }

            public int fullImageSizeToUse(int pos, int offset) {
                return 480; // TODO compute this
            }
            
			public void imageLoaded(int pos, int offset, Bitmap bitmap,
					boolean isThumb) {
                long timeRemaining = Math.max(0,
                        targetDisplayTime - System.currentTimeMillis());
                final Bitmap bm=bitmap;
                mHandler.postDelayedGetterCallback(new Runnable() {
                    public void run() {
                        if (mMode == MODE_NORMAL) {
                            return;
                        }

                        ImageViewTouchBase oldView =
                                mSlideShowImageViews[mSlideShowImageCurrent];

                        if (++mSlideShowImageCurrent
                                == mSlideShowImageViews.length) {
                            mSlideShowImageCurrent = 0;
                        }

                        ImageViewTouchBase newView =
                                mSlideShowImageViews[mSlideShowImageCurrent];
                        newView.setVisibility(View.VISIBLE);
                        newView.setImageRotateBitmapResetBase(new RotateBitmap(bm,0), true);
                        newView.bringToFront();
                        currentBitmap=bm;

                        int animation = 0;

                        if (mAnimationIndex == -1) {
                            int n = mRandom.nextInt(
                                    mSlideShowInAnimation.length);
                            animation = n;
                        } else {
                            animation = mAnimationIndex;
                        }

                        Animation aIn = mSlideShowInAnimation[animation];
                        newView.startAnimation(aIn);
                        newView.setVisibility(View.VISIBLE);

                        Animation aOut = mSlideShowOutAnimation[animation];
                        oldView.setVisibility(View.INVISIBLE);
                        oldView.startAnimation(aOut);

                        mCurrentPosition = requestedPos;

                        if (mCurrentPosition == mLastSlideShowImage
                                && !firstCall) {
                            if (!mSlideShowLoop) {
                                setMode(MODE_NORMAL);
                                return;
                            }
                        }

                        loadNextImage(
                                (mCurrentPosition + 1) % mImagePathList.size(),
                                mSlideShowInterval, false);
                    }
                }, timeRemaining);
            }

        };
        // Could be null if we're stopping a slide show in the course of pausing
        if (mGetter != null) {
            int pos = requestedPos;
            mGetter.setPosition(pos, cb, mImagePathList, mHandler);
        }
    }
    
    /**
     * 鍔犺浇涓婁竴寮犱笅涓?紶鍥剧墖
     * @param delta
     */
    private void moveNextOrPrevious(int delta) {
        int nextImagePos = mCurrentPosition + delta;
        if ((0 <= nextImagePos) && (nextImagePos < mImagePathList.size())) {
            setImage(nextImagePos, true);
        }
    }
    
    void setImage(int pos, boolean showControls) {
        mCurrentPosition = pos;

        Bitmap b = mCache.getBitmap(pos);
        if (b != null) {           
            mCurrentImageView.setImageRotateBitmapResetBase(
                    new RotateBitmap(b, 0), true);             
            currentBitmap=b;
        }

        ImageGetterCallback cb = new ImageGetterCallback() {
            public void completed() {
            }

            public boolean wantsThumbnail(int pos, int offset) {
                return !mCache.hasBitmap(pos + offset);
            }

            public boolean wantsFullImage(int pos, int offset) {
                return offset == 0;
            }

            public int fullImageSizeToUse(int pos, int offset) {
                // this number should be bigger so that we can zoom.  we may
                // need to get fancier and read in the fuller size image as the
                // user starts to zoom.
                // Originally the value is set to 480 in order to avoid OOM.
                // Now we set it to 2048 because of using
                // native memory allocation for Bitmaps.
                final int imageViewSize = 2048;
                return imageViewSize;
            }

            public int [] loadOrder() {
                return sOrderAdjacents;
            }
			
			public void imageLoaded(int pos, int offset, Bitmap bitmap,
					boolean isThumb) {
				// TODO Auto-generated method stub
                // We may get a result from a previous request. Ignore it.
                if (pos != mCurrentPosition) {
                    bitmap.recycle();
                    return;
                }

                if (isThumb) {
                    mCache.put(pos + offset, bitmap);
                }
                if (offset == 0) {
                    // isThumb: We always load thumb bitmap first, so we will
                    // reset the supp matrix for then thumb bitmap, and keep
                    // the supp matrix when the full bitmap is loaded.
                    mCurrentImageView.setImageRotateBitmapResetBase(new RotateBitmap(bitmap,0), isThumb);
                    currentBitmap=bitmap;
                }
				
			}
        };

        // Could be null if we're stopping a slide show in the course of pausing
        if (mGetter != null) {
            mGetter.setPosition(pos, cb, mImagePathList, mHandler);
        }
    }

	@Override  
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	       
	    requestWindowFeature(Window.FEATURE_NO_TITLE);    
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,      
	                      WindowManager.LayoutParams.FLAG_FULLSCREEN);    
	       
	    setContentView(R.layout.galleryview);  
	    
        mCurrentImageView = (ImageViewTouch) findViewById(R.id.image);
        mCurrentImageView.setEnableTrackballScroll(true);
        mCache = new BitmapCache(3);
        mCurrentImageView.setRecycler(mCache);
        
        mSaveConfirm=(Button)findViewById(R.id.save_confirm);
        mSaveCancel=(Button)findViewById(R.id.save_cancel);
        mSaveConfirm.setOnClickListener((android.view.View.OnClickListener) onSaveConfirmListener);
        mSaveCancel.setOnClickListener((android.view.View.OnClickListener) onSaveCancelListener);
	    makeGetter();
        mAnimationIndex = -1;

       
       

        mSlideShowImageViews[0] =
                (ImageViewTouchBase) findViewById(R.id.image1_slideShow);
        mSlideShowImageViews[1] =
                (ImageViewTouchBase) findViewById(R.id.image2_slideShow);
        for (ImageViewTouchBase v : mSlideShowImageViews) {
            v.setVisibility(View.INVISIBLE);
            v.setRecycler(mCache);
        }
	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		/*Display display = getWindowManager().getDefaultDisplay();
		displayWidth=display.getWidth();
		displayHeight=display.getHeight();
		Log.i("view_Display" , "height:" +displayWidth);
		Log.i("view_Display" , "width:" +displayHeight);*/
          
		 // DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		 // Log.i("view_DisplayMetrics" , "height" +displayMetrics.heightPixels);
		 // Log.i("view_DisplayMetrics" , "width" +displayMetrics.widthPixels);
	    /*Animation load_anim=AnimationUtils.loadAnimation(ImageGalleryView.this,R.anim.load_photo);
	    anim=AnimationUtils.loadAnimation(ImageGalleryView.this,R.anim.to_large); 
	    //gallery = (Gallery)this.findViewById(R.id.galleryView);
	    mCurrentImageView.startAnimation(load_anim);*/
	    Intent intent = getIntent();      
	    paths = intent.getStringArrayExtra("data");
        if(paths==null)
        {
        	Log.i(TAG, "pathArray=null");
        	return;
        }
	    path=intent.getStringExtra("path");
        int position=intent.getIntExtra("id", 0);
        mCurrentPosition=position;
        mLastSlideShowImage = mCurrentPosition;
        Log.i("ImageGalleryView_onCreate", "position="+position+"; path="+path);
        mImagePathList=new ArrayList<String>();
        for(String str: paths)
	    {
        	mImagePathList.add(str);		    
	    }
        
        setupOnTouchListeners(findViewById(R.id.rootLayout));
        //galleryAdapter=new GalleryAdapter(this,paths, displayWidth, displayHeight);
        //gallery.setAdapter(galleryAdapter);
        
        // currentPosition=position;
		//currentPath=(String) galleryAdapter.getItem(position);
		Log.i(TAG, "currentPostion="+currentPosition+"; currentPath="+currentPath);
		//mCurrentImageView = galleryAdapter.getCurrentView();
		//currentBitmap=galleryAdapter.getBitmap();	

	 }
	
	
	
	 protected static final int MENU_ABOUT=Menu.FIRST;

	 protected static final int MENU_EXIT=Menu.FIRST+1;
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
	 	// TODO Auto-generated method stub
	 	super.onCreateOptionsMenu(menu);
	 	menu.add(0, MENU_ABOUT, 0, "上传图片");
	 //	menu.add(0, MENU_EXIT, 0, "删除图片");
	 	return true;
	 }


	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
	 	// TODO Auto-generated method stub
	 	super.onOptionsItemSelected(item);
	 	  Log.e(TAG, "danjishijian1111111111111111111111111122222222222222222222" );
	 Intent intent =new Intent(ImageGalleryPlay.this,SendImage.class);
	 startActivity(intent);
	 	return true;
	 }	
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        int count = mImagePathList.size();
        if (count == 0) {
            finish();
            return;
        } else if (count <= mCurrentPosition) {
            mCurrentPosition = count - 1;
        }
        if (mGetter == null) {
            makeGetter();
        }
        setImage(mCurrentPosition, false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        // mGetter could be null if we call finish() and leave early in
        // onStart().
        if (mGetter != null) {
            mGetter.cancelCurrent();
            mGetter.stop();
            mGetter = null;
        }
        // removing all callback in the message queue
        mHandler.removeAllGetterCallbacks();
        
        mCurrentImageView.clear();
        mCache.clear();
	}

	private void makeGetter() {
	    mGetter = new ImageGetter(getContentResolver());
	}
	 public void startAnimation()
	 {
		 mCurrentImageView.startAnimation(anim);
	 }	 

    

    Handler handler = new Handler(){  
           public void handleMessage(Message msg) {  
    	          switch (msg.what) {      
    	            case 1:
    	            	currentPosition++;
    	            	int animationPosition=currentPosition%paths.length;
    	            	Log.i(TAG, "currentPosition="+currentPosition+"; animationPosition="+animationPosition);
    	            	gallery.setSelection(animationPosition);
    	                break;      
    	            }      
    	            super.handleMessage(msg);  
    	   }       
     };  
    
     private Runnable runnable = new Runnable() {  
    	    public void run() {  
    	       // update();  
    	    	Message message = new Message();      
                message.what = 1;      
                handler.sendMessage(message); 
    	        handler.postDelayed(this, 3000);  
    	    }  
    	 
     };
    	
     TimerTask task = new TimerTask(){  
    	 public void run() {  
    	    Message message = new Message();      
            message.what = 1;      
            handler.sendMessage(message);    
         }            
     }; 
    	 
	
	
	
	

	
	
    private android.view.View.OnClickListener onSaveConfirmListener = new android.view.View.OnClickListener(){
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			File file = new File(mImagePathList.get(mCurrentPosition));
	          FileOutputStream out;
	          try{
	                out = new FileOutputStream(file);
	                if(effectsBitmap.compress(Bitmap.CompressFormat.PNG, 100, out))
	                {
	                    out.flush();
	                    out.close();
	                    return ;
	                }
	          }
	          catch (FileNotFoundException e)
	          {
	        	   Log.i(TAG, "saveBitmap has err! FileNotFoundException!");
	               e.printStackTrace();
	          }
	          catch (IOException e)
	          {
	        	  Log.i(TAG, "saveBitmap has err! IOException!");
	               e.printStackTrace();
	          }
			   mSaveConfirm.setVisibility(View.GONE);
			   mSaveCancel.setVisibility(View.GONE);
		}
    	
    };
    private android.view.View.OnClickListener onSaveCancelListener = new android.view.View.OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			 mCurrentImageView.setImageBitmap(currentBitmap);	
			   mSaveConfirm.setVisibility(View.GONE);
			   mSaveCancel.setVisibility(View.GONE);
		}
    	
    };
	OnClickListener onSetEffectsSelect = new OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	          
	      switch(which)
	      {
	        case 0:
	        	try{	
	        		Log.v(TAG, "Set Effects!");
	    			int Width = currentBitmap.getWidth();
	    			int Height = currentBitmap.getHeight();
	    			Log.v(TAG, "Set Effects!w="+Width+"h="+Height);
	    			int [] pixelBuf = new int[Width * Height];	
	    			Log.v(TAG, "Set Effects!pixelBuf.length="+pixelBuf.length);
	    			effectsBitmap=Bitmap.createBitmap(Width, Height, Config.ARGB_8888);
					//Bitmap newBitmap=Bitmap.createBitmap(currentBitmap);
	    			currentBitmap.getPixels(pixelBuf, 0, Width, 0, 0, Width, Height);
	    			Log.v(TAG, "Set Effects!w="+Width+"h="+Height);
	        		for(int x=0;x<Width;x++)
	        			for(int y=0;y<Height;y++)
	        			{
	        				int r;
	        				int g;
	        				int b;	
	        				int index = y*Width+x;
	        				r=255-(pixelBuf[index]>>16)&0xff;
	        				g=255-(pixelBuf[index]>>8)&0xff;
	        				b=255-(pixelBuf[index])&0xff;
	        				pixelBuf[index]=0xFF000000|(r<<16)|(g<<8)|b;
	        			}
	        		Log.v(TAG, "Set Effects!");	        		
	        		effectsBitmap.setPixels(pixelBuf, 0, Width, 0, 0, Width, Height);
	        	   mCurrentImageView.setImageBitmap(effectsBitmap);	 
	    		   mSaveConfirm.setVisibility(View.VISIBLE);
	    		   mSaveCancel.setVisibility(View.VISIBLE);
	        	   //currentBitmap=newBitmap;
	        	   //mCurrentImageView.setImageRotateBitmapResetBase(new RotateBitmap(newBitmap,0), false);
	        	   
	        	}
	        	catch(Exception e)
	        	{
	        		Log.v(TAG, "Set Effects Fails!");
	        	}
	    		break;
	        case 1:
	        	
	    		break;
	        case 2:
	        	
	    		break;
	      }
	   }
    };
	
}
class ImageViewTouch extends ImageViewTouchBase {
    private final ImageGalleryPlay mViewImage;
    private boolean mEnableTrackballScroll;

    public ImageViewTouch(Context context) {
        super(context);
        mViewImage = (ImageGalleryPlay) context;
    }

    public ImageViewTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewImage = (ImageGalleryPlay) context;
    }

    public void setEnableTrackballScroll(boolean enable) {
        mEnableTrackballScroll = enable;
    }

    protected void postTranslateCenter(float dx, float dy) {
        super.postTranslate(dx, dy);
        center(true, true);
    }

    private static final float PAN_RATE = 20;

    // This is the time we allow the dpad to change the image position again.
    private long mNextChangePositionTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mViewImage.mPaused) return false;

        // Don't respond to arrow keys if trackball scrolling is not enabled
        if (!mEnableTrackballScroll) {
            if ((keyCode >= KeyEvent.KEYCODE_DPAD_UP)
                    && (keyCode <= KeyEvent.KEYCODE_DPAD_RIGHT)) {
                return super.onKeyDown(keyCode, event);
            }
        }

        int current = mViewImage.mCurrentPosition;

        int nextImagePos = -2; // default no next image
        try {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT: {
                    if (getScale() <= 1F && event.getEventTime()
                            >= mNextChangePositionTime) {
                        nextImagePos = current - 1;
                        mNextChangePositionTime = event.getEventTime() + 500;
                    } else {
                        panBy(PAN_RATE, 0);
                        center(true, false);
                    }
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT: {
                    if (getScale() <= 1F && event.getEventTime()
                            >= mNextChangePositionTime) {
                        nextImagePos = current + 1;
                        mNextChangePositionTime = event.getEventTime() + 500;
                    } else {
                        panBy(-PAN_RATE, 0);
                        center(true, false);
                    }
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_UP: {
                    panBy(0, PAN_RATE);
                    center(false, true);
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_DOWN: {
                    panBy(0, -PAN_RATE);
                    center(false, true);
                    return true;
                }
                /*case KeyEvent.KEYCODE_DEL:
                    MenuHelper.deletePhoto(
                            mViewImage, mViewImage.mDeletePhotoRunnable);
                    break;*/
            }
        } finally {
            if (nextImagePos >= 0
                    && nextImagePos < mViewImage.mImagePathList.size()) {
                synchronized (mViewImage) {
                    //mViewImage.setMode(ImageGalleryView.MODE_NORMAL);
                    mViewImage.setImage(nextImagePos, true);
                }
           } else if (nextImagePos != -2) {
               center(true, true);
           }
        }

        return super.onKeyDown(keyCode, event);
    }
}


// This is a cache for Bitmap displayed in ViewImage (normal mode, thumb only).
class BitmapCache implements ImageViewTouchBase.Recycler {
    public static class Entry {
        int mPos;
        Bitmap mBitmap;
        public Entry() {
            clear();
        }
        public void clear() {
            mPos = -1;
            mBitmap = null;
        }
    }

    private final Entry[] mCache;

    public BitmapCache(int size) {
        mCache = new Entry[size];
        for (int i = 0; i < mCache.length; i++) {
            mCache[i] = new Entry();
        }
    }

    // Given the position, find the associated entry. Returns null if there is
    // no such entry.
    private Entry findEntry(int pos) {
        for (Entry e : mCache) {
            if (pos == e.mPos) {
                return e;
            }
        }
        return null;
    }

    // Returns the thumb bitmap if we have it, otherwise return null.
    public synchronized Bitmap getBitmap(int pos) {
        Entry e = findEntry(pos);
        if (e != null) {
            return e.mBitmap;
        }
        return null;
    }

    public synchronized void put(int pos, Bitmap bitmap) {
        // First see if we already have this entry.
        if (findEntry(pos) != null) {
            return;
        }

        // Find the best entry we should replace.
        // See if there is any empty entry.
        // Otherwise assuming sequential access, kick out the entry with the
        // greatest distance.
        Entry best = null;
        int maxDist = -1;
        for (Entry e : mCache) {
            if (e.mPos == -1) {
                best = e;
                break;
            } else {
                int dist = Math.abs(pos - e.mPos);
                if (dist > maxDist) {
                    maxDist = dist;
                    best = e;
                }
            }
        }

        // Recycle the image being kicked out.
        // This only works because our current usage is sequential, so we
        // do not happen to recycle the image being displayed.
        if (best.mBitmap != null) {
            best.mBitmap.recycle();
        }

        best.mPos = pos;
        best.mBitmap = bitmap;
    }

    // Recycle all bitmaps in the cache and clear the cache.
    public synchronized void clear() {
        for (Entry e : mCache) {
            if (e.mBitmap != null) {
                e.mBitmap.recycle();
            }
            e.clear();
        }
    }

    // Returns whether the bitmap is in the cache.
    public synchronized boolean hasBitmap(int pos) {
        Entry e = findEntry(pos);
        return (e != null);
    }

    // Recycle the bitmap if it's not in the cache.
    // The input must be non-null.
    public synchronized void recycle(Bitmap b) {
        for (Entry e : mCache) {
            if (e.mPos != -1) {
                if (e.mBitmap == b) {
                    return;
                }
            }
        }
        b.recycle();
    }
}
