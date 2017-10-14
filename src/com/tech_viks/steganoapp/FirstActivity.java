package com.tech_viks.steganoapp;

import java.net.URISyntaxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
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
	int backFlag = 0;
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
					Intent toprocess = new Intent(FirstActivity.this,EncryptMessage.class);
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
                
                selected_image_URL = getFilePath(FirstActivity.this , selectedImageUri);
                
                if(selected_image_URL!=null || selected_image_URL!=""){
                	//Toast.makeText(FirstActivity.this, selected_image_URL, Toast.LENGTH_LONG).show();
					Intent toDisplayMessage = new Intent(FirstActivity.this,DisplayMessage.class);
					toDisplayMessage.putExtra("ImageURL", selected_image_URL);
					startActivity(toDisplayMessage);
					finish();
				}
				else{
					Toast.makeText(FirstActivity.this, "Something went wrong...try again", Toast.LENGTH_LONG)
					.show();
				}
            }
        }
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		backFlag=0;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (backFlag!=0) {
			super.onBackPressed();
		}
		//super.onBackPressed();
				
		AlertDialog adb = new AlertDialog.Builder(FirstActivity.this).setTitle("Are you sure you want to exit")
				.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						backFlag+=1;
						onBackPressed();
					}
				})
				.setNegativeButton("Cancel", null).show();
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
