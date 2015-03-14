package com.superapp.sip;

import java.util.Hashtable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zoolu.net.SocketAddress;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.CSeqHeader;
import org.zoolu.sip.header.CallIdHeader;
import org.zoolu.sip.header.ContactHeader;
import org.zoolu.sip.header.ExpiresHeader;
import org.zoolu.sip.header.FromHeader;
import org.zoolu.sip.header.MaxForwardsHeader;
import org.zoolu.sip.header.MultipleHeader;
import org.zoolu.sip.header.RequestLine;
import org.zoolu.sip.header.SipHeaders;
import org.zoolu.sip.header.ToHeader;
import org.zoolu.sip.header.UserAgentHeader;
import org.zoolu.sip.header.ViaHeader;
import org.zoolu.sip.message.BaseMessageFactory;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.tools.HashSet;
import org.zoolu.tools.Log;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.superapp.G711.AudioInfo;
import com.superapp.info.Gongdan;
import com.superapp.sip.XML.MSG_TYPE;

public class Sip extends SipProvider 
{
	
	//IpAddress.getLocalHostAddress().toString()
	
	private Random port = new Random();
	private Context context;
	
	
	/** Whether using UDP as transport protocol */
	   static boolean transport_udp=false;
	   /** Whether using TCP as transport protocol */
	   static boolean transport_tcp=false;
	   /** Whether adding 'rport' parameter on outgoing requests. */
	   static boolean rport=true;
	   
	   /** Whether forcing 'rport' parameter on incoming requests ('force-rport' mode). */
	   static boolean force_rport=false;

	   /** List of provider listeners */
	   static Hashtable listeners=null;
	   
	   /** List of exception listeners */
	   static HashSet exception_listeners=null;
	   /** Connections */
	   static Hashtable connections=null;
	   /** Max number of (contemporary) open connections */
	   static int nmax_connections=0;
	   /** Outbound proxy (host_addr[:host_port]).
	     * Use 'NONE' for not using an outbound proxy (or let it undefined). */
	   static SocketAddress outbound_proxy=null;
	   /** Outbound proxy addr (for backward compatibility). */
	   private static String outbound_addr=null;
	   /** Outbound proxy port (for backward compatibility). */
	   private static int outbound_port=-1;
	   /** UDP protocol type */
	   public static final String PROTO_UDP="udp";
	   /** TCP protocol type */
	   public static final String PROTO_TCP="tcp";
	   /** String value "auto-configuration" used for auto configuration of the host address. */
	   public static final String AUTO_CONFIGURATION="AUTO-CONFIGURATION";

	   /** String value "auto-configuration" used for auto configuration of the host address. */
	   public static final String ALL_INTERFACES="ALL-INTERFACES";
	   /** Event Loger */
	   protected static Log event_log=null;

	   /** Message Loger */
	   protected static Log message_log=null;
	   
	public Sip(String via_addr, int port, String[] protocols, String ifaddr) {
		super(via_addr, port, protocols, ifaddr);
		// TODO Auto-generated constructor stub
	}
	
