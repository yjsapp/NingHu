package com.superapp.gps;



public class GpsUploadThread extends Thread {
	
	private boolean running = false;
	public void startThread(){
		this.running = true;
		super.start();
	}
	public void run() {
		
		
		//final Message msg = Sip.CreateMessage(SipInfo.sip_provider, msg_type, SipInfo.To, SipInfo.From);//Gps�ϴ���Ϣ		 
	  
	
	while(running){
		
		try {
			sleep(10*1000);
			
			 //  SipInfo.sip_provider.sendMessage(msg);
			
			System.out.println("����="+GPSInfo.Latitude+"---------"+"γ��"+GPSInfo.Longitude);
			//�ϴ�GPS ����
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
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
