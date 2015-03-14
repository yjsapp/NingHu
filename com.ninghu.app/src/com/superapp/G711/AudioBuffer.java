package com.superapp.G711;

import android.util.Log;

class AudioBuffer {
	private short[] audioBuf;
	private boolean isReadable;
	private boolean isWriteable;
	private int audioLen;
	
	AudioBuffer(){
		audioBuf=new short[160*5];
		isReadable=false;
		isWriteable=true;
		audioLen=0;
	}
	
	public void Clean() {
		isReadable=false;
		isWriteable=true;
		audioLen=0;
	}	
	
	public boolean IsEmpty() {
		if(audioLen > 0)
			return false;
		else
			return true;
	}
	
	public synchronized void IsReadAble()
	{	
		if(!isReadable)
		{
			try
			{
				this.wait();
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	public synchronized void ReadLock(){
		isReadable=false;
		isWriteable=true;
		notify();
	}
	public synchronized short[] Read()
	{
		IsReadAble();
		
		short[] tmp_NalBuf = new short[this.audioLen];
		System.arraycopy(this.audioBuf, 0, tmp_NalBuf, 0, this.audioLen); //to make sure useable info copyed
		Log.v("zlj","read");
		
		this.audioLen = 0;
		ReadLock();
		
    	return tmp_NalBuf;	
	}
	
	
	public synchronized void IsWriteAble()
	{	
		if(!isWriteable)
		{
			try
			{
				this.wait();
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	public synchronized void WriteLock() {
		isReadable=true;
		isWriteable=false;
		notify();
	}
	public synchronized void Write(short[] data, int dataLen)
	{	
		IsWriteAble();
		
		System.arraycopy(data, 0, this.audioBuf, 0, dataLen);
		audioLen += dataLen;
		Log.v("zlj","write");
		
		WriteLock();
	}
}