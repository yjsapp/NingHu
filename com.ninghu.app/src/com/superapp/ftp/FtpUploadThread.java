package com.superapp.ftp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPFile;
import org.zoolu.sip.message.Message;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.superapp.sip.Sip;
import com.superapp.sip.SipInfo;
import com.superapp.sip.XML.MSG_TYPE;

public class FtpUploadThread extends Thread {
	public  MSG_TYPE msg_type;
	private FTP ftp;
	String localpath;
	
	public FtpUploadThread(MSG_TYPE msg_type,String path){
		this.msg_type=msg_type;
		this.localpath=path;
	
	}
	public void run() {
		
		
		final Message msg = Sip.CreateMessage(SipInfo.sip_provider, msg_type, SipInfo.To, SipInfo.From);//Ftp上传信息查询		 
	  
	 int i=0;
	int j = 0;
	while(i<3){
	   SipInfo.sip_provider.sendMessage(msg);
				while(!FtpInfo.ftpinfo_query_respon&&j<40)
				{
					try {
						sleep(50);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					j++;
				}
				if(FtpInfo.ftpinfo_query_respon)
					break;
				else
					i++;
	}
			if(FtpInfo.ftpinfo_query_respon) {
		
			         Result result = null;
				
				 try {
			            if (ftp != null) {
			                // 关闭FTP服务
			                ftp.closeConnect();
			            }
			            // 初始化FTP
			           
			            ftp = new FTP(FtpInfo.serverip, FtpInfo.usename, FtpInfo.password);
			            // 打开FTP服务
			            ftp.openConnect();
			           
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
									
	            try {
	            	    	
	                // 上传
	            	
	            	File file=new File(localpath);
	            	
	            	
	            	String FTPPATH=CreatFtpPath(localpath);
	            	
	            	
	                result = ftp.uploading(file,FTPPATH );
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            if (result.isSucceed()) {
	              System.out.println("上传成功");
	                
	            } else {
	            	
	            	System.out.println("上传失败");
	            	
	               
	            }
				
				
				
				
					
				
				
			}
//				if(Info.on_media_state){
//					try {
//						rtpRecv = new RTP();
//			
//						rtpSocket = new DatagramSocket();										
//					} catch (Exception e) {
//						e.printStackTrace();
//					}										
//					rtpRecv.rtpSession = new RTPSession(rtpSocket, null);
//					rtpRecv.rtpSession.naivePktReception(false);
//					rtpRecv.rtpSession.RTPSessionRegister(rtpRecv, null, null);		
//					p = new Participant(Info.media_info_ip,Integer.valueOf(Info.media_info_port).intValue(),Integer.valueOf(Info.media_info_port).intValue()+1);										
//					rtpRecv.rtpSession.addParticipant(p);
//				    rtpRecv.SendActivePacket();//发送视频激活包
//				     rtpsave=new RtpSave(rtpRecv);
//					rtpsave.StartRtpSave();
//					rtpsave.start();
//					  sleep(1000);}	
//	 
}
	
	
	
	
	
	public String CreatFtpPath(String path){
		
		String jpgname = path.substring(path.lastIndexOf("/")+1);
		String year=jpgname.substring(0, 3);
		String month=jpgname.substring(5, 6);
		String day=jpgname.substring(8, 9);
		
		//String Path=SipInfo.devid+"/Pic/"+year+month+day+"/"+SipInfo.devid+"_"+path;
		String Path=SipInfo.devid+"/Pic/"+year+month+day+"/";
		
		System.out.println(Path);
		
		return Path;		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

