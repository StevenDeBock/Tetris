package com.tetris.objects;

import junit.framework.Assert;

import com.framework.gameobjects.GameObject;

public abstract class TetrisBlock extends GameObject {
	public static final int BLOCKGRIDWIDTH = 4;
	public static final int BLOCKGRIDHEIGHT = 4;
	public static final int BLOCKGRIDSIZE = BLOCKGRIDWIDTH * BLOCKGRIDHEIGHT;
	
	public enum TetrisBlockTypes {
		TETRISBLOCKI, 
		TETRISBLOCKJ, 
		TETRISBLOCKL, 
		TETRISBLOCKO, 
		TETRISBLOCKS, 
		TETRISBLOCKT, 
		TETRISBLOCKZ
	};
	
	protected short mColor;
	protected short mRotation = 0;
	protected short mBlockGrid0[];
	protected short mBlockGrid1[];
	protected short mBlockGrid2[];
	protected short mBlockGrid3[];
	
	private int mPosXTB; //screencoordinates
	private int mPosYTB; //screencoordinates
	
	private final int RENDERPOSX = 5;  //Leave 2 * 16x16 pixels blocks at left
	private final int RENDERPOSY = 27; //Leave 5 * 16x16 pixels blocks at bottom
	
	public TetrisBlock()
	{
		mPosXTB = RENDERPOSX;  
		mPosYTB = RENDERPOSY; 
	}
	
	public void resetPosition() {
		mPosXTB = RENDERPOSX;  
		mPosYTB = RENDERPOSY; 
	}
		
	public short getColor() {
		return mColor;
	}
	
	public void rotateLeft() {
		--mRotation;
		if(mRotation < 0) {
			mRotation = 3;
		}
	}
	
	public void rotateRight() {
		++mRotation;
		if(mRotation > 3) {
			mRotation = 0;
		}
	}
	
	public short[] getBlockGrid() {
		switch(mRotation) {
		case 0:
			return mBlockGrid0;
		case 1:
			return mBlockGrid1;
		case 2:
			return mBlockGrid2;
		case 3:
			return mBlockGrid3;
		default:
			return null;
		}
	}
	
	public short[] getBlockGridLeftRotation() {
		int blockGridIndex = mRotation - 1;
		if(blockGridIndex < 0) {
			blockGridIndex = 3;
		}
		
		switch(blockGridIndex) {
		case 0:
			return mBlockGrid0;
		case 1:
			return mBlockGrid1;
		case 2:
			return mBlockGrid2;
		case 3:
			return mBlockGrid3;
		default:
			return null;
		}
	}

	public short[] getBlockGridRightRotation() {
		int blockGridIndex = mRotation + 1;
		if(blockGridIndex > 3) {
			blockGridIndex = 0;
		}
		
		switch(blockGridIndex) {
		case 0:
			return mBlockGrid0;
		case 1:
			return mBlockGrid1;
		case 2:
			return mBlockGrid2;
		case 3:
			return mBlockGrid3;
		default:
			return null;
		}
	}

	public short getBlockGridCellValue(int x, int y) {
		Assert.assertTrue("Column value for the TetrisGrid cannot be negative: " + x, x >= 0);
		Assert.assertTrue("Column value for the TetrisGrid is too large: " + x, x < BLOCKGRIDWIDTH);
		Assert.assertTrue("Row value for the TetrisGrid cannot be negative: " + y, y >= 0); 
		Assert.assertTrue("Row value for the TetrisGrid is too large: " + y, y < BLOCKGRIDHEIGHT); 
		
		return getBlockGrid()[y * BLOCKGRIDWIDTH + x];
	}
	
	public int getXPosition() {
		return mPosXTB;
	}
	
	public int getYPosition() {
		return mPosYTB;
	}
	
	public void setXPosition(int xPos) {
		mPosXTB = xPos;
	}
	
	public void setYPosition(int yPos) {
		mPosYTB = yPos;
	}
	
	public void lowerTetrisBlock() {
		--mPosYTB;
	}
	
	public int getHighestRowFilled()
	{
		short[] blockGrid = getBlockGrid();
		
		int index = BLOCKGRIDSIZE - 1;
		for(int i = index; i >= 0; --i) {
			if(blockGrid[i] != 0) {
				index = i;
				break;
			}
		}
		
		return (index / BLOCKGRIDWIDTH);
	}

	public int getLowestRowFilled()
	{ 
		short[] blockGrid = getBlockGrid();
		
		int index = -1;
		for(int i = 0; i < BLOCKGRIDHEIGHT; ++i) {
			for(int j = 0; j < BLOCKGRIDWIDTH; ++j) {
				if(blockGrid[i * BLOCKGRIDWIDTH + j] != 0) {
					index = i;
					break;
				}
			}
			if(index != -1) break;
		}
		
		return index;
	}
	
	public int getLowestColumnFilled() {
		short[] blockGrid = getBlockGrid();
		int res = -1;
		
		for(int i = 0; i < BLOCKGRIDWIDTH; ++i) {
			for(int j = 0; j < BLOCKGRIDWIDTH; ++j) {
				if(blockGrid[j * BLOCKGRIDWIDTH + i] != 0) {
					res = i;
					break;
				}
			}
			if(res != -1 ) break;
		}
		
		return res;
	}
	
	public int getHighestColumnFilled() {
		short[] blockGrid = getBlockGrid();
		int res = -1;
		
		for(int i = BLOCKGRIDWIDTH - 1; i >= 0; --i) {
			for(int j = 0; j < BLOCKGRIDWIDTH; ++j) {
				if(blockGrid[j * BLOCKGRIDWIDTH + i] != 0) {
					res = i;
					break;
				}
			}
			if(res != -1 ) break;
		}
		
		return res;
	}
}