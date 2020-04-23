package com.hay.remote_youtube_controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Config {
    public int videoTime;
    public String videoLink;
    public boolean wantPlay;

    public Config() {
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public boolean isWantPlay() {
        return wantPlay;
    }

    public void setWantPlay(boolean wantPlay) {
        this.wantPlay = wantPlay;
    }
}
