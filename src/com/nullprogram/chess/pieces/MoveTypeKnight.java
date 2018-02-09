package com.nullprogram.chess.pieces;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;

public class MoveTypeKnight extends MoveTypeLeaper {
	
	public MoveTypeKnight()
	{
		super(1,2);
	}

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new MoveTypeKnight();
	}

}
