package com.imagefilter.andy.imagefilter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;

public class FilterTask extends AsyncTask <Void,Integer,Bitmap>{

    public static final int MEAN_FILTER = 1;
    public static final int MEDIAN_FILTER = 2;

    Context context;
    ImageButton imageButton;
    Button callingButton;
    int type;
    int mask;

    Bitmap refImage, newImage;
    int width, height, totalPixels;

    ProgressDialog progressDialog;

    FilterTask (Context context, ImageButton imageButton, Button callingButton, int type, int mask) {
        this.context = context;
        this.imageButton = imageButton;
        this.callingButton = callingButton;
        this.type = type;
        this.mask = mask;

        refImage = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        this.width = refImage.getWidth();
        this.height = refImage.getHeight();
        this.totalPixels = width*height;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        int i = 0;

        synchronized (this)
        {
            newImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            //using goto here to break out of multiple loops
            loop:
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if(isCancelled()) {
                        break loop;
                    }

                    if (type == MEAN_FILTER) {
                        meanFilter(x,y);
                    }

                    if (type == MEDIAN_FILTER) {
                        medianFilter(x,y);
                    }
                }
                publishProgress(i);
                i++;
            }
        }

        return newImage;
    }

    private void meanFilter (int x, int y) {
        int a, r, g, b;

        int sumA = 0, sumR = 0, sumG = 0, sumB = 0, count = 0;

        //add current value of pixel to sum
        sumA += Color.alpha(refImage.getPixel(x, y));
        sumR += Color.red(refImage.getPixel(x, y));
        sumG += Color.green(refImage.getPixel(x, y));
        sumB += Color.blue(refImage.getPixel(x, y));
        count++;

        //slight loop unrolling to increase efficiency maybe?
        for(int m = 0; m < mask/2; m++) {
            //adding every other pixel in mask to sum
            if (x+m < width) {
                if (y+m < height) {
                    sumA += Color.alpha(refImage.getPixel(x+m,y+m));
                    sumR += Color.red(refImage.getPixel(x + m, y + m));
                    sumG += Color.green(refImage.getPixel(x + m, y + m));
                    sumB += Color.blue(refImage.getPixel(x + m, y + m));
                    count++;
                }
                if (y-m >= 0) {
                    sumA += Color.alpha(refImage.getPixel(x+m,y-m));
                    sumR += Color.red(refImage.getPixel(x + m, y - m));
                    sumG += Color.green(refImage.getPixel(x + m, y - m));
                    sumB += Color.blue(refImage.getPixel(x + m, y - m));
                    count++;
                }
            }

            if (x-m >= 0) {
                if (y+m < height) {
                    sumA += Color.alpha(refImage.getPixel(x-m,y+m));
                    sumR += Color.red(refImage.getPixel(x - m, y + m));
                    sumG += Color.green(refImage.getPixel(x - m, y + m));
                    sumB += Color.blue(refImage.getPixel(x - m, y + m));
                    count++;
                }
                if (y-m >= 0) {
                    sumA += Color.alpha(refImage.getPixel(x-m,y-m));
                    sumR += Color.red(refImage.getPixel(x - m, y - m));
                    sumG += Color.green(refImage.getPixel(x - m, y - m));
                    sumB += Color.blue(refImage.getPixel(x - m, y - m));
                    count++;
                }
            }


        }

        a = sumA/count;
        r = sumR/count;
        g = sumG/count;
        b = sumB/count;

        newImage.setPixel(x, y, Color.argb(a, r, g, b));
    }

    private void medianFilter(int x, int y) {
        int a, r, g, b;
        //lists of all the bits in a mask
        //could refactor this into a 4D arrayList
        ArrayList<Integer> aList = new ArrayList<>(),
                rList = new ArrayList<>(),
                gList = new ArrayList<>(),
                bList = new ArrayList<>();

        //add current value of pixel to lists
        aList.add(Color.alpha(refImage.getPixel(x, y)));
        rList.add(Color.red(refImage.getPixel(x, y)));
        gList.add(Color.green(refImage.getPixel(x, y)));
        bList.add(Color.blue(refImage.getPixel(x, y)));

        //slight loop unrolling to increase efficiency maybe?
        for (int m = 0; m < mask / 2; m++) {
            //adding every other pixel in mask to lists
            if (x + m < width) {
                if (y + m < height) {
                    aList.add(Color.alpha(refImage.getPixel(x + m, y + m)));
                    rList.add(Color.red(refImage.getPixel(x + m, y + m)));
                    gList.add(Color.green(refImage.getPixel(x + m, y + m)));
                    bList.add(Color.blue(refImage.getPixel(x + m, y + m)));
                }
                if (y - m >= 0) {
                    aList.add(Color.alpha(refImage.getPixel(x + m, y - m)));
                    rList.add(Color.red(refImage.getPixel(x + m, y - m)));
                    gList.add(Color.green(refImage.getPixel(x + m, y - m)));
                    bList.add(Color.blue(refImage.getPixel(x + m, y - m)));
                }
            }

            if (x - m >= 0) {
                if (y + m < height) {
                    aList.add(Color.alpha(refImage.getPixel(x - m, y + m)));
                    rList.add(Color.red(refImage.getPixel(x - m, y + m)));
                    gList.add(Color.green(refImage.getPixel(x - m, y + m)));
                    bList.add(Color.blue(refImage.getPixel(x - m, y + m)));
                }
                if (y - m >= 0) {
                    aList.add(Color.alpha(refImage.getPixel(x - m, y - m)));
                    rList.add(Color.red(refImage.getPixel(x - m, y - m)));
                    gList.add(Color.green(refImage.getPixel(x - m, y - m)));
                    bList.add(Color.blue(refImage.getPixel(x - m, y - m)));
                }
            }


        }

        a = getMedian(aList);
        r = getMedian(rList);
        g = getMedian(gList);
        b = getMedian(bList);

        newImage.setPixel(x, y, Color.argb(a, r, g, b));
    }

    //using algorithm that finds median without sorting
    //https://discuss.codechef.com/questions/1489/find-median-in-an-unsorted-array-without-sorting-it
    private int partitions(int low,int high, ArrayList<Integer> list) {
        int x=list.get(high),i=low-1;
        for(int j=low;j<=high-1;j++)
        {
            if (list.get(j) <= x)
            {
                i=i+1;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i+1, high);
        return i+1;
    }

    private int getMedian(ArrayList<Integer> list) {
        int left = 1;
        int right = list.size()-1;
        int kth = list.size()/2;

        //handling situation where finding median is trivial
        if (list.size() == 1) {
            return list.get(0);
        }

        while (true) {
            int pivotIndex = partitions(left,right,list);          //Select the Pivot Between Left and Right
            int len = pivotIndex-left+1;

            if(kth == len)
                return list.get(pivotIndex);

            else if(kth < len)
                right = pivotIndex-1;

            else
            {
                kth = kth-len;
                left = pivotIndex+1;
            }
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Filtering...");
        progressDialog.setMax(width);
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
