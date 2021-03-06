package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.Direction;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.pieces.movement.MoveType;
import com.nullprogram.chess.util.Wrap;

public class MoveTypeRookCylinder extends MoveTypeRiderCylinderAbstract {
	public MoveTypeRookCylinder(MoveMode mode, DirectionMode directionMode, int max, Wrap wrap) {
		super(mode, directionMode, max, wrap);
	}

	@Override
	protected Position[] getDirections() {
		return Direction.ORTHOGONAL_POS;
	}

	@Override
	protected MoveType create(JsonObject json, MoveMode moveMode, DirectionMode directionMode, int max, Wrap wrap, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeRookCylinder(moveMode, directionMode, max, wrap);
	}

	@Override
	public String getTypeName() {
		return "RookCylinder";
	}

}
