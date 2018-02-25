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
	public MoveTypeAdvancer() {
		super(MoveMode.CAPTURE);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : Direction.ALL_POS)
        {
        	Position pos = start.offset(dir);
        	Move move = new Move(start, pos);
        	while (list.addMove(move) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir);
        		//Check for capture
        		if (!p.getBoard().isFree(pos) && p.getBoard().isFree(pos, p.getSide()))
        		{
        			move.setNext(new Move(pos, null));
        			break;
        		}
        		
        		move = new Move(start, pos);
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeAdvancer();
	}

}
