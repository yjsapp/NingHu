package com.superapp.gps;



public class GpsUploadThread extends Thread {
	
	private boolean running = false;
	public void startThread(){
		this.running = true;
		super.start();
	}
	public void run() {
		
		
		//final Message msg = Sip.CreateMessage(SipInfo.sip_provider, msg_type, SipInfo.To, SipInfo.From);//Gps上传信息		 
	  
	
	while(running){
		
		try {
			sleep(10*1000);
			
			 //  SipInfo.sip_provider.sendMessage(msg);
			
			System.out.println("经度="+GPSInfo.Latitude+"---------"+"纬度"+GPSInfo.Longitude);
			//上传GPS 数据
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	
				
				
	}
			

}
	
	
	public void setRunning(boolean running){
		this.running = running;
	}
	public void stopThread(){
		this.setRunning(false);
	}
	
	
	
	
	
	
}
