package com.nullprogram.chess.pieces.movement.modifier;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;

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
