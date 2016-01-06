package com.example.imagefilter;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

		Button meanButton = (Button) findViewById(R.id.Mean);
		meanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new FilterTask().execute();
				//change imagebutton after
			}
		});
		
		Button medianButton = (Button) findViewById(R.id.Median);
		medianButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new FilterTask().execute();
				//change imagebutton after
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    if(item.getItemId() == R.id.settings) {
	    	//Start new activity that deals with settings
	    }
	    
	    return true;
	}
	
	//Snippet 5
	public File getAlbumStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory. 
	    File file = new 	File(Environment.getExternalStoragePublicDirectory(
	          Environment.DIRECTORY_PICTURES), albumName);
	    if (!file.mkdirs()) {
	        Log.e("pubpic", "Directory not created");
	    }
	    return file;
	}


	public File getAlbumStorageDir(Context context, String 	albumName) {
	    	// Get the directory for the app's private pictures directory. 
	    	File file = new File(context.getExternalFilesDir(
	          Environment.DIRECTORY_PICTURES), albumName);
	    	if (!file.mkdirs()) {
	        	Log.e("privpic", "Directory not created");
	    	}
	    	return file;
	}
	
	private class FilterTask extends AsyncTask<String, Void, Void> {

		/* 
		 * maybe pass in these methods for filtering instead of defining them here
		 */
	    protected void meanFilter() {
	        
	    }

	    protected void medianFilter() {
	        
	    }
	    
		@Override
		protected Void doInBackground(String... params) {
			if (params.toString().equals("mean")) {
				meanFilter();
			} else {
				medianFilter();
			}

			return null;
		}
	    
	    protected void onPostExecute() {
	        
	    }
	}
}



