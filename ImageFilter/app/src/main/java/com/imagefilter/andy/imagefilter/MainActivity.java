package com.imagefilter.andy.imagefilter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_REQUEST = 57;
    public static final int NO_IMAGE = 0;
    public static final int HAS_IMAGE = 1;

    private ImageButton imgButton;
    private Button meanButton;
    private Button medianButton;

    private FilterTask filterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        imgButton = (ImageButton) findViewById(R.id.Image);
        imgButton.setTag(NO_IMAGE);

        //could refactor these onClicks to use tags and a single listener
        Button loadButton = (Button) findViewById(R.id.Load);
        loadButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onLoadImageClicked(v);
            }
        });

        meanButton = (Button) findViewById(R.id.Mean);
        meanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                cancelFilterTask();
                int mask = getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.convuMask), 1);
                filterTask = new FilterTask(MainActivity.this, imgButton, meanButton, FilterTask.MEAN_FILTER, mask);
                filterTask.execute();
                meanButton.setEnabled(false);
            }
        });

        medianButton = (Button) findViewById(R.id.Median);
        medianButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                cancelFilterTask();
                int mask = getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.convuMask), 1);
                filterTask = new FilterTask(MainActivity.this, imgButton, medianButton, FilterTask.MEDIAN_FILTER, mask);
                filterTask.execute();
                medianButton.setEnabled(false);
            }
        });

        meanButton.setEnabled(false);
        medianButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //convolution mask with no image doesn't make sense
            if ((Integer)imgButton.getTag() == NO_IMAGE) {
                Toast.makeText(this, "Load Image first!", Toast.LENGTH_LONG).show();
            } else {
                Intent settings = new Intent(this, SettingsActivity.class);
                //giving image parameters to settings
                //http://stackoverflow.com/questions/8880376/how-to-get-height-and-width-of-a-image-used-in-android
                //http://stackoverflow.com/questions/8306623/get-bitmap-attached-to-imageview

                Bitmap b = ((BitmapDrawable)imgButton.getDrawable()).getBitmap();

                settings.putExtra("ImageSize", Math.min(b.getHeight(),b.getWidth()));
                startActivity(settings);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //the method will run then load image button is clicked
    public void onLoadImageClicked (View v) {
        //using snippet 5 to get images
        Intent imageIntent = new Intent(Intent.ACTION_PICK);

        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String imageDirPath = imageDir.getPath();
        Uri imageURI = Uri.parse(imageDirPath);

        imageIntent.setDataAndType(imageURI, "image/*");

        startActivityForResult(imageIntent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_REQUEST) {
                Uri imageURI = data.getData();

                try {
                    //image load success, change imagebutton and activate buttons
                    //reset convolution mask, cancel any existing filtering
                    cancelFilterTask();

                    InputStream inputStream = getContentResolver().openInputStream(imageURI);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    imgButton.setImageBitmap(image);
                    imgButton.setTag(HAS_IMAGE);

                    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putInt(getString(R.string.convuMask), 1);
                    editor.commit();

                    meanButton.setEnabled(true);
                    medianButton.setEnabled(true);

                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private void cancelFilterTask() {
        if (filterTask != null) {
            filterTask.cancel(true);
        }
    }
}
