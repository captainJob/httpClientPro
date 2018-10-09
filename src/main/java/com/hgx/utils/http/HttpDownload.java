package com.hgx.utils.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;

/**
 * 通用下载类 代码引用网址： https://www.cnblogs.com/ygjlch/p/6054078.html
 * 
 * @author hgx
 *
 */
public class HttpDownload {
	/**
	 * 是否为windows系统
	 */
	public static final boolean isWindows;
	
	public static final String splash;

	/**
	 * 根目录
	 */
	public static final String root;
	static {
		// windows系统
		if (StringUtils.containsIgnoreCase(System.getProperty("os.name"), "Windows")) {
			isWindows = true;
			splash = "\\";
			root = "D:\\javadownload";
		} else {
			isWindows = false;
			splash = "/";
			root = "/home";
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param url 下载地址，保存在默认位置，文件名随机生成
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String download(String url) throws ClientProtocolException, IOException {
		return download(url, null);
	}

	/**下载文件，保存到filepath
	 * @param url 下载地址
	 * @param filepath 文件存放路径
	 * @return JSON消息
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String download(String url,String filepath) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientPool.getHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = client.execute(httpget);
		HttpEntity entity = response.getEntity();
		// 放到实体包装器中
		BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
		// 获取数据流
		InputStream is = bufferedHttpEntity.getContent();
		if(StringUtils.isBlank(filepath)) {
			filepath=getFilePath(response); 
		}
		File file = new File(filepath);
		//创建文件
		file.getParentFile().mkdirs();
		FileOutputStream fileout = new FileOutputStream(file); 
		 /** 
		    * 根据实际运行效果 设置缓冲区大小 
         */  
        byte[] buffer=new byte[1024*10];  
        int ch = 0;  
        while ((ch = is.read(buffer)) != -1) {  
            fileout.write(buffer,0,ch);  
        }  
        is.close();  
        fileout.flush();  
        fileout.close();  
		return "";
	}

	/**
	 * 获取response header中Content-Disposition中的filename值
	 * 
	 * @param response
	 * @return
	 */
	public static String getFileName(HttpResponse response) {
		Header contentHeader = response.getFirstHeader("Content-Disposition");
		String filename = null;
		if (contentHeader != null) {
			HeaderElement[] values = contentHeader.getElements();
			if (values.length == 1) {
				NameValuePair param = values[0].getParameterByName("filename");
				if (param != null) {
					try {
						// filename = new String(param.getValue().toString().getBytes(), "utf-8");
						// filename=URLDecoder.decode(param.getValue(),"utf-8");
						filename = param.getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return filename;
	}

	/**获取本地路径
	 * @param response
	 * @return
	 */
	public static String getFilePath(HttpResponse response) {
		String filepath = root + splash;
		String filename = getFileName(response);
		if (filename != null) {
			filepath += filename;
		} else {
			filepath += getRandomFileName();
		}
		return filepath;
	}

	/**
	 * 获取随机文件名
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static void main(String[] args) {
		String url = "https://dldir1.qq.com/qqfile/qq/QQ9.0.6/24046/QQ9.0.6.24046.exe";
		String url2="http://www.subku.net/download/MTA5MDkzfGVjY2Q0YmU5OWM0YmQzN2MwNWFmMzAzYXwxNTM5MDU4ODkyfDY1ZTMzZDkyfHJlbW90ZQ%3D%3D/svr/dx1";
		String filepath = "D:\\test\\a.exe";
        try {
//			HttpDownload.download(url, filepath);
        	download(url);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
