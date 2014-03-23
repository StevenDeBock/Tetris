package com.framework.gamescenes;

import java.util.ArrayList;

import android.content.Context;

import com.framework.gameobjects.GameObject;

public abstract class GameScene {
	final private static String TAG = "GAMESCENE";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private ArrayList<GameObject> mGameObjects = null;
	protected int mState = GSState.UNDEF; 
	
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/
	
	public GameScene() {
		mGameObjects = new ArrayList<GameObject>();
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
	
	//TODO
	//abstract public void setState();
    //abstract public int getState();
	
    abstract protected void createGameObjects();
    abstract protected void addGameObjects();
    
	public final void addGameObject(GameObject go) {
		mGameObjects.add(go);
	}
    
	public void initialize(Context context) {
		createGameObjects();
		addGameObjects();
		
		for (GameObject go : mGameObjects) {
			go.initialize(context);
		}
	}
	
	public void update(Context context, float deltaTS) {
		for (GameObject go : mGameObjects) {
			go.update(context, deltaTS);
		}
	}

	public void render() {
		for (GameObject go : mGameObjects) {
			go.render();
		}
	}
	
	public abstract static class GSState {
		public static final int UNDEF = 0;
	}
}
