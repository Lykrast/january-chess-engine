package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.IMoveList;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeRider extends MoveType {
	/**
	 * An array of all 8 directions the rider can move.
	 */
	private final Position[] directions;

	public MoveTypeRider(MoveMode mode, int near, int far) {
		super(mode);
		directions = new Position[8];
		directions[0] = new Position(near, far);
		directions[1] = new Position(far, near);
		directions[2] = new Position(-far, near);
		directions[3] = new Position(-far, -near);
		directions[4] = new Position(far, -near);
		directions[5] = new Position(near, -far);
		directions[6] = new Position(-near, -far);
		directions[7] = new Position(-near, far);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position dir : directions)
        {
        	Position pos = start.offset(dir);
        	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir);
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRider(mode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "Rider";
	}

}
