package com.lewis.test.sysuser.entity;

import lombok.Data;

/**
 * @author Lewis - 2021年6月22日, 022 - 16:55:04
 */
@Data
public class UserEntity {

    private Integer userId;//用户ID

    private String username;//用户名称

    private String password;//用户密码

    private String mobile;//用户手机

    public UserEntity() {

    }

    public UserEntity(Integer userId, String username, String password, String mobile) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }
}
