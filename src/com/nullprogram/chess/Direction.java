package com.nullprogram.chess;

import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.Piece.Side;
import com.nullprogram.chess.pieces.movement.MoveType.DirectionMode;

public enum Direction {
	UP(new Position(0,1)),
	UP_RIGHT(new Position(1,1)),
	RIGHT(new Position(1,0)),
	DOWN_RIGHT(new Position(1,-1)),
	DOWN(new Position(0,-1)),
	DOWN_LEFT(new Position(-1,-1)),
	LEFT(new Position(-1,0)),
	UP_LEFT(new Position(-1,1));
	
	private Position pos;
	
	public static final Direction[] ALL = Direction.values(),
			ORTHOGONAL = {UP, RIGHT, DOWN, LEFT},
			DIAGONAL = {UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT};
	public static final Position[] ALL_POS = new Position[8],
			ORTHOGONAL_POS = new Position[4],
			DIAGONAL_POS = new Position[4];
	static
	{
		for (int i = 0; i < ALL.length; i++) ALL_POS[i] = ALL[i].getPosition();
		for (int i = 0; i < ORTHOGONAL.length; i++) ORTHOGONAL_POS[i] = ORTHOGONAL[i].getPosition();
		for (int i = 0; i < DIAGONAL.length; i++) DIAGONAL_POS[i] = DIAGONAL[i].getPosition();
	}
	
	private Direction(Position pos) {
		this.pos = pos;
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public Direction opposite() {
		return ALL[(ordinal() + 4) % 8];
	}

    /**
     * Check if this Direction goes in the specified direction (if used as an offset).
     * 
     * @param mode DirectionMode to check against
     * @param piece Piece to check for, for the forward and back directions
     * @return true if this goes according to the mode, false otherwise
     */
    public boolean match(DirectionMode mode,  Piece piece) {
    	return match(mode, piece.getSide());
    }
    
    /**
     * Check if this Direction goes in the specified direction (if used as an offset).
     * 
     * @param mode DirectionMode to check against
     * @param side Side to check for, for the forward and back directions
     * @return true if this goes according to the mode, false otherwise
     */
    public boolean match(DirectionMode mode,  Side side) {
    	switch(this)
    	{
    	case UP:
    		if (side == Side.WHITE) return mode.forward();
    		else return mode.back();
    	case DOWN:
    		if (side == Side.WHITE) return mode.back();
    		else return mode.forward();
		case LEFT:
			return mode.left();
		case RIGHT:
			return mode.right();
		case DOWN_LEFT:
			return DOWN.match(mode, side) || mode.left();
		case DOWN_RIGHT:
			return DOWN.match(mode, side) || mode.right();
		case UP_LEFT:
			return UP.match(mode, side) || mode.left();
		case UP_RIGHT:
			return UP.match(mode, side) || mode.right();
    	}
    	return false;
    }

}
