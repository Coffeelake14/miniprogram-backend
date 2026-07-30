package com.miniprogram.miniprogram.controller;

import com.miniprogram.miniprogram.entity.Question;
import com.miniprogram.miniprogram.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/question")
public class AdminQuestionController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/list/{courseId}")
    public List<Question> list(@PathVariable Long courseId) {
        return questionMapper.findByCourseId(courseId);
    }

    @GetMapping("/{id}")
    public Question get(@PathVariable Long id) {
        return questionMapper.findById(id);
    }

    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody Question question) {
        questionMapper.insert(question);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", question.getId());
        return result;
    }

    @PutMapping("/update/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        questionMapper.update(question);
        return Map.of("success", true);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        questionMapper.deleteById(id);
        return Map.of("success", true);
    }
}