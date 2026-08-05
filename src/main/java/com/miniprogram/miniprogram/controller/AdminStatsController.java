package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/api/stats")
public class AdminStatsController {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerRecordMapper answerRecordMapper;
    @Autowired
    private LearningProgressMapper progressMapper;

    // 总览统计
    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> stats = new HashMap<>();

        // 课程统计
        List<?> courses = courseMapper.findAll();
        stats.put("totalCourses", courses.size());

        // 总浏览量
        int totalViews = 0;
        for (Object c : courses) {
            try {
                totalViews += (int) c.getClass().getMethod("getViewCount").invoke(c);
            } catch (Exception ignored) {}
        }
        stats.put("totalViews", totalViews);

        // 用户统计
        stats.put("totalUsers", userMapper.countAll());

        // 题目统计
        stats.put("totalQuestions", questionMapper.countAll());

        // 答题统计
        stats.put("totalAnswers", answerRecordMapper.countAllAnswers());
        stats.put("correctAnswers", answerRecordMapper.countAllCorrect());

        // 学习进度统计
        stats.put("totalLearningRecords", progressMapper.countAllProgress());

        return stats;
    }

    // 课程浏览量排行
    @GetMapping("/course-views")
    public List<Map<String, Object>> courseViews() {
        List<?> courses = courseMapper.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object c : courses) {
            try {
                Map<String, Object> item = new HashMap<>();
                item.put("id", c.getClass().getMethod("getId").invoke(c));
                item.put("title", c.getClass().getMethod("getTitle").invoke(c));
                item.put("viewCount", c.getClass().getMethod("getViewCount").invoke(c));
                result.add(item);
            } catch (Exception ignored) {}
        }
        result.sort((a, b) -> Integer.compare(
                (int) b.get("viewCount"), (int) a.get("viewCount")));
        return result;
    }

    // 用户列表
    @GetMapping("/users")
    public List<Map<String, Object>> users() {
        return userMapper.findAllUsers();
    }
}
