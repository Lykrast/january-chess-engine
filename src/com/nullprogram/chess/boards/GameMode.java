package com.nullprogram.chess.boards;

import com.nullprogram.chess.pieces.Piece;

public class GameMode implements Comparable<GameMode>{
    /** Name of this gamemode. */
    private String name;
    
	private int width, height;
	private PiecePlacement[] placements;
	
	/** The list of pieces a generic Pawn can promote to in this gamemode */
	private String[] promotions;

	/** Says whether each side started with a royal piece, used by AI */
	private boolean hasRoyalWhite, hasRoyalBlack;

	/** If false, any royal piece can be checkmated to win, if true then all of them must be (so some can be captured) */
	private boolean checkMultiple;
	
	public GameMode(String name, int width, int height, PiecePlacement[] placements, String[] promotions, boolean checkMultiple)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.placements = placements;
		this.promotions = promotions;
		this.checkMultiple = checkMultiple;
		
		hasRoyalWhite = false;
		hasRoyalBlack = false;
		for (PiecePlacement p : placements)
		{
			if (p.getModel().isRoyal())
			{
				if (p.getSide() == Piece.Side.WHITE) hasRoyalWhite = true;
				else hasRoyalBlack = true;
			}
		}
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
	
	/**
	 * Says whether or not the given side started with a royal piece on the board.
	 * Used for AI evaluation.
	 * @param side side to check
	 * @return true if, on the starting position, the given side has a royal piece
	 */
	public boolean hasRoyal(Piece.Side side) {
		return side == Piece.Side.WHITE ? hasRoyalWhite : hasRoyalBlack;
	}
	
	/**
	 * Gives the policy for handling check (and checkmate) on multiple royal pieces.
	 * <br>
	 * False means that checkmating any royal piece wins, so all of them need to be clear of check at the same time.
	 * <br>
	 * True means that all royal pieces must be checkmated, which means that a royal piece can be put into capture
	 * as long as others are protected.
	 * 
	 * @return whether all royal pieces must be checkmated or just one
	 */
	public boolean checkMultiple() {
		return checkMultiple;
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
