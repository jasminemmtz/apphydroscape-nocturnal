package com.example.apphydroscape_nocturnal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoTourActivity extends AppCompatActivity {
    private VideoView videoView1;
    private VideoView videoView2;
    private VideoView videoView3;
    private VideoView videoView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_tour);
        Button backbutton = findViewById(R.id.backbtn);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoTourActivity.this, HomePageActivity.class));
            }
        });

        // Video 1
        VideoView videoView1 = findViewById(R.id.videoView1);
        Uri videoUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.penanaman);
        setupVideoView(videoView1, videoUri1);

        // Video 2
        VideoView videoView2 = findViewById(R.id.videoView2);
        Uri videoUri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.perawatan);
        setupVideoView(videoView2, videoUri2);

        // Video 3
        VideoView videoView3 = findViewById(R.id.videoView3);
        Uri videoUri3 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.panen);
        setupVideoView(videoView3, videoUri3);

        // Video 4
        VideoView videoView4 = findViewById(R.id.videoView4);
        Uri videoUri4 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.packing);
        setupVideoView(videoView4, videoUri4);

        // Set click listeners for each VideoView
        setVideoClickListener(videoView1, videoView2, videoView3, videoView4);
    }

    private void setupVideoView(VideoView videoView, Uri videoUri) {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
    }

    private void setVideoClickListener(VideoView... videoViews) {
        for (VideoView videoView : videoViews) {
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Stop playback in all other VideoViews
                    stopOtherVideoViews(videoView);

                    // Start or toggle playback for the clicked VideoView
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    } else {
                        videoView.start();
                    }
                }
            });
        }
    }

    private void stopOtherVideoViews(VideoView currentVideoView) {
        // Stop playback in all VideoViews except the current one
        for (VideoView videoView : new VideoView[]{videoView1, videoView2, videoView3, videoView4}) {
            if (videoView != currentVideoView && videoView.isPlaying()) {
                videoView.pause();
            }
        }
    }
}