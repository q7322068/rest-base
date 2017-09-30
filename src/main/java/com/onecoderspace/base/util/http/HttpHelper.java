package com.onecoderspace.base.util.http;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public final class HttpHelper extends AbstractHttpHelper {

	protected static HttpHelper instance = new HttpHelper();

	public static HttpHelper get() {
		return instance;
	}

	private HttpHelper() {
		httpClient = HttpClientBuilder.create().setConnectionManager(getClientConnectionManager())
				.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.DEFAULT)
				.addInterceptorFirst(GZIPRequestInterceptor.DEFAULT).addInterceptorLast(GZIPResponseInterceptor.DEFAULT)
				.build();
	}

	protected HttpClient httpClient;

	protected HttpClient getHttpClient() {
		return this.httpClient;
	}

	protected HttpClientConnectionManager getClientConnectionManager() {
		PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager(5,
				TimeUnit.MINUTES);
		clientConnectionManager.setDefaultConnectionConfig(
				ConnectionConfig.custom().setBufferSize(4096).setCharset(Consts.UTF_8).build());
		clientConnectionManager.setDefaultSocketConfig(SocketConfig.custom().build());
		clientConnectionManager.setDefaultMaxPerRoute(Runtime.getRuntime().availableProcessors() * 1024);
		clientConnectionManager.setMaxTotal(Runtime.getRuntime().availableProcessors() * 1024);
		return clientConnectionManager;
	}

	/**
	 * ********** post by parameters and handler **********
	 */

	public <T> T doPost(String url, Map<String, ?> parameters, ResponseHandler<T> handler) {
		return super.doPost(url, parameters, handler);
	}

	public String doPost(String uri, Map<String, String> parameters, String charset) {
		return super.doPost(uri, parameters, new ResponseToString(charset));
	}

	public String doPost(String uri, Map<String, String> parameters) {
		return super.doPost(uri, parameters, ResponseToString.DEFAULT);
	}

	/**
	 * *********** post by body and handler ******************
	 */

	public <T> T doPost(String uri, String body, ResponseHandler<T> handler) {
		return super.doPost(uri, body.getBytes(), handler);
	}

	public String doPost(String uri, String body, String charset) {
		return super.doPost(uri, body.getBytes(), new ResponseToString(charset));
	}

	public String doPost(String uri, String body) {
		return super.doPost(uri, body.getBytes(), ResponseToString.DEFAULT);
	}

	/**
	 * ****** get by url *******************
	 */

	public <T> T doGet(String url, ResponseHandler<T> handler) {
		return super.doGet(url, handler);
	}

	public String doGet(String uri, String charset) {
		return super.doGet(uri, new ResponseToString(charset));
	}

	public String doGet(String uri) {
		return super.doGet(uri, ResponseToString.DEFAULT);
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		HttpHelper helper = HttpHelper.get();
		String text = helper.doGet("http://www.baidu123.com");
		System.out.println(text);
		// CloseableHttpClient httpClient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet("http://www.baidu.com");
//		HttpResponse response = httpClient.execute(httpGet);
//		System.err.println(response.getStatusLine());
//		HttpEntity entity = response.getEntity();
//		if (entity != null) {
//			String text = EntityUtils.toString(entity);
//			EntityUtils.consume(entity);
//			System.out.println(text);
//		}

	}

}
