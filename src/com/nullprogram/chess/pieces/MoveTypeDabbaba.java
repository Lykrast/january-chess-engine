package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeDabbaba extends MoveTypeLeaperOrthogonal {
	
	public MoveTypeDabbaba(MoveMode mode)
	{
		super(mode, 2);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeDabbaba(mode);
	}

}
