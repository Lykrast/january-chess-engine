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

public class MoveTypeBishopReflecting extends MoveType {

	public MoveTypeBishopReflecting(MoveMode mode, DirectionMode directionMode) {
		super(mode, directionMode);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        int maxX = p.getBoard().getWidth() - 1, maxY = p.getBoard().getHeight() - 1;
        
        for (Position dir : Direction.DIAGONAL_POS)
        {
        	if (!dir.match(getDirectionMode(), p)) continue;
        	//Using ints instead of positions because of the bounces
        	int x = dir.getX(), y = dir.getY();
        	Position pos = start.offset(x, y);
        	while (!pos.equals(start) && list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		//Bounce
        		boolean bounceX = (pos.getX() + x < 0 || pos.getX() + x > maxX),
        				bounceY = (pos.getY() + y < 0 || pos.getY() + y > maxY);
        		if (bounceX && bounceY) break; //Bouncing on corner does not cover more ground
        		else if (bounceX) x = -x; //Bouncing on x
        		else if (bounceY) y = -y; //Bouncing on y
        		pos = pos.offset(x, y);
        	}
        }
        
        return list;
	}

	@Override
	public MoveType create(JsonObject elem, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeBishopReflecting(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "BishopReflecting";
	}

}
