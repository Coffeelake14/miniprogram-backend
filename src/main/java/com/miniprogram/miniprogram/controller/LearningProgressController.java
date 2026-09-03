package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.entity.Course;
import com.miniprogram.miniprogram.entity.LearningProgress;
import com.miniprogram.miniprogram.entity.LearningRecord;
import com.miniprogram.miniprogram.mapper.CourseMapper;
import com.miniprogram.miniprogram.mapper.LearningProgressMapper;
import com.miniprogram.miniprogram.mapper.LearningRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/learning")
public class LearningProgressController {

    @Autowired
    private LearningProgressMapper progressMapper;

    @Autowired
    private LearningRecordMapper recordMapper;

    @Autowired
    private CourseMapper courseMapper;

    // 上报学习进度
    @PostMapping("/report")
    public Map<String, Object> reportProgress(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Long courseId = Long.valueOf(params.get("courseId").toString());
        Integer duration = Integer.valueOf(params.get("duration").toString());

        // 获取课程信息
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "课程不存在");
            return result;
        }

        // 查询或创建进度记录
        LearningProgress progress = progressMapper.findByUserAndCourse(userId, courseId);
        if (progress == null) {
            progress = new LearningProgress();
            progress.setUserId(userId);
            progress.setCourseId(courseId);
            progress.setTotalDuration(course.getDuration());
            progress.setWatchedDuration(0);
            progress.setProgressPercent(0);
            progress.setIsCompleted(0);
            progress.setLastPosition(0);
            progressMapper.insert(progress);
        }

        // 记录上报前的进度位置，避免后续覆盖后丢失
        int oldPosition = progress.getLastPosition() != null ? progress.getLastPosition() : 0;

        // 计算本次新增时长
        int newDuration = duration - oldPosition;
        if (newDuration < 0) newDuration = 0;
        if (newDuration > 60) newDuration = 60; // 单次最多上报60秒

        int watched = progress.getWatchedDuration() + newDuration;
        int total = progress.getTotalDuration() != null ? progress.getTotalDuration() : 1;
        int percent = Math.min((watched * 100) / total, 100);
        int completed = percent >= 90 ? 1 : 0;

        // 更新进度
        progress.setWatchedDuration(watched);
        progress.setProgressPercent(percent);
        progress.setIsCompleted(completed);
        progress.setLastPosition(duration);
        progressMapper.update(progress);

        // 记录本次学习记录
        LearningRecord record = new LearningRecord();
        record.setUserId(userId);
        record.setCourseId(courseId);
        record.setDuration(newDuration);
        record.setStartPosition(oldPosition);
        record.setEndPosition(duration);
        recordMapper.insert(record);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("progress", percent);
        result.put("isCompleted", completed == 1);
        return result;
    }

    // 获取用户学习进度
    @GetMapping("/progress/{userId}/{courseId}")
    public Map<String, Object> getProgress(@PathVariable Long userId, @PathVariable Long courseId) {
        LearningProgress progress = progressMapper.findByUserAndCourse(userId, courseId);
        Map<String, Object> result = new HashMap<>();
        if (progress == null) {
            result.put("success", true);
            result.put("watchedDuration", 0);
            result.put("progressPercent", 0);
            result.put("isCompleted", false);
            result.put("lastPosition", 0);
        } else {
            result.put("success", true);
            result.put("watchedDuration", progress.getWatchedDuration());
            result.put("progressPercent", progress.getProgressPercent());
            result.put("isCompleted", progress.getIsCompleted() == 1);
            result.put("lastPosition", progress.getLastPosition());
        }
        return result;
    }

    // 获取用户所有课程进度
    @GetMapping("/all/{userId}")
    public List<LearningProgress> getAllProgress(@PathVariable Long userId) {
        return progressMapper.findByUserId(userId);
    }

    // 获取学习统计
    @GetMapping("/stats/{userId}")
    public Map<String, Object> getStats(@PathVariable Long userId) {
        int completed = progressMapper.countCompleted(userId);
        Integer totalTime = progressMapper.totalLearningTime(userId);
        if (totalTime == null) totalTime = 0;

        Map<String, Object> result = new HashMap<>();
        result.put("completedCourses", completed);
        result.put("totalLearningTime", totalTime);
        result.put("totalLearningHours", String.format("%.1f", totalTime / 3600.0));
        return result;
    }
}
