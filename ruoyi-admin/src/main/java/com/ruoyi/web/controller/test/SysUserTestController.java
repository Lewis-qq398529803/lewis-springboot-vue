package com.ruoyi.web.controller.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.web.controller.test.entity.UserEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;

/**
 * swagger 用户测试方法
 *
 * @author taozi
 */
@RestController
@RequestMapping("/test/user")
public class SysUserTestController extends BaseController {
    private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();

    {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("/list")
    public AjaxResult userList() {
        List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
        return AjaxResult.success(userList);
    }

    /**
     * 获取用户详细
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public AjaxResult getUser(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            return AjaxResult.success(users.get(userId));
        } else {
            return AjaxResult.error("用户不存在");
        }
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(UserEntity user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
            return AjaxResult.error("用户ID不能为空");
        }
        return AjaxResult.success(users.put(user.getUserId(), user));
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @PutMapping("/update")
    public AjaxResult update(UserEntity user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
            return AjaxResult.error("用户ID不能为空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId())) {
            return AjaxResult.error("用户不存在");
        }
        users.remove(user.getUserId());
        return AjaxResult.success(users.put(user.getUserId(), user));
    }

    /**
     * 删除用户信息
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public AjaxResult delete(@PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return AjaxResult.success();
        } else {
            return AjaxResult.error("用户不存在");
        }
    }
}

