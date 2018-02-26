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

public class MoveTypeGrasshopper extends MoveType {
	//TODO make possible in rook/bishop style
	public MoveTypeGrasshopper(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position home = p.getPosition();
        for (Position dir : Direction.ALL_POS)
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	Position pos = home.offset(dir);
        	while (p.getBoard().inRange(pos))
        	{
        		if (!p.getBoard().isFree(pos))
        		{
        			//Jump over hurdle
        			list.add(new Move(home, pos.offset(dir)), getMoveMode());
        			break;
        		}
        		
        		pos = pos.offset(dir);
        	}
        }
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeGrasshopper(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Grasshopper";
	}

}
