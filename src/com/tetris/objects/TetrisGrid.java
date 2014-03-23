package com.tetris.objects;

import java.util.List;

import junit.framework.Assert;
import android.content.Context;
import android.util.Log;

import com.framework.RenderUtilities;
import com.framework.gameobjects.GameObject;
import com.tetris.controller.TetrisController;
import com.tetris.controller.TetrisController.Direction;
import com.tetris.controller.TetrisController.State;

public class TetrisGrid extends GameObject {
	final private static String TAG = "TETRISGRID";
	
	final private int GRIDWIDTH = 10;
	final private int GRIDHEIGHT = 22;
	
	final private int RENDERPOSX = 2;
	final private int RENDERPOSY = 27;
	
	private short[] mGridValues;
	private TetrisBlock mTB;
	private final TetrisNextBlock mNB;
	
	//private final InputManager mIM;
	
	private List<TetrisController.Action> mActions;
	
	private boolean mEndGame = false;
	
	private TetrisController mTetrisController = new TetrisController(TetrisController.State.RUNNING);
	
	public TetrisGrid(TetrisNextBlock nb) {
		mNB = nb;
		
		//TODO: remove
		//setGridCellValue(0, 0, (short)1);
		//setGridCellValue(9, 0, (short)1);
		//TODO: remove
		//setGridCellValue(9, 21, (short)1);
		//setGridCellValue(8, 21, (short)1);
		//setGridCellValue(7, 21, (short)1);
		//setGridCellValue(6, 21, (short)1);
		//setGridCellValue(5, 21, (short)1);
		//setGridCellValue(4, 21, (short)1);
		//setGridCellValue(3, 21, (short)1);
		//setGridCellValue(2, 21, (short)1);
		//setGridCellValue(1, 21, (short)1);
		//setGridCellValue(0, 21, (short)1);
	}
	
	public void setGridCellValue(int x, int y, short val) {
		Assert.assertTrue("Column value for the TetrisGrid cannot be negative: " + x, x >= 0);
		Assert.assertTrue("Column value for the TetrisGrid is too large: " + x, x < GRIDWIDTH);
		Assert.assertTrue("Row value for the TetrisGrid cannot be negative: " + y, y >= 0);
		Assert.assertTrue("Row value for the TetrisGrid is too large: " + y, y < GRIDHEIGHT);
		
		mGridValues[y * GRIDWIDTH + x] = val;
	}
	
	public short getGridCellValue(int x, int y) {
		Assert.assertTrue("Column value for the TetrisGrid cannot be negative: " + x, x >= 0);
		Assert.assertTrue("Column value for the TetrisGrid is too large: " + x, x < GRIDWIDTH);
		Assert.assertTrue("Row value for the TetrisGrid cannot be negative: " + y, y >= 0);
		Assert.assertTrue("Row value for the TetrisGrid is too large: " + y, y < GRIDHEIGHT);
		
		return mGridValues[y * GRIDWIDTH + x];
	}
	
	private boolean canLowerBlock() {
		Assert.assertNotNull(mTB);
		
		boolean res = true;
		
		//The block should not fall below the grid
		int minRow = mTB.getYPosition() - mTB.getHighestRowFilled();
		if(minRow <= (RENDERPOSY - GRIDHEIGHT + 1)) 
		{
			return res = false;
		} 
		
		//The block should come to rest on top of other blocks already merged into the grid
		final int x = mTB.getXPosition() - RENDERPOSX; 
		final int y = RENDERPOSY - mTB.getYPosition();
		for(int i = mTB.getHighestRowFilled(); i >= 0; --i) {
			for(int j = 0; j < TetrisBlock.BLOCKGRIDWIDTH; ++j) {
				if(mTB.getBlockGridCellValue(j, i) != 0) {
					//Log.v(TAG, "Block type: " + mTB.getColor() + " x: " + x + " j: " + j);
					if(getGridCellValue(x + j , y + i + 1) != 0) {
						return res = false; 
					}
				}
			}
		}

		return res;
	}
	
	private void mergeTetrisBlockWithGrid() {
		for(int i = mTB.getHighestRowFilled(); i >= 0; --i) {
			for(int j = 0; j < TetrisBlock.BLOCKGRIDWIDTH; ++j) {
				if(mTB.getBlockGridCellValue(j, i) != 0) {
					int x = mTB.getXPosition() - RENDERPOSX;
					int y = RENDERPOSY - mTB.getYPosition();
					setGridCellValue(x + j , y + i, (short) mTB.getColor());
				}
			}
		}
	}
	
	private void removeOneLine(int row) {
		for(int i = row; i > 0; --i) {
			for(int j = 0; j < GRIDWIDTH; ++j) {
				setGridCellValue(j, i, getGridCellValue(j, i - 1));
			}
		}
		
		for(int i = 0; i < GRIDWIDTH; ++i) {
			setGridCellValue(i, 0, (short) 0);
		}
	}
	
	private void removeFullLines() {
		//TODO: check how I can do an animated line removal 
		
		//Check all rows, if a is full, then remove it
		for(int i = 0; i < GRIDHEIGHT; ++i) {
			for(int j = 0; j < GRIDWIDTH; ++j) {
				if(getGridCellValue(j, i) == 0) break;
				if(j == GRIDWIDTH - 1) {
					removeOneLine(i);
				}
			}
		}
	}
	
	private boolean isEndGame() {
		//TODO Check if we can set the next block
		TetrisBlock tmp = mTB;
		
		mTB = mNB.getNextBlock();
		
		if(isOverlap()) {
			mEndGame = true;
			mTB = tmp;
			return true;
		} else {
			mNB.generateNextBlock();
			return false;
		}
	}
	
	public boolean getEndGame() {
		return mEndGame;
	}
	
