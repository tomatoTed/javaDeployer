package com.gx.javaDeployer;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * �ļ��ϴ�������
 */
public class SimpleProgressMonitor implements SftpProgressMonitor{
	private long transfered; // ��¼�Ѵ���������ܴ�С
	private long fileSize;
	private String fileSizeUnit;
	private long start;
	private UIComponent ui;
	public SimpleProgressMonitor(long fileSize,UIComponent ui){
		this.fileSize=fileSize;
		this.fileSizeUnit=getFileSizeUnit(fileSize);
		this.ui=ui;
	}
	@Override
    public boolean count(long count) {
        transfered = transfered + count;
        ui.setText("������...�ļ���С:"+fileSizeUnit+",�����:"+(transfered*100/fileSize)+"%");
        return true;
    }

    @Override
    public void end() {
    	long passd=System.nanoTime()-start;
    	String passdStr="";
    	if(TimeUnit.NANOSECONDS.toHours(passd)>0){
    		passdStr=TimeUnit.NANOSECONDS.toHours(passd)+"h";
    	}else if(TimeUnit.NANOSECONDS.toMinutes(passd)>0){
    		passdStr=TimeUnit.NANOSECONDS.toMinutes(passd)+"m";
    	}else if(TimeUnit.NANOSECONDS.toSeconds(passd)>0){
    		passdStr=TimeUnit.NANOSECONDS.toSeconds(passd)+"s";
    	}else if(TimeUnit.NANOSECONDS.toMillis(passd)>0){
    		passdStr=TimeUnit.NANOSECONDS.toMillis(passd)+"ms";
    	}else{
    		passdStr=passd+"ns";
    	}
    	ui.appendText("�������,��ʱ"+passdStr);
    }

    @Override
    public void init(int op, String src, String dest, long max) {
    	start=System.nanoTime();
        ui.appendText("���俪ʼ,�ļ���С:"+getFileSizeUnit(fileSize));
    }
    private String getFileSizeUnit(long fileSize){
    	if(getExactDigital(fileSize,1024,3)>=1){
    		return getExactDigital(fileSize,1024,3)+"gb";
    	}else if(getExactDigital(fileSize,1024,2)>=1){
    		return getExactDigital(fileSize,1024,2)+"mb";
    	}else if(getExactDigital(fileSize,1024,1)>=1){
    		return getExactDigital(fileSize,1024,1)+"kb";
    	}else{
    		return fileSize+"b";
    	}
    }
    private double getExactDigital(long orign,long base,int powerOfBase){
    	long count=1l;
    	for(int i=0;i<powerOfBase;i++){
    		count=count*base;
    	}
    	BigDecimal baseBd=new BigDecimal(count);
    	BigDecimal bd=new BigDecimal(orign);
    	return bd.divide(baseBd,2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static void main(String[] args){
    }

}
