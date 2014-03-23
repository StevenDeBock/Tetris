package com.tetris.objects;

import java.util.Random;

import com.framework.RenderUtilities;
import com.framework.gameobjects.GameObject;

public class TetrisNextBlock extends GameObject {
	private final TetrisBlockI mTBI = new TetrisBlockI();
	private final TetrisBlockJ mTBJ = new TetrisBlockJ();
	private final TetrisBlockL mTBL = new TetrisBlockL();
	private final TetrisBlockO mTBO = new TetrisBlockO();
	private final TetrisBlockS mTBS = new TetrisBlockS();
	private final TetrisBlockT mTBT = new TetrisBlockT();
	private final TetrisBlockZ mTBZ = new TetrisBlockZ();
	
	private TetrisBlock mTB = mTBJ;
	private TetrisBlock mTBQueued = mTBJ;
	
	private final int RENDERPOSX = 14;
	private final int RENDERPOSY = 27; 
	
	final Random mRand = new Random();
	
	private int mBlockNo = 0;
	
	public TetrisNextBlock() {
	}
	
	public TetrisBlock getNextBlock() {
		mTB = mTBQueued;
		mTB.resetPosition();
		return mTB;
	}
	
	public void generateNextBlock() {
		int mBlockNoTmp = mRand.nextInt(7);
		while(mBlockNoTmp == mBlockNo) {
			mBlockNoTmp = mRand.nextInt(7); 
		}
		mBlockNo = mBlockNoTmp;
		
		switch(mBlockNo) {
		case 0:
			mTBQueued = mTBI;
			break;
		case 1:
			mTBQueued = mTBJ; 
			break;
		case 2:
			mTBQueued = mTBL;
			break;
		case 3:
			mTBQueued = mTBO;
			break;
		case 4:
			mTBQueued = mTBS;
			break;
		case 5:
			mTBQueued = mTBT;
			break;
		case 6:
			mTBQueued = mTBZ;
			break;
		default:
			return;
		}
		
		mTBQueued.setXPosition(RENDERPOSX);
		mTBQueued.setYPosition(RENDERPOSY);
	}
	
	@Override
	public void render() {
		if(mTBQueued == null) return;
		
		//render the grid
		for(int i = 0; i < TetrisBlock.BLOCKGRIDHEIGHT; ++i) {
			for(int j = 0; j < TetrisBlock.BLOCKGRIDWIDTH; ++j) {
				float offsetX = -1.0f + (RENDERPOSX + j) * 2.0f/20.0f;
				float offsetY = -1.0f + (RENDERPOSY - i) * 2.0f/30.0f; 
				if(mTBQueued.getBlockGrid()[i * TetrisBlock.BLOCKGRIDWIDTH + j] == 1) {
					RenderUtilities.Square.renderSquare(offsetX, offsetY, RenderUtilities.Color.values()[mTBQueued.getColor() - 1]);	
				}
			}
		}
	}
}
