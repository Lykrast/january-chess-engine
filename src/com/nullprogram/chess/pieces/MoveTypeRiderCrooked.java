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

public class MoveTypeRiderCrooked extends MoveType {
	/**
	 * An array of all 8 directions the rider can move.
	 */
	private final Position[][] directions;

	public MoveTypeRiderCrooked(MoveMode mode, DirectionMode directionMode, int near, int far) {
		super(mode, directionMode);
		directions = new Position[8][2];
		//There's probably a better way to fill this
		//Or even to store this
		directions[0][0] = new Position(near, far);
		directions[0][1] = new Position(-near, far);
		directions[1][0] = new Position(far, near);
		directions[1][1] = new Position(far, -near);
		directions[2][0] = new Position(-far, near);
		directions[2][1] = new Position(-far, -near);
		directions[3][0] = new Position(-far, -near);
		directions[3][1] = new Position(-far, near);
		directions[4][0] = new Position(far, -near);
		directions[4][1] = new Position(far, near);
		directions[5][0] = new Position(near, -far);
		directions[5][1] = new Position(-near, -far);
		directions[6][0] = new Position(-near, -far);
		directions[6][1] = new Position(near, -far);
		directions[7][0] = new Position(-near, far);
		directions[7][1] = new Position(near, far);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position[] dir : directions)
        {
        	if (!dir[0].match(getDirectionMode(), p)) continue;
        	Position pos = start.offset(dir[0]);
        	int next = 1;
        	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir[next]);
        		next = next == 0 ? 1 : 0;
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRiderCrooked(mode, directionMode, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "RiderCrooked";
	}

}
