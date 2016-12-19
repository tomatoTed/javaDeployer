package com.gx.javaDeployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 远程连接工具
 */
public class Remote {
    private Session session=null;
    private ChannelSftp sftpChannel=null;
    private UIComponent ui=null;

    public Remote(String host,int port,String username,String password,UIComponent ui) throws JSchException {
        this.ui=ui;
        createSession(host,port,username,password);
    }

    /**
     * 创建链接
     * @authdate gaoxiang 2016年7月20日
     */
    private void createSession(String host,int port,String username,String password) throws JSchException{
        if(session==null){
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
        }
    }
    /**
     * 创建sftp链接
     * @authdate gaoxiang 2016年7月20日
     */
    private void createSftp() throws JSchException{
        if (sftpChannel == null) {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
        }
    }
    /**
     * 上传文件
     * @throws JSchException
     * @throws SftpException
     * @throws FileNotFoundException
     * @authdate gaoxiang 2016年7月20日
     */
    public void upload(String remoteDir, File localFile) throws JSchException {
        createSftp();
        try {
            sftpChannel.cd(remoteDir);
            sftpChannel.put(new FileInputStream(localFile),
                    localFile.getName(),
                    new SimpleProgressMonitor(localFile.length(),ui));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 下载文件
     * @throws JSchException
     * @authdate gaoxiang 2016年7月20日
     */
    public void download(String directory, String downloadFile, String saveFile) throws JSchException {
        createSftp();
        try {
            sftpChannel.cd(directory);
            File file = new File(saveFile);
            sftpChannel.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭链接
     * @authdate gaoxiang 2016年7月20日
     */
    public void closeSession(){
        if (sftpChannel != null && !sftpChannel.isClosed()) {
            sftpChannel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
    /**
     * 远程执行命令
     * @throws JSchException
     * @authdate gaoxiang 2016年7月20日
     */
    public String exec(String command,boolean showCommand,boolean skipFirstLine) throws JSchException {
        if(showCommand){
            ui.appendText(">> "+command);
        }
        String result = "";
        ChannelExec execChannel = null;
        try {
            execChannel = (ChannelExec) session.openChannel("exec");
            execChannel.setCommand(command);
            execChannel.connect();
            result = Util.streamToString(execChannel.getInputStream(),skipFirstLine);
            ui.appendText(result);
        } catch (JSchException e) {
            result += e.getMessage();
        }catch ( IOException e) {
            result += e.getMessage();
        } finally {
            if (execChannel != null && !execChannel.isClosed()) {
                execChannel.disconnect();
            }
        }
        return result;
    }
    /**
     * 远程执行shell
     * @author gaoxiang @date 2016年8月23日
     */
    public void shell(String cmd) throws Exception {
        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
        channelShell.connect(3000);
        //获取输入流和输出流
        InputStream instream = channelShell.getInputStream();
        OutputStream outstream = channelShell.getOutputStream();
        ui.appendText(">>"+cmd);
        // 发送需要执行的SHELL命令，需要用\n结尾，表示回车
        cmd=cmd+"\n";
        outstream.write(cmd.getBytes());
        outstream.flush();
        Thread.sleep(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        String line = null ;
        while ((line = reader.readLine()) != null) {
            if (line.equals("exit_qwertyuiop")) {
                break;
            }
            ui.appendText(line);
            if (Data.codeMap.containsKey(line)) {
                ui.setText("部署中:" + Data.codeMap.get(line));
            }
        }
        outstream.close();
        instream.close();
        channelShell.disconnect();
    }

    public String exec(String command) throws JSchException {
        ui.appendText(">> "+command);
        String result = "";
        ChannelExec execChannel = null;
        try {
            execChannel = (ChannelExec) session.openChannel("exec");
            execChannel.setCommand(command);
            execChannel.connect();
            result = Util.streamToString(execChannel.getInputStream());
            ui.appendText(result);
        } catch (JSchException e) {
            result += e.getMessage();
        }catch ( IOException e) {
            result += e.getMessage();
        }finally {
            if (execChannel != null && !execChannel.isClosed()) {
                execChannel.disconnect();
            }
        }
        return result;
    }
}
