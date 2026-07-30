package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.entity.AnswerRecord;
import com.miniprogram.miniprogram.entity.Question;
import com.miniprogram.miniprogram.mapper.AnswerRecordMapper;
import com.miniprogram.miniprogram.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    // 获取课程的所有题目（含用户答题状态）
    @GetMapping("/list/{courseId}")
    public Map<String, Object> getQuestions(@PathVariable Long courseId, @RequestParam Long userId) {
        List<Question> questions = questionMapper.findQuestionsWithAnswer(userId, courseId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", questions);
        return result;
    }

    // 提交答案
    @PostMapping("/submit")
    public Map<String, Object> submitAnswer(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Long questionId = Long.valueOf(params.get("questionId").toString());
        String userAnswer = params.get("userAnswer").toString();

        Question question = questionMapper.findById(questionId);
        if (question == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "题目不存在");
            return result;
        }

        boolean isCorrect = question.getAnswer().equalsIgnoreCase(userAnswer);

        AnswerRecord record = answerRecordMapper.findByUserAndQuestion(userId, questionId);
        if (record == null) {
            record = new AnswerRecord();
            record.setUserId(userId);
            record.setQuestionId(questionId);
            record.setUserAnswer(userAnswer);
            record.setIsCorrect(isCorrect ? 1 : 0);
            answerRecordMapper.insert(record);
        } else {
            record.setUserAnswer(userAnswer);
            record.setIsCorrect(isCorrect ? 1 : 0);
            answerRecordMapper.update(record);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("isCorrect", isCorrect);
        result.put("correctAnswer", question.getAnswer());
        result.put("explanation", question.getExplanation());
        return result;
    }

    // 获取答题统计
    @GetMapping("/stats/{userId}")
    public Map<String, Object> getStats(@PathVariable Long userId) {
        int total = answerRecordMapper.countTotal(userId);
        int correct = answerRecordMapper.countCorrect(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("correct", correct);
        result.put("rate", total > 0 ? (correct * 100 / total) : 0);
        return result;
    }
}