package com.hay.remote_youtube_controller.domain;

public interface EventListener {
    void onPlayStatusChanged(boolean wantPlay);
    void onUpdatePlayedVideo();
    void onGetUpcomingVideo(Video video);
}
