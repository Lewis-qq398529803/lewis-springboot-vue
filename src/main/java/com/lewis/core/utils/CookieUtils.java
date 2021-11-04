package com.lewis.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * CookieUtils包含功能：
 * 得到Cookie的值，是否解编码
 * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效
 * 设置Cookie的值 在指定时间内生效
 * 删除Cookie 带cookie域名
 * 设置Cookie的值，并使其在指定时间内生效
 * 得到cookie的域名
 * 判断是否是一个IP
 *
 * @author Lewis
 */
@Slf4j
public final class CookieUtils {

	/**
	 * 得到Cookie的值, 不编码
	 *
	 * @param request
	 * @param cookieName
	 * @return String Cookie的值
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return getCookieValue(request, cookieName, false);
	}

	/**
	 * 得到Cookie的值
	 *
	 * @param request
	 * @param cookieName
	 * @param isDecoder
	 * @return String Cookie的值
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (Cookie cookie : cookieList) {
				if (cookie.getName().equals(cookieName)) {
					if (isDecoder) {
						retValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} else {
						retValue = cookie.getValue();
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	/**
	 * 得到Cookie的值
	 *
	 * @param request
	 * @param cookieName
	 * @param encodeString
	 * @return String Cookie的值
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (Cookie cookie : cookieList) {
				if (cookie.getName().equals(cookieName)) {
					retValue = URLDecoder.decode(cookie.getValue(), encodeString);
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	/**
	 * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
		setCookie(request, response, cookieName, cookieValue, -1);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效,但不编码
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxAge
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxAge) {
		setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
	}

	/**
	 * 设置Cookie的值 不设置生效时间,但编码
	 * 在服务器被创建，返回给客户端，并且保存客户端
	 * 如果设置了SETMAXAGE(int seconds)，会把cookie保存在客户端的硬盘中
	 * 如果没有设置，会默认把cookie保存在浏览器的内存中
	 * 一旦设置setPath()：只能通过设置的路径才能获取到当前的cookie信息
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param isEncode
	 */
	public static void setCookie(
			HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, boolean isEncode
	) {
		setCookie(request, response, cookieName, cookieValue, -1, isEncode);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效, 编码参数
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxAge
	 * @param isEncode
	 */
	public static void setCookie(
			HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxAge, boolean isEncode
	) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
	}

	/**
	 * 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxAge
	 * @param encodeString
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
								 String cookieValue, int cookieMaxAge, String encodeString) {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
	}

	/**
	 * 删除Cookie带cookie域名
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		doSetCookie(request, response, cookieName, null, -1, false);
	}


	/**
	 * 设置Cookie的值，并使其在指定时间内生效
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxAge cookie生效的最大秒数
	 * @param isEncode
	 */
	private static void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxAge, boolean isEncode) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else if (isEncode) {
				cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxAge > 0) {
				cookie.setMaxAge(cookieMaxAge);
			}
			// 设置域名的cookie
			if (null != request) {
				String domainName = getDomainName(request);
				log.info("========== domainName: {} ==========" + domainName);
				String localhost = "localhost";
				if (!localhost.equals(domainName)) {
					cookie.setDomain(domainName);
				}
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置Cookie的值，并使其在指定时间内生效
	 *
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxAge cookie生效的最大秒数
	 * @param encodeString
	 */
	private static void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxAge, String encodeString) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else {
				cookieValue = URLEncoder.encode(cookieValue, encodeString);
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxAge > 0) {
				cookie.setMaxAge(cookieMaxAge);
			}
			// 设置域名的cookie
			if (null != request) {
				String domainName = getDomainName(request);
				log.info("========== domainName: {} ==========" + domainName);
				String localhost = "localhost";
				if (!localhost.equals(domainName)) {
					cookie.setDomain(domainName);
				}
			}
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到cookie的域名
	 *
	 * @param request
	 * @return String cookie的域名
	 */
	private static String getDomainName(HttpServletRequest request) {
		String domainName = null;

		String serverName = request.getRequestURL().toString();
		if ("".equals(serverName)) {
			domainName = "";
		} else {
			serverName = serverName.toLowerCase();
			serverName = serverName.substring(7);
			final int end = serverName.indexOf("/");
			serverName = serverName.substring(0, end);
			String index = ":";
			if (serverName.indexOf(index) > 0) {
				String[] ary = serverName.split("\\:");
				serverName = ary[0];
			}

			final String[] domains = serverName.split("\\.");
			int len = domains.length;
			int lenCompare = 3;
			if (len > lenCompare && !isIp(serverName)) {
				// www.xxx.com.cn
				domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
			} else if (len <= lenCompare && len > 1) {
				// xxx.com or xxx.cn
				domainName = "." + domains[len - 2] + "." + domains[len - 1];
			} else {
				domainName = serverName;
			}
		}
		return domainName;
	}

	/**
	 * 去掉IP字符串前后所有的空格
	 *
	 * @param ip
	 * @return String IP字符串
	 */
	public static String trimSpaces(String ip) {
		String space = " ";
		while (ip.startsWith(space)) {
			ip = ip.substring(1, ip.length()).trim();
		}
		while (ip.endsWith(space)) {
			ip = ip.substring(0, ip.length() - 1).trim();
		}
		return ip;
	}

	/**
	 * 判断是否是一个IP
	 *
	 * @param ip
	 * @return boolean
	 */
	public static boolean isIp(String ip) {
		boolean result = false;
		ip = trimSpaces(ip);
		if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			String[] s = ip.split("\\.");
			if (Integer.parseInt(s[0]) < 255) {
				if (Integer.parseInt(s[1]) < 255) {
					if (Integer.parseInt(s[2]) < 255) {
						if (Integer.parseInt(s[3]) < 255) {
							result = true;
						}
					}
				}
			}
		}
		return result;
	}
}

