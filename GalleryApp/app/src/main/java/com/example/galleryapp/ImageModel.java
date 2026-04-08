package com.example.galleryapp;

import android.graphics.Bitmap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageModel {
    private Bitmap bitmap;
    private String timestamp;
    private String name;

    public ImageModel(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        this.name = "IMG_" + System.currentTimeMillis();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public long getSizeInBytes() {
        return bitmap.getByteCount();
    }
}