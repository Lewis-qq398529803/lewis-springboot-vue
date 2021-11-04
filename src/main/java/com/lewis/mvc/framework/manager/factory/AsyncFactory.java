package com.lewis.mvc.framework.manager.factory;

import com.lewis.core.constant.Constants;
import com.lewis.core.utils.ServletUtils;
import com.lewis.core.utils.SpringUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.ip.AddressUtils;
import com.lewis.core.utils.ip.IpUtils;
import com.lewis.mvc.system.entity.SysLogininfor;
import com.lewis.mvc.system.entity.SysOperLog;
import com.lewis.mvc.system.service.ISysLogininforService;
import com.lewis.mvc.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author Lewis
 */
@Slf4j
public class AsyncFactory {

	/**
	 * 记录登录信息
	 *
	 * @param username 用户名
	 * @param status   状态
	 * @param message  消息
	 * @param args     列表
	 * @return 任务task
	 */
	public static TimerTask recordLogininfor(final String username, final String status, final String message,
											 final Object... args) {
		final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
		final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
		return new TimerTask() {
			@Override
			public void run() {
				String address = AddressUtils.getRealAddressByIp(ip);
				StringBuilder s = new StringBuilder();
				s.append(getBlock(ip));
				s.append(address);
				s.append(getBlock(username));
				s.append(getBlock(status));
				s.append(getBlock(message));
				// 打印信息到日志
				log.info(s.toString(), args);
				// 获取客户端操作系统
				String os = userAgent.getOperatingSystem().getName();
				// 获取客户端浏览器
				String browser = userAgent.getBrowser().getName();
				// 封装对象
				SysLogininfor logininfor = new SysLogininfor();
				logininfor.setUserName(username);
				logininfor.setIpaddr(ip);
				logininfor.setLoginLocation(address);
				logininfor.setBrowser(browser);
				logininfor.setOs(os);
				logininfor.setMsg(message);
				// 日志状态
				if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)){
					logininfor.setStatus(Constants.SUCCESS);
				} else if (Constants.LOGIN_FAIL.equals(status)) {
					logininfor.setStatus(Constants.FAIL);
				}
				// 插入数据
				SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
			}
		};
	}

	/**
	 * 操作日志记录
	 *
	 * @param operLog 操作日志信息
	 * @return 任务task
	 */
	public static TimerTask recordOper(final SysOperLog operLog) {
		return new TimerTask() {
			@Override
			public void run() {
				// 远程查询操作地点
				operLog.setOperLocation(AddressUtils.getRealAddressByIp(operLog.getOperIp()));
				SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
			}
		};
	}

	public static String getBlock(Object msg) {
		if (msg == null) {
			msg = "";
		}
		return "[" + msg.toString() + "]";
	}
}
