package com.tech_viks.steganoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
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
						Toast.makeText(EncryptMessage.this, "image file not selected...", Toast.LENGTH_LONG)
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
	                selected_image_URL = getFilePath(EncryptMessage.this,selectedImageUri);  
	                Imagepath.setText(selected_image_URL);
	            }
	        }
	    }
		
		// ============= function to get the absolute path for an uri ===================
		
		 public static String getFilePath(Context context, Uri uri) {
		        String selection = null;
		        String[] selectionArgs = null;
		        // Uri is different in versions after KITKAT (Android 4.4), we need to
		        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
		            if (isExternalStorageDocument(uri)) {
		                final String docId = DocumentsContract.getDocumentId(uri);
		                final String[] split = docId.split(":");
		                return Environment.getExternalStorageDirectory() + "/" + split[1];
		            } else if (isDownloadsDocument(uri)) {
		                final String id = DocumentsContract.getDocumentId(uri);
		                uri = ContentUris.withAppendedId(
		                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
		            } else if (isMediaDocument(uri)) {
		                final String docId = DocumentsContract.getDocumentId(uri);
		                final String[] split = docId.split(":");
		                final String type = split[0];
		                if ("image".equals(type)) {
		                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		                } else if ("video".equals(type)) {
		                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		                } else if ("audio".equals(type)) {
		                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		                }
		                selection = "_id=?";
		                selectionArgs = new String[]{
		                        split[1]
		                };
		            }
		        }
		        if ("content".equalsIgnoreCase(uri.getScheme())) {
		            String[] projection = {
		                    MediaStore.Images.Media.DATA
		            };
		            Cursor cursor = null;
		            try {
		                cursor = context.getContentResolver()
		                        .query(uri, projection, selection, selectionArgs, null);
		                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		                if (cursor.moveToFirst()) {
		                    return cursor.getString(column_index);
		                }
		            } catch (Exception e) {
		            }
		        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
		            return uri.getPath();
		        }
		        return null;
		    }

		    public static boolean isExternalStorageDocument(Uri uri) {
		        return "com.android.externalstorage.documents".equals(uri.getAuthority());
		    }

		    public static boolean isDownloadsDocument(Uri uri) {
		        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
		    }

		    public static boolean isMediaDocument(Uri uri) {
		        return "com.android.providers.media.documents".equals(uri.getAuthority());
		    }
		
}
