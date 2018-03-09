package com.nullprogram.chess.boards;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.pieces.Model;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.Piece.Side;

public class PiecePlacement {
	private int x, y, xlen, ylen;
	private Piece.Side side;
	private Model model;
	
	public PiecePlacement(int x, int y, int xlen, int ylen, Side side, Model model) {
		this.x = x;
		this.y = y;
		this.xlen = xlen;
		this.ylen = ylen;
		this.side = side;
		this.model = model;
	}
	
	public void place(Board b)
	{
		if (xlen > 1)
		{
			for (int i = 0; i < xlen; i++)
			{
				placeY(x + i, b);
			}
		}
		else
		{
			placeY(x, b);
		}
	}
	
	private void placeY(int x, Board b)
	{
		if (ylen > 1)
		{
			for (int i = 0; i < ylen; i++)
			{
				b.setPiece(x, y + i, new Piece(side, model));
			}
		}
		else
		{
			b.setPiece(x, y, new Piece(side, model));
		}
	}
	
	public PiecePlacement(int x, int y, Side side, Model model) {
		this(x, y, 1, 1, side, model);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Piece.Side getSide() {
		return side;
	}
	public Model getModel() {
		return model;
	}
}
