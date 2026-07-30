package com.miniprogram.miniprogram.mapper;

import com.miniprogram.miniprogram.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Select("SELECT * FROM question WHERE course_id = #{courseId} ORDER BY sort_order ASC, id ASC")
    List<Question> findByCourseId(Long courseId);

    @Select("SELECT * FROM question WHERE id = #{id}")
    Question findById(Long id);

    @Insert("INSERT INTO question(course_id, content, option_a, option_b, option_c, option_d, answer, explanation, sort_order) " +
            "VALUES(#{courseId}, #{content}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, #{answer}, #{explanation}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Question question);

    @Update("UPDATE question SET course_id=#{courseId}, content=#{content}, option_a=#{optionA}, option_b=#{optionB}, " +
            "option_c=#{optionC}, option_d=#{optionD}, answer=#{answer}, explanation=#{explanation}, sort_order=#{sortOrder} WHERE id=#{id}")
    int update(Question question);

    @Delete("DELETE FROM question WHERE id = #{id}")
    int deleteById(Long id);
}