package com.miniprogram.miniprogram.entity;

import java.util.Date;

public class LearningRecord {
    private Long id;
    private Long userId;
    private Long courseId;
    private Integer duration;
    private Integer startPosition;
    private Integer endPosition;
    private Date createTime;

    // ========== getter / setter ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Integer getStartPosition() { return startPosition; }
    public void setStartPosition(Integer startPosition) { this.startPosition = startPosition; }

    public Integer getEndPosition() { return endPosition; }
    public void setEndPosition(Integer endPosition) { this.endPosition = endPosition; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}