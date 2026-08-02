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

    @Select("SELECT q.*, a.user_answer, a.is_correct FROM question q " +
            "LEFT JOIN answer_record a ON q.id = a.question_id AND a.user_id = #{userId} " +
            "WHERE q.course_id = #{courseId} ORDER BY q.sort_order ASC")
    List<Question> findQuestionsWithAnswer(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Insert("INSERT INTO question(course_id, chapter, question_type, content, options, answer, score, sort_order) " +
            "VALUES(#{courseId}, #{chapter}, #{questionType}, #{content}, #{options}, #{answer}, #{score}, #{sortOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Question question);

    @Update("UPDATE question SET course_id=#{courseId}, chapter=#{chapter}, question_type=#{questionType}, " +
            "content=#{content}, options=#{options}, answer=#{answer}, score=#{score}, sort_order=#{sortOrder} WHERE id=#{id}")
    int update(Question question);

    @Delete("DELETE FROM question WHERE id = #{id}")
    int deleteById(Long id);
}