package com.nullprogram.chess.pieces;

import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;

public class MoveTypeKnight extends MoveType {

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
        list = MoveUtil.getKnightMoves(p, list);
        return list;
	}

}
