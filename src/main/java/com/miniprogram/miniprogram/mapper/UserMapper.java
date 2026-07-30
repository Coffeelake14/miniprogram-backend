package com.miniprogram.miniprogram.mapper;

import com.miniprogram.miniprogram.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User findByOpenid(String openid);

    @Insert("INSERT INTO user (openid, nickname, avatar, role, is_bound) VALUES (#{openid}, #{nickname}, #{avatar}, 'user', 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET nickname = #{nickname}, avatar = #{avatar} WHERE id = #{id}")
    int update(User user);

    @Update("UPDATE user SET student_id = #{studentId}, is_bound = 1 WHERE openid = #{openid}")
    int bindStudent(@Param("openid") String openid, @Param("studentId") String studentId);

    @Select("SELECT * FROM user WHERE student_id = #{studentId} AND is_bound = 1")
    User findByStudentId(String studentId);
}
