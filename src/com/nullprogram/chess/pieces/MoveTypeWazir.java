package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeWazir extends MoveTypeLeaperOrthogonal {
	
	public MoveTypeWazir(MoveMode mode)
	{
		super(mode, 1);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeWazir(mode);
	}

}
