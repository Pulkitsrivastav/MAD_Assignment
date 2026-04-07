package com.example.mediaplayerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;

    Button openAudio, openLocalVideo, playFromUrlBtn;
    FloatingActionButton playBtn, pauseBtn, stopBtn, restartBtn;
    TextView videoPlaceholder;
    ProgressBar progressBar;
    MediaPlayer mediaPlayer;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        openAudio = findViewById(R.id.openAudio);
        openLocalVideo = findViewById(R.id.openLocalVideo);
        playFromUrlBtn = findViewById(R.id.playFromUrlBtn);
        playBtn = findViewById(R.id.playBtn);
        pauseBtn = findViewById(R.id.pauseBtn);
        stopBtn = findViewById(R.id.stopBtn);
        restartBtn = findViewById(R.id.restartBtn);
        videoView = findViewById(R.id.videoView);
        videoPlaceholder = findViewById(R.id.videoPlaceholder);
        progressBar = findViewById(R.id.progressBar);

        // Add Video Controls
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Open local audio file
        openAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_AUDIO_REQUEST);
        });

        // Open local video file
        openLocalVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(intent, PICK_VIDEO_REQUEST);
        });

        // Play from URL - Opens a Dialog
        playFromUrlBtn.setOnClickListener(v -> showUrlDialog());

        // Control Buttons
        playBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else if (videoView != null) {
                videoView.start();
            }
        });

        pauseBtn.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else if (videoView.isPlaying()) {
                videoView.pause();
            }
        });

        stopBtn.setOnClickListener(v -> {
            stopCurrentMedia();
            videoPlaceholder.setVisibility(View.VISIBLE);
            videoPlaceholder.setText("Ready to play");
            Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
        });

        restartBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            } else if (videoView != null) {
                videoView.seekTo(0);
                videoView.start();
            }
        });
    }

    private void showUrlDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Video URL");

        // Use a simple layout for the dialog input
        final EditText input = new EditText(this);
        input.setHint("https://example.com/video.mp4");
        input.setPadding(50, 40, 50, 40);
        builder.setView(input);

        builder.setPositiveButton("Load", (dialog, which) -> {
            String url = input.getText().toString().trim();
            if (!url.isEmpty()) {
                loadUrlVideo(url);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void loadUrlVideo(String url) {
        stopCurrentMedia();
        videoPlaceholder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        try {
            videoView.setVideoURI(Uri.parse(url));
            videoView.setOnPreparedListener(mp -> {
                progressBar.setVisibility(View.GONE);
                videoView.start();
                Toast.makeText(this, "Video Started", Toast.LENGTH_SHORT).show();
            });
            videoView.setOnErrorListener((mp, what, extra) -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Invalid Link. Ensure it's a direct .mp4 link.", Toast.LENGTH_LONG).show();
                videoPlaceholder.setVisibility(View.VISIBLE);
                return true;
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopCurrentMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (videoView != null) {
            videoView.stopPlayback();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            stopCurrentMedia();
            Uri mediaUri = data.getData();
            videoPlaceholder.setVisibility(View.GONE);

            if (requestCode == PICK_AUDIO_REQUEST) {
                mediaPlayer = MediaPlayer.create(this, mediaUri);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    videoPlaceholder.setVisibility(View.VISIBLE);
                    videoPlaceholder.setText("Playing Audio: " + mediaUri.getLastPathSegment());
                }
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                videoView.setVideoURI(mediaUri);
                videoView.start();
                Toast.makeText(this, "Video Loaded from Storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCurrentMedia();
    }
}