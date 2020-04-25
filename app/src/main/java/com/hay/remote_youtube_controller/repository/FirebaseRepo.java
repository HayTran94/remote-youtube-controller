package com.hay.remote_youtube_controller.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Snapshot;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hay.remote_youtube_controller.domain.EventListener;
import com.hay.remote_youtube_controller.domain.Statistics;
import com.hay.remote_youtube_controller.domain.Video;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRepo {
    public static final String TAG = FirebaseRepo.class.getSimpleName();

    private static FirebaseRepo instance;
    private EventListener eventListener;
    private DatabaseReference db;
    private boolean wantPlay;
    private Video currentVideo;

    private FirebaseRepo() {
        db = FirebaseDatabase.getInstance().getReference("android_box");
        setUp();
    };

    public synchronized static FirebaseRepo getInstance() {
        if (instance == null) {
            instance = new FirebaseRepo();
        }
        return instance;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public boolean isWantPlay() {
        return wantPlay;
    }

    public void setWantPlay(boolean wantPlay) {
        this.wantPlay = wantPlay;
    }

    public Video getCurrentVideo() {
        return currentVideo;
    }

    public void setCurrentVideo(Video currentVideo) {
        this.currentVideo = currentVideo;
    }

    private void setUp() {
        db.child("config/wantPlay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: setUp");
                boolean wantPlay = dataSnapshot.getValue(boolean.class);
                if (eventListener != null) {
                    eventListener.onPlayStatusChanged(wantPlay);
                }
                setWantPlay(wantPlay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getUpcomingVideo();
    }

    public void getUpcomingVideo() {
        db.child("config/videos")
                .orderByChild("isPlayed_addTime")
                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: getUpcomingVideo");
                DataSnapshot object = dataSnapshot.getChildren().iterator().next();
                Video video = object.getValue(Video.class);
                video.setId(object.getKey());
                if (eventListener != null) {
                    eventListener.onGetUpcomingVideo(video);
                }
                setCurrentVideo(video);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updatePlayedVideo(final Video video) {
        Map<String, Object> map = new HashMap<>();
        map.put("isPlayed", true);
        map.put("isPlayed_addTime", "1_" + video.getAddTime());
        db.child("config/videos")
                .child(video.getId())
                .updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Log.d(TAG, "onComplete: updatePlayedVideo: " + databaseError);
                        if (eventListener != null) {
                            eventListener.onUpdatePlayedVideo();
                        }
                    }
                });
    }

    public void sendStatistic(Statistics statistics) {
        db.child("current").setValue(statistics);
    }

}
