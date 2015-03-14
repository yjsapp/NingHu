/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright 漏 2010 France Telecom S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.opencore.avch264;


public class NativeH264Encoder{

    public NativeH264Encoder(){
    }

    public static native int InitEncoder(int i, int j, int k);

    public static native byte[] EncodeFrame(byte abyte0[], long l);

    public static native int DeinitEncoder();

    //encode success or fail
    public static native int getLastEncodeStatus();   //取得最后的编码状态，判断压缩成功还是失败
    
    //if keyframe return 0x64 
    // if p frame return 0x66
    // or return -1;
    public static native int getKeyFrame();

    static {
        String libname = "H264Encoder";
        try{
            System.loadLibrary(libname);
        }
        catch(UnsatisfiedLinkError unsatisfiedlinkerror) { 
        	
        }
    }
}
