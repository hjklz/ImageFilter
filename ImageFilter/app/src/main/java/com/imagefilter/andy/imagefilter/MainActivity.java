package com.imagefilter.andy.imagefilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ImageButton button = (ImageButton) findViewById(R.id.Image);
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                //should this do anything on click?
            }
        };
        button.setOnClickListener(listener);

        Button loadButton = (Button) findViewById(R.id.Load);
        loadButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //use snippet 5 to select image
            }
        });
        loadButton.setOnClickListener(listener);

        Button meanButton = (Button) findViewById(R.id.Mean);
        meanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //change imagebutton after
            }
        });
        meanButton.setOnClickListener(listener);

        Button medianButton = (Button) findViewById(R.id.Median);
        medianButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //change imagebutton after
            }
        });
        medianButton.setOnClickListener(listener);
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
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