	//To,From头
	public static NameAddress SetFromTo(String displayname,String userid, String hostname,int portnumber)
	{	
		SipURL sipurl = new SipURL(userid, hostname, portnumber);//
		NameAddress FromTo = new NameAddress(displayname, sipurl);
		return FromTo;
	}
	/**创建Sip消息*/
	public static Message CreateMessage(SipProvider sip_provider,MSG_TYPE msg_type, NameAddress to, NameAddress from)
	{
		String method=null;
		String body = null;
		switch(msg_type)
		{
	    	
	    	case ERegister_in: 
	    		method=SipMethods.REGISTER;
	    		break;
	    	case ERegister_in_p:
	    		method=SipMethods.REGISTER;
	    		body = XML.Create_XML_Register();
	    		break;
	    	case EKeepalive:
	    		method=SipMethods.REGISTER;
	    		body = XML.Create_XML_Keepalive();
	    		break;
	    	case Ezhujia:
	    		method=SipMethods.NOTIFY;
	    		body = XML.Create_XML_Ezhujia();
	    		break;
	    	case Ezhujia_ack:
	    		method=SipMethods.NOTIFY;
	    		body = XML.Create_XML_Ezhujia_ack();
	    		break;	
	    	case EFtpinfo_query:
	    		method=SipMethods.NOTIFY;
	    		body = XML.Create_XML_Ftpinfo_query();
	    		break;	
	    	case Ehujiao:
	    		method=SipMethods.INVITE;
	    		body = XML.Create_XML_yuyinhujiao_query();
	    		break;	
	    	case Egua_duan:	
	    		method=SipMethods.BYE;
	    		body = XML.Create_XML_Egua_duan();
	    		break;
	    	case ESpeech_cancel:	
	    		method=SipMethods.NOTIFY;
	    		body = XML.Create_XML_ESpeech_cancel();
	    		break;
	    	case ECalled_gua_duan:	
	    		method=SipMethods.BYE;
	    		body = XML.Create_XML_ECalled_gua_duan();
	    		break;
	    		
	    	
	    	default:
	    		return null;	    		
		}
		Message msg=new Message();
		//String contact_user=from.getAddress().getUserName();
		SipURL request_uri=to.getAddress();
		String Hostname = request_uri.getHost();
		int Hostport = request_uri.getPort();
		SipURL msg_uri = new SipURL(Hostname,Hostport);
		
	    NameAddress contact=new NameAddress(new SipURL(sip_provider.getViaAddress(),sip_provider.getPort()));//修改
	    String call_id=sip_provider.pickCallId();
	    int cseq=SipProvider.pickInitialCSeq();
	    String local_tag=SipProvider.pickTag();
	    String via_addr=sip_provider.getViaAddress();
	    int host_port=sip_provider.getPort();
	   // boolean rport=sip_provider.isRportSet();
	    String proto;
	    if (request_uri.hasTransport()) proto=request_uri.getTransport();
	    	else proto=sip_provider.getDefaultTransport();
	   
	      //mandatory headers first (To, From, Via, Max-Forwards, Call-ID, CSeq):
	      msg.setRequestLine(new RequestLine(method,msg_uri));
	      ViaHeader via=new ViaHeader(proto,via_addr,host_port);
	      if (rport) via.setRport();
	      String branch=SipProvider.pickBranch();
	      via.setBranch(branch);
	      msg.addViaHeader(via);
	      msg.setMaxForwardsHeader(new MaxForwardsHeader(70));
	      msg.setFromHeader(new FromHeader(from,local_tag));
	     // if (remote_tag==null) 
	      msg.setToHeader(new ToHeader(to));
	       //  else req.setToHeader(new ToHeader(to,remote_tag));
	      
	      msg.setCallIdHeader(new CallIdHeader(call_id));
	      msg.setCSeqHeader(new CSeqHeader(cseq,method));
	      //optional headers:
	      msg.setExpiresHeader(new ExpiresHeader(String.valueOf(SipStack.default_expires)));
	      if (contact!=null){  
	    	  MultipleHeader contacts=new MultipleHeader(SipHeaders.Contact);
	    	  contacts.addBottom(new ContactHeader(contact));
	    	  //System.out.println("DEBUG: Contact: "+contact.toString());
	    	  msg.setContacts(contacts);
	      }
	      
	      // add User-Agent header field
	      if (SipStack.ua_info!=null) 
	    	  msg.setUserAgentHeader(new UserAgentHeader(SipStack.ua_info));
	      msg.setBody(body);
	      return msg;
	}
		 
