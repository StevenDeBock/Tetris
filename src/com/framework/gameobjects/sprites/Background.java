package com.framework.gameobjects.sprites;


public class Background extends Sprite {
	final private static String TAG = "BACKGROUND";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
		
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/
	
	/**
	 * This function creates the requested sprite
	 * 
	 * @param topLeftCornerNDC
	 * @param widthNDC
	 * @param heightNDC
	 * @param texture
	 * @param transparency
	 */
	public Background(float[] topLeftCornerNDC, float widthNDC, float heightNDC, String fileName, Transparency transparency) {
		super(topLeftCornerNDC, widthNDC, heightNDC, fileName, transparency);
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
}
