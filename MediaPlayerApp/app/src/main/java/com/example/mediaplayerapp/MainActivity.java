package com.example.mediaplayerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button openAudio, openVideo, playBtn, pauseBtn, stopBtn, restartBtn;
    MediaPlayer mediaPlayer;
    VideoView videoView;
    Uri audioUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openAudio = findViewById(R.id.openAudio);
        openVideo = findViewById(R.id.openVideo);
        playBtn = findViewById(R.id.playBtn);
        pauseBtn = findViewById(R.id.pauseBtn);
        stopBtn = findViewById(R.id.stopBtn);
        restartBtn = findViewById(R.id.restartBtn);
        videoView = findViewById(R.id.videoView);

        // Open audio file
        openAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, 1);
        });

        // Open video from URL
        openVideo.setOnClickListener(v -> {
            String url = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
            videoView.setVideoURI(Uri.parse(url));
            mediaPlayer = null; // stop audio if video selected
        });

        // ▶️ Play
        playBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else {
                videoView.start();
            }
        });

        // ⏸️ Pause
        pauseBtn.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else if (videoView.isPlaying()) {
                videoView.pause();
            }
        });

        // ⏹️ Stop
        stopBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
        });

        // 🔁 Restart
        restartBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            } else {
                videoView.seekTo(0);
                videoView.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            audioUri = data.getData();
            mediaPlayer = MediaPlayer.create(this, audioUri);
            videoView.stopPlayback(); // stop video if audio selected
        }
    }
}