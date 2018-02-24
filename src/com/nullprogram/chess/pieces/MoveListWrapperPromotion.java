package com.nullprogram.chess.pieces;

import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Piece.Side;

public class MoveListWrapperPromotion extends MoveListWrapper {
	private String promoted;
	private int start, end;

	public MoveListWrapperPromotion(Piece piece, IMoveList list, String promoted, int rows) {
		super(piece, list);
		this.promoted = promoted;
		if (piece.getSide() == Side.BLACK)
		{
			start = 0;
			end = rows - 1;
		}
		else
		{
			end = piece.getBoard().getHeight() - 1;
			start = end - rows + 1;
		}
	}

	@Override
	protected Move modify(Move move) {
		int y = move.getDest().getY();
        if (y < start || y > end) {
            return move;
        }
        
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
