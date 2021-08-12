//package com.taozi.web.controller.dingding;
//
//import com.taobao.api.ApiException;
//import com.taozi.common.core.domain.model.BaseResult;
//import com.taozi.common.utils.DateUtils;
//import com.taozi.common.utils.dingding.DingUtils;
//import com.taozi.common.utils.dingding.entity.SignResultVO;
//import com.taozi.common.utils.dingding.entity.SignVO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 钉钉鉴权接口
// *
// * @author TAOZI
// */
//@Api(tags = "对接钉钉接口")
//@RestController
//public class DingController {
//
//	/**
//	 * 一键获取签名
//	 *
//	 * @return String sign
//	 * @throws ApiException
//	 */
//	@ApiOperation("一键获取签名")
//	@GetMapping("/directlySign")
//	public BaseResult directlySign() throws Exception {
//		String accessToken = getAccessToken();
//		String ticket = getTicket(accessToken);
//		SignVO data = new SignVO();
//		data.setJsTicket(ticket);
//		data.setTimeStamp(DateUtils.current());
//		data.setNonceStr("nonceStr");
//		data.setUrl("http://172.16.7.217:8989/");
//		SignResultVO sign = getSign(data);
//		System.out.println(sign.toString());
//		return BaseResult.ok().setData(sign);
//	}
//
//	/**
//	 * 获取access_token
//	 *
//	 * @return String access_token
//	 * @throws ApiException
//	 */
//	@ApiOperation("获取access_token")
//	@GetMapping("/getAccessToken")
//	public String getAccessToken() throws ApiException {
//		return DingUtils.getAccessToken();
//	}
//
//	/**
//	 * 获取ticket
//	 *
//	 * @param accessToken
//	 * @return String ticket
//	 * @throws ApiException
//	 */
//	@ApiOperation("获取ticket")
//	@GetMapping("/getTicket")
//	public String getTicket(String accessToken) throws ApiException {
//		return DingUtils.getTicket(accessToken);
//	}
//
//	/**
//	 * 获取签名
//	 *
//	 * @param data
//	 * @return sign
//	 * @throws Exception
//	 */
//	@ApiOperation("获取签名")
//	@PostMapping(value = "/getSign", produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public SignResultVO getSign(@RequestBody SignVO data) throws Exception {
//		return DingUtils.getSign(data);
//	}
//}