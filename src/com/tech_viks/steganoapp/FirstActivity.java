package com.tech_viks.steganoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends Activity {

	// ============== variable declaration ===============
	Button enc,dec;
	private static final int SELECT_PICTURE = 1;
	String selected_image_URL; // stores url of the image to be used
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		enc = (Button)findViewById(R.id.encrypt);
		dec = (Button)findViewById(R.id.retrieve);
		
		// ============ coding for encrypt button ==============
		enc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//choose_image();
				
				//if(selected_image_URL!=null || selected_image_URL!=""){
				try{
					Intent toprocess = new Intent(FirstActivity.this,ProcessImage.class);
					//toDisplayMessage.putExtra("ImageURL", selected_image_URL);
					startActivity(toprocess);
				}
				catch(Exception e){
					
				}
				//}
				/*else{
					Toast.makeText(FirstActivity.this, "Something went wrong...try again", Toast.LENGTH_LONG)
					.show();
				}*/
			}
		});
		
		// ============ coding for decrypt button ==============
		dec.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				choose_image();
			}
		});
		
		
	}
	
	private void choose_image(){
		
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
		
	}
	//=========== Activity popup to select image to pocess ============
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selected_image_URL = getPath(selectedImageUri);   
                if(selected_image_URL!=null || selected_image_URL!=""){
					Intent toDisplayMessage = new Intent(FirstActivity.this,DisplayMessage.class);
					toDisplayMessage.putExtra("ImageURL", selected_image_URL);
					startActivity(toDisplayMessage);
				}
				else{
					Toast.makeText(FirstActivity.this, "Something went wrong...try again", Toast.LENGTH_LONG)
					.show();
				}
            }
        }
    }
	// ============ function to get the path of the uri of image ===========
	public String getPath(Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
	}
	
}
