package com.nullprogram.chess.pieces.movement.modifier;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveListWrapper;

public class MoveListWrapperRestriction extends MoveListWrapper {
	private int xmin, xmax, ymin, ymax;
	
	public MoveListWrapperRestriction(Piece piece, IMoveList list, int xmin, int xmax, int ymin, int ymax) {
		super(piece, list);
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}

	@Override
	protected Move modify(Move move) {
		Position dest = move.getDest();
		//-1 means unbounded in that direction
        if ((xmin != -1 && dest.getX() < xmin)
        		|| (xmax != -1 && dest.getX() > xmax)
        		|| (ymin != -1 && dest.getY() < ymin)
        		|| (ymax != -1 && dest.getY() > ymax))
        	return null;
		
		return move;
	}

}
