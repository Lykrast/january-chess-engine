package com.nullprogram.chess.pieces.movement.generic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.pieces.movement.MoveType;

public class MoveTypeAlfil extends MoveTypeLeaperDiagonal {
	
	public MoveTypeAlfil(MoveMode mode, DirectionMode directionMode)
	{
		super(mode, directionMode, 2);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeAlfil(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Alfil";
	}

}
