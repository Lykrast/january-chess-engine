package com.nullprogram.chess;

import com.nullprogram.chess.boards.PiecePlacement;

public class GameMode {
	private int width, height;
	private PiecePlacement[] placements;
	
	public GameMode(int width, int height, PiecePlacement... placements)
	{
		this.width = width;
		this.height = height;
		this.placements = placements;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public PiecePlacement[] getPlacements() {
		return placements;
	}
}
