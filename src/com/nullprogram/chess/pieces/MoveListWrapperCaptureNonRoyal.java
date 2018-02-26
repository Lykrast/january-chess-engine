package com.nullprogram.chess.pieces;

import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveListWrapperCaptureNonRoyal extends MoveListWrapper {
	public MoveListWrapperCaptureNonRoyal(Piece piece, IMoveList list) {
		super(piece, list);
	}

	@Override
	protected Move modify(Move move) {
		//Cannot capture a royal piece
		Position dest = move.getDest();
		if (piece.getBoard().inRange(dest))
		{
			Piece target = piece.getBoard().getPiece(dest);
			if (target != null && target.getModel().isRoyal()) return null;
		}
        return move;
	}

}
