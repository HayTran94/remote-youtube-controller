package com.hay.remote_youtube_controller.domain;

public interface EventListener {
    void onPlayStatusChanged(boolean wantPlay);
    void onGetUpcomingVideo(Video video);
}
