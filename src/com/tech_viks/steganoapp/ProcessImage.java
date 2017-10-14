package com.tech_viks.steganoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
public class ProcessImage {

	Bitmap bmp,bmp1;
	double NoOfImagePixels;
	
	
	// =========== function to create stego image   ==============
	public void createStegoImage(String imagePath,String MessageString){
		int r,g,b;
		//final String file_name;
		try{
			// ================ creating bitmap file ================
			bmp = BitmapFactory.decodeFile(imagePath);
			bmp1 = bmp.copy(Bitmap.Config.ARGB_8888, true); 
			bmp.recycle();
			int width = bmp1.getWidth();
			int height = bmp1.getHeight();
			NoOfImagePixels = width*height;
			
			
			
			if(NoOfImagePixels*3<=MessageString.length()*8){
				return;
			}
			int[] pixels = new int[width * height];
			//=============================== setting message String ========================
			String str = MessageString;
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
					
					}
				}						
			}
			//==================== end of for loop ===========================
			//========== dialog to get file name from user ===========			
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
	public String bitmapToFile(String file_name){
		String path = Environment.getExternalStorageDirectory().toString();
	    OutputStream fOutputStream = null;
	   // change the path to store image here
	    File dir = new File(path + "/Captures/");
	    if(!dir.exists()) {
	        dir.mkdirs();
	    }
	    
	    File file = new File(path + "/Captures/"+file_name+".png");
	    try {
	        fOutputStream = new FileOutputStream(file);
	  
	        bmp1.compress(Bitmap.CompressFormat.PNG, 0, fOutputStream);
	        fOutputStream.flush();
	        fOutputStream.close();
	        
	    } 
	    catch (FileNotFoundException e) {
	        return "Error occured while saving file";
	    } 
	    catch (IOException e) {
	        return "Error occured while saving file";
	    }
	    return "Message stored in location as : "+file.getPath();
	}
	
	
	public  String displayMessage(String imagePath){
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
		int textlength = pixels[0] & 0xff; // returns the length of message string blue color value of very first pixel
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
			//Toast.makeText(DisplayMessage.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
	
}
