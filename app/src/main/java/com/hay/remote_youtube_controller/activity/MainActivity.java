package com.hay.remote_youtube_controller.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hay.remote_youtube_controller.domain.EventListener;
import com.hay.remote_youtube_controller.domain.Statistics;
import com.hay.remote_youtube_controller.domain.Video;
import com.hay.remote_youtube_controller.repository.FirebaseRepo;
import com.hay.remote_youtube_controller.utils.Constant;
import com.hay.remote_youtube_controller.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private YouTubePlayer youTubePlayer;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private FirebaseRepo firebaseRepo = FirebaseRepo.getInstance();
    private Statistics statistics = new Statistics();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final YouTubePlayerView youtubePlayerView = findViewById(R.id.youtubePlayerView);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
        youtubePlayerView.initialize(Constant.API_KEY, this);
        firebaseRepo.setEventListener(new EventListener() {
            @Override
            public void onPlayStatusChanged(boolean wantPlay) {
                if (youTubePlayer == null) {
                    return;
                }
                if (!wantPlay) {
                    youTubePlayer.pause();
                } else {
                    youTubePlayer.play();
                }
            }

            @Override
            public void onGetUpcomingVideo(Video video) {
                if (youTubePlayer == null) {
                    return;
                }
                youTubePlayer.cueVideo(video.getLink());
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                statistics.setPlaying(youTubePlayer != null ? youTubePlayer.isPlaying() : false);
                statistics.setPlayedTime(youTubePlayer != null ? youTubePlayer.getCurrentTimeMillis() : 0);
                statistics.setDuration(youTubePlayer != null ? youTubePlayer.getDurationMillis() : 0);
                statistics.setUpdateTime(System.currentTimeMillis());
                firebaseRepo.sendStatistic(statistics);
            }
        }, 0, 5*1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.purge();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    //-------------------------------------------------------------------------------------------------
    // ---------------------- Implement initiation youtube---------------------------------------------

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess, wasRestored: " + wasRestored);
        this.youTubePlayer = player;
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        boolean wantPlay = firebaseRepo.isWantPlay();
        Video video = firebaseRepo.getCurrentVideo();
        if (!wantPlay) {
            youTubePlayer.pause();
        } else if (video != null) {
            youTubePlayer.cueVideo(video.getLink());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        Log.d(TAG, "onInitializationFailure: " + errorReason);
    }

    //-------------------------------------------------------------------------------------------------
    // ---------------------- Implement playback event---------------------------------------------

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            Log.d(TAG, "onPlaying: ");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            Log.d(TAG, "onPaused: ");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            Log.d(TAG, "onStopped: ");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
            Log.d(TAG, "onBuffering: " + b);
            statistics.setBuffering(b);
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
            Log.d(TAG, "onSeekTo: " + i);
        }
    }


    //-------------------------------------------------------------------------------------------------
    // ---------------------- Implement player state event---------------------------------------------


    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
            Log.d(TAG, "onLoading: ");
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
            Log.d(TAG, "onLoaded: " + s);
            if (youTubePlayer != null) {
                youTubePlayer.play();
            }
            statistics.setVideoLink(s);
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
            Log.d(TAG, "onAdStarted: ");
            statistics.setPlayingAds(true);
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
            Log.d(TAG, "onVideoStarted: ");
            statistics.setPlayingAds(false);
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
            Log.d(TAG, "onVideoEnded: ");
            firebaseRepo.updatePlayedVideo(FirebaseRepo.getInstance().getCurrentVideo());
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
            Log.d(TAG, "onError: " + errorReason);
        }
    }

}
