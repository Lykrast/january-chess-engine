package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;

public class MoveTypeLocust extends MoveType {
	//TODO make possible in rook/bishop style
	public MoveTypeLocust(DirectionMode directionMode) {
		super(MoveMode.CAPTURE, directionMode);
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
        			//Is enemy?
        			if (p.getBoard().isFree(pos, p.getSide()))
        			{
            			//Jump over enemy hurdle and capture it
            			Move move = new Move(home, pos.offset(dir));
            			move.setNext(new Move(pos, null));
            			list.addMove(move);
            			break;
        			}
        			else break;
        		}
        		
        		pos = pos.offset(dir);
        	}
        }
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeLocust(directionMode);
	}

	@Override
	public String getTypeName() {
		return "Locust";
	}

}
