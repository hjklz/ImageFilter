package com.example.imagefilter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.imagefilter.R;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageButton button = (ImageButton) findViewById(R.id.Image);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				
			}
		};
		button.setOnClickListener(listener);

		Button loadButton = (Button) findViewById(R.id.Load);

		loadButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});

		Button meanButton = (Button) findViewById(R.id.Mean);

		meanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		
		Button medianButton = (Button) findViewById(R.id.Median);

		medianButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
}
