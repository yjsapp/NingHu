package com.superapp.sip;

import com.superapp.G711.AudioInfo;
import com.superapp.ftp.FtpInfo;




public class XML {
	
	public final static String Response[]={
		"<negotiate_response>",
		"<login_response>",
		"<md_login_response>",
		"<md_login_ack_response>",
		"<query>",
		"<media>",
		"<ftpinfo_query_response>",
		"<speech_response>",
		"<speech_bye_response>",
		"<speech>",
		"<speech_cancel>",
		"<speech_bye>",
		"<task>",
		
	};
	
	public static enum MSG_TYPE{
	   ERegister_in,
	   ERegister_in_p,
	   Ezhujia,
	   EzhujiaBack,
	   Ezhujia_ack,
	   Ezhujia_ack_res,
	   Efujia,
	   EKeepalive,
	   EMediaQuery,
	   EMedia,
	   EFtpinfo_query,
	   EFtpinfo_query_response,
	   Ehujiao,
	   Ehujiao_response,
	   Egua_duan,
	   Egua_duan_response,
	   Elai_dian,
	   Elai_dian_cancel,
	   ESpeech_cancel,
	   EJie_ting,
	   EDui_fang_gua_duan,
	   ECalled_gua_duan,
	   EGong_dan,
	      
	}
	//XML解析
	public static MSG_TYPE XML_parse(String body,int len){
		int begin=-1;
		int end=-1;
		String tmp=null;	
		
		 if (body.indexOf(Response[0])!=-1)//注册设备第一步
	 	{
			begin=body.indexOf("<seed>");
			end=body.indexOf("</seed>");
			if(begin!=-1&&end!=-1)
			{
				SipInfo.seed=body.substring(begin+6, end);
			}
			
			return MSG_TYPE.ERegister_in;
			
	 	}
	    //register 2
		else if (body.indexOf(Response[1])!=-1)
	 	{
			
	 		begin=body.indexOf("<time>");
			end=body.indexOf("</time>");
			if(begin!=-1&&end!=-1)
			{
				SipInfo.time=body.substring(begin+6, end);
				
			}
			return MSG_TYPE.ERegister_in_p;
	 	}
		else if (body.indexOf(Response[2])!=-1)
	 	{
			
	 		begin=body.indexOf("<name>");
			end=body.indexOf("</name>");
			if(begin!=-1&&end!=-1)
			{
				SipInfo.zhujianame=body.substring(begin+6, end);
				
			}
			SipInfo.zhujiadenglu_state=true;
			return MSG_TYPE.EzhujiaBack;
	 	}
		 
		else if (body.indexOf(Response[3])!=-1)
	 	{
			
	 		begin=body.indexOf("<result>");
			end=body.indexOf("</result>");
			if(begin!=-1&&end!=-1)
			{
				SipInfo.zhujianamequeren=body.substring(begin+8, end);
				System.out.println(SipInfo.zhujianamequeren);
			}
			return MSG_TYPE.Ezhujia_ack_res;
	 	} 
		 
		 
		else if (body.indexOf(Response[4])!=-1)
	 	{
			
	 		begin=body.indexOf("<dev_type>");
			end=body.indexOf("</dev_type>");
			if(begin!=-1&&end!=-1)
			{
				SipInfo.dev_type=body.substring(begin+10, end);
				System.out.println(SipInfo.dev_type);
			}
			return MSG_TYPE.EMediaQuery;
	 	} 
		else if (body.indexOf(Response[5])!=-1)
	 	{
			
	 		
			if(body.indexOf("<peer>")!=-1&&body.indexOf("</peer>")!=-1)
			{
				SipInfo.media_info_peer=body.substring(body.indexOf("<peer>")+6, body.indexOf("</peer>"));
	 			SipInfo.media_info_ip=SipInfo.media_info_peer.substring(0, SipInfo.media_info_peer.indexOf(" "));
	 			SipInfo.media_info_port=SipInfo.media_info_peer.substring(SipInfo.media_info_peer.lastIndexOf(" ")+1);	 
	 			System.out.println("服务器的接收端口为：" + SipInfo.media_info_port);
				SipInfo.serverport =  Integer.parseInt(SipInfo.media_info_port);   //服务器的接收端口号，字符串转为整型
			}
			
			if(body.indexOf("<magic>")!=-1&&body.indexOf("</magic>")!=-1){
	 			String magic=body.substring(body.indexOf("<magic>")+7, body.indexOf("</magic>"));
	 			//字符转换为十六进制
	 			byte[] magic_tmp=new byte[magic.length()];
	 			
	 			int j=0;
	 			for(int i=0;i<32;i++){
	 				if(magic.charAt(i)<='9'){
	 					magic_tmp[i]=(byte)((magic.charAt(i)-48) & 0x0F);
	 				}
	 				else if(magic.charAt(i)<'F'){
	 					magic_tmp[i]=(byte)((magic.charAt(i)-'A'+10) & 0x0F);
	 				}
	 				else{
	 					magic_tmp[i]=(byte)((magic.charAt(i)-'a'+10) & 0x0F);
	 				}	 				
	 			}
	 			for(int i=0;i<16;i++){
	 				magic_tmp[j]=(byte) (magic_tmp[j]<<4);
	 				SipInfo.media_info_magic[i]=(byte) (magic_tmp[j] | magic_tmp[j+1]);
	 				j+=2;
	 			}
	 		}
			return MSG_TYPE.EMedia;
	 	} 
		 
		else if (body.indexOf(Response[6])!=-1)
	 	{
			FtpInfo.ftpinfo_query_respon=true;
			begin=body.indexOf("<result>");
			end=body.indexOf("</result>");
			if(begin!=-1&&end!=-1)
			{
				FtpInfo.result=body.substring(begin+8, end);
				System.out.println(FtpInfo.result);
				 begin=-1;
				 end=-1;
			}
			begin=body.indexOf("<server_ip>");
			end=body.indexOf("</server_ip>");
			if(begin!=-1&&end!=-1)
			{
				FtpInfo.serverip=body.substring(begin+11, end);
				System.out.println(FtpInfo.serverip);
				 begin=-1;
				 end=-1;
			}
			begin=body.indexOf("<username>");
			end=body.indexOf("</username>");
			if(begin!=-1&&end!=-1)
			{
				FtpInfo.usename=body.substring(begin+10, end);
				System.out.println(FtpInfo.usename);
				 begin=-1;
				 end=-1;
			}
			begin=body.indexOf("<pwd>");
			end=body.indexOf("</pwd>");
			if(begin!=-1&&end!=-1)
			{
				FtpInfo.password=body.substring(begin+5, end);
				System.out.println(FtpInfo.password);
				 begin=-1;
				 end=-1;
			}
			
				
			return 	MSG_TYPE.EFtpinfo_query_response;
			}
			
	 	
		else if (body.indexOf(Response[7])!=-1)
	 	{
			
			if(body.indexOf("<code>")!=-1&&body.indexOf("</code>")!=-1)
			{
				begin=body.indexOf("<code>");
				end=body.indexOf("</code>");
				
				AudioInfo.code=body.substring(begin+6, end);
			}
			
			if(body.indexOf("<peer>")!=-1&&body.indexOf("</peer>")!=-1)
			{
				AudioInfo.audio_info_peer=body.substring(body.indexOf("<peer>")+6, body.indexOf("</peer>"));
	 			AudioInfo.audio_info_ip=AudioInfo.audio_info_peer.substring(0, AudioInfo.audio_info_peer.indexOf(" "));
	 			AudioInfo.audio_info_port=AudioInfo.audio_info_peer.substring(AudioInfo.audio_info_peer.lastIndexOf(" ")+1);	 
	 			System.out.println("服务器的接收端口为：" + AudioInfo.audio_info_port);
				AudioInfo.audio_serverport =  Integer.parseInt(AudioInfo.audio_info_port);   //服务器的接收端口号，字符串转为整型
			}
			
			if(body.indexOf("<magic>")!=-1&&body.indexOf("</magic>")!=-1){
				System.out.println("--------------zheli------1");
	 			String magic=body.substring(body.indexOf("<magic>")+7, body.indexOf("</magic>"));
	 			
	 			if(!magic.equals("0")){
	 			
	 			//字符转换为十六进制
	 			byte[] magic_tmp=new byte[magic.length()];
	 			
	 			int j=0;
	 			for(int i=0;i<32;i++){
	 				if(magic.charAt(i)<='9'){
	 					magic_tmp[i]=(byte)((magic.charAt(i)-48) & 0x0F);
	 				}
	 				else if(magic.charAt(i)<'F'){
	 					magic_tmp[i]=(byte)((magic.charAt(i)-'A'+10) & 0x0F);
	 				}
	 				
	 				else{
	 					magic_tmp[i]=(byte)((magic.charAt(i)-'a'+10) & 0x0F);
	 				}	 				
	 			}
	 			
	 			System.out.println("--------------zheli------2");
	 			for(int i=0;i<16;i++){
	 				magic_tmp[j]=(byte) (magic_tmp[j]<<4);
	 				AudioInfo.audio_info_magic[i]=(byte) (magic_tmp[j] | magic_tmp[j+1]);
	 				j+=2;
	 			}
	 		}
	 			else{System.out.println("---------magic-------0");}
			}
			
			
			return MSG_TYPE.Ehujiao_response;
	 	}  
		 
		 
		 
			else if (body.indexOf(Response[8])!=-1)
		 	{
				
		 		
				return MSG_TYPE.Egua_duan_response;
		 	} 
		 
			else if (body.indexOf(Response[9])!=-1)
		 	{
				System.out.println("laidian-------------0");
				
				if(body.indexOf("<from_number>")!=-1&&body.indexOf("</from_number>")!=-1)
				{
					AudioInfo.CalledFromNumber=body.substring(body.indexOf("<from_number>")+13, body.indexOf("</from_number>"));
					System.out.println("laidian-------------1");
		 			System.out.println("来电号码为：" + AudioInfo.CalledFromNumber);
					
				}
				
				
				
				
				
				if(body.indexOf("<peer>")!=-1&&body.indexOf("</peer>")!=-1)
				{
					AudioInfo.audio_info_peer=body.substring(body.indexOf("<peer>")+6, body.indexOf("</peer>"));
		 			AudioInfo.audio_info_ip=AudioInfo.audio_info_peer.substring(0, AudioInfo.audio_info_peer.indexOf(" "));
		 			AudioInfo.audio_info_port=AudioInfo.audio_info_peer.substring(AudioInfo.audio_info_peer.lastIndexOf(" ")+1);	 
		 			System.out.println("服务器的接收端口为：" + AudioInfo.audio_info_port);
					AudioInfo.audio_serverport =  Integer.parseInt(AudioInfo.audio_info_port);   //服务器的接收端口号，字符串转为整型
				}
				
				if(body.indexOf("<magic>")!=-1&&body.indexOf("</magic>")!=-1){
					System.out.println("--------------zheli------1");
		 			String magic=body.substring(body.indexOf("<magic>")+7, body.indexOf("</magic>"));
		 			
		 			if(!magic.equals("0")){
		 			
		 			//字符转换为十六进制
		 			byte[] magic_tmp=new byte[magic.length()];
		 			
		 			int j=0;
		 			for(int i=0;i<32;i++){
		 				if(magic.charAt(i)<='9'){
		 					magic_tmp[i]=(byte)((magic.charAt(i)-48) & 0x0F);
		 				}
		 				else if(magic.charAt(i)<'F'){
		 					magic_tmp[i]=(byte)((magic.charAt(i)-'A'+10) & 0x0F);
		 				}
		 				
		 				else{
		 					magic_tmp[i]=(byte)((magic.charAt(i)-'a'+10) & 0x0F);
		 				}	 				
		 			}
		 			
		 			System.out.println("--------------zheli------2");
		 			for(int i=0;i<16;i++){
		 				magic_tmp[j]=(byte) (magic_tmp[j]<<4);
		 				AudioInfo.audio_info_magic[i]=(byte) (magic_tmp[j] | magic_tmp[j+1]);
		 				j+=2;
		 			}
		 		}
		 			else{System.out.println("---------magic-------0");}
				} 		
				return MSG_TYPE.Elai_dian;
		 	} 
		 
			else if (body.indexOf(Response[10])!=-1)
		 	{
				
		 		
				return MSG_TYPE.Elai_dian_cancel;
		 	} 
		 
			else if (body.indexOf(Response[11])!=-1)
		 	{
				
		 		
				return MSG_TYPE.EDui_fang_gua_duan;
		 	} 
		 
			else if (body.indexOf(Response[12])!=-1)
		 	{
				
		 		
				return MSG_TYPE.EGong_dan;
		 	} 
		 
		 
		 
		
		return null;
	
	}
	
	
	public static String Create_XML_Ezhujia()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
				"<md_login><worker_id></worker_id><type_flag></type_flag></md_login>\r\n");
		body.insert(body.indexOf("<worker_id>")+11, SipInfo.zhujianumber);
		body.insert(body.indexOf("<type_flag>")+11, SipInfo.loginmeans);
		
		return body.toString();
	}
	public static String Create_XML_Ezhujia_ack()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
				"<md_login_ack><worker_id></worker_id><type_flag></type_flag></md_login_ack>\r\n");
		body.insert(body.indexOf("<worker_id>")+11, SipInfo.zhujianumber);
		body.insert(body.indexOf("<type_flag>")+11, SipInfo.loginmeans);
		
		return body.toString();
	}
	
	public static String Create_XML_Keepalive()//心跳包
	{
		String body="<?xml version=\"1.0\"?>\r\n<heartbeat_request></heartbeat_request>\r\n";
		return body;
		
	}
	public static String Create_XML_Register()//第二步注册包
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<login_request><password></password></login_request>\r\n");
		body.insert(body.indexOf("<password>")+10, SipInfo.seed);

		return body.toString();
	}
	
	public static String Create_XML_QueryMediaResponse()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<query_response><variable>MediaInfo_Video</variable><result>0</result><video>H.264</video><resolution>CIF</resolution><framerate>25</framerate><bitrate>256</bitrate><bright>51</bright><contrast>49</contrast><saturation>50</saturation></query_response>\r\n");
		

		return body.toString();
	}
	
	
	public static String Create_XML_RequestMediaResponse()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<media>\r\n<resolution>QCIF_MOBILE_SOFT</resolution>\r\n<video>H.264</video>\r\n<audio>G.722</audio>\r\n<kbps>800</kbps>\r\n<self>192.168.1.6  UDP  5000</self>\r\n<mode>active</mode>\r\n<magic>01234567890123456789012345678901</magic>\r\n</media>\r\n");
		

		return body.toString();
	}
	
	public static String Create_XML_Ftpinfo_query()
	{
		String body="<?xml version=\"1.0\"?>\r\n<ftpinfo_query></ftpinfo_query>\r\n";
		return body;
	}
		
	public static String Create_XML_yuyinhujiao_query()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<speech>\r\n<audio>G.711</audio>\r\n<kbps>8000</kbps>\r\n<self>192.168.1.112 UDP 5200</self>\r\n<from_number></from_number>\r\n<to_number></to_number>\r\n</speech>\r\n");
		
		body.insert(body.indexOf("<from_number>")+13, AudioInfo.CallingFromNumber);
		body.insert(body.indexOf("<to_number>")+11, AudioInfo.CallingToNumber);
		
		return body.toString();
	}
		
	public static String Create_XML_Egua_duan()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<speech_bye>\r\n<from_number></from_number>\r\n<to_number></to_number>\r\n</speech_bye>\r\n");
		body.insert(body.indexOf("<from_number>")+13, AudioInfo.CallingFromNumber);
		body.insert(body.indexOf("<to_number>")+11, AudioInfo.CallingToNumber);
		
		return body.toString();
	}
	
	
	
	public static String Create_XML_ECalled_gua_duan()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<speech_bye>\r\n<from_number></from_number>\r\n<to_number></to_number>\r\n</speech_bye>\r\n");
		body.insert(body.indexOf("<from_number>")+13, AudioInfo.CalledFromNumber);
		body.insert(body.indexOf("<to_number>")+11, AudioInfo.CalledToNumber);
		
		return body.toString();
	}
	
	
	public static String Create_XML_ESpeech_cancel()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<speech_cancel>\r\n<from_number></from_number>\r\n<to_number></to_number>\r\n</speech_cancel>\r\n");
		body.insert(body.indexOf("<from_number>")+13, AudioInfo.CallingFromNumber);
		body.insert(body.indexOf("<to_number>")+11, AudioInfo.CallingToNumber);
		
		
		
		return body.toString();
	}
	
	public static String Create_XML_AudioResponse_Error()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<speech_response>\r\n<code>486</code>\r\n<audio>G.711</audio>\r\n<kbps>8000</kbps>\r\n<self>192.168.1.112 UDP 5200</self>\r\n<from_number></from_number>\r\n<to_number></to_number>\r\n</speech_response>\r\n");
		
		body.insert(body.indexOf("<from_number>")+13, AudioInfo.CalledFromNumber);
		body.insert(body.indexOf("<to_number>")+11, AudioInfo.CalledToNumber);
		
		return body.toString();
	}
	
	public static String Create_XML_AudioResponse_OK()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<speech_response>\r\n<code>200</code>\r\n<audio>G.711</audio>\r\n<kbps>8000</kbps>\r\n<self>192.168.1.112 UDP 5200</self>\r\n<from_number></from_number>\r\n<to_number></to_number>\r\n</speech_response>\r\n");
		
		body.insert(body.indexOf("<from_number>")+13, AudioInfo.CalledFromNumber);
		body.insert(body.indexOf("<to_number>")+11, AudioInfo.CalledToNumber);
		
		return body.toString();
	}
	
	
	
	
	
	public static String Create_XML_GongDanResponse_OK()
	{
		StringBuffer body=new StringBuffer("<?xml version=\"1.0\"?>\r\n<task_response>\r\n<result>0</result>\r\n<file_name></file_name>\r\n</task_response>\r\n");
		
		
		return body.toString();
	}
	
	
	
	
	
	
	
}
