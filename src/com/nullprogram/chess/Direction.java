package com.nullprogram.chess;

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

}
