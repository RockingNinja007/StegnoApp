package com.tech_viks.steganoapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GetExternalImage extends Activity {

	Uri image_uri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_external_image);
		try{
		
			image_uri = getIntent().getData();
			String imagePath = image_uri.getPath();
			
			Intent toDisplay = new Intent(GetExternalImage.this,DisplayMessage.class);
			toDisplay.putExtra("ImageURL", imagePath);
			toDisplay.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			toDisplay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(toDisplay);
			finish();
			
			
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_external_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
