package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeFerz extends MoveTypeLeaperDiagonal {
	
	public MoveTypeFerz(MoveMode mode, DirectionMode directionMode)
	{
		super(mode, directionMode, 1);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeFerz(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Ferz";
	}

}
