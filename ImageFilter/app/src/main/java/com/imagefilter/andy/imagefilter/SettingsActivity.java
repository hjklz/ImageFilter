package com.imagefilter.andy.imagefilter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;


import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private NumberPicker convuPicker;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setConvuPicker(getIntent().getIntExtra("ImageSize", 1));
        sharedPref = getSharedPreferences("ImageFilter", Context.MODE_PRIVATE);

        TextView curConvu = (TextView) findViewById(R.id.curConvu);

        //snippet 2
        curConvu.setText(Integer.toString(sharedPref.getInt(getString(R.string.convuMask), 1))); // grab value from shared preferences

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();

                //the number picker value is the index so need to reference the displayed values to get actual value
                editor.putInt(getString(R.string.convuMask), Integer.parseInt(convuPicker.getDisplayedValues()[convuPicker.getValue()-1]));

                editor.commit();
                finish();
            }
        });
    }

    private void setConvuPicker (int maxSize) {
        ArrayList<String> values = new ArrayList<>();

        //making number picker only display valid convolution masks
        for (int i = 1; i <= maxSize; i+=2) {
            values.add(Integer.toString(i));
        }

        String[] valuesArr = new String[values.size()];
        valuesArr = values.toArray(valuesArr);

        convuPicker = (NumberPicker) findViewById(R.id.convuPicker);
        convuPicker.setWrapSelectorWheel(false);

        // As per http://stackoverflow.com/questions/22370310/modifying-or-changing-min-max-and-displayed-values-for-numberpicker
        // making number picker ignore the length check of the values
        convuPicker.setDisplayedValues(null);
        convuPicker.setMinValue(1);
        convuPicker.setMaxValue(valuesArr.length);
        convuPicker.setDisplayedValues(valuesArr);
    }
}
