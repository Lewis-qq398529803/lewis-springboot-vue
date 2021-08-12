//package com.taozi.common.utils.dingding;
//
//import com.alibaba.fastjson.JSONObject;
//import com.dingtalk.api.DefaultDingTalkClient;
//import com.dingtalk.api.DingTalkClient;
//import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
//import com.dingtalk.api.request.OapiGettokenRequest;
//import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
//import com.dingtalk.api.response.OapiGettokenResponse;
//import com.taobao.api.ApiException;
//import com.taozi.common.utils.DateUtils;
//import com.taozi.common.utils.dingding.entity.DingConfig;
//import com.taozi.common.utils.dingding.entity.SignResultVO;
//import com.taozi.common.utils.dingding.entity.SignVO;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.net.URL;
//import java.net.URLDecoder;
//import java.security.MessageDigest;
//import java.util.Formatter;
//
///**
// * 钉钉utils
// *
// * @author taozi - 2021年8月9日, 009 - 11:10:35
// */
//public class DingUtils {
//
//	/**
//	 * 获取access_token
//	 *
//	 * @return String access_token
//	 * @throws ApiException
//	 */
//	public static String getAccessToken() throws ApiException {
//		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
//		OapiGettokenRequest request = new OapiGettokenRequest();
//		request.setAppkey(DingConfig.APP_KEY);
//		request.setAppsecret(DingConfig.APP_SECREY);
//		request.setHttpMethod("GET");
//		OapiGettokenResponse response = client.execute(request);
//		String body = response.getBody();
//		System.out.println(body);
//		JSONObject jsonObject = JSONObject.parseObject(new String(body));
//		String accessToken = jsonObject.getString("access_token");
//		System.out.println("access_token： " + accessToken);
//		return accessToken;
//	}
//
//	/**
//	 * 获取ticket
//	 * @param accessToken
//	 * @return String ticket
//	 * @throws ApiException
//	 */
//	public static String getTicket(String accessToken) throws ApiException {
//		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/get_jsapi_ticket");
//		OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
//		req.setHttpMethod("GET");
//		OapiGetJsapiTicketResponse rsp = client.execute(req, accessToken);
//		String body = rsp.getBody();
//		System.out.println(body);
//		return body;
//	}
//
//	/**
//	 * 获取签名
//	 * @param data
//	 * @return sign
//	 * @throws Exception
//	 */
//	public static SignResultVO getSign(@RequestBody SignVO data) throws Exception {
//		String plain = "jsapi_ticket=" + data.getJsTicket() + "&noncestr=" + data.getNonceStr() + "&timestamp=" + String.valueOf(data.getTimeStamp())+ "&url=" + decodeUrl(data.getUrl());
//
//		try {
//			MessageDigest sha1 = MessageDigest.getInstance("SHA-256");
//			sha1.reset();
//			sha1.update(plain.getBytes("UTF-8"));
//			String sign = byteToHex(sha1.digest());
//			System.out.println(sign);
//			// 拷贝vo
//			SignResultVO signResultVO = new SignResultVO();
//			signResultVO.setAgentId(DingConfig.AGENT_ID);
//			signResultVO.setCorpId(DingConfig.CORP_ID);
//			signResultVO.setNonceStr(data.getNonceStr());
//			signResultVO.setTimeStamp(data.getTimeStamp());
//			signResultVO.setSignature(sign);
//			return signResultVO;
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		}
//
//		// return com.alibaba.fastjson.JSON.toJSONString(data);
//	}
//
//	private static String byteToHex(final byte[] hash) {
//		Formatter formatter = new Formatter();
//		for (byte b : hash) {
//			formatter.format("%02x", b);
//		}
//		String result = formatter.toString();
//		formatter.close();
//		return result;
//	}
//
//	private static String decodeUrl(String url) throws Exception {
//		URL hurler = new URL(url);
//		StringBuilder urlBuffer = new StringBuilder();
//		urlBuffer.append(hurler.getProtocol());
//		urlBuffer.append(":");
//		if (hurler.getAuthority() != null && hurler.getAuthority().length() > 0) {
//			urlBuffer.append("//");
//			urlBuffer.append(hurler.getAuthority());
//		}
//		if (hurler.getPath() != null) {
//			urlBuffer.append(hurler.getPath());
//		}
//		if (hurler.getQuery() != null) {
//			urlBuffer.append('?');
//			urlBuffer.append(URLDecoder.decode(hurler.getQuery(), "utf-8"));
//		}
//		return urlBuffer.toString();
//	}
//}
