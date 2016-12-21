package com.gx.javaDeployer;

import java.io.*;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Util {
	private static  Document doc;
	private static Properties prop;
	private static String configFile;

	public static void setConfigFile(String configFile) {
		Util.configFile = configFile;
	}

	public static void writeFile(String filePath, String text){
		try {
			File file=new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fileWriter=new FileWriter(file, false); // true代表追加
			fileWriter.write(text);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取文件
	 * @author gaoxiang @date 2016年9月19日
	 */
	public static String readFile(String filePath){
		try {
			File file=new File(filePath);
			if(!file.exists()){
				return "";
			}
			StringBuffer sb=new StringBuffer();
			FileReader filrReader=new FileReader(file);
			BufferedReader br = new BufferedReader (filrReader);
			String line;
			while(null!=(line=br.readLine())){
				sb.append(line);
			}
			filrReader.close();
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	/**
	 * InputStream转String
	 * @Author gaoxiang
	 * @Date 2015年8月17日
	 */
	public static String streamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		String charset=null;
		try {
			while ((line = reader.readLine()) != null) {
				if(charset==null){
					charset=getEncoding(line);
				}
				sb.append(new String(line.getBytes(), "utf-8") + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	/**
	 * InputStream转String
	 * @Author gaoxiang
	 * @Date 2015年8月17日
	 */
	public static String streamToString(InputStream is,boolean skipFirstLine) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		String charset=null;
		try {
			while ((line = reader.readLine()) != null) {
				if(charset==null){
					charset=getEncoding(line);
					if(skipFirstLine)continue;
				}
				sb.append(new String(line.getBytes(), "utf-8") + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	/**
	 * 解析pom
	 * @author gaoxiang @date 2016年11月10日
	 */
	public static String getValueFromPom(String dom){
		if(doc==null){
	         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	         DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				File file=new File(Data.localAppDir +"/pom.xml");
				doc = db.parse(file);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] eles=dom.split("/");
		Element root=doc.getDocumentElement();// 得到一个elment根元素
		Node node = null;
		for(int i=0;i<eles.length;i++){
			if(i==0){
				node=getNode(root.getChildNodes(),eles[i]);
			}else{
				node=getNode(node.getChildNodes(),eles[i]);
			}
			if(node==null){//没找到
				return null;
			}
		}
		return node.getTextContent()+"";
	}
	private static Node getNode(NodeList list,String nodeName){
		for(int i=0;i<list.getLength();i++){
			//System.out.println(list.item(i).getParentNode().getNodeName()+"."+list.item(i).getNodeName()+"="+list.item(i).getTextContent());
			if(list.item(i).getNodeName().equalsIgnoreCase(nodeName)){
				return list.item(i);
			}
		}
		return null;
	}
	/**
	 * 获取编码格式
	 * @author gaoxiang @date 2016年8月23日
	 */
	public static String getEncoding(String str) throws UnsupportedEncodingException {
		if (str.equals(new String(str.getBytes("UTF-8"), "UTF-8"))) { // 判断是不是GB2312
			return "UTF-8"; 
		}else if (str.equals(new String(str.getBytes("GB2312"), "GB2312"))) { // 判断是不是GB2312
			return "GB2312"; // 是的话，返回“GB2312“，以下代码同理
		}else if (str.equals(new String(str.getBytes("GBK"), "GBK"))) { // 判断是不是GB2312
			return "GBK"; 
		}else if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) { // 判断是不是GB2312
			return "ISO-8859-1"; 
		}else{
			return "GBK";
		}
	}
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir")+"/config.properties");
	}
	public static String getValueFromProp(String key){
		if(prop==null){
			if(configFile==null)configFile=System.getProperty("user.dir")+"/config.properties";
			File defaultFile=new File(configFile);
			prop = new Properties();
			try {
				prop.load(new FileInputStream(defaultFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop.getProperty(key);
	}
	

}
