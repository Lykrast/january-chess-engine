package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeBishop extends MoveType {

	public MoveTypeBishop(MoveMode mode) {
		super(mode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : Direction.DIAGONAL_POS)
        {
        	Position pos = start.offset(dir);
        	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir);
        	}
        }
        
        return list;
	}

	@Override
	public MoveType create(JsonObject elem, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeBishop(mode);
	}

}
