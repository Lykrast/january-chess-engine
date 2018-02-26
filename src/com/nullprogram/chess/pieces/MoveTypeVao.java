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

public class MoveTypeVao extends MoveType {
	//TODO merge with Cannon to use less classes
	public MoveTypeVao() {
		super(MoveMode.MOVE_CAPTURE);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
		Position home = p.getPosition();
        for (Position dir : Direction.DIAGONAL_POS)
        {
        	Position pos = home.offset(dir);
        	boolean hurdle = false;
        	while (p.getBoard().inRange(pos))
        	{
        		//Hurdle found, find a capture
        		if (hurdle)
        		{
        			//Found another piece, end the mvoe
        			if (!p.getBoard().isFree(pos))
        			{
        				list.addCaptureOnly(new Move(home, pos));
        				break;
        			}
        		}
        		//Move until we find a hurdle
        		else
        		{
        			if (!list.addMove(new Move(home, pos)))
        			{
        				//Is there an hurdle ?
        				if (!p.getBoard().isFree(pos))
        				{
        					hurdle = true;
        				}
        				else break;
        			}
        		}
        		
        		pos = pos.offset(dir);
        	}
        }
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeVao();
	}

	@Override
	public String getTypeName() {
		return "Vao";
	}

}
