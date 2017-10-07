package com.tech_viks.steganoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EncryptMessage extends Activity {

	String selected_image_URL=""; // to store the path of chosen image
	String newFileName="";  // to store the stegno image (file name)
	TextView Imagepath,message_error;
	Button selectImage,proceed;
	EditText getMessageString;
	private static final int SELECT_PICTURE = 1;
	ProcessImage processImage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_image);
		
		try{
			
			selectImage = (Button)findViewById(R.id.choose_image_btn);
			proceed = (Button)findViewById(R.id.proceed_btn);
			Imagepath = (TextView)findViewById(R.id.image_path_textView);
			getMessageString = (EditText) findViewById(R.id.message_editText);
			message_error = (TextView)findViewById(R.id.error_message);
			processImage = new ProcessImage();
			
			
			selectImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
			        intent.setType("image/*");
			        intent.setAction(Intent.ACTION_GET_CONTENT);
			        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
				}
			});
			
			
			proceed.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(ProcessImage.this, "working!", Toast.LENGTH_LONG).show();
					
					if(selected_image_URL != null && selected_image_URL != "")
					{
						
						processImage.createStegoImage(selected_image_URL, getMessageString.getText().toString());
						
						
						//========= layout inflater to inflate layout for dialog ============
						LayoutInflater prompt_li = LayoutInflater.from(EncryptMessage.this);
						View promptView = prompt_li.inflate(R.layout.user_single_input, null);
						
						//alert dialog to pop up dialog
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EncryptMessage.this);
						final EditText et = (EditText) promptView.findViewById(R.id.user_input);
						et.setHint("Enter name of file");
						
						//dialog program
						alertDialogBuilder.setView(promptView).setTitle("Enter name of new file")
				        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(et.getText().toString()!=null || et.getText().toString()!="")
								{
									message_error.setText(processImage.bitmapToFile(et.getText().toString()));
								}//bitmapToFile(et.getText().toString());}
								else{
									//Toast.makeText(EncryptMessage.this, "failed to save image", Toast.LENGTH_LONG).show();
								}
							}
						})
				        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						})
				
				        .show();
						
					}
					else{
						Toast.makeText(EncryptMessage.this, "image file no selected...", Toast.LENGTH_LONG)
						.show();
					}
				}
			});
			
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	//=========== Activity popup to select image to pocess ============
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selected_image_URL = getPath(selectedImageUri);  
	                Imagepath.setText(selected_image_URL);
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
	
		// =========== function to create stego image   ==============
		
		
		
		
		//================ function to create image file form bitmap file ============
		
}
