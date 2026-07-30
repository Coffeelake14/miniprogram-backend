package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.entity.User;
import com.miniprogram.miniprogram.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    // 1. 微信登录接口
    @PostMapping("/wechat/login")
    public Map<String, Object> wechatLogin(@RequestBody Map<String, String> params) {
        String openid = params.get("openid");
        String nickname = params.get("nickname");
        String avatar = params.get("avatar");

        // 空值处理
        if (openid == null || openid.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "openid不能为空");
            return result;
        }

        User user = userMapper.findByOpenid(openid);

        if (user == null) {
            // 新用户：注册
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname != null ? nickname : "");
            user.setAvatar(avatar != null ? avatar : "");
            userMapper.insert(user);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("need_bind", true);
            result.put("userId", user.getId());
            result.put("message", "请绑定学号");
            return result;
        } else {
            // 老用户：更新昵称和头像
            if (nickname != null && !nickname.isEmpty()) {
                user.setNickname(nickname);
            }
            if (avatar != null && !avatar.isEmpty()) {
                user.setAvatar(avatar);
            }
            userMapper.update(user);

            // 重新查询获取最新的绑定状态
            User freshUser = userMapper.findByOpenid(openid);

            // 处理 is_bound 为 null 的情况
            Integer isBound = freshUser.getIsBound();
            boolean bound = (isBound != null && isBound == 1);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("need_bind", !bound);
            result.put("is_bound", bound);
            result.put("userId", freshUser.getId());
            result.put("role", freshUser.getRole() != null ? freshUser.getRole() : "user");
            return result;
        }
    }

    // 2. 学号绑定接口
    @PostMapping("/bind/student")
    public Map<String, Object> bindStudent(@RequestBody Map<String, String> params) {
        String openid = params.get("openid");
        String studentId = params.get("studentId");

        if (openid == null || openid.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "openid不能为空");
            return result;
        }

        if (studentId == null || studentId.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "学号不能为空");
            return result;
        }

        if (!studentId.matches("\\d{5,20}")) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "学号必须是5-20位数字");
            return result;
        }

        // 检查学号是否已被其他微信绑定
        User existing = userMapper.findByStudentId(studentId);
        if (existing != null && !existing.getOpenid().equals(openid)) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "该学号已被其他微信绑定");
            return result;
        }

        int rows = userMapper.bindStudent(openid, studentId);

        Map<String, Object> result = new HashMap<>();
        if (rows > 0) {
            result.put("success", true);
            result.put("message", "绑定成功");
        } else {
            result.put("success", false);
            result.put("message", "绑定失败，请重试");
        }
        return result;
    }
}