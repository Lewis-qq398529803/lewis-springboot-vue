package com.taozi.web.controller.system;

import com.taozi.common.config.TaoZiConfig;
import com.taozi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author taozi
 */
@RestController
public class SysIndexController {

	/**
	 * 系统基础配置
	 */
	@Autowired
	private TaoZiConfig taoZiConfig;

	/**
	 * 访问首页，提示语
	 */
	@RequestMapping("/")
	public String index() {
		return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", taoZiConfig.getName(), taoZiConfig.getVersion());
	}

}
