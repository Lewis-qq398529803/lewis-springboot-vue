package com.lewis.web.system;

import com.lewis.common.constant.Constants;
import com.lewis.common.core.domain.AjaxResult;
import com.lewis.common.core.domain.entity.SysMenu;
import com.lewis.common.core.domain.entity.SysUser;
import com.lewis.common.core.domain.model.LoginBody;
import com.lewis.common.core.domain.model.LoginUser;
import com.lewis.common.utils.ServletUtils;
import com.lewis.service.framework.ISysLoginService;
import com.lewis.service.framework.ISysPermissionService;
import com.lewis.service.framework.ITokenService;
import com.lewis.service.system.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author Lewis
 */
@Api(tags = "登录验证")
@RestController
public class SysLoginController {

    @Autowired
    private ISysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ITokenService tokenService;

    @ApiOperation(value = "登录方法", notes = "登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user" , user);
        ajax.put("roles" , roles);
        ajax.put("permissions" , permissions);
        return ajax;
    }

    @ApiOperation(value = "获取路由信息", notes = "获取路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}