package com.nullprogram.chess.pieces;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeFerz extends MoveTypeLeaperDiagonal {
	
	public MoveTypeFerz(MoveMode mode)
	{
		super(mode, 1);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeFerz(mode);
	}

}
