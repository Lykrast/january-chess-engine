package com.nullprogram.chess.pieces;

import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveListWrapperCoordinator extends MoveListWrapper {
	//A new one wrapper is created per move calculation, so we only need to find the royal piece once
	private Position royal;

	public MoveListWrapperCoordinator(Piece piece, IMoveList list) {
		super(piece, list);
		royal = piece.getBoard().findKing(piece.getSide());
	}

	@Override
	protected Move modify(Move move) {
		//No royal piece to coordinate with
		if (royal == null) return move;
        
        //Add at the end of the current move
        Move innermost = move.getLast();

        //Coordinate
        innermost = coordinate(innermost, move.getDest().getX(), royal.getY());
        innermost = coordinate(innermost, royal.getX(), move.getDest().getY());
        
        return move;
	}
	
	/**
	 * Append the coordination capture if possible. Returns either the move or the added move that is now the deepest in the sequence.
	 */
	private Move coordinate(Move move, int x, int y)
	{
		Position target = new Position(x, y);
		if (piece.getBoard().isEnemy(target, piece.getSide()))
		{
			Move coordination = new Move(target, null);
			move.setNext(coordination);
			return coordination;
		}
		else return move;
	}

}
