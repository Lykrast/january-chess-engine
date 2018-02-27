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

public class MoveTypeRiderBent extends MoveType {
	/**
	 * An array of all 8 directions the rider can move.
	 */
	private final Position[][] directions;

	public MoveTypeRiderBent(MoveMode mode, DirectionMode directionMode, int firstnear, int firstfar, int secondnear, int secondfar) {
		super(mode, directionMode);
		directions = new Position[8][2];
		//There is probably a better way to fill this
		directions[0][0] = new Position(firstnear, firstfar);
		directions[1][0] = new Position(firstfar, firstnear);
		directions[2][0] = new Position(-firstfar, firstnear);
		directions[3][0] = new Position(-firstfar, -firstnear);
		directions[4][0] = new Position(firstfar, -firstnear);
		directions[5][0] = new Position(firstnear, -firstfar);
		directions[6][0] = new Position(-firstnear, -firstfar);
		directions[7][0] = new Position(-firstnear, firstfar);
		
		directions[0][1] = new Position(secondnear, secondfar);
		directions[1][1] = new Position(secondfar, secondnear);
		directions[2][1] = new Position(-secondfar, secondnear);
		directions[3][1] = new Position(-secondfar, -secondnear);
		directions[4][1] = new Position(secondfar, -secondnear);
		directions[5][1] = new Position(secondnear, -secondfar);
		directions[6][1] = new Position(-secondnear, -secondfar);
		directions[7][1] = new Position(-secondnear, secondfar);
	}

	@Override
	public IMoveList getMoves(Piece p, IMoveList list) {
        Position start = p.getPosition();
        
        for (Position[] dir : directions)
        {
        	if (!dir[0].match(getDirectionMode(), p)) continue;
        	Position pos = start.offset(dir[0]);
        	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir[1]);
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRiderBent(mode, directionMode, 
				JSONUtils.getMandatory(json, "firstnear").getAsInt(), 
				JSONUtils.getMandatory(json, "firstfar").getAsInt(), 
				JSONUtils.getMandatory(json, "secondnear").getAsInt(), 
				JSONUtils.getMandatory(json, "secondfar").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "RiderBent";
	}

}
