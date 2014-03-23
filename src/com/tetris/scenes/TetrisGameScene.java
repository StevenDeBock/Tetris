package com.tetris.scenes;

import java.util.List;

import junit.framework.Assert;
import android.content.Context;

import com.framework.Game;
import com.framework.gamescenes.GameScene;
import com.framework.input.InputManager;
import com.tetris.controller.TetrisController;
import com.tetris.objects.TetrisBackground;
import com.tetris.objects.TetrisGrid;
import com.tetris.objects.TetrisNextBlock;

public class TetrisGameScene extends GameScene {
	final private static String TAG = "TETRISGAMESCENE";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	public enum TetrisGameSceneState {
		UNDEFINED,
		RESTART,
		RUNNING,
		END
	}
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private TetrisBackground mBackground;
	private TetrisGrid mGrid;
	private TetrisNextBlock mNextBlock;
	private TetrisGameSceneState mState;
	private InputManager mInputManager;
	private final TetrisController mTetrisController;
	
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/
	
	public TetrisGameScene(TetrisController tetrisController) {
		mTetrisController = tetrisController;
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
	@Override
    protected void createGameObjects() {
    	mBackground = new TetrisBackground();
    	mNextBlock = new TetrisNextBlock();
    	
    	mInputManager = new InputManager();
    	mGrid = new TetrisGrid(mNextBlock);
    }
    
	@Override
    protected void addGameObjects() {
    	this.addGameObject(mBackground);
    	this.addGameObject(mNextBlock);
    	this.addGameObject(mGrid);
	}

	public void setState(TetrisGameSceneState state) {
		mState = state;
	}
	
	public TetrisGameSceneState getState() {
		return mState;
	}
	
	@Override
	public void update(Context context, float deltaTS) {
		//Get the new inputs
		List<TetrisController.Action> tmp = mTetrisController.parseAndConsumeMotionEvents(Game.getInputManager().getMotionEventBuffer());
		mGrid.setActions(tmp);
		
		//Update all objects
		super.update(context, deltaTS);
		
		//Evaluate the game state
		if(mState == TetrisGameSceneState.RESTART) {
			//Reset the field and create the new blocks
			mGrid.startGame();
			
			mState = TetrisGameSceneState.RUNNING;
		} else if(mState == TetrisGameSceneState.RUNNING) {
			if(mGrid.getEndGame()) {
				mState = TetrisGameSceneState.END;
			}
		} else if(mState == TetrisGameSceneState.END) {
			//Nothing to do...
			Assert.assertFalse(TAG + "should no longer update when it has ended!", true);
		}
	}
}