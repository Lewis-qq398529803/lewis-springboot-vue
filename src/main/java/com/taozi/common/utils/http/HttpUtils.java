package com.taozi.common.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * 通用http发送方法
 *
 * @author taozi
 */
@Slf4j
public class HttpUtils {

	/**
	 * UTF-8 字符集
	 */
	private static final String UTF8 = "UTF-8";

	/**
	 * json 数据格式
	 */
	private static final String APPLICATION_JSON = "application/json";

	/**
	 * form表单格式
	 */
	private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

	/**
	 * 向指定 URL 发送GET方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String doGet(String url, String param) {
		return doGet(url, param, UTF8);
	}

	/**
	 * 向指定 URL 发送GET方法的请求
	 *
	 * @param url         发送请求的 URL
	 * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param contentType 编码类型
	 * @return 所代表远程资源的响应结果
	 */
	public static String doGet(String url, String param, String contentType) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			log.info("sendGet - {}", urlNameString);
			log.info("param - {}", param);
			log.info("contentType - {}", contentType);
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			log.info("result - {}", result);
		} catch (ConnectException e) {
			log.error("调用HttpUtils.sendGet ConnectException, url - {}", url);
			log.error(e + "");
		} catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendGet SocketTimeoutException, url - {}", url);
			log.error(e + "");
		} catch (IOException e) {
			log.error("调用HttpUtils.sendGet IOException, url - {}" + url);
			log.error(e + "");
		} catch (Exception e) {
			log.error("调用HttpsUtil.sendGet Exception, url - {}", url);
			log.error(e + "");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				log.error("调用in.close Exception, url - {}", url);
				log.error(ex + "");
			}
		}
		return result.toString();
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String doPost(String url, String param) {
		return doPost(url, param, APPLICATION_JSON);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param httpUrl     发送请求的 URL
	 * @param param       请求参数，
	 *                    1.json请求参数应该是：{key: value}
	 *                    2.form请求参数应该是："name=value&name=value&...."
	 * @param contentType 请求数据格式
	 * @return 所代表远程资源的响应结果
	 */
	public static String doPost(String httpUrl, String param, String contentType) {
		return doPost(httpUrl, param, contentType, null);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param httpUrl     发送请求的 URL
	 * @param param       请求参数，
	 *                    1.json请求参数应该是：{key: value}
	 *                    2.form请求参数应该是："name=value&name=value&...."
	 * @param contentType 	请求数据格式
	 * @param authorization 鉴权信息
	 * @return 所代表远程资源的响应结果
	 */
	public static String doPost(String httpUrl, String param, String contentType, String authorization) {
		HttpURLConnection connection = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null;
		try {
			String realUrl = httpUrl;
			// form表单数据情况
			if (APPLICATION_X_WWW_FORM_URLENCODED.equals(contentType)) {
				realUrl += ("?" + param);
			}
			log.info("doPost - {}", realUrl);
			URL url = new URL(realUrl);
			// 通过远程url连接对象打开连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机服务器超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取主机服务器返回数据超时时间：60000毫秒
			connection.setReadTimeout(60000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Accept-Charset", UTF8);
			// 设置传入参数的格式
			connection.setRequestProperty("Content-Type", contentType);
			// 鉴权不为空的情况
			if (!"".equals(authorization) && null != authorization) {
				// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
				connection.setRequestProperty("Authorization", authorization);
			}
			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
			connection.setDoOutput(true);
			// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
			connection.setDoInput(true);
			// 通过连接对象获取一个输出流
			os = connection.getOutputStream();
			// json 情况
			if (APPLICATION_JSON.equals(contentType)) {
				// 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
				os.write(param.getBytes());
			}
			// 通过连接对象获取一个输入流，向远程读取
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				// 对输入流对象进行包装:charset根据工作项目组的要求来设置
				br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				// 循环遍历一行一行读取数据
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开与远程地址url的连接
			connection.disconnect();
		}
		return result;
	}
}