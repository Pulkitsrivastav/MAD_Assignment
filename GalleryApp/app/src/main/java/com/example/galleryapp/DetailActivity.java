package com.example.galleryapp;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    MaterialButton deleteBtn;
    TextView infoText;
    Toolbar toolbar;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        imageView = findViewById(R.id.imageView);
        deleteBtn = findViewById(R.id.deleteBtn);
        infoText = findViewById(R.id.infoText);

        index = getIntent().getIntExtra("index", -1);

        if (index != -1 && index < MainActivity.imageList.size()) {
            ImageModel imageModel = MainActivity.imageList.get(index);
            imageView.setImageBitmap(imageModel.getBitmap());

            String details = "<b>Name:</b> " + imageModel.getName() + "<br/><br/>" +
                             "<b>Date Captured:</b> " + imageModel.getTimestamp() + "<br/><br/>" +
                             "<b>Dimensions:</b> " + imageModel.getWidth() + " x " + imageModel.getHeight() + " px<br/><br/>" +
                             "<b>Memory Size:</b> " + (imageModel.getSizeInBytes() / 1024) + " KB";
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                infoText.setText(Html.fromHtml(details, Html.FROM_HTML_MODE_COMPACT));
            } else {
                infoText.setText(Html.fromHtml(details));
            }
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        deleteBtn.setOnClickListener(v -> {
            showDeleteConfirmation();
        });
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("This action cannot be undone. Are you sure you want to delete this image?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (index != -1 && index < MainActivity.imageList.size()) {
                        MainActivity.imageList.remove(index);
                        Toast.makeText(DetailActivity.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}