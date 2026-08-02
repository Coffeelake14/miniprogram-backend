package com.miniprogram.miniprogram.entity;

import java.util.Date;

public class Question {
    private Long id;
    private Long courseId;
    private String chapter;
    private String questionType;
    private String content;
    private String options;
    private String answer;
    private String explanation;      // 新增
    private Integer score;
    private Integer sortOrder;
    private Date createTime;

    // ===== getter / setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getExplanation() { return explanation; }    // 新增
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}