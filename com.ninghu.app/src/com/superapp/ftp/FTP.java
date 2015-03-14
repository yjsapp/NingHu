package com.superapp.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP��װ��.
 *  
 * @author cui_tao
 */
public class FTP {
    /**
     * ��������.
     */
    private String hostName;

    /**
     * �û���.
     */
    private String userName;

    /**
     * ����.
     */
    private String password;

    /**
     * FTP����.
     */
    private FTPClient ftpClient;

    /**
     * FTP�б�.
     */
    private List<FTPFile> list;

    /**
     * FTP��Ŀ¼.
     */
    public static final String REMOTE_PATH = "\\";

    /**
     * FTP��ǰĿ¼.
     */
    private String currentPath = "";

    /**
     * ͳ������.
     */
    private double response;

    /**
     * ���캯��.
     * @param host hostName ��������
     * @param user userName �û���
     * @param pass password ����
     */
    public FTP(String host, String user, String pass) {
        this.hostName = host;
        this.userName = user;
        this.password = pass;
        this.ftpClient = new FTPClient();
        this.list = new ArrayList<FTPFile>();
    }

    /**
     * ��FTP����.
     * @throws IOException 
     */
    public void openConnect() throws IOException {
        // ����ת��
        ftpClient.setControlEncoding("UTF-8");
        int reply; // ��������Ӧֵ
        // ������������
        ftpClient.connect(hostName);
        // ��ȡ��Ӧֵ
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // �Ͽ�����
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        }
        // ��¼��������
        ftpClient.login(userName, password);
        // ��ȡ��Ӧֵ
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // �Ͽ�����
            ftpClient.disconnect();
            throw new IOException("connect fail: " + reply);
        } else {
            // ��ȡ��¼��Ϣ
            FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);
            // ʹ�ñ���ģʽ��ΪĬ��
            ftpClient.enterLocalPassiveMode();
            // �������ļ�֧��
            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            System.out.println("login");
        }
    }

    /**
     * �ر�FTP����.
     * @throws IOException 
     */
    public void closeConnect() throws IOException {
        if (ftpClient != null) {
            // �ǳ�FTP
            ftpClient.logout();
            // �Ͽ�����
            ftpClient.disconnect();
            System.out.println("logout");
        }
    }

    /**
     * �г�FTP�������ļ�.
     * @param remotePath ������Ŀ¼
     * @return FTPFile����
     * @throws IOException 
     */
    public List<FTPFile> listFiles(String remotePath) throws IOException {
        // ��ȡ�ļ�
        FTPFile[] files = ftpClient.listFiles(remotePath);
        // ����������ӵ�����
        for (FTPFile file : files) {
            list.add(file);
        }
        return list;
    }

    /**
     * ����.
     * @param remotePath FTPĿ¼
     * @param fileName �ļ���
     * @param localPath ����Ŀ¼
     * @return Result
     * @throws IOException 
     */
    public Result download(String remotePath, String fileName, String localPath) throws IOException {
        boolean flag = true;
        Result result = null;
        // ��ʼ��FTP��ǰĿ¼
        currentPath = remotePath;
        // ��ʼ����ǰ����
        response = 0;
        // ����FTPĿ¼
        ftpClient.changeWorkingDirectory(remotePath);
        // �õ�FTP��ǰĿ¼�������ļ�
        FTPFile[] ftpFiles = ftpClient.listFiles();
        // ѭ������
        for (FTPFile ftpFile : ftpFiles) {
            // �ҵ���Ҫ���ص��ļ�
            if (ftpFile.getName().equals(fileName)) {
                System.out.println("download...");
                // ��������Ŀ¼
                File file = new File(localPath + "/" + fileName);
                // ����ǰʱ��
                Date startTime = new Date();
                if (ftpFile.isDirectory()) {
                    // ���ض���ļ�
                    flag = downloadMany(file);
                } else {
                    // ���ص����ļ�
                    flag = downloadSingle(file, ftpFile);
                }
                // ������ʱ��
                Date endTime = new Date();
                // ����ֵ
                result = new Result(flag, Util.getFormatTime(endTime.getTime() - startTime.getTime()), Util.getFormatSize(response));
            }
        }
        return result;
    }

    /**
     * ���ص����ļ�.
     * @param localFile ����Ŀ¼
     * @param ftpFile FTPĿ¼
     * @return true���سɹ�, false����ʧ��
     * @throws IOException 
     */
    private boolean downloadSingle(File localFile, FTPFile ftpFile) throws IOException {
        boolean flag = true;
        // ���������
        OutputStream outputStream = new FileOutputStream(localFile);
        // ͳ������
        response += ftpFile.getSize();
        // ���ص����ļ�
        flag = ftpClient.retrieveFile(localFile.getName(), outputStream);
        // �ر��ļ���
        outputStream.close();
        return flag;
    }

    /**
     * ���ض���ļ�.
     * @param localFile ����Ŀ¼
     * @return true���سɹ�, false����ʧ��
     * @throws IOException 
     */
    private boolean downloadMany(File localFile) throws IOException {
        boolean flag = true;
        // FTP��ǰĿ¼
        if (!currentPath.equals(REMOTE_PATH)) {
            currentPath = currentPath + REMOTE_PATH + localFile.getName();
        } else {
            currentPath = currentPath + localFile.getName();
        }
        // ���������ļ���
        localFile.mkdir();
        // ����FTP��ǰĿ¼
        ftpClient.changeWorkingDirectory(currentPath);
        // �õ�FTP��ǰĿ¼�������ļ�
        FTPFile[] ftpFiles = ftpClient.listFiles();
        // ѭ������
        for (FTPFile ftpFile : ftpFiles) {
            // �����ļ�
            File file = new File(localFile.getPath() + "/" + ftpFile.getName());
            if (ftpFile.isDirectory()) {
                // ���ض���ļ�
                flag = downloadMany(file);
            } else {
                // ���ص����ļ�
                flag = downloadSingle(file, ftpFile);
            }
        }
        return flag;
    }

    /**
     * �ϴ�.
     * @param localFile �����ļ�
     * @param remotePath FTPĿ¼
     * @return Result
     * @throws IOException 
     */
    public Result uploading(File localFile, String remotePath) throws IOException {
        boolean flag = true;
        Result result = null;
        //ΪԶ�̵�FTP�����������ϴ���Ŀ¼��
       
        
        
        if (ftpClient.listFiles(remotePath) == null || ftpClient.listFiles(remotePath).length == 0) {  
        	ftpClient.makeDirectory(remotePath);  
        	System.out.println("chuanjianmulu");
         
        } 
        
        
        // ��ʼ��FTP��ǰĿ¼
        currentPath = remotePath;
        // ��ʼ����ǰ����
        response = 0;
        // �������ļ�֧��
        ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        // ʹ�ñ���ģʽ��ΪĬ��
        ftpClient.enterLocalPassiveMode();
        // ����ģʽ
        ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
        // �ı�FTPĿ¼
        ftpClient.changeWorkingDirectory(REMOTE_PATH);
        // ��ȡ�ϴ�ǰʱ��
        Date startTime = new Date();
        if (localFile.isDirectory()) {
            // �ϴ�����ļ�
            flag = uploadingMany(localFile);
        } else {
            // �ϴ������ļ�
            flag = uploadingSingle(localFile);
        }
        // ��ȡ�ϴ���ʱ��
        Date endTime = new Date();
        // ����ֵ
        result = new Result(flag, Util.getFormatTime(endTime.getTime() - startTime.getTime()), Util.getFormatSize(response));
        return result;
    }

    /**
     * �ϴ������ļ�.
     * @param localFile �����ļ�
     * @return true�ϴ��ɹ�, false�ϴ�ʧ��
     * @throws IOException 
     */
    private boolean uploadingSingle(File localFile) throws IOException {
        boolean flag = true;
        // ����������
        InputStream inputStream = new FileInputStream(localFile);
        // ͳ������
        response += (double) inputStream.available() / 1;
        // �ϴ������ļ�
        flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // �ر��ļ���
        inputStream.close();
        return flag;
    }

    /**
     * �ϴ�����ļ�.
     * @param localFile �����ļ���
     * @return true�ϴ��ɹ�, false�ϴ�ʧ��
     * @throws IOException 
     */
    private boolean uploadingMany(File localFile) throws IOException {
        boolean flag = true;
        // FTP��ǰĿ¼
        if (!currentPath.equals(REMOTE_PATH)) {
            currentPath = currentPath + REMOTE_PATH + localFile.getName();
        } else {
            currentPath = currentPath + localFile.getName();
        }
        // FTP�´����ļ���
        ftpClient.makeDirectory(currentPath);
        // ����FTPĿ¼
        ftpClient.changeWorkingDirectory(currentPath);
        // �õ���ǰĿ¼�������ļ�
        File[] files = localFile.listFiles();
        // �����õ�ÿ���ļ����ϴ�
        for (File file : files) {
            if (file.isHidden()) {
                continue;
            }
            if (file.isDirectory()) {
                // �ϴ�����ļ�
                flag = uploadingMany(file);
            } else {
                // �ϴ������ļ�
                flag = uploadingSingle(file);
            }
        }
        return flag;
    }
}

