package com.hay.remote_youtube_controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Video {
    public String id;
    public String link;
    public int duration;
    public int categoryId;
    public int sessionId;
    public String isPlayed_playTimes_addTime;
    public int isPlayed;
    public int playTime;
    public long addTime;
    public long updateTime;

    public Video() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getIsPlayed_playTimes_addTime() {
        return isPlayed_playTimes_addTime;
    }

    public void setIsPlayed_playTimes_addTime(String isPlayed_playTimes_addTime) {
        this.isPlayed_playTimes_addTime = isPlayed_playTimes_addTime;
    }

    public Video(int isPlayed) {
        this.isPlayed = isPlayed;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
