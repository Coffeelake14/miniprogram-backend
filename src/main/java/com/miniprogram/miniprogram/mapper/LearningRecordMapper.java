package com.miniprogram.miniprogram.mapper;

import com.miniprogram.miniprogram.entity.LearningRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LearningRecordMapper {

    @Insert("INSERT INTO learning_record(user_id, course_id, duration, start_position, end_position) " +
            "VALUES(#{userId}, #{courseId}, #{duration}, #{startPosition}, #{endPosition})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LearningRecord record);

    @Select("SELECT * FROM learning_record WHERE user_id = #{userId} AND course_id = #{courseId} " +
            "ORDER BY create_time DESC LIMIT #{limit}")
    List<LearningRecord> findRecent(@Param("userId") Long userId, @Param("courseId") Long courseId, @Param("limit") int limit);
}