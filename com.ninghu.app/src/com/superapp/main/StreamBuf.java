package com.superapp.main;


class StreamBufNode{
	public byte[] m_pData;   //�յ���RTP������
	public StreamBufNode m_pNext;  //StreamBufNode���͵�ָ��ָ꣬����һ��StreamBufNode�ڵ�
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
 * ����RTP���棬ÿ�������һ��RTP����ʵ�ֶԽ��յ���RTP�����������ܣ��������н��յ�����5��RTP��ʱ��
 *  ��m_bReady��Ϊtrue�����������
 */
class StreamBuf{
	private int   m_nLen;            //��������ĳ���
	private int   m_nCurLen;         //����������RTP���Ļ���ĸ���
	private int   m_nReadyLen;       //��ʼ�������СRTP�ĸ���
	private boolean  m_bReady;       //��־λ��Ϊtrueʱ��ʼ����GetNal����
	private StreamBufNode m_pHead;   //�ײ��ڵ�
	private StreamBufNode m_pTail;   //β���ڵ�

	
	StreamBuf ( int nLen,  int nReadyLen){
	    m_nLen = nLen;
	    m_nCurLen = 0;
	    m_nReadyLen = nReadyLen;
	    m_pHead = null;
	    m_pTail = null;
	    m_bReady = false;
	}
	
	/**�������ͷ����ȡһ��RTP�����ݻ���*/
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
		//����ǰ����RTP�������������泤�ȣ�����ջ��棬Ȼ��������´����RTP��
		if(m_nCurLen  > m_nLen){
			ClearBuf();
			m_pHead = m_pTail = pNode;
			m_nCurLen ++;
			m_bReady = false;
		}
		//������Ϊ�գ������Ľڵ��Ϊͷ�ڵ㣬Ҳ��β���ڵ㣻����Ϊ�գ����������β��
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
