package com.lewis.app.system.controller;

import com.lewis.common.core.controller.BaseController;
import com.lewis.common.core.domain.AjaxResult;
import com.lewis.common.core.domain.model.RegisterBody;
import com.lewis.common.utils.StringUtils;
import com.lewis.framework.web.service.SysRegisterService;
import com.lewis.app.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author Lewis
 */
@Api(tags = "注册验证")
@RestController
public class SysRegisterController extends BaseController {

	@Autowired
	private SysRegisterService registerService;

	@Autowired
	private ISysConfigService configService;

	@ApiOperation(value = "用户注册", notes = "用户注册")
	@PostMapping("/register")
	public AjaxResult register(@RequestBody RegisterBody user) {
		if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
			return error("当前系统没有开启注册功能！");
		}
		String msg = registerService.register(user);
		return StringUtils.isEmpty(msg) ? success() : error(msg);
	}
}