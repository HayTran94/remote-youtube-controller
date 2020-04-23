package com.hay.remote_youtube_controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Statistic {
    public boolean isLoading;
    public boolean isBuffering;
    public boolean isPlaying;
    public boolean isStopped;
    public boolean isAdStarted;
    public String videoLink;
    public String videoName;
    public int videoTime;
    public long timestamp;

    public Statistic() {
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isBuffering() {
        return isBuffering;
    }

    public void setBuffering(boolean buffering) {
        isBuffering = buffering;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public boolean isAdStarted() {
        return isAdStarted;
    }

    public void setAdStarted(boolean adStarted) {
        isAdStarted = adStarted;
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

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
