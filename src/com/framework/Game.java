package com.framework;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import junit.framework.Assert;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.framework.gamescenes.GameScene;
import com.framework.input.InputManager;

public abstract class Game extends Activity {
	final private static String TAG = "GAME";
	
	/********************************************************************/
	/* 						ENUMS										*/
	/********************************************************************/	
	
	/********************************************************************/
	/* 						MEMBERS										*/
	/********************************************************************/
	
	private boolean mIsInitialized = false;
	
	private GameScene mActiveGameScene = null;
	private ArrayList<GameScene> mGameScenes = null;
	
	private static InputManager mIM;
	
	/********************************************************************/
	/* 						CONSTANTS									*/
	/********************************************************************/
	
	/********************************************************************/
	/* 						CONSTRUCTORS								*/
	/********************************************************************/

	public Game() {	
		mGameScenes = new ArrayList<GameScene>();
	}
	
	/********************************************************************/
	/* 						GETTERS and SETTERS							*/
	/********************************************************************/
	
	public boolean isInitialized() {
		return mIsInitialized;
	}
	
    protected GameScene getActiveGameScene() {
    	return mActiveGameScene;
    }
    
    protected void setActiveGameScene(GameScene gs) {
    	mActiveGameScene = gs;
    }
    
	
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/
	// Overrides from Activity
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {	
    	Log.d(TAG, "onCreate() called");
    	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
		WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
        super.onCreate(savedInstanceState);
        
        mIM = new InputManager();
        Assert.assertNotNull(TAG + " failed to create the InputManager!", mIM);
        
        GameGLSurfaceView gglsv = new GameGLSurfaceView(this);
        gglsv.setOnTouchListener(mIM);
        setContentView(gglsv);
    }
	
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	Log.d(TAG, "onStart() called");
    }
    
    @Override 
    protected void onResume() {
    	super.onResume();
    	
    	Log.d(TAG, "onResume() called");
    }
       
    @Override 
    protected void onRestart() {
    	super.onRestart();
    	
    	Log.d(TAG, "onRestart() called");
    }
   
    @Override 
    protected void onPause() {
    	super.onPause();
    	
    	Log.d(TAG, "onPause() called");
    }
    
    @Override 
    protected void onStop() {
    	super.onStop();
    	
    	Log.d(TAG, "onStop() called");
    }
    
    @Override 
    protected void onDestroy() {
    	super.onDestroy();
    	
    	Log.d(TAG, "onDestroy() called");
    }
    
    public static InputManager getInputManager() {
    	return mIM;
    }
    
	/********************************************************************/
	/* 						FUNCTIONALITIES								*/
	/********************************************************************/

    /**
     * Mandatory function, all GameScenes should be created here.
     * You should add all GameScenes that were created in addGameScenes().
     */
    abstract protected void createGameScenes();
    
    /**
     * Mandatory function, all GameScenes should be added to the Game here.
     * You typically want to add all GameScenes that were created in 
     * createGameScenes().
     */
    abstract protected void addGameScenes();
    
    /**
     * Mandatory function, it should return the currently active GameScene.
     * 
     * @return the GameScene that is currently active.
     */
    abstract protected GameScene determineActiveGameScene();
    
    /**
     * Add a GameScene
     * 
     * @param gs a GameScene
     */
    final protected void addGameScene(GameScene gs) {
    	mGameScenes.add(gs);
    }
    
    /**
     * This function can be overridden, but should be called when overridden. This
     * function will initialize all GameScene that were added through addGameScenes(). 
     * 
     * @param context
     */
    protected void initialize(Context context) {
    	//TODO clean up
    	GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
    	
    	//TODO clean up
    	RenderUtilities.Square.initialize(context);
    	
    	for(GameScene gs : mGameScenes) {
    		gs.initialize(this);
    	}
    }
 
    /**
     * This function can be overridden, but should be called when overridden. This 
     * function will call update() on the current active game scene.
     * 
     * @param context
     * @param deltaTS The time in seconds passed since this function was called previously.
     */
    protected void update(Context context, float deltaTS) {
    	mActiveGameScene = determineActiveGameScene();
    	mActiveGameScene.update(context, deltaTS);
    }
    
    /**
     * This function can be overridden, but should be called when overridden. This 
     * function will call render() on the current active game scene.
     */   
    protected void render() {
    	mActiveGameScene.render();
    }
    
	/********************************************************************/
	/* 						INNER CLASSES									*/
	/********************************************************************/
    
    final private class GameGLSurfaceView extends GLSurfaceView {

		public GameGLSurfaceView(Context context) {
			super(context);
			
			construct();
		}
		
    	public GameGLSurfaceView(Context context, AttributeSet attrs) { 
    		super(context, attrs); 
    	
    		construct();
    	}
    	
    	private void construct() {
			GameRenderer gr = new GameRenderer(this.getContext());
			setEGLContextClientVersion(2);
	        setRenderer(gr);
	        setRenderMode(RENDERMODE_CONTINUOUSLY);    		
    	}
    	
    	final private class GameRenderer implements GLSurfaceView.Renderer {
    		final static String TAG = "TetrisRenderer";    	
    		
    		private long mTime;
    		private float mTimeAccS;
    		
    		private Context mContext;
    		
    		public GameRenderer(Context context) {
    			mContext = context;
    		}
    		
			@Override
			public void onDrawFrame(GL10 gl) {
				//Log.d(TAG, "onDrawFrame() called");
				
				float deltaTS = (System.nanoTime() - mTime) / 1000000000.0f;
				mTime = System.nanoTime();
				mTimeAccS += deltaTS;
				if(mTimeAccS > 0.1f) {
					update(mContext, mTimeAccS);
					mTimeAccS = 0.0f;
				}
				
				render();
			}

			@Override
			public void onSurfaceChanged(GL10 gl, int width, int height) {
				Log.d(TAG, "onSurfaceChanged() called");
			}

			@Override
			public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				Log.d(TAG, "onSurfaceCreated() called");
				
				createGameScenes();
				addGameScenes();
				initialize(mContext);
				mActiveGameScene = determineActiveGameScene();
				
				mTime = System.nanoTime();
				mTimeAccS = 0.0f;
			}
    	}
    }
}
