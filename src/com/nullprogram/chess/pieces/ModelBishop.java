package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

public class ModelBishop extends Model {
	
	public ModelBishop() {
		super("Bishop", 3.0);
	}

	@Override
    public final MoveList getMoves(Piece p, final boolean check) {
        MoveList list = new MoveList(p.getBoard(), check);
        list = MoveUtil.getBishopMoves(p, list);
        return list;
    }

}
