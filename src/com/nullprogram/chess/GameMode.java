package com.nullprogram.chess;

import com.nullprogram.chess.boards.PiecePlacement;

public class GameMode {
    /** Name of this gamemode. */
    private String name;
    
	private int width, height;
	private PiecePlacement[] placements;
	
	public GameMode(String name, int width, int height, PiecePlacement... placements)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.placements = placements;
	}
	
	public String getName() {
		return name;
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
