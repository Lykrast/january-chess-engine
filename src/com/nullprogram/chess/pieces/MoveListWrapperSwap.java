package com.nullprogram.chess.pieces;

import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveListWrapperSwap extends MoveListWrapper {
	public MoveListWrapperSwap(Piece piece, IMoveList list) {
		super(piece, list);
	}

	@Override
	protected Move modify(Move move) {
		Position dest = move.getDest();
		if (piece.getBoard().inRange(dest) && !piece.getBoard().isEmpty(dest)) move.setSwap(true);
        
        return move;
	}

}
