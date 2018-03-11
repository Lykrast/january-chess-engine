package com.nullprogram.chess.pieces.movement.modifier;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.util.AreaSided;

public class MoveListWrapperRestriction extends MoveListWrapper {
	private AreaSided area;
	private boolean invert;
	
	public MoveListWrapperRestriction(Piece piece, IMoveList list, AreaSided area, boolean invert) {
		super(piece, list);
		this.area = area;
		this.invert = invert;
	}

	@Override
	protected Move modify(Move move) {
		Position dest = move.getDest();
		if (area.inside(dest, piece.getBoard(), piece.getSide())) return invert ? null : move;
		else return invert ? move : null;
	}

}
