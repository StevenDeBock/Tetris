package com.tetris.scenes;

import java.util.List;

import android.content.Context;

import com.framework.Game;
import com.framework.gameobjects.sprites.Sprite;
import com.framework.gameobjects.sprites.Sprite.Transparency;
import com.framework.gamescenes.GameScene;
import com.tetris.controller.TetrisController;
import com.tetris.objects.TetrisBackground;

public class TetrisEndScene extends GameScene {
	final private static String TAG = "TETRISENDSCENE";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	public enum TetrisEndSceneState {
		UNDEFINED,
		DEFAULT,
		RESTARTREQUESTED
	}
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private TetrisEndSceneState mState;
	private TetrisBackground mBackground;
	private Sprite mGameOverSprite;
	private Sprite mRestartSprite;
	private final TetrisController mTetrisController;
	
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/
	
	public TetrisEndScene(TetrisController tetrisController) {
		mTetrisController = tetrisController;
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	public void setState(TetrisEndSceneState state) {
		mState = state;
	}
	
	public TetrisEndSceneState getState() {
		return mState;
	}
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
	@Override
    protected void createGameObjects() {
		mBackground = new TetrisBackground();
		
		float[] topLeftCornerNDC1 = {-1.0f,  (float)2/3};
		mGameOverSprite = new Sprite(topLeftCornerNDC1, 2.0f, (float)2/3, "textures/gameover.png", Transparency.TRANSPARENT);
		
		float[] topLeftCornerNDC2 = {-1.0f,  (float)0};
		mRestartSprite = new Sprite(topLeftCornerNDC2, 2.0f, (float)2/3, "textures/restart.png", Transparency.TRANSPARENT);
    }
    
	@Override
    protected void addGameObjects() {
		this.addGameObject(mBackground);
		this.addGameObject(mGameOverSprite);
		this.addGameObject(mRestartSprite);
    }
	
	@Override
	public void initialize(Context context) {
		super.initialize(context);
		
		mState = TetrisEndSceneState.DEFAULT;
	}
	
	@Override
	public void update(Context context, float deltaTS) {
		//Get the new inputs
		List<TetrisController.Action> tmp = mTetrisController.parseAndConsumeMotionEvents(Game.getInputManager().getMotionEventBuffer());
			
		for(int i = 0; i < tmp.size(); ++i) {
			//TODO: should get a better button implementation!!
			if(tmp.get(i).getY() >= 240 && tmp.get(i).getY() < 360) {
				mState = TetrisEndSceneState.RESTARTREQUESTED;
				//mEndGame = false;
				//mTetrisController.setState(State.END);
				//mTetrisController.clear();
				//mIM.clear();
				//restart();
				//break;
			}
		}
		
		//Update all objects
		super.update(context, deltaTS);
	}
}