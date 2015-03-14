package com.superapp.info;

public class Info {
	
	
	
	public static String serverid="101.69.255.130";
	
	
	public static boolean IsFirstCast=false;

	
	/**每一个新的NAL设置首包打包状态为false，即没有打包首包*/
	public static boolean firstPktReceived = false;  
	/**记录打包分片的索引*/
	public static int pktflag = 0; 
	/**若未打包到末包，则此状态一直为true*/
	public static boolean status = true;   
	/**打包分片长度*/
	public static int divide_length = 800;
	/**分片标志位*/
	public static boolean dividingFrame = false;

}
