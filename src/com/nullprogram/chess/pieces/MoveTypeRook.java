package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.Position;

public class MoveTypeRook extends MoveTypeRiderAbstract {
	public MoveTypeRook(MoveMode mode, DirectionMode directionMode, int max) {
		super(mode, directionMode, max);
	}

	@Override
	protected Position[] getDirections() {
		return Direction.ORTHOGONAL_POS;
	}

	@Override
	protected MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, int max,
			JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRook(moveMode, directionMode, max);
	}

	@Override
	public String getTypeName() {
		return "Rook";
	}

}
