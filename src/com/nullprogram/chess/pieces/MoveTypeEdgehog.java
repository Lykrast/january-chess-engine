package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

public class MoveTypeEdgehog extends MoveType {
	//TODO make more generic
	public MoveTypeEdgehog(MoveMode mode) {
		super(mode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        Board b = p.getBoard();
        
        if (onEdge(start, b))
        {
        	//On edge, move as normal
            for (Position dir : Direction.ALL_POS)
            {
            	Position pos = start.offset(dir);
            	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
            	{
            		pos = pos.offset(dir);
            	}
            }
        }
        else
        {
        	//Not on edge, must end on an edge
            for (Position dir : Direction.ALL_POS)
            {
            	Position pos = start.offset(dir);
            	//Handling if right next to the edge
        		if (onEdge(pos, b))
        		{
        			list.add(new Move(start, pos), getMoveMode());
        		}
        		else while (p.getBoard().isFree(pos))
            	{
            		if (onEdge(pos, b))
            		{
            			//Should only be able to make a single move
            			list.add(new Move(start, pos), getMoveMode());
            			break;
            		}
            		pos = pos.offset(dir);
            	}
            }
        }
        
        return list;
	}
	
	private boolean onEdge(Position p, Board b)
	{
		return p.getX() == 0 || p.getY() == 0 || p.getX() == b.getWidth() - 1 || p.getY() == b.getHeight() - 1;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeEdgehog(mode);
	}

}