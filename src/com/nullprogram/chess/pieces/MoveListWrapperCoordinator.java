package com.nullprogram.chess.pieces;

import java.util.List;

import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveListWrapper;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveListWrapperCoordinator extends MoveListWrapper {
	//A new one wrapper is created per move calculation, so we only need to find the royal piece once
	private List<Position> royal;

	public MoveListWrapperCoordinator(Piece piece, IMoveList list) {
		super(piece, list);
		royal = piece.getBoard().findRoyal(piece.getSide());
	}

	@Override
	protected Move modify(Move move) {
		//No royal piece to coordinate with
		if (royal.isEmpty()) return move;
        
        //Add at the end of the current move
        Move innermost = move.getLast();

        //Coordinate
        for (Position p : royal) innermost = coordinate(innermost, p);
        
        return move;
	}

	/**
	 * Append the coordination captures of a given king if possible.
	 * Returns either the move or the added move that is now the deepest in the sequence.
	 */
	private Move coordinate(Move move, Position king)
	{
		move = coordinate(move, move.getDest().getX(), king.getY());
		move = coordinate(move, king.getX(), move.getDest().getY());
        
        return move;
	}
	
	/**
	 * Append a single coordination capture if possible.
	 * Returns either the move or the added move that is now the deepest in the sequence.
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
