package com.hay.remote_youtube_controller.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hay.remote_youtube_controller.domain.Config;
import com.hay.remote_youtube_controller.domain.EventListener;
import com.hay.remote_youtube_controller.domain.Statistic;
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
    private Statistic statistic = new Statistic();
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
            public void newVideoAdded(Config config) {
                if (youTubePlayer == null) {
                    return;
                }
                if (!config.isWantPlay()) {
                    youTubePlayer.pause();
                } else {
                    youTubePlayer.cueVideo(config.getVideoLink(), config.getVideoTime());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                statistic.setVideoTime(youTubePlayer != null ? youTubePlayer.getCurrentTimeMillis() : 0);
                statistic.setTimestamp(System.currentTimeMillis());
                firebaseRepo.sendStatistic(statistic);
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
        if (!wasRestored) {
            youTubePlayer.setFullscreen(true);
            youTubePlayer.setShowFullscreenButton(false);
        }
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        Config config = firebaseRepo.getLatestConfig();
        if (config != null) {
            if (!config.isWantPlay()) {
                youTubePlayer.pause();
            } else {
                youTubePlayer.cueVideo(config.getVideoLink(), config.getVideoTime());
            }
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
            statistic.setPlaying(true);
            statistic.setStopped(false);
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            Log.d(TAG, "onPaused: ");
            statistic.setPlaying(false);
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            Log.d(TAG, "onStopped: ");
            statistic.setPlaying(false);
            statistic.setStopped(true);
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
            Log.d(TAG, "onBuffering: " + b);
            statistic.setBuffering(b);
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
            statistic.setLoading(true);
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
            Log.d(TAG, "onLoaded: " + s);
            if (youTubePlayer != null) {
                youTubePlayer.play();
            }
            statistic.setLoading(false);
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
            Log.d(TAG, "onAdStarted: ");
            statistic.setAdStarted(true);
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
            Log.d(TAG, "onVideoStarted: ");
            statistic.setAdStarted(false);
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
            Log.d(TAG, "onVideoEnded: ");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
            Log.d(TAG, "onError: " + errorReason);
        }
    }

}
