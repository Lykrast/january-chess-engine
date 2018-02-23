package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeGrasshopper extends MoveType {
	private static final Position[] DIRECTIONS = {
			new Position(1, 0),
			new Position(1, 1),
			new Position(0, 1),
			new Position(-1, 1),
			new Position(-1, 0),
			new Position(-1, -1),
			new Position(0, -1),
			new Position(1, -1)
	};

	public MoveTypeGrasshopper(MoveMode mode) {
		super(mode);
	}

	@Override
	public MoveList getMoves(Piece p, MoveList list) {
		Position home = p.getPosition();
        for (Position dir : DIRECTIONS) 
        {
        	Position pos = home.offset(dir);
        	while (p.getBoard().inRange(pos))
        	{
        		if (!p.getBoard().isFree(pos))
        		{
        			list.add(new Move(home, pos.offset(dir)), getMoveMode());
        			break;
        		}
        		
        		pos = pos.offset(dir);
        	}
        }
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeGrasshopper(mode);
	}

}
