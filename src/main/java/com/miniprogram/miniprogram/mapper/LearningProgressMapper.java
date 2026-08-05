package com.miniprogram.miniprogram.mapper;

import com.miniprogram.miniprogram.entity.LearningProgress;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LearningProgressMapper {

    @Select("SELECT * FROM learning_progress WHERE user_id = #{userId} AND course_id = #{courseId}")
    LearningProgress findByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Insert("INSERT INTO learning_progress(user_id, course_id, total_duration) " +
            "VALUES(#{userId}, #{courseId}, #{totalDuration})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LearningProgress progress);

    @Update("UPDATE learning_progress SET " +
            "watched_duration = #{watchedDuration}, " +
            "progress_percent = #{progressPercent}, " +
            "is_completed = #{isCompleted}, " +
            "last_position = #{lastPosition} " +
            "WHERE id = #{id}")
    int update(LearningProgress progress);

    @Select("SELECT COUNT(*) FROM learning_progress WHERE user_id = #{userId} AND is_completed = 1")
    int countCompleted(@Param("userId") Long userId);

    @Select("SELECT SUM(watched_duration) FROM learning_progress WHERE user_id = #{userId}")
    Integer totalLearningTime(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM learning_progress")
    int countAllProgress();

}