package com.shiwen.shares.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 这说明在多线程环境下应该使用一个全局单例的HttpClient，并且使用MultiThreadHttpConnectionManager来管理Connection。
/**
 * 线程安全
 * 
 * @author zhanghw
 */
public class HttpClientUtils {
	private static final Logger LOGGER;
	private static final HttpClient httpClient;
	private static final HttpClientUtils instance = new HttpClientUtils();
	private volatile static AtomicInteger errorCount = new AtomicInteger();

	static {
		LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
		httpClient = new HttpClient();
		int maxTotalConnections = 50000;
		int maxHostConnections = 50000;
		int maxHostConnectionsPerHost = 50000;
		int connectionTimeout = 50_000;
		setHttpClientConfig(maxTotalConnections, maxHostConnections, maxHostConnectionsPerHost, connectionTimeout);
	}

	public static HttpClientUtils getInstance() {
		return instance;
	}

	/**
	 * 最大耗时，最小耗时，90%的耗时，QPS
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		args = new String[] { "1" };

		InputStream inputStream = HttpClientUtils.class.getClassLoader().getResourceAsStream("test.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		System.setProperties(properties);
		inputStream.close();

		String reqCountStr = System.getProperty("reqCount");
		String threadPoolSizeStr = System.getProperty("threadPoolSize");

		final int reqCount = Integer.valueOf(reqCountStr);
		int threadPoolSize = Integer.valueOf(threadPoolSizeStr);

		if (args.length < 1) {
			System.exit(0);
			System.out.println("需要jvm参数传入！");
		}

		final HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
		final ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);
		final CountDownLatch countDown = new CountDownLatch(reqCount);
		final HashMap<String, String> hashMap = new HashMap<>();

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < reqCount; i++) {
			int index = i % args.length;
			final String requestUrl = args[index];

			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						httpClientUtils.getUrl(requestUrl, hashMap);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						countDown.countDown();
						if (countDown.getCount() % 10000 == 0) {
							System.err.println("deal count =" + (reqCount - countDown.getCount()));
						}
					}
				}
			});
		}

		try {
			countDown.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();

		long useTime = (endTime - startTime);
		useTime = useTime > 1 ? useTime : 1;
		useTime = useTime / 1000;

		System.out.println("use total time:>>>>>>>>>>>>>" + useTime);
		long QPS = reqCount / useTime;

		System.out.println(reqCount + "/" + useTime + "=" + QPS + ";error count=" + errorCount.get());
		System.out.println("program hash exited!");
		System.exit(0);

	}

	private HttpClientUtils() {

	}

	public AtomicInteger getErrorCount() {
		return errorCount;
	}

	private static void setHttpClientConfig(int maxTotalConnections, int maxHostConnections,
			int maxHostConnectionsPerHost, int connectionTimeout) {
		MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();

		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setMaxTotalConnections(maxTotalConnections);
		params.setConnectionTimeout(connectionTimeout);
		params.setDefaultMaxConnectionsPerHost(maxHostConnections);

		httpConnectionManager.setParams(params);
		httpClient.setHttpConnectionManager(httpConnectionManager);
	}

	public String postUrl(String url, Map<String, String> paramMap) throws Exception {
		LOGGER.debug("post url:'{}',parameters={}", url, paramMap);
		String result = null;
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		if (paramMap != null && !paramMap.isEmpty()) {
			setParamsPost(post, paramMap);
		}
		int responseCode = -400;
		try {
			responseCode = httpClient.executeMethod(post);
			result = post.getResponseBodyAsString();
			if (responseCode != 200) {// TODO 由于上边的IO异常导致此处空指针
				LOGGER.error("the response code error:{},errorCount ={}", result, errorCount.incrementAndGet());

			}
			// 此处可能会由于http服务器处理时间过长而导致超时，造成IO异常
		} catch (HttpException e) {// 不可恢复的
			String msg = String.format("Failed to call the url [%s].", url);
			LOGGER.error(msg, e);
			throw new Exception(msg, e);
		} catch (IOException e) {// 可以被恢复
			String msg = String.format("call the url [%s] occured an IO Exception.", url);
			LOGGER.error(msg, e);
			// throw new FileDeliverCallbackException(msg, e);
		} finally {
			post.releaseConnection();
		}

		LOGGER.debug("success post url:{},param:{} ", url, paramMap);
		return result;
	}

	public String getUrl(String url, Map<String, String> paramMap) throws Exception {
		String result = null;
		GetMethod get = new GetMethod();
		get.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		URI uri = new URI(url, true);
		get.setURI(uri);
		setParamsGet(get, paramMap);
		int responseCode = -200;
		try {
			responseCode = httpClient.executeMethod(get);
			// 此处可能会由于http服务器处理时间过长而导致超时，造成IO异常
		} catch (HttpException e) {// 不可恢复的
			String msg = String.format("Failed to call the url [%s].", url);
			LOGGER.error(msg, e);
			throw new Exception(msg, e);
		} catch (IOException e) {// 可以被恢复
			String msg = String.format("call the url [%s] occured an IO Exception.", url);
			LOGGER.error(msg, e);
			// throw new FileDeliverCallbackException(msg, e);
		}

		result = get.getResponseBodyAsString();
		if (get.getStatusCode() != 200) {// TODO 由于上边的IO异常导致此处空指针
			try {
				LOGGER.error("the response code error:{},current error Count={}", result, errorCount.incrementAndGet());

			} catch (Exception e) {
				LOGGER.error("get response content error:", e);
			}
			String msg = String.format("The response status code is not 200 but [%s]", responseCode);
			throw new Exception(msg);
		}
		LOGGER.debug("success post url:{},param:{} ,Result={}", url, paramMap, result);

		return result;
	}

	private static void setParamsPost(PostMethod post, Map<String, String> paramMap) {
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			NameValuePair param = new NameValuePair(entry.getKey(), entry.getValue());
			post.addParameter(param);
		}
	}

	private static void setParamsGet(GetMethod get, Map<String, String> paramMap) {
		HttpMethodParams params = new HttpMethodParams();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			params.setParameter(entry.getKey(), entry.getValue());
		}
		get.setParams(params);
	}

}
