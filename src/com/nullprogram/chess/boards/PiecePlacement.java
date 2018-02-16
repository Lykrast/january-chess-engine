package com.nullprogram.chess.boards;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Piece.Side;

public class PiecePlacement {
	private int x, y;
	private Piece.Side side;
	private Model model;
	
	public PiecePlacement(int x, int y, Side side, Model model) {
		this.x = x;
		this.y = y;
		this.side = side;
		this.model = model;
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
