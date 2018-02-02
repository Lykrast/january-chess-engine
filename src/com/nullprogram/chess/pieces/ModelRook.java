package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

public class ModelRook extends Model {
	
	public ModelRook() {
		super("Rook", 5.0);
	}

	@Override
    public final MoveList getMoves(Piece p, final boolean check) {
        MoveList list = new MoveList(p.getBoard(), check);
        list = MoveUtil.getRookMoves(p, list);
        return list;
    }

}
