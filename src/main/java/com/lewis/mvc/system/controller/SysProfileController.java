package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.config.LewisConfig;
import com.lewis.core.constant.UserConstants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.domain.entity.SysUser;
import com.lewis.core.base.domain.model.LoginUser;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.MapUtils;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.ServletUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.file.FileUploadUtils;
import com.lewis.mvc.framework.service.ITokenService;
import com.lewis.mvc.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author Lewis
 */
@Api(tags = "个人信息 业务处理")
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ITokenService tokenService;

    @ApiOperation(value = "个人信息", notes = "个人信息")
    @GetMapping
    public Object profile() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        BaseResult ok = BaseResult.ok(user);
        Map<String, Object> ajax = MapUtils.object2MapByReflect(ok);
        ajax.put("roleGroup" , userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup" , userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    @ApiOperation(value = "修改用户", notes = "修改用户")
    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult updateProfile(@RequestBody SysUser user) {
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return BaseResult.fail("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return BaseResult.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser sysUser = loginUser.getUser();
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        if (userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return BaseResult.ok();
        }
        return BaseResult.fail("修改个人信息异常，请联系管理员");
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @Log(title = "个人信息" , businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public BaseResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return BaseResult.fail("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return BaseResult.fail("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return BaseResult.ok();
        }
        return BaseResult.fail("修改密码异常，请联系管理员");
    }

    @ApiOperation(value = "头像上传", notes = "头像上传")
    @Log(title = "用户头像" , businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public Object avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(LewisConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                BaseResult ok = BaseResult.ok();
        Map<String, Object> ajax = MapUtils.object2MapByReflect(ok);
                ajax.put("imgUrl" , avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return BaseResult.fail("上传图片异常，请联系管理员");
    }
}
