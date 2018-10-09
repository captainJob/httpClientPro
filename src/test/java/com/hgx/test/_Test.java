package com.hgx.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgx.utils.http.HttpClientPool;

public class _Test {

	@Test
	public void post() throws IOException, URISyntaxException {
		HttpClient httpClient = HttpClientPool.getHttpClient();
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		String userMapJson = objectMapper.writeValueAsString(map);

		// 参数拼接，url为请求接口地址
		URIBuilder builder = new URIBuilder("http://www.baidu.com");
//		builder.setPath("/index");
		HttpPost post = new HttpPost(builder.build());
		// 构建消息实体
		StringEntity entity = new StringEntity(userMapJson, Charset.forName("UTF-8"));
		entity.setContentEncoding("UTF-8");
		// 发送Json格式的数据请求
		entity.setContentType("application/json");
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		// 状态码
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			// 从response里获取数据实体并放到实体包装器中
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity == null) {
				System.out.println("httpEntity 为 NULL ！");
				return;
			}
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
			Header header = bufferedHttpEntity.getContentType();
			if (StringUtils.contains(header.getValue(), "application/json")) {
				// 返回json字符串
				String body = EntityUtils.toString(bufferedHttpEntity, "UTF-8");

				System.out.println(body);
			} else if (StringUtils.contains(header.getValue(), "text/plain")) {
				// 返回压缩包
				// 获取数据流
				InputStream in = bufferedHttpEntity.getContent();
				// 封装成zip输入流
				ZipInputStream zin = new ZipInputStream(in, Charset.forName("UTF-8"));

				// 文件存放地址
				String path = "D:\\";
				File file = null;
				if (in != null) {
					in.close();
				}
			} else {
				System.out.println("返回了其他类型：" + header.getValue());
			}
		} else {
			System.out.println("请求失败，返回状态码为：" + statusCode);
		}

//	关闭过期连接	httpClient.getConnectionManager().closeExpiredConnections();

		/*if (response != null) {
			(response).close();
		}

		HttpGet request = new HttpGet("http://localhost:8080/tao-manager-web/get/" + "啊啊啊");
		request.setHeader("Accept", "application/json");
		HttpResponse response = httpClient.execute(request);

//		终止
		if (null != post) {
			post.abort();
		}*/

	}
	
	@Test
	public void testName1() throws Exception {
		System.out.println(StringUtils.containsIgnoreCase(System.getProperty("os.name"),"Windows"));
	}

}
