package com.hay.remote_youtube_controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Statistics {
    public String videoLink;
    public String videoName;
    public int duration;
    public boolean isPlaying;
    public boolean isBuffering;
    public boolean isPlayingAds;
    public int playedTime;
    public long updateTime;

    public Statistics() {
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isBuffering() {
        return isBuffering;
    }

    public void setBuffering(boolean buffering) {
        isBuffering = buffering;
    }

    public boolean isPlayingAds() {
        return isPlayingAds;
    }

    public void setPlayingAds(boolean playingAds) {
        isPlayingAds = playingAds;
    }

    public int getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(int playedTime) {
        this.playedTime = playedTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
