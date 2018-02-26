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

public class MoveTypeRook extends MoveType {

	public MoveTypeRook(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : Direction.ORTHOGONAL_POS)
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
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRook(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Rook";
	}

}
