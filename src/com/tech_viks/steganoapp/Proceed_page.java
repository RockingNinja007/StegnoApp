package com.tech_viks.steganoapp;

import android.support.v7.app.ActionBarActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.BitSet;

import android.R.string;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Proceed_page extends ActionBarActivity {
	ImageView iv ;
	Button back;
	Button proceed; 
	public String current_image_path;
	//integers to store colors from a pixel.
	
	//integer to hold index of pixel and message charecter
	//BigInteger BITS_OF_CURRENT_PIXEL_BLUE,BITS_OF_CURRENT_CHARACTER;
	//Bitmap bmp,bmp2;
	TextView tv;
	EditText et;
	//BigInteger n,m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proceed_page);
		
		
		
		try{
		
		Bundle bndl = getIntent().getExtras();
		current_image_path = bndl.getString("image_path");
		
		et = (EditText)findViewById(R.id.editText1);
		//tv = (TextView) findViewById(R.id.textView1);
		
		iv = (ImageView) findViewById(R.id.imageView1);
		
		iv.setImageURI(Uri.fromFile(new File(current_image_path)));
		
		
		
		//Toast.makeText(getApplicationContext(), current_image_path, Toast.LENGTH_LONG).show();
		back = (Button) findViewById(R.id.back);
		proceed = (Button) findViewById(R.id.proceed);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		// ================= on Back button press ============================
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//go_to_main();
				String r,g,b;
				String s = Environment.getExternalStorageDirectory().toString() + "/Captures/screen.png";
					//et.setText("path to the image file : " + s);
				try{
					
					int index=1;
					String Curr_blue_in_bits="";
					Bitmap bmp2 = BitmapFactory.decodeFile(s);				
					int width = bmp2.getWidth();
					int height = bmp2.getHeight();
					String msg=""; 
					int[] pixels = new int[width * height];
					bmp2.getPixels(pixels, 0, width, 0, 0, width, height);
					int textlength = pixels[0] & 0xff;
					et.append("\nLength of message is : "+textlength+"\n");
					int m=0;//
					String messege_in_bits = ""; 
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
								//getting form red color of pixel
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
					}
					et.append("Messege in its is : "+messege_in_bits);
					for(int i=0;i<messege_in_bits.length();i+=8){
						String temp = messege_in_bits.substring(i, i+8);
						//msg+=temp;
						msg+=toBinary(temp);
						//et.append("\n"+temp);
					}
					//et.append("Messsage is : " + msg);
					et.append("message is : "+msg);
					
				
					/*for (int y = 0; y < height; y++){
			            for (int x = 0; x < width; x++){
			            
			            	index = y*height+x;
			            	msgIndexBit = index%8;
			            	
			            	if(msgIndexBit == 0)
			            	{
			            		if(y==0 && x==0){
			            			
			            		}
			            		else{
			            			msg=msg+a;
			            			//msgIndex++;
			            		}
			            	}
			            	
			            	
			            	b = pixels[index] & 0xff;
			            	
			            	
			            	if(y==0 && x<5){
			            		et.append("\n"+x+" "+Integer.toString(b, 2));
			            	}
			            	else{
			            		break;
			            	}
			            	Curr_blue_in_bits = Integer.toString(b,2);
			            	while(Curr_blue_in_bits.length() != 8){
			            		Curr_blue_in_bits = '0'+Curr_blue_in_bits;
			            	}
			            	if(isSet(Curr_blue_in_bits.charAt(7))){
			            		a |= (msgIndexBit);
			            	}
			            	else
			            	{
			            		a &= ~(msgIndexBit);
			            	}
			            	
			            	
			            }
					}*/
				}
				catch(Exception e){
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					et.setText(e.getMessage());
				}
				//et.setText(msg);
				
				
			}
		});
		
		// =================== on proceed button press ===========================
		
		proceed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//converting image into bitmap file
				//Toast.makeText(getApplicationContext(), current_image_path, Toast.LENGTH_LONG).show();
				int r,g,b;
				try{
					
					//creating a bitmap
					
					Bitmap bmp = BitmapFactory.decodeFile(current_image_path);
					Bitmap bmp1 = bmp.copy(Bitmap.Config.ARGB_8888, true); 
					// ============================== Bitmap cofig data ==============================
					int width = bmp1.getWidth();
					int height = bmp1.getHeight();
					int[] pixels = new int[width * height];
					
					//=============================== setting message String ========================
					String str = "vikas";
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
					et.append("\nMessage in bits is : "+message_in_bits+"\n");
					//======================= editing bitmap file  ==================
					
					
					//============== Getting all the pixels from the bitmap =======================
					bmp1.getPixels(pixels, 0, width, 0, 0, width, height);
					
					// ========== changing pixel values (hiding text into image) ===========
					int m=0;// ======= counter for message in bits string ========= 
					for(int i=0;i<height;i++){
						for (int j = 0; j < width; j++){
							if(m >= message_in_bits.length()){
								break;
							}
							if(i==0 && j==0){
								bmp1.setPixel(i, j, Color.rgb(0,0,str.length()));
								et.append("\nlength of message is : "+str.length());
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
							
							et.append("\nred : "+Integer.toString(r,2)+"Green : "+Integer.toString(g,2)+"Blue : "+Integer.toString(b,2));
							//m++;
							
							}
						}						
					}
					
					
					//================ initializing msg index ===========================
					//int msgIndex = 0;
					
					// Toast.makeText(getApplicationContext(),"pixel is:"+pixels[200*200]+"red:"+r+"   green:"+g+"   blue:"+b , 10000).show();
					
					
					// ========================= processing each single pixel ========================
					/*for (int y = 0; y < height; y++){
					            for (int x = 0; x < width; x++){
					                     // ================== getting index of pixel ====================
					            		 int index = y * width + x;
					                     // ================== getting bit of current character from the string ======
					                     int msgbit = index%8;
					                     // ============= selecting character form the Message ==================
					                     if(msgbit==0){
					                    	 if(y==0 && x==0)
					                    	 {
					                    		
					                    	 }
					                    	 else{
					                    		 msgIndex++;
					                    	 }
					                    	 
					                    	
					                    	
					                     }
					                     	
					                     if(msgIndex >= str.length()){
				                    		 
				                    		 break;
				                    	 }
					                    // ======================== getting colors from pixels ========================
					                    // r = (pixels[index] >> 16) & 0xff;     // bitwise shifting
					                    // g = (pixels[index] >> 8) & 0xff;
					                     b = pixels[index] & 0xff;
					                     
					                     r= b;
					                     //index++; //no any significant use 
					                     
					                     int cur_char = str.charAt(msgIndex);
					                     Curr_char_in_bits = Integer.toString(cur_char,2);
					                     //et.append("\n"+ Curr_char_in_bits+"\n");
					                     //et.append(Integer.toString(pixels[index],2));
					                     
					                     //=================== making character size to 8 bits ========================
					                     while(Curr_char_in_bits.length() != 8)
					                     {
					                    	 Curr_char_in_bits = '0'+ Curr_char_in_bits;  
					                     }
					                     
					                     if(isSet(Curr_char_in_bits.charAt(msgbit))){
					                    	 b |= 0b1;
					                     }
					                     else{
					                    	 b &= ~0b1;
					                     }
					                     //pixels[index] = b;
					                     if(y==0 && x<5){
					                    	 et.append(""+x +" "+Integer.toString(pixels[index],2)+"\n"+Integer.toString(r,2)+"\n"+ Integer.toString(b,2)+"\n"); 
					                     }
					                     
					            }
					}*/
					
					//String t = Integer.toString(b,2);
					
					
					//====================== storing bitmap generated as png file =============================
					String path = Environment.getExternalStorageDirectory().toString();
				    OutputStream fOutputStream = null;
				   
				    File dir = new File(path + "/Captures/");
				    if(!dir.exists()) {
				        dir.mkdirs();
				    }
				    
				    File file = new File(path + "/Captures/", "screen.png");
				    try {
				        fOutputStream = new FileOutputStream(file);

				        bmp1.compress(Bitmap.CompressFormat.PNG, 100, fOutputStream);
				        et.append("message stored sucessfully");
				        fOutputStream.flush();
				        fOutputStream.close();

				        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
				    } catch (FileNotFoundException e) {
				        e.printStackTrace();
				        Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
				        return;
				    } catch (IOException e) {
				        e.printStackTrace();
				        Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
				        return;
				    }
				    
				    
					//Toast.makeText(getApplicationContext(), "Messasge hidden sucessfully!!" , Toast.LENGTH_LONG).show();
				}
				catch(Exception e){
					Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}
	
	// ======================== function to redirect to main_Activity ===========================
	public void go_to_main(){
		/*Intent to_main = new Intent(Proceed_page.this,MainActivity.class);
		to_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(to_main);
		finish();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.proceed_page, menu);
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
	
	//=========================== function to check if bit is set or not ========================
	public boolean isSet(char ch){
		
		if(ch == '1'){
		return true;
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		go_to_main();
	}
	
	public char toBinary(String toConvert){
		int sum=0;
		et.append("\n"+toConvert+"\n");
		int length_of_string= toConvert.length();
		for(int i=0;i<length_of_string;i++){
			int j = toConvert.charAt(i)-48;
			et.append(""+j);
			sum+=j*(Math.pow(2, (length_of_string-i-1)));
			//sum=(int) (sum + ((int)(toConvert.charAt(i)))*Math.pow(2,(8-i)));
		}
		return (char)sum;
	}
}