	//接收到消息回调
	protected void processReceivedMessage(Message msg) {
		// TODO Auto-generated method stub
		//BaseMessage.message=msg.toString();
		String body = msg.getBody();
		System.out.println(body);
	
		MSG_TYPE k=null;
		int point=-1;
		//NameAddress contact=new NameAddress(new SipURL(Info.sip_provider.getViaAddress(),Info.sip_provider.getPort()));
				
		   
		if (msg.isResponse())//是否为响应消息
	    {  
			int code=msg.getStatusLine().getCode();
			String status=msg.getStatusLine().getReason();
			if(code==100)
			{
				return;
				
			}
			else if(code==200)
			{
				
				for(int i=0;i<XML.Response.length;i++)
				   {
					   point = body.indexOf(XML.Response[i]);
					   if(point!=-1)
					   { break;}
					   
				   }	
					if(point == -1){
						return;
					}
				k=XML.XML_parse(body,body.length());
			}
			else if(code==401)
			{
				if(status.compareTo("Unauthorized")==0){
					//Info.login_info_incorrect=true;
				}
				//Info.reg_success=false;
				//Info.timeout=false;
				return;
			}
//			else if(code==408&&!Info.reg_state)
//			{
//				//Info.reg_success=false;
//				//Info.timeout=true;
//				return;
//			}
			else if(code==409||code==500||code==408)
			{
				return;
			}
			switch(k)
			{
			case ERegister_in:
			   {
			   //第一步注册   种子加密
				   SipInfo.first_register_step_succeccful = true ;				
				SipInfo.sip_provider.sendMessage(CreateMessage(SipInfo.sip_provider,MSG_TYPE.ERegister_in_p, SipInfo.To, SipInfo.From));
				System.out.println("<-------注册第一步完成--------->");
			   }
			   break;
		   case ERegister_in_p:
			   
		   if(!SipInfo.reg_state)
			   {
				  //第二步注册
				  SipInfo.reg_success=true;
				  SipInfo.timeout=false;
				  SipInfo.reg_state=true;	
				  
				  
				  SipInfo.keepalive.startThread();
		 System.out.println("<-------注册已完成--------->");
		 
		 
		
		 
				 
	   }
			   
			   break;
		   case EzhujiaBack:
			 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
			   System.out.println("<-------登录确认--------->");
			   
			   break;
		  
		   case Ehujiao_response:
				 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
				   System.out.println("<-------呼叫反馈--------->");
				   
				   
				   if(AudioInfo.code.equals("200")){
				   AudioInfo.IsSpeechResponse=true;
				   
				   android.os.Message AudioG711=new android.os.Message();	//创建handler消息
			        AudioG711.arg1=1;
					AudioInfo.G711.sendMessage(AudioG711);	
				   }
				   
				   else{
					   android.os.Message AudioG711Error=new android.os.Message();	//创建handler消息
				       AudioG711Error.arg1=1;
					AudioInfo.G711HujiaoError.sendMessage(AudioG711Error);
					   
					   
				   }
				   
				   
				   
				   break; 
		  
			  // android.util.Log.e("result","现在是0");
		   case Egua_duan_response:
				 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
				   System.out.println("<-------呼叫已经挂断    calling  主叫方挂断--------->");
				  
				   
				   
				   AudioInfo.IsGuaDuanResponse=true;
				   break; 
				    
			   
		   default:
			   
			   
			   System.out.println("shi  xiang ying"); 
			   break;
		   }    
		}
		else
		{
			k=XML.XML_parse(body,body.length());
			
			if(k!=null){
			
		
			switch(k)
			   {
			   case EMediaQuery:
				   
				   System.out.println("<-------媒体邀请here--------->");
				SipInfo.toh = msg.getToHeader();      //重新获取To
				SipInfo.fromh = msg.getFromHeader();  //重新获取From
					//匹配FROM中的":6061"字符串，用空字符串替换
					Pattern p_from = Pattern.compile(":6061");
					Matcher m_from = p_from.matcher(SipInfo.fromh.getValue());
					String from_value = m_from.replaceAll("");
				
					SipInfo.fromh.setValue(from_value);
					
			   
					//匹配TO中的":6061"字符串，用空字符串替换
					Pattern p_to = Pattern.compile(":6061");
					Matcher m_to = p_to.matcher(SipInfo.toh.getValue());
					String to_value = m_to.replaceAll("");
				
					SipInfo.toh.setValue(to_value);
					
					
					ViaHeader vh=msg.getViaHeader();
					boolean via_changed=false;
					String src_addr=msg.getRemoteAddress();
					int src_port=msg.getRemotePort();
					
					String via_addr=vh.getHost();
					int via_port=vh.getPort();
				
					if (via_port<=0) via_port=SipStack.default_port;
					 
					if (!via_addr.equals(src_addr)){  
						vh.setReceived(src_addr);
					   via_changed=true;
					}
					
					if (vh.hasRport()){  
						vh.setRport(src_port);
					   via_changed=true;
					}
					else{ 
						if (force_rport && via_port!=src_port){ 
							vh.setRport(src_port);
					      via_changed=true;
					   }
					}
				
					SipInfo.viash = vh;   //设置新的via
                    SipInfo.query_response = true;
		     		SipInfo.QueryMediaResponseXML = XML.Create_XML_QueryMediaResponse();
					Message response2=BaseMessageFactory.createResponse(msg, 200, "OK", null);
					SipInfo.sip_provider.sendMessage(response2);
				   
				   
				 
				   break;
			   case EMedia:
				   
				  System.out.println("media"); 
				   if(SipInfo.query_response){
					   SipInfo.query_response = false;
				   SipInfo.request_media = true;
					SipInfo.RequestMediaResponseXML = XML.Create_XML_RequestMediaResponse();
					Message response3=BaseMessageFactory.createResponse(msg, 200, "OK", null);
					SipInfo.sip_provider.sendMessage(response3);
					 System.out.println("<-------视频邀请第二步OK--------->");
					 
				
//					 Intent intent=new Intent();
//	    			 intent.setAction("com.superapp.BroadcastReceiver.video");
//	    			 context.sendBroadcast(intent);
	    			
	    			System.out.println("发出视频邀请handler");	 
					 android.os.Message notofy_media=new android.os.Message();	//创建handler消息
					 notofy_media.arg1=1;
					SipInfo.notifymedia.sendMessage(notofy_media);
					 
					 
				   }
					 
					 
					 
					 
				   break;
//			   case EResponse1:
//					 //  StatusLine sl= new StatusLine(200,"OK");
//				   	//Info.login_replace = true;
//					   Message response2=BaseMessageFactory.createResponse(msg, 200, "OK", null);
//					   //response.setStatusLine(sl);
//					  // Info.sip_provider.sendMessage(response2);
//
//
//					   android.os.Message notimsg=new android.os.Message();	//创建handler消息
//						notimsg.arg1=1;
//						//Info.loginHandler.sendMessage(notimsg);
//
//						break;
//			   
				   
				   
			   case Ehujiao_response:
					 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
					   System.out.println("<-------hujiaojieshoudao l --------->");
					   
					   break;
					   
			   case Elai_dian:
					 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
					   System.out.println("<-------来电 l --------->");
					
						AudioInfo.toh = msg.getToHeader();      //重新获取To
						AudioInfo.fromh = msg.getFromHeader();  //重新获取From
							//匹配FROM中的":6061"字符串，用空字符串替换
							Pattern p_from_audio = Pattern.compile(":6061");
							Matcher m_from_audio = p_from_audio.matcher(AudioInfo.fromh.getValue());
							String from_value_audio = m_from_audio.replaceAll("");
						
							AudioInfo.fromh.setValue(from_value_audio);
							
					   
							//匹配TO中的":6061"字符串，用空字符串替换
							Pattern p_to_audio = Pattern.compile(":6061");
							Matcher m_to_audio = p_to_audio.matcher(AudioInfo.toh.getValue());
							String to_value_audio = m_to_audio.replaceAll("");
						
							AudioInfo.toh.setValue(to_value_audio);
							
							
							ViaHeader vh_audio=msg.getViaHeader();
							boolean via_changed_audio=false;
							String src_addr_audio=msg.getRemoteAddress();
							int src_port_audio=msg.getRemotePort();
							
							String via_addr_audio=vh_audio.getHost();
							int via_port_audio=vh_audio.getPort();
						
							if (via_port_audio<=0) via_port_audio=SipStack.default_port;
							 
							if (!via_addr_audio.equals(src_addr_audio)){  
								vh_audio.setReceived(src_addr_audio);
								via_changed_audio=true;
							}
							
							if (vh_audio.hasRport()){  
								vh_audio.setRport(src_port_audio);
								via_changed_audio=true;
							}
							else{ 
								if (force_rport && via_port_audio!=src_port_audio){ 
									vh_audio.setRport(src_port_audio);
									via_changed_audio=true;
							   }
							}
						
							AudioInfo.viash = vh_audio;   //设置新的via
					   
					   AudioInfo.message=msg;
					   
					   
							
					   if(AudioInfo.IsTalking){
						
						
						
						//回复占线状态的 消息
						   AudioInfo.AudioResponse_Error= XML.Create_XML_AudioResponse_Error();
							Message calledresponse=BaseMessageFactory.createResponse1(msg, 200, "OK", null,"Error");
							SipInfo.sip_provider.sendMessage(calledresponse);
						
					}   
					else
					
					
					
					{
				 android.os.Message LaiDian=new android.os.Message();	//创建handler消息
						 LaiDian.arg1=1;
						AudioInfo.Called.sendMessage(LaiDian);
						
					
					
					}
					   
					   
					   break;		   
					   
			   case Elai_dian_cancel:
					 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
					   System.out.println("<-------来电方 未接通 主动挂断 --------->");
					   
					   break;		   
					   
			   case EDui_fang_gua_duan:
					 //  SipInfo.sip_provider.sendMessage(Sip.CreateMessage(SipInfo.sip_provider,MSG_TYPE.Ezhujia_ack, SipInfo.To, SipInfo.From)); 
					   System.out.println("<-------来电方 接通 主动挂断 --------->");
					   
					
					   
					   android.os.Message duifangjietongguaduan=new android.os.Message();	//创建handler消息
					   duifangjietongguaduan.arg1=1;
						AudioInfo.G711DuiFangJieTongGuaDuan.sendMessage(duifangjietongguaduan);
					   break;
					   
					   
			   case EGong_dan:
				   
				   Gongdan.toh = msg.getToHeader();      //重新获取To
				   Gongdan.fromh = msg.getFromHeader();  //重新获取From
						//匹配FROM中的":6061"字符串，用空字符串替换
						Pattern p_from_Gongdan = Pattern.compile(":6061");
						Matcher m_from_Gongdan = p_from_Gongdan.matcher(Gongdan.fromh.getValue());
						String from_value_Gongdan = m_from_Gongdan.replaceAll("");
					
						Gongdan.fromh.setValue(from_value_Gongdan);
						
				   
						//匹配TO中的":6061"字符串，用空字符串替换
						Pattern p_to_Gongdan = Pattern.compile(":6061");
						Matcher m_to_Gongdan = p_to_Gongdan.matcher(Gongdan.toh.getValue());
						String to_value_Gongdan = m_to_Gongdan.replaceAll("");
					
						Gongdan.toh.setValue(to_value_Gongdan);
						
						
						ViaHeader vh_Gongdan=msg.getViaHeader();
						boolean via_changed_Gongdan=false;
						String src_addr_Gongdan=msg.getRemoteAddress();
						int src_port_Gongdan=msg.getRemotePort();
						
						String via_addr_Gongdan=vh_Gongdan.getHost();
						int via_port_Gongdan=vh_Gongdan.getPort();
					
						if (via_port_Gongdan<=0) via_port_Gongdan=SipStack.default_port;
						 
						if (!via_addr_Gongdan.equals(src_addr_Gongdan)){  
							vh_Gongdan.setReceived(src_addr_Gongdan);
							via_changed_Gongdan=true;
						}
						
						if (vh_Gongdan.hasRport()){  
							vh_Gongdan.setRport(src_port_Gongdan);
							via_changed_Gongdan=true;
						}
						else{ 
							if (force_rport && via_port_Gongdan!=src_port_Gongdan){ 
								vh_Gongdan.setRport(src_port_Gongdan);
								via_changed_Gongdan=true;
						   }
						}
					
						Gongdan.viash = vh_Gongdan;   //设置新的via
				   
				   
				   
						Gongdan.GongDanResponse_OK= XML.Create_XML_GongDanResponse_OK();
						Message gongdanresponse=BaseMessageFactory.createResponse2(msg, 200, "OK", null);
						SipInfo.sip_provider.sendMessage(gongdanresponse);
				   
				   
				   
				   
				   
				   android.os.Message gongdan=new android.os.Message();	//创建handler消息
				   gongdan.arg1=1;
				   Bundle b = new Bundle();
				   b.putString("GongDan", body);
				   gongdan.setData(b);
					SipInfo.xingongdan.sendMessage(gongdan);
				   
				   System.out.println("工单收到了  ");
				   break;
					   
					   
			   default:
				   
				   
				   
				   System.out.println("shi  qita  ");
			   break; 
			   }
			}
			//消息体为空时  调用如下：
			
			
			
			else{
				
				if(msg.isAck()){
				
			System.out.println("ack  shoudaol  ");}
				
				
				
				else if(msg.isBye()){  //若发来的request为BYE
					System.out.println("结束发送视频！");

					SipInfo.endView  = true;   //结束视频发送标志位
				}
			
				else{
				System.out.println("qita    ");}
			}		
			
			
			
		}
		return;
	}
}