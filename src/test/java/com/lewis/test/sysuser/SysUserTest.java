package com.lewis.test.sysuser;

import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.AjaxResult;
import com.lewis.core.utils.StringUtils;
import com.lewis.test.sysuser.entity.UserEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger 用户测试方法
 *
 * @author Lewis
 */
@RestController
@RequestMapping("/test/user")
public class SysUserTest extends BaseController {

    private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();

    {
        users.put(1, new UserEntity(1, "admin" , "admin123" , "15888888888"));
        users.put(2, new UserEntity(2, "ry" , "admin123" , "15666666666"));
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

