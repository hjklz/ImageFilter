package com.imagefilter.andy.imagefilter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Andy on 2016-01-17.
 */
public class FilterTask extends AsyncTask <Void,Integer,Bitmap>{

    public static final int MEAN_FILTER = 1;
    public static final int MEDIAN_FILTER = 2;

    Context context;
    ImageButton imageButton;
    Button callingButton;
    int type;
    int mask;

    Bitmap refImage, newImage;
    int totalPixels;

    ProgressDialog progressDialog;

    FilterTask (Context context, ImageButton imageButton, Button callingButton, int type, int mask) {
        this.context = context;
        this.imageButton = imageButton;
        this.callingButton = callingButton;
        this.type = type;
        this.mask = mask;

        refImage = ((BitmapDrawable)imageButton.getBackground()).getBitmap();
        this.totalPixels = refImage.getWidth()*refImage.getHeight();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        int i = 0;

        synchronized (this)
        {
            while (i<totalPixels) {




                i++;
                publishProgress(i);
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Filtering...");
        progressDialog.setMax(totalPixels);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageButton.setImageBitmap(result);
        callingButton.setEnabled(true);
        progressDialog.hide();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        progressDialog.setProgress(progress);
    }
}
