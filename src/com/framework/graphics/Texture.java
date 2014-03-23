package com.framework.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.framework.Utilities;

public class Texture {
	final private static String TAG = "Texture";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/
	
	public enum Filter {LINEAR}; 	//TODO: what other filter modes do we need to implement?
	public enum WrapMode {REPEAT};  //TODO: what other wrap modes do we need to implement?
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private int mID = -1;
	private int mWidth;
	private int mHeight;
	private int mMinFilter = GLES20.GL_LINEAR;
	private int mMagFilter = GLES20.GL_LINEAR;
	private int mWrapModeS = GLES20.GL_REPEAT;
	private int mWrapModeT = GLES20.GL_REPEAT;
	final private String mNAME;

	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/
	
	/**
	 * Constructor for the Texture class, a texture will be created using 
	 * the default Linear interpolation for the minification and 
	 * magnification filter. The wrapmodes will be default set to repeat.
	 * You need to initialize the texture before utilization...
	 * <p>
	 * TODO: need to unlock different filter and wrapmodes for the texture
	 * by providing multiple constructors
	 * <p>
	 * TODO: a texture manager might be interesting to monitor the 
	 * creation of textures, memory consumption, error logging... Correct?
	 * It should also prevent from loading the same texture twice.
	 * 
	 * @param	name	filename of the figure that needs to be used as 
	 * 					texture
	 */
	public Texture(String name) {
		mNAME = name;
	}
	
	/*
	public Texture(String name, int minFilter, int magFilter, int wrapModeS, int wrapModeT) {
		mNAME = name;
		
	}
	*/
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	public String getName() {
		return mNAME;
	}
	
	/**
	 * Gets the texture ID as it was defined by OpenGL.
	 * 
	 * @return -1 in case no texture is loaded.
	 */
	public int getID() {
		return mID;
	}
	
	/**
	 * Return the width of the texture in pixels
	 * 
	 * @return the width of the texture in pixels
	 */
	public int getWidthPx() {
		return mWidth;
	}
	
	/**
	 * Return the height of the texture in pixels
	 * 
	 * @return the height of the texture in pixels
	 */
	public int getHeightPx() {
		return mHeight;
	}
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
		
	public void initialize(Context context) {
		loadFromAssets(context);
	}
	
	/**
	 * Texture will be created in (GPU-)memory
	 * 
	 * @param 	context	android.content.Context
	 * @return -1	should be returned in case loading failed, TODO
	 */
	private int loadFromAssets(Context context) {
		int[] mTexIDTmp = new int[1];
		GLES20.glGenTextures(1, mTexIDTmp, 0);
		mID = mTexIDTmp[0];
		
		//GLES20.glActiveTexture(GLES20.GL_TEXTURE0); //Do we require an active texture unit??
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mID);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, mMinFilter);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, mMagFilter);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, mWrapModeS);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, mWrapModeT);

	    Bitmap tmp = Utilities.getBitmapFromAsset(context, mNAME);
	    mWidth = tmp.getWidth();
	    mHeight = tmp.getHeight();
	    if(tmp != null) {
	    	GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tmp, 0);
	    }
	    
	    //TODO return correct error code, set mID to -1 in case it failed
	    return mID;
	}
	
	/**
	 * Wrapper function to bind the texture
	 */
	public void bind() {
		if(mID != -1) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mID);
		}
	}
	
	/**
	 * Removes the Texture from (GPU-)memory. You should dispose of
	 * your texture if it is no longer being used to free memory.
	 */
	public void dispose() {
		if(mID != -1) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mID);
			int[] mTexIDTemp = {mID};
			GLES20.glDeleteTextures(1, mTexIDTemp, 0);
			mID = -1;
		}
	}
}
