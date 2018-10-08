package com.hgx.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 	连接管理器
 * 
 *
 */
public class HttpClientPool {
	/**
	 * 各配置的默认值
	 * 
	 * Builder() { 
		 * super(); 
		 * this.staleConnectionCheckEnabled = false; 
		 * this.redirectsEnabled = true; 
		 * this.maxRedirects = 50; 
		 * this.relativeRedirectsAllowed = true; 
		 * this.authenticationEnabled = true;
		 * this.connectionRequestTimeout = -1; 
		 * this.connectTimeout = -1; 
		 * this.socketTimeout = -1; 
		 * this.contentCompressionEnabled = true; 
	 * }
	 *
	 *
	 */
	private HttpClientPool() {
		
	}
	/**
	 * 连接池的最大连接数
	 */
	private static final int POOL_MAX_TOTAL = 300;

	/**
	 * 设置每一个路由的最大连接数
	 */
	private static final int POOL_MAX_PRE_ROUTE = 50;
	/**
	 * 连接超时时间，客户端请求服务器与服务器建立连接（三次握手）成功的最大接受时间
	 */
	private static final int CONNECT_TIMEOUT = -1;

	/**
	 * 请求获取数据的超时时间,访问一个接口指定时间内无法返回数据,直接放弃此次调用
	 */
	private static final int SOCKET_TIMEOUT = -1;

	/**
	 * 从连接池中获取可用连接的时间
	 */
	private static final int CONNECTION_REQUEST_TIMEOUT = -1;
	/**
	 * 
	 */
	private static RequestConfig requestConfig=null;
	/**
	 * 
	 */
	private static PoolingHttpClientConnectionManager connectionManager = null;
	static {
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(POOL_MAX_TOTAL);
		connectionManager.setDefaultMaxPerRoute(POOL_MAX_PRE_ROUTE);
		// 设置HttpClient请求超时配置
		requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
	}
	/**获取HttpClient
	 * @return HttpClient
	 */
	public static CloseableHttpClient getHttpClient(){
		// 获取HttpClient
		return HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
	}

}
