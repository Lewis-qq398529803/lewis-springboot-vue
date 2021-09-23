package com.lewis.web.system;

import com.lewis.common.config.LewisConfig;
import com.lewis.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author Lewis
 */
@Api(tags = "首页")
@RestController
public class SysIndexController {

	@Autowired
	private LewisConfig taoZiConfig;

	@ApiOperation(value = "访问首页，提示语", notes = "访问首页，提示语")
	@RequestMapping("/")
	public String index() {
		return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", taoZiConfig.getName(), taoZiConfig.getVersion());
	}

}
