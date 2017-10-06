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
	int textlength;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		message = (TextView)findViewById(R.id.messageText);
		Bundle bndl = getIntent().getExtras();
		imageToProcess = bndl.getString("ImageURL");
		
		String message_to_show = displayMessage(imageToProcess);
		message.append("Message\n"+message_to_show);
	}
	
	private  String displayMessage(String imagePath){
		// ========== variable declaration ============
		String r,g,b;   // stores red green and blue color values of current pixel
		//imagePath =  Environment.getExternalStorageDirectory().toString() +"/Captures/screen.png";
		Bitmap bmp = BitmapFactory.decodeFile(imagePath);   // stores image file bitmap format				
		
		int width = bmp.getWidth();   // width of image		
		int height = bmp.getHeight();  // height of image
		
		String msg="";    // stores actual message		
		int[] pixels = new int[width * height];  //stores all the pixel values
		
		// ========== putting pixel values from bitmap file to pixels array(int) ==============
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		textlength = pixels[0] & 0xff; // returns the length of message string blue color value of very first pixel
		int check_condition = (pixels[0] >>16) &0xff;
		//=========check if message is present or not ===========
		if(check_condition!=83){
			return "no hidden message";
		}
		//message.append("\nLength of message is : "+textlength);//display message 
		int m=0;//counter variable
		String messege_in_bits = "";// stores message in bin format ie. base 2 form
		// loop to get message in base 2 format
		try{
		//start of for loop
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				if(i==0&&j==0){}
				else{
					if(m>=textlength*8){
						break;
					}
					r=Integer.toString(((pixels[i*width+j] >> 16) & 0xff),2);
					g=Integer.toString(((pixels[i*width+j] >> 8) & 0xff),2);
					b=Integer.toString(((pixels[i*width+j]) & 0xff),2);
					//getting from red color of pixel
					messege_in_bits += r.charAt(r.length()-1);	
					m++;
					if(m>=textlength*8){
						break;
					}
					//getting from green color of pixel
					messege_in_bits += g.charAt(g.length()-1);	
					m++;
					if(m>=textlength*8){
						break;
					}
					//getting from blue color from pixel
					messege_in_bits += b.charAt(b.length()-1);	
					m++;
				}
			}
		}// end of for loop  
		
		//message.append("\nMessege in base2 form : "+messege_in_bits);
		// =========== loop to convert message to String format form base2 ====================
		for(int i=0;i<messege_in_bits.length();i+=8){
			String temp = messege_in_bits.substring(i, i+8);			
			msg+=toBinary(temp);			
		}
		}
		catch(Exception e){
			Toast.makeText(DisplayMessage.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		// ====== displaying actual message ======================
		return msg;
	}
	
	
	public char toBinary(String toConvert){
		int sum=0;
		//et.append("\n"+toConvert+"\n");
		int length_of_string= toConvert.length();
		for(int i=0;i<length_of_string;i++){
			int j = toConvert.charAt(i)-48;
			//et.append(""+j);
			sum+=j*(Math.pow(2, (length_of_string-i-1)));
		}
		return (char)sum;
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
