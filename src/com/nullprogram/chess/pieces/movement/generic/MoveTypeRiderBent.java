package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.Piece;
import com.nullprogram.chess.pieces.movement.IMoveList;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeRiderBent extends MoveType {
	public static final int RIDE_CONTINUE = 0, RIDE_ALTERNATE = 1;
	
	/**
	 * An array of all 8 directions the rider can move.
	 */
	private final Position[][] directions;
	private int rideMode;

	public MoveTypeRiderBent(MoveMode mode, DirectionMode directionMode, int rideMode, int firstnear, int firstfar, int secondnear, int secondfar) {
		super(mode, directionMode);
		this.rideMode = rideMode;
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
        	int next = 1;
        	while (list.add(new Move(start, pos), getMoveMode()) && p.getBoard().isFree(pos))
        	{
        		pos = pos.offset(dir[next]);
        		if (rideMode == RIDE_ALTERNATE) next = next == 0 ? 1 : 0;
        	}
        }
        
        return list;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		int ride = RIDE_CONTINUE;
		JsonElement jRide = json.get("ridemode");
		if (jRide != null)
		{
			String sRide = jRide.getAsString();
			if (sRide.equalsIgnoreCase("continue")) ride = RIDE_CONTINUE;
			else if (sRide.equalsIgnoreCase("alternate")) ride = RIDE_ALTERNATE;
			else throw new JsonParseException("Invalid riding mode value : " + sRide + " - must be continue or alternate");
		}
		
		return new MoveTypeRiderBent(mode, directionMode, ride, 
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
