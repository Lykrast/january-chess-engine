package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeWazir extends MoveTypeLeaperOrthogonal {
	
	public MoveTypeWazir(MoveMode mode, DirectionMode directionMode)
	{
		super(mode, directionMode, 1);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, DirectionMode directionMode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeWazir(mode, directionMode);
	}

	@Override
	public String getTypeName() {
		return "Wazir";
	}

}
