package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeKnight extends MoveTypeLeaper {
	
	public MoveTypeKnight(MoveMode mode)
	{
		super(mode, 1,2);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeKnight(mode);
	}

	@Override
	public String getTypeName() {
		return "Knight";
	}

}
