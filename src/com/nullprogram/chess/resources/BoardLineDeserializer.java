package com.nullprogram.chess.resources;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.boards.BoardLine;

public class BoardLineDeserializer implements JsonDeserializer<BoardLine> {
	public static final BoardLineDeserializer INSTANCE = new BoardLineDeserializer();

	private BoardLineDeserializer() {
	}

	@Override
	public BoardLine deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray arr = json.getAsJsonArray();
		if (arr.size() < 4) throw new JsonParseException("Cosmetic line too short, need exactly 4 numbers.");
		if (arr.size() > 4) throw new JsonParseException("Cosmetic line too long, need exactly 4 numbers.");
		
		return new BoardLine(arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt(), arr.get(3).getAsInt());
	}

}
