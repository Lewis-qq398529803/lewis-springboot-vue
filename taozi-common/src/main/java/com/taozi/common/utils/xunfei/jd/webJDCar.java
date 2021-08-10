package com.taozi.common.utils.xunfei.jd;

import com.alibaba.fastjson.JSONObject;
import com.taozi.common.utils.xunfei.jd.entity.Property;
import com.taozi.common.utils.xunfei.jd.util.FileUtil;
import com.taozi.common.utils.xunfei.jd.util.HttpUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 车牌识别 JD WebAPI 接口调用示例
 * 运行前：请先填写Appid、APIKey、APISecret以及图片路径
 * 运行方法：直接运行 main() 即可
 * 结果： 控制台输出结果信息
 * 接口文档（必看）：https://www.xfyun.cn/doc/words/vehicleLicensePlateRecg/API.html
 *
 * @author taozi
 */
public class webJDCar {

	public static void main(String[] args) throws Exception {
		webJDCar demo = new webJDCar();
		String respData = demo.imageContrast(Property.IMAGE_PATH);
		System.out.println("车牌识别结果(text)base64解码后：");
		System.out.println(respData);
	}

	public String getParam(String imageBase641, String imageEncoding1) {
		JSONObject jso = new JSONObject();

		// header
		JSONObject header = new JSONObject();
		header.put("app_id", Property.APP_ID);
		header.put("status", 3);

		jso.put("header", header);

		// parameter
		JSONObject parameter = new JSONObject();
		JSONObject service = new JSONObject();

		JSONObject faceCompareResult = new JSONObject();
		faceCompareResult.put("encoding", "utf8");
		faceCompareResult.put("format", "json");
		faceCompareResult.put("compress", "raw");
		service.put("carLicenseRes", faceCompareResult);
		parameter.put("jd_ocr_car", service);
		jso.put("parameter", parameter);

		// payload
		JSONObject payload = new JSONObject();
		JSONObject inputImage1 = new JSONObject();
		inputImage1.put("encoding", imageEncoding1);
		inputImage1.put("image", imageBase641);
		//3:一次性传完
		inputImage1.put("status", 3);
		payload.put("carImgBase64Str", inputImage1);

		System.out.println(jso.toString());
		jso.put("payload", payload);
		return jso.toString();
	}


	/**
	 * 读取image
	 * @param imagePath
	 * @return imageByteArray1
	 * @throws IOException
	 */
	private byte[] readImage(String imagePath) throws IOException {
		InputStream is = new FileInputStream(imagePath);
		byte[] imageByteArray1 = FileUtil.read(imagePath);
		//return is.readAllBytes();
		return imageByteArray1;
	}

	public String imageContrast(String imageFirstUrl) throws Exception {

		String url = assembleRequestUrl(Property.REQUEST_URL, Property.API_KEY, Property.API_SECRET);

		String imageBase641 = Base64.getEncoder().encodeToString(readImage(imageFirstUrl));
		String imageEncoding1 = imageFirstUrl.substring(imageFirstUrl.lastIndexOf(".") + 1);

		//System.out.println("url:"+url);
		return handleImageContrastRes(url, getParam(imageBase641, imageEncoding1));
	}

	public static final JSONObject JSON = new JSONObject();

	private String handleImageContrastRes(String url, String bodyParam) {

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-type", "application/json");
		String result = HttpUtil.doPost2(url, headers, bodyParam);
		if (result != null) {
			System.out.println("车牌识别接口调用结果：" + result);
			return result;
		} else {
			return null;
		}
	}


	/**
	 * 构建url
	 * @param requestUrl
	 * @param apiKey
	 * @param apiSecret
	 * @return String url
	 */
	public static String assembleRequestUrl(String requestUrl, String apiKey, String apiSecret) {
		URL url = null;
		// 替换调schema前缀 ，原因是URL库不支持解析包含ws,wss schema的url
		String httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://", "https://");
		try {
			url = new URL(httpRequestUrl);
			//获取当前日期并格式化
			SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			String date = format.format(new Date());

			String host = url.getHost();
			if (url.getPort() != 80 && url.getPort() != 443) {
				host = host + ":" + String.valueOf(url.getPort());
			}
			StringBuilder builder = new StringBuilder("host: ").append(host).append("\n").
					append("date: ").append(date).append("\n").
					append("POST ").append(url.getPath()).append(" HTTP/1.1");
			Charset charset = Charset.forName("UTF-8");
			Mac mac = Mac.getInstance("hmacsha256");
			SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
			mac.init(spec);
			byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
			String sha = Base64.getEncoder().encodeToString(hexDigits);

			String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
			String authBase = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
			return String.format("%s?authorization=%s&host=%s&date=%s", requestUrl, URLEncoder.encode(authBase), URLEncoder.encode(host), URLEncoder.encode(date));

		} catch (Exception e) {
			throw new RuntimeException("assemble requestUrl error:" + e.getMessage());
		}
	}
}
