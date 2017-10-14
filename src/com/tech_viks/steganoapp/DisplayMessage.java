package com.tech_viks.steganoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessage extends Activity {

	// ========= variable declaration =============
	TextView message ;
	String imageToProcess;
	
	ProcessImage process_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		process_image = new ProcessImage();
		
		setContentView(R.layout.activity_display_message);
		message = (TextView)findViewById(R.id.messageText);
		Bundle bndl = getIntent().getExtras();
		imageToProcess = bndl.getString("ImageURL");
		if(imageToProcess==null || imageToProcess==""){
		 Toast.makeText(getApplicationContext(), "no image seleted", Toast.LENGTH_LONG).show();
		}
		String message_to_show = process_image.displayMessage(imageToProcess);
		Toast.makeText(getApplicationContext(), message_to_show, Toast.LENGTH_LONG).show();
		message.setText("Message:"+message_to_show);
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent backPage = new Intent(DisplayMessage.this,FirstActivity.class);
		backPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(backPage);
		finish();
	}
	
}
