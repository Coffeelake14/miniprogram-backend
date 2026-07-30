package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.entity.Course;
import com.miniprogram.miniprogram.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/list")
    public List<Course> list() {
        return courseMapper.findPublished();
    }

    @GetMapping("/detail/{id}")
    public Course detail(@PathVariable Long id) {
        courseMapper.incrementViewCount(id);
        return courseMapper.findById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Course> getByCategory(@PathVariable Long categoryId) {
        return courseMapper.findByCategory(categoryId);
    }
}