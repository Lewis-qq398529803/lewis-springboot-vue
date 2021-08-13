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
//import java.util.UUID;
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
//	 */
//	@ApiOperation("鉴权（一键式）：获取签名")
//	@GetMapping("/directlySign")
//	public BaseResult directlySign() {
//		String accessToken = getAccessToken();
//		String ticket = getTicket(accessToken);
//		SignVO data = new SignVO();
//		data.setJsTicket(ticket);
//		data.setTimeStamp(DateUtil.current());
//		data.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
//		data.setUrl("http://172.16.7.217:8080/#/textdd");
//		SignResultVO sign = getSign(data);
//		return BaseResult.ok().setData(sign);
//	}
//
//	/**
//	 * 获取access_token
//	 *
//	 * @return String access_token
//	 */
//	@ApiOperation("鉴权（分步）1：获取access_token")
//	@GetMapping("/getAccessToken")
//	public String getAccessToken() {
//		return DingUtils.getAccessToken();
//	}
//
//	/**
//	 * 获取ticket
//	 *
//	 * @param accessToken
//	 * @return String ticket
//	 */
//	@ApiOperation("鉴权（分步）2：获取ticket")
//	@GetMapping("/getTicket")
//	public String getTicket(String accessToken) {
//		return DingUtils.getTicket(accessToken);
//	}
//
//	/**
//	 * 获取签名
//	 *
//	 * @param data
//	 * @return sign
//	 */
//	@ApiOperation("鉴权（分布）3：获取签名")
//	@PostMapping(value = "/getSign", produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public SignResultVO getSign(@RequestBody SignVO data) {
//		return DingUtils.getSign(data);
//	}
//}
