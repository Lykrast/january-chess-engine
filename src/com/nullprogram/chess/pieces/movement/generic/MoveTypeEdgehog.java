package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.boards.Board;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;

public class MoveTypeEdgehog extends MoveType {
	//TODO make more generic
	public MoveTypeEdgehog(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
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
            	if (!dir.match(getDirectionMode(), p)) continue;
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
            	if (!dir.match(getDirectionMode(), p)) continue;
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
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeEdgehog(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Edgehog";
	}

}
