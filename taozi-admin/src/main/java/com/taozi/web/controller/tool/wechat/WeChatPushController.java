package com.taozi.web.controller.tool.wechat;


import com.taozi.common.core.domain.model.CommonResult;
import com.taozi.common.utils.StringUtils;
import com.taozi.common.utils.wechat.wxcommon.AccessTokenUtils;
import com.taozi.common.utils.wechat.wxpush.WxPushUtils;
import com.taozi.common.utils.wechat.wxpush.vo.WxPushVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 微信公众号推送
 *
 * @author taozi - 2021年7月27日, 027 - 10:04:52
 */
@Api(tags = "信息推送")
@Controller
@RequestMapping("/WxPush")
public class WeChatPushController {

	@PostMapping("/sendMessage2OpenId")
	@ApiOperation(value = "微信公众号推送", notes = "必要参数： openid、templateid、code、data")
	public CommonResult sendMessage2OpenId(@RequestBody WxPushVo wxPushVo) {
		if (wxPushVo.getOpenid() == null || wxPushVo.getTemplate_id() == null || wxPushVo.getCode() == null) {
			return CommonResult.fail().setMsg("参数缺失，请仔细检查，并且对比接口文档确认是否有必要参数未传入！");
		}

		String accessToken = AccessTokenUtils.getAccessToken(wxPushVo.getCode());
		if (StringUtils.equals(accessToken, "获取access_token失败") || StringUtils.equals(accessToken, "获取access_token出现异常")) {
			return CommonResult.fail().setMsg("access_token获取失败，请检查参数例如code的正确性！");
		}
		wxPushVo.setACCESS_TOKEN(accessToken);

		return CommonResult.okOrFail(WxPushUtils.sendWxMsg2User(wxPushVo));
	}

}