	public void startGame() {
		mEndGame = false;
		
		mGridValues = new short[GRIDWIDTH * GRIDHEIGHT];
		
		for(int i = 0; i < mGridValues.length; ++i) {
			mGridValues[i] = 0;
		}
		
		mTB = mNB.getNextBlock();
		mNB.generateNextBlock();
	}
	
	public void setActions(List<TetrisController.Action> actions) {
		mActions = actions;
	}
	
	@Override
	public void update(Context context, float deltaTS) {
		//TODO: This logic to control blocks and tetrisgrid should probably reside in a gamescene...
		for(int i = 0; i < mActions.size(); ++i) {
			moveBlock(mActions.get(i).getDirection());
		}
		
		if(canLowerBlock()) {
			mTB.lowerTetrisBlock();
		} else {
			mergeTetrisBlockWithGrid();
			removeFullLines();
			if(mEndGame = isEndGame()) {
				mTetrisController.setState(State.END);
			}
		}
	}
	
	@Override 
	public void initialize(Context context) {
		super.initialize(context);
		
		startGame();
	}
		
	private void moveBlock(Direction dir) {
		switch(dir) {
		case LEFT:
			tryMoveLeft();
			break;
		case RIGHT:
			tryMoveRight();
			break;
		case UP:
			tryRotateRight();
			break;
		case DOWN:
			tryRotateLeft();
			break;
		default:
			break;
		}
	}
		
	private boolean isOverlap() {
		boolean res = false;
		
		for(int i = mTB.getLowestRowFilled(); i <= mTB.getHighestRowFilled(); ++i) {
			for(int j = mTB.getLowestColumnFilled(); j <= mTB.getHighestColumnFilled(); ++j) {
				final int tmpX = mTB.getXPosition() - RENDERPOSX; 
				final int tmpY = RENDERPOSY - mTB.getYPosition();
				int checkPosX = tmpX + j;
				int checkPosY = tmpY + i;
				
				if((mTB.getBlockGrid()[i * TetrisBlock.BLOCKGRIDWIDTH + j] != 0) && (getGridCellValue(checkPosX, checkPosY) != 0)) {
					return res = true;
				}
			}
		}
		
		return res;
	}
	
	private boolean isOutsideGrid() {
		boolean res = false;
		
		for(int i = mTB.getLowestRowFilled(); i <= mTB.getHighestRowFilled(); ++i) {
			for(int j = mTB.getLowestColumnFilled(); j <= mTB.getHighestColumnFilled(); ++j) {
				final int tmpX = mTB.getXPosition() - RENDERPOSX; 
				final int tmpY = RENDERPOSY - mTB.getYPosition();
				int checkPosX = tmpX + j;
				int checkPosY = tmpY + i;
							
				if((checkPosX < 0) || 
					checkPosX >= GRIDWIDTH || 
					checkPosY < 0 ||
					checkPosY >= GRIDHEIGHT) {
					return res = true;
				} 
			}
		}
		
		return res;
	}
	
	private void tryRotateLeft() {
		mTB.rotateLeft();
		
		if(isOutsideGrid()) { 
			mTB.rotateRight();
			return;
		}
		
		if(isOverlap()) {
			mTB.rotateRight();
			return;
		}
	}
	
	private void tryRotateRight() {
		mTB.rotateRight();
		
		if(isOutsideGrid()) { 
			mTB.rotateLeft();
			return;
		}
		
		if(isOverlap()) {
			mTB.rotateLeft();
			return;
		}
	}  
	
	private void tryMoveRight() {
		mTB.setXPosition(mTB.getXPosition() + 1);
		
		if(isOutsideGrid()) { 
			mTB.setXPosition(mTB.getXPosition() - 1);
			return;
		}
		
		if(isOverlap()) {
			mTB.setXPosition(mTB.getXPosition() - 1);
			return;
		}		
	}
	
	private void tryMoveLeft() {
		mTB.setXPosition(mTB.getXPosition() - 1);
		
		if(isOutsideGrid()) { 
			mTB.setXPosition(mTB.getXPosition() + 1);
			return;
		}
		
		if(isOverlap()) {
			mTB.setXPosition(mTB.getXPosition() + 1);
			return;
		}		
	}	
	
	public void render() {
		//render the tetrisblock
		//TODO this should actually be done by the TetrisBlock itself imo...
		if(mTB != null) {
			short[] tb = mTB.getBlockGrid();
			
			for(int i = 0; i < TetrisBlock.BLOCKGRIDHEIGHT; ++i) {
				for(int j = 0; j < TetrisBlock.BLOCKGRIDWIDTH; ++j) {
					float offsetX = -1.0f + (mTB.getXPosition() + j) * 2.0f/20.0f;
					float offsetY = -1.0f + (mTB.getYPosition() - i) * 2.0f/30.0f; 
					if(tb[i * TetrisBlock.BLOCKGRIDWIDTH + j] == 1) {
						RenderUtilities.Square.renderSquare(offsetX, offsetY, RenderUtilities.Color.values()[mTB.getColor() - 1]);	
					}
				}
			}
		}
		
		//render the grid
		for(int i = 0; i < GRIDHEIGHT; ++i) {
			for(int j = 0; j < GRIDWIDTH; ++j) {
				float offsetX = -1.0f + (RENDERPOSX + j) * 2.0f/20.0f;
				float offsetY = -1.0f + (RENDERPOSY - i) * 2.0f/30.0f; 
				short tmp = mGridValues[i * GRIDWIDTH + j];
				if(tmp != 0) {
					RenderUtilities.Square.renderSquare(offsetX, offsetY, RenderUtilities.Color.values()[tmp - 1]);	
				}
			}
		}
	}
}
