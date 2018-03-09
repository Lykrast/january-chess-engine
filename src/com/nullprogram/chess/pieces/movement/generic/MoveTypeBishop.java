package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.movement.MoveType;

public class MoveTypeBishop extends MoveTypeRiderAbstract {
	public MoveTypeBishop(MoveMode mode, DirectionMode directionMode, int max) {
		super(mode, directionMode, max);
	}

	@Override
	protected Position[] getDirections() {
		return Direction.DIAGONAL_POS;
	}

	@Override
	protected MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, int max,
			JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeBishop(moveMode, directionMode, max);
	}

	@Override
	public String getTypeName() {
		return "Bishop";
	}

}
