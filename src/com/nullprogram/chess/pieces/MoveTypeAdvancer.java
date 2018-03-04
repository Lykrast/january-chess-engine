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

public class MoveTypeAdvancer extends MoveType {
	//TODO make possible in rook/bishop style
	public MoveTypeAdvancer(DirectionMode directionMode) {
		super(MoveMode.MOVE_CAPTURE, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : Direction.ALL_POS)
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	Position pos = start.offset(dir);
        	Move move = new Move(start, pos);
        	while (p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir);
        		//Check for capture
        		if (p.getBoard().isEnemy(pos, p.getSide()))
        		{
        			move.setNext(new Move(pos, null));
        		}
        		
        		if (!list.addMove(move) || move.getNext() != null) break;
        		move = new Move(start, pos);
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeAdvancer(directionMode);
	}

	@Override
	public String getTypeName() {
		return "Advancer";
	}

}
