package com.framework;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utilities {
	
	/********************************************************************/
	/* 						DUMMY										*/
	/********************************************************************/
	
	/**
	 * This function allows to homogenize the assertion strings that are
	 * thrown 
	 * 
	 * @param	TAG			the class name that throws the assertion
	 * @param	function	the function name that throws the assertion
	 * @param	message		used to send some additional information
	 * @return	the formatted string
	 */
	//public static String createAssertString(String TAG, String function, String message) {
	//	return TAG + ": " + function + ": " + message;
	//}
	
	/**
	 * Create a bitmap from an Asset file
	 * 
	 * @param 	context		android.content.Context
	 * @param 	fileName	filename of the asset to be decoded as bitmap
	 * @return	returns the bitmap, returns null in case of failure
	 */
	public static Bitmap getBitmapFromAsset(Context context, String fileName) {
		AssetManager am = context.getAssets();
		
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = am.open(fileName);
			bitmap = BitmapFactory.decodeStream(is);
		} catch(IOException e) {
			bitmap = null;
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			} catch(IOException e) {
				// Exception can be ignored
			}
		}
		
		return bitmap;
	}
	
	//returns the string from the given path
	//returns null in case an Exception happened
	//Untested version
	public static String readStringFromFile(String filePath, Charset charset) {
		String res = null;
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(filePath);
			
			byte[] tmpBuffer = new byte[1024];
			
			while(fis.read(tmpBuffer, 0, 1024) >= 0) {
				res += new String(tmpBuffer, charset);
			}
		} catch (FileNotFoundException e) {
			res = null;
		} catch (IOException ioe) {
			res = null;
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch(IOException ioe) {
				res = null;
			}
		}
		
		return res;
	}
	
	//returns the string from the given asset
	//returns null in case an Exception happened
	//Modified solution from http://stackoverflow.com/questions/9544737/read-file-from-assets
	public static String readStringFromAsset(Context context, String assetFileName, Charset charset) 
	{
	    StringBuilder sb = new StringBuilder();
	    String res = "";
	    InputStream is = null;
	    InputStreamReader isr = null;
	    BufferedReader input = null;
	    
	    try {
	        is = context.getResources().getAssets().open(assetFileName);
	        isr = new InputStreamReader(is);
	        input = new BufferedReader(isr);
	        String line = "";
	        
	        while ((line = input.readLine()) != null) {
	            sb.append(line);
	        }
	        res += sb.toString();
	    } catch (Exception e) {
	    	res = null;
	        e.getMessage();
	    } finally {
	        try {
	            if (isr != null)
	                isr.close();
	            if (is != null)
	                is.close();
	            if (input != null)
	                input.close();
	        } catch (Exception e2) {
	        	res = null;
	            e2.getMessage();
	        }
	    }
	    
	    return res;
	}
}
