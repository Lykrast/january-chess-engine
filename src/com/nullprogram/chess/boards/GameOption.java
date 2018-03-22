package com.nullprogram.chess.boards;

import com.nullprogram.chess.pieces.Piece;

public class GameOption {
    /** Name of this option. */
    private String name;
    
	private PiecePlacement[] placements;
	private String[] promotionsW, promotionsB;
	
	public GameOption(String name, PiecePlacement[] placements, String[] promotionsW, String[] promotionsB)
	{
		this.name = name;
		this.placements = placements;
		this.promotionsW = promotionsW;
		this.promotionsB = promotionsB;
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

	public String[] getPromotions(Piece.Side side) {
		return side == Piece.Side.WHITE ? promotionsW : promotionsB;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
