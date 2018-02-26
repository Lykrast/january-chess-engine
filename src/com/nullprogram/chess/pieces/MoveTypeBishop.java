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

	public MoveTypeBishop(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : Direction.DIAGONAL_POS)
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	Position pos = start.offset(dir);
        	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir);
        	}
        }
        
        return list;
	}

	@Override
	public MoveType create(JsonObject elem, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeBishop(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Bishop";
	}

}
