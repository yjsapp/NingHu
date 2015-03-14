package com.superapp.main;


class StreamBufNode{
	public byte[] m_pData;   //收到的RTP包数据
	public StreamBufNode m_pNext;  //StreamBufNode类型的指向标，指向下一个StreamBufNode节点
	StreamBufNode(){
		m_pData=null;
		m_pNext=null;
	}

	StreamBufNode(byte[] pData){
		if(pData!=null){
			m_pData=pData;
		}
	}
	
	byte[] GetData(){
		return m_pData;
	}
	int GetLen(){
		return m_pData.length;
	}

	StreamBufNode GetNext(){
		return m_pNext;
	}
}
/**
 * 建立RTP缓存，每个缓存存一个RTP包，实现对接收到的RTP包进行排序功能，当缓存中接收到超过5个RTP包时，
 *  把m_bReady置为true，进行组包。
 */
class StreamBuf{
	private int   m_nLen;            //整个缓存的长度
	private int   m_nCurLen;         //缓存中已有RTP包的缓存的个数
	private int   m_nReadyLen;       //开始组包的最小RTP的个数
	private boolean  m_bReady;       //标志位，为true时开始调用GetNal函数
	private StreamBufNode m_pHead;   //首部节点
	private StreamBufNode m_pTail;   //尾部节点

	
	StreamBuf ( int nLen,  int nReadyLen){
	    m_nLen = nLen;
	    m_nCurLen = 0;
	    m_nReadyLen = nReadyLen;
	    m_pHead = null;
	    m_pTail = null;
	    m_bReady = false;
	}
	
	/**从链表的头部读取一个RTP包数据缓存*/
	StreamBufNode GetFromBuf(){
		if(IsEmpty()){
			return null;
		}
		StreamBufNode pNode = m_pHead;
		m_pHead = m_pHead.m_pNext;
		m_nCurLen --;
		if(m_nCurLen < m_nReadyLen){
			m_bReady = false; 
		}
		return pNode;
	}
	
	
	boolean IsEmpty(){
		return m_nCurLen == 0;
	}
	boolean IsReady(){
		return m_bReady == true;
	}
	int GetCurLen(){
		return m_nCurLen;
	}

	void SetReadyLen(int nDelay){
		if(nDelay > 0){
			if(nDelay > m_nReadyLen && m_bReady)
				m_bReady = false;
			m_nReadyLen = nDelay;
			while(m_nCurLen >= m_nReadyLen){
				StreamBufNode pNode = m_pHead;
				if(m_pHead!=null)
					m_pHead = m_pHead.GetNext();
				m_nCurLen --;
			}
		}
	}
	
	void ClearBuf(){
		StreamBufNode pNode = m_pHead;
		if(m_pHead!=null)
			m_pHead = m_pHead.m_pNext;
		while(pNode!=null){
			m_nCurLen--;
			pNode = m_pHead;
			if(m_pHead!=null)
				m_pHead = m_pHead.m_pNext;
		}
		if(m_nCurLen!=0){
			m_nCurLen = 0;
		}
			
		m_nCurLen = 0;
		m_pHead = m_pTail = null;
		m_bReady = false;
	}
	
	boolean AddToBuf(StreamBufNode pNode){
		//若当前缓存RTP包个数超过缓存长度，则清空缓存，然后放入最新打包的RTP包
		if(m_nCurLen  > m_nLen){
			ClearBuf();
			m_pHead = m_pTail = pNode;
			m_nCurLen ++;
			m_bReady = false;
		}
		//若缓存为空，则放入的节点既为头节点，也是尾部节点；若不为空，则插入链表尾部
		if(IsEmpty()){
			m_pHead = m_pTail = pNode;
		}
		else{
			m_pTail.m_pNext = pNode;
			m_pTail = pNode;
		}
		m_nCurLen ++;
		return true;
	}
}
