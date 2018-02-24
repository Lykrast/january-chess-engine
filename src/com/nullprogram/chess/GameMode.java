package com.nullprogram.chess;

import com.nullprogram.chess.boards.PiecePlacement;

public class GameMode implements Comparable<GameMode>{
    /** Name of this gamemode. */
    private String name;
    
	private int width, height;
	private PiecePlacement[] placements;
	/** The list of pieces a generic Pawn can promote to in this gamemode */
	private String[] promotions;
	
	public GameMode(String name, int width, int height, PiecePlacement[] placements, String[] promotions)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.placements = placements;
		this.promotions = promotions;
	}
	
	public void initialize(Board b)
	{
    	b.setWidth(width);
    	b.setHeight(height);
    	b.clear();
    	placePieces(b);
	}
	
	public void placePieces(Board b)
	{
		for (PiecePlacement p : placements)
    	{
    		p.place(b);
    	}
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
	
	public String[] getPromotions() {
		return promotions;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(GameMode arg0) {
		return name.compareTo(arg0.name);
	}
}
