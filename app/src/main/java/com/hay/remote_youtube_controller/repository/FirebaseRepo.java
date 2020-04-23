package com.hay.remote_youtube_controller.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hay.remote_youtube_controller.domain.Config;
import com.hay.remote_youtube_controller.domain.EventListener;
import com.hay.remote_youtube_controller.domain.Statistic;

public class FirebaseRepo {

    private static FirebaseRepo instance;
    private EventListener eventListener;
    private DatabaseReference db;
    private Config config;

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

    public Config getLatestConfig() {
        return this.config;
    }

    private void setUp() {
        db.child("config").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Config c = dataSnapshot.getValue(Config.class);
                if (eventListener != null) {
                    eventListener.newVideoAdded(c);
                }
                config = c;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendStatistic(Statistic statistic) {
        db.child("current").setValue(statistic);
    }

}
