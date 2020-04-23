package com.hay.remote_youtube_controller.domain;

import com.hay.remote_youtube_controller.domain.Config;

public interface EventListener {
    void newVideoAdded(Config config);
}
