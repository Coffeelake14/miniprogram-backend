package com.miniprogram.miniprogram.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String openid;
    private String nickname;
    private String avatar;
    private String role;
    private Date createTime;
    private String studentId;
    private Integer isBound;
}