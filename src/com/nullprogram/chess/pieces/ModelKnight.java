package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

public class ModelKnight extends Model {
	
	public ModelKnight() {
		super("Knight", 3.0);
	}

	@Override
	public MoveList getMoves(Piece p, boolean checkCheck) {
        MoveList list = new MoveList(p.getBoard(), checkCheck);
        list = MoveUtil.getKnightMoves(p, list);
        return list;
	}

}
