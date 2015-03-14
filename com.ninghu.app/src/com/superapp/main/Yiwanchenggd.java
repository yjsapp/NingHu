package com.superapp.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;



public class Yiwanchenggd extends Activity{
    
	public TextView Jieshouren;
	public TextView Gongdanhao;
	public TextView Jieshoushijian;
	public TextView Xinxilaiyuan;
	public TextView Shijianbiaoti;
	public TextView Renwuleixing;
	public TextView Yanzhongchengdu;
	public TextView Shijiandidian;
	public TextView Fashengshijian;
	public TextView Chexing;
	public TextView Chehao;
	
	public TextView Tingchedidian;
	
	public TextView Zaihuoliang;
	
	public TextView Zhuanghao;
	public TextView Qingzhangchechehao;
	public TextView Qingzhangchechexing;
	public TextView Qingzhangchezhujia;
	public TextView Qingzhangchefujia;
	public TextView Zhanyongchedao;
	
	
	
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yiwanchenggd);
        
       
	    Init();
	    Display();
	
//	  Log.e("TAG", TelephoneInfo.StageNumber);
        	}
    
   
    	       

    	 
	
	
	    

		
//        初始化控件
		   
   public  void Init() {
	   

	
	   Jieshouren= (TextView)findViewById(R.id.jieshouren);
	   Gongdanhao= (TextView)findViewById(R.id.gongdanhao);
	   Jieshoushijian= (TextView)findViewById(R.id.jieshoushijian);
	   Xinxilaiyuan= (TextView)findViewById(R.id.xinxilaiyuan);
	   Shijianbiaoti= (TextView)findViewById(R.id.shijianbiaoti);
	   Renwuleixing= (TextView)findViewById(R.id.renwuleixing);
	   Yanzhongchengdu= (TextView)findViewById(R.id.yanzhongchengdu);
	   Shijiandidian= (TextView)findViewById(R.id.shijiandidian);
	   Fashengshijian= (TextView)findViewById(R.id.fashengshijian);
	   Chexing= (TextView)findViewById(R.id.chexing);
	   Chehao= (TextView)findViewById(R.id.chehao);
	   Tingchedidian= (TextView)findViewById(R.id.tingchedidian);
	   Zaihuoliang= (TextView)findViewById(R.id.zaihuoliang);
	   Zhuanghao= (TextView)findViewById(R.id.zhuanghao);
	   Qingzhangchechehao= (TextView)findViewById(R.id.qingzhangchechehao);
	   Qingzhangchechexing= (TextView)findViewById(R.id.qingzhangchechexing);
	   
	   Qingzhangchezhujia= (TextView)findViewById(R.id.qingzhangchezhujia);
	   Qingzhangchefujia= (TextView)findViewById(R.id.qingzhangchefujia);
	   Zhanyongchedao= (TextView)findViewById(R.id.zhanyongchedao);
	   
	   
	  
	   
	   }
    
   
 public void Display(){
	 		  
		
		   

	 		 Jieshouren.setText("潘丽策");
	 		   Gongdanhao.setText("38383838383838383838383838383838388383883838383838");
	 		   Jieshoushijian.setText("2014-03-08");
	 		   Xinxilaiyuan.setText("王老板");
	 		   Shijianbiaoti.setText("有人猝死");
	 		   Renwuleixing.setText("清理尸体");
	 		   Yanzhongchengdu.setText("不严重");
	 		   Shijiandidian.setText("健B301");
	 		   Fashengshijian.setText("2014-03-08 03:08");
	 		   Chexing.setText("奥迪A6L");
	 		   Chehao.setText("383838");
	 		   Tingchedidian.setText("健B停车场");
	 		   Zaihuoliang.setText("21g");
	 		   Zhuanghao.setText("383838");
	 		   Qingzhangchechehao.setText("3838383");
	 		   Qingzhangchechexing.setText("自行车");
	 		   
	 		   Qingzhangchezhujia.setText("王惠州");
	 		   Qingzhangchefujia.setText("潘丽策");
	 		   Zhanyongchedao.setText("门口");
	

  
 }
  
  
  
  } 
   
   
 

