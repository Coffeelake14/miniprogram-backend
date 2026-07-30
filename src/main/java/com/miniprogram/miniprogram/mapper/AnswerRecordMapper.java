package com.miniprogram.miniprogram.mapper;

import com.miniprogram.miniprogram.entity.AnswerRecord;
import com.miniprogram.miniprogram.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnswerRecordMapper {

    @Select("SELECT * FROM answer_record WHERE user_id = #{userId} AND question_id = #{questionId}")
    AnswerRecord findByUserAndQuestion(@Param("userId") Long userId, @Param("questionId") Long questionId);

    @Insert("INSERT INTO answer_record(user_id, question_id, user_answer, is_correct) " +
            "VALUES(#{userId}, #{questionId}, #{userAnswer}, #{isCorrect})")
    int insert(AnswerRecord record);

    @Update("UPDATE answer_record SET user_answer = #{userAnswer}, is_correct = #{isCorrect} WHERE id = #{id}")
    int update(AnswerRecord record);

    @Select("SELECT q.*, a.user_answer, a.is_correct FROM question q " +
            "LEFT JOIN answer_record a ON q.id = a.question_id AND a.user_id = #{userId} " +
            "WHERE q.course_id = #{courseId} ORDER BY q.sort_order ASC")
    List<Question> findQuestionsWithAnswer(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Select("SELECT COUNT(*) FROM answer_record WHERE user_id = #{userId} AND is_correct = 1")
    int countCorrect(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM answer_record WHERE user_id = #{userId}")
    int countTotal(@Param("userId") Long userId);
}