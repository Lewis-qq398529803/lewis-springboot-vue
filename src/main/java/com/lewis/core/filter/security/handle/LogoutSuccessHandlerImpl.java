package com.lewis.core.filter.security.handle;

import com.alibaba.fastjson.JSON;
import com.lewis.core.constant.Constants;
import com.lewis.core.constant.HttpStatus;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.domain.model.LoginUser;
import com.lewis.core.utils.ServletUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.mvc.framework.manager.AsyncManager;
import com.lewis.mvc.framework.manager.factory.AsyncFactory;
import com.lewis.mvc.framework.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author Lewis
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private ITokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(BaseResult.fail(HttpStatus.SUCCESS, "退出成功")));
    }
}
