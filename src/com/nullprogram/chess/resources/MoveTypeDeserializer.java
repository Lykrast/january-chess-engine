package com.nullprogram.chess.resources;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.MoveType;
import com.nullprogram.chess.pieces.MoveTypeBishop;
import com.nullprogram.chess.pieces.MoveTypeKing;
import com.nullprogram.chess.pieces.MoveTypeKnight;
import com.nullprogram.chess.pieces.MoveTypePawn;
import com.nullprogram.chess.pieces.MoveTypeRook;

public class MoveTypeDeserializer implements JsonDeserializer<MoveType> {
	public static final MoveTypeDeserializer INSTANCE = new MoveTypeDeserializer();
	private MoveTypeDeserializer() {}

	@Override
	public MoveType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		String name = obj.get("type").getAsString();
		switch (name)
		{
		case "Pawn":
			return new MoveTypePawn();
		case "Rook":
			return new MoveTypeRook();
		case "Knight":
			return new MoveTypeKnight();
		case "Bishop":
			return new MoveTypeBishop();
		case "King":
			return new MoveTypeKing();
		}
		return null;
	}

}
