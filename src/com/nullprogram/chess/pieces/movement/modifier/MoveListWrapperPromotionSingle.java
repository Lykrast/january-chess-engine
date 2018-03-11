package com.nullprogram.chess.pieces.movement.modifier;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;
import com.nullprogram.chess.util.AreaSided;

public class MoveListWrapperPromotionSingle extends MoveListWrapper {
	private String promoted;
	private AreaSided area;

	public MoveListWrapperPromotionSingle(Piece piece, IMoveList list, String promoted, AreaSided area) {
		super(piece, list);
		this.promoted = promoted;
		this.area = area;
	}

	@Override
	protected Move modify(Move move) {
        if (!area.inside(move.getDest(), piece.getBoard(), piece.getSide())) return move;
        
        //Add at the end of the current move
        Move innermost = move.getLast();
        
        innermost.setNext(new Move(move.getDest(), null)); // remove the piece
        Move upgrade = new Move(null, move.getDest());
        upgrade.setReplacement(promoted); // put the replacement
        upgrade.setReplacementSide(piece.getSide());
        innermost.getNext().setNext(upgrade);
        
        return move;
	}

}
