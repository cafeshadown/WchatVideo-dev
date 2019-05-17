package com.video.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "users_fans")
@Data
public class UsersFans {
    @Id
    String id;

    /**
     * 用户
     */
    @Column(name = "user_id")
    String userId;

    /**
     * 粉丝
     */
    @Column(name = "fan_id")
    String fanId;


}