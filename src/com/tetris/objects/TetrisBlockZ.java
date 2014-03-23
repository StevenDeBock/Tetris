package com.tetris.objects;

public class TetrisBlockZ extends TetrisBlock {
	public TetrisBlockZ() {
		super();
		this.mColor = 7;
		this.mBlockGrid0 = new short[] {
				1, 1, 0, 0,
				0, 1, 1, 0, 
				0, 0, 0, 0,
				0, 0, 0, 0 };
		this.mBlockGrid1 = new short[] {
				0, 0, 1, 0,
				0, 1, 1, 0, 
				0, 1, 0, 0,
				0, 0, 0, 0 };
		this.mBlockGrid2 = new short[] {
				0, 0, 0, 0,
				1, 1, 0, 0, 
				0, 1, 1, 0,
				0, 0, 0, 0 };
		this.mBlockGrid3 = new short[] {
				0, 1, 0, 0,
				1, 1, 0, 0, 
				1, 0, 0, 0,
				0, 0, 0, 0 };
	}
}