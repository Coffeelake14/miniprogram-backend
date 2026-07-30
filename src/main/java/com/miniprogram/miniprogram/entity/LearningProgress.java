package com.miniprogram.miniprogram.entity;

import java.util.Date;

public class LearningProgress {
    private Long id;
    private Long userId;
    private Long courseId;
    private Integer watchedDuration;
    private Integer totalDuration;
    private Integer progressPercent;
    private Integer isCompleted;
    private Integer lastPosition;
    private Date updateTime;

    // ========== getter / setter ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Integer getWatchedDuration() { return watchedDuration; }
    public void setWatchedDuration(Integer watchedDuration) { this.watchedDuration = watchedDuration; }

    public Integer getTotalDuration() { return totalDuration; }
    public void setTotalDuration(Integer totalDuration) { this.totalDuration = totalDuration; }

    public Integer getProgressPercent() { return progressPercent; }
    public void setProgressPercent(Integer progressPercent) { this.progressPercent = progressPercent; }

    public Integer getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Integer isCompleted) { this.isCompleted = isCompleted; }

    public Integer getLastPosition() { return lastPosition; }
    public void setLastPosition(Integer lastPosition) { this.lastPosition = lastPosition; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}