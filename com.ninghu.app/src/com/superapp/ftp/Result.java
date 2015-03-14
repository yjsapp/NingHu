package com.superapp.ftp;

/**
 * ִ��ÿһ����������Ӧ�Ľ���������ɹ��ĺ�ʧ�ܵ�.
 * 
 * @author hao_yujie, cui_tao
 *
 */
public class Result {

    /**
     * ��Ӧ������.
     */
    private String response;

    /**
     * ��Ӧ�Ľ��.
     */
    private boolean succeed;

    /**
     * ��Ӧ��ʱ��.
     */
    private String time;

    /**
     * �޲εĹ��췽��.
     */
    public Result() {
    }

    /**
     * ���췽��.
     * 
     * @param res ��Ӧ������
     */
    public Result(String res) {
        this.response = res;
    }

    /**
     * ���췽��.
     * 
     * @param suc ��Ӧ�Ľ��
     * @param ti ��Ӧ��ʱ��
     * @param res ��Ӧ������
     */
    public Result(boolean suc, String ti, String res) {
        this.succeed = suc;
        this.time = ti;
        this.response = res;
    }

    /**
     * �õ���Ӧ����.
     * 
     * @return ��Ӧ����
     */
    public String getResponse() {
        return response;
    }

    /**
     * ������Ӧ����.
     * 
     * @param response ��Ӧ����
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * �õ���Ӧ���.
     * 
     * @return ��Ӧ���
     */
    public boolean isSucceed() {
        return succeed;
    }

    /**
     * ������Ӧ���.
     * 
     * @param succeed ��Ӧ���
     */
    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    /**
     * �õ���Ӧʱ��.
     * 
     * @return ��Ӧʱ��
     */
    public String getTime() {
        return time;
    }

    /**
     * ������Ӧʱ��.
     * 
     * @param time ��Ӧʱ��
     */
    public void setTime(String time) {
        this.time = time;
    }

}
