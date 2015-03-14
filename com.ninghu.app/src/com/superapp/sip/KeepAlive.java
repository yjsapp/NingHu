package com.superapp.sip;


import java.util.Random;

import org.zoolu.sip.message.Message;

import com.superapp.sip.XML.MSG_TYPE;

public class KeepAlive extends Thread
{
	private boolean running = false;
	private boolean bStartActive=false;//rtp¼¤»î	
	public void startThread(){
		this.running = true;
		super.start();
	}
	public void run(){
		try{
			
			
			
			
			while(running){
				Thread.sleep(10*1000);
				
				
				Message message=Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.EKeepalive, SipInfo.To, SipInfo.From);
				SipInfo.sip_provider.sendMessage(message);
				System.out.println("xintiao");
				
				
				if(bStartActive){
					for(int i=0;i<3;i++){
			//						DevList.rtpRecv.SendActivePacket();
					}
				}					
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	public void setRunning(boolean running){
		this.running = running;
	}
	public void stopThread(){
		this.setRunning(false);
	}
	public void setMediaActive(boolean bStartActive){
		this.bStartActive=bStartActive;
	}
}
