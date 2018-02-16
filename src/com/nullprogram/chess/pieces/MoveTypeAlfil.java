package com.nullprogram.chess.pieces;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeAlfil extends MoveTypeLeaperDiagonal {
	
	public MoveTypeAlfil(MoveMode mode)
	{
		super(mode, 2);
	}
	
	@Override
	public MoveType create(JsonObject json, MoveMode mode) throws JsonParseException {
		return new MoveTypeAlfil(mode);
	}

}
