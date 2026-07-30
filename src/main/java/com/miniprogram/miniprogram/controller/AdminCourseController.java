package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.entity.Course;
import com.miniprogram.miniprogram.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/course")
public class AdminCourseController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/list")
    public List<Course> list() {
        return courseMapper.findAll();
    }

    @GetMapping("/{id}")
    public Course get(@PathVariable Long id) {
        return courseMapper.findById(id);
    }

    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody Course course) {
        courseMapper.insert(course);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", course.getId());
        return result;
    }

    @PutMapping("/update/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Course course) {
        // 先查询是否存在
        Course existing = courseMapper.findById(id);
        if (existing == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "课程不存在");
            return result;
        }
        // 设置ID并更新
        course.setId(id);
        int rows = courseMapper.update(course);
        Map<String, Object> result = new HashMap<>();
        result.put("success", rows > 0);
        result.put("message", rows > 0 ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        int rows = courseMapper.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", rows > 0);
        result.put("message", rows > 0 ? "删除成功" : "删除失败");
        return result;
    }
}