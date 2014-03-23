package com.tetris.controller;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.util.Log;
import android.view.MotionEvent;

public class TetrisController {
	private static String TAG = "TETRISCONTROLLER";
	
	public enum ActionType {NULL, MOVE, CLICK};
	public enum Direction {NULL, LEFT, RIGHT, UP, DOWN};
	public enum State {NULL, RUNNING, END};
	
	public class Action 
	{
		ActionType mActionType;
		float X;
		float Y;
		Direction D;
		
		public Action(ActionType at, Direction d) {
			mActionType = at;
			X = 0.0f;
			Y = 0.0f;
			D = d;
		}
		
		public Action(ActionType at, float x, float y) {
			mActionType = at;
			X = x;
			Y = y;
			D = Direction.NULL;
		}
		
		public ActionType getActionType() {
			return mActionType;
		}
				
		public Direction getDirection() {
			return D;
		}
		
		public float getX() {
			return X;
		}
		
		public float getY() {
			return Y;
		}
	}
	
	private State mState = State.NULL;
	private float mXPositions[] = new float[2];
	private float mYPositions[] = new float[2];
	private List<Action> mResults = new ArrayList<Action>();
	
	private final Action MACTIONLEFT = new Action(ActionType.MOVE, Direction.LEFT);
	private final Action MACTIONRIGHT = new Action(ActionType.MOVE, Direction.RIGHT);
	private final Action MACTIONUP = new Action(ActionType.MOVE, Direction.UP);
	private final Action MACTIONDOWN = new Action(ActionType.MOVE, Direction.DOWN);
	
	public TetrisController(State state) {
		mState = state;
	}
	
	public void setState(State state) {
		mState = state;
		mResults.clear();
	}
	
	public State getState() {
		return mState;
	}
	
	/*
	private void clear() {
		mResults.clear();
	}
	*/
	
	/**
	 * Interprete the touch events that have been received up till now, based on the current state.
	 * Make sure to call clear after the action in the returned list have been used.
	 * 
	 * @param mes
	 * @return a list of actions parsed from the touch events.
	 */
	public List<Action> parseAndConsumeMotionEvents(List<MotionEvent> mes) {
		mResults.clear();
		
		switch(mState) {
		case RUNNING:
			return parseMotionEventsRunning(mes);
		case END:
			return parseMotionEventsEnd(mes);
		case NULL:
			Assert.assertTrue("Needs to be implemented", false);
			return null;
		default:
			Assert.assertTrue("Needs to be implemented", false);
			return null;
		}
	}
	
	private List<Action> parseMotionEventsEnd(List<MotionEvent> mes) {
		for(int i = 0; i < mes.size(); ++i) {
			MotionEvent e = mes.get(i);
		
			switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				mResults.add(new Action(ActionType.CLICK, e.getX(), e.getY()));
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			}
		}
		
		return mResults;
	}
	
	private List<Action> parseMotionEventsRunning(List<MotionEvent> mes) {
		for(int i = 0; i < mes.size(); ++i) {
			MotionEvent e = mes.get(i);
		
			switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d(TAG, "Action down");
				mXPositions[0] = e.getX();
				mYPositions[0] = e.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d(TAG, "Action move");
				break;
			case MotionEvent.ACTION_UP:
				Log.d(TAG, "Action up");
				mXPositions[1] = e.getX();
				mYPositions[1] = e.getY();
				mResults.add(getMovement());
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			}
		}
		
		mes.clear();
		
		return mResults;
	}
	
	private Action getMovement() {
		float deltaX = mXPositions[0] - mXPositions[1];
		float deltaY = mYPositions[0] - mYPositions[1];
		
		if(Math.abs(deltaX) > Math.abs(deltaY)) {
			if((deltaX < 0)) {
				return MACTIONRIGHT;
			} else {
				return MACTIONLEFT;
			}
		} else {
			if((deltaY < 0)) {
				return MACTIONUP;
			} else {
				return MACTIONDOWN;
			}			
		}
	}
}
