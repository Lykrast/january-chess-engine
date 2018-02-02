package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Model;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;

public class ModelQueen extends Model {
	
	public ModelQueen() {
		super("Queen", 9.0);
	}

	@Override
    public final MoveList getMoves(Piece p, final boolean check) {
        MoveList list = new MoveList(p.getBoard(), check);
        /* Take advantage of the Bishop and Rook implementations. */
        list = MoveUtil.getRookMoves(p, list);
        list = MoveUtil.getBishopMoves(p, list);
        return list;
    }

	

}
