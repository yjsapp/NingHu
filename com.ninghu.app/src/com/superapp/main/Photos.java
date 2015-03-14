package com.superapp.main;

import java.io.File;
import java.util.Date;

import com.superapp.ftp.FtpUploadThread;
import com.superapp.sip.XML.MSG_TYPE;
import com.superapp.view.PhotoImageView;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Photos extends Activity implements OnClickListener{
	
	private PhotoImageView mImageView;
	private Bitmap mBitmap;
	
	
	public static final String IMAGE_PATH = "My_Super_App";
	private static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
	        .getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD,IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
	        "images/qiangzhang");
	
	
	
	private MediaScannerConnection mediaScanConn = null; 
    private ImageSannerClient client = null; 
    private File filePath = null; 
    private String fileType = null; 
	
	
	
	
	
	
	
	
	
	//private CropImage mCrop;
	
	private Button FaSong;
	private Button mCancel,rotateLeft,rotateRight;
	private String mPath = "CropImageActivity";
	private String TAG = "";
	public int screenWidth = 0;
	public int screenHeight = 0;
	
	private ProgressBar mProgressBar;
	
	public static final int SHOW_PROGRESS = 2000;

	public static final int REMOVE_PROGRESS = 2001;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				mHandler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}

		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);
        
        init();
//        client = new ImageSannerClient(); 
//        mediaScanConn = new MediaScannerConnection(this, client); 
//        scanfile(new File("/sdcard")); 
        
        
        
        
        
    }
    @Override
    protected void onStop(){
    	super.onStop();
    	if(mBitmap!=null){
    		mBitmap=null;
    	}
    }
    
    private void init()
    {
    	getWindowWH();
    	mPath = getIntent().getStringExtra("path");
    	
    	sdScan(mPath);
    	Log.i(TAG, "得到的图片的路径是 = " + mPath);
        mImageView = (PhotoImageView) findViewById(R.id.gl_modify_avatar_image);
        FaSong = (Button) this.findViewById(R.id.gl_modify_avatar_fasong);
        mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
        rotateLeft = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_left);
        rotateRight = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_right);
        FaSong.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        try{
            mBitmap = createBitmap(mPath,screenWidth,screenHeight);
            if(mBitmap==null){
            	Toast.makeText(Photos.this, "没有找到图片", 0).show();
    			finish();
            }else{
            	resetImageView(mBitmap);
            }
        }catch (Exception e) {
        	Toast.makeText(Photos.this, "没有找到图片", 0).show();
			finish();
		}
        addProgressbar();       
    }
    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth=dm.widthPixels;
		screenHeight=dm.heightPixels;
	}
    private void resetImageView(Bitmap b){
    	 mImageView.clear();
    	 mImageView.setImageBitmap(b);
         mImageView.setImageBitmapResetBase(b, true);
        // mCrop = new CropImage(this, mImageView,mHandler);
        // mCrop.crop(b);
    }
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.gl_modify_avatar_cancel:
//    		mCrop.cropCancel();
    		//finish();
    		
    		String status = Environment.getExternalStorageState();
    		if (status.equals(Environment.MEDIA_MOUNTED)) {
    			try {
    				localTempImageFileName = "";
    				localTempImageFileName = String.valueOf((new Date())
    						.getTime()) + ".png";
    				File filePath = FILE_PIC_SCREENSHOT;
    				if (!filePath.exists()) {
    					filePath.mkdirs();
    				}
    				Intent intent = new Intent(
    						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    				File f = new File(filePath, localTempImageFileName);
    				// localTempImgDir和localTempImageFileName是自己定义的名字
    				Uri u = Uri.fromFile(f);
    				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
    				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
    				startActivityForResult(intent, 8);
    			} catch (ActivityNotFoundException e) {
    				//
    			}
    		}
    			
    		break;
    	case R.id.gl_modify_avatar_fasong:
    		MSG_TYPE msg_type=MSG_TYPE.EFtpinfo_query;
    		FtpUploadThread ftp=new FtpUploadThread(msg_type,mPath);
    		
    		ftp.start();
    		break;
    	case R.id.gl_modify_avatar_rotate_left:
    	//	mCrop.startRotate(270.f);
    		break;
    	case R.id.gl_modify_avatar_rotate_right:
    		//mCrop.startRotate(90.f);
    		break;
    		
    	}
    }
    protected void addProgressbar() {
		mProgressBar = new ProgressBar(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addContentView(mProgressBar, params);
		mProgressBar.setVisibility(View.INVISIBLE);
	}
    
    public Bitmap createBitmap(String path,int w,int h){
    	try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
 
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
    		if (data != null) {
    			Uri uri = data.getData();
    			if (!TextUtils.isEmpty(uri.getAuthority())) {
    				Cursor cursor = getContentResolver().query(uri,
    						new String[] { MediaStore.Images.Media.DATA },
    						null, null, null);
    				if (null == cursor) {
//    					Toast.makeText(mCon, "图片没找到", 0).show();
    					return;
    				}
    				cursor.moveToFirst();
    				String path = cursor.getString(cursor
    						.getColumnIndex(MediaStore.Images.Media.DATA));
    				cursor.close();
    				Log.i(TAG,"path=" + path);
    				Intent intent = new Intent(this, Photos.class);
    				intent.putExtra("path", path);
    				startActivityForResult(intent, FLAG_MODIFY_FINISH);
    			} else {
    				Log.i(TAG,"path=" + uri.getPath());
    				Intent intent = new Intent(this, Photos.class);
    				intent.putExtra("path", uri.getPath());
    				startActivityForResult(intent, FLAG_MODIFY_FINISH);
    			}
    		}
    	} 
    	
    	else if (requestCode == 8&& resultCode == RESULT_OK) {
    		
    		
    		
    		
    		File f = new File(FILE_PIC_SCREENSHOT,localTempImageFileName);
    		
    		Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath()); 
    		 mImageView.clear();
 			mImageView.setImageBitmap(b);
    		
    	}
    }
    
    public void sdScan(String ss){     
    	
    	 //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(ss)));
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, exportToGallery(ss)));
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"    
                + Environment.getExternalStorageDirectory()))); 
    
    
    } 
    
    private Uri exportToGallery(String filename) {
        // Save the name and description of a video in a ContentValues map.
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.Video.Media.DATA, filename);
        // Add a new record (identified by uri)
        final Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://"+ filename)));
        return uri;
    }
    
    
    
    
    
    
    class ImageSannerClient implements MediaScannerConnectionClient { 

public void onMediaScannerConnected() { 
    Log.e("---------", "media service connected"); 

    if (filePath != null) { 

        if (filePath.isDirectory()) { 
            File[] files = filePath.listFiles(); 
            if (files != null) { 
                for (int i = 0; i < files.length; i++) { 
                    if (files[i].isDirectory()) 
                        scanfile(files[i]); 
                    else { 
                        mediaScanConn.scanFile( 
                                files[i].getAbsolutePath(), fileType); 
                    } 
                } 
            } 
        } 
    } 

    filePath = null; 

    fileType = null; 

} 

public void onScanCompleted(String path, Uri uri) { 
    // TODO Auto-generated method stub  
    mediaScanConn.disconnect(); 
} 

} 

private void scanfile(File f) { 
this.filePath = f; 
mediaScanConn.connect(); 
} 
 
    
}
