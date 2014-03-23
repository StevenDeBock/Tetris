package com.framework.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class InputManager implements OnTouchListener {
	private static final String TAG = "INPUTMANAGER";
	
	private List<MotionEvent> mMotionEventBuffer1;
	private List<MotionEvent> mMotionEventBuffer2;
	private List<MotionEvent> mMotionEventBufferActive;
			
	public InputManager() {
		mMotionEventBuffer1 = new ArrayList<MotionEvent>();
		mMotionEventBuffer2 = new ArrayList<MotionEvent>();
		mMotionEventBufferActive = mMotionEventBuffer1;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		Log.d(TAG, "onTouch() was called!");
		
		synchronized(this) {
			if(e.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d(TAG, "ACTION_DOWN");
			} else if(e.getAction() == MotionEvent.ACTION_MOVE) {
				Log.d(TAG, "ACTION_MOVE");
			} else if(e.getAction() == MotionEvent.ACTION_UP) {
				Log.d(TAG, "ACTION_UP");
			}
			//mMotionEventBufferActive.add(new MotionEvent(e));
			mMotionEventBufferActive.add(MotionEvent.obtain(e));
		}
		return true;
	}
	
	public void clear() {
		synchronized(this) {
			mMotionEventBuffer1.clear();
			mMotionEventBuffer2.clear();
		}
	}
	
	public List<MotionEvent> getMotionEventBuffer() {
		synchronized(this) {
			if(mMotionEventBufferActive == mMotionEventBuffer1) {
				mMotionEventBufferActive = mMotionEventBuffer2;
				
				return mMotionEventBuffer1;
			} else {
				mMotionEventBufferActive = mMotionEventBuffer1;
				
				return mMotionEventBuffer2;
			}
		}
	}
}
