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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerRecordMapper answerRecordMapper;

    // 获取课程所有题目（含用户答题状态）
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

    // 获取课程答题统计
    @GetMapping("/stats/{courseId}")
    public Map<String, Object> getStats(@PathVariable Long courseId, @RequestParam Long userId) {
        List<Question> questions = questionMapper.findByCourseId(courseId);
        List<AnswerRecord> records = answerRecordMapper.findByUserAndCourse(userId, courseId);

        int total = questions.size();
        int correct = 0;
        int score = 0;
        int maxScore = 0;

        Map<Long, AnswerRecord> recordMap = records.stream()
                .collect(Collectors.toMap(AnswerRecord::getQuestionId, r -> r));

        for (Question q : questions) {
            maxScore += q.getScore() != null ? q.getScore() : 1;
            AnswerRecord r = recordMap.get(q.getId());
            if (r != null && r.getIsCorrect() == 1) {
                correct++;
                score += q.getScore() != null ? q.getScore() : 1;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("total", total);
        result.put("correct", correct);
        result.put("score", score);
        result.put("maxScore", maxScore);
        result.put("percent", total > 0 ? (correct * 100 / total) : 0);
        return result;
    }

    // 获取用户答题统计（所有课程）
    @GetMapping("/stats/all/{userId}")
    public Map<String, Object> getAllStats(@PathVariable Long userId) {
        int total = answerRecordMapper.countTotal(userId);
        int correct = answerRecordMapper.countCorrect(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("total", total);
        result.put("correct", correct);
        result.put("rate", total > 0 ? (correct * 100 / total) : 0);
        return result;
    }
}