package com.nullprogram.chess.resources;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nullprogram.chess.GameMode;
import com.nullprogram.chess.boards.PiecePlacement;

public class GameModeDeserializer implements JsonDeserializer<GameMode> {
	public static final GameModeDeserializer INSTANCE = new GameModeDeserializer();
	private GameModeDeserializer() {}

	@Override
	public GameMode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		JsonArray piecesJson = obj.get("pieces").getAsJsonArray();
		List<PiecePlacement> piecesList = new ArrayList<>();
		for (JsonElement elem : piecesJson)
		{
			piecesList.add(PiecePlacementDeserializer.INSTANCE.deserialize(elem, typeOfT, context));
		}
		
		return new GameMode(obj.get("name").getAsString(),
				obj.get("width").getAsInt(), 
				obj.get("height").getAsInt(), 
				piecesList.toArray(new PiecePlacement[piecesList.size()]));
	}

}
