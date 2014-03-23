package com.tetris;

import junit.framework.Assert;
import android.content.Context;

import com.framework.Game;
import com.framework.gamescenes.GameScene;
import com.tetris.controller.TetrisController;
import com.tetris.controller.TetrisController.State;
import com.tetris.scenes.TetrisEndScene;
import com.tetris.scenes.TetrisGameScene;
import com.tetris.scenes.TetrisEndScene.TetrisEndSceneState;
import com.tetris.scenes.TetrisGameScene.TetrisGameSceneState;

public class Tetris extends Game {
	final static String TAG = "TETRIS";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private TetrisGameScene mTGS;
	private TetrisEndScene mTES;
	private TetrisController mTetrisController;
	
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/

	public Tetris() {
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
		
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/	
	@Override
    protected void createGameScenes() {
		//TODO: should clean this up
    	//mTGS = new TetrisGameScene();
		//mTES = new TetrisEndScene();

    }
	
    @Override
    protected void addGameScenes() {
    	//TODO: should clean this up
		//this.addGameScene(mTGS);
		//this.addGameScene(mTES);
    }
    
    @Override
    protected void initialize(Context context) {
    	mTetrisController = new TetrisController(State.RUNNING);
    	Assert.assertNotNull(TAG + " failed to create the TetrisController", mTetrisController);
    	
    	mTGS = new TetrisGameScene(mTetrisController);
    	this.addGameScene(mTGS);
    	
    	mTES = new TetrisEndScene(mTetrisController);
		this.addGameScene(mTES);
		
    	super.initialize(context);
    	
    	this.setActiveGameScene(mTGS);
    	mTGS.setState(TetrisGameSceneState.RUNNING);
    }
    
    @Override
    protected GameScene determineActiveGameScene() {
    	GameScene res = null;
    	
    	if(this.getActiveGameScene() == mTGS) {
    		if(mTGS.getState() == TetrisGameSceneState.RESTART) {
    			res = mTGS;
    		} else if(mTGS.getState() == TetrisGameSceneState.RUNNING) {
        		res = mTGS;
        	} else if(mTGS.getState() == TetrisGameSceneState.END) {
        		mTetrisController.setState(State.END);
        		Game.getInputManager().clear();
        		res = mTES;
        	}
    	} else if(this.getActiveGameScene() == mTES) {
    		if(mTES.getState() == TetrisEndSceneState.RESTARTREQUESTED) {
    			//Restart the game
    			mTGS.setState(TetrisGameSceneState.RESTART);
    			mTES.setState(TetrisEndSceneState.DEFAULT);
    			mTetrisController.setState(State.RUNNING);
    			Game.getInputManager().clear();
    			res = mTGS;
    		} else {
    			res = mTES;
    		}
    	} else {
    		Assert.assertTrue(TAG + " unexpected else clause executed!", false);
    	}
    	
    	Assert.assertNotNull(TAG + " null is an invalid GameScene!", res);
    	return res;
    }
}