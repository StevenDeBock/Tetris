package com.framework.gameobjects;

import android.content.Context;

public abstract class GameObject {
	final private static String TAG = "GAMEOBJECT";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private boolean mIsInitialized = false;
	
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/

	public GameObject() {	
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	public boolean isInitialized() {
		return mIsInitialized;
	}
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
		
	public void initialize(Context context) {
		mIsInitialized = true;
	}
	
	public void update(Context context, float deltaTS) {
	}
	
	public void render() {
	}
}
