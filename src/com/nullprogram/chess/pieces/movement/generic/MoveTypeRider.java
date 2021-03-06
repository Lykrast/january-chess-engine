package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.resources.JSONUtils;

public class MoveTypeRider extends MoveTypeRiderAbstract {
	/**
	 * An array of all 8 directions the rider can move.
	 */
	private final Position[] directions;

	public MoveTypeRider(MoveMode mode, DirectionMode directionMode, int max, int near, int far) {
		super(mode, directionMode, max);
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
	protected Position[] getDirections() {
		return directions;
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, int max, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRider(mode, directionMode, max, JSONUtils.getMandatory(json, "near").getAsInt(), JSONUtils.getMandatory(json, "far").getAsInt());
	}

	@Override
	public String getTypeName() {
		return "Rider";
	}

}
