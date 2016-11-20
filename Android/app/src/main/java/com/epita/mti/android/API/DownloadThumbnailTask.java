package com.epita.mti.android.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by VisionElf on 19/01/2016.
 */
public class DownloadThumbnailTask extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;

    public DownloadThumbnailTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap thumbnail = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            thumbnail = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return thumbnail;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        this.imageView.setImageBitmap(result);
    }
}
