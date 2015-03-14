package com.superapp.sip;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaEncrypt {
	
	
	
	public ShaEncrypt() {}
    
	public String Encrypt(String strSrc) {
                //parameter strSrc is a string will be encrypted,
                //parameter encName is the algorithm name will be used.
                //encName dafault to "MD5"
			MessageDigest md=null;
			String strDes=null;

			byte[] bt=strSrc.getBytes();
			try {
                        //if (encName==null||encName.equals("")) {
                        //    encName="MD5";
                        //}
				md=MessageDigest.getInstance("SHA-1");
				md.update(bt);
                strDes=bytes2Hex(md.digest());  //to HexString
			}
			catch (NoSuchAlgorithmException e) {
				System.out.println("Invalid algorithm.");
				return null;
			}
                return strDes;
	}

        public String bytes2Hex(byte[]bts) {
         String des="";
         String tmp=null;
         for (int i=0;i<bts.length;i++) {
                    tmp=(Integer.toHexString(bts[i] & 0xFF));
                    if (tmp.length()==1) {
                        des+="0";
                    }
                    des+=tmp;
                }
                return des;
        }             

}
