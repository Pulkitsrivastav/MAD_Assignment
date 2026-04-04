package com.example.galleryapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    Button deleteBtn;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);
        deleteBtn = findViewById(R.id.deleteBtn);

        index = getIntent().getIntExtra("index", -1);

        if (index != -1) {
            Bitmap image = MainActivity.imageList.get(index);
            imageView.setImageBitmap(image);
        }

        deleteBtn.setOnClickListener(v -> {
            if (index != -1) {
                MainActivity.imageList.remove(index);
                finish();
            }
        });
    }
}