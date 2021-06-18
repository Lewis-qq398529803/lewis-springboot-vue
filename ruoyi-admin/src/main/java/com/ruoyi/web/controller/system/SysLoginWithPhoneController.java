package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.AliSendSmsConfig;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.CommonResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.AliSendSmsUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 手机验证码登录controller
 *
 * @author chenyitao - 2021年6月17日, 017 - 14:14:21
 */
@Api(tags = "手机验证码登录")
@RestController
@RequestMapping("/api")
public class SysLoginWithPhoneController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @GetMapping("/getSmsCode")
    @ApiOperation("获取手机验证码")
    public CommonResult getSmsCode(String phone) {
        //随机生成六位数验证码
        int smsCode = (int) (Math.random() * 1000000);
        //redis缓存
        redisCache.setCacheObject("smsCode:" + phone, smsCode, 60 * 5, TimeUnit.SECONDS);
        //实体封装
        AliSendSmsConfig aliSendSmsConfig = new AliSendSmsConfig();
        aliSendSmsConfig.setPhoneNumbers(phone);
        aliSendSmsConfig.setTemplateParam("{\"code\":" + smsCode + "}");
        aliSendSmsConfig.setTemplateCode("SMS_217850728");
        //发送验证码
        AliSendSmsUtils.sendSmsMessage(aliSendSmsConfig);
        //返回验证码
        return CommonResult.ok().setData(redisCache.getCacheObject("smsCode:" + phone));
    }

    @PostMapping("/loginWithPhone")
    @ApiOperation("手机验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "smsCode", value = "验证码", required = true),
            @ApiImplicitParam(name = "userType", value = "用户类型", required = true)
    })
    public CommonResult loginWithPhone(@RequestBody JSONObject jsonObject) {
        String phone = jsonObject.getString("phone");
        String smsCode = jsonObject.getString("smsCode");
        //用户类型
        String userType = jsonObject.getString("userType");
        Integer smsCode_t = redisCache.getCacheObject("smsCode:" + phone);
        //验证码验证
        if (!StringUtils.equals(smsCode, smsCode_t + "")) {
            return CommonResult.fail().setMsg("验证码错误");
        }
        //根据手机号以及用户类型查询账号密码
        SysUser sysUser = new SysUser();
        sysUser.setPhonenumber(phone);
        sysUser.setUserType(userType);
        List<SysUser> sysUsers = sysUserService.selectUserList(sysUser);
        //手机号唯一验证
        if (sysUsers.size() > 1) {
            return CommonResult.fail().setMsg("该手机号存在异常");
        } else if (sysUsers.size() == 1) {
            //存在用户，直接返回token
            SysUser sysUser1 = sysUsers.get(0);
            //实体封装
            LoginUser loginUser = new LoginUser();
            loginUser.setUser(sysUser1);
            loginUser.setPermissions(getPermissionsSet());
            //获取token
            return CommonResult.ok().setData(tokenService.createToken(loginUser));
        } else {
            //不存在，注册，后返回token
            SysUser sysUser1 = new SysUser();
            sysUser1.setPhonenumber(phone);
            sysUser1.setUserName(phone);
            sysUser1.setNickName(phone);
            sysUser1.setCreateTime(DateUtils.getNowDate());
            sysUser1.setCreateBy(phone + "");
            sysUser1.setRemark("用户手机验证码注册");
            sysUser1.setUserType(userType);
            if (userService.insertUser(sysUser1) != 1) {
                CommonResult.fail().setMsg("用户未创建成功");
            }
            //实体封装
            LoginUser loginUser = new LoginUser();
            loginUser.setUser(sysUser1);
            loginUser.setPermissions(getPermissionsSet());
            //获取token
            return CommonResult.ok().setData(tokenService.createToken(loginUser));
        }
    }

    //获取权限列表
    public Set<String> getPermissionsSet() {
        Set<String> permissions = new HashSet<>();
        permissions.add("block:manager:list");
        permissions.add("block:manager:add");
        permissions.add("block:manager:edit");
        permissions.add("block:manager:remove");
        permissions.add("bill:manager:list");
        permissions.add("bill:manager:add");
        permissions.add("bill:manager:edit");
        permissions.add("bill:manager:remove");
        permissions.add("lease:manager:list");
        permissions.add("lease:manager:add");
        permissions.add("lease:manager:edit");
        permissions.add("lease:manager:remove");
        permissions.add("device:manager:list");
        permissions.add("device:manager:add");
        permissions.add("device:manager:edit");
        permissions.add("device:manager:remove");
        permissions.add("device:record:list");
        permissions.add("device:record:add");
        permissions.add("device:record:edit");
        permissions.add("device:record:remove");
        return permissions;
    }
}
