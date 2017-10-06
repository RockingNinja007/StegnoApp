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

public class ProcessImage extends Activity {

	String selected_image_URL=""; // to store the path of choosed image
	String newFileName="";  // to store the stegno image (file name)
	TextView Imagepath,message_error;
	Button selectImage,proceed;
	EditText getMessageString;
	private static final int SELECT_PICTURE = 1;
	Bitmap bmp,bmp1;
	double NoOfImagePixels;
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
					
					if(selected_image_URL != null || selected_image_URL != "")
					{createStegoImage(selected_image_URL);}
					else{
						Toast.makeText(ProcessImage.this, "image file no selected...", Toast.LENGTH_LONG)
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
	
		// =========== function to create stego image   ==============
		private void createStegoImage(String imagePath){
			int r,g,b;
			//final String file_name;
			try{
				// ================ creating bitmap file ================
				bmp = BitmapFactory.decodeFile(imagePath);
				//Toast.makeText(ProcessImage.this, bmp.getConfig().toString(), Toast.LENGTH_LONG).show();
				//Imagepath.setText(bmp.getConfig().toString());
				bmp1 = bmp.copy(Bitmap.Config.ARGB_8888, true); 
				bmp.recycle();
				int width = bmp1.getWidth();
				int height = bmp1.getHeight();
				NoOfImagePixels = width*height;
				message_error.setText("");
				//message_error.append("no. of pixels : "+NoOfImagePixels*3);
				
				
				if(NoOfImagePixels*3<=getMessageString.getText().toString().length()*8){
					Toast.makeText(ProcessImage.this, "Message length too long to be stored..", Toast.LENGTH_LONG).show();
					message_error.setText("Try smaller message or image with larger resolution");
					return;
				}
				int[] pixels = new int[width * height];
				//=============================== setting message String ========================
				String str = getMessageString.getText().toString();
				//================ converting message into stream of bits ===================
				String message_in_bits="";
				String current_message_char_bits;
				
				for(int i=0;i<str.length();i++){
					current_message_char_bits = Integer.toString(str.charAt(i),2);
					while(current_message_char_bits.length()!=8){
						current_message_char_bits = '0' + current_message_char_bits;
					}
					message_in_bits += current_message_char_bits;					
				}
				//message_error.append("\nMessage size : " + getMessageString.getText().toString().length()+" : "+ message_in_bits.length());
				
				//============== Getting all the pixels from the bitmap =======================
				bmp1.getPixels(pixels, 0, width, 0, 0, width, height);
				
				// ========== changing pixel values (hiding text into image) ===========
				int m=0;// ======= counter for message in bits string ========= 
				
				//================== changing bitmap pixel values ========================
				for(int i=0;i<height;i++){
					for (int j=0;j<width;j++){
						if(m >= message_in_bits.length()){
							break;
						}
						if(i==0 && j==0){
							//====== code to indicate message is present ==========
							bmp1.setPixel(i, j, Color.rgb(83,0,str.length()));
							//et.append("\nlength of message is : "+str.length());
						}
						else{								
						r=(pixels[i*width+j] >> 16) & 0xff;
						g=(pixels[i*width+j] >> 8) & 0xff;
						b=(pixels[i*width+j]) & 0xff;
						
						//changing red byte form pixel 
						if(isSet(message_in_bits.charAt(m))){
							r |= 0b1;
						}
						else{
							r &= ~0b1;
						}
						bmp1.setPixel(j,i, Color.argb(0,r,g,b));
						m++;
						if(m >= message_in_bits.length()){
							break;
						}
						// changing green byte from pixel
						if(isSet(message_in_bits.charAt(m))){
							g |= 0b1;
						}
						else{
							g &= ~0b1;
						}
						bmp1.setPixel(j,i, Color.argb(0,r,g,b));
						m++;
						if(m >= message_in_bits.length()){
							break;
						}
						//changing for blue color form pixel
						if(isSet(message_in_bits.charAt(m))){
							b |= 0b1;
						}
						else{
							b &= ~0b1;
						}
						bmp1.setPixel(j,i, Color.argb(0,r,g,b));
						m++;
						
					//	et.append("\nred : "+Integer.toString(r,2)+"Green : "+Integer.toString(g,2)+"Blue : "+Integer.toString(b,2));
						//m++;
						
						}
					}						
				}
				//==================== end of for loop ===========================
				//========== dialog to get file name from user ===========
				
				//========= layout inflater to inflate layout for dialog ============
				LayoutInflater prompt_li = LayoutInflater.from(ProcessImage.this);
				View promptView = prompt_li.inflate(R.layout.user_single_input, null);
				
				//alert dialog to pop up dialog
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				final EditText et = (EditText) promptView.findViewById(R.id.user_input);
				et.setHint("Enter name of file");
				
				//dialog program
				alertDialogBuilder.setView(promptView).setTitle("Enter name of new file")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(et.getText().toString()!=null || et.getText().toString()!="")
						{bitmapToFile(et.getText().toString());}
						else{
							Toast.makeText(ProcessImage.this, "failed to save image", Toast.LENGTH_LONG).show();
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
			catch(Exception e){
			
			}
		}
	
		public boolean isSet(char ch){
			
			if(ch == '1'){
			return true;
			}
			return false;
		}
		//====================== storing bitmap generated as png file =============================
		
		//================ function to create image file form bitmap file ============
		private void bitmapToFile(String file_name){
			String path = Environment.getExternalStorageDirectory().toString();
		    OutputStream fOutputStream = null;
		   
		    File dir = new File(path + "/Captures/");
		    if(!dir.exists()) {
		        dir.mkdirs();
		    }
		    
		    File file = new File(path + "/Captures/"+file_name+".png");
		    try {
		        fOutputStream = new FileOutputStream(file);
		        //Bitmap bmp2 = bmp1.copy(Bitmap.Config.RGB_565, true);
		        //bmp1.setConfig(Bitmap.Config.RGB_565);
		        //bmp1.recycle();
		        bmp1.compress(Bitmap.CompressFormat.PNG, 0, fOutputStream);
		        //et.append("message stored sucessfully");
		        Toast.makeText(ProcessImage.this, "Message stored Sucessfully", Toast.LENGTH_LONG).show();
		        message_error.setText("\nMessage stored in location as : "+file.getPath());
		        fOutputStream.flush();
		        fOutputStream.close();

		       // MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		        Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
		        return;
		    } catch (IOException e) {
		        e.printStackTrace();
		        Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
		        return;
		    }
		}
}
